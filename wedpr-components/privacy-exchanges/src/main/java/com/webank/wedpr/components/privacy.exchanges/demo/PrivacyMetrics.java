package com.webank.wedpr.components.privacy.exchanges.demo;

import com.webank.wedpr.components.http.client.model.BaseRequest;
import com.webank.wedpr.core.utils.ObjectMapperFactory;

/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/29 10:06
 *
 * @description:
 */
public class PrivacyMetrics implements BaseRequest {

    private String requestId; // 请求ID 流水号
    private String projectId; // 交付项目ID
    private String taskName; // 交付任务名称
    private String taskId; // 交付任务ID
    private String invokeType; // 交付任务类型
    private String status; // 任务状态 success canceled failed timeout
    private String failReason; // 失败原因
    private String queryCount; // 计算查询所消耗资产行数
    private String queryResultCount; // 命中或者交中数
    private String invokeTime; // 执行时间
    private String duration; // 执行耗时 单位ms

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getInvokeType() {
        return invokeType;
    }

    public void setInvokeType(String invokeType) {
        this.invokeType = invokeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(String queryCount) {
        this.queryCount = queryCount;
    }

    public String getQueryResultCount() {
        return queryResultCount;
    }

    public void setQueryResultCount(String queryResultCount) {
        this.queryResultCount = queryResultCount;
    }

    public String getInvokeTime() {
        return invokeTime;
    }

    public void setInvokeTime(String invokeTime) {
        this.invokeTime = invokeTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String serialize() throws Exception {
        return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
    }
}
