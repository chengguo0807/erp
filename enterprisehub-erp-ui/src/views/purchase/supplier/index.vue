<template>
  <div class="supplier-container">
    <n-card title="供应商管理">
      <n-space vertical :size="16">
        <n-space justify="space-between">
          <n-space>
            <n-input v-model:value="queryParams.name" placeholder="供应商名称" clearable style="width: 200px" />
            <n-input v-model:value="queryParams.contact" placeholder="联系人" clearable style="width: 200px" />
            <n-button type="primary" @click="handleQuery">
              <template #icon><n-icon><SearchOutline /></n-icon></template>
              查询
            </n-button>
            <n-button @click="handleReset">
              <template #icon><n-icon><RefreshOutline /></n-icon></template>
              重置
            </n-button>
          </n-space>
          <n-button type="primary" @click="handleAdd">
            <template #icon><n-icon><AddOutline /></n-icon></template>
            新增供应商
          </n-button>
        </n-space>

        <n-data-table
          :columns="columns"
          :data="dataList"
          :loading="loading"
          :pagination="pagination"
          :bordered="false"
        />
      </n-space>
    </n-card>

    <n-modal v-model:show="showModal" :title="modalTitle" preset="card" style="width: 600px">
      <n-form ref="formRef" :model="formData" :rules="rules" label-placement="left" label-width="100px">
        <n-form-item label="供应商名称" path="name">
          <n-input v-model:value="formData.name" placeholder="请输入供应商名称" />
        </n-form-item>
        <n-form-item label="联系人" path="contact">
          <n-input v-model:value="formData.contact" placeholder="请输入联系人" />
        </n-form-item>
        <n-form-item label="联系电话" path="phone">
          <n-input v-model:value="formData.phone" placeholder="请输入联系电话" />
        </n-form-item>
        <n-form-item label="邮箱" path="email">
          <n-input v-model:value="formData.email" placeholder="请输入邮箱" />
        </n-form-item>
        <n-form-item label="地址" path="address">
          <n-input v-model:value="formData.address" type="textarea" placeholder="请输入地址" />
        </n-form-item>
        <n-form-item label="状态" path="status">
          <n-radio-group v-model:value="formData.status">
            <n-radio :value="1">启用</n-radio>
            <n-radio :value="0">禁用</n-radio>
          </n-radio-group>
        </n-form-item>
        <n-form-item label="备注" path="remark">
          <n-input v-model:value="formData.remark" type="textarea" placeholder="请输入备注" />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showModal = false">取消</n-button>
          <n-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted } from 'vue'
import { NButton, NSpace, NTag, useMessage, useDialog } from 'naive-ui'
import { SearchOutline, RefreshOutline, AddOutline } from '@vicons/ionicons5'
import { getSupplierList, addSupplier, updateSupplier, deleteSupplier } from '@/api/purchase'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const submitLoading = ref(false)
const showModal = ref(false)
const modalTitle = ref('')
const dataList = ref([])
const formRef = ref()

const queryParams = reactive({
  name: '',
  contact: '',
  pageNum: 1,
  pageSize: 10
})

const formData = reactive({
  id: null,
  name: '',
  contact: '',
  phone: '',
  email: '',
  address: '',
  status: 1,
  remark: ''
})

const rules = {
  name: { required: true, message: '请输入供应商名称', trigger: 'blur' },
  contact: { required: true, message: '请输入联系人', trigger: 'blur' },
  phone: { required: true, message: '请输入联系电话', trigger: 'blur' }
}

const pagination = reactive({
  page: 1,
  pageSize: 10,
  itemCount: 0,
  showSizePicker: true,
  pageSizes: [10, 20, 50, 100],
  onChange: (page: number) => {
    queryParams.pageNum = page
    getList()
  },
  onUpdatePageSize: (pageSize: number) => {
    queryParams.pageSize = pageSize
    queryParams.pageNum = 1
    getList()
  }
})

const columns = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '供应商名称', key: 'name' },
  { title: '联系人', key: 'contact' },
  { title: '联系电话', key: 'phone' },
  { title: '邮箱', key: 'email' },
  { title: '地址', key: 'address', ellipsis: { tooltip: true } },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row: any) => {
      return h(NTag, { type: row.status === 1 ? 'success' : 'error' }, 
        { default: () => row.status === 1 ? '启用' : '禁用' })
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 200,
    render: (row: any) => {
      return h(NSpace, null, {
        default: () => [
          h(NButton, { size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }),
          h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row) }, { default: () => '删除' })
        ]
      })
    }
  }
]

const getList = async () => {
  loading.value = true
  try {
    const res = await getSupplierList(queryParams)
    dataList.value = res.data.records
    pagination.itemCount = res.data.total
  } catch (error) {
    message.error('获取供应商列表失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

const handleReset = () => {
  queryParams.name = ''
  queryParams.contact = ''
  queryParams.pageNum = 1
  getList()
}

const handleAdd = () => {
  modalTitle.value = '新增供应商'
  resetForm()
  showModal.value = true
}

const handleEdit = (row: any) => {
  modalTitle.value = '编辑供应商'
  Object.assign(formData, row)
  showModal.value = true
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: '确定要删除该供应商吗？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await deleteSupplier(row.id)
        message.success('删除成功')
        getList()
      } catch (error) {
        message.error('删除失败')
      }
    }
  })
}

const handleSubmit = () => {
  formRef.value?.validate(async (errors: any) => {
    if (!errors) {
      submitLoading.value = true
      try {
        if (formData.id) {
          await updateSupplier(formData)
          message.success('更新成功')
        } else {
          await addSupplier(formData)
          message.success('新增成功')
        }
        showModal.value = false
        getList()
      } catch (error) {
        message.error('操作失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const resetForm = () => {
  formData.id = null
  formData.name = ''
  formData.contact = ''
  formData.phone = ''
  formData.email = ''
  formData.address = ''
  formData.status = 1
  formData.remark = ''
}

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.supplier-container {
  padding: 20px;
}
</style>
