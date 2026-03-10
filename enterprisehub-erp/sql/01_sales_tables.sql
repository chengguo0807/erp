-- =============================================
-- 销售管理模块数据库表
-- =============================================

USE `erp_system`;

-- ----------------------------
-- 客户表
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `customer_name` VARCHAR(200) NOT NULL COMMENT '客户名称',
    `customer_code` VARCHAR(50) DEFAULT NULL COMMENT '客户编码',
    `contact` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `address` VARCHAR(500) DEFAULT NULL COMMENT '地址',
    `credit_level` TINYINT DEFAULT 1 COMMENT '信用等级 (1-5)',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0正常 1停用)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_customer_code` (`customer_code`, `del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户表';

-- ----------------------------
-- 销售订单表
-- ----------------------------
DROP TABLE IF EXISTS `sales_order`;
CREATE TABLE `sales_order` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单编号',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `customer_name` VARCHAR(200) DEFAULT NULL COMMENT '客户名称',
    `order_date` DATE NOT NULL COMMENT '订单日期',
    `delivery_date` DATE DEFAULT NULL COMMENT '交货日期',
    `total_amount` DECIMAL(15,2) DEFAULT 0.00 COMMENT '订单总金额',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0待审核 1已审核 2已完成 3已取消)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`, `del_flag`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_order_date` (`order_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售订单表';
