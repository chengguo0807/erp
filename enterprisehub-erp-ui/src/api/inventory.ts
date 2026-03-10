import request from '@/utils/request'

// 产品管理 API
export const getProductList = (params: any) => {
  return request.get('/inventory/v1/products', { params })
}

export const getProductInfo = (id: number) => {
  return request.get(`/inventory/v1/products/${id}`)
}

export const createProduct = (data: any) => {
  return request.post('/inventory/v1/products', data)
}

export const updateProduct = (data: any) => {
  return request.put('/inventory/v1/products', data)
}

export const deleteProduct = (id: number) => {
  return request.delete(`/inventory/v1/products/${id}`)
}

// 仓库管理 API
export const getWarehouseList = (params: any) => {
  return request.get('/inventory/v1/warehouses', { params })
}

export const getWarehouseInfo = (id: number) => {
  return request.get(`/inventory/v1/warehouses/${id}`)
}

export const createWarehouse = (data: any) => {
  return request.post('/inventory/v1/warehouses', data)
}

export const updateWarehouse = (data: any) => {
  return request.put('/inventory/v1/warehouses', data)
}

export const deleteWarehouse = (id: number) => {
  return request.delete(`/inventory/v1/warehouses/${id}`)
}

// 库存管理 API
export const getInventoryList = (params: any) => {
  return request.get('/inventory/v1/inventories', { params })
}

export const getInventoryInfo = (id: number) => {
  return request.get(`/inventory/v1/inventories/${id}`)
}

export const updateInventory = (data: any) => {
  return request.put('/inventory/v1/inventories', data)
}

export const getInventoryWarning = () => {
  return request.get('/inventory/v1/inventories/warning')
}

export const transferInventory = (data: any) => {
  return request.post('/inventory/v1/inventories/transfer', data)
}

// 产品分类管理 API
export const getCategoryList = (params: any) => {
  return request.get('/inventory/v1/categories', { params })
}

export const createCategory = (data: any) => {
  return request.post('/inventory/v1/categories', data)
}

export const updateCategory = (data: any) => {
  return request.put('/inventory/v1/categories', data)
}

export const deleteCategory = (id: number) => {
  return request.delete(`/inventory/v1/categories/${id}`)
}

// 出库管理 API
export const getStockOutList = (params: any) => {
  return request.get('/inventory/v1/stock-outs', { params })
}

export const getStockOutInfo = (id: number) => {
  return request.get(`/inventory/v1/stock-outs/${id}`)
}

export const createStockOut = (data: any) => {
  return request.post('/inventory/v1/stock-outs', data)
}

export const auditStockOut = (id: number) => {
  return request.put(`/inventory/v1/stock-outs/${id}/audit`)
}

export const deleteStockOut = (id: number) => {
  return request.delete(`/inventory/v1/stock-outs/${id}`)
}

// 库存盘点 API
export const getStockCheckList = (params: any) => {
  return request.get('/inventory/v1/stock-checks', { params })
}

export const getStockCheckInfo = (id: number) => {
  return request.get(`/inventory/v1/stock-checks/${id}`)
}

export const createStockCheck = (data: any) => {
  return request.post('/inventory/v1/stock-checks', data)
}

export const completeStockCheck = (id: number) => {
  return request.put(`/inventory/v1/stock-checks/${id}/complete`)
}

export const deleteStockCheck = (id: number) => {
  return request.delete(`/inventory/v1/stock-checks/${id}`)
}

// 库存流水 API
export const getTransactionList = (params: any) => {
  return request.get('/inventory/v1/transactions', { params })
}
