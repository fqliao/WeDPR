package com.webank.wedpr.components.publish.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.webank.wedpr.components.dataset.config.DatasetConfig;
import com.webank.wedpr.components.dataset.dao.Dataset;
import com.webank.wedpr.components.dataset.mapper.DatasetMapper;
import com.webank.wedpr.components.dataset.service.DatasetServiceApi;
import com.webank.wedpr.components.dataset.utils.ThreadPoolUtils;
import com.webank.wedpr.components.publish.entity.WedprPublishedService;
import com.webank.wedpr.components.publish.entity.request.PirConfigRequest;
import com.webank.wedpr.components.publish.entity.request.PublishCreateRequest;
import com.webank.wedpr.components.publish.entity.request.PublishSearchRequest;
import com.webank.wedpr.components.publish.entity.response.WedprPublishCreateResponse;
import com.webank.wedpr.components.publish.entity.response.WedprPublishSearchResponse;
import com.webank.wedpr.components.publish.entity.response.WedprPublishSearchReturn;
import com.webank.wedpr.components.publish.helper.JsonHelper;
import com.webank.wedpr.components.publish.helper.PublishServiceHelper;
import com.webank.wedpr.components.publish.mapper.PirTableMapper;
import com.webank.wedpr.components.publish.mapper.WedprPublishedServiceMapper;
import com.webank.wedpr.components.publish.service.WedprPublishedServiceService;
import com.webank.wedpr.components.publish.sync.PublishSyncAction;
import com.webank.wedpr.components.publish.sync.api.PublishSyncerApi;
import com.webank.wedpr.components.storage.api.FileStorageInterface;
import com.webank.wedpr.components.storage.api.StoragePath;
import com.webank.wedpr.core.config.WeDPRCommonConfig;
import com.webank.wedpr.core.utils.CSVFileParser;
import com.webank.wedpr.core.utils.Constant;
import com.webank.wedpr.core.utils.WeDPRException;
import com.webank.wedpr.core.utils.WeDPRResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现类
 *
 * @author caryliao
 * @since 2024-08-31
 */
@Service
public class WedprPublishedServiceServiceImpl
        extends ServiceImpl<WedprPublishedServiceMapper, WedprPublishedService>
        implements WedprPublishedServiceService {

    private static final Logger logger =
            LoggerFactory.getLogger(WedprPublishedServiceServiceImpl.class);

    @Qualifier("publishSyncer")
    @Autowired
    private PublishSyncerApi publishSyncer;

    @Autowired
    @Qualifier("datasetAsyncExecutor")
    private Executor executor;

    @Override
    @Transactional(rollbackFor = WeDPRException.class)
    public WeDPRResponse createPublishService(String username, PublishCreateRequest publishCreate)
            throws WeDPRException {
        try {
            prepareCreatePublishServiceRequest(publishCreate);
            String serviceId = PublishServiceHelper.newPublishServiceId();
            // 创建新服务
            WedprPublishedService wedprPublished = new WedprPublishedService();
            wedprPublished.setServiceId(serviceId);
            wedprPublished.setServiceName(publishCreate.getServiceName());
            wedprPublished.setServiceDesc(publishCreate.getServiceDesc());
            wedprPublished.setAgency(WeDPRCommonConfig.getAgency());
            wedprPublished.setOwner(username);
            wedprPublished.setServiceConfig(publishCreate.getServiceConfig());
            wedprPublished.setServiceType(publishCreate.getServiceType());
            wedprPublished.setCreateTime(LocalDateTime.now());
            if (this.save(wedprPublished)) {
                publishSyncer.publishSync(wedprPublished.serialize());
                return new WeDPRResponse(
                        Constant.WEDPR_SUCCESS,
                        Constant.WEDPR_SUCCESS_MSG,
                        new WedprPublishCreateResponse(serviceId));
            } else {
                return new WeDPRResponse(Constant.WEDPR_FAILED, serviceId + "服务创建失败");
            }
        } catch (Exception e) {
            throw new WeDPRException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = WeDPRException.class)
    public WeDPRResponse updatePublishService(String username, PublishCreateRequest publishCreate)
            throws WeDPRException {
        LambdaQueryWrapper<WedprPublishedService> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WedprPublishedService::getServiceId, publishCreate.getServiceId());
        WedprPublishedService wedprPublishedService = this.getOne(lambdaQueryWrapper);
        if (wedprPublishedService == null) {
            return new WeDPRResponse(
                    Constant.WEDPR_FAILED, publishCreate.getServiceId() + "无法撤销，不属于用户" + username);
        }

        wedprPublishedService.setServiceDesc(publishCreate.getServiceDesc());
        wedprPublishedService.setServiceConfig(publishCreate.getServiceConfig());
        wedprPublishedService.setLastUpdateTime(LocalDateTime.now());
        boolean updated = this.update(wedprPublishedService, lambdaQueryWrapper);
        if (updated) {
            publishSyncer.publishSync(wedprPublishedService.serialize());
            return new WeDPRResponse(Constant.WEDPR_SUCCESS, Constant.WEDPR_SUCCESS_MSG);
        } else {
            return new WeDPRResponse(
                    Constant.WEDPR_FAILED, publishCreate.getServiceId() + "服务更新失败");
        }
    }

    @Override
    @Transactional(rollbackFor = WeDPRException.class)
    public WeDPRResponse revokePublishService(String username, String serviceId)
            throws WeDPRException {

        try {
            LambdaQueryWrapper<WedprPublishedService> lambdaQueryWrapper =
                    new LambdaQueryWrapper<>();
            lambdaQueryWrapper
                    .eq(WedprPublishedService::getServiceId, serviceId)
                    .eq(WedprPublishedService::getOwner, username)
                    .eq(WedprPublishedService::getAgency, WeDPRCommonConfig.getAgency());
            WedprPublishedService one = this.getOne(lambdaQueryWrapper);
            if (Objects.isNull(one)) {
                return new WeDPRResponse(Constant.WEDPR_FAILED, serviceId + "服务用户无权撤回");
            }

            PirConfigRequest pirConfigRequest =
                    JsonHelper.jsonString2Object(one.getServiceConfig(), PirConfigRequest.class);

            boolean removed = this.remove(lambdaQueryWrapper);
            if (removed) {
                WedprPublishedService wedprPublish = new WedprPublishedService();
                wedprPublish.setServiceId(serviceId);
                publishSyncer.revokeSync(wedprPublish.serialize());
                String tableId =
                        PublishServiceHelper.PIR_TEMP_TABLE_PREFIX
                                + pirConfigRequest.getDatasetId().substring(2);
                String sql = String.format("DROP TABLE IF EXISTS %s", tableId);
                pirTableMapper.executeTableByNativeSql(sql);
                return new WeDPRResponse(Constant.WEDPR_SUCCESS, Constant.WEDPR_SUCCESS_MSG);
            } else {
                return new WeDPRResponse(Constant.WEDPR_FAILED, serviceId + "服务撤回失败");
            }
        } catch (Exception e) {
            throw new WeDPRException("撤回任务失败");
        }
    }

    @Override
    public WeDPRResponse listPublishService(PublishSearchRequest request) {
        String owner = request.getOwner();
        String serviceName = request.getServiceName();
        String agency = request.getAgency();
        String serviceType = request.getServiceType();
        String createDate = request.getCreateDate();
        LambdaQueryWrapper<WedprPublishedService> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(owner)) {
            lambdaQueryWrapper.like(WedprPublishedService::getOwner, owner);
        }
        if (StringUtils.isNotBlank(serviceName)) {
            lambdaQueryWrapper.like(WedprPublishedService::getServiceName, serviceName);
        }
        if (StringUtils.isNotBlank(agency)) {
            lambdaQueryWrapper.eq(WedprPublishedService::getAgency, agency);
        }
        if (StringUtils.isNotBlank(serviceType)) {
            lambdaQueryWrapper.eq(WedprPublishedService::getServiceType, serviceType);
        }
        if (StringUtils.isNotBlank(createDate)) {
            lambdaQueryWrapper.apply("DATE_FORMAT(create_time, '%Y-%m-%d') = {0}", createDate);
        }

        Page<WedprPublishedService> wedprPublishPage =
                new Page<>(request.getPageNum(), request.getPageSize());
        Page<WedprPublishedService> page = this.page(wedprPublishPage, lambdaQueryWrapper);
        return new WeDPRResponse(
                Constant.WEDPR_SUCCESS,
                Constant.WEDPR_SUCCESS_MSG,
                new WedprPublishSearchResponse(page.getTotal(), page.getRecords()));
    }

    @Override
    public WeDPRResponse searchPublishService(String serviceId) {
        LambdaQueryWrapper<WedprPublishedService> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WedprPublishedService::getServiceId, serviceId);
        WedprPublishedService wedprPublish = this.getOne(lambdaQueryWrapper);
        return new WeDPRResponse(
                Constant.WEDPR_SUCCESS,
                Constant.WEDPR_SUCCESS_MSG,
                new WedprPublishSearchReturn(wedprPublish));
    }

    @Override
    public WedprPublishedService getPublishService(String serviceId) {
        LambdaQueryWrapper<WedprPublishedService> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WedprPublishedService::getServiceId, serviceId);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public void syncPublishService(
            PublishSyncAction action, WedprPublishedService wedprPublishedService) {
        String serviceId = wedprPublishedService.getServiceId();
        if (action == PublishSyncAction.SYNC) {
            if (getPublishService(serviceId) == null) {
                this.save(wedprPublishedService);
            } else {
                LambdaQueryWrapper<WedprPublishedService> wrapper =
                        new LambdaQueryWrapper<WedprPublishedService>()
                                .eq(WedprPublishedService::getServiceId, serviceId);
                this.update(wedprPublishedService, wrapper);
            }
        }

        if (action == PublishSyncAction.REVOKE) {
            if (getPublishService(serviceId) != null) {
                LambdaQueryWrapper<WedprPublishedService> lambdaQueryWrapper =
                        new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(WedprPublishedService::getServiceId, serviceId);
                this.remove(lambdaQueryWrapper);
            }
        }
    }

    @Qualifier("datasetService")
    @Autowired
    private DatasetServiceApi datasetService;

    @Autowired private DatasetMapper datasetMapper;

    @Autowired private DatasetConfig datasetConfig;

    @Qualifier("fileStorage")
    @Autowired
    private FileStorageInterface fileStorage;

    private void prepareCreatePublishServiceRequest(PublishCreateRequest publishCreate)
            throws WeDPRException {
        // 1.检验 publishType
        String[] types =
                Arrays.stream(PublishServiceHelper.PublishType.values())
                        .map(PublishServiceHelper.PublishType::getType)
                        .toArray(String[]::new);
        if (!Arrays.asList(types).contains(publishCreate.getServiceType())) {
            throw new WeDPRException("输入的类型必须为pir/xgb/lr");
        }

        // 判断pir的serviceConfig
        if (publishCreate.getServiceType().equals(PublishServiceHelper.PublishType.PIR.getType())) {
            PirConfigRequest pirConfigRequest =
                    JsonHelper.jsonString2Object(
                            publishCreate.getServiceConfig(), PirConfigRequest.class);

            // 检验dataset标头是否有id
            Dataset dataset =
                    datasetMapper.getDatasetByDatasetId(pirConfigRequest.getDatasetId(), false);
            String[] datasetFields =
                    Arrays.stream(dataset.getDatasetFields().trim().split(","))
                            .map(String::trim)
                            .toArray(String[]::new);
            if (!Arrays.asList(datasetFields)
                    .contains(PublishServiceHelper.PUBLISH_PIR_NEED_COLUMN)) {
                throw new WeDPRException("发布的数据集" + pirConfigRequest.getDatasetId() + "必须含有id列");
            }

            String datasetId = pirConfigRequest.getDatasetId();
            StoragePath datasetStoragePath = datasetService.getDatasetStoragePath(datasetId);
            String csvFilePath =
                    datasetConfig.getDatasetBaseDir()
                            + File.separator
                            + PublishServiceHelper.PIR_TEMP_DIR
                            + File.separator
                            + datasetId;

            ThreadPoolUtils.execute(
                    executor,
                    "DataSourceProcessor.processDataToDB",
                    datasetId,
                    () -> {
                        // hdfs下载数据到  csvFilePath
                        fileStorage.download(datasetStoragePath, csvFilePath);
                        try {
                            processCsv2Db(datasetId, datasetFields, csvFilePath);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    @Autowired private PirTableMapper pirTableMapper;

    private void processCsv2Db(String datasetId, String[] datasetFields, String csvFilePath)
            throws Exception {
        List<List<String>> sqlValues = CSVFileParser.processCsv2SqlMap(datasetFields, csvFilePath);
        if (sqlValues.size() == 0) {
            throw new WeDPRException("数据集为空,请退回");
        }

        String tableId = PublishServiceHelper.PIR_TEMP_TABLE_PREFIX + datasetId.substring(2);
        String createSqlFormat =
                "DROP TABLE IF EXISTS %s ; CREATE TABLE %s ( %s , PRIMARY KEY (`id`) USING BTREE )";

        String[] fieldsWithType = new String[datasetFields.length];
        for (int i = 0; i < datasetFields.length; i++) {
            if (datasetFields[i].equalsIgnoreCase(PublishServiceHelper.PUBLISH_PIR_NEED_COLUMN)) {
                fieldsWithType[i] = datasetFields[i] + " VARCHAR(64)";
            } else {
                fieldsWithType[i] = datasetFields[i] + " TEXT";
            }
        }
        String sql = String.format(createSqlFormat, tableId, String.join(",", fieldsWithType));
        logger.info("create pir db : " + sql);
        pirTableMapper.executeTableByNativeSql(sql);

        StringBuilder sb = new StringBuilder();
        for (List<String> values : sqlValues) {
            sb.append("(").append(String.join(",", values)).append("), ");
        }
        String insertValues = sb.toString();
        insertValues = insertValues.substring(0, insertValues.length() - 2);

        String insertSqlFormat = "INSERT INTO %s (%s) VALUES %s ";
        sql =
                String.format(
                        insertSqlFormat, tableId, String.join(",", datasetFields), insertValues);
        logger.info("fill data to pir db size : " + sqlValues.size());
        pirTableMapper.executeTableByNativeSql(sql);
    }
}
