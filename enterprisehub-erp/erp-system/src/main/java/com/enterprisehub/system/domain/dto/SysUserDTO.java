package com.enterprisehub.system.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户DTO
 */
@Data
public class SysUserDTO {
    
    private Long id;
    
    /** 用户名 */
    private String username;
    
    /** 密码 */
    private String password;
    
    /** 昵称 */
    private String nickname;
    
    /** 真实姓名 */
    private String realName;
    
    /** 邮箱 */
    private String email;
    
    /** 手机号 */
    private String phone;
    
    /** 性别 (0男 1女 2未知) */
    private Integer gender;
    
    /** 头像 */
    private String avatar;
    
    /** 部门ID */
    private Long deptId;
    
    /** 状态 (0正常 1停用) */
    private Integer status;
    
    /** 角色ID列表 */
    private List<Long> roleIds;
}
