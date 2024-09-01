package com.webank.wedpr.components.publish.controller;

import com.webank.wedpr.components.publish.entity.request.NodeInvokeRequest;
import com.webank.wedpr.components.publish.entity.request.PublishInvokeSearchRequest;
import com.webank.wedpr.components.publish.service.WedprServiceInvokeTableService;
import com.webank.wedpr.core.utils.Constant;
import com.webank.wedpr.core.utils.WeDPRResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前端控制器
 *
 * @author caryliao
 * @since 2024-08-31
 */
@RestController
@RequestMapping(
        path = Constant.WEDPR_API_PREFIX + "/publish/invoke",
        produces = {"application/json"})
@Slf4j
public class WedprServiceInvokeTableController {
    private static final Logger logger =
            LoggerFactory.getLogger(WedprServiceInvokeTableController.class);

    @Autowired private WedprServiceInvokeTableService wedprPublishInvokeService;

    @GetMapping("/search/{serviceId}")
    public WeDPRResponse searchPublishRecord(
            @PathVariable String publishId, PublishInvokeSearchRequest publishInvokeRequest) {
        try {
            return wedprPublishInvokeService.seachPublishInvokeService(
                    publishId, publishInvokeRequest);
        } catch (Exception e) {
            logger.warn("发起方搜索申报记录 exception, error: ", e);
            return new WeDPRResponse(
                    Constant.WEDPR_FAILED, "searchPublishRecord failed for " + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public WeDPRResponse invokePublishRecord(NodeInvokeRequest nodeInvokeRequest) {
        try {
            return wedprPublishInvokeService.invokePublishRecordService(nodeInvokeRequest);
        } catch (Exception e) {
            logger.warn("任务结果申报失败 exception, error: ", e);
            return new WeDPRResponse(Constant.WEDPR_FAILED, "任务结果申报失败 for " + e.getMessage());
        }
    }
}
