/*
 * Copyright 2017-2025  [webank-wedpr]
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.webank.wedpr.components.task.plugin.pir.controller;

import com.webank.wedpr.common.utils.Common;
import com.webank.wedpr.common.utils.Constant;
import com.webank.wedpr.common.utils.WeDPRResponse;
import com.webank.wedpr.components.db.mapper.service.publish.dao.PublishedServiceInfo;
import com.webank.wedpr.components.db.mapper.service.publish.model.PirServiceSetting;
import com.webank.wedpr.components.task.plugin.pir.service.PirService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        path = Constant.WEDPR_API_PREFIX + "/pir",
        produces = {"application/json"})
public class PirController {
    private static final Logger logger = LoggerFactory.getLogger(PirController.class);
    @Autowired private PirService pirService;

    @PostMapping("/publish")
    public WeDPRResponse publish(@RequestBody PublishedServiceInfo publishedServiceInfo) {
        try {
            Common.requireNonEmpty("serviceId", publishedServiceInfo.getServiceId());
            PirServiceSetting pirServiceSetting =
                    PirServiceSetting.deserialize(publishedServiceInfo.getServiceConfig());
            return this.pirService.publish(publishedServiceInfo.getServiceId(), pirServiceSetting);
        } catch (Exception e) {
            logger.warn("publish service {} failed, error: ", publishedServiceInfo.toString(), e);
            return new WeDPRResponse(Constant.WEDPR_FAILED, e.getMessage());
        }
    }
}
