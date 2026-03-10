package com.enterprisehub.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enterprisehub.common.core.domain.LoginUser;
import com.enterprisehub.common.core.domain.PageResult;
import com.enterprisehub.system.domain.dto.SysUserDTO;
import com.enterprisehub.system.domain.entity.SysUser;
import com.enterprisehub.system.domain.vo.SysUserVO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名查询用户
     */
    SysUser getByUsername(String username);

    /**
     * 构建登录用户信息
     */
    LoginUser buildLoginUser(SysUser user);

    /**
     * 分页查询用户
     */
    PageResult<SysUserVO> getUserPage(Integer pageNum, Integer pageSize, String username, Integer status, Long deptId);

    /**
     * 新增用户
     */
    void createUser(SysUserDTO userDTO);

    /**
     * 修改用户
     */
    void updateUser(SysUserDTO userDTO);

    /**
     * 重置密码
     */
    void resetPassword(Long userId, String newPassword);
}
