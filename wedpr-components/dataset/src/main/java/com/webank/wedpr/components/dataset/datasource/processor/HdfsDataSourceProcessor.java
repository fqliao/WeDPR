package com.webank.wedpr.components.dataset.datasource.processor;

import com.webank.wedpr.components.dataset.datasource.DataSourceMeta;
import com.webank.wedpr.components.dataset.datasource.category.HdfsDataSource;
import com.webank.wedpr.components.dataset.exception.DatasetException;
import com.webank.wedpr.components.dataset.utils.JsonUtils;
import com.webank.wedpr.components.storage.api.FileStorageInterface;
import com.webank.wedpr.components.storage.impl.hdfs.HDFSStoragePath;
import com.webank.wedpr.core.utils.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HdfsDataSourceProcessor implements DataSourceProcessor {

    private static final Logger logger = LoggerFactory.getLogger(HdfsDataSourceProcessor.class);

    protected DataSourceProcessorContext dataSourceProcessorContext;

    @Override
    public void setContext(DataSourceProcessorContext context) {
        this.dataSourceProcessorContext = context;
    }

    @Override
    public DataSourceMeta parseDataSourceMeta(String strDataSourceMeta) throws DatasetException {
        long startTimeMillis = System.currentTimeMillis();

        HdfsDataSource hdfsDataSource =
                (HdfsDataSource)
                        JsonUtils.jsonString2Object(strDataSourceMeta, HdfsDataSource.class);

        String filePath = hdfsDataSource.getFilePath();
        Common.requireNonEmpty("filePath", filePath);

        FileStorageInterface fileStorage = dataSourceProcessorContext.getFileStorage();
        checkFileExists(fileStorage, filePath);

        long endTimeMillis = System.currentTimeMillis();
        logger.info(
                " => data source processor stage parse data source meta end, datasetId: {}, hdfsDataSource: {}, cost(ms): {}",
                filePath,
                hdfsDataSource,
                endTimeMillis - startTimeMillis);

        return hdfsDataSource;
    }

    public void checkFileExists(FileStorageInterface storageInterface, String filePath)
            throws DatasetException {
        HDFSStoragePath hdfsStoragePath = new HDFSStoragePath(filePath);
        boolean exists = storageInterface.exists(hdfsStoragePath);
        if (!exists) {
            logger.error("HDFS file does not exist, filePath: {}", filePath);
            throw new DatasetException("HDFS file does not exist, filePath: " + filePath);
        }
    }

    @Override
    public void prepareData() throws DatasetException {}

    @Override
    public void analyzeData() throws DatasetException {}

    @Override
    public void uploadData() throws DatasetException {
        // do nothing
    }

    @Override
    public void cleanupData() throws DatasetException {}
}
