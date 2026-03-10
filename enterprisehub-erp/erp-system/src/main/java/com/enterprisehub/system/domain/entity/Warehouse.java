package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse")
public class Warehouse extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String warehouseCode;
    private String warehouseName;
    private String address;
    private String manager;
    private String phone;
    private Integer status;
    private String remark;
    
    @TableLogic
    private Integer delFlag;
}
