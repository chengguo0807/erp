package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.system.domain.entity.SysMenu;
import com.enterprisehub.system.mapper.SysMenuMapper;
import com.enterprisehub.system.mapper.SysRoleMenuMapper;
import com.enterprisehub.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<SysMenu> selectMenuList(String menuName) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(menuName), SysMenu::getMenuName, menuName)
               .orderByAsc(SysMenu::getSort);
        
        List<SysMenu> menuList = this.list(wrapper);
        return buildMenuTree(menuList);
    }

    @Override
    public SysMenu selectMenuById(Long id) {
        return this.getById(id);
    }

    @Override
    public int insertMenu(SysMenu menu) {
        return this.save(menu) ? 1 : 0;
    }

    @Override
    public int updateMenu(SysMenu menu) {
        return this.updateById(menu) ? 1 : 0;
    }

    @Override
    public int deleteMenuById(Long id) {
        return this.removeById(id) ? 1 : 0;
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<>();
        List<Long> tempList = menus.stream().map(SysMenu::getId).collect(Collectors.toList());
        
        for (SysMenu menu : menus) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    @Override
    public List<SysMenu> selectMenusByUserId(Long userId) {
        // TODO: 根据用户ID查询菜单权限（需要关联角色-菜单表）
        // 暂时返回所有菜单
        return this.list();
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<>();
        for (SysMenu n : list) {
            if (n.getParentId() != null && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

    @Override
    public List<SysMenu> selectMenuTree() {
        List<SysMenu> allMenus = this.list();
        return buildMenuTree(allMenus);
    }

    @Override
    public List<SysMenu> selectRoleMenuTree(Long roleId) {
        List<SysMenu> allMenus = this.list();
        List<Long> roleMenuIds = roleMenuMapper.selectMenuIdsByRoleId(roleId);
        for (SysMenu menu : allMenus) {
            if (roleMenuIds.contains(menu.getId())) {
                menu.setChecked(true);
            }
        }
        return buildMenuTree(allMenus);
    }
}
