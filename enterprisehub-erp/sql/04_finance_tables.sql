-- =============================================
-- 财务管理模块数据库表
-- =============================================

USE `erp_system`;

-- ----------------------------
-- 收付款表
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `payment_no` VARCHAR(50) NOT NULL COMMENT '收付款单号',
    `payment_type` TINYINT NOT NULL COMMENT '类型 (1收款 2付款)',
    `payment_method` TINYINT DEFAULT 1 COMMENT '支付方式 (1现金 2银行转账 3支票 4其他)',
    `amount` DECIMAL(15,2) NOT NULL COMMENT '金额',
    `payment_date` DATE NOT NULL COMMENT '收付款日期',
    `related_type` VARCHAR(20) DEFAULT NULL COMMENT '关联类型 (sales/purchase)',
    `related_no` VARCHAR(50) DEFAULT NULL COMMENT '关联单号',
    `payer_payee` VARCHAR(200) DEFAULT NULL COMMENT '付款人/收款人',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0待审核 1已审核 2已完成)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_payment_no` (`payment_no`, `del_flag`),
    KEY `idx_payment_date` (`payment_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收付款表';

-- ----------------------------
-- 费用表
-- ----------------------------
DROP TABLE IF EXISTS `expense`;
CREATE TABLE `expense` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `expense_no` VARCHAR(50) NOT NULL COMMENT '费用单号',
    `expense_type` VARCHAR(50) DEFAULT NULL COMMENT '费用类型',
    `amount` DECIMAL(15,2) NOT NULL COMMENT '金额',
    `expense_date` DATE NOT NULL COMMENT '费用日期',
    `applicant` VARCHAR(50) DEFAULT NULL COMMENT '申请人',
    `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
    `dept_name` VARCHAR(100) DEFAULT NULL COMMENT '部门名称',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0待审核 1已审核 2已支付 3已拒绝)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_expense_no` (`expense_no`, `del_flag`),
    KEY `idx_expense_date` (`expense_date`),
    KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='费用表';
