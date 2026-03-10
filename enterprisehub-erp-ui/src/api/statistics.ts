import request from '@/utils/request'

// 销售统计 API
export const getSalesStatistics = (params: any) => {
  return request.get('/statistics/v1/sales', { params })
}

export const exportSalesStatistics = (params: any) => {
  return request.get('/statistics/v1/sales/export', { params, responseType: 'blob' })
}

// 采购统计 API
export const getPurchaseStatistics = (params: any) => {
  return request.get('/statistics/v1/purchase', { params })
}

export const exportPurchaseStatistics = (params: any) => {
  return request.get('/statistics/v1/purchase/export', { params, responseType: 'blob' })
}

// 财务统计 API
export const getPaymentStatistics = (params: any) => {
  return request.get('/statistics/v1/payment', { params })
}

export const exportPaymentStatistics = (params: any) => {
  return request.get('/statistics/v1/payment/export', { params, responseType: 'blob' })
}
