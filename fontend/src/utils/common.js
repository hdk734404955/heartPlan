// 通用工具函数
import { storage } from './storage'

// 防抖函数
export function debounce(func, wait, immediate = false) {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      timeout = null
      if (!immediate) func(...args)
    }
    const callNow = immediate && !timeout
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
    if (callNow) func(...args)
  }
}

// 节流函数
export function throttle(func, limit) {
  let inThrottle
  return function(...args) {
    if (!inThrottle) {
      func.apply(this, args)
      inThrottle = true
      setTimeout(() => inThrottle = false, limit)
    }
  }
}

// 深拷贝
export function deepClone(obj) {
  if (obj === null || typeof obj !== 'object') return obj
  if (obj instanceof Date) return new Date(obj.getTime())
  if (obj instanceof Array) return obj.map(item => deepClone(item))
  if (typeof obj === 'object') {
    const clonedObj = {}
    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        clonedObj[key] = deepClone(obj[key])
      }
    }
    return clonedObj
  }
}

// 格式化日期
export function formatDate(date, format = 'YYYY-MM-DD HH:mm:ss') {
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')

  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

// 相对时间格式化
export function formatRelativeTime(date) {
  const now = new Date()
  const targetDate = new Date(date)
  const diffMs = now - targetDate
  const diffSeconds = Math.floor(diffMs / 1000)
  const diffMinutes = Math.floor(diffSeconds / 60)
  const diffHours = Math.floor(diffMinutes / 60)
  const diffDays = Math.floor(diffHours / 24)

  if (diffSeconds < 60) {
    return 'Just now'
  } else if (diffMinutes < 60) {
    return `${diffMinutes} minutes ago`
  } else if (diffHours < 24) {
    return `${diffHours} hours ago`
  } else if (diffDays < 7) {
    return `${diffDays} days ago`
  } else {
    return formatDate(date, 'MM-DD')
  }
}

// 验证邮箱格式
export function validateEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

// 验证密码强度
export function validatePassword(password) {
  // 至少8位，包含字母和数字
  const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{8,}$/
  return passwordRegex.test(password)
}

// 生成随机字符串
export function generateRandomString(length = 8) {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  let result = ''
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result
}

// 生成UUID
export function generateUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    const r = Math.random() * 16 | 0
    const v = c === 'x' ? r : (r & 0x3 | 0x8)
    return v.toString(16)
  })
}

// 格式化文件大小
export function formatFileSize(bytes) {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 获取文件扩展名
export function getFileExtension(filename) {
  return filename.slice((filename.lastIndexOf('.') - 1 >>> 0) + 2)
}

// 检查是否为图片文件
export function isImageFile(filename) {
  const imageExtensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp']
  const extension = getFileExtension(filename).toLowerCase()
  return imageExtensions.includes(extension)
}

// 压缩图片
export function compressImage(filePath, quality = 0.8) {
  return new Promise((resolve, reject) => {
    uni.compressImage({
      src: filePath,
      quality,
      success: (res) => {
        resolve(res.tempFilePath)
      },
      fail: (error) => {
        reject(error)
      }
    })
  })
}

// 选择图片
export function chooseImage(count = 1, sourceType = ['album', 'camera']) {
  return new Promise((resolve, reject) => {
    uni.chooseImage({
      count,
      sourceType,
      success: (res) => {
        resolve(res.tempFilePaths)
      },
      fail: (error) => {
        reject(error)
      }
    })
  })
}

// 预览图片
export function previewImage(urls, current = 0) {
  uni.previewImage({
    urls,
    current: typeof current === 'number' ? urls[current] : current
  })
}

// 复制到剪贴板
export function copyToClipboard(text) {
  return new Promise((resolve, reject) => {
    uni.setClipboardData({
      data: text,
      success: () => {
        uni.showToast({
          title: 'Copied to clipboard',
          icon: 'success',
          duration: 1500
        })
        resolve()
      },
      fail: (error) => {
        reject(error)
      }
    })
  })
}

// 获取系统信息
export function getSystemInfo() {
  return new Promise((resolve, reject) => {
    uni.getSystemInfo({
      success: (res) => {
        resolve(res)
      },
      fail: (error) => {
        reject(error)
      }
    })
  })
}

// 获取网络状态
export function getNetworkType() {
  return new Promise((resolve, reject) => {
    uni.getNetworkType({
      success: (res) => {
        resolve(res.networkType)
      },
      fail: (error) => {
        reject(error)
      }
    })
  })
}

// 震动反馈
export function vibrateShort() {
  uni.vibrateShort({
    type: 'light'
  })
}

// 长震动反馈
export function vibrateLong() {
  uni.vibrateLong()
}

// 显示加载提示
export function showLoading(title = 'Loading...', mask = true) {
  uni.showLoading({
    title,
    mask
  })
}

// 隐藏加载提示
export function hideLoading() {
  uni.hideLoading()
}

// 显示成功提示
export function showSuccess(title, duration = 2000) {
  uni.showToast({
    title,
    icon: 'success',
    duration
  })
}

// 显示错误提示
export function showError(title, duration = 2000) {
  uni.showToast({
    title,
    icon: 'error',
    duration
  })
}

// 显示普通提示
export function showToast(title, duration = 2000) {
  uni.showToast({
    title,
    icon: 'none',
    duration
  })
}

// 显示确认对话框
export function showConfirm(content, title = 'Confirm') {
  return new Promise((resolve) => {
    uni.showModal({
      title,
      content,
      showCancel: true,
      confirmColor: '#FF6B6B',
      success: (res) => {
        resolve(res.confirm)
      }
    })
  })
}

// 页面跳转
export function navigateTo(url, params = {}) {
  const queryString = Object.keys(params)
    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
    .join('&')
  
  const fullUrl = queryString ? `${url}?${queryString}` : url
  
  uni.navigateTo({
    url: fullUrl
  })
}

// 页面重定向
export function redirectTo(url, params = {}) {
  const queryString = Object.keys(params)
    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
    .join('&')
  
  const fullUrl = queryString ? `${url}?${queryString}` : url
  
  uni.redirectTo({
    url: fullUrl
  })
}

// 返回上一页
export function navigateBack(delta = 1) {
  uni.navigateBack({
    delta
  })
}

// 记录应用使用统计
export function recordAppUsage(action, data = {}) {
  const usageData = {
    action,
    data,
    timestamp: new Date().toISOString(),
    page: getCurrentPages().pop()?.route || 'unknown'
  }
  
  // 获取现有统计数据
  const existingStats = storage.getItem('appUsageStats', [])
  existingStats.push(usageData)
  
  // 只保留最近1000条记录
  if (existingStats.length > 1000) {
    existingStats.splice(0, existingStats.length - 1000)
  }
  
  // 保存统计数据
  storage.setItem('appUsageStats', existingStats)
}