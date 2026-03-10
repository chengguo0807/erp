package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product")
public class Product extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 商品编码
     */
    private String productCode;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品分类ID
     */
    private Long categoryId;
    
    /**
     * 规格型号
     */
    private String specification;
    
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 成本价
     */
    private BigDecimal costPrice;
    
    /**
     * 销售价
     */
    private BigDecimal salePrice;
    
    /**
     * 库存预警值
     */
    private Integer warningStock;
    
    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 删除标志 0-未删除 1-已删除
     */
    @TableLogic
    private Integer delFlag;
}
