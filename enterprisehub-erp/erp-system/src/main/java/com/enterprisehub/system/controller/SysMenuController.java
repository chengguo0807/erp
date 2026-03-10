package com.enterprisehub.system.controller;

import com.enterprisehub.common.core.domain.R;
import com.enterprisehub.common.log.annotation.OperationLog;
import com.enterprisehub.system.domain.entity.SysMenu;
import com.enterprisehub.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 */
@RestController
@RequestMapping("/system/v1/menus")
public class SysMenuController {

    @Autowired
    private SysMenuService menuService;

    /**
     * 查询菜单列表（树形结构）
     */
    @GetMapping
    public R<List<SysMenu>> list(String menuName) {
        List<SysMenu> list = menuService.selectMenuList(menuName);
        return R.ok(list);
    }

    /**
     * 根据ID查询菜单详情
     */
    @GetMapping("/{id}")
    public R<SysMenu> getInfo(@PathVariable Long id) {
        SysMenu menu = menuService.selectMenuById(id);
        return R.ok(menu);
    }

    /**
     * 根据用户ID查询菜单权限
     */
    @GetMapping("/user/{userId}")
    public R<List<SysMenu>> getMenusByUserId(@PathVariable Long userId) {
        List<SysMenu> menus = menuService.selectMenusByUserId(userId);
        return R.ok(menus);
    }

    /**
     * 新增菜单
     */
    @PostMapping
    @OperationLog(title = "菜单管理", businessType = 1)
    public R<Void> add(@RequestBody SysMenu menu) {
        menuService.insertMenu(menu);
        return R.ok();
    }

    /**
     * 修改菜单
     */
    @PutMapping("/{id}")
    @OperationLog(module = "菜单管理", businessType = 2)
    public R<Void> edit(@PathVariable Long id, @RequestBody SysMenu menu) {
        menu.setId(id);
        menuService.updateMenu(menu);
        return R.ok();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    @OperationLog(title = "菜单管理", businessType = 3)
    public R<Void> remove(@PathVariable Long id) {
        menuService.deleteMenuById(id);
        return R.ok();
    }

    /**
     * 获取菜单树形结构
     */
    @GetMapping("/tree")
    public R<List<SysMenu>> getMenuTree() {
        List<SysMenu> menuTree = menuService.selectMenuTree();
        return R.ok(menuTree);
    }

    /**
     * 获取角色的菜单树（用于权限分配时显示）
     */
    @GetMapping("/tree/role/{roleId}")
    public R<List<SysMenu>> getRoleMenuTree(@PathVariable Long roleId) {
        List<SysMenu> menuTree = menuService.selectRoleMenuTree(roleId);
        return R.ok(menuTree);
    }
}
