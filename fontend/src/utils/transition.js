// 页面转场工具函数
import { ref } from 'vue'

/**
 * 创建页面转场hook
 * @param {Object} options 配置选项
 * @returns {Object} 转场相关的响应式数据和方法
 */
export function usePageTransition(options = {}) {
  const {
    duration = 300,
    type = 'fade',
    text = ''
  } = options

  const showTransition = ref(false)
  const transitionText = ref(text)

  /**
   * 执行页面转场并跳转
   * @param {Function} navigationFn 导航函数
   * @param {Object} transitionOptions 转场选项
   */
  const transitionTo = (navigationFn, transitionOptions = {}) => {
    const {
      text: newText = text,
      type: transitionType = type,
      duration: transitionDuration = duration
    } = transitionOptions

    transitionText.value = newText
    showTransition.value = true

    setTimeout(() => {
      if (typeof navigationFn === 'function') {
        navigationFn()
      }
    }, transitionDuration)
  }

  /**
   * 快速跳转到登录页面
   */
  const transitionToLogin = (text = 'Redirecting to login...') => {
    transitionTo(() => {
      uni.reLaunch({
        url: '/pages/auth/login'
      })
    }, { text, type: 'slide' })
  }

  /**
   * 快速跳转到注册页面
   */
  const transitionToRegister = (text = 'Going to registration...') => {
    transitionTo(() => {
      uni.navigateTo({
        url: '/pages/auth/register'
      })
    }, { text, type: 'fade' })
  }

  /**
   * 快速跳转到主页面
   */
  const transitionToMain = (text = 'Welcome to HeartPlan...') => {
    transitionTo(() => {
      uni.reLaunch({
        url: '/pages/main/index'
      })
    }, { text, type: 'zoom' })
  }

  /**
   * 返回上一页面
   */
  const transitionBack = (text = 'Going back...') => {
    transitionTo(() => {
      uni.navigateBack({
        delta: 1,
        fail: () => {
          uni.reLaunch({
            url: '/pages/auth/login'
          })
        }
      })
    }, { text, type: 'slide' })
  }

  return {
    showTransition,
    transitionText,
    transitionTo,
    transitionToLogin,
    transitionToRegister,
    transitionToMain,
    transitionBack
  }
}

/**
 * 转场类型枚举
 */
export const TransitionTypes = {
  FADE: 'fade',
  SLIDE: 'slide',
  ZOOM: 'zoom'
}

/**
 * 预定义的转场文本
 */
export const TransitionTexts = {
  LOGIN: 'Signing you in...',
  REGISTER: 'Creating your account...',
  LOGOUT: 'Signing you out...',
  LOADING: 'Loading...',
  REDIRECTING: 'Redirecting...',
  WELCOME: 'Welcome to HeartPlan...',
  GOING_BACK: 'Going back...'
}