package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sales_order")
public class SalesOrder extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long customerId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private Integer status;
    private String remark;
    @TableLogic
    private Integer delFlag;
    
    /** 订单明细列表 */
    @TableField(exist = false)
    private List<SalesOrderItem> items;
}
