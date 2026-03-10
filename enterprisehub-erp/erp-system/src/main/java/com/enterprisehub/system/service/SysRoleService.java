package com.enterprisehub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.SysRole;

/**
 * 角色服务接口
 */
public interface SysRoleService extends IService<SysRole> {
    
    /**
     * 分页查询角色列表
     */
    PageResult<SysRole> selectRoleList(PageQuery pageQuery, String roleName);
    
    /**
     * 根据ID查询角色
     */
    SysRole selectRoleById(Long id);
    
    /**
     * 新增角色
     */
    int insertRole(SysRole role);
    
    /**
     * 修改角色
     */
    int updateRole(SysRole role);
    
    /**
     * 删除角色
     */
    int deleteRoleById(Long id);
    
    /**
     * 批量删除角色
     */
    int deleteRoleByIds(Long[] ids);
    
    /**
     * 根据角色ID查询菜单ID列表
     */
    java.util.List<Long> selectMenuIdsByRoleId(Long roleId);
    
    /**
     * 为角色分配菜单权限
     */
    void assignMenusToRole(Long roleId, Long[] menuIds);
}
