package com.webank.wedpr.components.publish.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.webank.wedpr.components.publish.entity.WedprServiceInvokeTable;
import com.webank.wedpr.components.publish.entity.request.NodeInvokeRequest;
import com.webank.wedpr.components.publish.entity.request.PublishInvokeSearchRequest;
import com.webank.wedpr.core.utils.WeDPRResponse;

/**
 * 服务类
 *
 * @author caryliao
 * @since 2024-08-31
 */
public interface WedprServiceInvokeTableService extends IService<WedprServiceInvokeTable> {
    WeDPRResponse seachPublishInvokeService(
            String publishId, PublishInvokeSearchRequest publishInvokeRequest);

    WeDPRResponse invokePublishRecordService(NodeInvokeRequest nodeInvokeRequest);
}
