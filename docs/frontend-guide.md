# EnterpriseHub ERP 前端开发规范

## 1. 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4+ | 前端框架（Composition API） |
| TypeScript | 5.3+ | 类型安全 |
| Element Plus | 2.6+ | UI组件库 |
| Pinia | 2.1+ | 状态管理 |
| Vue Router | 4.x | 路由管理 |
| Axios | 1.6+ | HTTP请求 |
| Vite | 5.x | 构建工具 |
| Sass | 1.71+ | CSS预处理器 |

---

## 2. 项目结构

```
src/
├── main.ts                # 入口文件
├── App.vue                # 根组件
├── env.d.ts               # 类型声明
├── router/
│   └── index.ts           # 路由配置 + 路由守卫
├── store/
│   └── user.ts            # 用户状态管理
├── utils/
│   └── request.ts         # Axios封装（拦截器、Token注入）
├── api/                   # 后端API封装（按模块拆分）
│   ├── auth.ts            # 认证接口
│   ├── system.ts          # 系统管理接口
│   ├── purchase.ts        # 采购管理接口
│   └── ...
├── components/            # 公共组件
├── layout/
│   └── index.vue          # 主布局（侧边栏 + 顶部导航 + 内容区）
├── styles/
│   └── index.scss         # 全局样式
└── views/                 # 页面视图
    ├── login/index.vue    # 登录页
    ├── dashboard/index.vue # 工作台
    ├── system/            # 系统管理
    │   ├── user/index.vue
    │   ├── role/index.vue
    │   └── dept/index.vue
    ├── purchase/          # 采购管理（规划中）
    ├── inventory/         # 库存管理（规划中）
    ├── sales/             # 销售管理（规划中）
    └── finance/           # 财务管理（规划中）
```

---

## 3. 编码规范

### 3.1 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 文件名 | 小写短横线 | `user-list.vue`、`index.vue` |
| 组件名 | 大驼峰 | `UserList`、`SearchForm` |
| 变量/函数 | 小驼峰 | `userName`、`handleSubmit` |
| 常量 | 全大写+下划线 | `API_BASE_URL` |
| CSS类名 | BEM或短横线 | `user-management`、`stat-card` |

### 3.2 Vue 组件规范

```vue
<template>
  <div class="user-management">
    <!-- 模板内容 -->
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

// ==================== 类型定义 ====================
interface UserItem {
  id: number
  username: string
  nickname: string
  status: number
}

// ==================== 响应式数据 ====================
const tableData = ref<UserItem[]>([])
const loading = ref(false)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  username: '',
})

// ==================== 方法 ====================
const handleQuery = async () => {
  loading.value = true
  try {
    const res = await request.get('/system/v1/users', { params: queryParams })
    tableData.value = res.data.list
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  // ...
}

// ==================== 生命周期 ====================
onMounted(() => {
  handleQuery()
})
</script>

<style scoped lang="scss">
.user-management {
  // ...
}
</style>
```

**规则**：
- 使用 `<script setup lang="ts">` 语法
- 使用 `scoped` 限定样式作用域
- 代码块按 类型定义 → 响应式数据 → 方法 → 生命周期 顺序组织

### 3.3 API 封装规范

```typescript
// api/system.ts
import request from '@/utils/request'

// 用户管理API
export function getUserList(params: any) {
  return request.get('/system/v1/users', { params })
}

export function createUser(data: any) {
  return request.post('/system/v1/users', data)
}

export function updateUser(id: number, data: any) {
  return request.put(`/system/v1/users/${id}`, data)
}

export function deleteUser(id: number) {
  return request.delete(`/system/v1/users/${id}`)
}
```

### 3.4 Axios 封装

```typescript
// utils/request.ts
const service = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

// 请求拦截器：自动注入Token
service.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：统一错误处理
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.msg || '请求失败')
      if (res.code === 401) {
        // Token过期，跳转登录
      }
      return Promise.reject(res)
    }
    return res
  },
  error => {
    ElMessage.error('网络异常')
    return Promise.reject(error)
  }
)
```

---

## 4. 路由规范

```typescript
// router/index.ts
const routes = [
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '工作台', icon: 'Odometer' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    meta: { title: '系统管理', icon: 'Setting' },
    children: [
      {
        path: 'user',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理' }
      }
    ]
  }
]

// 路由守卫：Token校验
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
  } else {
    next()
  }
})
```

---

## 5. 状态管理规范

```typescript
// store/user.ts
export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null as any,
  }),
  actions: {
    async login(username: string, password: string) {
      const res = await request.post('/auth/login', { username, password })
      this.token = res.data.token
      localStorage.setItem('token', this.token)
    },
    logout() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
    }
  }
})
```

**规则**：
- 只在需要跨组件共享的状态才使用 Pinia
- 页面内的局部状态使用 `ref/reactive`

---

## 6. UI 组件使用规范

### 表格页面标准结构

```
┌─────────────────────────────────────────────────┐
│  搜索栏（el-form inline）                        │
├─────────────────────────────────────────────────┤
│  操作栏（新增/批量删除/导出 按钮）                 │
├─────────────────────────────────────────────────┤
│  数据表格（el-table + stripe + border）           │
├─────────────────────────────────────────────────┤
│  分页（el-pagination，靠右对齐）                  │
└─────────────────────────────────────────────────┘
```

### 表单弹窗

- 新增/编辑使用 `el-dialog` + `el-form`
- 使用 `el-form` 的 rules 进行表单验证
- 提交后关闭弹窗并刷新列表

---

## 7. Git 提交规范

```
feat: 新增用户管理页面
fix: 修复登录页样式问题
style: 调整表格列宽
refactor: 重构API封装
docs: 更新前端开发规范
```
