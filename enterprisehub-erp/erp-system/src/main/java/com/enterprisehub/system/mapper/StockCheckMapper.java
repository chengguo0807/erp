package com.enterprisehub.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprisehub.system.domain.entity.StockCheck;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存盘点Mapper接口
 */
@Mapper
public interface StockCheckMapper extends BaseMapper<StockCheck> {
}
