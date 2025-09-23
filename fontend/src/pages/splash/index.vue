<template>
  <view class="splash-container">
    <!-- 背景装饰 -->
    <view class="bg-decoration">
      <view class="circle circle-1"></view>
      <view class="circle circle-2"></view>
      <view class="circle circle-3"></view>
      <view class="circle circle-4"></view>
    </view>
    
    <!-- 主要内容 -->
    <view class="content">
      <!-- Logo区域 -->
      <view class="logo-section">
        <view class="logo-container">
          <text class="logo-icon">💖</text>
        </view>
        <view class="app-name">HeartPlan</view>
        <view class="app-tagline">AI-Powered Love Journey</view>
      </view>
      
      <!-- 加载动画 -->
      <view class="loading-section">
        <u-loading-icon
          :show="true"
          color="#FF6B6B"
          size="24"
          mode="circle"
        ></u-loading-icon>
        <view class="loading-text">{{ loadingText }}</view>
      </view>
      
      <!-- 版本信息 -->
      <view class="version-info">
        <u-text
          text="Version 1.0.0"
          color="#7F8C8D"
          size="12"
        ></u-text>
      </view>
    </view>
  </view>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/store/modules/auth'
import { useUserStore } from '@/store/modules/user'

export default {
  name: 'SplashPage',
  setup() {
    // Store
    const authStore = useAuthStore()
    const userStore = useUserStore()
    
    // 响应式数据
    const loadingText = ref('Initializing...')
    
    // 加载文本动画
    const loadingTexts = [
      'Initializing...',
      'Loading your profile...',
      'Preparing AI insights...',
      'Almost ready...'
    ]
    
    let textIndex = 0
    
    // 方法
    const updateLoadingText = () => {
      const interval = setInterval(() => {
        textIndex = (textIndex + 1) % loadingTexts.length
        loadingText.value = loadingTexts[textIndex]
      }, 800)
      
      return interval
    }
    
    const checkAuthAndNavigate = async () => {
      try {
        // 初始化认证状态
        authStore.initAuth()
        userStore.initUserProfile()
        
        // 等待最少2秒显示启动画面
        await new Promise(resolve => setTimeout(resolve, 2000))
        
        // 检查认证状态
        if (authStore.isAuthenticated) {
          // 已登录，检查用户资料完整性
          if (userStore.hasCompleteProfile) {
            // 资料完整，跳转到主页面
            uni.reLaunch({
              url: '/pages/main/index'
            })
          } else {
            // 资料不完整，跳转到资料完善页面
            uni.reLaunch({
              url: '/pages/profile/edit'
            })
          }
        } else {
          // 未登录，跳转到登录页面
          uni.reLaunch({
            url: '/pages/auth/login'
          })
        }
      } catch (error) {
        console.error('Splash initialization error:', error)
        
        // 出错时跳转到登录页面
        uni.reLaunch({
          url: '/pages/auth/login'
        })
      }
    }
    
    // 生命周期
    onMounted(() => {
      // 开始加载文本动画
      const textInterval = updateLoadingText()
      
      // 执行初始化检查
      checkAuthAndNavigate().finally(() => {
        // 清除文本动画
        clearInterval(textInterval)
      })
    })
    
    return {
      // 响应式数据
      loadingText
    }
  }
}
</script>

<style lang="scss" scoped>
.splash-container {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 50%, #FFB3BA 100%);
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
  
  .circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.2;
    
    &.circle-1 {
      width: 300px;
      height: 300px;
      background: rgba(255, 255, 255, 0.1);
      top: -150px;
      right: -150px;
      animation: float 6s ease-in-out infinite;
    }
    
    &.circle-2 {
      width: 200px;
      height: 200px;
      background: rgba(255, 255, 255, 0.15);
      bottom: -100px;
      left: -100px;
      animation: float 8s ease-in-out infinite reverse;
    }
    
    &.circle-3 {
      width: 150px;
      height: 150px;
      background: rgba(255, 255, 255, 0.1);
      top: 20%;
      left: 10%;
      animation: float 7s ease-in-out infinite;
    }
    
    &.circle-4 {
      width: 100px;
      height: 100px;
      background: rgba(255, 255, 255, 0.2);
      bottom: 30%;
      right: 20%;
      animation: float 5s ease-in-out infinite reverse;
    }
  }
}

.content {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 60px 40px;
  width: 100%;
  height: 100%;
}

.logo-section {
  margin-bottom: 80px;
  animation: fadeInUp 1s ease-out;
  
  .logo-container {
    margin-bottom: 24px;
    
    .logo-icon {
      font-size: 80px;
      animation: heartbeat 2s ease-in-out infinite;
    }
  }
  
  .app-name {
    font-size: 36px;
    font-weight: 700;
    color: #FFFFFF;
    margin-bottom: 12px;
    letter-spacing: 2px;
    text-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
  
  .app-tagline {
    font-size: 16px;
    color: rgba(255, 255, 255, 0.9);
    font-weight: 400;
    letter-spacing: 1px;
  }
}

.loading-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  margin-bottom: 60px;
  animation: fadeInUp 1s ease-out 0.5s both;
  
  .loading-text {
    font-size: 14px;
    color: rgba(255, 255, 255, 0.8);
    font-weight: 500;
    animation: pulse 1.5s ease-in-out infinite;
  }
}

.version-info {
  position: absolute;
  bottom: 40px;
  animation: fadeInUp 1s ease-out 1s both;
}

/* 动画定义 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes heartbeat {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

/* 响应式适配 */
@media screen and (max-width: 375px) {
  .content {
    padding: 40px 30px;
  }
  
  .logo-section {
    margin-bottom: 60px;
    
    .logo-container .logo-icon {
      font-size: 60px;
    }
    
    .app-name {
      font-size: 28px;
    }
    
    .app-tagline {
      font-size: 14px;
    }
  }
}

/* 安全区域适配 */
.splash-container {
  padding-top: constant(safe-area-inset-top);
  padding-top: env(safe-area-inset-top);
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}
</style>