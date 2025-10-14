// API配置文件
import { useAuthStore } from '@/store/modules/auth'

/**
 * 获取API基础URL
 * 根据运行环境自动选择合适的URL
 */
function getBaseURL() {
  // 检查是否在uni-app环境中
  if (typeof uni !== 'undefined') {
    // 获取系统信息
    const systemInfo = uni.getSystemInfoSync()
    
    // 如果是Android平台（包括模拟器）
    if (systemInfo.platform === 'android') {
      // Android模拟器使用10.0.2.2访问宿主机
      return 'http://10.0.2.2:8080/api'
    }
    
    // 如果是iOS平台
    if (systemInfo.platform === 'ios') {
      // iOS模拟器可以直接使用localhost
      return 'http://localhost:8080/api'
    }
  }
  
  // Web环境或其他情况
  if (process.env.NODE_ENV === 'development') {
    // 开发环境使用代理
    return '/api'
  } else {
    // 生产环境使用完整URL
    return 'http://localhost:8080/api'
  }
}

// 获取并打印BASE_URL用于调试
const baseURL = getBaseURL()
console.log('API_CONFIG - 当前BASE_URL:', baseURL)

// API基础配置
export const API_CONFIG = {
  // 后端API基础URL - 根据运行环境自动选择
  BASE_URL: baseURL,
  
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

/**
 * 响应拦截器 - 统一处理ApiResponse格式 {code, data, message}
 * 
 * 优化说明：
 * - 统一响应格式处理逻辑，基于code字段判断成功/失败
 * - 简化错误处理流程，减少重复代码
 * - 提供向后兼容支持，同时支持新旧响应格式
 * - 修复：即使HTTP状态码不是200，也要检查响应体中的ApiResponse格式
 * 
 * @param {Object} response - uni.request的响应对象
 * @returns {*} 处理后的响应数据或抛出错误
 */
export const responseInterceptor = (response) => {
  const authStore = useAuthStore()
  const responseData = response.data

  // 首先检查是否为统一ApiResponse格式：{code, data, message}
  if (responseData && typeof responseData === 'object' && 'code' in responseData) {
    // 统一ApiResponse格式处理 - 无论HTTP状态码是什么，都基于code字段判断
    if (responseData.code >= 200 && responseData.code < 300) {
      // 业务成功：直接返回data字段给业务代码使用
      return responseData.data
    } else {
      // 业务错误：根据code字段统一处理不同类型的业务错误
      return handleBusinessError(responseData, authStore)
    }
  } else {
    // 非ApiResponse格式，按HTTP状态码处理
    if (response.statusCode === 200) {
      // 向后兼容：支持旧格式响应，直接返回原始数据
      return responseData
    } else {
      // HTTP错误状态码：统一处理网络层面的错误
      return handleHttpError(response, authStore)
    }
  }
}

/**
 * 处理业务错误（基于ApiResponse的code字段）
 * 
 * 优化说明：
 * - 统一业务错误处理逻辑，基于code字段分类处理
 * - 简化错误提示显示，使用统一的用户体验
 * - 减少重复的错误处理代码
 * - 修复：400错误（如登录失败）不应该跳转登录页面，只显示错误提示
 * 
 * @param {Object} responseData - ApiResponse格式的响应数据
 * @param {Object} authStore - 认证状态管理器
 * @returns {Promise} 拒绝的Promise，包含错误信息
 */
const handleBusinessError = async (responseData, authStore) => {
  const { code, message } = responseData
  
  // 根据code字段统一处理不同类型的业务错误
  if (code === 401) {
    // 401认证失败：JWT token无效/过期，尝试刷新令牌
    try {
      const refreshSuccess = await authStore.refreshAccessToken()
      if (refreshSuccess) {
        // 刷新成功，但这里无法重试原始请求，让调用方处理
        return Promise.reject(new Error('TOKEN_REFRESHED'))
      } else {
        // 刷新失败，清除认证信息并跳转登录
        handleUnauthorizedError(authStore)
        return Promise.reject(new Error(message || 'Session expired'))
      }
    } catch (error) {
      // 刷新令牌失败，清除认证信息并跳转登录
      handleUnauthorizedError(authStore)
      return Promise.reject(new Error(message || 'Session expired'))
    }
  } else if (code === 400) {
    // 400客户端错误：登录凭据错误、参数错误等，只显示错误提示，不跳转
    uni.showToast({
      title: message || 'Invalid request',
      icon: 'none',
      duration: 3000
    })
    return Promise.reject(new Error(message || 'Bad Request'))
  } else if (code === 403) {
    // 403权限不足：显示权限错误提示
    uni.showModal({
      title: 'Access Denied',
      content: message || 'You do not have permission to access this resource.',
      showCancel: false,
      confirmText: 'OK'
    })
    return Promise.reject(new Error(message || 'Access Denied'))
  } else if (code === 409) {
    // 409冲突错误：如邮箱已存在等
    uni.showToast({
      title: message || 'Data conflict occurred',
      icon: 'none',
      duration: 3000
    })
    return Promise.reject(new Error(message || 'Conflict'))
  } else if (code >= 400 && code < 500) {
    // 其他4xx客户端错误：参数错误、资源不存在等
    uni.showToast({
      title: message || 'Request failed',
      icon: 'none',
      duration: 3000
    })
    return Promise.reject(new Error(message || 'Client Error'))
  } else if (code >= 500) {
    // 5xx服务器错误：显示重试选项
    uni.showModal({
      title: 'Server Error',
      content: message || 'The server encountered a problem. Please try again later.',
      showCancel: true,
      cancelText: 'Cancel',
      confirmText: 'Retry'
    })
    return Promise.reject(new Error(message || 'Server Error'))
  } else {
    // 其他未知错误码：显示通用错误提示
    uni.showToast({
      title: message || 'Unknown error',
      icon: 'none',
      duration: 3000
    })
    return Promise.reject(new Error(message || 'Unknown Error'))
  }
}

/**
 * 统一处理HTTP错误状态码
 * 
 * 优化说明：
 * - 简化HTTP错误处理逻辑，减少重复代码
 * - 统一错误提示显示方式，提供一致的用户体验
 * - 清晰区分不同类型的HTTP错误
 * 
 * @param {Object} response - HTTP响应对象
 * @param {Object} authStore - 认证状态管理器
 * @returns {Promise} 拒绝的Promise，包含错误信息
 */
const handleHttpError = (response, authStore) => {
  console.log(response)
  
  const { statusCode, data } = response
  
  if (statusCode === 401) {
    // 401未授权：自动清除认证信息并跳转登录页面
    handleUnauthorizedError(authStore)
    return Promise.reject(new Error('Unauthorized'))
  } else if (statusCode === 403) {
    // 403禁止访问：区分CORS错误和权限错误
    return handleForbiddenError(data)
  } else if (statusCode >= 500) {
    // 5xx服务器错误：显示简洁的错误提示
    return handleServerError(statusCode, data)
  } else {
    // 4xx客户端错误：显示简洁的错误提示
    return handleClientError(statusCode, data)
  }
}

/**
 * 简化401错误处理 - 自动清除认证信息并跳转登录页面
 * 
 * 优化说明：
 * - 统一认证失败处理逻辑，自动清理状态
 * - 简化用户提示，提供清晰的操作指引
 * 
 * @param {Object} authStore - 认证状态管理器
 */
const handleUnauthorizedError = (authStore) => {
  // 清除认证信息，触发自动跳转登录页面
  authStore.logout()
  
  // 显示简洁的会话过期提示
  uni.showToast({
    title: 'Session expired, please login again',
    icon: 'none',
    duration: 2000
  })
}

/**
 * 简化403错误处理 - 清晰区分CORS错误和权限错误
 * 
 * 优化说明：
 * - 智能识别CORS错误和权限错误，提供针对性提示
 * - 简化错误分类逻辑，减少判断复杂度
 * 
 * @param {*} data - 响应数据
 * @returns {Promise} 拒绝的Promise，包含错误信息
 */
const handleForbiddenError = (data) => {
  const responseText = typeof data === 'string' ? data : JSON.stringify(data)
  const isCorsError = responseText.includes('CORS') || responseText.includes('Invalid CORS request')
  
  if (isCorsError) {
    // CORS错误：显示网络配置错误提示
    uni.showToast({
      title: 'Network configuration error',
      icon: 'none',
      duration: 3000
    })
    return Promise.reject(new Error('CORS Error'))
  } else {
    // 权限错误：显示访问被拒绝提示
    uni.showToast({
      title: 'Access denied',
      icon: 'none',
      duration: 3000
    })
    return Promise.reject(new Error('Access Denied'))
  }
}

/**
 * 简化服务器错误处理 - 显示简洁的错误提示
 * 
 * 优化说明：
 * - 统一服务器错误提示，提供一致的用户体验
 * - 简化错误信息构建，减少重复代码
 * 
 * @param {number} statusCode - HTTP状态码
 * @param {*} data - 响应数据
 * @returns {Promise} 拒绝的Promise，包含错误信息
 */
const handleServerError = (statusCode, data) => {
  // 显示统一的服务器错误提示
  uni.showToast({
    title: 'Server error, please try again later',
    icon: 'none',
    duration: 3000
  })
  
  const errorMessage = data?.message || 'Internal server error'
  return Promise.reject(new Error(`Server Error (${statusCode}): ${errorMessage}`))
}

/**
 * 简化客户端错误处理
 * 
 * 优化说明：
 * - 统一客户端错误提示显示方式
 * - 简化错误信息提取和显示逻辑
 * 
 * @param {number} statusCode - HTTP状态码
 * @param {*} data - 响应数据
 * @returns {Promise} 拒绝的Promise，包含错误信息
 */
const handleClientError = (statusCode, data) => {
  const errorMessage = data?.message || 'Request failed'
  
  // 显示客户端错误提示
  uni.showToast({
    title: errorMessage,
    icon: 'none',
    duration: 3000
  })
  
  return Promise.reject(new Error(`Client Error (${statusCode}): ${errorMessage}`))
}



/**
 * 简化网络错误处理器 - 实现简单直观的网络错误处理机制
 * 
 * 优化说明：
 * - 统一网络错误分类和提示显示
 * - 简化错误类型判断逻辑，提供清晰的用户反馈
 * - 减少重复的错误处理代码
 * 
 * @param {Object} error - 网络错误对象
 * @returns {Promise} 拒绝的Promise，包含原始错误
 */
export const errorHandler = (error) => {
  if (error.errMsg) {
    // 根据错误消息类型提供不同的用户提示
    if (error.errMsg.includes('timeout')) {
      // 请求超时错误
      uni.showToast({
        title: 'Request timeout',
        icon: 'none',
        duration: 2000
      })
    } else if (error.errMsg.includes('fail') || error.errMsg.includes('network')) {
      // 网络连接失败错误
      uni.showToast({
        title: 'Network connection failed',
        icon: 'none',
        duration: 2000
      })
    } else {
      // 其他网络相关错误
      uni.showToast({
        title: 'Network error',
        icon: 'none',
        duration: 2000
      })
    }
  } else {
    // 未知类型错误
    uni.showToast({
      title: 'Unknown error occurred',
      icon: 'none',
      duration: 2000
    })
  }

  return Promise.reject(error)
}

