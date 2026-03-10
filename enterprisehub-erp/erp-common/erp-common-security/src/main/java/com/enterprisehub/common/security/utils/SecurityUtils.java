package com.enterprisehub.common.security.utils;

import com.enterprisehub.common.core.domain.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    /**
     * 获取当前登录用户
     */
    public static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            return (LoginUser) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUserId() : null;
    }

    /**
     * 获取当前租户ID
     */
    public static Long getCurrentTenantId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getTenantId() : null;
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUsername() : null;
    }

    /**
     * 判断是否为超级管理员
     */
    public static boolean isSuperAdmin() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null && loginUser.getRoles() != null
                && loginUser.getRoles().contains("admin");
    }
}
