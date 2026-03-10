package com.enterprisehub.common.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 * 
 * @author EnterpriseHub
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
    
    /**
     * 锁的key，支持SpEL表达式
     * 例如：#productId 或 'product:' + #productId
     */
    String key();
    
    /**
     * 等待锁的时间（秒）
     */
    long waitTime() default 10;
    
    /**
     * 锁的过期时间（秒）
     */
    long leaseTime() default 30;
    
    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    
    /**
     * 是否公平锁
     */
    boolean fair() default false;
}
