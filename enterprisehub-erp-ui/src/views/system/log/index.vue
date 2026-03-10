<template>
  <n-card title="操作日志" :bordered="false">
    <n-space vertical :size="16">
      <!-- 搜索栏 -->
      <n-form inline :model="queryParams">
        <n-form-item label="操作模块">
          <n-input v-model:value="queryParams.title" placeholder="请输入操作模块" clearable />
        </n-form-item>
        <n-form-item label="操作人员">
          <n-input v-model:value="queryParams.operName" placeholder="请输入操作人员" clearable />
        </n-form-item>
        <n-form-item label="业务类型">
          <n-select
            v-model:value="queryParams.businessType"
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
        <n-button type="error" @click="handleClear">
          <template #icon>
            <n-icon :component="TrashOutline" />
          </template>
          清空日志
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
import { SearchOutline, RefreshOutline, TrashOutline } from '@vicons/ionicons5'
import request from '@/utils/request'
import type { DataTableColumns } from 'naive-ui'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const tableData = ref([])

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  title: '',
  operName: '',
  businessType: null as number | null,
})

const typeOptions = [
  { label: '新增', value: 1 },
  { label: '修改', value: 2 },
  { label: '删除', value: 3 },
  { label: '导入', value: 4 },
  { label: '导出', value: 5 }
]

const columns: DataTableColumns = [
  { title: '日志ID', key: 'id', width: 80 },
  { title: '操作模块', key: 'title', width: 150 },
  {
    title: '业务类型',
    key: 'businessType',
    width: 100,
    render(row: any) {
      const typeMap: any = {
        1: { type: 'success', text: '新增' },
        2: { type: 'info', text: '修改' },
        3: { type: 'error', text: '删除' },
        4: { type: 'warning', text: '导入' },
        5: { type: 'default', text: '导出' }
      }
      const type = typeMap[row.businessType] || { type: 'default', text: '其他' }
      return h(NTag, { type: type.type }, { default: () => type.text })
    }
  },
  { title: '请求方法', key: 'method', width: 150, ellipsis: { tooltip: true } },
  { title: '请求方式', key: 'requestMethod', width: 100 },
  { title: '操作人员', key: 'operName', width: 120 },
  { title: '操作IP', key: 'operIp', width: 130 },
  { title: '操作地点', key: 'operLocation', width: 120 },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render(row: any) {
      return h(
        NTag,
        { type: row.status === 0 ? 'success' : 'error' },
        { default: () => row.status === 0 ? '成功' : '失败' }
      )
    }
  },
  { title: '操作时间', key: 'operTime', width: 180 },
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
                type: 'info',
                text: true,
                onClick: () => handleView(row)
              },
              { default: () => '详情' }
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
    const res: any = await request.get('/system/v1/operation-logs', { params: queryParams })
    tableData.value = res.data.list
    pagination.itemCount = res.data.total
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.title = ''
  queryParams.operName = ''
  queryParams.businessType = null
  queryParams.pageNum = 1
  handleQuery()
}

const handleView = (row: any) => {
  message.info(`查看日志详情: ${row.id}`)
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: `确认删除该条日志记录？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await request.delete(`/system/v1/operation-logs/${row.id}`)
      message.success('删除成功')
      handleQuery()
    }
  })
}

const handleClear = () => {
  dialog.warning({
    title: '警告',
    content: '确认清空所有操作日志？此操作不可恢复！',
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await request.delete('/system/v1/operation-logs/clear')
      message.success('清空成功')
      handleQuery()
    }
  })
}

// 初始化
handleQuery()
</script>
