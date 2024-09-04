package com.webank.wedpr.components.privacy.exchanges.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.webank.wedpr.components.http.client.model.BaseResponse;
import com.webank.wedpr.core.utils.Constant;
import com.webank.wedpr.core.utils.ObjectMapperFactory;

/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/28 16:23
 *
 * @description: privacy-ezchanges接口对接返回类
 */
public class PrivacyResponse implements BaseResponse {

    private String requestld; // 请求ID
    private String code; // 请求状态码 200代表成功
    private String message; // 提示信息
    private String data; // 内容
    private Boolean success; // true代表成功

    @Override
    public Boolean statusOk() {
        return code.equals(String.valueOf(Constant.HTTP_SUCCESS));
    }

    @Override
    public String serialize() throws Exception {
        return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
    }

    public String getRequestld() {
        return requestld;
    }

    public void setRequestld(String requestld) {
        this.requestld = requestld;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public static PrivacyResponse deserialize(String data) throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().readValue(data, PrivacyResponse.class);
    }
}
