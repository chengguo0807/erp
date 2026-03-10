import { h } from 'vue'
import { NIcon } from 'naive-ui'
import {
  HomeOutline,
  SettingsOutline,
  PeopleOutline,
  ShieldOutline,
  BusinessSharp,
  DocumentTextOutline,
  CartOutline,
  CubeOutline,
  CashOutline,
  BarChartOutline,
  ListOutline,
  GridOutline,
  ReceiptOutline
} from '@vicons/ionicons5'
import type { MenuOption } from 'naive-ui'

// 渲染图标
const renderIcon = (icon: any) => {
  return () => h(NIcon, null, { default: () => h(icon) })
}

// 菜单配置接口
export interface MenuConfig {
  label: string
  key: string
  icon?: any
  children?: MenuConfig[]
  roles?: string[] // 允许访问的角色，不设置表示所有角色都可以访问
}

// 完整的菜单配置
export const menuConfigs: MenuConfig[] = [
  {
    label: '工作台',
    key: '/dashboard',
    icon: renderIcon(HomeOutline),
    // 所有角色都可以访问工作台
  },
  {
    label: '系统管理',
    key: '/system',
    icon: renderIcon(SettingsOutline),
    roles: ['admin', 'system_admin'], // 只有管理员可以访问
    children: [
      {
        label: '用户管理',
        key: '/system/user',
        icon: renderIcon(PeopleOutline),
        roles: ['admin', 'system_admin']
      },
      {
        label: '角色管理',
        key: '/system/role',
        icon: renderIcon(ShieldOutline),
        roles: ['admin', 'system_admin']
      },
      {
        label: '部门管理',
        key: '/system/dept',
        icon: renderIcon(BusinessSharp),
        roles: ['admin', 'system_admin']
      },
      {
        label: '菜单管理',
        key: '/system/menu',
        icon: renderIcon(ListOutline),
        roles: ['admin']
      },
      {
        label: '操作日志',
        key: '/system/log',
        icon: renderIcon(DocumentTextOutline),
        roles: ['admin', 'system_admin']
      }
    ]
  },
  {
    label: '销售管理',
    key: '/sales',
    icon: renderIcon(DocumentTextOutline),
    roles: ['admin', 'sales_manager', 'sales_staff'], // 销售相关角色
    children: [
      {
        label: '销售订单',
        key: '/sales/order',
        icon: renderIcon(DocumentTextOutline),
        roles: ['admin', 'sales_manager', 'sales_staff']
      },
      {
        label: '客户管理',
        key: '/sales/customer',
        icon: renderIcon(PeopleOutline),
        roles: ['admin', 'sales_manager', 'sales_staff']
      }
    ]
  },
  {
    label: '采购管理',
    key: '/purchase',
    icon: renderIcon(CartOutline),
    roles: ['admin', 'purchase_manager', 'purchase_staff'], // 采购相关角色
    children: [
      {
        label: '采购订单',
        key: '/purchase/order',
        icon: renderIcon(CartOutline),
        roles: ['admin', 'purchase_manager', 'purchase_staff']
      },
      {
        label: '供应商管理',
        key: '/purchase/supplier',
        icon: renderIcon(BusinessSharp),
        roles: ['admin', 'purchase_manager', 'purchase_staff']
      },
      {
        label: '入库管理',
        key: '/purchase/stockin',
        icon: renderIcon(CubeOutline),
        roles: ['admin', 'purchase_manager', 'warehouse_staff']
      }
    ]
  },
  {
    label: '库存管理',
    key: '/inventory',
    icon: renderIcon(CubeOutline),
    roles: ['admin', 'warehouse_manager', 'warehouse_staff'], // 仓库相关角色
    children: [
      {
        label: '产品管理',
        key: '/inventory/product',
        icon: renderIcon(CubeOutline),
        roles: ['admin', 'warehouse_manager', 'warehouse_staff']
      },
      {
        label: '产品分类',
        key: '/inventory/category',
        icon: renderIcon(GridOutline),
        roles: ['admin', 'warehouse_manager']
      },
      {
        label: '仓库管理',
        key: '/inventory/warehouse',
        icon: renderIcon(BusinessSharp),
        roles: ['admin', 'warehouse_manager']
      },
      {
        label: '库存查询',
        key: '/inventory/inventory',
        icon: renderIcon(ListOutline),
        roles: ['admin', 'warehouse_manager', 'warehouse_staff', 'sales_staff', 'purchase_staff']
      },
      {
        label: '出库管理',
        key: '/inventory/stockout',
        icon: renderIcon(CubeOutline),
        roles: ['admin', 'warehouse_manager', 'warehouse_staff']
      },
      {
        label: '库存盘点',
        key: '/inventory/stockcheck',
        icon: renderIcon(ListOutline),
        roles: ['admin', 'warehouse_manager', 'warehouse_staff']
      },
      {
        label: '库存流水',
        key: '/inventory/transaction',
        icon: renderIcon(ReceiptOutline),
        roles: ['admin', 'warehouse_manager', 'warehouse_staff']
      }
    ]
  },
  {
    label: '财务管理',
    key: '/finance',
    icon: renderIcon(CashOutline),
    roles: ['admin', 'finance_manager', 'finance_staff'], // 财务相关角色
    children: [
      {
        label: '收付款管理',
        key: '/finance/payment',
        icon: renderIcon(CashOutline),
        roles: ['admin', 'finance_manager', 'finance_staff']
      },
      {
        label: '费用管理',
        key: '/finance/expense',
        icon: renderIcon(CashOutline),
        roles: ['admin', 'finance_manager', 'finance_staff']
      }
    ]
  },
  {
    label: '统计报表',
    key: '/statistics',
    icon: renderIcon(BarChartOutline),
    roles: ['admin', 'sales_manager', 'purchase_manager', 'warehouse_manager', 'finance_manager'], // 各部门经理和管理员
    children: [
      {
        label: '销售统计',
        key: '/statistics/sales',
        icon: renderIcon(BarChartOutline),
        roles: ['admin', 'sales_manager', 'finance_manager']
      },
      {
        label: '采购统计',
        key: '/statistics/purchase',
        icon: renderIcon(BarChartOutline),
        roles: ['admin', 'purchase_manager', 'finance_manager']
      },
      {
        label: '财务统计',
        key: '/statistics/payment',
        icon: renderIcon(BarChartOutline),
        roles: ['admin', 'finance_manager']
      }
    ]
  }
]

/**
 * 根据用户角色过滤菜单
 * @param menus 菜单配置
 * @param userRoles 用户角色列表
 * @returns 过滤后的菜单
 */
export function filterMenusByRoles(menus: MenuConfig[], userRoles: string[]): MenuOption[] {
  const result: MenuOption[] = []

  for (const menu of menus) {
    // 如果菜单没有设置roles，或者用户角色包含菜单所需的任一角色，则显示该菜单
    const hasPermission = !menu.roles || menu.roles.length === 0 || 
                          menu.roles.some(role => userRoles.includes(role))

    if (hasPermission) {
      const menuOption: MenuOption = {
        label: menu.label,
        key: menu.key,
        icon: menu.icon
      }

      // 递归处理子菜单
      if (menu.children && menu.children.length > 0) {
        const filteredChildren = filterMenusByRoles(menu.children, userRoles)
        if (filteredChildren.length > 0) {
          menuOption.children = filteredChildren
        } else {
          // 如果子菜单全部被过滤掉，则不显示父菜单
          continue
        }
      }

      result.push(menuOption)
    }
  }

  return result
}

/**
 * 获取用户可访问的所有路由路径
 * @param menus 菜单配置
 * @param userRoles 用户角色列表
 * @returns 路由路径数组
 */
export function getAccessibleRoutes(menus: MenuConfig[], userRoles: string[]): string[] {
  const routes: string[] = []

  for (const menu of menus) {
    const hasPermission = !menu.roles || menu.roles.length === 0 || 
                          menu.roles.some(role => userRoles.includes(role))

    if (hasPermission) {
      // 如果不是父菜单（没有children或children为空），则添加到路由列表
      if (!menu.children || menu.children.length === 0) {
        routes.push(menu.key)
      }

      // 递归处理子菜单
      if (menu.children && menu.children.length > 0) {
        routes.push(...getAccessibleRoutes(menu.children, userRoles))
      }
    }
  }

  return routes
}
