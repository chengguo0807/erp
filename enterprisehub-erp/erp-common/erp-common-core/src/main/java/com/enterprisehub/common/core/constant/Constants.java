package com.enterprisehub.common.core.constant;

/**
 * 系统常量
 */
public final class Constants {

    private Constants() {}

    /** UTF-8编码 */
    public static final String UTF8 = "UTF-8";

    /** Token前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** Token header */
    public static final String TOKEN_HEADER = "Authorization";

    /** 登录用户缓存前缀 */
    public static final String LOGIN_USER_KEY = "login:user:";

    /** 验证码缓存前缀 */
    public static final String CAPTCHA_KEY = "captcha:";

    /** 超级管理员角色标识 */
    public static final String SUPER_ADMIN = "admin";

    /** 正常状态 */
    public static final int STATUS_NORMAL = 0;

    /** 停用状态 */
    public static final int STATUS_DISABLE = 1;

    /** 删除标记-正常 */
    public static final int DEL_FLAG_NORMAL = 0;

    /** 删除标记-已删除 */
    public static final int DEL_FLAG_DELETED = 1;

    /** 菜单类型-目录 */
    public static final String MENU_TYPE_DIR = "D";

    /** 菜单类型-菜单 */
    public static final String MENU_TYPE_MENU = "M";

    /** 菜单类型-按钮 */
    public static final String MENU_TYPE_BUTTON = "B";
}
