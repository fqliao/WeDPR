package com.webank.wedpr.components.publish.entity.request;

import lombok.Data;

/** @author zachma */
@Data
public class PublishCreateRequest {
    private String serviceId;

    private String serviceName;

    private String serviceDesc;

    private String serviceType;

    private String serviceConfig;
}
