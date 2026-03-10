package com.enterprisehub.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprisehub.system.domain.entity.StockOutItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockOutItemMapper extends BaseMapper<StockOutItem> {
}
