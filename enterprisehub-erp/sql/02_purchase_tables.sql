-- =============================================
-- 采购管理模块数据库表
-- =============================================

USE `erp_system`;

-- ----------------------------
-- 供应商表
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `supplier_name` VARCHAR(200) NOT NULL COMMENT '供应商名称',
    `supplier_code` VARCHAR(50) DEFAULT NULL COMMENT '供应商编码',
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
    UNIQUE KEY `uk_supplier_code` (`supplier_code`, `del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供应商表';

-- ----------------------------
-- 采购订单表
-- ----------------------------
DROP TABLE IF EXISTS `purchase_order`;
CREATE TABLE `purchase_order` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单编号',
    `supplier_id` BIGINT NOT NULL COMMENT '供应商ID',
    `supplier_name` VARCHAR(200) DEFAULT NULL COMMENT '供应商名称',
    `order_date` DATE NOT NULL COMMENT '订单日期',
    `expected_date` DATE DEFAULT NULL COMMENT '预计到货日期',
    `total_amount` DECIMAL(15,2) DEFAULT 0.00 COMMENT '订单总金额',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0待审核 1已审核 2已入库 3已取消)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`, `del_flag`),
    KEY `idx_supplier_id` (`supplier_id`),
    KEY `idx_order_date` (`order_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购订单表';
