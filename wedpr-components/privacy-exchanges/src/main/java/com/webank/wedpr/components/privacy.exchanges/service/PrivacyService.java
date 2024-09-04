package com.webank.wedpr.components.privacy.exchanges.service;

import com.webank.wedpr.components.http.client.model.BaseRequest;


/**
 * @Company: Dinglian @Author: xieshida @Date: 2024/8/28 16:37
 *
 * @description:
 */
public interface PrivacyService {

    void getToken();

    String sendPost(BaseRequest request, String url) throws Exception;

}
