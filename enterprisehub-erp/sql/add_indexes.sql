-- =============================================
-- 数据库索引优化脚本
-- 用途：为常用查询字段添加索引，提升查询性能
-- =============================================

USE erp_system;

-- ----------------------------
-- 用户表索引
-- ----------------------------
ALTER TABLE sys_user ADD INDEX IF NOT EXISTS idx_status (status);
ALTER TABLE sys_user ADD INDEX IF NOT EXISTS idx_dept_id (dept_id);
ALTER TABLE sys_user ADD INDEX IF NOT EXISTS idx_create_time (create_time);
ALTER TABLE sys_user ADD INDEX IF NOT EXISTS idx_status_dept (status, dept_id);

-- ----------------------------
-- 角色表索引
-- ----------------------------
ALTER TABLE sys_role ADD INDEX IF NOT EXISTS idx_status (status);
ALTER TABLE sys_role ADD INDEX IF NOT EXISTS idx_role_key (role_key);

-- ----------------------------
-- 菜单表索引
-- ----------------------------
ALTER TABLE sys_menu ADD INDEX IF NOT EXISTS idx_parent_id (parent_id);
ALTER TABLE sys_menu ADD INDEX IF NOT EXISTS idx_status (status);

-- ----------------------------
-- 部门表索引
-- ----------------------------
ALTER TABLE sys_dept ADD INDEX IF NOT EXISTS idx_parent_id (parent_id);
ALTER TABLE sys_dept ADD INDEX IF NOT EXISTS idx_status (status);

-- ----------------------------
-- 操作日志表索引
-- ----------------------------
ALTER TABLE sys_operation_log ADD INDEX IF NOT EXISTS idx_operator_id (operator_id);
ALTER TABLE sys_operation_log ADD INDEX IF NOT EXISTS idx_status (status);
ALTER TABLE sys_operation_log ADD INDEX IF NOT EXISTS idx_module (module);
ALTER TABLE sys_operation_log ADD INDEX IF NOT EXISTS idx_create_time (create_time);
ALTER TABLE sys_operation_log ADD INDEX IF NOT EXISTS idx_operator_time (operator_id, create_time);

-- ----------------------------
-- 验证索引创建结果
-- ----------------------------
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    COLUMN_NAME,
    SEQ_IN_INDEX
FROM 
    information_schema.STATISTICS
WHERE 
    TABLE_SCHEMA = 'erp_system'
    AND TABLE_NAME IN ('sys_user', 'sys_role', 'sys_menu', 'sys_dept', 'sys_operation_log')
ORDER BY 
    TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;

-- 完成提示
SELECT '✅ 数据库索引优化完成！' AS message;
