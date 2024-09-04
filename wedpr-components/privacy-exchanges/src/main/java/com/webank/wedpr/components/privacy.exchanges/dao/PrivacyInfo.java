package com.webank.wedpr.components.privacy.exchanges.dao;

import org.springframework.stereotype.Component;

/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/29 15:57
 *
 * @description:
 */
@Component
public class PrivacyInfo {

    public static final String APP_ID = "appId";
    public static final String APP_KEY = "appKey";

    // 生成调用凭证
    public static final String TOKEN = "/v1/api/open/token";
    // 接收上报的组网信息
    public static final String NETWORK = "/v1/api/node/networking/report";
    // 接收数据服务权限开通信息
    public static final String AUTHORIZE_REPORT = "/v1/api/product/authorize/report";
    // 接收数据服务取消权限开通信息
    public static final String AUTHORIZE_REVOKED = "/v1/api/product/authorize/revoked";
    // 数据服务能否使用核验
    public static final String USAGE = "/v1/api/project/usage/check";
    // 任务运行日志上报
    public static final String LOGS = "/v1/api/task/logs/report";
    // 计量信息上报
    public static final String METRICS = "/v1/api/metrics/report";
    // 接收管理系统分配的接入信息
    public static final String SYSTEM_INFO = "/v1/api/system-access-info";
    // 节点连通性检测
    public static final String PING_PONG = "/v1/api/pong";
    // 接收交付项目信息
    public static final String PROJECT_SYNC = "/v1/api/project/sync";
    // 终止交付项目
    public static final String PROJECT_TERMINATION = "/v1/api/project/termination";
}
