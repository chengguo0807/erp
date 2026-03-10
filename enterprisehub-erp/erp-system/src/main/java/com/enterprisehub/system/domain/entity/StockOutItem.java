package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("stock_out_item")
public class StockOutItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long stockOutId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal amount;
}
