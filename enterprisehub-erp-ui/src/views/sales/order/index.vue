<template>
  <n-card title="销售订单" :bordered="false">
    <n-space vertical :size="16">
      <!-- 搜索栏 -->
      <n-form inline :model="queryParams">
        <n-form-item label="订单号">
          <n-input v-model:value="queryParams.orderNo" placeholder="请输入订单号" clearable />
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
          新增订单
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
import { SearchOutline, RefreshOutline, AddOutline } from '@vicons/ionicons5'
import request from '@/utils/request'
import type { DataTableColumns } from 'naive-ui'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const tableData = ref([])

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  orderNo: '',
  status: null as number | null,
})

const statusOptions = [
  { label: '待审核', value: 0 },
  { label: '已审核', value: 1 },
  { label: '已完成', value: 2 },
  { label: '已关闭', value: 3 }
]

const columns: DataTableColumns = [
  { title: '订单号', key: 'orderNo', width: 180 },
  { title: '客户ID', key: 'customerId', width: 100 },
  { title: '订单日期', key: 'orderDate', width: 180 },
  { title: '订单金额', key: 'totalAmount', width: 120 },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render(row: any) {
      const statusMap: any = {
        0: { type: 'warning', text: '待审核' },
        1: { type: 'info', text: '已审核' },
        2: { type: 'success', text: '已完成' },
        3: { type: 'error', text: '已关闭' }
      }
      const status = statusMap[row.status] || { type: 'default', text: '未知' }
      return h(NTag, { type: status.type }, { default: () => status.text })
    }
  },
  { title: '备注', key: 'remark', ellipsis: { tooltip: true } },
  { title: '创建时间', key: 'createTime', width: 180 },
  {
    title: '操作',
    key: 'actions',
    width: 280,
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
                type: 'info',
                text: true,
                onClick: () => handleView(row)
              },
              { default: () => '查看' }
            ),
            row.status === 0 && h(
              NButton,
              {
                size: 'small',
                type: 'primary',
                text: true,
                onClick: () => handleAudit(row)
              },
              { default: () => '审核' }
            ),
            row.status === 1 && h(
              NButton,
              {
                size: 'small',
                type: 'success',
                text: true,
                onClick: () => handleComplete(row)
              },
              { default: () => '完成' }
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
    const res: any = await request.get('/sales/v1/orders', { params: queryParams })
    tableData.value = res.data.list
    pagination.itemCount = res.data.total
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.orderNo = ''
  queryParams.status = null
  queryParams.pageNum = 1
  handleQuery()
}

const handleAdd = () => {
  message.info('新增销售订单功能 - 待实现')
}

const handleView = (row: any) => {
  message.info(`查看订单: ${row.orderNo}`)
}

const handleAudit = async (row: any) => {
  dialog.info({
    title: '提示',
    content: `确认审核订单"${row.orderNo}"？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await request.put(`/sales/v1/orders/${row.id}/audit`)
      message.success('审核成功')
      handleQuery()
    }
  })
}

const handleComplete = async (row: any) => {
  dialog.info({
    title: '提示',
    content: `确认完成订单"${row.orderNo}"？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await request.put(`/sales/v1/orders/${row.id}/complete`)
      message.success('操作成功')
      handleQuery()
    }
  })
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: `确认删除订单"${row.orderNo}"？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await request.delete(`/sales/v1/orders/${row.id}`)
      message.success('删除成功')
      handleQuery()
    }
  })
}

// 初始化
handleQuery()
</script>
