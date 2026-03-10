<template>
  <div class="dashboard">
    <!-- 欢迎信息 -->
    <n-card :bordered="false" class="welcome-card">
      <n-space align="center">
        <n-avatar :size="64" round color="#18a058">
          <n-icon :size="32" :component="PersonOutline" />
        </n-avatar>
        <div>
          <n-h3 style="margin: 0">{{ greeting }}，{{ userStore.username }}</n-h3>
          <n-text depth="3">{{ roleDescription }}</n-text>
        </div>
      </n-space>
    </n-card>

    <!-- 管理员工作台 -->
    <template v-if="isAdmin">
      <n-grid :cols="4" :x-gap="16" :y-gap="16" style="margin-top: 16px">
        <n-gi>
          <n-card title="用户总数" :bordered="false" hoverable>
            <n-statistic :value="1234">
              <template #prefix>
                <n-icon :component="PeopleOutline" />
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="订单总数" :bordered="false" hoverable>
            <n-statistic :value="5678">
              <template #prefix>
                <n-icon :component="DocumentTextOutline" />
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="总销售额" :bordered="false" hoverable>
            <n-statistic :value="987650">
              <template #prefix>
                <n-icon :component="CashOutline" />
              </template>
              <template #suffix>元</template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="库存总量" :bordered="false" hoverable>
            <n-statistic :value="3456">
              <template #prefix>
                <n-icon :component="CubeOutline" />
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
      </n-grid>

      <n-card title="快捷操作" style="margin-top: 16px" :bordered="false">
        <n-space>
          <n-button type="primary" @click="router.push('/system/user')">
            <template #icon><n-icon :component="PeopleOutline" /></template>
            用户管理
          </n-button>
          <n-button type="info" @click="router.push('/system/role')">
            <template #icon><n-icon :component="ShieldOutline" /></template>
            角色管理
          </n-button>
          <n-button type="success" @click="router.push('/system/dept')">
            <template #icon><n-icon :component="BusinessSharp" /></template>
            部门管理
          </n-button>
          <n-button @click="router.push('/system/log')">
            <template #icon><n-icon :component="DocumentTextOutline" /></template>
            操作日志
          </n-button>
        </n-space>
      </n-card>
    </template>

    <!-- 销售人员工作台 -->
    <template v-else-if="isSales">
      <n-grid :cols="3" :x-gap="16" :y-gap="16" style="margin-top: 16px">
        <n-gi>
          <n-card title="我的销售订单" :bordered="false" hoverable>
            <n-statistic :value="45">
              <template #prefix>
                <n-icon :component="DocumentTextOutline" />
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="本月销售额" :bordered="false" hoverable>
            <n-statistic :value="156780">
              <template #prefix>
                <n-icon :component="CashOutline" />
              </template>
              <template #suffix>元</template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="客户数量" :bordered="false" hoverable>
            <n-statistic :value="89">
              <template #prefix>
                <n-icon :component="PeopleOutline" />
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
      </n-grid>

      <n-card title="快捷操作" style="margin-top: 16px" :bordered="false">
        <n-space>
          <n-button type="primary" @click="router.push('/sales/order')">
            <template #icon><n-icon :component="DocumentTextOutline" /></template>
            销售订单
          </n-button>
          <n-button type="info" @click="router.push('/sales/customer')">
            <template #icon><n-icon :component="PeopleOutline" /></template>
            客户管理
          </n-button>
          <n-button @click="router.push('/inventory/inventory')">
            <template #icon><n-icon :component="CubeOutline" /></template>
            库存查询
          </n-button>
        </n-space>
      </n-card>
    </template>

    <!-- 采购人员工作台 -->
    <template v-else-if="isPurchase">
      <n-grid :cols="3" :x-gap="16" :y-gap="16" style="margin-top: 16px">
        <n-gi>
          <n-card title="我的采购订单" :bordered="false" hoverable>
            <n-statistic :value="32">
              <template #prefix>
                <n-icon :component="CartOutline" />
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="本月采购额" :bordered="false" hoverable>
            <n-statistic :value="234560">
              <template #prefix>
                <n-icon :component="CashOutline" />
              </template>
              <template #suffix>元</template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="供应商数量" :bordered="false" hoverable>
            <n-statistic :value="56">
              <template #prefix>
                <n-icon :component="BusinessSharp" />
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
      </n-grid>

      <n-card title="快捷操作" style="margin-top: 16px" :bordered="false">
        <n-space>
          <n-button type="primary" @click="router.push('/purchase/order')">
            <template #icon><n-icon :component="CartOutline" /></template>
            采购订单
          </n-button>
          <n-button type="info" @click="router.push('/purchase/supplier')">
            <template #icon><n-icon :component="BusinessSharp" /></template>
            供应商管理
          </n-button>
          <n-button @click="router.push('/purchase/stockin')">
            <template #icon><n-icon :component="ArrowDownOutline" /></template>
            入库管理
          </n-button>
          <n-button @click="router.push('/inventory/inventory')">
            <template #icon><n-icon :component="CubeOutline" /></template>
            库存查询
          </n-button>
        </n-space>
      </n-card>
    </template>

    <!-- 仓库人员工作台 -->
    <template v-else-if="isWarehouse">
      <n-grid :cols="4" :x-gap="16" :y-gap="16" style="margin-top: 16px">
        <n-gi>
          <n-card title="库存总量" :bordered="false" hoverable>
            <n-statistic :value="3456">
              <template #prefix>
                <n-icon :component="CubeOutline" />
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="待入库" :bordered="false" hoverable>
            <n-statistic :value="23">
              <template #prefix>
                <n-icon :component="ArrowDownOutline" />
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="待出库" :bordered="false" hoverable>
            <n-statistic :value="18">
              <template #prefix>
                <n-icon :component="ArrowUpOutline" />
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="低库存预警" :bordered="false" hoverable>
            <n-statistic :value="7">
              <template #prefix>
                <n-icon :component="WarningOutline" />
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
      </n-grid>

      <n-card title="快捷操作" style="margin-top: 16px" :bordered="false">
        <n-space>
          <n-button type="primary" @click="router.push('/inventory/inventory')">
            <template #icon><n-icon :component="CubeOutline" /></template>
            库存查询
          </n-button>
          <n-button type="info" @click="router.push('/purchase/stockin')">
            <template #icon><n-icon :component="ArrowDownOutline" /></template>
            入库管理
          </n-button>
          <n-button type="warning" @click="router.push('/inventory/stockout')">
            <template #icon><n-icon :component="ArrowUpOutline" /></template>
            出库管理
          </n-button>
          <n-button @click="router.push('/inventory/stockcheck')">
            <template #icon><n-icon :component="ClipboardOutline" /></template>
            库存盘点
          </n-button>
        </n-space>
      </n-card>
    </template>

    <!-- 财务人员工作台 -->
    <template v-else-if="isFinance">
      <n-grid :cols="3" :x-gap="16" :y-gap="16" style="margin-top: 16px">
        <n-gi>
          <n-card title="本月收入" :bordered="false" hoverable>
            <n-statistic :value="567890">
              <template #prefix>
                <n-icon :component="TrendingUpOutline" />
              </template>
              <template #suffix>元</template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="本月支出" :bordered="false" hoverable>
            <n-statistic :value="234560">
              <template #prefix>
                <n-icon :component="TrendingDownOutline" />
              </template>
              <template #suffix>元</template>
            </n-statistic>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="待处理费用" :bordered="false" hoverable>
            <n-statistic :value="12">
              <template #prefix>
                <n-icon :component="WalletOutline" />
              </template>
            </n-statistic>
          </n-card>
        </n-gi>
      </n-grid>

      <n-card title="快捷操作" style="margin-top: 16px" :bordered="false">
        <n-space>
          <n-button type="primary" @click="router.push('/finance/payment')">
            <template #icon><n-icon :component="CashOutline" /></template>
            收付款管理
          </n-button>
          <n-button type="info" @click="router.push('/finance/expense')">
            <template #icon><n-icon :component="WalletOutline" /></template>
            费用管理
          </n-button>
          <n-button @click="router.push('/statistics/payment')">
            <template #icon><n-icon :component="BarChartOutline" /></template>
            财务统计
          </n-button>
        </n-space>
      </n-card>
    </template>

    <!-- 通用工作台（无特定角色） -->
    <template v-else>
      <n-card title="欢迎使用 EnterpriseHub ERP" style="margin-top: 16px" :bordered="false">
        <n-space vertical>
          <n-text>这是一个基于 Vue 3 + Naive UI + Spring Boot 3 的企业资源计划管理系统</n-text>
          <n-divider />
          <n-text depth="3">系统功能模块：</n-text>
          <n-ul>
            <n-li>系统管理：用户管理、角色管理、部门管理、菜单管理</n-li>
            <n-li>销售管理：客户管理、销售订单、出库管理</n-li>
            <n-li>采购管理：供应商管理、采购订单、入库管理</n-li>
            <n-li>库存管理：商品管理、仓库管理、库存查询、盘点管理</n-li>
            <n-li>财务管理：应收应付、费用管理、财务报表</n-li>
          </n-ul>
        </n-space>
      </n-card>
    </template>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { 
  PeopleOutline, 
  DocumentTextOutline, 
  CashOutline, 
  CubeOutline,
  PersonOutline,
  ShieldOutline,
  BusinessSharp,
  CartOutline,
  ArrowDownOutline,
  ArrowUpOutline,
  WarningOutline,
  ClipboardOutline,
  TrendingUpOutline,
  TrendingDownOutline,
  WalletOutline,
  BarChartOutline
} from '@vicons/ionicons5'

const router = useRouter()
const userStore = useUserStore()

// 获取当前时间的问候语
const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 6) return '凌晨好'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 18) return '下午好'
  if (hour < 22) return '晚上好'
  return '夜深了'
})

// 判断用户角色
const userRoles = computed(() => userStore.roles || [])

const isAdmin = computed(() => 
  userRoles.value.includes('admin') || userRoles.value.includes('system_admin')
)

const isSales = computed(() => 
  userRoles.value.includes('sales_manager') || userRoles.value.includes('sales_staff')
)

const isPurchase = computed(() => 
  userRoles.value.includes('purchase_manager') || userRoles.value.includes('purchase_staff')
)

const isWarehouse = computed(() => 
  userRoles.value.includes('warehouse_manager') || userRoles.value.includes('warehouse_staff')
)

const isFinance = computed(() => 
  userRoles.value.includes('finance_manager') || userRoles.value.includes('finance_staff')
)

// 角色描述
const roleDescription = computed(() => {
  if (isAdmin.value) return '系统管理员 - 全局管理权限'
  if (isSales.value) return '销售部门 - 管理客户和销售订单'
  if (isPurchase.value) return '采购部门 - 管理供应商和采购订单'
  if (isWarehouse.value) return '仓库部门 - 管理库存和出入库'
  if (isFinance.value) return '财务部门 - 管理收付款和费用'
  return '欢迎使用系统'
})
</script>

<style scoped lang="scss">
.dashboard {
  width: 100%;
}

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  
  :deep(.n-card__content) {
    color: white;
  }
  
  :deep(.n-h3) {
    color: white;
  }
  
  :deep(.n-text) {
    color: rgba(255, 255, 255, 0.9);
  }
}
</style>
