package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.PageQuery;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.entity.SysRole;
import com.enterprisehub.system.domain.entity.SysRoleMenu;
import com.enterprisehub.system.mapper.SysRoleMapper;
import com.enterprisehub.system.mapper.SysRoleMenuMapper;
import com.enterprisehub.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色服务实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Override
    public PageResult<SysRole> selectRoleList(PageQuery pageQuery, String roleName) {
        Page<SysRole> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleName), SysRole::getRoleName, roleName)
               .orderByAsc(SysRole::getSort);
        
        Page<SysRole> result = this.page(page, wrapper);
        return PageResult.build(result);
    }

    @Override
    public SysRole selectRoleById(Long id) {
        return this.getById(id);
    }

    @Override
    public int insertRole(SysRole role) {
        return this.save(role) ? 1 : 0;
    }

    @Override
    public int updateRole(SysRole role) {
        return this.updateById(role) ? 1 : 0;
    }

    @Override
    public int deleteRoleById(Long id) {
        return this.removeById(id) ? 1 : 0;
    }

    @Override
    public int deleteRoleByIds(Long[] ids) {
        return this.removeBatchByIds(java.util.Arrays.asList(ids)) ? ids.length : 0;
    }

    @Override
    public List<Long> selectMenuIdsByRoleId(Long roleId) {
        return roleMenuMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    @Transactional
    public void assignMenusToRole(Long roleId, Long[] menuIds) {
        // 先删除该角色的所有菜单权限
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        
        // 如果有新的菜单权限，则批量插入
        if (menuIds != null && menuIds.length > 0) {
            List<SysRoleMenu> list = new ArrayList<>();
            for (Long menuId : menuIds) {
                SysRoleMenu rm = new SysRoleMenu();
                rm.setRoleId(roleId);
                rm.setMenuId(menuId);
                list.add(rm);
            }
            for (SysRoleMenu rm : list) {
                roleMenuMapper.insert(rm);
            }
        }
    }
}
