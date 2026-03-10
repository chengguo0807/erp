package com.enterprisehub.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprisehub.system.domain.entity.SysRole;
import com.enterprisehub.system.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 用户Mapper
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND del_flag = 0")
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 查询用户的角色标识集合
     */
    @Select("SELECT r.role_key FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.status = 0 AND r.del_flag = 0")
    Set<String> selectRoleKeysByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的权限标识集合
     */
    @Select("SELECT DISTINCT m.permission FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.status = 0 AND m.del_flag = 0 " +
            "AND m.permission IS NOT NULL AND m.permission != ''")
    Set<String> selectPermissionsByUserId(@Param("userId") Long userId);
    
    /**
     * 查询用户的角色列表
     */
    @Select("SELECT r.* FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.del_flag = 0")
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);
}
