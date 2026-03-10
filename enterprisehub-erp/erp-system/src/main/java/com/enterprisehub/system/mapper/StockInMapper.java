package com.enterprisehub.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprisehub.system.domain.entity.StockIn;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入库单 Mapper
 */
@Mapper
public interface StockInMapper extends BaseMapper<StockIn> {
}
