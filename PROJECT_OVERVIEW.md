# EnterpriseHub ERP 企业资源计划管理系统

## 项目概述

### 项目名称
**EnterpriseHub ERP** - 轻量级企业资源计划管理系统

### 项目定位
面向中小型企业的轻量级ERP解决方案，聚焦企业核心业务流程，集成**采购管理、库存管理、销售管理、财务管理、系统管理**五大核心模块，帮助中小企业实现进销存一体化管理，提升运营效率。

### 设计理念
- **轻量实用** — 不追求大而全，聚焦中小企业实际需求
- **快速部署** — 单体架构，开箱即用，降低运维成本
- **易于扩展** — 模块化设计，可按需添加业务模块
- **操作简单** — 简洁直观的UI，降低学习成本

---

## 技术栈

### 后端技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.2.x | 核心框架 |
| JDK | 17+ | Java版本 |
| MySQL | 8.0+ | 主数据库 |
| Redis | 7.0+ | 缓存 & 会话管理 |
| Spring Security + JWT | - | 安全认证 |
| MyBatis-Plus | 3.5+ | ORM框架 |
| SpringDoc OpenAPI | 3.0 | API文档 |

### 前端技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4+ | 前端框架 |
| Element Plus | 2.6+ | UI组件库 |
| Pinia | 2.1+ | 状态管理 |
| Vue Router | 4.x | 路由管理 |
| Axios | 1.6+ | HTTP客户端 |
| Vite | 5.x | 构建工具 |
| TypeScript | 5.3+ | 类型安全 |
| Sass | 1.71+ | CSS预处理器 |

### 开发 & 运维工具
| 工具 | 用途 |
|------|------|
| Git | 版本控制 |
| Docker + Docker Compose | 容器化部署 |
| Postman / Apifox | 接口测试 |

---

## 项目结构

### 整体目录
```
e:\ERP\
├── PROJECT_OVERVIEW.md              # 项目总览文档
├── docs/                            # 文档目录
│   ├── requirements.md              # 需求分析文档
│   ├── architecture.md              # 系统架构设计
│   ├── database-design.md           # 数据库设计
│   ├── api-documentation.md         # API接口文档
│   ├── frontend-guide.md            # 前端开发规范
│   ├── backend-guide.md             # 后端开发规范
│   └── deployment-guide.md          # 部署运维手册
├── enterprisehub-erp/               # 后端项目（Java/Maven）
│   ├── pom.xml                      # 父POM
│   ├── docker-compose.yml           # Docker编排
│   ├── sql/                         # 数据库脚本
│   │   └── init.sql                 # 初始化SQL
│   ├── erp-common/                  # 公共模块
│   │   ├── erp-common-core/         # 核心工具
│   │   ├── erp-common-redis/        # Redis配置
│   │   ├── erp-common-mybatis/      # MyBatis-Plus配置
│   │   ├── erp-common-security/     # 安全工具
│   │   └── erp-common-log/          # 日志注解
│   ├── erp-api/                     # API接口定义
│   │   └── erp-api-system/          # 系统服务API
│   ├── erp-gateway/                 # API网关
│   └── erp-system/                  # 系统管理服务
└── enterprisehub-erp-ui/            # 前端项目（Vue 3）
    ├── package.json
    ├── vite.config.ts
    ├── index.html
    └── src/
        ├── main.ts
        ├── App.vue
        ├── router/                  # 路由
        ├── store/                   # 状态管理
        ├── utils/                   # 工具类
        ├── layout/                  # 布局组件
        ├── styles/                  # 全局样式
        └── views/                   # 页面视图
```

### 后端模块说明

#### erp-common（公共模块）
| 子模块 | 说明 | 核心类 |
|--------|------|--------|
| erp-common-core | 核心基础 | R、BaseEntity、PageResult、BusinessException���GlobalExceptionHandler、LoginUser、Constants |
| erp-common-redis | Redis配置 | RedisConfig |
| erp-common-mybatis | MyBatis-Plus配置 | MybatisPlusConfig（分页、乐观锁、自动填充） |
| erp-common-security | 安全工具 | JwtUtils、SecurityUtils |
| erp-common-log | 日志注解 | @OperationLog |

#### erp-system（系统管理服务，端口8082）
- 实体：SysUser / SysRole / SysMenu / SysDept
- Mapper层：MyBatis-Plus BaseMapper
- Service层：用户CRUD、登录用户构建
- Controller层：AuthController（登录/登出）、SysUserController（用户CRUD）
- 安全：SecurityConfig + JwtAuthenticationFilter

#### erp-gateway（API网关，端口8080）
- 基于 Spring Cloud Gateway
- 路由转发到 erp-system

---

## 核心功能模块

### 1. 系统管理模块（已实现）
- 用户管理（增删改查、重置密码、状态管理）
- 角色管理（RBAC权限模型）
- 部门管理（树形组织结构）
- 菜单权限管理
- 操作日志审计
- JWT认证与鉴权

### 2. 采购管理模块（规划中）
- 供应商信息管理
- 采购订单管理（创建、审核、跟踪）
- 采购入库管理
- 采购退货管理
- 采购统计报表

### 3. 库存管理模块（规划中）
- 商品/物料管理（分类、规格、单位）
- 仓库管理
- 入库管理（采购入库、退货入库）
- 出库管理（销售出库、报损出库）
- 库存盘点
- 库存预警（安全库存提醒）
- 库存台账与报表

### 4. 销售管理模块（规划中）
- 客户信息管理
- 销售订单管理（创建、审核、发货）
- 销售出库管理
- 销售退货管理
- 收款管理
- 销售统计报表

### 5. 财务管理模块（规划中）
- 应收账款管理
- 应付账款管理
- 收款/付款记录
- 费用管理
- 经营报表（收支汇总、利润统计）

---

## API接口设计

### 认证接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /auth/login | 用户登录 |
| POST | /auth/logout | 用户登出 |
| GET | /auth/info | 获取当前用户信息 |

### 用户管理接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /system/v1/users | 分页查询用户 |
| GET | /system/v1/users/{id} | 用户详情 |
| POST | /system/v1/users | 新增用户 |
| PUT | /system/v1/users/{id} | 修改用户 |
| DELETE | /system/v1/users/{id} | 删除用户 |
| PUT | /system/v1/users/{id}/reset-password | 重置密码 |

### 统一响应格式
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```

---

## 数据库设计

### 系统管理库（erp_system）
| 表名 | 说明 |
|------|------|
| sys_user | 用户表 |
| sys_role | 角色表 |
| sys_menu | 菜单表 |
| sys_dept | 部门表 |
| sys_user_role | 用户-角色关联表 |
| sys_role_menu | 角色-菜单关联表 |
| sys_operation_log | 操作日志表 |

---

## 部署说明

### 服务端口
| 服务 | 端口 | 说明 |
|------|------|------|
| erp-gateway | 8080 | API网关 |
| erp-system | 8082 | 系统管理服务 |
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存 |
| 前端应用 | 80 (生产) / 3000 (开发) | Vue 3 应用 |

### Docker Compose 一键部署
```bash
cd enterprisehub-erp
docker-compose up -d
```

### 开发环境启动
```bash
# 后端
cd enterprisehub-erp
mvn clean install
java -jar erp-system/target/erp-system.jar

# 前端
cd enterprisehub-erp-ui
npm install
npm run dev
```

---

## 项目特色

1. **轻量级架构** — 单体应用，无需复杂的微服务运维
2. **进销存一体化** — 采购→库存→销售→财务业务闭环
3. **开箱即用** — Docker一键部署，快速上线
4. **操作简洁** — Element Plus现代化UI，学习成本低
5. **数据安全** — JWT认证、RBAC权限、操作日志审计
6. **易于扩展** — 模块化设计，可自由添加业务模块

---

## 默认账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 超级管理员 |
| user | admin123 | 普通用户 |

---

## 项目周期

| 阶段 | 时间 | 说明 |
|------|------|------|
| 需求分析 | 1周 | 需求梳理与确认 |
| 架构设计 | 1周 | 技术选型与架构设计 |
| 系统管理模块 | 2周 | 用户/角色/部门/菜单/认证 |
| 采购管理模块 | 3周 | 供应商、采购订单、入库 |
| 库存管理模块 | 3周 | 商品、仓库、出入库、盘点 |
| 销售管理模块 | 3周 | 客户、销售订单、出库 |
| 财务管理模块 | 2周 | 收付款、费用、报表 |
| 测试与优化 | 2周 | 集成测试、性能优化 |
| 上线部署 | 1周 | 部署与培训 |
| **总计** | **约4个月** | |

### 团队配置
| 角色 | 人数 |
|------|------|
| 项目经理 | 1人 |
| 后端开发 | 2人 |
| 前端开发 | 1人 |
| 测试工程师 | 1人 |
| **总计** | **5人** |

---

## 相关文档

- [需求分析文档](./docs/requirements.md)
- [系统架构设计](./docs/architecture.md)
- [数据库设计](./docs/database-design.md)
- [API接口文档](./docs/api-documentation.md)
- [前端开发规范](./docs/frontend-guide.md)
- [后端开发规范](./docs/backend-guide.md)
- [部署运维手册](./docs/deployment-guide.md)
