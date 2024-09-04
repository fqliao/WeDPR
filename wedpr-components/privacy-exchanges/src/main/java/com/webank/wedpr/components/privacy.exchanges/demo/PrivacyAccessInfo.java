package com.webank.wedpr.components.privacy.exchanges.demo;

import com.webank.wedpr.components.http.client.model.BaseRequest;
import com.webank.wedpr.core.utils.ObjectMapperFactory;

/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/29 10:12
 *
 * @description:接入信息
 */
public class PrivacyAccessInfo implements BaseRequest {

    private String appId; // 账号
    private String appKey; // 密码
    private String openApiHost; // 系统分配的Host

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getOpenApiHost() {
        return openApiHost;
    }

    public void setOpenApiHost(String openApiHost) {
        this.openApiHost = openApiHost;
    }

    @Override
    public String serialize() throws Exception {
        return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
    }
}
