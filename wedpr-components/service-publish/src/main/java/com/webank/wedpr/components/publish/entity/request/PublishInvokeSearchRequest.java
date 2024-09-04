package com.webank.wedpr.components.publish.entity.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zachma
 * @date 2024/8/31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PublishInvokeSearchRequest extends BasePageRequest {
    private String invokeAgency;

    private String expireDate;

    private String invokeDate;

    private String invokeStatus;
}
