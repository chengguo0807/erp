# Element Plus 到 Naive UI 迁移指南

## 概述

本文档提供从 Element Plus 迁移到 Naive UI 的完整指南，包括所有需要修改的文件和代码示例。

---

## 1. 依赖更新

### 已完成
✅ package.json 已更新为 Naive UI

### 安装依赖
```bash
cd enterprisehub-erp-ui
npm install
```

---

## 2. 主要差异对比

### 组件导入方式

**Element Plus**:
```typescript
import { ElMessage, ElMessageBox } from 'element-plus'
```

**Naive UI**:
```typescript
import { useMessage, useDialog } from 'naive-ui'
// 在 setup 中使用
const message = useMessage()
const dialog = useDialog()
```

### 组件命名对比

| Element Plus | Naive UI | 说明 |
|-------------|----------|------|
| el-button | n-button | 按钮 |
| el-input | n-input | 输入框 |
| el-form | n-form | 表单 |
| el-table | n-data-table | 表格 |
| el-pagination | n-pagination | 分页 |
| el-dialog | n-modal | 对话框 |
| el-card | n-card | 卡片 |
| el-menu | n-menu | 菜单 |
| el-dropdown | n-dropdown | 下拉菜单 |
| el-tag | n-tag | 标签 |
| el-avatar | n-avatar | 头像 |
| el-icon | n-icon | 图标 |
| el-breadcrumb | n-breadcrumb | 面包屑 |
| el-container | n-layout | 布局 |
| el-aside | n-layout-sider | 侧边栏 |
| el-header | n-layout-header | 顶栏 |
| el-main | n-layout-content | 内容区 |

---

## 3. 文件修改清单

### 3.1 src/main.ts

**修改前（Element Plus）**:
```typescript
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import App from './App.vue'
import router from './router'
import './styles/index.scss'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

app.mount('#app')
```

**修改后（Naive UI）**:
```typescript
import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import './styles/index.scss'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
```

**说明**: Naive UI 不需要全局注册，组件按需导入即可。

---

### 3.2 vite.config.ts

**修改前**:
```typescript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
      imports: ['vue', 'vue-router', 'pinia'],
      dts: 'src/auto-imports.d.ts',
    }),
    Components({
      resolvers: [ElementPlusResolver()],
      dts: 'src/components.d.ts',
    }),
  ],
  // ...
})
```

**修改后**:
```typescript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { NaiveUiResolver } from 'unplugin-vue-components/resolvers'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      imports: [
        'vue',
        'vue-router',
        'pinia',
        {
          'naive-ui': [
            'useDialog',
            'useMessage',
            'useNotification',
            'useLoadingBar'
          ]
        }
      ],
      dts: 'src/auto-imports.d.ts',
    }),
    Components({
      resolvers: [NaiveUiResolver()],
      dts: 'src/components.d.ts',
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 3000,
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})
```

---

### 3.3 src/App.vue

**修改后**:
```vue
<template>
  <n-config-provider :theme="theme">
    <n-message-provider>
      <n-dialog-provider>
        <n-notification-provider>
          <n-loading-bar-provider>
            <router-view />
          </n-loading-bar-provider>
        </n-notification-provider>
      </n-dialog-provider>
    </n-message-provider>
  </n-config-provider>
</template>

<script setup lang="ts">
import { NConfigProvider, NMessageProvider, NDialogProvider, NNotificationProvider, NLoadingBarProvider } from 'naive-ui'
import { computed } from 'vue'

// 可以根据需要切换主题
const theme = computed(() => null) // null 为亮色主题
</script>

<style>
html, body, #app {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>
```

---

### 3.4 src/views/login/index.vue

**完整代码**:
```vue
<template>
  <div class="login-container">
    <n-card class="login-card" title="EnterpriseHub ERP" :bordered="false">
      <template #header-extra>
        <span class="subtitle">企业资源计划管理系统</span>
      </template>
      
      <n-form
        ref="formRef"
        :model="loginForm"
        :rules="rules"
        size="large"
      >
        <n-form-item path="username">
          <n-input
            v-model:value="loginForm.username"
            placeholder="用户名"
            @keydown.enter="handleLogin"
          >
            <template #prefix>
              <n-icon :component="PersonOutline" />
            </template>
          </n-input>
        </n-form-item>
        
        <n-form-item path="password">
          <n-input
            v-model:value="loginForm.password"
            type="password"
            show-password-on="click"
            placeholder="密码"
            @keydown.enter="handleLogin"
          >
            <template #prefix>
              <n-icon :component="LockClosedOutline" />
            </template>
          </n-input>
        </n-form-item>
        
        <n-form-item>
          <n-button
            type="primary"
            :loading="loading"
            block
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </n-button>
        </n-form-item>
      </n-form>
      
      <n-text depth="3" style="font-size: 12px; text-align: center; display: block;">
        默认账号：admin / admin123
      </n-text>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useMessage } from 'naive-ui'
import { PersonOutline, LockClosedOutline } from '@vicons/ionicons5'
import type { FormInst, FormRules } from 'naive-ui'
import { useUserStore } from '@/store/user'

const router = useRouter()
const route = useRoute()
const message = useMessage()
const userStore = useUserStore()

const formRef = ref<FormInst | null>(null)
const loading = ref(false)

const loginForm = reactive({
  username: 'admin',
  password: 'admin123',
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    await userStore.login(loginForm.username, loginForm.password)
    message.success('登录成功')
    
    const redirect = (route.query.redirect as string) || '/'
    router.push(redirect)
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 420px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  
  .subtitle {
    font-size: 14px;
    color: #999;
  }
}
</style>
```

---

### 3.5 src/layout/index.vue

**完整代码**（由于篇幅限制，这里提供关键部分）:

```vue
<template>
  <n-layout has-sider style="height: 100vh">
    <n-layout-sider
      bordered
      :collapsed="collapsed"
      collapse-mode="width"
      :collapsed-width="64"
      :width="220"
      show-trigger
      @collapse="collapsed = true"
      @expand="collapsed = false"
    >
      <div class="logo">
        <n-icon :size="32" :component="BusinessOutline" color="#18a058" />
        <span v-show="!collapsed" class="logo-title">EnterpriseHub ERP</span>
      </div>
      
      <n-menu
        v-model:value="activeKey"
        :collapsed="collapsed"
        :collapsed-width="64"
        :collapsed-icon-size="22"
        :options="menuOptions"
        @update:value="handleMenuSelect"
      />
    </n-layout-sider>
    
    <n-layout>
      <n-layout-header bordered style="height: 60px; padding: 0 20px; display: flex; align-items: center; justify-content: space-between;">
        <n-breadcrumb>
          <n-breadcrumb-item>首页</n-breadcrumb-item>
          <n-breadcrumb-item v-if="$route.meta.title">{{ $route.meta.title }}</n-breadcrumb-item>
        </n-breadcrumb>
        
        <n-dropdown :options="userOptions" @select="handleUserAction">
          <n-button text>
            <n-avatar round :size="28">
              <n-icon :component="PersonOutline" />
            </n-avatar>
            <span style="margin-left: 8px;">{{ userStore.username || 'Admin' }}</span>
          </n-button>
        </n-dropdown>
      </n-layout-header>
      
      <n-layout-content content-style="padding: 16px; background: #f0f2f5;">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </n-layout-content>
    </n-layout>
  </n-layout>
</template>

<script setup lang="ts">
import { ref, computed, h } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useDialog, useMessage, NIcon } from 'naive-ui'
import { 
  BusinessOutline, 
  PersonOutline, 
  HomeOutline,
  SettingsOutline,
  PeopleOutline,
  ShieldOutline,
  BusinessSharp,
  LogOutOutline
} from '@vicons/ionicons5'
import { useUserStore } from '@/store/user'
import type { MenuOption } from 'naive-ui'

const router = useRouter()
const route = useRoute()
const dialog = useDialog()
const message = useMessage()
const userStore = useUserStore()

const collapsed = ref(false)
const activeKey = ref(route.path)

// 渲染图标
const renderIcon = (icon: any) => {
  return () => h(NIcon, null, { default: () => h(icon) })
}

// 菜单选项
const menuOptions: MenuOption[] = [
  {
    label: '工作台',
    key: '/dashboard',
    icon: renderIcon(HomeOutline)
  },
  {
    label: '系统管理',
    key: '/system',
    icon: renderIcon(SettingsOutline),
    children: [
      {
        label: '用户管理',
        key: '/system/user',
        icon: renderIcon(PeopleOutline)
      },
      {
        label: '角色管理',
        key: '/system/role',
        icon: renderIcon(ShieldOutline)
      },
      {
        label: '部门管理',
        key: '/system/dept',
        icon: renderIcon(BusinessSharp)
      }
    ]
  }
]

// 用户下拉菜单
const userOptions = [
  {
    label: '退出登录',
    key: 'logout',
    icon: renderIcon(LogOutOutline)
  }
]

const handleMenuSelect = (key: string) => {
  router.push(key)
}

const handleUserAction = (key: string) => {
  if (key === 'logout') {
    dialog.warning({
      title: '提示',
      content: '确认退出登录吗？',
      positiveText: '确认',
      negativeText: '取消',
      onPositiveClick: async () => {
        await userStore.logoutAction()
        message.success('已退出登录')
        router.push('/login')
      }
    })
  }
}
</script>

<style scoped lang="scss">
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 16px;
  
  .logo-title {
    color: #333;
    font-size: 16px;
    font-weight: 600;
    margin-left: 10px;
    white-space: nowrap;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
```

---

### 3.6 src/views/system/user/index.vue

**完整代码**:

```vue
<template>
  <n-card title="用户管理" :bordered="false">
    <n-space vertical :size="16">
      <!-- 搜索栏 -->
      <n-form inline :model="queryParams">
        <n-form-item label="用户名">
          <n-input v-model:value="queryParams.username" placeholder="请输入用户名" clearable />
        </n-form-item>
        <n-form-item label="状态">
          <n-select
            v-model:value="queryParams.status"
            placeholder="请选择状态"
            clearable
            :options="statusOptions"
            style="width: 120px"
          />
        </n-form-item>
        <n-form-item>
          <n-space>
            <n-button type="primary" @click="handleQuery">
              <template #icon>
                <n-icon :component="SearchOutline" />
              </template>
              搜索
            </n-button>
            <n-button @click="handleReset">
              <template #icon>
                <n-icon :component="RefreshOutline" />
              </template>
              重置
            </n-button>
          </n-space>
        </n-form-item>
      </n-form>

      <!-- 操作栏 -->
      <n-space>
        <n-button type="primary" @click="handleAdd">
          <template #icon>
            <n-icon :component="AddOutline" />
          </template>
          新增用户
        </n-button>
      </n-space>

      <!-- 表格 -->
      <n-data-table
        :columns="columns"
        :data="tableData"
        :loading="loading"
        :pagination="pagination"
        :bordered="false"
      />
    </n-space>
  </n-card>
</template>

<script setup lang="ts">
import { ref, reactive, h } from 'vue'
import { useMessage, useDialog, NButton, NTag, NSpace } from 'naive-ui'
import { SearchOutline, RefreshOutline, AddOutline, CreateOutline, TrashOutline } from '@vicons/ionicons5'
import request from '@/utils/request'
import type { DataTableColumns } from 'naive-ui'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const tableData = ref([])

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  username: '',
  status: null as number | null,
})

const statusOptions = [
  { label: '正常', value: 0 },
  { label: '停用', value: 1 }
]

const columns: DataTableColumns = [
  { title: '用户名', key: 'username', width: 120 },
  { title: '昵称', key: 'nickname', width: 120 },
  { title: '真实姓名', key: 'realName', width: 120 },
  { title: '手机号', key: 'phone', width: 130 },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render(row: any) {
      return h(
        NTag,
        { type: row.status === 0 ? 'success' : 'error' },
        { default: () => row.status === 0 ? '正常' : '停用' }
      )
    }
  },
  { title: '创建时间', key: 'createTime', width: 180 },
  {
    title: '操作',
    key: 'actions',
    width: 200,
    render(row: any) {
      return h(
        NSpace,
        {},
        {
          default: () => [
            h(
              NButton,
              {
                size: 'small',
                type: 'primary',
                text: true,
                onClick: () => handleEdit(row)
              },
              { default: () => '编辑' }
            ),
            h(
              NButton,
              {
                size: 'small',
                type: 'error',
                text: true,
                onClick: () => handleDelete(row)
              },
              { default: () => '删除' }
            )
          ]
        }
      )
    }
  }
]

const pagination = reactive({
  page: 1,
  pageSize: 10,
  showSizePicker: true,
  pageSizes: [10, 20, 50],
  onChange: (page: number) => {
    queryParams.pageNum = page
    handleQuery()
  },
  onUpdatePageSize: (pageSize: number) => {
    queryParams.pageSize = pageSize
    queryParams.pageNum = 1
    handleQuery()
  }
})

const handleQuery = async () => {
  loading.value = true
  try {
    const res: any = await request.get('/system/v1/users', { params: queryParams })
    tableData.value = res.data.list
    pagination.itemCount = res.data.total
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.username = ''
  queryParams.status = null
  queryParams.pageNum = 1
  handleQuery()
}

const handleAdd = () => {
  message.info('新增用户功能 - 待实现')
}

const handleEdit = (row: any) => {
  message.info(`编辑用户: ${row.username}`)
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: `确认删除用户"${row.username}"？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await request.delete(`/system/v1/users/${row.id}`)
      message.success('删除成功')
      handleQuery()
    }
  })
}

// 初始化
handleQuery()
</script>
```

---

## 4. 其他文件修改

### 4.1 src/views/dashboard/index.vue
### 4.2 src/views/system/role/index.vue
### 4.3 src/views/system/dept/index.vue

这些文件的修改模式与 user/index.vue 类似，主要是：
1. 替换组件名称（el-xxx → n-xxx）
2. 使用 useMessage、useDialog 替代 ElMessage、ElMessageBox
3. 使用 Naive UI 的图标系统（@vicons/ionicons5）

---

## 5. 工具函数更新

### src/utils/request.ts

**修改后**:
```typescript
import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { createDiscreteApi } from 'naive-ui'
import { useUserStore } from '@/store/user'
import router from '@/router'

const { message } = createDiscreteApi(['message'])

const service: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 30000,
})

service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data
    if (res.code !== 200) {
      message.error(res.msg || '请求失败')
      if (res.code === 401) {
        const userStore = useUserStore()
        userStore.logout()
        router.push('/login')
      }
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    return res
  },
  (error) => {
    const msg = error.response?.data?.msg || error.message || '网络异常'
    message.error(msg)
    if (error.response?.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

export default service
```

---

## 6. 迁移检查清单

### 必须修改的文件
- [x] package.json - 依赖更新
- [ ] src/main.ts - 移除 Element Plus 配置
- [ ] src/App.vue - 添加 Naive UI Provider
- [ ] vite.config.ts - 更新自动导入配置
- [ ] src/utils/request.ts - 使用 createDiscreteApi
- [ ] src/views/login/index.vue - 重写登录页
- [ ] src/layout/index.vue - 重写布局
- [ ] src/views/dashboard/index.vue - 重写工作台
- [ ] src/views/system/user/index.vue - 重写用户管理
- [ ] src/views/system/role/index.vue - 重写角色管理
- [ ] src/views/system/dept/index.vue - 重写部门管理

### 可选修改
- [ ] src/styles/index.scss - 调整全局样式
- [ ] src/router/index.ts - 移除 nprogress（Naive UI 有内置的 LoadingBar）

---

## 7. 常见问题

### Q1: 图标如何使用？
A: Naive UI 使用 @vicons 图标库：
```typescript
import { PersonOutline } from '@vicons/ionicons5'
import { NIcon } from 'naive-ui'

// 在模板中
<n-icon :component="PersonOutline" />
```

### Q2: Message 和 Dialog 如何使用？
A: 使用 Composition API：
```typescript
import { useMessage, useDialog } from 'naive-ui'

const message = useMessage()
const dialog = useDialog()

message.success('成功')
dialog.warning({ title: '警告', content: '确认删除？' })
```

### Q3: 表格如何自定义渲染？
A: 使用 render 函数：
```typescript
import { h } from 'vue'
import { NButton } from 'naive-ui'

const columns = [
  {
    title: '操作',
    key: 'actions',
    render(row) {
      return h(NButton, { onClick: () => handleEdit(row) }, { default: () => '编辑' })
    }
  }
]
```

---

## 8. 参考资源

- [Naive UI 官方文档](https://www.naiveui.com/)
- [Naive UI GitHub](https://github.com/tusen-ai/naive-ui)
- [@vicons 图标库](https://www.xicons.org/)
- [Naive UI 示例](https://www.naiveui.com/zh-CN/os-theme/components/button)

---

## 9. 下一步操作

1. 运行 `npm install` 安装 Naive UI
2. 按照本文档逐个修改文件
3. 测试每个页面的功能
4. 调整样式以符合设计要求

---

## 10. 预计工作量

- 配置文件修改：30分钟
- 登录页重写：30分钟
- 布局组件重写：1小时
- 管理页面重写：2小时
- 测试和调试：1小时

**总计：约 5 小时**
