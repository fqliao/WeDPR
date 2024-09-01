package com.webank.wedpr.components.publish.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.webank.wedpr.components.publish.entity.WedprServiceInvokeTable;
import com.webank.wedpr.components.publish.entity.request.NodeInvokeRequest;
import com.webank.wedpr.components.publish.entity.request.PublishInvokeSearchRequest;
import com.webank.wedpr.components.publish.entity.response.WedprPublishInvokeSearchResponse;
import com.webank.wedpr.components.publish.entity.result.WedprServiceInvokeResult;
import com.webank.wedpr.components.publish.mapper.WedprServiceInvokeTableMapper;
import com.webank.wedpr.components.publish.service.WedprServiceInvokeTableService;
import com.webank.wedpr.components.uuid.generator.WeDPRUuidGenerator;
import com.webank.wedpr.core.utils.Constant;
import com.webank.wedpr.core.utils.WeDPRResponse;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author caryliao
 * @since 2024-08-31
 */
@Service
public class WedprServiceInvokeTableServiceImpl
        extends ServiceImpl<WedprServiceInvokeTableMapper, WedprServiceInvokeTable>
        implements WedprServiceInvokeTableService {

    @Override
    public WeDPRResponse seachPublishInvokeService(
            String serviceId, PublishInvokeSearchRequest publishInvokeRequest) {
        String invokeAgency = publishInvokeRequest.getInvokeAgency();
        String invokeStatus = publishInvokeRequest.getInvokeStatus();
        String invokeDate = publishInvokeRequest.getInvokeDate();
        String expireDate = publishInvokeRequest.getExpireDate();
        Page<WedprServiceInvokeResult> wedprPublishResultPage =
                new Page<>(publishInvokeRequest.getPageNum(), publishInvokeRequest.getPageSize());

        List<WedprServiceInvokeResult> wedprServiceInvokeResults =
                this.baseMapper.selectWedprPublishInvokeOnCondition(
                        wedprPublishResultPage,
                        serviceId,
                        invokeAgency,
                        invokeStatus,
                        invokeDate,
                        expireDate);

        return new WeDPRResponse(
                Constant.WEDPR_SUCCESS,
                Constant.WEDPR_SUCCESS_MSG,
                new WedprPublishInvokeSearchResponse(
                        wedprServiceInvokeResults.size(), wedprServiceInvokeResults));
    }

    @Override
    public WeDPRResponse invokePublishRecordService(NodeInvokeRequest nodeInvokeRequest) {
        WedprServiceInvokeTable wedprPublishRecord = new WedprServiceInvokeTable();
        wedprPublishRecord.setInvokeAgency(nodeInvokeRequest.getInvokeAgency());
        wedprPublishRecord.setInvokeUser(nodeInvokeRequest.getInvokeUser());
        wedprPublishRecord.setInvokeId(WeDPRUuidGenerator.generateID());
        wedprPublishRecord.setServiceId(nodeInvokeRequest.getServiceId());
        wedprPublishRecord.setInvokeStatus(nodeInvokeRequest.getInvokeStatus());
        wedprPublishRecord.setInvokeTime(nodeInvokeRequest.getInvokeTime());
        boolean save = this.save(wedprPublishRecord);
        if (save) {
            return new WeDPRResponse(Constant.WEDPR_SUCCESS, Constant.WEDPR_SUCCESS_MSG);
        } else {
            return new WeDPRResponse(Constant.WEDPR_FAILED, "invokePublishRecord failed");
        }
    }
}
