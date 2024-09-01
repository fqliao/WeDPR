package com.webank.wedpr.components.publish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.webank.wedpr.components.publish.entity.WedprServiceInvokeTable;
import com.webank.wedpr.components.publish.entity.result.WedprServiceInvokeResult;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Mapper 接口
 *
 * @author caryliao
 * @since 2024-08-31
 */
public interface WedprServiceInvokeTableMapper extends BaseMapper<WedprServiceInvokeTable> {

    @Select(
            "SELECT t.invoke_id,t.invoke_user,t.invoke_agency,t.invoke_status,t.invoke_time,s.expire_time,s.apply_time "
                    + "FROM wedpr_service_invoke_table t , wedpr_service_auth_table s WHERE s.service_id = t.service_id AND s.service_id = #{serviceId} "
                    + "<where> "
                    + "<if test='invokeAgency != \"\"'> AND t.invoke_agency = #{invokeAgency} </if> "
                    + "<if test='invokeStatus != \"\"'> AND t.invoke_status = #{invokeStatus} </if> "
                    + "<if test='invokeDate != \"\"'> AND DATE_FORMAT(t.invoke_time, '%Y-%m-%d') = #{invokeDate} </if> "
                    + "<if test='expireDate != \"\"'> AND DATE_FORMAT(s.expire_time, '%Y-%m-%d') = #{expireDate} </if> "
                    + "</where>")
    List<WedprServiceInvokeResult> selectWedprPublishInvokeOnCondition(
            Page<WedprServiceInvokeResult> page,
            @Param("serviceId") String serviceId,
            @Param("invokeAgency") String invokeAgency,
            @Param("invokeStatus") String invokeStatus,
            @Param("invokeDate") String invokeDate,
            @Param("expireDate") String expireDate);
}
