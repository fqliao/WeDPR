package com.webank.wedpr.components.publish.entity.result;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author zachma
 * @date 2024/8/31
 */
@Data
public class WedprServiceInvokeResult {
    private String invokeId;
    private String invokeUser;
    private String invokeAgency;
    private String invokeStatus;
    private LocalDateTime invokeTime;
    private LocalDateTime expireTime;
    private LocalDateTime applyTime;
}
