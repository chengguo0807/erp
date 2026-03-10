package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory_transaction")
public class InventoryTransaction extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private Long warehouseId;
    private String transactionType;
    private Integer quantity;
    private String referenceNo;
    private LocalDateTime transactionDate;
    private String remark;
    @TableLogic
    private Integer delFlag;
}
