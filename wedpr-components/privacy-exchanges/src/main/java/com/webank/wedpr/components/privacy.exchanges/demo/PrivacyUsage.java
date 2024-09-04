package com.webank.wedpr.components.privacy.exchanges.demo;

import com.webank.wedpr.components.http.client.model.BaseRequest;
import com.webank.wedpr.core.utils.ObjectMapperFactory;

/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/29 09:58
 *
 * @description:
 */
public class PrivacyUsage implements BaseRequest {

    private String projectId; // 交付项目ID

    private String productId; // 数据产品ID

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

    @Override
    public String serialize() throws Exception {
        return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
    }
}
