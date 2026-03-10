package com.enterprisehub.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprisehub.system.domain.entity.SalesOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 销售订单Mapper接口
 */
@Mapper
public interface SalesOrderMapper extends BaseMapper<SalesOrder> {
}
