<template>
  <div class="error-container">
    <n-result
      status="403"
      title="403 无权限"
      description="抱歉，您没有权限访问此页面"
    >
      <template #footer>
        <n-space>
          <n-button @click="goBack">返回上一页</n-button>
          <n-button type="primary" @click="goHome">返回首页</n-button>
        </n-space>
      </template>
    </n-result>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.back()
}

const goHome = () => {
  router.push('/')
}
</script>

<style scoped lang="scss">
.error-container {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
}
</style>
