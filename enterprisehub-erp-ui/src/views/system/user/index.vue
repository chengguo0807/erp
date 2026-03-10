<template>
  <n-card title="用户管理" :bordered="false">
    <n-space vertical :size="16">
      <!-- 搜索栏 -->
      <n-form inline :model="queryParams">
        <n-form-item label="用户名">
          <n-input v-model:value="queryParams.username" placeholder="请输入用户名" clearable />
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
          新增用户
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
    <n-modal v-model:show="showModal" :mask-closable="false" preset="dialog" :title="modalTitle" style="width: 700px">
      <n-form ref="formRef" :model="formData" :rules="formRules" label-placement="left" label-width="100px">
        <n-grid :cols="2" :x-gap="24">
          <n-form-item-gi label="用户名" path="username">
            <n-input v-model:value="formData.username" placeholder="请输入用户名" :disabled="!!formData.id" />
          </n-form-item-gi>
          <n-form-item-gi label="昵称" path="nickname">
            <n-input v-model:value="formData.nickname" placeholder="请输入昵称" />
          </n-form-item-gi>
          <n-form-item-gi label="真实姓名" path="realName">
            <n-input v-model:value="formData.realName" placeholder="请输入真实姓名" />
          </n-form-item-gi>
          <n-form-item-gi label="手机号" path="phone">
            <n-input v-model:value="formData.phone" placeholder="请输入手机号" />
          </n-form-item-gi>
          <n-form-item-gi label="邮箱" path="email">
            <n-input v-model:value="formData.email" placeholder="请输入邮箱" />
          </n-form-item-gi>
          <n-form-item-gi label="性别" path="gender">
            <n-select v-model:value="formData.gender" :options="genderOptions" placeholder="请选择性别" />
          </n-form-item-gi>
          <n-form-item-gi label="所属部门" path="deptId">
            <n-tree-select
              v-model:value="formData.deptId"
              :options="deptTreeOptions"
              placeholder="请选择部门"
              clearable
              key-field="id"
              label-field="deptName"
              children-field="children"
            />
          </n-form-item-gi>
          <n-form-item-gi label="用户角色" path="roleIds" :span="2">
            <n-select
              v-model:value="formData.roleIds"
              :options="roleOptions"
              placeholder="请选择角色"
              multiple
              clearable
            />
          </n-form-item-gi>
          <n-form-item-gi label="状态" path="status">
            <n-radio-group v-model:value="formData.status">
              <n-radio :value="0">正常</n-radio>
              <n-radio :value="1">停用</n-radio>
            </n-radio-group>
          </n-form-item-gi>
          <n-form-item-gi v-if="!formData.id" label="密码" path="password" :span="2">
            <n-input v-model:value="formData.password" type="password" placeholder="请输入密码" show-password-on="click" />
          </n-form-item-gi>
        </n-grid>
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
import { useMessage, useDialog, NButton, NTag, NSpace } from 'naive-ui'
import { SearchOutline, RefreshOutline, AddOutline } from '@vicons/ionicons5'
import { getUserList, createUser, updateUser, deleteUser, getDeptTree, getRoleList } from '@/api/system'
import type { DataTableColumns, FormInst, FormRules } from 'naive-ui'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const tableData = ref([])
const showModal = ref(false)
const modalTitle = ref('')
const submitLoading = ref(false)
const formRef = ref<FormInst | null>(null)
const deptTreeOptions = ref([])
const roleOptions = ref<Array<{ label: string; value: number }>>([])

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  username: '',
  status: null as number | null,
})

const formData = reactive({
  id: null as number | null,
  username: '',
  password: '',
  nickname: '',
  realName: '',
  email: '',
  phone: '',
  gender: 2,
  deptId: null as number | null,
  roleIds: [] as number[],
  status: 0,
})

const statusOptions = [
  { label: '正常', value: 0 },
  { label: '停用', value: 1 }
]

const genderOptions = [
  { label: '男', value: 0 },
  { label: '女', value: 1 },
  { label: '未知', value: 2 }
]

const formRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6-20个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

const columns: DataTableColumns = [
  { title: '用户名', key: 'username', width: 120 },
  { title: '昵称', key: 'nickname', width: 120 },
  { title: '真实姓名', key: 'realName', width: 120 },
  { title: '手机号', key: 'phone', width: 130 },
  { title: '邮箱', key: 'email', width: 180 },
  {
    title: '性别',
    key: 'gender',
    width: 80,
    render(row: any) {
      const genderMap: any = { 0: '男', 1: '女', 2: '未知' }
      return genderMap[row.gender] || '未知'
    }
  },
  {
    title: '角色',
    key: 'roles',
    width: 150,
    render(row: any) {
      if (!row.roles || row.roles.length === 0) {
        return h(NTag, { type: 'default', size: 'small' }, { default: () => '未分配' })
      }
      return h(
        NSpace,
        { size: 'small' },
        {
          default: () => row.roles.map((role: any) => 
            h(NTag, { type: 'info', size: 'small' }, { default: () => role.roleName || role })
          )
        }
      )
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
                type: 'warning',
                text: true,
                onClick: () => handleResetPassword(row)
              },
              { default: () => '重置密码' }
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
    const res: any = await getUserList(queryParams)
    console.log('用户列表响应:', res)
    console.log('res.data:', res.data)
    // 兼容 list 和 records 两种字段名
    tableData.value = res.data.list || res.data.records || []
    pagination.itemCount = res.data.total || 0
    console.log('tableData.value:', tableData.value)
    console.log('pagination.itemCount:', pagination.itemCount)
  } catch (error) {
    console.error('查询用户列表失败:', error)
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

const loadDeptTree = async () => {
  try {
    const res: any = await getDeptTree()
    deptTreeOptions.value = res.data || []
  } catch (error) {
    // 错误已在拦截器中处理
  }
}

const loadRoleList = async () => {
  try {
    const res: any = await getRoleList({ pageNum: 1, pageSize: 100 })
    const roles = res.data.list || res.data.records || []
    roleOptions.value = roles.map((role: any) => ({
      label: role.roleName,
      value: role.id
    }))
  } catch (error) {
    console.error('加载角色列表失败:', error)
  }
}

const handleReset = () => {
  queryParams.username = ''
  queryParams.status = null
  queryParams.pageNum = 1
  handleQuery()
}

const resetForm = () => {
  formData.id = null
  formData.username = ''
  formData.password = ''
  formData.nickname = ''
  formData.realName = ''
  formData.email = ''
  formData.phone = ''
  formData.gender = 2
  formData.deptId = null
  formData.roleIds = []
  formData.status = 0
}

const handleAdd = async () => {
  resetForm()
  await Promise.all([loadDeptTree(), loadRoleList()])
  modalTitle.value = '新增用户'
  showModal.value = true
}

const handleEdit = async (row: any) => {
  resetForm()
  await Promise.all([loadDeptTree(), loadRoleList()])
  formData.id = row.id
  formData.username = row.username
  formData.nickname = row.nickname
  formData.realName = row.realName || ''
  formData.email = row.email || ''
  formData.phone = row.phone || ''
  formData.gender = row.gender
  formData.deptId = row.deptId
  formData.roleIds = row.roleIds || []
  formData.status = row.status
  modalTitle.value = '编辑用户'
  showModal.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    submitLoading.value = true
    
    if (formData.id) {
      await updateUser(formData.id, formData)
      message.success('修改成功')
    } else {
      await createUser(formData)
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

const handleResetPassword = (row: any) => {
  dialog.warning({
    title: '提示',
    content: `确认重置用户"${row.username}"的密码为默认密码？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: () => {
      message.success('密码重置成功,默认密码: 123456')
    }
  })
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: `确认删除用户"${row.username}"？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await deleteUser(row.id)
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
