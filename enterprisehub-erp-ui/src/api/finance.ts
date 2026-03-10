import request from '@/utils/request'

// 收付款管理 API
export const getPaymentList = (params: any) => {
  return request.get('/finance/v1/payments', { params })
}

export const getPaymentInfo = (id: number) => {
  return request.get(`/finance/v1/payments/${id}`)
}

export const createPayment = (data: any) => {
  return request.post('/finance/v1/payments', data)
}

export const updatePayment = (data: any) => {
  return request.put('/finance/v1/payments', data)
}

export const deletePayment = (id: number) => {
  return request.delete(`/finance/v1/payments/${id}`)
}

// 费用管理 API
export const getExpenseList = (params: any) => {
  return request.get('/finance/v1/expenses', { params })
}

export const getExpenseInfo = (id: number) => {
  return request.get(`/finance/v1/expenses/${id}`)
}

export const createExpense = (data: any) => {
  return request.post('/finance/v1/expenses', data)
}

export const updateExpense = (data: any) => {
  return request.put('/finance/v1/expenses', data)
}

export const deleteExpense = (id: number) => {
  return request.delete(`/finance/v1/expenses/${id}`)
}
