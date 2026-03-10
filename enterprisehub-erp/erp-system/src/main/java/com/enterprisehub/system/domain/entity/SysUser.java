package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.enterprisehub.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

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
}
