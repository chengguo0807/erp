package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 供应商实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("supplier")
public class Supplier extends BaseEntity {
    
    /**
     * 供应商ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 供应商编码
     */
    private String supplierCode;
    
    /**
     * 供应商名称
     */
    private String supplierName;
    
    /**
     * 联系人
     */
    private String contact;
    
    /**
     * 联系电话
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 状态（0正常 1停用）
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
}
