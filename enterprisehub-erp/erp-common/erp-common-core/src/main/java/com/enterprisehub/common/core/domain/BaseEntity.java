package com.enterprisehub.common.core.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据库实体基类
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 租户ID */
    private Long tenantId;

    /** 创建人 */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新人 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 删除标志 (0正常 1删除) */
    @TableLogic
    private Integer delFlag;

    /** 乐观锁版本号 */
    @Version
    private Integer version;

    /** 备注 */
    private String remark;
}
