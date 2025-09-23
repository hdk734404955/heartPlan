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

  // 添加认证令牌
  if (authStore.accessToken) {
    config.header = {
      ...config.header,
      'Authorization': `Bearer ${authStore.accessToken}`
    }
  }

  // 添加基础URL
  if (!config.url.startsWith('http')) {
    config.url = `${API_CONFIG.BASE_URL}${config.url}`
  }

  // 设置超时时间
  config.timeout = config.timeout || API_CONFIG.TIMEOUT

  // 添加默认请求头
  config.header = {
    ...API_CONFIG.HEADERS,
    ...config.header
  }

  console.log('Request:', config)
  return config
}

// 响应拦截器
export const responseInterceptor = (response) => {
  console.log('Response:', response)

  const authStore = useAuthStore()

  // 处理HTTP状态码
  if (response.statusCode === 200) {
    return response.data
  } else if (response.statusCode === 401) {
    // 未授权，清除令牌并跳转到登录页
    authStore.logout()
    uni.showToast({
      title: 'Please login again',
      icon: 'none',
      duration: 2000
    })
    return Promise.reject(new Error('Unauthorized'))
  } else if (response.statusCode === 403) {
    // 禁止访问 - 可能是CORS问题
    const errorMessage = response.data === 'Invalid CORS request' 
      ? 'CORS configuration error. Please check server settings.' 
      : 'Access denied';
    
    uni.showToast({
      title: errorMessage,
      icon: 'none',
      duration: 3000
    })
    
    console.error('403 Error Details:', {
      data: response.data,
      headers: response.header,
      statusCode: response.statusCode
    })
    
    return Promise.reject(new Error('Forbidden: ' + response.data))
  } else if (response.statusCode >= 500) {
    // 服务器错误
    uni.showToast({
      title: 'Server error, please try again',
      icon: 'none',
      duration: 2000
    })
    return Promise.reject(new Error('Server Error'))
  } else {
    // 其他错误
    const errorMessage = response.data?.message || 'Request failed'
    uni.showToast({
      title: errorMessage,
      icon: 'none',
      duration: 2000
    })
    return Promise.reject(new Error(errorMessage))
  }
}

// 错误处理器
export const errorHandler = (error) => {
  console.error('API Error:', error)

  if (error.errMsg) {
    // 网络错误
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