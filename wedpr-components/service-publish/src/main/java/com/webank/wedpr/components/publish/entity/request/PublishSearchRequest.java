package com.webank.wedpr.components.publish.entity.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zachma
 * @date 2024/8/31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PublishSearchRequest extends BasePageRequest {

    private String serviceName;

    private String agency;

    private String serviceType;

    private String owner;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String createDate;
}
