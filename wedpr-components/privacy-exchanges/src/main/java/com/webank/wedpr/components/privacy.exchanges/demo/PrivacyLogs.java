package com.webank.wedpr.components.privacy.exchanges.demo;

import com.webank.wedpr.components.http.client.model.BaseRequest;
import com.webank.wedpr.core.utils.ObjectMapperFactory;
import java.util.List;

/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/29 09:59
 *
 * @description:
 */
public class PrivacyLogs implements BaseRequest {

    private String requestId; // 请求ID 流水号
    private String projectId; // 交付项目ID
    private String taskName; // 任务名称
    private String taskId; // 任务ID
    private List<Log> logs; // 日志列表

    @Override
    public String serialize() throws Exception {
        return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
    }

    public class Log {

        private String timestamp; // 日志时间戳
        private String level; // 日志级别
        private String message; // 日志详情
        private List<String> tags; // 标签信息 用于给日志做分类使用

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }
}
