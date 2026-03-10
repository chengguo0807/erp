import { defineStore } from 'pinia'
import request from '@/utils/request'

interface UserInfo {
  userId: number
  username: string
  nickname: string
  roles: string[]
  permissions: string[]
}

interface UserState {
  token: string
  userInfo: UserInfo | null
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: localStorage.getItem('token') || '',
    userInfo: null,
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    username: (state) => state.userInfo?.nickname || state.userInfo?.username || '',
    roles: (state) => {
      if (!state.userInfo?.roles) return []
      // 将Set转换为数组（如果后端返回的是Set）
      return Array.isArray(state.userInfo.roles) 
        ? state.userInfo.roles 
        : Array.from(state.userInfo.roles as any)
    },
  },

  actions: {
    async login(username: string, password: string) {
      const res: any = await request.post('/auth/login', { username, password })
      this.token = res.data.token
      this.userInfo = res.data.userInfo
      localStorage.setItem('token', this.token)
      
      console.log('登录成功，用户信息:', this.userInfo)
      console.log('用户角色:', this.userInfo?.roles)
    },

    async getUserInfo() {
      const res: any = await request.get('/auth/info')
      this.userInfo = res.data
      
      console.log('获取用户信息成功:', this.userInfo)
      console.log('用户角色:', this.userInfo?.roles)
    },

    logout() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
    },

    async logoutAction() {
      try {
        await request.post('/auth/logout')
      } finally {
        this.logout()
      }
    },
  },
})
