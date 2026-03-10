import request from '@/utils/request'

// 供应商管理
export const supplierApi = {
  // 分页查询供应商列表
  getList: (params: any) => {
    return request.get('/purchase/v1/suppliers', { params })
  },
  
  // 根据ID查询供应商详情
  getById: (id: number) => {
    return request.get(`/purchase/v1/suppliers/${id}`)
  },
  
  // 新增供应商
  create: (data: any) => {
    return request.post('/purchase/v1/suppliers', data)
  },
  
  // 修改供应商
  update: (data: any) => {
    return request.put('/purchase/v1/suppliers', data)
  },
  
  // 删除供应商
  delete: (id: number) => {
    return request.delete(`/purchase/v1/suppliers/${id}`)
  }
}

// 采购订单管理
export const purchaseOrderApi = {
  // 分页查询采购订单列表
  getList: (params: any) => {
    return request.get('/purchase/v1/orders', { params })
  },
  
  // 根据ID查询采购订单详情
  getById: (id: number) => {
    return request.get(`/purchase/v1/orders/${id}`)
  },
  
  // 新增采购订单
  create: (data: any) => {
    return request.post('/purchase/v1/orders', data)
  },
  
  // 修改采购订单
  update: (data: any) => {
    return request.put('/purchase/v1/orders', data)
  },
  
  // 删除采购订单
  delete: (id: number) => {
    return request.delete(`/purchase/v1/orders/${id}`)
  },
  
  // 审核采购订单
  audit: (id: number) => {
    return request.put(`/purchase/v1/orders/${id}/audit`)
  }
}

// 入库管理
export const stockInApi = {
  // 分页查询入库单列表
  getList: (params: any) => {
    return request.get('/purchase/v1/stockin', { params })
  },
  
  // 根据ID查询入库单详情
  getById: (id: number) => {
    return request.get(`/purchase/v1/stockin/${id}`)
  },
  
  // 新增入库单
  create: (data: any) => {
    return request.post('/purchase/v1/stockin', data)
  },
  
  // 修改入库单
  update: (data: any) => {
    return request.put('/purchase/v1/stockin', data)
  },
  
  // 删除入库单
  delete: (id: number) => {
    return request.delete(`/purchase/v1/stockin/${id}`)
  },
  
  // 审核入库单
  audit: (id: number) => {
    return request.put(`/purchase/v1/stockin/${id}/audit`)
  }
}

// 导出便捷函数
export const getSupplierList = supplierApi.getList
export const addSupplier = supplierApi.create
export const updateSupplier = supplierApi.update
export const deleteSupplier = supplierApi.delete

export const getPurchaseOrderList = purchaseOrderApi.getList
export const addPurchaseOrder = purchaseOrderApi.create
export const updatePurchaseOrder = purchaseOrderApi.update
export const deletePurchaseOrder = purchaseOrderApi.delete
export const auditPurchaseOrder = purchaseOrderApi.audit

export const getStockInList = stockInApi.getList
export const addStockIn = stockInApi.create
export const updateStockIn = stockInApi.update
export const deleteStockIn = stockInApi.delete
export const auditStockIn = stockInApi.audit
