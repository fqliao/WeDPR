package com.webank.wedpr.components.privacy.exchanges.demo;

import com.webank.wedpr.components.http.client.model.BaseRequest;
import com.webank.wedpr.core.utils.ObjectMapperFactory;

/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/28 19:37
 *
 * @description: 将组网的结果同步给交付管理系统
 */
public class PrivacyNetworking implements BaseRequest {

    private Node sourceNode; // 起始节点
    private Node targetNode; // 目标节点
    private String operationTime; // 节点组网时间
    private String status; // 组网状态
    private String failReason; // 失败原因

    public Node getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(Node sourceNode) {
        this.sourceNode = sourceNode;
    }

    public Node getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(Node targetNode) {
        this.targetNode = targetNode;
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



    public class Node {

        private String nodeId; // 节点编码
        private String partnerCode; // 机构编码
        private String partnerName; // 机构名称

        public String getNodeId() {
            return nodeId;
        }

        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }

        public String getPartnerCode() {
            return partnerCode;
        }

        public void setPartnerCode(String partnerCode) {
            this.partnerCode = partnerCode;
        }

        public String getPartnerName() {
            return partnerName;
        }

        public void setPartnerName(String partnerName) {
            this.partnerName = partnerName;
        }
    }
}
