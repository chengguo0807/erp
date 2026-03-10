package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory")
public class Inventory extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long productId;
    private Long warehouseId;
    private BigDecimal quantity;
    private BigDecimal availableQuantity;
    private BigDecimal lockedQuantity;
    private BigDecimal minQuantity;
    private BigDecimal maxQuantity;
    
    @TableLogic
    private Integer delFlag;
}
