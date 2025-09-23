<template>
  <view class="container">
    <!-- 欢迎区域 -->
    <view class="welcome-section">
      <up-image 
        :src="logoUrl" 
        width="200rpx" 
        height="200rpx" 
        border-radius="24rpx"
        class="logo-image"
      ></up-image>
      
      <view class="welcome-text">
        <up-text 
          :text="appTitle" 
          type="primary" 
          size="48rpx" 
          bold
          class="app-title"
        ></up-text>
        <up-text 
          :text="appSubtitle" 
          color="#7F8C8D" 
          size="28rpx" 
          class="app-subtitle"
        ></up-text>
      </view>
    </view>

    <!-- 功能展示卡片 -->
    <view class="features-section">
      <up-card 
        v-for="(feature, index) in features" 
        :key="index"
        :title="feature.title"
        :sub-title="feature.subtitle"
        :thumb="feature.icon"
        thumb-width="80rpx"
        thumb-height="80rpx"
        class="feature-card"
        @click="handleFeatureClick(feature)"
      >
        <template #body>
          <up-text 
            :text="feature.description" 
            color="#2C3E50" 
            size="26rpx"
            class="feature-description"
          ></up-text>
        </template>
      </up-card>
    </view>

    <!-- 操作按钮区域 -->
    <view class="action-section">
      <up-button 
        text="Get Started" 
        type="primary"
        size="large"
        :custom-style="primaryButtonStyle"
        @click="handleGetStarted"
        class="get-started-btn"
      ></up-button>
      
      <up-button 
        text="Learn More" 
        type="default"
        size="large"
        :custom-style="secondaryButtonStyle"
        @click="handleLearnMore"
        class="learn-more-btn"
      ></up-button>
    </view>

    <!-- 状态展示 -->
    <view class="status-section">
      <up-alert 
        :title="statusMessage"
        type="success"
        :show-icon="true"
        :closable="false"
        class="status-alert"
      ></up-alert>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      appTitle: 'HeartPlan',
      appSubtitle: 'AI-powered dating planning tool',
      logoUrl: '/static/logo.png',
      statusMessage: 'uView components and warm trust design system successfully integrated!',
      features: [
        {
          title: 'AI Templates',
          subtitle: 'Smart Planning',
          description: 'Personalized templates for singles and couples with AI-powered insights',
          icon: '💝'
        },
        {
          title: 'Community',
          subtitle: 'Connect & Share',
          description: 'Join a supportive community to share experiences and get advice',
          icon: '👥'
        },
        {
          title: 'AI Coach',
          subtitle: '24/7 Support',
          description: 'Get personalized guidance from our AI relationship coach anytime',
          icon: '🤖'
        },
        {
          title: 'Profile',
          subtitle: 'Your Journey',
          description: 'Track your progress and manage your relationship growth',
          icon: '📊'
        }
      ],
      primaryButtonStyle: {
        background: 'linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%)',
        border: 'none',
        borderRadius: '48rpx',
        color: '#FFFFFF',
        fontWeight: '500'
      },
      secondaryButtonStyle: {
        background: '#FFFFFF',
        border: '2rpx solid #FF6B6B',
        borderRadius: '48rpx',
        color: '#FF6B6B',
        fontWeight: '500'
      }
    }
  },
  onLoad() {
    console.log('HeartPlan index page loaded with uView components')
  },
  methods: {
    handleGetStarted() {
      uni.showToast({
        title: 'Welcome to HeartPlan!',
        icon: 'success',
        duration: 2000
      })
    },
    handleLearnMore() {
      uni.showModal({
        title: 'HeartPlan Features',
        content: 'Discover AI-powered relationship planning tools designed to help you build meaningful connections.',
        showCancel: false,
        confirmText: 'Got it',
        confirmColor: '#FF6B6B'
      })
    },
    handleFeatureClick(feature) {
      uni.showToast({
        title: `${feature.title} coming soon!`,
        icon: 'none',
        duration: 1500
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  padding: 64rpx 32rpx;
  min-height: 100vh;
  background: linear-gradient(180deg, #FFF5F5 0%, #FFFFFF 100%);
}

.welcome-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 80rpx;
  
  .logo-image {
    margin-bottom: 48rpx;
    box-shadow: 0 8px 24px 0 rgba(255, 107, 107, 0.2);
  }
  
  .welcome-text {
    text-align: center;
    
    .app-title {
      margin-bottom: 16rpx;
      background: linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
    
    .app-subtitle {
      line-height: 1.6;
    }
  }
}

.features-section {
  margin-bottom: 80rpx;
  
  .feature-card {
    margin-bottom: 32rpx;
    border-radius: 24rpx !important;
    box-shadow: 0 4px 16px 0 rgba(255, 107, 107, 0.15) !important;
    
    .feature-description {
      line-height: 1.5;
      margin-top: 16rpx;
    }
  }
}

.action-section {
  margin-bottom: 64rpx;
  
  .get-started-btn {
    margin-bottom: 32rpx;
    box-shadow: 0 4px 16px 0 rgba(255, 107, 107, 0.3);
  }
  
  .learn-more-btn {
    box-shadow: 0 2px 8px 0 rgba(255, 107, 107, 0.1);
  }
}

.status-section {
  .status-alert {
    border-radius: 16rpx !important;
    background: rgba(0, 184, 148, 0.1) !important;
    border: 1rpx solid #00B894 !important;
  }
}

/* 响应式适配 */
@media screen and (max-width: 480rpx) {
  .container {
    padding: 48rpx 24rpx;
  }
  
  .welcome-section {
    margin-bottom: 64rpx;
  }
  
  .features-section {
    margin-bottom: 64rpx;
  }
}
</style>
