package com.enterprisehub.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enterprisehub.common.core.domain.LoginUser;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.common.core.exception.BusinessException;
import com.enterprisehub.system.domain.dto.SysUserDTO;
import com.enterprisehub.system.domain.entity.SysRole;
import com.enterprisehub.system.domain.entity.SysUser;
import com.enterprisehub.system.domain.entity.SysUserRole;
import com.enterprisehub.system.domain.vo.SysUserVO;
import com.enterprisehub.system.mapper.SysUserMapper;
import com.enterprisehub.system.mapper.SysUserRoleMapper;
import com.enterprisehub.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SysUser getByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public LoginUser buildLoginUser(SysUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setTenantId(user.getTenantId());
        loginUser.setDeptId(user.getDeptId());
        loginUser.setUsername(user.getUsername());
        loginUser.setNickname(user.getNickname());

        // 查询角色和权限
        Set<String> roles = sysUserMapper.selectRoleKeysByUserId(user.getId());
        Set<String> permissions = sysUserMapper.selectPermissionsByUserId(user.getId());
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);

        return loginUser;
    }

    @Override
    public PageResult<SysUserVO> getUserPage(Integer pageNum, Integer pageSize,
                                            String username, Integer status, Long deptId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(username), SysUser::getUsername, username)
                .eq(status != null, SysUser::getStatus, status)
                .eq(deptId != null, SysUser::getDeptId, deptId)
                .orderByDesc(SysUser::getCreateTime);

        Page<SysUser> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        
        // 转换为VO并查询角色信息
        List<SysUserVO> voList = page.getRecords().stream().map(user -> {
            SysUserVO vo = new SysUserVO();
            BeanUtils.copyProperties(user, vo);
            
            // 查询用户角色
            List<SysRole> roles = sysUserMapper.selectRolesByUserId(user.getId());
            vo.setRoles(roles);
            vo.setRoleIds(roles.stream().map(SysRole::getId).collect(Collectors.toList()));
            
            return vo;
        }).collect(Collectors.toList());
        
        return new PageResult<>(voList, page.getTotal(), pageNum, pageSize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(SysUserDTO userDTO) {
        // 校验用户名唯一
        SysUser existing = getByUsername(userDTO.getUsername());
        if (existing != null) {
            throw new BusinessException("用户名'" + userDTO.getUsername() + "'已存在");
        }
        
        // 创建用户
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        this.save(user);
        
        // 保存用户角色关系
        saveUserRoles(user.getId(), userDTO.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserDTO userDTO) {
        // 获取原用户信息
        SysUser existingUser = this.getById(userDTO.getId());
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 更新用户信息
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(existingUser.getPassword()); // 保留原密码
        
        // 如果用户名变更，检查唯一性
        if (!existingUser.getUsername().equals(userDTO.getUsername())) {
            SysUser duplicate = getByUsername(userDTO.getUsername());
            if (duplicate != null) {
                throw new BusinessException("用户名'" + userDTO.getUsername() + "'已存在");
            }
        }
        
        this.updateById(user);
        
        // 更新用户角色关系
        sysUserRoleMapper.deleteByUserId(user.getId());
        saveUserRoles(user.getId(), userDTO.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId, String newPassword) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        this.updateById(user);
    }
    
    /**
     * 保存用户角色关系
     */
    private void saveUserRoles(Long userId, List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        
        List<SysUserRole> userRoles = roleIds.stream()
                .map(roleId -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    return userRole;
                })
                .collect(Collectors.toList());
        
        sysUserRoleMapper.batchInsert(userRoles);
    }
}
