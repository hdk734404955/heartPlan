// 本地存储工具类
class StorageUtil {
  // 设置存储项
  setItem(key, value) {
    try {
      const serializedValue = JSON.stringify(value)
      uni.setStorageSync(key, serializedValue)
      return true
    } catch (error) {
      console.error('Storage setItem error:', error)
      return false
    }
  }

  // 获取存储项
  getItem(key, defaultValue = null) {
    try {
      const serializedValue = uni.getStorageSync(key)
      if (serializedValue) {
        return JSON.parse(serializedValue)
      }
      return defaultValue
    } catch (error) {
      console.error('Storage getItem error:', error)
      return defaultValue
    }
  }

  // 移除存储项
  removeItem(key) {
    try {
      uni.removeStorageSync(key)
      return true
    } catch (error) {
      console.error('Storage removeItem error:', error)
      return false
    }
  }

  // 清空所有存储
  clear() {
    try {
      uni.clearStorageSync()
      return true
    } catch (error) {
      console.error('Storage clear error:', error)
      return false
    }
  }

  // 获取存储信息
  getInfo() {
    try {
      return uni.getStorageInfoSync()
    } catch (error) {
      console.error('Storage getInfo error:', error)
      return null
    }
  }

  // 检查存储项是否存在
  hasItem(key) {
    try {
      const value = uni.getStorageSync(key)
      return value !== '' && value !== null && value !== undefined
    } catch (error) {
      console.error('Storage hasItem error:', error)
      return false
    }
  }

  // 批量设置存储项
  setItems(items) {
    const results = {}
    for (const [key, value] of Object.entries(items)) {
      results[key] = this.setItem(key, value)
    }
    return results
  }

  // 批量获取存储项
  getItems(keys, defaultValues = {}) {
    const results = {}
    keys.forEach(key => {
      results[key] = this.getItem(key, defaultValues[key])
    })
    return results
  }

  // 批量移除存储项
  removeItems(keys) {
    const results = {}
    keys.forEach(key => {
      results[key] = this.removeItem(key)
    })
    return results
  }
}

// 创建存储工具实例
export const storage = new StorageUtil()

// 预定义的存储键名
export const STORAGE_KEYS = {
  // 认证相关
  ACCESS_TOKEN: 'accessToken',
  REFRESH_TOKEN: 'refreshToken',
  IS_LOGGED_IN: 'isLoggedIn',
  
  // 用户相关
  USER_PROFILE: 'userProfile',
  USER_PREFERENCES: 'userPreferences',
  PRIVACY_SETTINGS: 'privacySettings',
  
  // 应用设置
  APP_SETTINGS: 'appSettings',
  THEME_SETTINGS: 'themeSettings',
  LANGUAGE_SETTINGS: 'languageSettings',
  
  // 缓存相关
  CACHE_TEMPLATES: 'cacheTemplates',
  CACHE_COMMUNITY: 'cacheCommunity',
  CACHE_CHAT: 'cacheChat',
  
  // 临时数据（已清理，不再使用）
  // TEMP_REGISTER_DATA: 'tempRegisterData',
  // TEMP_FORM_DATA: 'tempFormData',
  
  // 统计数据
  APP_USAGE_STATS: 'appUsageStats',
  LAST_ACTIVE_TIME: 'lastActiveTime'
}