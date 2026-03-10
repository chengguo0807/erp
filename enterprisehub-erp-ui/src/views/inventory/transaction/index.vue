<template>
  <n-card title="库存流水" :bordered="false">
    <n-space vertical :size="16">
      <!-- 搜索栏 -->
      <n-form inline :model="queryParams">
        <n-form-item label="产品ID">
          <n-input v-model:value="queryParams.productId" placeholder="请输入产品ID" clearable />
        </n-form-item>
        <n-form-item label="仓库ID">
          <n-input v-model:value="queryParams.warehouseId" placeholder="请输入仓库ID" clearable />
        </n-form-item>
        <n-form-item label="交易类型">
          <n-select
            v-model:value="queryParams.transactionType"
            placeholder="请选择类型"
            clearable
            :options="typeOptions"
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
import { useMessage, NTag } from 'naive-ui'
import { SearchOutline, RefreshOutline } from '@vicons/ionicons5'
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
  transactionType: null as number | null,
})

const typeOptions = [
  { label: '入库', value: 0 },
  { label: '出库', value: 1 },
  { label: '调拨', value: 2 },
  { label: '盘点', value: 3 }
]

const columns: DataTableColumns = [
  { title: '流水号', key: 'transactionNo', width: 180 },
  { title: '产品ID', key: 'productId', width: 100 },
  { title: '仓库ID', key: 'warehouseId', width: 100 },
  {
    title: '交易类型',
    key: 'transactionType',
    width: 100,
    render(row: any) {
      const typeMap: any = {
        0: { type: 'success', text: '入库' },
        1: { type: 'warning', text: '出库' },
        2: { type: 'info', text: '调拨' },
        3: { type: 'default', text: '盘点' }
      }
      const type = typeMap[row.transactionType] || { type: 'default', text: '未知' }
      return h(NTag, { type: type.type }, { default: () => type.text })
    }
  },
  { title: '变动数量', key: 'quantity', width: 120 },
  { title: '变动前数量', key: 'beforeQuantity', width: 120 },
  { title: '变动后数量', key: 'afterQuantity', width: 120 },
  { title: '关联单号', key: 'relatedNo', width: 180 },
  { title: '备注', key: 'remark', ellipsis: { tooltip: true } },
  { title: '创建时间', key: 'createTime', width: 180 }
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
    const res: any = await request.get('/inventory/v1/transactions', { params: queryParams })
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
  queryParams.transactionType = null
  queryParams.pageNum = 1
  handleQuery()
}

// 初始化
handleQuery()
</script>
