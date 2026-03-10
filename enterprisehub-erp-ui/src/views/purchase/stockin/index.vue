<template>
  <div class="stockin-container">
    <n-card title="入库管理">
      <n-space vertical :size="16">
        <n-space justify="space-between">
          <n-space>
            <n-input v-model:value="queryParams.stockInNo" placeholder="入库单号" clearable style="width: 200px" />
            <n-select v-model:value="queryParams.status" placeholder="状态" clearable style="width: 150px" :options="statusOptions" />
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
            新增入库单
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

    <n-modal v-model:show="showModal" :title="modalTitle" preset="card" style="width: 900px">
      <n-form ref="formRef" :model="formData" label-placement="left" label-width="100px">
        <n-grid :cols="2" :x-gap="24">
          <n-form-item-gi label="采购订单">
            <n-select v-model:value="formData.purchaseOrderId" placeholder="请选择采购订单" :options="orderOptions" />
          </n-form-item-gi>
          <n-form-item-gi label="仓库">
            <n-select v-model:value="formData.warehouseId" placeholder="请选择仓库" :options="warehouseOptions" />
          </n-form-item-gi>
          <n-form-item-gi label="入库日期">
            <n-date-picker v-model:value="formData.stockInDate" type="date" style="width: 100%" />
          </n-form-item-gi>
          <n-form-item-gi label="状态">
            <n-select v-model:value="formData.status" :options="statusOptions" />
          </n-form-item-gi>
        </n-grid>
        <n-form-item label="备注">
          <n-input v-model:value="formData.remark" type="textarea" placeholder="请输入备注" />
        </n-form-item>
        
        <n-divider>入库明细</n-divider>
        <n-space vertical>
          <n-button @click="handleAddItem" size="small">添加商品</n-button>
          <n-data-table :columns="itemColumns" :data="formData.items" :pagination="false" size="small" />
        </n-space>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showModal = false">取消</n-button>
          <n-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</n-button>
        </n-space>
      </template>
    </n-modal>

    <n-modal v-model:show="showItemModal" title="添加商品" preset="card" style="width: 500px">
      <n-form :model="itemForm" label-placement="left" label-width="100px">
        <n-form-item label="商品名称">
          <n-input v-model:value="itemForm.productName" placeholder="请输入商品名称" />
        </n-form-item>
        <n-form-item label="入库数量">
          <n-input-number v-model:value="itemForm.quantity" :min="1" style="width: 100%" />
        </n-form-item>
        <n-form-item label="单价">
          <n-input-number v-model:value="itemForm.price" :min="0" :precision="2" style="width: 100%" />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showItemModal = false">取消</n-button>
          <n-button type="primary" @click="handleConfirmItem">确定</n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted } from 'vue'
import { NButton, NSpace, NTag, useMessage, useDialog } from 'naive-ui'
import { SearchOutline, RefreshOutline, AddOutline } from '@vicons/ionicons5'
import { getStockInList, addStockIn, updateStockIn, deleteStockIn, auditStockIn } from '@/api/purchase'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const submitLoading = ref(false)
const showModal = ref(false)
const showItemModal = ref(false)
const modalTitle = ref('')
const dataList = ref([])
const formRef = ref()
const orderOptions = ref([])
const warehouseOptions = ref([])

const statusOptions = [
  { label: '待审核', value: 0 },
  { label: '已审核', value: 1 },
  { label: '已完成', value: 2 }
]

const queryParams = reactive({
  stockInNo: '',
  status: null,
  pageNum: 1,
  pageSize: 10
})

const formData = reactive({
  id: null,
  purchaseOrderId: null,
  warehouseId: null,
  stockInDate: Date.now(),
  status: 0,
  remark: '',
  items: []
})

const itemForm = reactive({
  productName: '',
  quantity: 1,
  price: 0
})

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
  { title: '入库单号', key: 'stockInNo' },
  { title: '采购订单号', key: 'purchaseOrderNo' },
  { title: '仓库', key: 'warehouseName' },
  { title: '入库日期', key: 'stockInDate' },
  { title: '总金额', key: 'totalAmount' },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row: any) => {
      const statusMap: any = {
        0: { type: 'warning', text: '待审核' },
        1: { type: 'info', text: '已审核' },
        2: { type: 'success', text: '已完成' }
      }
      const status = statusMap[row.status]
      return h(NTag, { type: status.type }, { default: () => status.text })
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 280,
    render: (row: any) => {
      return h(NSpace, null, {
        default: () => [
          h(NButton, { size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }),
          row.status === 0 && h(NButton, { size: 'small', type: 'info', onClick: () => handleAudit(row) }, { default: () => '审核' }),
          h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row) }, { default: () => '删除' })
        ]
      })
    }
  }
]

const itemColumns = [
  { title: '商品名称', key: 'productName' },
  { title: '入库数量', key: 'quantity' },
  { title: '单价', key: 'price' },
  { 
    title: '小计', 
    key: 'amount',
    render: (row: any) => (row.quantity * row.price).toFixed(2)
  },
  {
    title: '操作',
    key: 'actions',
    width: 100,
    render: (row: any, index: number) => {
      return h(NButton, { 
        size: 'small', 
        type: 'error',
        onClick: () => formData.items.splice(index, 1)
      }, { default: () => '删除' })
    }
  }
]

const getList = async () => {
  loading.value = true
  try {
    const res = await getStockInList(queryParams)
    dataList.value = res.data.records
    pagination.itemCount = res.data.total
  } catch (error) {
    message.error('获取入库单列表失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

const handleReset = () => {
  queryParams.stockInNo = ''
  queryParams.status = null
  queryParams.pageNum = 1
  getList()
}

const handleAdd = () => {
  modalTitle.value = '新增入库单'
  resetForm()
  showModal.value = true
}

const handleEdit = (row: any) => {
  modalTitle.value = '编辑入库单'
  Object.assign(formData, row)
  showModal.value = true
}

const handleAudit = (row: any) => {
  dialog.info({
    title: '提示',
    content: '确定要审核该入库单吗？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await auditStockIn(row.id)
        message.success('审核成功')
        getList()
      } catch (error) {
        message.error('审核失败')
      }
    }
  })
}

const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: '确定要删除该入库单吗？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await deleteStockIn(row.id)
        message.success('删除成功')
        getList()
      } catch (error) {
        message.error('删除失败')
      }
    }
  })
}

const handleAddItem = () => {
  itemForm.productName = ''
  itemForm.quantity = 1
  itemForm.price = 0
  showItemModal.value = true
}

const handleConfirmItem = () => {
  if (!itemForm.productName) {
    message.warning('请输入商品名称')
    return
  }
  formData.items.push({ ...itemForm })
  showItemModal.value = false
}

const handleSubmit = () => {
  formRef.value?.validate(async (errors: any) => {
    if (!errors) {
      if (formData.items.length === 0) {
        message.warning('请添加入库明细')
        return
      }
      submitLoading.value = true
      try {
        if (formData.id) {
          await updateStockIn(formData)
          message.success('更新成功')
        } else {
          await addStockIn(formData)
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
  formData.purchaseOrderId = null
  formData.warehouseId = null
  formData.stockInDate = Date.now()
  formData.status = 0
  formData.remark = ''
  formData.items = []
}

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.stockin-container {
  padding: 20px;
}
</style>
