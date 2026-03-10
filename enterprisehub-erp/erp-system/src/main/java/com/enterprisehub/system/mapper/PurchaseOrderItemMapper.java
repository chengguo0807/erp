package com.enterprisehub.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprisehub.system.domain.entity.PurchaseOrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购订单明细 Mapper
 */
@Mapper
public interface PurchaseOrderItemMapper extends BaseMapper<PurchaseOrderItem> {
}
