// 用户认证状态管理
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  // 状态
  const accessToken = ref('')
  const refreshToken = ref('')
  const isLoggedIn = ref(false)
  const loginLoading = ref(false)
  const registerLoading = ref(false)

  // 计算属性
  const isAuthenticated = computed(() => {
    return isLoggedIn.value && accessToken.value
  })

  // 操作方法
  const setTokens = (tokens) => {
    accessToken.value = tokens.accessToken
    refreshToken.value = tokens.refreshToken
    isLoggedIn.value = true

    // 存储到本地
    uni.setStorageSync('accessToken', tokens.accessToken)
    uni.setStorageSync('refreshToken', tokens.refreshToken)
    uni.setStorageSync('isLoggedIn', true)
  }

  const clearTokens = () => {
    accessToken.value = ''
    refreshToken.value = ''
    isLoggedIn.value = false

    // 清除本地存储
    uni.removeStorageSync('accessToken')
    uni.removeStorageSync('refreshToken')
    uni.removeStorageSync('isLoggedIn')
  }

  const initAuth = () => {
    // 从本地存储恢复认证状态
    const storedAccessToken = uni.getStorageSync('accessToken')
    const storedRefreshToken = uni.getStorageSync('refreshToken')
    const storedIsLoggedIn = uni.getStorageSync('isLoggedIn')

    if (storedAccessToken && storedRefreshToken && storedIsLoggedIn) {
      accessToken.value = storedAccessToken
      refreshToken.value = storedRefreshToken
      isLoggedIn.value = storedIsLoggedIn
    }
  }

  const setLoginLoading = (loading) => {
    loginLoading.value = loading
  }

  const setRegisterLoading = (loading) => {
    registerLoading.value = loading
  }

  const refreshAccessToken = async () => {
    try {
      if (!refreshToken.value) {
        throw new Error('No refresh token available')
      }

      // 调用刷新令牌的API - 响应拦截器已处理统一格式，直接获取data
      const { authAPI } = await import('@/api/auth')
      const responseData = await authAPI.refreshToken(refreshToken.value)
      setTokens(responseData)

      // Token刷新成功后，直接更新用户信息
      const { useUserStore } = await import('./user')
      const userStore = useUserStore()
      userStore.updateUserProfile(responseData.user)

      return true
    } catch (error) {
      console.error('Token refresh failed:', error)
      logout()
      return false
    }
  }

  const logout = () => {
    clearTokens()
    // 跳转到登录页面
    uni.reLaunch({
      url: '/pages/auth/login'
    })
  }

  return {
    // 状态
    accessToken,
    refreshToken,
    isLoggedIn,
    loginLoading,
    registerLoading,

    // 计算属性
    isAuthenticated,

    // 方法
    setTokens,
    clearTokens,
    initAuth,
    setLoginLoading,
    setRegisterLoading,
    refreshAccessToken,
    logout
  }
})