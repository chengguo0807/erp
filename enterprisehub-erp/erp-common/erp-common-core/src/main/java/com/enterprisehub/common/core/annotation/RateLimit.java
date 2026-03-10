package com.enterprisehub.common.core.annotation;

import java.lang.annotation.*;

/**
 * 限流注解
 * 
 * @author EnterpriseHub
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    
    /**
     * 限流key前缀
     */
    String key() default "rate_limit";
    
    /**
     * 时间窗口（秒）
     */
    int time() default 60;
    
    /**
     * 时间窗口内最大请求数
     */
    int count() default 100;
    
    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;
    
    /**
     * 限流类型枚举
     */
    enum LimitType {
        /**
         * 默认策略全局限流
         */
        DEFAULT,
        
        /**
         * 根据请求者IP进行限流
         */
        IP,
        
        /**
         * 根据用户ID进行限流
         */
        USER
    }
}
