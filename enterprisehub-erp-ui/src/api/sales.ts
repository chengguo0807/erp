import request from '@/utils/request'

// 销售订单 API
export const getSalesOrderList = (params: any) => {
  return request.get('/sales/v1/orders', { params })
}

export const getSalesOrderInfo = (id: number) => {
  return request.get(`/sales/v1/orders/${id}`)
}

export const createSalesOrder = (data: any) => {
  return request.post('/sales/v1/orders', data)
}

export const updateSalesOrder = (data: any) => {
  return request.put('/sales/v1/orders', data)
}

export const deleteSalesOrder = (id: number) => {
  return request.delete(`/sales/v1/orders/${id}`)
}

export const auditSalesOrder = (id: number) => {
  return request.put(`/sales/v1/orders/${id}/audit`)
}

export const completeSalesOrder = (id: number) => {
  return request.put(`/sales/v1/orders/${id}/complete`)
}

export const closeSalesOrder = (id: number) => {
  return request.put(`/sales/v1/orders/${id}/close`)
}

// 客户管理 API
export const getCustomerList = (params: any) => {
  return request.get('/sales/v1/customers', { params })
}

export const getCustomerInfo = (id: number) => {
  return request.get(`/sales/v1/customers/${id}`)
}

export const createCustomer = (data: any) => {
  return request.post('/sales/v1/customers', data)
}

export const updateCustomer = (data: any) => {
  return request.put('/sales/v1/customers', data)
}

export const deleteCustomer = (id: number) => {
  return request.delete(`/sales/v1/customers/${id}`)
}
