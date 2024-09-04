package com.webank.wedpr.components.privacy.exchanges.demo;

import com.webank.wedpr.components.http.client.model.BaseRequest;
import com.webank.wedpr.core.utils.ObjectMapperFactory;

/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/28 19:31
 *
 * @description:生成调用凭证
 */
public class PrivacyToken implements BaseRequest {

    private String appId;
    private String appSecret;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Override
    public String serialize() throws Exception {
        return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
    }
}
