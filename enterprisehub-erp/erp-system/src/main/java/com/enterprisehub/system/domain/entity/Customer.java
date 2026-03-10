package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer")
public class Customer extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String customerCode;
    private String customerName;
    private String contact;
    private String phone;
    private String email;
    private String address;
    private Integer status;
    private String remark;
    @TableLogic
    private Integer delFlag;
}
