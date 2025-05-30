package com.webank.wedpr.components.publish.sync.handler;

import com.webank.wedpr.common.utils.WeDPRException;
import com.webank.wedpr.components.db.mapper.service.publish.dao.PublishedServiceInfo;
import com.webank.wedpr.components.publish.sync.PublishSyncAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zachma
 * @date 2024/8/28
 */
public class SyncPublishActionHandler implements PublishActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(SyncPublishActionHandler.class);

    @Override
    public void handle(String content, PublishSyncerImpl syncer) throws WeDPRException {
        try {
            PublishedServiceInfo publishedServiceInfo = PublishedServiceInfo.deserialize(content);
            syncer.syncPublishService(PublishSyncAction.SYNC, publishedServiceInfo);
        } catch (Exception e) {
            logger.error("sync publish service failed, content: {}, error: ", content, e);
            throw new WeDPRException(e);
        }
    }
}
