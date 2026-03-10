package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购订单实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("purchase_order")
public class PurchaseOrder extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 订单编号
     */
    private String orderNo;
    
    /**
     * 供应商ID
     */
    private Long supplierId;
    
    /**
     * 供应商名称
     */
    @TableField(exist = false)
    private String supplierName;
    
    /**
     * 订单日期
     */
    private LocalDateTime orderDate;
    
    /**
     * 预计到货日期
     */
    private LocalDateTime expectedDate;
    
    /**
     * 订单总额
     */
    private BigDecimal totalAmount;
    
    /**
     * 状态（0待审核 1已审核 2已完成 3已取消）
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 订单明细列表
     */
    @TableField(exist = false)
    private List<PurchaseOrderItem> items;
}
