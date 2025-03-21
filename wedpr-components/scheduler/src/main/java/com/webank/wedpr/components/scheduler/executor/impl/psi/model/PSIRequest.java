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

package com.webank.wedpr.components.scheduler.executor.impl.psi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.webank.wedpr.common.utils.ObjectMapperFactory;
import com.webank.wedpr.common.utils.WeDPRException;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PSIRequest {
    public static enum AlgorithmType {
        CM_PSI_2PC(0),
        ECDH_PSI_MULTI(4);
        private final Integer algorithmType;

        AlgorithmType(Integer algorithmType) {
            this.algorithmType = algorithmType;
        }

        public Integer getAlgorithmType() {
            return this.algorithmType;
        }
    }

    private String taskID;
    private Integer type = 0;
    // the algorithm
    private Integer algorithm;
    private Boolean syncResult;
    private Boolean enableOutputExists = Boolean.TRUE;
    private Boolean lowBandwidth = Boolean.FALSE;
    private List<PartyInfo> parties;
    private List<String> receiverList;
    // the user information
    private String user;

    public void setParties(List<PartyInfo> parties) throws Exception {

        this.parties = parties;
        if (this.parties == null || this.parties.size() < 2) {
            throw new WeDPRException("Invalid PSIRequest, Must define at least two parties!");
        }
        if (this.parties.size() == 2) {
            this.algorithm = AlgorithmType.CM_PSI_2PC.getAlgorithmType();
        } else {
            this.algorithm = AlgorithmType.ECDH_PSI_MULTI.getAlgorithmType();
        }
    }

    public void setEnableOutputExists(Boolean enableOutputExists) {
        if (enableOutputExists == null) {
            return;
        }
        this.enableOutputExists = enableOutputExists;
    }

    public String serialize() throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
    }

    public static PSIRequest deserialize(String data) throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().readValue(data, PSIRequest.class);
    }
}
