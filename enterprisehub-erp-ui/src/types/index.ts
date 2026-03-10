// 通用响应类型
export interface ApiResponse<T = any> {
  code: number
  msg: string
  data: T
}

// 分页响应类型
export interface PageResult<T = any> {
  list: T[]
  total: number
  pageNum: number
  pageSize: number
}

// 分页查询参数
export interface PageQuery {
  pageNum: number
  pageSize: number
}

// 用户类型
export interface User {
  id: number
  username: string
  nickname: string
  realName?: string
  email?: string
  phone?: string
  avatar?: string
  status: number
  deptId?: number
  roleIds?: number[]
  createTime?: string
  updateTime?: string
}

// 角色类型
export interface Role {
  id: number
  roleName: string
  roleKey: string
  sort: number
  status: number
  remark?: string
  createTime?: string
  updateTime?: string
}

// 部门类型
export interface Dept {
  id: number
  parentId: number
  deptName: string
  leader?: string
  phone?: string
  email?: string
  sort: number
  status: number
  children?: Dept[]
  createTime?: string
  updateTime?: string
}

// 菜单类型
export interface Menu {
  id: number
  parentId: number
  menuName: string
  menuType: string
  path?: string
  component?: string
  icon?: string
  sort: number
  visible: number
  status: number
  perms?: string
  children?: Menu[]
  createTime?: string
  updateTime?: string
}

// 登录表单
export interface LoginForm {
  username: string
  password: string
}

// 登录响应
export interface LoginResponse {
  token: string
  userInfo: User
}
