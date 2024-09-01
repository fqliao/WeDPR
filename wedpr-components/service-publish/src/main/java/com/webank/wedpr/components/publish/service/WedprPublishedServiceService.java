package com.webank.wedpr.components.publish.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.webank.wedpr.components.publish.entity.WedprPublishedService;
import com.webank.wedpr.components.publish.entity.request.PublishCreateRequest;
import com.webank.wedpr.components.publish.entity.request.PublishSearchRequest;
import com.webank.wedpr.components.publish.sync.PublishSyncAction;
import com.webank.wedpr.core.utils.WeDPRException;
import com.webank.wedpr.core.utils.WeDPRResponse;

/**
 * 服务类
 *
 * @author caryliao
 * @since 2024-08-31
 */
public interface WedprPublishedServiceService extends IService<WedprPublishedService> {
    WeDPRResponse createPublishService(String username, PublishCreateRequest publishCreate)
            throws WeDPRException;

    WeDPRResponse updatePublishService(String username, PublishCreateRequest publishCreate)
            throws WeDPRException;

    WeDPRResponse revokePublishService(String username, String serviceId) throws WeDPRException;

    void syncPublishService(PublishSyncAction action, WedprPublishedService wedprPublish);

    WeDPRResponse listPublishService(PublishSearchRequest request);

    WeDPRResponse searchPublishService(String publishId);

    WedprPublishedService getPublishService(String serviceId);
}
