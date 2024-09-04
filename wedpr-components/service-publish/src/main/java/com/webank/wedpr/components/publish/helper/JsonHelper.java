package com.webank.wedpr.components.publish.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.webank.wedpr.core.utils.WeDPRException;

/**
 * @author zachma
 * @date 2024/8/31
 */
public class JsonHelper {

    private JsonHelper() {}

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public static <T> T jsonString2Object(String jsonStr, Class<T> cls) throws WeDPRException {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, cls);
        } catch (JsonProcessingException e) {
            throw new WeDPRException("Invalid json object format, e: " + e.getMessage());
        }
    }

    public static String object2jsonString(Object cls) throws WeDPRException {
        try {
            return OBJECT_MAPPER.writeValueAsString(cls);
        } catch (JsonProcessingException e) {
            throw new WeDPRException("Invalid json object format, e: " + e.getMessage());
        }
    }
}
