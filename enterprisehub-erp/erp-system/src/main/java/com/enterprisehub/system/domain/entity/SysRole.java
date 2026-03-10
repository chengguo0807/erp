package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {

    /** 角色名称 */
    private String roleName;

    /** 角色标识 */
    private String roleKey;

    /** 显示顺序 */
    private Integer sort;

    /** 数据权限范围 (1全部 2自定义 3本部门 4本部门及以下 5仅本人) */
    private Integer dataScope;

    /** 状态 (0正常 1停用) */
    private Integer status;
}
