<template>
  <div class="login-container">
    <n-card class="login-card" title="EnterpriseHub ERP" :bordered="false">
      <template #header-extra>
        <span class="subtitle">企业资源计划管理系统</span>
      </template>
      
      <n-form
        ref="formRef"
        :model="loginForm"
        :rules="rules"
        size="large"
      >
        <n-form-item path="username">
          <n-input
            v-model:value="loginForm.username"
            placeholder="用户名"
            @keydown.enter="handleLogin"
          >
            <template #prefix>
              <n-icon :component="PersonOutline" />
            </template>
          </n-input>
        </n-form-item>
        
        <n-form-item path="password">
          <n-input
            v-model:value="loginForm.password"
            type="password"
            show-password-on="click"
            placeholder="密码"
            @keydown.enter="handleLogin"
          >
            <template #prefix>
              <n-icon :component="LockClosedOutline" />
            </template>
          </n-input>
        </n-form-item>
        
        <n-form-item>
          <n-button
            type="primary"
            :loading="loading"
            block
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </n-button>
        </n-form-item>
      </n-form>
      
      <n-text depth="3" style="font-size: 12px; text-align: center; display: block;">
        默认账号：admin / admin123
      </n-text>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useMessage } from 'naive-ui'
import { PersonOutline, LockClosedOutline } from '@vicons/ionicons5'
import type { FormInst, FormRules } from 'naive-ui'
import { useUserStore } from '@/store/user'

const router = useRouter()
const route = useRoute()
const message = useMessage()
const userStore = useUserStore()

const formRef = ref<FormInst | null>(null)
const loading = ref(false)

const loginForm = reactive({
  username: 'admin',
  password: 'admin123',
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    await userStore.login(loginForm.username, loginForm.password)
    message.success('登录成功')
    
    const redirect = (route.query.redirect as string) || '/'
    router.push(redirect)
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 420px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  
  .subtitle {
    font-size: 14px;
    color: #999;
  }
}
</style>
