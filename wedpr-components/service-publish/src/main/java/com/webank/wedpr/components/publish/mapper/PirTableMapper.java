package com.webank.wedpr.components.publish.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author zachma
 * @date 2024/9/2
 */
public interface PirTableMapper {

    @Update("${tableSql}")
    void executeTableByNativeSql(@Param("tableSql") String tableSql);
}
