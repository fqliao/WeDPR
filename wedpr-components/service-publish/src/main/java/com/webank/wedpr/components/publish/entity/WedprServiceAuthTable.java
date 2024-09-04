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
@TableName("wedpr_service_auth_table")
@ApiModel(value = "WedprServiceAuthTable对象", description = "")
public class WedprServiceAuthTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "服务ID")
    private String serviceId;

    @ApiModelProperty(value = "访问ID(用于标志用户)")
    private String accessKeyId;

    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applyTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime lastUpdateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public LocalDateTime getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(LocalDateTime applyTime) {
        this.applyTime = applyTime;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "WedprServiceAuthTable{"
                + "id="
                + id
                + ", serviceId="
                + serviceId
                + ", accessKeyId="
                + accessKeyId
                + ", expireTime="
                + expireTime
                + ", applyTime="
                + applyTime
                + ", lastUpdateTime="
                + lastUpdateTime
                + "}";
    }
}
