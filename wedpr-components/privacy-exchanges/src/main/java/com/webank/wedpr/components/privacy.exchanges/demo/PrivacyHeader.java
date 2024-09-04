package com.webank.wedpr.components.privacy.exchanges.demo;

/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/28 16:23
 *
 * @description: privacy-ezchanges接口对接请求主类
 */
public class PrivacyHeader {

    private static volatile PrivacyHeader instance;

    private String token; // 请求header

    public String getHeader() {
        return token;
    }

    public void setHeader(String header) {
        this.token = token;
    }

    private PrivacyHeader(String token) {
        this.token = token;
    }

    public static void setInstanceToken(String token) {
        if (instance == null) {
            synchronized (PrivacyHeader.class) {
                if (instance == null) {
                    instance = new PrivacyHeader(token);
                }
            }
        }
    }

    public static PrivacyHeader getInstance() {
        return instance;
    }
}
