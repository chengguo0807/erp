# EnterpriseHub ERP API接口文档

## 1. 概述

- **基础路径**：`http://localhost:8080`（通过网关访问）
- **认证方式**：JWT Bearer Token，在请求头中携带 `Authorization: Bearer <token>`
- **数据格式**：JSON（Content-Type: application/json）

### 统一响应格式

```json
{
  "code": 200,       // 200成功，500失败，401未认证，403无权限
  "msg": "操作成功",
  "data": {}
}
```

### 分页响应格式

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "list": [],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10
  }
}
```

---

## 2. 认证接口

### 2.1 用户登录

**POST** `/auth/login`

请求体：
```json
{
  "username": "admin",
  "password": "admin123"
}
```

响应：
```json
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

### 2.2 用户登出

**POST** `/auth/logout`

请求头：`Authorization: Bearer <token>`

响应：
```json
{
  "code": 200,
  "msg": "登出成功"
}
```

### 2.3 获取当前用户信息

**GET** `/auth/info`

请求头：`Authorization: Bearer <token>`

响应：
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "roles": ["admin"],
    "permissions": ["*:*:*"]
  }
}
```

---

## 3. 系统管理接口

### 3.1 用户管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/system/v1/users` | 分页查询用户列表 |
| GET | `/system/v1/users/{id}` | 查询用户详情 |
| POST | `/system/v1/users` | 新增用户 |
| PUT | `/system/v1/users/{id}` | 修改用户 |
| DELETE | `/system/v1/users/{id}` | 删除用户 |
| PUT | `/system/v1/users/{id}/reset-password` | 重置密码 |

#### 分页查询用户

**GET** `/system/v1/users?pageNum=1&pageSize=10&username=admin&status=0`

#### 新增用户

**POST** `/system/v1/users`

```json
{
  "username": "zhangsan",
  "password": "123456",
  "nickname": "张三",
  "realName": "张三",
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "deptId": 102,
  "roleIds": [2]
}
```

### 3.2 角色管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/system/v1/roles` | 查询角色列表 |
| GET | `/system/v1/roles/{id}` | 查询角色详情 |
| POST | `/system/v1/roles` | 新增角色 |
| PUT | `/system/v1/roles/{id}` | 修改角色 |
| DELETE | `/system/v1/roles/{id}` | 删除角色 |

### 3.3 部门管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/system/v1/depts` | 查询部门树 |
| GET | `/system/v1/depts/{id}` | 查询部门详情 |
| POST | `/system/v1/depts` | 新增部门 |
| PUT | `/system/v1/depts/{id}` | 修改部门 |
| DELETE | `/system/v1/depts/{id}` | 删除部门 |

### 3.4 菜单管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/system/v1/menus` | 查询菜单树 |
| POST | `/system/v1/menus` | 新增菜单 |
| PUT | `/system/v1/menus/{id}` | 修改菜单 |
| DELETE | `/system/v1/menus/{id}` | 删除菜单 |

---

## 4. 采购管理接口（规划中）

### 4.1 供应商管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/purchase/v1/suppliers` | 分页查询供应商 |
| POST | `/purchase/v1/suppliers` | 新增供应商 |
| PUT | `/purchase/v1/suppliers/{id}` | 修改供应商 |
| DELETE | `/purchase/v1/suppliers/{id}` | 删除供应商 |

### 4.2 采购订单管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/purchase/v1/orders` | 分页查询采购订单 |
| GET | `/purchase/v1/orders/{id}` | 采购订单详情 |
| POST | `/purchase/v1/orders` | 创建采购订单 |
| PUT | `/purchase/v1/orders/{id}` | 修改采购订单 |
| PUT | `/purchase/v1/orders/{id}/audit` | 审核采购订单 |
| PUT | `/purchase/v1/orders/{id}/receive` | 采购入库 |

---

## 5. 库存管理接口（规划中）

### 5.1 商品管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/inventory/v1/products` | 分页查询商品 |
| POST | `/inventory/v1/products` | 新增商品 |
| PUT | `/inventory/v1/products/{id}` | 修改商品 |
| DELETE | `/inventory/v1/products/{id}` | 删除商品 |
| POST | `/inventory/v1/products/import` | 导入商品(Excel) |
| GET | `/inventory/v1/products/export` | 导出商品(Excel) |

### 5.2 库存查询

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/inventory/v1/stocks` | 查询库存列表 |
| GET | `/inventory/v1/stocks/warning` | 库存预警列表 |
| GET | `/inventory/v1/stock-records` | 出入库流水 |

---

## 6. 销售管理接口（规划中）

### 6.1 客户管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/sales/v1/customers` | 分页查询客户 |
| POST | `/sales/v1/customers` | 新增客户 |
| PUT | `/sales/v1/customers/{id}` | 修改客户 |
| DELETE | `/sales/v1/customers/{id}` | 删除客户 |

### 6.2 销售订单管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/sales/v1/orders` | 分页查询销售订单 |
| GET | `/sales/v1/orders/{id}` | 销售订单详情 |
| POST | `/sales/v1/orders` | 创建销售订单 |
| PUT | `/sales/v1/orders/{id}/audit` | 审核销售订单 |
| PUT | `/sales/v1/orders/{id}/ship` | 销售出库 |

---

## 7. 财务管理接口（规划中）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/finance/v1/receivables` | 应收账款汇总 |
| GET | `/finance/v1/payables` | 应付账款汇总 |
| POST | `/finance/v1/payments` | 登记收款/付款 |
| GET | `/finance/v1/payments` | 收付款记录 |
| POST | `/finance/v1/expenses` | 录入费用 |
| GET | `/finance/v1/expenses` | 费用列表 |
| GET | `/finance/v1/reports/summary` | 收支汇总报表 |
| GET | `/finance/v1/reports/profit` | 利润报表 |

---

## 8. 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 401 | 未认证（Token缺失或过期） |
| 403 | 无权限 |
| 500 | 系统错误 |
| 1001 | 用户名或密码错误 |
| 1002 | 账号已停用 |
| 1003 | 数据不存在 |
| 1004 | 数据已存在（重复） |
| 1005 | 参数校验失败 |
