package com.webank.wedpr.components.publish.entity.response;

import com.webank.wedpr.components.publish.entity.result.WedprServiceInvokeResult;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zachma
 * @date 2024/8/31
 */
@Data
@AllArgsConstructor
public class WedprPublishInvokeSearchResponse {
    private long total;
    List<WedprServiceInvokeResult> wedprPublishInvokeList;
}
