<template>
  <n-card title="库存管理" :bordered="false">
    <n-space vertical :size="16">
      <!-- 搜索栏 -->
      <n-form inline :model="queryParams">
        <n-form-item label="产品ID">
          <n-input v-model:value="queryParams.productId" placeholder="请输入产品ID" clearable />
        </n-form-item>
        <n-form-item label="仓库ID">
          <n-input v-model:value="queryParams.warehouseId" placeholder="请输入仓库ID" clearable />
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
        <n-button type="warning" @click="handleWarning">
          <template #icon>
            <n-icon :component="WarningOutline" />
          </template>
          库存预警
        </n-button>
        <n-button type="info" @click="handleTransfer">
          <template #icon>
            <n-icon :component="SwapHorizontalOutline" />
          </template>
          库存调拨
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
import { useMessage, NButton, NTag, NSpace } from 'naive-ui'
import { SearchOutline, RefreshOutline, WarningOutline, SwapHorizontalOutline } from '@vicons/ionicons5'
import request from '@/utils/request'
import type { DataTableColumns } from 'naive-ui'

const message = useMessage()

const loading = ref(false)
const tableData = ref([])

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  productId: '',
  warehouseId: '',
})

const columns: DataTableColumns = [
  { title: '产品ID', key: 'productId', width: 100 },
  { title: '仓库ID', key: 'warehouseId', width: 100 },
  { title: '库存数量', key: 'quantity', width: 120 },
  { title: '可用数量', key: 'availableQuantity', width: 120 },
  { title: '锁定数量', key: 'lockedQuantity', width: 120 },
  { title: '最小库存', key: 'minQuantity', width: 120 },
  { title: '最大库存', key: 'maxQuantity', width: 120 },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render(row: any) {
      const quantity = parseFloat(row.quantity || 0)
      const minQuantity = parseFloat(row.minQuantity || 0)
      if (quantity <= minQuantity) {
        return h(NTag, { type: 'error' }, { default: () => '库存不足' })
      }
      return h(NTag, { type: 'success' }, { default: () => '正常' })
    }
  },
  { title: '更新时间', key: 'updateTime', width: 180 },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    fixed: 'right',
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
              { default: () => '调整' }
            ),
            h(
              NButton,
              {
                size: 'small',
                type: 'info',
                text: true,
                onClick: () => handleView(row)
              },
              { default: () => '流水' }
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
    const res: any = await request.get('/inventory/v1/inventories', { params: queryParams })
    tableData.value = res.data.list
    pagination.itemCount = res.data.total
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.productId = ''
  queryParams.warehouseId = ''
  queryParams.pageNum = 1
  handleQuery()
}

const handleWarning = async () => {
  try {
    const res: any = await request.get('/inventory/v1/inventories/warning')
    message.success(`查询到 ${res.data.length} 条库存预警`)
  } catch (error) {
    // 错误已在拦截器中处理
  }
}

const handleTransfer = () => {
  message.info('库存调拨功能 - 待实现')
}

const handleEdit = (row: any) => {
  message.info(`调整库存: 产品${row.productId} 仓库${row.warehouseId}`)
}

const handleView = (row: any) => {
  message.info(`查看流水: 产品${row.productId} 仓库${row.warehouseId}`)
}

// 初始化
handleQuery()
</script>
