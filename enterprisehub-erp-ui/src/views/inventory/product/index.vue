<template>
  <div class="product-container">
    <n-card title="商品管理">
      <n-space vertical :size="16">
        <n-space justify="space-between">
          <n-space>
            <n-input v-model:value="queryParams.productName" placeholder="商品名称" clearable style="width: 200px" />
            <n-input v-model:value="queryParams.productCode" placeholder="商品编码" clearable style="width: 200px" />
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
            新增商品
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

    <n-modal v-model:show="showModal" :title="modalTitle" preset="card" style="width: 700px">
      <n-form ref="formRef" :model="formData" :rules="rules" label-placement="left" label-width="100px">
        <n-grid :cols="2" :x-gap="24">
          <n-form-item-gi label="商品编码" path="productCode">
            <n-input v-model:value="formData.productCode" placeholder="请输入商品编码" />
          </n-form-item-gi>
          <n-form-item-gi label="商品名称" path="productName">
            <n-input v-model:value="formData.productName" placeholder="请输入商品名称" />
          </n-form-item-gi>
          <n-form-item-gi label="规格型号" path="specification">
            <n-input v-model:value="formData.specification" placeholder="请输入规格型号" />
          </n-form-item-gi>
          <n-form-item-gi label="单位" path="unit">
            <n-input v-model:value="formData.unit" placeholder="请输入单位" />
          </n-form-item-gi>
          <n-form-item-gi label="成本价" path="costPrice">
            <n-input-number v-model:value="formData.costPrice" :min="0" :precision="2" style="width: 100%" />
          </n-form-item-gi>
          <n-form-item-gi label="销售价" path="salePrice">
            <n-input-number v-model:value="formData.salePrice" :min="0" :precision="2" style="width: 100%" />
          </n-form-item-gi>
          <n-form-item-gi label="库存预警值" path="warningStock">
            <n-input-number v-model:value="formData.warningStock" :min="0" style="width: 100%" />
          </n-form-item-gi>
          <n-form-item-gi label="状态" path="status">
            <n-radio-group v-model:value="formData.status">
              <n-radio :value="1">启用</n-radio>
              <n-radio :value="0">禁用</n-radio>
            </n-radio-group>
          </n-form-item-gi>
        </n-grid>
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
import { getProductList, createProduct, updateProduct, deleteProduct } from '@/api/inventory'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const submitLoading = ref(false)
const showModal = ref(false)
const modalTitle = ref('')
const dataList = ref([])
const formRef = ref()

const queryParams = reactive({
  productName: '',
  productCode: '',
  pageNum: 1,
  pageSize: 10
})

const formData = reactive({
  id: null,
  productCode: '',
  productName: '',
  specification: '',
  unit: '',
  costPrice: 0,
  salePrice: 0,
  warningStock: 0,
  status: 1,
  remark: ''
})

const rules = {
  productCode: { required: true, message: '请输入商品编码', trigger: 'blur' },
  productName: { required: true, message: '请输入商品名称', trigger: 'blur' },
  unit: { required: true, message: '请输入单位', trigger: 'blur' }
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
  { title: '商品编码', key: 'productCode' },
  { title: '商品名称', key: 'productName' },
  { title: '规格型号', key: 'specification' },
  { title: '单位', key: 'unit', width: 80 },
  { title: '成本价', key: 'costPrice', width: 100 },
  { title: '销售价', key: 'salePrice', width: 100 },
  { title: '库存预警', key: 'warningStock', width: 100 },
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
    const res: any = await getProductList(queryParams)
    dataList.value = res.data.records || res.data.list || []
    pagination.itemCount = res.data.total || 0
  } catch (error) {
    message.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

const handleReset = () => {
  queryParams.productName = ''
  queryParams.productCode = ''
  queryParams.pageNum = 1
  getList()
}

const handleAdd = () => {
  modalTitle.value = '新增商品'
  resetForm()
  showModal.value = true
}

const handleEdit = (row: any) => {
  modalTitle.value = '编辑商品'
  Object.assign(formData, row)
  showModal.value = true
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: '确定要删除该商品吗？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await deleteProduct(row.id)
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
          await updateProduct(formData)
          message.success('更新成功')
        } else {
          await createProduct(formData)
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
  formData.productCode = ''
  formData.productName = ''
  formData.specification = ''
  formData.unit = ''
  formData.costPrice = 0
  formData.salePrice = 0
  formData.warningStock = 0
  formData.status = 1
  formData.remark = ''
}

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.product-container {
  padding: 20px;
}
</style>
