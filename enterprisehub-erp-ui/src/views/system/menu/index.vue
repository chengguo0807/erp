<template>
  <n-card title="菜单管理" :bordered="false">
    <n-space vertical :size="16">
      <!-- 操作栏 -->
      <n-space>
        <n-button type="primary" @click="handleAdd">
          <template #icon>
            <n-icon :component="AddOutline" />
          </template>
          新增菜单
        </n-button>
        <n-button @click="handleExpand">
          <template #icon>
            <n-icon :component="ExpandOutline" />
          </template>
          展开/折叠
        </n-button>
      </n-space>

      <!-- 表格 -->
      <n-data-table
        :columns="columns"
        :data="tableData"
        :loading="loading"
        :bordered="false"
        :row-key="(row: any) => row.id"
        default-expand-all
      />
    </n-space>
  </n-card>
</template>

<script setup lang="ts">
import { ref, h } from 'vue'
import { useMessage, useDialog, NButton, NTag, NSpace } from 'naive-ui'
import { AddOutline, ExpandOutline } from '@vicons/ionicons5'
import request from '@/utils/request'
import type { DataTableColumns } from 'naive-ui'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const tableData = ref([])

const columns: DataTableColumns = [
  { title: '菜单名称', key: 'menuName', width: 200 },
  { title: '图标', key: 'icon', width: 100 },
  { title: '排序', key: 'sort', width: 80 },
  { title: '路由路径', key: 'path', width: 200 },
  { title: '组件路径', key: 'component', ellipsis: { tooltip: true } },
  {
    title: '类型',
    key: 'menuType',
    width: 80,
    render(row: any) {
      const typeMap: any = {
        'D': { type: 'info', text: '目录' },
        'M': { type: 'success', text: '菜单' },
        'B': { type: 'warning', text: '按钮' }
      }
      const type = typeMap[row.menuType] || { type: 'default', text: '未知' }
      return h(NTag, { type: type.type }, { default: () => type.text })
    }
  },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render(row: any) {
      return h(
        NTag,
        { type: row.status === 0 ? 'success' : 'error' },
        { default: () => row.status === 0 ? '正常' : '停用' }
      )
    }
  },
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
                type: 'success',
                text: true,
                onClick: () => handleAddChild(row)
              },
              { default: () => '新增' }
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

const handleQuery = async () => {
  loading.value = true
  try {
    const res: any = await request.get('/system/v1/menus/tree')
    tableData.value = res.data
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  message.info('新增菜单功能 - 待实现')
}

const handleAddChild = (row: any) => {
  message.info(`新增子菜单: ${row.menuName}`)
}

const handleEdit = (row: any) => {
  message.info(`编辑菜单: ${row.menuName}`)
}

const handleExpand = () => {
  message.info('展开/折叠功能 - 待实现')
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: `确认删除菜单"${row.menuName}"？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await request.delete(`/system/v1/menus/${row.id}`)
      message.success('删除成功')
      handleQuery()
    }
  })
}

// 初始化
handleQuery()
</script>
