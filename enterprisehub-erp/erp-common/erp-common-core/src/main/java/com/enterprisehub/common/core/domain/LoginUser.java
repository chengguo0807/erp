package com.enterprisehub.common.core.domain;

import lombok.Data;
import java.io.Serializable;
import java.util.Set;

/**
 * 登录用户信息
 */
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 租户ID */
    private Long tenantId;

    /** 部门ID */
    private Long deptId;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 权限标识集合 */
    private Set<String> permissions;

    /** 角色标识集合 */
    private Set<String> roles;

    /** 数据权限范围 */
    private Integer dataScope;

    /** Token */
    private String token;
}
