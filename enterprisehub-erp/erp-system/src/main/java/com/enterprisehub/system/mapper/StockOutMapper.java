package com.enterprisehub.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprisehub.system.domain.entity.StockOut;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出库单Mapper接口
 */
@Mapper
public interface StockOutMapper extends BaseMapper<StockOut> {
}
