# EnterpriseHub ERP System

企业级ERP管理系统，包含销售、采购、库存、财务等核心业务模块。

## 项目结构

```
├── enterprisehub-erp/          # 后端Java项目
│   ├── erp-api/               # API网关模块
│   ├── erp-common/            # 公共模块
│   ├── erp-gateway/           # 网关服务
│   ├── erp-system/            # 系统核心模块
│   └── sql/                   # 数据库脚本
├── enterprisehub-erp-ui/       # 前端Vue项目
└── docs/                      # 项目文档
```

## 技术栈

### 后端
- Java 17
- Spring Boot 3.x
- Spring Security
- MyBatis Plus
- MySQL 8.0
- Maven

### 前端
- Vue 3
- TypeScript
- Vite
- Naive UI
- Pinia

## 快速开始

详细的启动和部署指南请参考 `docs/` 目录下的相关文档：

- [启动指南](启动指南.md)
- [开发计划](开发计划.md)
- [部署指南](docs/deployment-guide.md)
- [生产部署指南](docs/production-deployment-guide.md)

## 功能模块

- **系统管理**：用户、角色、权限、菜单管理
- **销售管理**：客户管理、销售订单
- **采购管理**：供应商管理、采购订单  
- **库存管理**：仓库、商品、库存盘点
- **财务管理**：收支管理、财务报表
- **统计分析**：销售统计、采购统计、财务统计

## 数据库

系统使用MySQL数据库，相关SQL脚本位于 `enterprisehub-erp/sql/` 目录：

- `01_sales_tables.sql` - 销售模块表结构
- `02_purchase_tables.sql` - 采购模块表结构
- `03_inventory_tables.sql` - 库存模块表结构
- `04_finance_tables.sql` - 财务模块表结构

## 开发状态

项目当前处于开发阶段，详细的开发进度和状态请查看：

- [当前状态](docs/current-status.md)
- [角色权限控制](docs/role-based-access-control.md)
- [前端页面总结](docs/frontend-pages-summary.md)

## 许可证

[MIT License](LICENSE)