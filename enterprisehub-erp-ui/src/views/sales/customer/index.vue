<template>
  <n-card title="客户管理" :bordered="false">
    <n-space vertical :size="16">
      <!-- 搜索栏 -->
      <n-form inline :model="queryParams">
        <n-form-item label="客户名称">
          <n-input v-model:value="queryParams.customerName" placeholder="请输入客户名称" clearable />
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
          新增客户
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
import { useMessage, useDialog, NButton, NSpace } from 'naive-ui'
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
  customerName: '',
})

const columns: DataTableColumns = [
  { title: '客户名称', key: 'customerName', width: 150 },
  { title: '联系人', key: 'contactPerson', width: 120 },
  { title: '联系电话', key: 'contactPhone', width: 130 },
  { title: '地址', key: 'address', ellipsis: { tooltip: true } },
  { title: '信用额度', key: 'creditLimit', width: 120 },
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
    const res: any = await request.get('/sales/v1/customers', { params: queryParams })
    tableData.value = res.data.list
    pagination.itemCount = res.data.total
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.customerName = ''
  queryParams.pageNum = 1
  handleQuery()
}

const handleAdd = () => {
  message.info('新增客户功能 - 待实现')
}

const handleEdit = (row: any) => {
  message.info(`编辑客户: ${row.customerName}`)
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: `确认删除客户"${row.customerName}"？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await request.delete(`/sales/v1/customers/${row.id}`)
      message.success('删除成功')
      handleQuery()
    }
  })
}

// 初始化
handleQuery()
</script>
