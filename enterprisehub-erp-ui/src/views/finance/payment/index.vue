<template>
  <n-card title="收付款管理" :bordered="false">
    <n-space vertical :size="16">
      <!-- 搜索栏 -->
      <n-form inline :model="queryParams">
        <n-form-item label="类型">
          <n-select
            v-model:value="queryParams.paymentType"
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

      <!-- 操作栏 -->
      <n-space>
        <n-button type="primary" @click="handleAdd">
          <template #icon>
            <n-icon :component="AddOutline" />
          </template>
          新增收付款
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
  paymentType: null as number | null,
})

const typeOptions = [
  { label: '收款', value: 0 },
  { label: '付款', value: 1 }
]

const columns: DataTableColumns = [
  { title: '单据编号', key: 'paymentNo', width: 180 },
  {
    title: '类型',
    key: 'paymentType',
    width: 80,
    render(row: any) {
      return h(
        NTag,
        { type: row.paymentType === 0 ? 'success' : 'warning' },
        { default: () => row.paymentType === 0 ? '收款' : '付款' }
      )
    }
  },
  { title: '金额', key: 'amount', width: 120 },
  { title: '付款方式', key: 'paymentMethod', width: 120 },
  { title: '交易日期', key: 'paymentDate', width: 180 },
  { title: '备注', key: 'remark', ellipsis: { tooltip: true } },
  { title: '创建时间', key: 'createTime', width: 180 },
  {
    title: '操作',
    key: 'actions',
    width: 200,
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
    const res: any = await request.get('/finance/v1/payments', { params: queryParams })
    tableData.value = res.data.list
    pagination.itemCount = res.data.total
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.paymentType = null
  queryParams.pageNum = 1
  handleQuery()
}

const handleAdd = () => {
  message.info('新增收付款功能 - 待实现')
}

const handleView = (row: any) => {
  message.info(`查看收付款: ${row.paymentNo}`)
}

const handleEdit = (row: any) => {
  message.info(`编辑收付款: ${row.paymentNo}`)
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: `确认删除收付款记录"${row.paymentNo}"？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await request.delete(`/finance/v1/payments/${row.id}`)
      message.success('删除成功')
      handleQuery()
    }
  })
}

// 初始化
handleQuery()
</script>
