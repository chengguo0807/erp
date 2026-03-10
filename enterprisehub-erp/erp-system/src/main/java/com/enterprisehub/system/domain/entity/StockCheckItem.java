package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("stock_check_item")
public class StockCheckItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long checkId;
    private Long productId;
    private Integer bookQuantity;
    private Integer actualQuantity;
    private Integer differenceQuantity;
}
