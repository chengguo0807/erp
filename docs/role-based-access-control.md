# 基于角色的访问控制（RBAC）实现文档

## 概述
系统实现了基于角色的访问控制（Role-Based Access Control, RBAC），不同角色的用户登录后会看到不同的菜单和功能。

## 角色定义

### 1. 管理员角色
- **admin**: 超级管理员，拥有所有权限
- **system_admin**: 系统管理员，可以管理用户、角色、部门等

### 2. 销售角色
- **sales_manager**: 销售经理，可以管理销售订单、客户、查看销售统计
- **sales_staff**: 销售员工，可以处理销售订单和客户信息

### 3. 采购角色
- **purchase_manager**: 采购经理，可以管理采购订单、供应商、查看采购统计
- **purchase_staff**: 采购员工，可以处理采购订单和供应商信息

### 4. 仓库角色
- **warehouse_manager**: 仓库经理，可以管理仓库、产品、库存盘点等
- **warehouse_staff**: 仓库员工，可以处理入库、出库、库存查询等

### 5. 财务角色
- **finance_manager**: 财务经理，可以管理收付款、费用、查看所有财务统计
- **finance_staff**: 财务员工，可以处理收付款和费用管理

## 菜单权限配置

### 工作台
- 所有角色都可以访问

### 系统管理
- **用户管理**: admin, system_admin
- **角色管理**: admin, system_admin
- **部门管理**: admin, system_admin
- **菜单管理**: admin（仅超级管理员）
- **操作日志**: admin, system_admin

### 销售管理
- **销售订单**: admin, sales_manager, sales_staff
- **客户管理**: admin, sales_manager, sales_staff

### 采购管理
- **采购订单**: admin, purchase_manager, purchase_staff
- **供应商管理**: admin, purchase_manager, purchase_staff
- **入库管理**: admin, purchase_manager, warehouse_staff

### 库存管理
- **产品管理**: admin, warehouse_manager, warehouse_staff
- **产品分类**: admin, warehouse_manager
- **仓库管理**: admin, warehouse_manager
- **库存查询**: admin, warehouse_manager, warehouse_staff, sales_staff, purchase_staff
- **出库管理**: admin, warehouse_manager, warehouse_staff
- **库存盘点**: admin, warehouse_manager, warehouse_staff
- **库存流水**: admin, warehouse_manager, warehouse_staff

### 财务管理
- **收付款管理**: admin, finance_manager, finance_staff
- **费用管理**: admin, finance_manager, finance_staff

### 统计报表
- **销售统计**: admin, sales_manager, finance_manager
- **采购统计**: admin, purchase_manager, finance_manager
- **财务统计**: admin, finance_manager

## 实现细节

### 前端实现

#### 1. 菜单配置（src/router/menus.ts）
```typescript
export interface MenuConfig {
  label: string
  key: string
  icon?: any
  children?: MenuConfig[]
  roles?: string[] // 允许访问的角色
}
```

#### 2. 动态菜单生成
- 用户登录后，系统根据用户的角色信息动态生成菜单
- 使用`filterMenusByRoles`函数过滤菜单项
- 只显示用户有权限访问的菜单

#### 3. 路由守卫
- 在路由跳转前检查用户是否有权限访问目标页面
- 如果没有权限，跳转到403页面
- 路由meta中配置roles字段来指定需要的角色

### 后端实现

#### 1. 用户角色查询
- 登录时查询用户的角色信息
- 通过`sys_user_role`和`sys_role`表关联查询
- 返回角色标识（roleKey）集合

#### 2. 角色数据结构
```java
public class LoginUser {
    private Long userId;
    private String username;
    private String nickname;
    private Set<String> roles;      // 角色标识集合
    private Set<String> permissions; // 权限标识集合
}
```

## 测试场景

### 场景1：管理员登录
- 用户名：admin
- 角色：admin
- 可见菜单：所有菜单

### 场景2：销售经理登录
- 角色：sales_manager
- 可见菜单：
  - 工作台
  - 销售管理（销售订单、客户管理）
  - 库存管理（库存查询）
  - 统计报表（销售统计）

### 场景3：仓库员工登录
- 角色：warehouse_staff
- 可见菜单：
  - 工作台
  - 采购管理（入库管理）
  - 库存管理（产品管理、库存查询、出库管理、库存盘点、库存流水）

### 场景4：财务员工登录
- 角色：finance_staff
- 可见菜单：
  - 工作台
  - 财务管理（收付款管理、费用管理）

## 数据库配置

### 1. 角色表（sys_role）
需要在数据库中创建对应的角色记录：

```sql
INSERT INTO sys_role (role_name, role_key, sort, status) VALUES
('超级管理员', 'admin', 1, 0),
('系统管理员', 'system_admin', 2, 0),
('销售经理', 'sales_manager', 3, 0),
('销售员工', 'sales_staff', 4, 0),
('采购经理', 'purchase_manager', 5, 0),
('采购员工', 'purchase_staff', 6, 0),
('仓库经理', 'warehouse_manager', 7, 0),
('仓库员工', 'warehouse_staff', 8, 0),
('财务经理', 'finance_manager', 9, 0),
('财务员工', 'finance_staff', 10, 0);
```

### 2. 用户角色关联（sys_user_role）
为用户分配角色：

```sql
-- 为admin用户分配admin角色
INSERT INTO sys_user_role (user_id, role_id) 
SELECT u.id, r.id 
FROM sys_user u, sys_role r 
WHERE u.username = 'admin' AND r.role_key = 'admin';
```

## 扩展说明

### 添加新角色
1. 在数据库`sys_role`表中添加新角色记录
2. 在`src/router/menus.ts`中更新菜单配置，添加新角色到相应菜单的roles数组
3. 在`src/router/index.ts`中更新路由配置，添加新角色到相应路由的meta.roles数组

### 添加新菜单
1. 在`src/router/menus.ts`中添加新菜单配置
2. 在`src/router/index.ts`中添加新路由
3. 创建对应的Vue组件
4. 配置菜单的roles字段，指定哪些角色可以访问

## 注意事项

1. **角色标识一致性**：前端配置的角色标识必须与数据库中的`role_key`字段一致
2. **权限检查**：前端的权限检查只是UI层面的控制，后端API也需要进行权限验证
3. **角色更新**：如果用户的角色发生变化，需要重新登录才能生效
4. **默认角色**：新用户创建时应该分配默认角色，避免无法访问任何功能

## 修改时间
2026-03-10
