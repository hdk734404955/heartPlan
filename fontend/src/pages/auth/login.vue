<template>
  <view class="login-container">
    <!-- 背景装饰 -->
    <view class="bg-decoration">
      <view class="circle circle-1"></view>
      <view class="circle circle-2"></view>
      <view class="circle circle-3"></view>
    </view>
    
    <!-- 主要内容 -->
    <view class="content">
      <!-- Logo和标题区域 -->
      <view class="header-section">
        <view class="logo-container">
          <text class="logo-icon">💖</text>
        </view>
        <view class="title">Welcome Back</view>
        <view class="subtitle">Sign in to continue your love journey</view>
      </view>
      
      <!-- 登录表单 -->
      <view class="form-section">
        <u-form
          :model="loginForm"
          :rules="loginRules"
          ref="loginFormRef"
          label-position="top"
          border="none"
        >
          <!-- 邮箱输入 -->
          <u-form-item prop="email" label="Email Address">
            <u-input
              v-model="loginForm.email"
              placeholder="Enter your email"
              type="email"
              border="none"
              :custom-style="inputStyle"
              prefix-icon="email"
              prefix-icon-style="color: #FF6B6B"
              @blur="validateEmail"
            ></u-input>
          </u-form-item>
          
          <!-- 密码输入 -->
          <u-form-item prop="password" label="Password">
            <u-input
              v-model="loginForm.password"
              placeholder="Enter your password"
              :password="!showPassword"
              border="none"
              :custom-style="inputStyle"
              prefix-icon="lock"
              prefix-icon-style="color: #FF6B6B"
              :suffix-icon="showPassword ? 'eye-off' : 'eye'"
              suffix-icon-style="color: #7F8C8D"
              @suffix-icon-click="togglePassword"
            ></u-input>
          </u-form-item>
        </u-form>
        
        <!-- 忘记密码链接 -->
        <view class="forgot-password">
          <u-text
            text="Forgot Password?"
            color="#FF6B6B"
            size="14"
            @click="handleForgotPassword"
          ></u-text>
        </view>
        
        <!-- 登录按钮 -->
        <view class="button-section">
          <u-button
            :loading="authStore.loginLoading"
            loading-text="Signing In..."
            :custom-style="primaryButtonStyle"
            @click="handleLogin"
          >
            <u-text text="Sign In" color="#FFFFFF" size="16" bold></u-text>
          </u-button>
        </view>
        
        <!-- 分割线 -->
        <view class="divider">
          <u-divider text="or" color="#7F8C8D" text-color="#7F8C8D"></u-divider>
        </view>
        
        <!-- 注册链接 -->
        <view class="register-section">
          <u-text text="Don't have an account? " color="#7F8C8D" size="14"></u-text>
          <u-text
            text="Sign Up"
            color="#FF6B6B"
            size="14"
            bold
            @click="goToRegister"
          ></u-text>
        </view>
      </view>
    </view>
    
    <!-- 页面转场动画 -->
    <u-transition
      :show="showTransition"
      mode="fade"
      :duration="300"
    >
      <view class="transition-overlay"></view>
    </u-transition>
  </view>
</template>

<script>
import { ref, reactive, computed } from 'vue'
import { useAuthStore } from '@/store/modules/auth'
import { useUserStore } from '@/store/modules/user'
import { authAPI } from '@/api/auth'

export default {
  name: 'LoginPage',
  setup() {
    // Store
    const authStore = useAuthStore()
    const userStore = useUserStore()
    
    // 响应式数据
    const loginFormRef = ref(null)
    const showPassword = ref(false)
    const showTransition = ref(false)
    
    // 表单数据
    const loginForm = reactive({
      email: '',
      password: ''
    })
    
    // 表单验证规则
    const loginRules = reactive({
      email: [
        {
          required: true,
          message: 'Email is required',
          trigger: ['blur', 'change']
        },
        {
          type: 'email',
          message: 'Please enter a valid email address',
          trigger: ['blur', 'change']
        }
      ],
      password: [
        {
          required: true,
          message: 'Password is required',
          trigger: ['blur', 'change']
        },
        {
          min: 6,
          message: 'Password must be at least 6 characters',
          trigger: ['blur', 'change']
        }
      ]
    })
    
    // 样式配置 - 使用rpx响应式单位
    const inputStyle = computed(() => ({
      backgroundColor: '#FFF5F5',
      borderRadius: '24rpx',
      padding: '32rpx 40rpx',
      fontSize: '30rpx',
      border: 'none'
    }))
    
    const primaryButtonStyle = computed(() => ({
      background: 'linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%)',
      borderRadius: '48rpx',
      height: '100rpx',
      border: 'none',
      boxShadow: '0 4px 16px 0 rgba(255, 107, 107, 0.3)'
    }))
    
    // 方法
    const togglePassword = () => {
      showPassword.value = !showPassword.value
    }
    
    const validateEmail = () => {
      if (loginFormRef.value) {
        loginFormRef.value.validateField('email')
      }
    }
    
    const handleLogin = async () => {
      try {
        // 表单验证
        const valid = await loginFormRef.value.validate()
        if (!valid) return
        
        // 设置加载状态
        authStore.setLoginLoading(true)
        
        // 调用登录API
        const response = await authAPI.login(loginForm)
        
        if (response.data.success) {
          // 保存认证信息
          authStore.setTokens({
            accessToken: response.data.data.accessToken,
            refreshToken: response.data.data.refreshToken
          })
          
          // 保存用户信息
          userStore.setUserProfile(response.data.data.userProfile)
          
          // 显示成功提示
          uni.showToast({
            title: 'Login Successful',
            icon: 'success',
            duration: 2000
          })
          
          // 页面转场动画
          showTransition.value = true
          
          // 延迟跳转到主页面
          setTimeout(() => {
            uni.reLaunch({
              url: '/pages/main/index'
            })
          }, 300)
        } else {
          // 显示错误信息
          uni.showToast({
            title: response.data.message || 'Login failed',
            icon: 'error',
            duration: 2000
          })
        }
      } catch (error) {
        console.error('Login error:', error)
        
        // 显示网络错误提示
        uni.showToast({
          title: 'Network error, please try again',
          icon: 'error',
          duration: 2000
        })
      } finally {
        // 清除加载状态
        authStore.setLoginLoading(false)
      }
    }
    
    const handleForgotPassword = () => {
      uni.showModal({
        title: 'Forgot Password',
        content: 'Password reset feature will be available soon.',
        showCancel: false,
        confirmText: 'OK'
      })
    }
    
    const goToRegister = () => {
      showTransition.value = true
      setTimeout(() => {
        uni.navigateTo({
          url: '/pages/auth/register'
        })
        showTransition.value = false
      }, 300)
    }
    
    
    
    return {
      // Store
      authStore,
      
      // 响应式数据
      loginFormRef,
      showPassword,
      showTransition,
      loginForm,
      loginRules,
      
      // 计算属性
      inputStyle,
      primaryButtonStyle,
      
      // 方法
      togglePassword,
      validateEmail,
      handleLogin,
      handleForgotPassword,
      goToRegister,
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #FFF5F5 0%, #FEFEFE 100%);
  position: relative;
  overflow: hidden;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
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
    opacity: 0.1;
    
    &.circle-1 {
      width: 400rpx;
      height: 400rpx;
      background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
      top: -200rpx;
      right: -200rpx;
    }
    
    &.circle-2 {
      width: 300rpx;
      height: 300rpx;
      background: linear-gradient(135deg, #FFB3BA, #FF9AA2);
      bottom: 20%;
      left: -150rpx;
    }
    
    &.circle-3 {
      width: 200rpx;
      height: 200rpx;
      background: linear-gradient(135deg, #FFAAA5, #FFB3BA);
      top: 30%;
      left: 20%;
    }
  }
}

.content {
  position: relative;
  z-index: 1;
  padding: 80rpx 64rpx 64rpx;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  box-sizing: border-box;
  overflow-y: auto;
  min-height: 0;
}

.header-section {
  text-align: center;
  margin-bottom: 60rpx;
  flex-shrink: 0;
  padding-top: 40rpx;
  
  .logo-container {
    margin-bottom: 32rpx;
    animation: fadeInDown 0.8s ease-out;
    
    .logo-icon {
      font-size: 80rpx;
    }
  }
  
  .title {
    font-size: 56rpx; /* 页面标题：48-64rpx */
    font-weight: 700;
    color: #2C3E50;
    margin-bottom: 16rpx;
    animation: fadeInUp 0.8s ease-out 0.2s both;
  }
  
  .subtitle {
    font-size: 28rpx; /* 辅助文字：24-28rpx */
    color: #7F8C8D;
    line-height: 1.4;
    animation: fadeInUp 0.8s ease-out 0.4s both;
  }
}

.form-section {
  flex: 1;
  animation: fadeInUp 0.8s ease-out 0.6s both;
  display: flex;
  flex-direction: column;
  min-height: 0;
  
  :deep(.u-form-item__label) {
    font-size: 30rpx; /* 正文内容：28-32rpx */
    font-weight: 600;
    color: #2C3E50;
    margin-bottom: 16rpx;
  }
  
  :deep(.u-form-item) {
    margin-bottom: 32rpx; /* 中间距：32rpx */
  }
}

.forgot-password {
  text-align: right;
  margin-top: -4rpx;
  margin-bottom: 40rpx;
}

.button-section {
  margin-bottom: 40rpx;
  flex-shrink: 0;
  
  :deep(.u-button) {
    width: 100%;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    text-align: center !important;
    display: flex !important;
    align-items: center !important;
    justify-content: center !important;
    position: relative;
    
    &:active {
      transform: translateY(2rpx) scale(0.98);
      box-shadow: 0 2px 8px 0 rgba(255, 107, 107, 0.4);
    }
    
    &:hover {
      transform: translateY(-2rpx);
      box-shadow: 0 6px 20px 0 rgba(255, 107, 107, 0.3);
    }
  }
  
  :deep(.u-button__text) {
    text-align: center !important;
    width: 100% !important;
    display: flex !important;
    align-items: center !important;
    justify-content: center !important;
    line-height: 1 !important;
  }
  
  :deep(.u-text) {
    text-align: center !important;
    width: 100% !important;
  }
}

.divider {
  margin: 32rpx 0;
  flex-shrink: 0;
}

.register-section {
  text-align: center;
  margin-bottom: 24rpx;
  flex-shrink: 0;
}

.test-links-section {
  text-align: center;
  margin-bottom: 20px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  
  text {
    color: #7F8C8D;
    font-size: 12px;
  }
}

.transition-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%);
  z-index: 9999;
}

/* 动画定义 */
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-60rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(60rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式适配 - iPhone 6/7/8（375px宽度，750rpx） */
@media screen and (max-width: 375px) {
  .content {
    padding: 60rpx 48rpx 40rpx;
  }
  
  .header-section {
    margin-bottom: 48rpx;
    padding-top: 20rpx;
    
    .logo-container {
      margin-bottom: 24rpx;
      
      .logo-icon {
        font-size: 72rpx;
      }
    }
    
    .title {
      font-size: 48rpx; /* 页面标题：48-64rpx */
    }
    
    .subtitle {
      font-size: 24rpx; /* 辅助文字：24-28rpx */
    }
  }
  
  .form-section {
    :deep(.u-form-item) {
      margin-bottom: 32rpx;
    }
  }
  
  .button-section {
    margin-bottom: 32rpx;
  }
  
  .divider {
    margin: 24rpx 0;
  }
  
  .register-section {
    margin-bottom: 20rpx;
  }
}

/* iPhone X系列适配（414px宽度，828rpx） */
@media screen and (min-width: 376px) and (max-width: 414px) {
  .content {
    padding: 80rpx 64rpx 64rpx;
  }
  
  .header-section {
    .title {
      font-size: 60rpx; /* 页面标题：48-64rpx */
    }
    
    .subtitle {
      font-size: 28rpx; /* 辅助文字：24-28rpx */
    }
  }
}

/* Android主流机型适配（360px-428px宽度） */
@media screen and (min-width: 360px) and (max-width: 428px) {
  .login-container {
    font-size: 30rpx; /* 确保基础字体大小适配 */
  }
}

/* 超小屏幕适配 - Android主流机型（360px-428px宽度） */
@media screen and (max-height: 600px) {
  .content {
    justify-content: flex-start;
    padding: 40rpx 64rpx 40rpx;
  }
  
  .header-section {
    margin-bottom: 40rpx;
    padding-top: 20rpx;
    
    .logo-container {
      margin-bottom: 20rpx;
      
      .logo-icon {
        font-size: 64rpx;
      }
    }
    
    .title {
      font-size: 48rpx; /* 页面标题：48-64rpx */
      margin-bottom: 8rpx;
    }
    
    .subtitle {
      font-size: 24rpx; /* 辅助文字：24-28rpx */
    }
  }
  
  .form-section {
    :deep(.u-form-item) {
      margin-bottom: 28rpx;
    }
  }
}

/* 安全区域适配 */
.login-container {
  padding-top: constant(safe-area-inset-top);
  padding-top: env(safe-area-inset-top);
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}
</style>