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

package com.webank.wedpr.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.webank.wedpr.admin.request.GetWedprAuditLogListRequest;
import com.webank.wedpr.components.sync.dao.ResourceActionDO;
import com.webank.wedpr.components.sync.service.impl.ResourceStatusResult;

public interface WedprSyncService extends IService<ResourceActionDO> {
    public abstract ResourceStatusResult queryRecordSyncStatus(GetWedprAuditLogListRequest request);
}
