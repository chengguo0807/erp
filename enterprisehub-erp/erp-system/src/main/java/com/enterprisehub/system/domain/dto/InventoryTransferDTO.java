package com.enterprisehub.system.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 库存调拨DTO
 */
@Data
public class InventoryTransferDTO {
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 源仓库ID
     */
    private Long fromWarehouseId;
    
    /**
     * 目标仓库ID
     */
    private Long toWarehouseId;
    
    /**
     * 调拨数量
     */
    private BigDecimal quantity;
    
    /**
     * 备注
     */
    private String remark;
}
