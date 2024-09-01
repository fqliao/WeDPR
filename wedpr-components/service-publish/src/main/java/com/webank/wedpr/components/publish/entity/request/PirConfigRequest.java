package com.webank.wedpr.components.publish.entity.request;

import lombok.Data;

/**
 * @author zachma
 * @date 2024/8/31
 */
@Data
public class PirConfigRequest {
    String datasetId;

    String[] exists;

    String[] values;
}
