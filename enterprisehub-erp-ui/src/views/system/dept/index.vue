<template>
  <n-card title="部门管理" :bordered="false">
    <n-space vertical :size="16">
      <!-- 搜索栏 -->
      <n-form inline :model="queryParams">
        <n-form-item label="部门名称">
          <n-input v-model:value="queryParams.deptName" placeholder="请输入部门名称" clearable />
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
          新增部门
        </n-button>
        <n-button @click="handleExpand">
          <template #icon>
            <n-icon :component="expandAll ? ChevronUpOutline : ChevronDownOutline" />
          </template>
          {{ expandAll ? '折叠' : '展开' }}
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

    <!-- 新增/编辑弹窗 -->
    <n-modal v-model:show="showModal" :mask-closable="false" preset="dialog" :title="modalTitle" style="width: 600px">
      <n-form ref="formRef" :model="formData" :rules="formRules" label-placement="left" label-width="100px">
        <n-form-item label="上级部门" path="parentId">
          <n-tree-select
            v-model:value="formData.parentId"
            :options="deptTreeOptions"
            placeholder="请选择上级部门"
            clearable
            key-field="id"
            label-field="deptName"
            children-field="children"
          />
        </n-form-item>
        <n-form-item label="部门名称" path="deptName">
          <n-input v-model:value="formData.deptName" placeholder="请输入部门名称" />
        </n-form-item>
        <n-form-item label="显示排序" path="sort">
          <n-input-number v-model:value="formData.sort" placeholder="请输入排序" :min="0" style="width: 100%" />
        </n-form-item>
        <n-form-item label="负责人" path="leader">
          <n-input v-model:value="formData.leader" placeholder="请输入负责人" />
        </n-form-item>
        <n-form-item label="联系电话" path="phone">
          <n-input v-model:value="formData.phone" placeholder="请输入联系电话" />
        </n-form-item>
        <n-form-item label="邮箱" path="email">
          <n-input v-model:value="formData.email" placeholder="请输入邮箱" />
        </n-form-item>
        <n-form-item label="状态" path="status">
          <n-radio-group v-model:value="formData.status">
            <n-radio :value="0">正常</n-radio>
            <n-radio :value="1">停用</n-radio>
          </n-radio-group>
        </n-form-item>
      </n-form>
      <template #action>
        <n-space>
          <n-button @click="showModal = false">取消</n-button>
          <n-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</n-button>
        </n-space>
      </template>
    </n-modal>
  </n-card>
</template>

<script setup lang="ts">
import { ref, reactive, h } from 'vue'
import { useMessage, useDialog, NButton, NSpace, NTag } from 'naive-ui'
import { SearchOutline, RefreshOutline, AddOutline, ChevronUpOutline, ChevronDownOutline } from '@vicons/ionicons5'
import { getDeptList, getDeptTree, createDept, updateDept, deleteDept } from '@/api/system'
import type { DataTableColumns, FormInst, FormRules } from 'naive-ui'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const tableData = ref([])
const expandAll = ref(true)
const showModal = ref(false)
const modalTitle = ref('')
const submitLoading = ref(false)
const formRef = ref<FormInst | null>(null)
const deptTreeOptions = ref<any[]>([])

const queryParams = reactive({
  deptName: '',
})

const formData = reactive({
  id: null as number | null,
  parentId: 0,
  deptName: '',
  sort: 0,
  leader: '',
  phone: '',
  email: '',
  status: 0,
})

const formRules: FormRules = {
  deptName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ],
  sort: [
    { required: true, type: 'number', message: '请输入排序', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

const columns: DataTableColumns = [
  { title: '部门名称', key: 'deptName', width: 200 },
  { title: '排序', key: 'sort', width: 80 },
  { title: '负责人', key: 'leader', width: 120 },
  { title: '联系电话', key: 'phone', width: 130 },
  { title: '邮箱', key: 'email', width: 180 },
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
  { 
    title: '创建时间', 
    key: 'createTime', 
    width: 180,
    render(row: any) {
      return row.createTime ? row.createTime.replace('T', ' ') : ''
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 200,
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
                type: 'info',
                text: true,
                onClick: () => handleAddChild(row)
              },
              { default: () => '新增下级' }
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
    const res: any = await getDeptList(queryParams)
    tableData.value = res.data || []
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

const loadDeptTree = async () => {
  try {
    const res: any = await getDeptTree()
    deptTreeOptions.value = [
      { id: 0, deptName: '顶级部门', children: res.data || [] }
    ]
  } catch (error) {
    // 错误已在拦截器中处理
  }
}

const handleReset = () => {
  queryParams.deptName = ''
  handleQuery()
}

const handleExpand = () => {
  expandAll.value = !expandAll.value
  message.info(expandAll.value ? '已展开全部' : '已折叠全部')
}

const resetForm = () => {
  formData.id = null
  formData.parentId = 0
  formData.deptName = ''
  formData.sort = 0
  formData.leader = ''
  formData.phone = ''
  formData.email = ''
  formData.status = 0
}

const handleAdd = async () => {
  resetForm()
  await loadDeptTree()
  modalTitle.value = '新增部门'
  showModal.value = true
}

const handleEdit = async (row: any) => {
  resetForm()
  await loadDeptTree()
  formData.id = row.id
  formData.parentId = row.parentId || 0
  formData.deptName = row.deptName
  formData.sort = row.sort
  formData.leader = row.leader || ''
  formData.phone = row.phone || ''
  formData.email = row.email || ''
  formData.status = row.status
  modalTitle.value = '编辑部门'
  showModal.value = true
}

const handleAddChild = async (row: any) => {
  resetForm()
  await loadDeptTree()
  formData.parentId = row.id
  modalTitle.value = '新增下级部门'
  showModal.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    submitLoading.value = true
    
    if (formData.id) {
      await updateDept(formData.id, formData)
      message.success('修改成功')
    } else {
      await createDept(formData)
      message.success('新增成功')
    }
    
    showModal.value = false
    handleQuery()
  } catch (error) {
    // 表单验证失败或请求失败
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: `确认删除部门"${row.deptName}"？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await deleteDept(row.id)
        message.success('删除成功')
        await handleQuery()
      } catch (error) {
        console.error('删除失败:', error)
      }
    }
  })
}

// 初始化
handleQuery()
</script>
