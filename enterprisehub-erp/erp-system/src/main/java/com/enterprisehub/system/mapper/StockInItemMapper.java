package com.enterprisehub.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprisehub.system.domain.entity.StockInItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入库单明细 Mapper
 */
@Mapper
public interface StockInItemMapper extends BaseMapper<StockInItem> {
}
