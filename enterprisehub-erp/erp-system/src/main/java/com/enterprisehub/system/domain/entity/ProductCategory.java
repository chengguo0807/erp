package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_category")
public class ProductCategory extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String categoryName;
    private Long parentId;
    private Integer sort;
    private Integer status;
    private String remark;
    
    @TableLogic
    private Integer delFlag;
}
