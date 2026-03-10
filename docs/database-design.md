# EnterpriseHub ERP 数据库设计

## 1. 设计规范

- 数据库：MySQL 8.0，字符集 `utf8mb4`，排序规则 `utf8mb4_general_ci`
- 引擎：InnoDB
- 命名：小写蛇形，`模块前缀_表名`（如 `sys_user`、`pur_order`）
- 主键：`id BIGINT AUTO_INCREMENT`
- 公共字段：`create_by`、`create_time`、`update_by`、`update_time`、`del_flag`、`remark`
- 逻辑删除：`del_flag`（0正常、1已删除）

---

## 2. 系统管理模块（sys_）

### 2.1 用户表 sys_user

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| username | VARCHAR(50) | 用户名（唯一） |
| password | VARCHAR(100) | 密码（BCrypt加密） |
| nickname | VARCHAR(50) | 昵称 |
| real_name | VARCHAR(50) | 真实姓名 |
| email | VARCHAR(100) | 邮箱 |
| phone | VARCHAR(20) | 手机号 |
| gender | TINYINT | 性别（0男 1女 2未知） |
| avatar | VARCHAR(200) | 头像URL |
| dept_id | BIGINT | 部门ID |
| status | TINYINT | 状态（0正常 1停用） |
| del_flag | TINYINT | 删除标志 |
| create_by / create_time / update_by / update_time / remark | - | 公共字段 |

### 2.2 角色表 sys_role

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| role_name | VARCHAR(50) | 角色名称 |
| role_key | VARCHAR(50) | 角色标识（唯一） |
| sort | INT | 排序 |
| status | TINYINT | 状态 |
| del_flag / 公共字段 | - | 同上 |

### 2.3 菜单表 sys_menu

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| menu_name | VARCHAR(50) | 菜单名称 |
| parent_id | BIGINT | 父菜单ID |
| sort | INT | 排序 |
| path | VARCHAR(200) | 路由地址 |
| component | VARCHAR(200) | 前端组件路径 |
| menu_type | CHAR(1) | 类型（D目录 M菜单 B按钮） |
| permission | VARCHAR(100) | 权限标识 |
| icon | VARCHAR(50) | 图标 |
| visible | TINYINT | 是否可见 |
| status | TINYINT | 状态 |

### 2.4 部门表 sys_dept

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| dept_name | VARCHAR(50) | 部门名称 |
| parent_id | BIGINT | 父部门ID |
| sort | INT | 排序 |
| leader | VARCHAR(50) | 负责人 |
| phone | VARCHAR(20) | 联系电话 |
| status | TINYINT | 状态 |

### 2.5 关联表

- **sys_user_role**：用户-角色关联（user_id, role_id）
- **sys_role_menu**：角色-菜单关联（role_id, menu_id）

### 2.6 操作日志表 sys_operation_log

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| module | VARCHAR(50) | 操作模块 |
| operation | VARCHAR(50) | 操作类型 |
| method | VARCHAR(200) | 请求方法 |
| request_url | VARCHAR(200) | 请求URL |
| request_params | TEXT | 请求参数 |
| response_result | TEXT | 响应结果 |
| status | TINYINT | 状态（0成功 1失败） |
| error_msg | TEXT | 错误信息 |
| operator | VARCHAR(50) | 操作人 |
| operator_ip | VARCHAR(50) | 操作IP |
| operation_time | DATETIME | 操作时间 |
| duration | BIGINT | 耗时(ms) |

---

## 3. 采购管理模块（pur_）— 规划中

### 3.1 供应商表 pur_supplier

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| supplier_name | VARCHAR(100) | 供应商名称 |
| contact_name | VARCHAR(50) | 联系人 |
| phone | VARCHAR(20) | 联系电话 |
| email | VARCHAR(100) | 邮箱 |
| address | VARCHAR(200) | 地址 |
| bank_name | VARCHAR(100) | 开户银行 |
| bank_account | VARCHAR(50) | 银行账号 |
| status | TINYINT | 状态（0合作中 1暂停） |
| 公共字段 | - | 同上 |

### 3.2 采购订单表 pur_order

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| order_no | VARCHAR(30) | 订单编号（唯一） |
| supplier_id | BIGINT | 供应商ID |
| total_amount | DECIMAL(12,2) | 总金额 |
| paid_amount | DECIMAL(12,2) | 已付金额 |
| status | TINYINT | 状态（0待���核 1已审核 2部分入库 3已完成 4已关闭） |
| audit_by | VARCHAR(50) | 审核人 |
| audit_time | DATETIME | 审核时间 |
| 公共字段 | - | 同上 |

### 3.3 采购订单明细表 pur_order_item

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| order_id | BIGINT | 采购订单ID |
| product_id | BIGINT | 商品ID |
| quantity | DECIMAL(12,2) | 采购数量 |
| price | DECIMAL(12,2) | 单价 |
| amount | DECIMAL(12,2) | 金额 |
| received_qty | DECIMAL(12,2) | 已入库数量 |

---

## 4. 库存管理模块（inv_）— 规划中

### 4.1 商品表 inv_product

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| product_code | VARCHAR(30) | 商品编码（唯一） |
| product_name | VARCHAR(100) | 商品名称 |
| category_id | BIGINT | 分类ID |
| spec | VARCHAR(100) | 规格 |
| unit | VARCHAR(20) | 单位 |
| purchase_price | DECIMAL(12,2) | 采购价 |
| sale_price | DECIMAL(12,2) | 销售价 |
| safety_stock | DECIMAL(12,2) | 安全库存量 |
| status | TINYINT | 状态 |
| 公共字段 | - | 同上 |

### 4.2 商品分类表 inv_category

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| category_name | VARCHAR(50) | 分类名称 |
| parent_id | BIGINT | 父分类ID |
| sort | INT | 排序 |

### 4.3 仓库表 inv_warehouse

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| warehouse_name | VARCHAR(50) | 仓库名称 |
| address | VARCHAR(200) | 地址 |
| manager | VARCHAR(50) | 负责人 |
| status | TINYINT | 状态 |

### 4.4 库存表 inv_stock

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| product_id | BIGINT | 商品ID |
| warehouse_id | BIGINT | 仓库ID |
| quantity | DECIMAL(12,2) | 当前库存量 |

### 4.5 出入库记录表 inv_stock_record

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| record_no | VARCHAR(30) | 单据编号 |
| type | TINYINT | 类型（1采购入库 2退货入库 3其他入库 4销售出库 5报损出库 6其他出库） |
| product_id | BIGINT | 商品ID |
| warehouse_id | BIGINT | 仓库ID |
| quantity | DECIMAL(12,2) | 数量 |
| ref_order_no | VARCHAR(30) | 关联单据号 |
| 公共字段 | - | 同上 |

---

## 5. 销售管理模块（sal_）— 规划中

### 5.1 客户表 sal_customer

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| customer_name | VARCHAR(100) | 客户名称 |
| contact_name | VARCHAR(50) | 联系人 |
| phone | VARCHAR(20) | 联系电话 |
| email | VARCHAR(100) | 邮箱 |
| address | VARCHAR(200) | 地址 |
| status | TINYINT | 状态 |
| 公共字段 | - | 同上 |

### 5.2 销售订单表 sal_order

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| order_no | VARCHAR(30) | 订单编号 |
| customer_id | BIGINT | 客户ID |
| total_amount | DECIMAL(12,2) | 总金额 |
| received_amount | DECIMAL(12,2) | 已收金额 |
| status | TINYINT | 状态（0待审核 1已审核 2部分出库 3已完成 4已关闭） |
| 公共字段 | - | 同上 |

### 5.3 销售订单明细表 sal_order_item

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| order_id | BIGINT | 销售订单ID |
| product_id | BIGINT | 商品ID |
| quantity | DECIMAL(12,2) | 销售数量 |
| price | DECIMAL(12,2) | 单价 |
| amount | DECIMAL(12,2) | 金额 |
| shipped_qty | DECIMAL(12,2) | 已出库数量 |

---

## 6. 财务管理模块（fin_）— 规划中

### 6.1 收付款记录表 fin_payment

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| payment_no | VARCHAR(30) | 单据编号 |
| type | TINYINT | 类型（1收款 2付款） |
| target_type | TINYINT | 对象类型（1客户 2供应商） |
| target_id | BIGINT | 对象ID |
| amount | DECIMAL(12,2) | 金额 |
| payment_method | VARCHAR(20) | 支付方式（���金/转账/支票） |
| ref_order_no | VARCHAR(30) | 关联订单号 |
| 公共字段 | - | 同上 |

### 6.2 费用记录表 fin_expense

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| expense_no | VARCHAR(30) | 费用编号 |
| category | VARCHAR(50) | 费用类别（房租/水电/工资/办公等） |
| amount | DECIMAL(12,2) | 金额 |
| expense_date | DATE | 费用日期 |
| description | VARCHAR(200) | 说明 |
| 公共字段 | - | 同上 |

---

## 7. ER关系概览

```
sys_user ──M:N── sys_role ──M:N── sys_menu
    │
    └── sys_dept

pur_supplier ──1:N── pur_order ──1:N── pur_order_item ──→ inv_product
                         │
                         └──→ inv_stock_record ──→ inv_stock

sal_customer ──1:N── sal_order ──1:N── sal_order_item ──→ inv_product
                         │
                         └──→ inv_stock_record ──→ inv_stock

fin_payment ──→ sal_customer / pur_supplier
fin_expense （独立）
```
