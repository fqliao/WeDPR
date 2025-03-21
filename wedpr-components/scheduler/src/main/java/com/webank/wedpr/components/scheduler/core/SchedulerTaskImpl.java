/*
 * Copyright 2017-2025  [webank-wedpr]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.webank.wedpr.components.scheduler.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webank.wedpr.common.config.WeDPRCommonConfig;
import com.webank.wedpr.common.protocol.JobStatus;
import com.webank.wedpr.common.protocol.JobType;
import com.webank.wedpr.common.protocol.ServiceName;
import com.webank.wedpr.common.utils.Constant;
import com.webank.wedpr.components.loadbalancer.LoadBalancer;
import com.webank.wedpr.components.project.dao.JobDO;
import com.webank.wedpr.components.project.dao.ProjectMapperWrapper;
import com.webank.wedpr.components.project.model.BatchJobList;
import com.webank.wedpr.components.scheduler.api.SchedulerApi;
import com.webank.wedpr.components.scheduler.config.SchedulerTaskConfig;
import com.webank.wedpr.components.sync.ResourceSyncer;
import com.webank.wedpr.sdk.jni.transport.model.ServiceMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerTaskImpl {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerTaskImpl.class);

    private final ProjectMapperWrapper projectMapperWrapper;
    private final SchedulerApi scheduler;
    private final JobSyncer jobSyncer;
    private final LoadBalancer loadBalancer;

    private final ScheduledExecutorService workerTimer = new ScheduledThreadPoolExecutor(1);

    public SchedulerTaskImpl(
            ProjectMapperWrapper projectMapperWrapper,
            ResourceSyncer resourceSyncer,
            SchedulerApi scheduler,
            LoadBalancer loadBalancer) {
        this.jobSyncer =
                new JobSyncer(
                        WeDPRCommonConfig.getAgency(),
                        ResourceSyncer.ResourceType.Job.getType(),
                        resourceSyncer,
                        scheduler,
                        projectMapperWrapper);
        this.projectMapperWrapper = projectMapperWrapper;
        this.scheduler = scheduler;
        this.loadBalancer = loadBalancer;
    }

    public void start() {
        logger.info("start schedulerTask");
        workerTimer.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            schedule();
                        } catch (Exception e) {
                            logger.warn("SchedulerTask: scheduler error: ", e);
                        }
                    }
                },
                0,
                SchedulerTaskConfig.getSchedulerIntervalMs(),
                TimeUnit.MILLISECONDS);
        logger.info("start schedulerTask success");
    }

    public SchedulerApi getScheduler() {
        return this.scheduler;
    }

    protected void schedule() {
        try {
            killTasks();
            schedulerAllTypeTasks();
        } catch (Exception e) {
            logger.warn("schedule exception, error: ", e);
        }
    }

    protected void killTasks() throws JsonProcessingException {
        JobDO condition = new JobDO(true);
        condition.setStatus(JobStatus.WaitToKill.getStatus());
        Set<JobDO> waitToKillJobs =
                this.projectMapperWrapper.queryJobMetasByStatus(
                        null, null, JobStatus.WaitToKill.getStatus(), null);
        if (waitToKillJobs == null || waitToKillJobs.isEmpty()) {
            return;
        }
        List<JobDO> jobDOList = new ArrayList<>(waitToKillJobs);
        // in case of been killed more than once
        this.projectMapperWrapper.batchUpdateJobStatus(
                null, null, jobDOList, JobStatus.ChainInProgress);
        logger.info(
                "Ready to kill tasks, size: {}, detail: {}",
                waitToKillJobs.size(),
                ArrayUtils.toString(waitToKillJobs));
        BatchJobList jobs = new BatchJobList(waitToKillJobs);
        jobs.resetStatus(JobStatus.Killing);
        this.jobSyncer.sync(Constant.SYS_USER, JobSyncer.JobAction.KillAction, jobs.serialize());
    }

    protected void schedulerAllTypeTasks() throws Exception {
        for (JobType jobType : JobType.values()) {
            ServiceName serviceType = jobType.getServiceName();
            List<ServiceMeta.EntryPointMeta> serviceInfos =
                    loadBalancer.selectAllEndPoint(serviceType.getValue());
            int concurrency = 0;
            if (serviceInfos != null) {
                concurrency = serviceInfos.size() * SchedulerTaskConfig.getJobConcurrency();
            }
            scheduleTasksToRun(concurrency, jobType);
        }
    }

    protected void scheduleTasksToRun(int concurrency, JobType jobType) throws Exception {
        // get the running task number
        Set<JobDO> runningJobs =
                this.projectMapperWrapper.queryJobMetasByStatus(
                        null,
                        WeDPRCommonConfig.getAgency(),
                        JobStatus.Running.getStatus(),
                        jobType);
        Integer runningJobSize = runningJobs == null ? 0 : runningJobs.size();
        if (runningJobs.size() > 0 && runningJobs.size() >= concurrency) {
            logger.info(
                    "scheduleTasksToRun: schedule nothing for the running tasks over than concurrency, runningJobs: {}, concurrency: {}",
                    runningJobs.size(),
                    concurrency);
            return;
        }
        if (runningJobSize > 0 && concurrency == 0) {
            logger.warn(
                    "All executors that can execute type {} tasks are abnormal! runningJobSize: {}",
                    jobType.getType(),
                    runningJobSize);
            return;
        }
        if (concurrency > 0 && concurrency <= runningJobSize) {
            return;
        }
        // at least fetch one job even if concurrency is 0
        int fetchedJobs = concurrency > runningJobSize ? (concurrency - runningJobSize) : 1;
        // query the submitted tasks
        JobDO condition = new JobDO(true);
        condition.setStatus(JobStatus.Submitted.getStatus());
        condition.setJobType(jobType.getType());
        condition.setLimitItems(fetchedJobs);
        Set<JobDO> jobsToRun =
                this.projectMapperWrapper.queryJobsByCondition(false, null, null, condition);
        if (jobsToRun == null || jobsToRun.isEmpty()) {
            // query the waitToRetry in the case of without submitted tasks
            condition.setStatus(JobStatus.WaitToRetry.getStatus());
            jobsToRun =
                    this.projectMapperWrapper.queryJobsByCondition(false, null, null, condition);
        } else if (jobsToRun.size() < condition.getLimitItems()) {
            // query the waitToRetry in the case of un-enough submit tasks
            condition.setStatus(JobStatus.WaitToRetry.getStatus());
            condition.setLimitItems(concurrency - runningJobSize - jobsToRun.size());
            jobsToRun.addAll(
                    this.projectMapperWrapper.queryJobsByCondition(false, null, null, condition));
        }
        if (jobsToRun == null || jobsToRun.isEmpty()) {
            return;
        }
        if (concurrency == 0 && jobsToRun.size() > 0) {
            logger.warn(
                    "All executors that can execute type {} tasks are abnormal", jobType.getType());
            return;
        }
        // in case of been scheduled more than once
        List<JobDO> jobsToSync = new ArrayList<>();
        List<JobDO> jobsToExecute = new ArrayList<>();
        for (JobDO job : jobsToRun) {
            if (job.getShouldSync()) {
                jobsToSync.add(job);
            } else {
                jobsToExecute.add(job);
            }
        }
        logger.info(
                "scheduleTasksToRun, syncJobs: {}, jobsToRun: {}, jobsToSync: {}, jobsToExecute: {}, jobType: {}",
                runningJobSize,
                jobsToRun.size(),
                jobsToSync.size(),
                jobsToExecute.size(),
                jobType.getType());
        syncJobs(jobsToSync);
        executeJobs(jobsToExecute);
    }

    protected void executeJobs(List<JobDO> jobList) {
        if (jobList == null || jobList.isEmpty()) {
            return;
        }
        // execute the jobs
        this.scheduler.batchRunJobs(jobList);
    }

    protected void syncJobs(List<JobDO> jobList) throws Exception {
        // No jobs to sync
        if (jobList == null || jobList.isEmpty()) {
            return;
        }
        this.projectMapperWrapper.batchUpdateJobStatus(
                null, null, jobList, JobStatus.ChainInProgress);

        BatchJobList jobs = new BatchJobList(jobList);
        jobs.resetStatus(JobStatus.Running);
        this.jobSyncer.sync(Constant.SYS_USER, JobSyncer.JobAction.RunAction, jobs.serialize());
    }
}
