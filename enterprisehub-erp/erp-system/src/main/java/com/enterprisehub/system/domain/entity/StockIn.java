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
 * 入库单实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stock_in")
public class StockIn extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 入库单号
     */
    private String stockInNo;
    
    /**
     * 采购订单ID
     */
    private Long purchaseOrderId;
    
    /**
     * 采购订单号
     */
    @TableField(exist = false)
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
     * 仓库ID
     */
    private Long warehouseId;
    
    /**
     * 仓库名称
     */
    @TableField(exist = false)
    private String warehouseName;
    
    /**
     * 入库日期
     */
    private LocalDateTime stockInDate;
    
    /**
     * 入库总额
     */
    private BigDecimal totalAmount;
    
    /**
     * 状态（0待审核 1已审核 2已完成）
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 入库明细列表
     */
    @TableField(exist = false)
    private List<StockInItem> items;
}
