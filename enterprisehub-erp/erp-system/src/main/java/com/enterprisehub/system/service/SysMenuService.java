package com.enterprisehub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enterprisehub.system.domain.entity.SysMenu;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface SysMenuService extends IService<SysMenu> {
    
    /**
     * 查询菜单列表（树形结构）
     */
    List<SysMenu> selectMenuList(String menuName);
    
    /**
     * 根据ID查询菜单
     */
    SysMenu selectMenuById(Long id);
    
    /**
     * 新增菜单
     */
    int insertMenu(SysMenu menu);
    
    /**
     * 修改菜单
     */
    int updateMenu(SysMenu menu);
    
    /**
     * 删除菜单
     */
    int deleteMenuById(Long id);
    
    /**
     * 构建菜单树形结构
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menus);
    
    /**
     * 根据用户ID查询菜单权限
     */
    List<SysMenu> selectMenusByUserId(Long userId);
    
    /**
     * 查询菜单树形结构
     */
    List<SysMenu> selectMenuTree();
    
    /**
     * 查询角色的菜单树
     */
    List<SysMenu> selectRoleMenuTree(Long roleId);
}
