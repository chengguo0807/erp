package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.common.log.annotation.OperationLog;
import com.enterprisehub.system.domain.entity.SysRole;
import com.enterprisehub.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/system/v1/roles")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping
    public R<PageResult<SysRole>> list(PageQuery pageQuery, String roleName) {
        PageResult<SysRole> result = roleService.selectRoleList(pageQuery, roleName);
        return R.ok(result);
    }

    /**
     * 根据ID查询角色详情
     */
    @GetMapping("/{id}")
    public R<SysRole> getInfo(@PathVariable Long id) {
        SysRole role = roleService.selectRoleById(id);
        return R.ok(role);
    }

    /**
     * 新增角色
     */
    @PostMapping
    @OperationLog(title = "角色管理", businessType = 1)
    public R<Void> add(@RequestBody SysRole role) {
        roleService.insertRole(role);
        return R.ok();
    }

    /**
     * 修改角色
     */
    @PutMapping("/{id}")
    @OperationLog(module = "角色管理", businessType = 2)
    public R<Void> edit(@PathVariable Long id, @RequestBody SysRole role) {
        role.setId(id);
        roleService.updateRole(role);
        return R.ok();
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @OperationLog(title = "角色管理", businessType = 3)
    public R<Void> remove(@PathVariable Long id) {
        roleService.deleteRoleById(id);
        return R.ok();
    }

    /**
     * 批量删除角色
     */
    @DeleteMapping("/batch")
    @OperationLog(title = "角色管理", businessType = 3)
    public R<Void> removeBatch(@RequestBody Long[] ids) {
        roleService.deleteRoleByIds(ids);
        return R.ok();
    }

    /**
     * 获取角色的菜单权限ID列表
     */
    @GetMapping("/{roleId}/menus")
    public R<java.util.List<Long>> getRoleMenuIds(@PathVariable Long roleId) {
        java.util.List<Long> menuIds = roleService.selectMenuIdsByRoleId(roleId);
        return R.ok(menuIds);
    }

    /**
     * 为角色分配菜单权限
     */
    @PutMapping("/{roleId}/menus")
    @OperationLog(module = "角色管理", businessType = 2)
    public R<Void> assignMenus(@PathVariable Long roleId, @RequestBody Long[] menuIds) {
        roleService.assignMenusToRole(roleId, menuIds);
        return R.ok();
    }
}
