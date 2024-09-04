package com.webank.wedpr.components.publish.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.webank.wedpr.components.publish.helper.JsonHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.SneakyThrows;

/**
 * @author caryliao
 * @since 2024-08-31
 */
@TableName("wedpr_published_service")
@ApiModel(value = "WedprPublishedService对象", description = "")
public class WedprPublishedService implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "发布的服务ID")
    private String serviceId;

    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    @ApiModelProperty(value = "服务描述")
    private String serviceDesc;

    @ApiModelProperty(value = "发布的服务类型, 如pir/lr/xgb")
    private String serviceType;

    @ApiModelProperty(value = "服务配置")
    private String serviceConfig;

    @ApiModelProperty(value = "属主")
    private String owner;

    @ApiModelProperty(value = "所属机构")
    private String agency;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime lastUpdateTime;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(String serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @SneakyThrows(Exception.class)
    public String serialize() {
        return JsonHelper.object2jsonString(this);
    }

    @Override
    public String toString() {
        return "WedprPublishedService{"
                + "serviceId="
                + serviceId
                + ", serviceName="
                + serviceName
                + ", serviceDesc="
                + serviceDesc
                + ", serviceType="
                + serviceType
                + ", serviceConfig="
                + serviceConfig
                + ", owner="
                + owner
                + ", agency="
                + agency
                + ", createTime="
                + createTime
                + ", lastUpdateTime="
                + lastUpdateTime
                + "}";
    }
}
