-- =============================================
-- 执行所有业务模块SQL脚本
-- =============================================

-- 执行销售管理模块
SOURCE 01_sales_tables.sql;

-- 执行采购管理模块
SOURCE 02_purchase_tables.sql;

-- 执行库存管理模块
SOURCE 03_inventory_tables.sql;

-- 执行财务管理模块
SOURCE 04_finance_tables.sql;

SELECT '所有业务表创建完成！' AS message;
