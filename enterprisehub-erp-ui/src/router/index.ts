import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' },
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '工作台', icon: 'HomeFilled' },
      },
      {
        path: 'system/user',
        name: 'User',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'User', roles: ['admin', 'system_admin'] },
      },
      {
        path: 'system/role',
        name: 'Role',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理', icon: 'UserFilled', roles: ['admin', 'system_admin'] },
      },
      {
        path: 'system/dept',
        name: 'Dept',
        component: () => import('@/views/system/dept/index.vue'),
        meta: { title: '部门管理', icon: 'OfficeBuilding', roles: ['admin', 'system_admin'] },
      },
      {
        path: 'system/menu',
        name: 'Menu',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { title: '菜单管理', icon: 'Menu', roles: ['admin'] },
      },
      {
        path: 'system/log',
        name: 'OperationLog',
        component: () => import('@/views/system/log/index.vue'),
        meta: { title: '操作日志', icon: 'Document', roles: ['admin', 'system_admin'] },
      },
      // 销售模块
      {
        path: 'sales/order',
        name: 'SalesOrder',
        component: () => import('@/views/sales/order/index.vue'),
        meta: { title: '销售订单', icon: 'DocumentText', roles: ['admin', 'sales_manager', 'sales_staff'] },
      },
      {
        path: 'sales/customer',
        name: 'Customer',
        component: () => import('@/views/sales/customer/index.vue'),
        meta: { title: '客户管理', icon: 'People', roles: ['admin', 'sales_manager', 'sales_staff'] },
      },
      // 采购模块
      {
        path: 'purchase/order',
        name: 'PurchaseOrder',
        component: () => import('@/views/purchase/order/index.vue'),
        meta: { title: '采购订单', icon: 'Cart', roles: ['admin', 'purchase_manager', 'purchase_staff'] },
      },
      {
        path: 'purchase/supplier',
        name: 'Supplier',
        component: () => import('@/views/purchase/supplier/index.vue'),
        meta: { title: '供应商管理', icon: 'Business', roles: ['admin', 'purchase_manager', 'purchase_staff'] },
      },
      {
        path: 'purchase/stockin',
        name: 'StockIn',
        component: () => import('@/views/purchase/stockin/index.vue'),
        meta: { title: '入库管理', icon: 'ArrowDown', roles: ['admin', 'purchase_manager', 'warehouse_staff'] },
      },
      // 库存模块
      {
        path: 'inventory/product',
        name: 'Product',
        component: () => import('@/views/inventory/product/index.vue'),
        meta: { title: '产品管理', icon: 'Cube', roles: ['admin', 'warehouse_manager', 'warehouse_staff'] },
      },
      {
        path: 'inventory/category',
        name: 'ProductCategory',
        component: () => import('@/views/inventory/category/index.vue'),
        meta: { title: '产品分类', icon: 'Grid', roles: ['admin', 'warehouse_manager'] },
      },
      {
        path: 'inventory/warehouse',
        name: 'Warehouse',
        component: () => import('@/views/inventory/warehouse/index.vue'),
        meta: { title: '仓库管理', icon: 'Home', roles: ['admin', 'warehouse_manager'] },
      },
      {
        path: 'inventory/inventory',
        name: 'Inventory',
        component: () => import('@/views/inventory/inventory/index.vue'),
        meta: { title: '库存查询', icon: 'List', roles: ['admin', 'warehouse_manager', 'warehouse_staff', 'sales_staff', 'purchase_staff'] },
      },
      {
        path: 'inventory/stockout',
        name: 'StockOut',
        component: () => import('@/views/inventory/stockout/index.vue'),
        meta: { title: '出库管理', icon: 'ArrowUp', roles: ['admin', 'warehouse_manager', 'warehouse_staff'] },
      },
      {
        path: 'inventory/stockcheck',
        name: 'StockCheck',
        component: () => import('@/views/inventory/stockcheck/index.vue'),
        meta: { title: '库存盘点', icon: 'Clipboard', roles: ['admin', 'warehouse_manager', 'warehouse_staff'] },
      },
      {
        path: 'inventory/transaction',
        name: 'InventoryTransaction',
        component: () => import('@/views/inventory/transaction/index.vue'),
        meta: { title: '库存流水', icon: 'Receipt', roles: ['admin', 'warehouse_manager', 'warehouse_staff'] },
      },
      // 财务模块
      {
        path: 'finance/payment',
        name: 'Payment',
        component: () => import('@/views/finance/payment/index.vue'),
        meta: { title: '收付款管理', icon: 'Cash', roles: ['admin', 'finance_manager', 'finance_staff'] },
      },
      {
        path: 'finance/expense',
        name: 'Expense',
        component: () => import('@/views/finance/expense/index.vue'),
        meta: { title: '费用管理', icon: 'Wallet', roles: ['admin', 'finance_manager', 'finance_staff'] },
      },
      // 统计报表模块
      {
        path: 'statistics/sales',
        name: 'SalesStatistics',
        component: () => import('@/views/statistics/sales/index.vue'),
        meta: { title: '销售统计', icon: 'TrendingUp', roles: ['admin', 'sales_manager', 'finance_manager'] },
      },
      {
        path: 'statistics/purchase',
        name: 'PurchaseStatistics',
        component: () => import('@/views/statistics/purchase/index.vue'),
        meta: { title: '采购统计', icon: 'Analytics', roles: ['admin', 'purchase_manager', 'finance_manager'] },
      },
      {
        path: 'statistics/payment',
        name: 'PaymentStatistics',
        component: () => import('@/views/statistics/payment/index.vue'),
        meta: { title: '财务统计', icon: 'PieChart', roles: ['admin', 'finance_manager'] },
      },
    ],
  },
  {
    path: '/403',
    name: 'Forbidden',
    component: () => import('@/views/error/403.vue'),
    meta: { title: '无权限' },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
const whiteList = ['/login', '/403']

router.beforeEach(async (to, _from, next) => {
  const userStore = useUserStore()

  if (userStore.token) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      // 如果没有用户信息，先获取
      if (!userStore.userInfo) {
        try {
          await userStore.getUserInfo()
        } catch (error) {
          console.error('获取用户信息失败:', error)
          userStore.logout()
          next(`/login?redirect=${to.path}`)
          return
        }
      }

      // 检查路由权限
      const roles = to.meta.roles as string[] | undefined
      if (roles && roles.length > 0) {
        const userRoles = userStore.roles || []
        const hasPermission = roles.some(role => userRoles.includes(role))
        
        if (!hasPermission) {
          console.warn('用户无权限访问:', to.path, '需要角色:', roles, '用户角色:', userRoles)
          next('/403')
          return
        }
      }

      next()
    }
  } else {
    if (whiteList.includes(to.path)) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
  }
})

export default router
