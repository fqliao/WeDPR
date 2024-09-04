package com.webank.wedpr.components.privacy.exchanges.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/28 18:19
 *
 * @description: 返回码
 */
@AllArgsConstructor
@Getter
public enum PrivacyEunm {
    FAILED("500", "服务内部错误"),
    NOT_FOUND("404", "请求资源不存在"),
    SUCCESS("200", "操作成功"),
    Auth_FAIL("401", "认证信息校验失败"),
    Param_CHECK_FAIL("522", "参数校验失败");

    private final String code;
    private final String message;
}
