package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收付款记录表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("payment")
public class Payment extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 单据编号 */
    private String paymentNo;
    
    /** 类型（1收款 2付款） */
    private Integer type;
    
    /** 对象类型（1客户 2供应商） */
    private Integer targetType;
    
    /** 对象ID */
    private Long targetId;
    
    /** 金额 */
    private BigDecimal amount;
    
    /** 支付方式（现金/转账/支票） */
    private String paymentMethod;
    
    /** 关联订单号 */
    private String refOrderNo;
    
    /** 支付日期 */
    private LocalDateTime paymentDate;
    
    /** 备注 */
    private String remark;
    
    @TableLogic
    private Integer delFlag;
}
