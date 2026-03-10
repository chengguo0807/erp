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
import { ref, h, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useDialog, useMessage, NIcon } from 'naive-ui'
import { 
  BusinessOutline, 
  PersonOutline, 
  LogOutOutline
} from '@vicons/ionicons5'
import { useUserStore } from '@/store/user'
import { menuConfigs, filterMenusByRoles } from '@/router/menus'
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

// 根据用户角色动态生成菜单
const menuOptions = computed<MenuOption[]>(() => {
  const userRoles = userStore.roles || []
  console.log('用户角色:', userRoles)
  
  // 如果没有角色信息，返回空菜单
  if (userRoles.length === 0) {
    return []
  }
  
  // 根据角色过滤菜单
  const filteredMenus = filterMenusByRoles(menuConfigs, userRoles as string[])
  console.log('过滤后的菜单:', filteredMenus)
  
  return filteredMenus
})

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

// 组件挂载时获取用户信息
onMounted(async () => {
  // 如果没有用户信息，则获取
  if (!userStore.userInfo) {
    try {
      await userStore.getUserInfo()
    } catch (error) {
      console.error('获取用户信息失败:', error)
      message.error('获取用户信息失败，请重新登录')
      router.push('/login')
    }
  }
})
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
