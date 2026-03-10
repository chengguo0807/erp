package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 采购订单明细实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("purchase_order_item")
public class PurchaseOrderItem extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 采购订单ID
     */
    private Long orderId;
    
    /**
     * 产品ID
     */
    private Long productId;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 产品规格
     */
    private String specification;
    
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 数量
     */
    private BigDecimal quantity;
    
    /**
     * 单价
     */
    private BigDecimal price;
    
    /**
     * 金额
     */
    private BigDecimal amount;
    
    /**
     * 备注
     */
    private String remark;
}
