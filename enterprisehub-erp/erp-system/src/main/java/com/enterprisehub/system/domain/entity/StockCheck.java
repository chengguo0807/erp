package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stock_check")
public class StockCheck extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String checkNo;
    private Long warehouseId;
    private LocalDateTime checkDate;
    private Integer status;
    private String remark;
    @TableLogic
    private Integer delFlag;
}
