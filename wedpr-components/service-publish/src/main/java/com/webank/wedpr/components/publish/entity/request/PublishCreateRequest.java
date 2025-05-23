package com.webank.wedpr.components.publish.entity.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.webank.wedpr.common.utils.Constant;
import com.webank.wedpr.common.utils.WeDPRException;
import com.webank.wedpr.components.db.mapper.dataset.dao.Dataset;
import com.webank.wedpr.components.db.mapper.dataset.mapper.DatasetMapper;
import com.webank.wedpr.components.db.mapper.service.publish.dao.PublishedServiceInfo;
import com.webank.wedpr.components.db.mapper.service.publish.model.PirSearchType;
import com.webank.wedpr.components.db.mapper.service.publish.model.PirServiceSetting;
import com.webank.wedpr.components.publish.helper.PublishServiceHelper;
import com.webank.wedpr.components.uuid.generator.WeDPRUuidGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

/** @author zachma */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublishCreateRequest extends PublishedServiceInfo {
    private String serviceId = Constant.PUBLISH_ID_PREFIX + WeDPRUuidGenerator.generateID();
    @JsonIgnore private PublishServiceHelper.PublishType publishType;

    // the granted access key list
    protected List<String> grantedAccessKeyList;

    public PublishCreateRequest() {
        this.serviceId = Constant.PUBLISH_ID_PREFIX + WeDPRUuidGenerator.generateID();
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
        this.publishType = PublishServiceHelper.PublishType.deserialize(serviceType);
    }

    public void setPublishType(PublishServiceHelper.PublishType publishType) {
        this.publishType = publishType;
        if (publishType == null) {
            return;
        }
        this.serviceType = this.publishType.getType();
    }

    public void setServiceId(String serviceId) {
        if (serviceId == null) {
            return;
        }
        this.serviceId = serviceId;
    }

    public void checkServiceConfig(DatasetMapper datasetMapper, String user, String agency)
            throws Exception {
        if (this.publishType == null) {
            throw new WeDPRException("The serviceType must be one of pir/xgb/lr");
        }
        if (this.serviceType == PublishServiceHelper.PublishType.PIR.getType()) {
            this.checkPirServiceConfig(datasetMapper, user, agency);
        }
    }

    protected void checkPirServiceConfig(DatasetMapper datasetMapper, String user, String agency)
            throws Exception {
        PirServiceSetting pirServiceSetting = PirServiceSetting.deserialize(this.serviceConfig);
        // check the datasetId
        Dataset dataset =
                datasetMapper.getDatasetByDatasetId(pirServiceSetting.getDatasetId(), false);
        if (dataset == null) {
            throw new WeDPRException(
                    "Publish pir service failed for the dataset "
                            + pirServiceSetting.getDatasetId()
                            + " not exists!");
        }
        // check the permission
        if (dataset.getOwnerAgencyName().compareToIgnoreCase(agency) != 0
                || dataset.getOwnerUserName().compareToIgnoreCase(user) != 0) {
            throw new WeDPRException("Only the owner can publish the dataset!");
        }
        // check the idField
        List<String> datasetFields =
                Arrays.stream(dataset.getDatasetFields().trim().split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());
        if (!datasetFields.contains(pirServiceSetting.getIdField())) {
            throw new WeDPRException(
                    "Publish pir service failed for the idField "
                            + pirServiceSetting.getIdField()
                            + " not exists!");
        }
        // check the accessibleValueQueryFields
        if (pirServiceSetting.getSearchTypeObject() == PirSearchType.SearchExist) {
            return;
        }
        if (pirServiceSetting.getAccessibleValueQueryFields() != null) {
            for (String field : pirServiceSetting.getAccessibleValueQueryFields()) {
                if (!datasetFields.contains(field)) {
                    throw new WeDPRException(
                            "Publish pir service failed for the field " + field + " not exists!");
                }
            }
        }
    }
}
