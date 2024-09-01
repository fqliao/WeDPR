package com.webank.wedpr.components.publish.entity.response;

import com.webank.wedpr.components.publish.entity.WedprPublishedService;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zachma
 * @date 2024/8/31
 */
@Data
@AllArgsConstructor
public class WedprPublishSearchReturn {
    private WedprPublishedService wedprPublishedService;
}
