/** Copyright (C) @2014-2022 Webank */
package com.webank.wedpr.components.report.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webank.wedpr.common.config.WeDPRCommonConfig;
import com.webank.wedpr.common.protocol.ReportStatusEnum;
import com.webank.wedpr.common.protocol.TransportComponentEnum;
import com.webank.wedpr.common.protocol.TransportTopicEnum;
import com.webank.wedpr.common.utils.Constant;
import com.webank.wedpr.common.utils.ObjectMapperFactory;
import com.webank.wedpr.components.meta.sys.config.dao.SysConfigDO;
import com.webank.wedpr.components.meta.sys.config.dao.SysConfigMapper;
import com.webank.wedpr.components.project.dao.JobDO;
import com.webank.wedpr.components.project.dao.JobDatasetDO;
import com.webank.wedpr.components.project.dao.ProjectDO;
import com.webank.wedpr.components.project.dao.ProjectMapper;
import com.webank.wedpr.components.report.handler.*;
import com.webank.wedpr.components.transport.CommonErrorCallback;
import com.webank.wedpr.sdk.jni.transport.WeDPRTransport;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DisallowConcurrentExecution
@Slf4j
public class ReportQuartzJob implements Job {
    @Autowired private ProjectMapper projectMapper;
    @Autowired private SysConfigMapper sysConfigMapper;

    @Autowired private WeDPRTransport weDPRTransport;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("ReportQuartzJob run at:{}", LocalDateTime.now());
        try {
            doReport();
        } catch (Throwable e) {
            log.warn("ReportQuartzJob run error", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void doReport() {
        log.debug("do report...");
        try {
            String adminAgency = WeDPRCommonConfig.getAgency();
            reportProjectInfo(adminAgency);
            reportJobInfo(adminAgency);
            reportJobDatasteRelationInfo(adminAgency);
        } catch (Exception e) {
            log.warn("report error", e);
        }
    }

    private void reportSysConfig(String agency) throws JsonProcessingException {
        SysConfigReportMessageHandler sysConfigReportMessageHandler =
                new SysConfigReportMessageHandler(sysConfigMapper);
        List<SysConfigDO> sysConfigDOList = sysConfigMapper.queryAllConfig();
        if (sysConfigDOList.isEmpty()) {
            return;
        }
        log.info("report sysConfigDOList:{}", sysConfigDOList);
        byte[] payload = ObjectMapperFactory.getObjectMapper().writeValueAsBytes(sysConfigDOList);
        weDPRTransport.asyncSendMessageByComponent(
                TransportTopicEnum.SYS_CONFIG_REPORT.name(),
                agency,
                TransportComponentEnum.REPORT.name(),
                payload,
                0,
                WeDPRCommonConfig.getReportTimeoutMs(),
                new CommonErrorCallback("reportSysConfig"),
                sysConfigReportMessageHandler);
    }

    private void reportJobDatasteRelationInfo(String agency) throws JsonProcessingException {
        JobDatasetRelationReportMessageHandler jobDatasetRelationReportMessageHandler =
                new JobDatasetRelationReportMessageHandler(projectMapper);
        JobDatasetDO jobDatasetDO = new JobDatasetDO();
        jobDatasetDO.setReportStatus(ReportStatusEnum.NO_REPORT.getReportStatus());
        jobDatasetDO.setLimitItems(Constant.DEFAULT_REPORT_PAGE_SIZE);
        List<JobDatasetDO> jobDatasetDOList = projectMapper.queryJobDatasetInfo(jobDatasetDO);
        if (jobDatasetDOList.isEmpty()) {
            return;
        }
        log.info("report jobDatasetDOList:{}", jobDatasetDOList);
        byte[] payload = ObjectMapperFactory.getObjectMapper().writeValueAsBytes(jobDatasetDOList);
        weDPRTransport.asyncSendMessageByComponent(
                TransportTopicEnum.JOB_DATASET_REPORT.name(),
                agency,
                TransportComponentEnum.REPORT.name(),
                payload,
                0,
                WeDPRCommonConfig.getReportTimeoutMs(),
                new CommonErrorCallback("reportSysConfig"),
                jobDatasetRelationReportMessageHandler);
    }

    private void reportJobInfo(String agency) throws JsonProcessingException {
        JobReportMessageHandler jobReportMessageHandler =
                new JobReportMessageHandler(projectMapper);
        JobDO jobDO = new JobDO();
        jobDO.setId(null);
        jobDO.setReportStatus(ReportStatusEnum.NO_REPORT.getReportStatus());
        jobDO.setLimitItems(Constant.DEFAULT_REPORT_PAGE_SIZE);
        List<JobDO> jobDOList = projectMapper.queryJobs(false, jobDO, null);
        if (jobDOList.isEmpty()) {
            return;
        }
        log.info("report jobDOList:{}", jobDOList);
        byte[] payload = ObjectMapperFactory.getObjectMapper().writeValueAsBytes(jobDOList);
        weDPRTransport.asyncSendMessageByComponent(
                TransportTopicEnum.JOB_REPORT.name(),
                agency,
                TransportComponentEnum.REPORT.name(),
                payload,
                0,
                WeDPRCommonConfig.getReportTimeoutMs(),
                new CommonErrorCallback("reportJobInfo"),
                jobReportMessageHandler);
    }

    private void reportProjectInfo(String agency) throws JsonProcessingException {
        ProjectReportMessageHandler projectReportMessageHandler =
                new ProjectReportMessageHandler(projectMapper);
        ProjectDO projectDO = new ProjectDO();
        projectDO.setId(null);
        projectDO.setReportStatus(ReportStatusEnum.NO_REPORT.getReportStatus());
        projectDO.setLimitItems(Constant.DEFAULT_REPORT_PAGE_SIZE);
        List<ProjectDO> projectDOList = projectMapper.queryProject(false, projectDO);
        if (projectDOList.isEmpty()) {
            return;
        }
        log.info("report projectDOList:{}", projectDOList);
        byte[] payload = ObjectMapperFactory.getObjectMapper().writeValueAsBytes(projectDOList);
        weDPRTransport.asyncSendMessageByComponent(
                TransportTopicEnum.PROJECT_REPORT.name(),
                agency,
                TransportComponentEnum.REPORT.name(),
                payload,
                0,
                WeDPRCommonConfig.getReportTimeoutMs(),
                new CommonErrorCallback("reportProjectInfo"),
                projectReportMessageHandler);
    }
}
