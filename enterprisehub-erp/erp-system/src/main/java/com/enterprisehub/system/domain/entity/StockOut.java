package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stock_out")
public class StockOut extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String stockOutNo;
    private Long salesOrderId;
    private Long warehouseId;
    private LocalDateTime stockOutDate;
    private BigDecimal totalAmount;
    private Integer status;
    private String remark;
    @TableLogic
    private Integer delFlag;
}
