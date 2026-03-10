<template>
  <n-card title="费用管理" :bordered="false">
    <n-space vertical :size="16">
      <!-- 搜索栏 -->
      <n-form inline :model="queryParams">
        <n-form-item label="费用类型">
          <n-select
            v-model:value="queryParams.expenseType"
            placeholder="请选择类型"
            clearable
            :options="typeOptions"
            style="width: 150px"
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
          新增费用
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
  expenseType: null as number | null,
})

const typeOptions = [
  { label: '办公费用', value: 0 },
  { label: '差旅费用', value: 1 },
  { label: '运输费用', value: 2 },
  { label: '其他费用', value: 3 }
]

const columns: DataTableColumns = [
  { title: '费用编号', key: 'expenseNo', width: 180 },
  {
    title: '费用类型',
    key: 'expenseType',
    width: 120,
    render(row: any) {
      const typeMap: any = {
        0: { type: 'info', text: '办公费用' },
        1: { type: 'warning', text: '差旅费用' },
        2: { type: 'success', text: '运输费用' },
        3: { type: 'default', text: '其他费用' }
      }
      const type = typeMap[row.expenseType] || { type: 'default', text: '未知' }
      return h(NTag, { type: type.type }, { default: () => type.text })
    }
  },
  { title: '费用金额', key: 'amount', width: 120 },
  { title: '申请人', key: 'applicant', width: 120 },
  { title: '申请日期', key: 'applyDate', width: 180 },
  { title: '费用说明', key: 'description', ellipsis: { tooltip: true } },
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
    const res: any = await request.get('/finance/v1/expenses', { params: queryParams })
    tableData.value = res.data.list
    pagination.itemCount = res.data.total
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.expenseType = null
  queryParams.pageNum = 1
  handleQuery()
}

const handleAdd = () => {
  message.info('新增费用功能 - 待实现')
}

const handleView = (row: any) => {
  message.info(`查看费用: ${row.expenseNo}`)
}

const handleEdit = (row: any) => {
  message.info(`编辑费用: ${row.expenseNo}`)
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: `确认删除费用记录"${row.expenseNo}"？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await request.delete(`/finance/v1/expenses/${row.id}`)
      message.success('删除成功')
      handleQuery()
    }
  })
}

// 初始化
handleQuery()
</script>
