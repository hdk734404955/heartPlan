// API配置文件
import { useAuthStore } from '@/store/modules/auth'

// API基础配置
export const API_CONFIG = {
  // 后端API基础URL - 开发环境使用代理，生产环境使用完整URL
  BASE_URL: process.env.NODE_ENV === 'development' ? '/api' : 'http://localhost:8080/api',
  
  // 请求超时时间（毫秒）
  TIMEOUT: 15000,
  
  // 请求头配置
  HEADERS: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    'X-Requested-With': 'XMLHttpRequest'
  }
}

// 请求拦截器
export const requestInterceptor = (config) => {
  const authStore = useAuthStore()

  // 初始化请求头
  config.header = config.header || {}

  // 添加认证令牌
  if (authStore.accessToken) {
    config.header['Authorization'] = `Bearer ${authStore.accessToken}`
  }

  // 添加基础URL
  if (!config.url.startsWith('http')) {
    config.url = `${API_CONFIG.BASE_URL}${config.url}`
  }

  // 设置超时时间
  config.timeout = config.timeout || API_CONFIG.TIMEOUT

  // 根据请求类型设置Content-Type
  if (config.method && config.method.toUpperCase() !== 'GET') {
    // 如果是文件上传，不设置Content-Type，让浏览器自动设置
    if (!config.isUpload && !config.header['Content-Type']) {
      config.header['Content-Type'] = 'application/json'
    }
  }

  // 添加其他默认请求头
  config.header = {
    'Accept': 'application/json',
    'X-Requested-With': 'XMLHttpRequest',
    ...config.header
  }

  return config
}

// 响应拦截器
export const responseInterceptor = (response) => {
  const authStore = useAuthStore()

  // 处理HTTP状态码
  if (response.statusCode === 200) {
    return response.data
  } else if (response.statusCode === 401) {
    // 未授权处理
    handleUnauthorizedError(authStore)
    return Promise.reject(new Error('Unauthorized'))
  } else if (response.statusCode === 403) {
    // 禁止访问处理 - 区分CORS错误和权限错误
    return handleForbiddenError(response)
  } else if (response.statusCode >= 500) {
    // 服务器错误处理
    return handleServerError(response)
  } else {
    // 其他客户端错误处理
    return handleClientError(response)
  }
}

// 处理401未授权错误
const handleUnauthorizedError = (authStore) => {
  // 清除认证信息
  authStore.logout()
  
  // 显示友好提示
  uni.showModal({
    title: 'Session Expired',
    content: 'Your login session has expired. Please log in again.',
    showCancel: false,
    confirmText: 'Login',
    success: () => {
      // 跳转到登录页面
      uni.reLaunch({
        url: '/pages/auth/login'
      })
    }
  })
}

// 处理403禁止访问错误
const handleForbiddenError = (response) => {
  const responseData = response.data
  const isCorsError = typeof responseData === 'string' && 
    (responseData.includes('CORS') || responseData.includes('Invalid CORS request'))
  
  if (isCorsError) {
    // CORS错误特殊处理
    uni.showModal({
      title: 'CORS Configuration Error',
      content: 'Cross-origin request blocked. Please check if the backend server is running and CORS is properly configured.',
      showCancel: false,
      confirmText: 'OK'
    })
    
    return Promise.reject(new Error(`CORS Error: ${responseData}`))
  } else {
    // 权限错误
    uni.showModal({
      title: 'Access Denied',
      content: 'You do not have permission to access this resource. Please contact the administrator.',
      showCancel: false,
      confirmText: 'OK'
    })
    
    return Promise.reject(new Error(`Access Denied: ${responseData?.message || responseData}`))
  }
}

// 处理服务器错误
const handleServerError = (response) => {
  const errorMessage = response.data?.message || 'Internal server error'
  
  uni.showModal({
    title: 'Server Error',
    content: `The server encountered a problem. Please try again later.\nError code: ${response.statusCode}`,
    showCancel: true,
    cancelText: 'Cancel',
    confirmText: 'Retry'
  })
  
  return Promise.reject(new Error(`Server Error (${response.statusCode}): ${errorMessage}`))
}

// 处理客户端错误
const handleClientError = (response) => {
  const errorMessage = response.data?.message || `Request failed (${response.statusCode})`
  
  uni.showToast({
    title: errorMessage,
    icon: 'none',
    duration: 3000
  })
  
  return Promise.reject(new Error(errorMessage))
}



// 错误处理器
export const errorHandler = (error) => {
  if (error.errMsg) {
    // 网络错误处理
    if (error.errMsg.includes('timeout')) {
      uni.showToast({
        title: 'Request timeout',
        icon: 'none',
        duration: 2000
      })
    } else if (error.errMsg.includes('fail')) {
      uni.showToast({
        title: 'Network error',
        icon: 'none',
        duration: 2000
      })
    }
  }

  return Promise.reject(error)
}

