package com.webank.wedpr.components.privacy.exchanges.service.impl;

import com.webank.wedpr.components.http.client.HttpClientImpl;
import com.webank.wedpr.components.http.client.model.BaseRequest;
import com.webank.wedpr.components.privacy.exchanges.dao.PrivacyEunm;
import com.webank.wedpr.components.privacy.exchanges.dao.PrivacyInfo;
import com.webank.wedpr.components.privacy.exchanges.demo.PrivacyResponse;
import com.webank.wedpr.components.privacy.exchanges.demo.PrivacyToken;
import com.webank.wedpr.components.privacy.exchanges.demo.PrivacyHeader;
import com.webank.wedpr.components.privacy.exchanges.service.PrivacyService;
import com.webank.wedpr.components.scheduler.executor.impl.ml.MLExecutorConfig;
import com.webank.wedpr.core.utils.Constant;
import com.webank.wedpr.core.utils.WeDPRException;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/28 16:34
 *
 * @description:
 */
@Service
public class PrivacyServiceImpl implements PrivacyService {

    private static final Logger logger = LoggerFactory.getLogger(PrivacyServiceImpl.class);

    @Override
    public void getToken() {

        HttpClientImpl httpClient =
                new HttpClientImpl(
                        PrivacyInfo.TOKEN,
                        MLExecutorConfig.getMaxTotalConnection(),
                        MLExecutorConfig.buildConfig(),
                        null);
        PrivacyToken request = new PrivacyToken();
        request.setAppSecret(PrivacyInfo.APP_KEY);
        request.setAppId(PrivacyInfo.APP_ID);
        try {
            String str =
                    httpClient.executeHttpsPostAndGetString(request, Constant.HTTP_SUCCESS, null);
            PrivacyResponse deserialize = PrivacyResponse.deserialize(str);
            if (!deserialize.getCode().equals(PrivacyEunm.SUCCESS.getCode())) {
                throw new WeDPRException(
                        "send request: "
                                + request.serialize()
                                + " failed, status: "
                                + deserialize.getCode() + ","
                                + deserialize.getSuccess()
                                + ", detail: "
                                + deserialize.getMessage());
            }
            logger.info(
                    "##### send  get token, request: {}, response: {},", request.serialize(), str);
            PrivacyHeader.setInstanceToken(deserialize.getData());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String sendPost(BaseRequest request ,String url) throws Exception {
        PrivacyHeader privacyHeader = checkToken();
        HttpClientImpl httpClient =
                new HttpClientImpl(
                        url,
                        MLExecutorConfig.getMaxTotalConnection(),
                        MLExecutorConfig.buildConfig(),
                        null);
        return httpClient.executeHttpsPostAndGetString(request, Constant.HTTP_SUCCESS, privacyHeader.getHeader());
    }

    public PrivacyHeader checkToken() {
        if (PrivacyHeader.getInstance() == null) {
            getToken();
        }
        return PrivacyHeader.getInstance();
    }
}
