package com.webank.wedpr.components.publish.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author caryliao
 * @since 2024-08-31
 */
@TableName("wedpr_service_invoke_table")
@ApiModel(value = "WedprServiceInvokeTable对象", description = "")
public class WedprServiceInvokeTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "调用ID")
    private String invokeId;

    @ApiModelProperty(value = "调用的服务ID")
    private String serviceId;

    @ApiModelProperty(value = "调用者")
    private String invokeUser;

    @ApiModelProperty(value = "调用方所属机构")
    private String invokeAgency;

    @ApiModelProperty(value = "调用服务状态")
    private String invokeStatus;

    @ApiModelProperty(value = "调用时间")
    private LocalDateTime invokeTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime lastUpdateTime;

    public String getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(String invokeId) {
        this.invokeId = invokeId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getInvokeUser() {
        return invokeUser;
    }

    public void setInvokeUser(String invokeUser) {
        this.invokeUser = invokeUser;
    }

    public String getInvokeAgency() {
        return invokeAgency;
    }

    public void setInvokeAgency(String invokeAgency) {
        this.invokeAgency = invokeAgency;
    }

    public LocalDateTime getInvokeTime() {
        return invokeTime;
    }

    public void setInvokeTime(LocalDateTime invokeTime) {
        this.invokeTime = invokeTime;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getInvokeStatus() {
        return invokeStatus;
    }

    public void setInvokeStatus(String invokeStatus) {
        this.invokeStatus = invokeStatus;
    }

    @Override
    public String toString() {
        return "WedprServiceInvokeTable{"
                + "invokeId="
                + invokeId
                + ", serviceId="
                + serviceId
                + ", invokeUser="
                + invokeUser
                + ", invokeAgency="
                + invokeAgency
                + ", invokeStatus="
                + invokeStatus
                + ", invokeTime="
                + invokeTime
                + ", lastUpdateTime="
                + lastUpdateTime
                + "}";
    }
}
