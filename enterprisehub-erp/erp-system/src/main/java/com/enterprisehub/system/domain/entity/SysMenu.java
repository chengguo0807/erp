package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * 菜单实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    /** 菜单名称 */
    private String menuName;

    /** 父菜单ID */
    private Long parentId;

    /** 显示顺序 */
    private Integer sort;

    /** 路由路径 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 菜单类型 (D目录 M菜单 B按钮) */
    private String menuType;

    /** 是否可见 (0显示 1隐藏) */
    private Integer visible;

    /** 权限标识 */
    private String permission;

    /** 图标 */
    private String icon;

    /** 状态 (0正常 1停用) */
    private Integer status;
    
    /** 子菜单列表 */
    @TableField(exist = false)
    private List<SysMenu> children;
    
    /** 是否选中(用于角色分配菜单) */
    @TableField(exist = false)
    private Boolean checked;
}
