package com.enterprisehub.system.domain.vo;

import com.enterprisehub.system.domain.entity.SysRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户VO
 */
@Data
public class SysUserVO {
    
    private Long id;
    
    /** 用户名 */
    private String username;
    
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
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
    
    /** 角色列表 */
    private List<SysRole> roles;
    
    /** 角色ID列表 */
    private List<Long> roleIds;
}
