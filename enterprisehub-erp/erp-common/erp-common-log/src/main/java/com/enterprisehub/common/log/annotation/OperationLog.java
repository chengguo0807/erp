package com.enterprisehub.common.log.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /** 操作标题 */
    String title() default "";

    /** 模块名称 */
    String module() default "";

    /** 业务类型 (1新增 2修改 3删除 4导入 5导出) */
    int businessType() default 0;

    /** 操作描述 */
    String description() default "";

    /** 是否保存请求参数 */
    boolean saveRequest() default true;

    /** 是否保存响应结果 */
    boolean saveResponse() default false;
}
