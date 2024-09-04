package com.webank.wedpr.components.privacy.exchanges.demo;

import com.webank.wedpr.components.http.client.model.BaseRequest;
import com.webank.wedpr.core.utils.ObjectMapperFactory;

/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/28 19:51
 *
 * @description:
 */
public class PrivacyAuthorize implements BaseRequest {

    private String projectId; // 交付项目ID
    private String productId; // 开通权限数据产品ID
    private String productName; // 开通权限数据产品名称
    private String serviceIds; // 开通权限的服务列表
    private String serviceType; // 服务类型
    private String authorizePartnerCode; // 被授权机构
    private String expirationDateFrom; // 授权开始时间
    private String expirationDateTo; // 授权结束时间
    private String operationTime; // 权限开通时间
    private String status; // 权限开通是否成功
    private String failReason; // 权限开通失败原因

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(String serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getAuthorizePartnerCode() {
        return authorizePartnerCode;
    }

    public void setAuthorizePartnerCode(String authorizePartnerCode) {
        this.authorizePartnerCode = authorizePartnerCode;
    }

    public String getExpirationDateFrom() {
        return expirationDateFrom;
    }

    public void setExpirationDateFrom(String expirationDateFrom) {
        this.expirationDateFrom = expirationDateFrom;
    }

    public String getExpirationDateTo() {
        return expirationDateTo;
    }

    public void setExpirationDateTo(String expirationDateTo) {
        this.expirationDateTo = expirationDateTo;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
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

    @Override
    public String serialize() throws Exception {
        return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
    }
}
