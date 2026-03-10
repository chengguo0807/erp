-- =============================================
-- EnterpriseHub ERP 系统管理数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS `erp_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `erp_system`;

-- ----------------------------
-- 部门表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `dept_name` VARCHAR(100) NOT NULL COMMENT '部门名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID',
    `ancestors` VARCHAR(500) DEFAULT '' COMMENT '祖级列表',
    `sort` INT DEFAULT 0 COMMENT '显示顺序',
    `leader` VARCHAR(50) DEFAULT NULL COMMENT '负责人',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0正常 1停用)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(200) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) DEFAULT '' COMMENT '昵称',
    `real_name` VARCHAR(50) DEFAULT '' COMMENT '真实姓名',
    `email` VARCHAR(100) DEFAULT '' COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT '' COMMENT '手机号',
    `gender` TINYINT DEFAULT 2 COMMENT '性别 (0男 1女 2未知)',
    `avatar` VARCHAR(500) DEFAULT '' COMMENT '头像',
    `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0正常 1停用)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`, `del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------
-- 角色表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `role_name` VARCHAR(100) NOT NULL COMMENT '角色名称',
    `role_key` VARCHAR(100) NOT NULL COMMENT '角色标识',
    `sort` INT DEFAULT 0 COMMENT '显示顺序',
    `data_scope` TINYINT DEFAULT 1 COMMENT '数据权限范围 (1全部 2自定义 3本部门 4本部门及以下 5仅本人)',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0正常 1停用)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_key` (`role_key`, `del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ----------------------------
-- 菜单表
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `menu_name` VARCHAR(100) NOT NULL COMMENT '菜单名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
    `sort` INT DEFAULT 0 COMMENT '显示顺序',
    `path` VARCHAR(200) DEFAULT '' COMMENT '路由路径',
    `component` VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
    `menu_type` CHAR(1) DEFAULT '' COMMENT '菜单类型 (D目录 M菜单 B按钮)',
    `visible` TINYINT DEFAULT 0 COMMENT '是否可见 (0显示 1隐藏)',
    `permission` VARCHAR(200) DEFAULT NULL COMMENT '权限标识',
    `icon` VARCHAR(100) DEFAULT '#' COMMENT '图标',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0正常 1停用)',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志 (0正常 1删除)',
    `version` INT DEFAULT 0 COMMENT '乐观锁版本号',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

-- ----------------------------
-- 用户-角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户-角色关联表';

-- ----------------------------
-- 角色-菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色-菜单关联表';

-- ----------------------------
-- 操作日志表
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID',
    `module` VARCHAR(50) DEFAULT '' COMMENT '模块名称',
    `business_type` TINYINT DEFAULT 0 COMMENT '业务类型',
    `description` VARCHAR(200) DEFAULT '' COMMENT '操作描述',
    `method` VARCHAR(200) DEFAULT '' COMMENT '请求方法',
    `request_url` VARCHAR(500) DEFAULT '' COMMENT '请求URL',
    `request_method` VARCHAR(10) DEFAULT '' COMMENT 'HTTP方法',
    `request_params` TEXT COMMENT '请求参数',
    `response_result` TEXT COMMENT '响应结果',
    `operator_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
    `operator_name` VARCHAR(50) DEFAULT '' COMMENT '操作人',
    `operator_ip` VARCHAR(50) DEFAULT '' COMMENT '操作IP',
    `status` TINYINT DEFAULT 0 COMMENT '状态 (0成功 1失败)',
    `error_msg` TEXT COMMENT '错误信息',
    `cost_time` BIGINT DEFAULT 0 COMMENT '耗时(毫秒)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- =============================================
-- 初始化数据
-- =============================================

-- 初始化部门
INSERT INTO `sys_dept` (`id`, `dept_name`, `parent_id`, `ancestors`, `sort`, `leader`, `status`) VALUES
(100, 'EnterpriseHub集团', 0, '0', 0, '管理员', 0),
(101, '总裁办', 100, '0,100', 1, NULL, 0),
(102, '技术部', 100, '0,100', 2, NULL, 0),
(103, '市场部', 100, '0,100', 3, NULL, 0),
(104, '财务部', 100, '0,100', 4, NULL, 0),
(105, '人力资源部', 100, '0,100', 5, NULL, 0),
(106, '采购部', 100, '0,100', 6, NULL, 0),
(107, '生产部', 100, '0,100', 7, NULL, 0),
(108, '仓储部', 100, '0,100', 8, NULL, 0);

-- 初始化角色
-- admin密码: admin123 (BCrypt加密)
INSERT INTO `sys_role` (`id`, `role_name`, `role_key`, `sort`, `data_scope`, `status`) VALUES
(1, '超级管理员', 'admin', 1, 1, 0),
(2, '普通用户', 'common', 2, 5, 0),
(3, '部门管理员', 'dept_admin', 3, 3, 0);

-- 初始化用户 (密码: admin123)
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `real_name`, `dept_id`, `status`) VALUES
(1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', '系统管理员', 100, 0),
(2, 'user', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '普通用户', '测试用户', 102, 0);

-- 用户-角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2);

-- 初始化菜单
INSERT INTO `sys_menu` (`id`, `menu_name`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `permission`, `icon`, `status`) VALUES
-- 系统管理
(1, '系统管理', 0, 1, '/system', NULL, 'D', 0, NULL, 'Setting', 0),
(101, '用户管理', 1, 1, '/system/user', 'system/user/index', 'M', 0, 'system:user:list', 'User', 0),
(102, '角色管理', 1, 2, '/system/role', 'system/role/index', 'M', 0, 'system:role:list', 'UserFilled', 0),
(103, '菜单管理', 1, 3, '/system/menu', 'system/menu/index', 'M', 0, 'system:menu:list', 'Menu', 0),
(104, '部门管理', 1, 4, '/system/dept', 'system/dept/index', 'M', 0, 'system:dept:list', 'OfficeBuilding', 0),
(105, '操作日志', 1, 5, '/system/log', 'system/log/index', 'M', 0, 'system:log:list', 'Document', 0),

-- 用户管理按钮
(1011, '用户查询', 101, 1, '', NULL, 'B', 0, 'system:user:query', '#', 0),
(1012, '用户新增', 101, 2, '', NULL, 'B', 0, 'system:user:add', '#', 0),
(1013, '用户修改', 101, 3, '', NULL, 'B', 0, 'system:user:edit', '#', 0),
(1014, '用户删除', 101, 4, '', NULL, 'B', 0, 'system:user:remove', '#', 0),
(1015, '重置密码', 101, 5, '', NULL, 'B', 0, 'system:user:resetPwd', '#', 0),

-- 采购管理目录（预留）
(2, '采购管理', 0, 2, '/purchase', NULL, 'D', 0, NULL, 'ShoppingCart', 0),
-- 库存管理目录（预留）
(3, '库存管理', 0, 3, '/inventory', NULL, 'D', 0, NULL, 'Box', 0),
-- 销售管理目录（预留）
(4, '销售管理', 0, 4, '/sales', NULL, 'D', 0, NULL, 'Goods', 0),
-- 财务管理目录（预留）
(5, '财务管理', 0, 5, '/finance', NULL, 'D', 0, NULL, 'Money', 0);

-- 超级管理员拥有所有菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 1), (1, 101), (1, 102), (1, 103), (1, 104), (1, 105),
(1, 1011), (1, 1012), (1, 1013), (1, 1014), (1, 1015),
(1, 2), (1, 3), (1, 4), (1, 5);

-- 普通用户仅有查看权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(2, 1), (2, 101), (2, 1011);
