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
          :label-width="'auto'"
        >
          <!-- 邮箱输入 -->
          <u-form-item prop="email" label="Email Address">
            <u-input
              v-model="loginForm.email"
              placeholder="Enter your email"
              type="email"
              border="none"
              class="warm-input"
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
              class="warm-input"
              prefix-icon="lock"
              prefix-icon-style="color: #FF6B6B"
              @suffix-icon-click="togglePassword"
            ></u-input>
          </u-form-item>
        </u-form>
        
        <!-- 忘记密码链接 -->
       <!-- <view class="forgot-password">
          <u-text
            text="Forgot Password?"
            color="#FF6B6B"
            size="14"
            @click="handleForgotPassword"
          ></u-text>
        </view> -->
        
        <!-- 登录按钮 -->
        <view class="button-section">
          <u-button
            :loading="authStore.loginLoading"
            loading-text="Signing In..."
            loading-icon="spinner-cycle"
            class="primary-button"
            @click="handleLogin"
            text="Sign In"
          >
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
    

  </view>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
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
        
        // 调用登录API - 响应拦截器已处理统一格式，直接获取data
        const responseData = await authAPI.login(loginForm)
        
        // 保存认证信息
        authStore.setTokens({
          accessToken: responseData.accessToken,
          refreshToken: responseData.refreshToken
        })
        
        // 保存用户信息
        userStore.setUserProfile(responseData.user)
        
        // 显示成功提示
        uni.showToast({
          title: 'Login Successful',
          icon: 'success',
          duration: 2000
        })
        
        // 跳转到主页面
        uni.reLaunch({
          url: '/pages/main/index'
        })
      } catch (error) {
        console.error('Login error:', error)
        
        // 显示错误提示 - 响应拦截器已处理错误信息显示
        // 这里只需要记录日志，用户已经看到错误提示
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
      uni.navigateTo({
        url: '/pages/auth/register'
      })
    }
    
    // 生命周期
    onMounted(() => {
      // 隐藏底部tab（如果是tabBar页面）
      try {
        uni.hideTabBar()
      } catch (error) {
        // 非tabBar页面会报错，忽略即可
      }
    })
    
    return {
      // Store
      authStore,
      
      // 响应式数据
      loginFormRef,
      showPassword,

      loginForm,
      loginRules,
      

      
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
  width: 100%;
  height: 100%;
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
  

}

.forgot-password {
  text-align: right;
  margin-top: -4rpx;
  margin-bottom: 40rpx;
}

.button-section {
  margin-bottom: 40rpx;
  flex-shrink: 0;
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
      margin-bottom: 64rpx; /* 增加间距为错误提示预留空间 */
      position: relative;
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
      margin-bottom: 64rpx; /* 增加间距为错误提示预留空间 */
      position: relative;
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