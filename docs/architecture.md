# EnterpriseHub ERP 系统架构设计

## 1. 架构概述

### 1.1 设计原则
- **轻量实用**：不过度设计，选择适合中小企业的技术方案
- **模块化**：业务模块解耦，可独立开发与维护
- **安全可靠**：JWT认证、RBAC权限、数据加密
- **易于部署**：支持 Docker 一键部署

### 1.2 技术选型

| 层次 | 技术 | 说明 |
|------|------|------|
| 前端框架 | Vue 3 + TypeScript | 响应式UI框架 |
| UI组件库 | Element Plus | 企业级组件库 |
| 状态管理 | Pinia | 轻量状态管理 |
| 构建工具 | Vite 5 | 快速构建 |
| 后端框架 | Spring Boot 3.2 | Java核心框架 |
| ORM | MyBatis-Plus 3.5 | 数据持久层 |
| 安全 | Spring Security + JWT | 认证与授权 |
| 数据库 | MySQL 8.0 | 关系型数据库 |
| 缓存 | Redis 7 | 缓存与会话 |
| API文档 | SpringDoc OpenAPI | 自动生成文档 |
| API网关 | Spring Cloud Gateway | 请求路由与过滤 |
| 容器化 | Docker + Docker Compose | 部署运维 |

---

## 2. 系统架构图

```
┌──────────────────────────────────────────────────┐
│                    客户端                          │
│          浏览器 (Vue 3 + Element Plus)             │
└──────────────────┬───────────────────────────────┘
                   │ HTTP/HTTPS
                   ▼
┌──────────────────────────────────────────────────┐
│              API 网关 (erp-gateway:8080)           │
│         Spring Cloud Gateway                      │
│         路由转发 · 跨域处理                         │
└──────────────────┬───────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────┐
│           业务服务 (erp-system:8082)               │
│                                                    │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ │
│  │系统管理  │ │采购管理  │ │库存管理  │ │销售管理  │ │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘ │
│                  ┌─────────┐                       │
│                  │财务管理  │                       │
│                  └─────────┘                       │
│                                                    │
│  Spring Boot 3 + Spring Security + MyBatis-Plus    │
└────────┬─────────────────────────┬───────────────┘
         │                         │
         ▼                         ▼
┌─────────────────┐    ┌─────────────────┐
│  MySQL 8.0      │    │  Redis 7        │
│  业务数据存储    │    │  缓存 · Token   │
│  端口: 3306     │    │  端口: 6379     │
└─────────────────┘    └─────────────────┘
```

---

## 3. 模块划分

### 3.1 后端模块结构

```
enterprisehub-erp/
├── erp-common/                  # 公共模块
│   ├── erp-common-core          # 核心基础（统一响应、异常处���、分页、常量）
│   ├── erp-common-redis         # Redis 配置
│   ├── erp-common-mybatis       # MyBatis-Plus 配置（分页、自动填充）
│   ├── erp-common-security      # 安全工具（JWT生成/验证、Security上下文）
│   └── erp-common-log           # 操作日志注解（AOP实现）
├── erp-api/                     # 服务间API定义
│   └── erp-api-system           # 系统服务API接口
├── erp-gateway/                 # API网关
└── erp-system/                  # 核心业务服务（系统管理+业务模块）
```

### 3.2 业务服务内部分层

```
erp-system/
├── controller/        # 控制器层 — 接收请求，参数校验，调用Service
├── service/           # 服务层 — 业务逻辑
│   └── impl/          # 服务实现
├── mapper/            # 数据访问层 — MyBatis-Plus Mapper
├── domain/
│   ├── entity/        # 数据库实体
│   ├── dto/           # 数据传输对象（请求参数）
│   └── vo/            # 视图对象（响应数据）
├── config/            # 配置类
└── security/          # 安全相关（JWT过滤器等）
```

### 3.3 前端结构

```
enterprisehub-erp-ui/src/
├── main.ts            # 入口
├── App.vue            # 根组件
├── router/            # 路由配置 + 路由守卫
├── store/             # Pinia 状态管理
├── utils/             # 工具类（Axios封装等）
├── layout/            # 布局组件（侧边栏+顶部导航+内容区）
├── styles/            # 全局样式
├── api/               # 后端API调用封装
├── components/        # 公共组件
└── views/             # 页面视图
    ├── login/         # 登录页
    ├── dashboard/     # 工作台
    ├── system/        # 系统管理（用户/角色/部门）
    ├── purchase/      # 采购管理（供应商/采购订单）
    ├── inventory/     # 库存管理（商品/仓库/出入库）
    ├── sales/         # 销售管理（客户/销售订单）
    └── finance/       # 财务管理（应收/应付/费用/报表）
```

---

## 4. 核心技术设计

### 4.1 认证与授权

**认证流程：**
```
用户登录 → 校验用户名密码 → 生成JWT Token → 返回Token
          → 后续请求携带 Authorization: Bearer <token>
          → JwtAuthenticationFilter 验证Token → 设置SecurityContext
```

**权限模型：** RBAC（基于角色的访问控制）
- 用户 → 角色（多对多）
- 角色 → 菜单/权限（多对多）
- 通过 `@PreAuthorize` 注解控制接口访问

### 4.2 统一响应格式

```json
{
  "code": 200,         // 状态码：200成功，500失败，401未认证
  "msg": "操作成功",    // 提示信息
  "data": {}           // 业务数据
}
```

### 4.3 分页查询

```json
// 请求
{ "pageNum": 1, "pageSize": 10 }

// 响应
{
  "code": 200,
  "data": {
    "list": [],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10
  }
}
```

### 4.4 异常处理

- `BusinessException` — 业务异常（可预期，返回提示信息）
- `GlobalExceptionHandler` — 全局异常捕获（@RestControllerAdvice）
- 统一返回 R 响应格式

### 4.5 操作日志

- 使用自定义注解 `@OperationLog` 标注需要记录日志的接口
- AOP切面自动记录操作人、操作时间、请求参数、执行结果
- 异步写入数据库，不影响接口性能

---

## 5. 数据库设计原则

- 表名使用 `模块前缀_表名` 命名（如 `sys_user`、`pur_supplier`）
- 所有表包含公共字段：`create_by`、`create_time`、`update_by`、`update_time`
- 逻辑删除字段 `del_flag`（0正常、1已删除）
- 主键使用自增ID
- 使用 InnoDB 引擎，UTF-8MB4 字符集

---

## 6. 部署架构

### 6.1 开���环境
- 后端直连本地 MySQL + Redis
- 前端 Vite Dev Server + 代理转发到后端

### 6.2 生产环境（Docker Compose）

```yaml
services:
  mysql:      # MySQL 8.0 数据库
  redis:      # Redis 7 缓存
  erp-server: # Spring Boot 应用
  erp-ui:     # Nginx 托管前端静态资源
```

### 6.3 端口分配

| 服务 | 端口 |
|------|------|
| API网关 | 8080 |
| 业务服务 | 8082 |
| MySQL | 3306 |
| Redis | 6379 |
| 前端(开发) | 3000 |
| 前端(生产) | 80 |

---

## 7. 安全设计

| 安全措施 | 实现方式 |
|----------|----------|
| 认证 | JWT Token（2小时过期） |
| 授权 | RBAC + Spring Security |
| 密码加密 | BCrypt |
| 接口防护 | Spring Security过滤链 |
| SQL注入防护 | MyBatis-Plus参数绑定 |
| XSS防护 | 前端输入过滤 + 后端响应编码 |
| 操作审计 | @OperationLog AOP记录 |
