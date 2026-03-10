package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 费用记录表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("expense")
public class Expense extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 费用编号 */
    private String expenseNo;
    
    /** 费用类别（房租/水电/工资/办公等） */
    private String category;
    
    /** 金额 */
    private BigDecimal amount;
    
    /** 费用日期 */
    private LocalDate expenseDate;
    
    /** 说明 */
    private String description;
    
    /** 备注 */
    private String remark;
    
    @TableLogic
    private Integer delFlag;
}
