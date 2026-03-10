import request from '@/utils/request'

// 用户管理 API
export const getUserList = (params: any) => {
  return request.get('/system/v1/users', { params })
}

export const getUserInfo = (id: number) => {
  return request.get(`/system/v1/users/${id}`)
}

export const createUser = (data: any) => {
  return request.post('/system/v1/users', data)
}

export const updateUser = (id: number, data: any) => {
  return request.put(`/system/v1/users/${id}`, data)
}

export const deleteUser = (id: number) => {
  return request.delete(`/system/v1/users/${id}`)
}

// 角色管理 API
export const getRoleList = (params: any) => {
  return request.get('/system/v1/roles', { params })
}

export const createRole = (data: any) => {
  return request.post('/system/v1/roles', data)
}

export const updateRole = (id: number, data: any) => {
  return request.put(`/system/v1/roles/${id}`, data)
}

export const deleteRole = (id: number) => {
  return request.delete(`/system/v1/roles/${id}`)
}

// 部门管理 API
export const getDeptList = (params?: any) => {
  return request.get('/system/v1/depts', { params })
}

export const getDeptTree = () => {
  return request.get('/system/v1/depts/tree')
}

export const createDept = (data: any) => {
  return request.post('/system/v1/depts', data)
}

export const updateDept = (id: number, data: any) => {
  return request.put(`/system/v1/depts/${id}`, data)
}

export const deleteDept = (id: number) => {
  return request.delete(`/system/v1/depts/${id}`)
}

// 菜单管理 API
export const getMenuList = (params?: any) => {
  return request.get('/system/v1/menus', { params })
}

export const getMenuTree = () => {
  return request.get('/system/v1/menus/tree')
}

export const createMenu = (data: any) => {
  return request.post('/system/v1/menus', data)
}

export const updateMenu = (id: number, data: any) => {
  return request.put(`/system/v1/menus/${id}`, data)
}

export const deleteMenu = (id: number) => {
  return request.delete(`/system/v1/menus/${id}`)
}

// 操作日志 API
export const getOperationLogList = (params: any) => {
  return request.get('/system/v1/operation-logs', { params })
}

export const getOperationLogInfo = (id: number) => {
  return request.get(`/system/v1/operation-logs/${id}`)
}

export const deleteOperationLog = (id: number) => {
  return request.delete(`/system/v1/operation-logs/${id}`)
}

export const clearOperationLogs = () => {
  return request.delete('/system/v1/operation-logs/clear')
}
