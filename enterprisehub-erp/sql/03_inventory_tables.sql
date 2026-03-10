-- =============================================
-- 库存管理模块数据库表
-- =============================================

USE `erp_system`;

-- ----------------------------
-- 产品分类表
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `category_name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `category_code` VARCHAR(50) DEFAULT NULL COMMENT '分类编码',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID',
    `sort` INT DEFAULT 0 COMMENT '显示顺序',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0正常 1停用)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品分类表';

-- ----------------------------
-- 产品表
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '产品名称',
    `product_code` VARCHAR(50) NOT NULL COMMENT '产品编码',
    `category_id` BIGINT DEFAULT NULL COMMENT '分类ID',
    `category_name` VARCHAR(100) DEFAULT NULL COMMENT '分类名称',
    `unit` VARCHAR(20) DEFAULT NULL COMMENT '单位',
    `spec` VARCHAR(200) DEFAULT NULL COMMENT '规格',
    `price` DECIMAL(15,2) DEFAULT 0.00 COMMENT '单价',
    `cost` DECIMAL(15,2) DEFAULT 0.00 COMMENT '成本',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0正常 1停用)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_code` (`product_code`, `del_flag`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表';

-- ----------------------------
-- 仓库表
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `warehouse_name` VARCHAR(100) NOT NULL COMMENT '仓库名称',
    `warehouse_code` VARCHAR(50) NOT NULL COMMENT '仓库编码',
    `address` VARCHAR(500) DEFAULT NULL COMMENT '仓库地址',
    `manager` VARCHAR(50) DEFAULT NULL COMMENT '仓库管理员',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0正常 1停用)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_warehouse_code` (`warehouse_code`, `del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='仓库表';

-- ----------------------------
-- 库存表
-- ----------------------------
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `product_id` BIGINT NOT NULL COMMENT '产品ID',
    `product_name` VARCHAR(200) DEFAULT NULL COMMENT '产品名称',
    `product_code` VARCHAR(50) DEFAULT NULL COMMENT '产品编码',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `warehouse_name` VARCHAR(100) DEFAULT NULL COMMENT '仓库名称',
    `quantity` DECIMAL(15,2) DEFAULT 0.00 COMMENT '库存数量',
    `available_quantity` DECIMAL(15,2) DEFAULT 0.00 COMMENT '可用数量',
    `locked_quantity` DECIMAL(15,2) DEFAULT 0.00 COMMENT '锁定数量',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_warehouse` (`product_id`, `warehouse_id`),
    KEY `idx_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存表';

-- ----------------------------
-- 入库单表
-- ----------------------------
DROP TABLE IF EXISTS `stock_in`;
CREATE TABLE `stock_in` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `stock_in_no` VARCHAR(50) NOT NULL COMMENT '入库单号',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `warehouse_name` VARCHAR(100) DEFAULT NULL COMMENT '仓库名称',
    `stock_in_date` DATE NOT NULL COMMENT '入库日期',
    `stock_in_type` TINYINT DEFAULT 1 COMMENT '入库类型 (1采购入库 2退货入库 3其他入库)',
    `related_no` VARCHAR(50) DEFAULT NULL COMMENT '关联单号',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0待审核 1已审核 2已完成)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_stock_in_no` (`stock_in_no`, `del_flag`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_stock_in_date` (`stock_in_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='入库单表';

-- ----------------------------
-- 出库单表
-- ----------------------------
DROP TABLE IF EXISTS `stock_out`;
CREATE TABLE `stock_out` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `stock_out_no` VARCHAR(50) NOT NULL COMMENT '出库单号',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `warehouse_name` VARCHAR(100) DEFAULT NULL COMMENT '仓库名称',
    `stock_out_date` DATE NOT NULL COMMENT '出库日期',
    `stock_out_type` TINYINT DEFAULT 1 COMMENT '出库类型 (1销售出库 2退货出库 3其他出库)',
    `related_no` VARCHAR(50) DEFAULT NULL COMMENT '关联单号',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0待审核 1已审核 2已完成)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_stock_out_no` (`stock_out_no`, `del_flag`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_stock_out_date` (`stock_out_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='出库单表';

-- ----------------------------
-- 库存盘点表
-- ----------------------------
DROP TABLE IF EXISTS `stock_check`;
CREATE TABLE `stock_check` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `check_no` VARCHAR(50) NOT NULL COMMENT '盘点单号',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `warehouse_name` VARCHAR(100) DEFAULT NULL COMMENT '仓库名称',
    `check_date` DATE NOT NULL COMMENT '盘点日期',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0盘点中 1已完成 2已取消)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_check_no` (`check_no`, `del_flag`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_check_date` (`check_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存盘点表';

-- ----------------------------
-- 库存流水表
-- ----------------------------
DROP TABLE IF EXISTS `inventory_transaction`;
CREATE TABLE `inventory_transaction` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `product_id` BIGINT NOT NULL COMMENT '产品ID',
    `product_name` VARCHAR(200) DEFAULT NULL COMMENT '产品名称',
    `product_code` VARCHAR(50) DEFAULT NULL COMMENT '产品编码',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `warehouse_name` VARCHAR(100) DEFAULT NULL COMMENT '仓库名称',
    `transaction_type` TINYINT NOT NULL COMMENT '交易类型 (1入库 2出库 3盘盈 4盘亏)',
    `quantity` DECIMAL(15,2) NOT NULL COMMENT '数量',
    `before_quantity` DECIMAL(15,2) DEFAULT 0.00 COMMENT '变动前数量',
    `after_quantity` DECIMAL(15,2) DEFAULT 0.00 COMMENT '变动后数量',
    `related_no` VARCHAR(50) DEFAULT NULL COMMENT '关联单号',
    `transaction_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '交易时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_transaction_date` (`transaction_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存流水表';
