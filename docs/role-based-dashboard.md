# 基于角色的工作台实现

## 概述

实现了根据用户角色显示不同工作台内容的功能，每个角色登录后都能看到专属的工作台视图。

## 实现方案

### 1. 统一入口
- 所有用户登录后都跳转到 `/dashboard` 路由
- 工作台页面根据用户角色动态渲染不同的内容

### 2. 角色分类

系统支持以下角色的专属工作台：

#### 管理员 (admin, system_admin)
- **统计数据**：
  - 用户总数
  - 订单总数
  - 总销售额
  - 库存总量
- **快捷操作**：
  - 用户管理
  - 角色管理
  - 部门管理
  - 操作日志

#### 销售人员 (sales_manager, sales_staff)
- **统计数据**：
  - 我的销售订单
  - 本月销售额
  - 客户数量
- **快捷操作**：
  - 销售订单
  - 客户管理
  - 库存查询

#### 采购人员 (purchase_manager, purchase_staff)
- **统计数据**：
  - 我的采购订单
  - 本月采购额
  - 供应商数量
- **快捷操作**：
  - 采购订单
  - 供应商管理
  - 入库管理
  - 库存查询

#### 仓库人员 (warehouse_manager, warehouse_staff)
- **统计数据**：
  - 库存总量
  - 待入库
  - 待出库
  - 低库存预警
- **快捷操作**：
  - 库存查询
  - 入库管理
  - 出库管理
  - 库存盘点

#### 财务人员 (finance_manager, finance_staff)
- **统计数据**：
  - 本月收入
  - 本月支出
  - 待处理费用
- **快捷操作**：
  - 收付款管理
  - 费用管理
  - 财务统计

### 3. 个性化欢迎信息

工作台顶部显示：
- 根据时间的问候语（早上好、下午好等）
- 用户昵称
- 角色描述

### 4. 技术实现

#### 角色判断
```typescript
const userRoles = computed(() => userStore.roles || [])

const isAdmin = computed(() => 
  userRoles.value.includes('admin') || userRoles.value.includes('system_admin')
)

const isSales = computed(() => 
  userRoles.value.includes('sales_manager') || userRoles.value.includes('sales_staff')
)
// ... 其他角色判断
```

#### 条件渲染
```vue
<template v-if="isAdmin">
  <!-- 管理员工作台 -->
</template>

<template v-else-if="isSales">
  <!-- 销售人员工作台 -->
</template>
<!-- ... 其他角色工作台 -->
```

## 优势

1. **用户体验优化**：每个用户看到的都是与其工作相关的内容
2. **信息聚焦**：只显示用户关心的统计数据和操作入口
3. **提高效率**：快捷操作按钮直达常用功能
4. **易于维护**：统一的路由结构，便于管理和扩展

## 未来扩展

1. **实时数据**：将静态数据替换为从后端API获取的实时数据
2. **图表展示**：添加图表组件展示趋势分析
3. **待办事项**：显示用户的待办任务列表
4. **消息通知**：显示系统通知和提醒
5. **自定义布局**：允许用户自定义工作台布局

## 相关文件

- `enterprisehub-erp-ui/src/views/dashboard/index.vue` - 工作台主页面
- `enterprisehub-erp-ui/src/router/index.ts` - 路由配置
- `enterprisehub-erp-ui/src/store/user.ts` - 用户状态管理
- `enterprisehub-erp-ui/src/layout/index.vue` - 布局组件
