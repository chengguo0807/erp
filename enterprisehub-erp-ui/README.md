# EnterpriseHub ERP 前端项目

基于 Vue 3 + TypeScript + Element Plus 的企业资源计划管理系统前端。

## 技术栈

- **框架**: Vue 3.4
- **语言**: TypeScript 5.3
- **UI 组件库**: Element Plus 2.6
- **状态管理**: Pinia 2.1
- **路由**: Vue Router 4.3
- **HTTP 客户端**: Axios 1.6
- **构建工具**: Vite 5.1
- **CSS 预处理器**: Sass 1.71

## 项目结构

```
enterprisehub-erp-ui/
├── public/              # 静态资源
├── src/
│   ├── api/            # API 接口
│   ├── assets/         # 资源文件
│   ├── components/     # 公共组件
│   ├── layout/         # 布局组件
│   ├── router/         # 路由配置
│   ├── store/          # 状态管理
│   ├── styles/         # 全局样式
│   ├── types/          # TypeScript 类型定义
│   ├── utils/          # 工具函数
│   ├── views/          # 页面组件
│   ├── App.vue         # 根组件
│   ├── main.ts         # 入口文件
│   └── env.d.ts        # 环境变量类型声明
├── index.html          # HTML 模板
├── package.json        # 项目配置
├── tsconfig.json       # TypeScript 配置
├── vite.config.ts      # Vite 配置
└── README.md           # 项目说明
```

## 开发指南

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:3000

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

### 代码检查

```bash
npm run lint
```

## 功能模块

### 已实现

- ✅ 用户登录/登出
- ✅ 路由守卫
- ✅ 权限控制
- ✅ 用户管理
- ✅ 角色管理
- ✅ 部门管理
- ✅ 工作台

### 待开发

- ⏳ 菜单管理
- ⏳ 采购管理
- ⏳ 库存管理
- ⏳ 销售管理
- ⏳ 财务管理

## 配置说明

### API 代理配置

在 `vite.config.ts` 中配置后端 API 代理：

```typescript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8082',
      changeOrigin: true,
      rewrite: (path) => path.replace(/^\/api/, ''),
    },
  },
}
```

### 环境变量

创建 `.env.development` 和 `.env.production` 文件配置不同环境的变量。

## 默认账号

- 用户名: `admin`
- 密码: `admin123`

## 浏览器支持

- Chrome >= 87
- Firefox >= 78
- Safari >= 14
- Edge >= 88

## 开发规范

### 命名规范

- 组件文件名：PascalCase（如 `UserList.vue`）
- 工具函数文件名：camelCase（如 `formatDate.ts`）
- 常量文件名：UPPER_CASE（如 `API_CONFIG.ts`）

### 代码风格

- 使用 2 空格缩进
- 使用单引号
- 语句末尾不加分号
- 使用 ES6+ 语法

### Git 提交规范

```
feat: 新功能
fix: 修复 bug
docs: 文档更新
style: 代码格式调整
refactor: 代码重构
test: 测试相关
chore: 构建/工具链相关
```

## 常见问题

### Q: 启动时端口被占用？

A: 修改 `vite.config.ts` 中的 `server.port` 配置。

### Q: API 请求失败？

A: 检查后端服务是否启动，以及代理配置是否正确。

### Q: 页面空白？

A: 打开浏览器控制台查看错误信息，检查路由配置。

## 相关文档

- [Vue 3 文档](https://cn.vuejs.org/)
- [Element Plus 文档](https://element-plus.org/zh-CN/)
- [Vite 文档](https://cn.vitejs.dev/)
- [TypeScript 文档](https://www.typescriptlang.org/zh/)

## License

MIT
