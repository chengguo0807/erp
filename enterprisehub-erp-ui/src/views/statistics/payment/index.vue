<template>
  <n-card title="财务统计" :bordered="false">
    <n-space vertical :size="16">
      <!-- 搜索栏 -->
      <n-form inline :model="queryParams">
        <n-form-item label="统计周期">
          <n-select
            v-model:value="queryParams.period"
            placeholder="请选择周期"
            :options="periodOptions"
            style="width: 120px"
          />
        </n-form-item>
        <n-form-item label="开始日期">
          <n-date-picker v-model:value="queryParams.startDate" type="date" clearable />
        </n-form-item>
        <n-form-item label="结束日期">
          <n-date-picker v-model:value="queryParams.endDate" type="date" clearable />
        </n-form-item>
        <n-form-item>
          <n-space>
            <n-button type="primary" @click="handleQuery">
              <template #icon>
                <n-icon :component="SearchOutline" />
              </template>
              查询
            </n-button>
            <n-button @click="handleExport">
              <template #icon>
                <n-icon :component="DownloadOutline" />
              </template>
              导出
            </n-button>
          </n-space>
        </n-form-item>
      </n-form>

      <!-- 统计卡片 -->
      <n-grid cols="4" x-gap="16">
        <n-gi>
          <n-statistic label="总收入" :value="statistics.totalIncome">
            <template #prefix>¥</template>
          </n-statistic>
        </n-gi>
        <n-gi>
          <n-statistic label="总支出" :value="statistics.totalExpense">
            <template #prefix>¥</template>
          </n-statistic>
        </n-gi>
        <n-gi>
          <n-statistic label="净利润" :value="statistics.netProfit">
            <template #prefix>¥</template>
          </n-statistic>
        </n-gi>
        <n-gi>
          <n-statistic label="利润率" :value="statistics.profitRate">
            <template #suffix>%</template>
          </n-statistic>
        </n-gi>
      </n-grid>

      <!-- 图表 -->
      <n-grid cols="2" x-gap="16">
        <n-gi>
          <n-card title="收支趋势" :bordered="false">
            <div style="height: 300px; display: flex; align-items: center; justify-content: center; color: #999;">
              图表区域 - 待集成ECharts
            </div>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="收支占比" :bordered="false">
            <div style="height: 300px; display: flex; align-items: center; justify-content: center; color: #999;">
              图表区域 - 待集成ECharts
            </div>
          </n-card>
        </n-gi>
      </n-grid>

      <!-- 明细表格 -->
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
import { ref, reactive } from 'vue'
import { useMessage } from 'naive-ui'
import { SearchOutline, DownloadOutline } from '@vicons/ionicons5'
import request from '@/utils/request'
import type { DataTableColumns } from 'naive-ui'

const message = useMessage()

const loading = ref(false)
const tableData = ref([])

const queryParams = reactive({
  period: 'month',
  startDate: null,
  endDate: null,
  pageNum: 1,
  pageSize: 10,
})

const periodOptions = [
  { label: '日', value: 'day' },
  { label: '周', value: 'week' },
  { label: '月', value: 'month' },
  { label: '年', value: 'year' }
]

const statistics = reactive({
  totalIncome: 0,
  totalExpense: 0,
  netProfit: 0,
  profitRate: 0
})

const columns: DataTableColumns = [
  { title: '日期', key: 'date', width: 150 },
  { title: '收入', key: 'income', width: 150 },
  { title: '支出', key: 'expense', width: 150 },
  { title: '净利润', key: 'profit', width: 150 },
  { title: '利润率', key: 'profitRate', width: 120 }
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
    const res: any = await request.get('/statistics/v1/payment', { params: queryParams })
    tableData.value = res.data.list
    pagination.itemCount = res.data.total
    
    // 更新统计数据
    Object.assign(statistics, res.data.summary || {})
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}

const handleExport = () => {
  message.info('导出功能 - 待实现')
}

// 初始化
handleQuery()
</script>
