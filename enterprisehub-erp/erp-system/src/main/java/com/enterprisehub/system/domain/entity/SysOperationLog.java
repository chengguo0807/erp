package com.enterprisehub.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 操作日志表
 */
@Data
@TableName("sys_operation_log")
public class SysOperationLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /** 操作模块 */
    private String module;
    
    /** 操作类型 */
    private String operation;
    
    /** 请求方法 */
    private String method;
    
    /** 请求URL */
    private String requestUrl;
    
    /** 请求参数 */
    private String requestParams;
    
    /** 响应结果 */
    private String responseResult;
    
    /** 状态（0成功 1失败） */
    private Integer status;
    
    /** 错误信息 */
    private String errorMsg;
    
    /** 操作人 */
    private String operator;
    
    /** 操作IP */
    private String operatorIp;
    
    /** 操作时间 */
    private LocalDateTime operationTime;
    
    /** 耗时(ms) */
    private Long duration;
}
