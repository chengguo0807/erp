package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 入库单明细实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stock_in_item")
public class StockInItem extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 入库单ID
     */
    private Long stockInId;
    
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
     * 入库数量
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
