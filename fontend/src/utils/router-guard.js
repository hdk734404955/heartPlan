// 路由守卫工具
import { useAuthStore } from '@/store/modules/auth'
import { useUserStore } from '@/store/modules/user'

// 需要认证的页面路径
const AUTH_REQUIRED_PAGES = [
  '/pages/main/index',
  '/pages/community/index',
  '/pages/chat/index',
  '/pages/profile/index',
  '/pages/templates/',
  '/pages/profile/edit'
]

// 认证页面路径
const AUTH_PAGES = [
  '/pages/auth/login',
  '/pages/auth/register'
]

// 公共页面路径
const PUBLIC_PAGES = [
  '/pages/splash/index',
  '/pages/index/index'
]

/**
 * 检查页面是否需要认证
 * @param {string} path 页面路径
 * @returns {boolean} 是否需要认证
 */
export const isAuthRequired = (path) => {
  return AUTH_REQUIRED_PAGES.some(authPath => path.startsWith(authPath))
}

/**
 * 检查是否为认证页面
 * @param {string} path 页面路径
 * @returns {boolean} 是否为认证页面
 */
export const isAuthPage = (path) => {
  return AUTH_PAGES.some(authPath => path.startsWith(authPath))
}

/**
 * 检查是否为公共页面
 * @param {string} path 页面路径
 * @returns {boolean} 是否为公共页面
 */
export const isPublicPage = (path) => {
  return PUBLIC_PAGES.some(publicPath => path.startsWith(publicPath))
}

/**
 * 路由守卫检查
 * @param {string} targetPath 目标页面路径
 * @returns {object} 守卫结果 { allowed: boolean, redirectTo?: string }
 */
export const routeGuard = (targetPath) => {
  const authStore = useAuthStore()
  const userStore = useUserStore()
  
  // 初始化认证状态
  authStore.initAuth()
  userStore.initUserProfile()
  
  console.log('Route guard check:', {
    targetPath,
    isAuthenticated: authStore.isAuthenticated,
    hasCompleteProfile: userStore.hasCompleteProfile
  })
  
  // 如果是公共页面，直接允许访问
  if (isPublicPage(targetPath)) {
    return { allowed: true }
  }
  
  // 如果是认证页面
  if (isAuthPage(targetPath)) {
    // 已登录用户访问认证页面，重定向到主页面
    if (authStore.isAuthenticated) {
      if (userStore.hasCompleteProfile) {
        return { allowed: false, redirectTo: '/pages/main/index' }
      } else {
        return { allowed: false, redirectTo: '/pages/profile/edit' }
      }
    }
    // 未登录用户可以访问认证页面
    return { allowed: true }
  }
  
  // 如果是需要认证的页面
  if (isAuthRequired(targetPath)) {
    // 未登录用户重定向到登录页
    if (!authStore.isAuthenticated) {
      return { allowed: false, redirectTo: '/pages/auth/login' }
    }
    
    // 已登录但资料不完整，重定向到资料完善页面
    if (!userStore.hasCompleteProfile && targetPath !== '/pages/profile/edit') {
      return { allowed: false, redirectTo: '/pages/profile/edit' }
    }
    
    // 已登录且资料完整，允许访问
    return { allowed: true }
  }
  
  // 默认允许访问
  return { allowed: true }
}

/**
 * 应用路由守卫
 * @param {string} targetPath 目标页面路径
 * @param {object} options 导航选项
 */
export const applyRouteGuard = (targetPath, options = {}) => {
  const guardResult = routeGuard(targetPath)
  
  if (!guardResult.allowed && guardResult.redirectTo) {
    console.log('Route guard redirect:', targetPath, '->', guardResult.redirectTo)
    
    // 执行重定向
    if (options.reLaunch) {
      uni.reLaunch({ url: guardResult.redirectTo })
    } else if (options.switchTab) {
      uni.switchTab({ url: guardResult.redirectTo })
    } else {
      uni.redirectTo({ url: guardResult.redirectTo })
    }
    
    return false
  }
  
  return true
}

/**
 * 页面导航前的守卫检查
 * 在页面的onLoad生命周期中调用
 */
export const beforePageLoad = () => {
  const pages = getCurrentPages()
  if (pages.length === 0) return
  
  const currentPage = pages[pages.length - 1]
  const currentPath = '/' + currentPage.route
  
  const guardResult = routeGuard(currentPath)
  
  if (!guardResult.allowed && guardResult.redirectTo) {
    console.log('Page load guard redirect:', currentPath, '->', guardResult.redirectTo)
    
    // 延迟执行重定向，避免在页面加载过程中导航
    setTimeout(() => {
      uni.reLaunch({ url: guardResult.redirectTo })
    }, 100)
  }
}

/**
 * 自动登录检查
 * 在应用启动时调用
 */
export const autoLoginCheck = async () => {
  const authStore = useAuthStore()
  const userStore = useUserStore()
  
  try {
    // 初始化状态
    authStore.initAuth()
    userStore.initUserProfile()
    
    // 如果有访问令牌，尝试刷新
    if (authStore.accessToken) {
      const refreshSuccess = await authStore.refreshAccessToken()
      if (!refreshSuccess) {
        // 刷新失败，清除状态
        authStore.clearTokens()
        userStore.clearUserProfile()
      }
    }
    
    return authStore.isAuthenticated
  } catch (error) {
    console.error('Auto login check failed:', error)
    return false
  }
}