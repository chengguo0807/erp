<template>
  <n-card title="角色管理" :bordered="false">
    <n-space vertical :size="16">
      <!-- 搜索栏 -->
      <n-form inline :model="queryParams">
        <n-form-item label="角色名称">
          <n-input v-model:value="queryParams.roleName" placeholder="请输入角色名称" clearable />
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
          新增角色
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

    <!-- 新增/编辑弹窗 -->
    <n-modal v-model:show="showModal" :mask-closable="false" preset="dialog" :title="modalTitle" style="width: 600px">
      <n-form ref="formRef" :model="formData" :rules="formRules" label-placement="left" label-width="100px">
        <n-form-item label="角色名称" path="roleName">
          <n-input v-model:value="formData.roleName" placeholder="请输入角色名称" />
        </n-form-item>
        <n-form-item label="角色编码" path="roleKey">
          <n-input v-model:value="formData.roleKey" placeholder="请输入角色编码" />
        </n-form-item>
        <n-form-item label="显示排序" path="sort">
          <n-input-number v-model:value="formData.sort" placeholder="请输入排序" :min="0" style="width: 100%" />
        </n-form-item>
        <n-form-item label="数据权限" path="dataScope">
          <n-select v-model:value="formData.dataScope" :options="dataScopeOptions" placeholder="请选择数据权限" />
        </n-form-item>
        <n-form-item label="状态" path="status">
          <n-radio-group v-model:value="formData.status">
            <n-radio :value="0">正常</n-radio>
            <n-radio :value="1">停用</n-radio>
          </n-radio-group>
        </n-form-item>
        <n-form-item label="备注" path="remark">
          <n-input v-model:value="formData.remark" type="textarea" placeholder="请输入备注" :rows="3" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-space>
          <n-button @click="showModal = false">取消</n-button>
          <n-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</n-button>
        </n-space>
      </template>
    </n-modal>

    <!-- 权限分配弹窗 -->
    <n-modal v-model:show="showPermissionModal" :mask-closable="false" preset="dialog" title="权限分配" style="width: 500px">
      <n-spin :show="permissionLoading">
        <n-tree
          ref="treeRef"
          :data="menuTreeData"
          checkable
          cascade
          :checked-keys="checkedKeys"
          @update:checked-keys="handleCheckedKeysChange"
          key-field="id"
          label-field="menuName"
          children-field="children"
        />
      </n-spin>
      <template #action>
        <n-space>
          <n-button @click="showPermissionModal = false">取消</n-button>
          <n-button type="primary" :loading="submitLoading" @click="handlePermissionSubmit">确定</n-button>
        </n-space>
      </template>
    </n-modal>
  </n-card>
</template>

<script setup lang="ts">
import { ref, reactive, h } from 'vue'
import { useMessage, useDialog, NButton, NSpace, NTag } from 'naive-ui'
import { SearchOutline, RefreshOutline, AddOutline } from '@vicons/ionicons5'
import { getRoleList, createRole, updateRole, deleteRole } from '@/api/system'
import { getMenuTree } from '@/api/system'
import type { DataTableColumns, FormInst, FormRules } from 'naive-ui'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const tableData = ref([])
const showModal = ref(false)
const modalTitle = ref('')
const submitLoading = ref(false)
const formRef = ref<FormInst | null>(null)
const showPermissionModal = ref(false)
const permissionLoading = ref(false)
const menuTreeData = ref([])
const checkedKeys = ref<number[]>([])
const currentRoleId = ref<number | null>(null)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleName: '',
})

const formData = reactive({
  id: null as number | null,
  roleName: '',
  roleKey: '',
  sort: 0,
  dataScope: 1,
  status: 0,
  remark: '',
})

const dataScopeOptions = [
  { label: '全部数据权限', value: 1 },
  { label: '自定义数据权限', value: 2 },
  { label: '本部门数据权限', value: 3 },
  { label: '本部门及以下数据权限', value: 4 },
  { label: '仅本人数据权限', value: 5 }
]

const formRules: FormRules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  roleKey: [
    { required: true, message: '请输入角色编码', trigger: 'blur' }
  ],
  sort: [
    { required: true, type: 'number', message: '请输入排序', trigger: 'blur' }
  ]
}

const columns: DataTableColumns = [
  { title: '角色名称', key: 'roleName', width: 150 },
  { title: '角色编码', key: 'roleKey', width: 150 },
  { title: '排序', key: 'sort', width: 80 },
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
  { title: '备注', key: 'remark' },
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
    width: 250,
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
                onClick: () => handlePermission(row)
              },
              { default: () => '权限分配' }
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
  itemCount: 0,
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
    const res: any = await getRoleList(queryParams)
    tableData.value = res.data.list || res.data.records || []
    pagination.itemCount = res.data.total || 0
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  queryParams.roleName = ''
  queryParams.pageNum = 1
  handleQuery()
}

const resetForm = () => {
  formData.id = null
  formData.roleName = ''
  formData.roleKey = ''
  formData.sort = 0
  formData.dataScope = 1
  formData.status = 0
  formData.remark = ''
}

const handleAdd = () => {
  resetForm()
  modalTitle.value = '新增角色'
  showModal.value = true
}

const handleEdit = (row: any) => {
  resetForm()
  formData.id = row.id
  formData.roleName = row.roleName
  formData.roleKey = row.roleKey
  formData.sort = row.sort
  formData.dataScope = row.dataScope
  formData.status = row.status
  formData.remark = row.remark || ''
  modalTitle.value = '编辑角色'
  showModal.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    submitLoading.value = true
    
    if (formData.id) {
      await updateRole(formData.id, formData)
      message.success('修改成功')
    } else {
      await createRole(formData)
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

const handlePermission = async (row: any) => {
  currentRoleId.value = row.id
  permissionLoading.value = true
  showPermissionModal.value = true
  
  try {
    // 加载菜单树
    const res: any = await getMenuTree()
    menuTreeData.value = res.data || []
    
    // TODO: 加载角色已有的权限
    checkedKeys.value = []
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    permissionLoading.value = false
  }
}

const handleCheckedKeysChange = (keys: number[]) => {
  checkedKeys.value = keys
}

const handlePermissionSubmit = async () => {
  try {
    submitLoading.value = true
    // TODO: 调用权限分配API
    message.success('权限分配成功')
    showPermissionModal.value = false
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: `确认删除角色"${row.roleName}"？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await deleteRole(row.id)
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
