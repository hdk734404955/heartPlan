<template>
  <view class="main-container">
    <!-- 顶部导航 -->
    <view class="nav-header">
      <view class="nav-content">
        <view class="greeting-section">
          <u-avatar
            :src="userStore.userProfile.avatarUrl"
            size="40"
            shape="circle"
            bg-color="#FFF5F5"
            icon="account"
            icon-color="#FF6B6B"
          ></u-avatar>
          <view class="greeting-text">
            <view class="greeting-title">Welcome back!</view>
            <view class="greeting-name">{{ userStore.displayName }}</view>
          </view>
        </view>
        <u-icon name="bell" size="24" color="#2C3E50"></u-icon>
      </view>
    </view>
    
    <!-- 主要内容 -->
    <view class="content">
      <!-- 状态卡片 -->
      <view class="status-card">
        <view class="status-header">
          <view class="status-title">Your Love Journey</view>
          <u-tag
            :text="relationshipStatusText"
            :bg-color="statusColor"
            color="#FFFFFF"
            size="small"
            shape="circle"
          ></u-tag>
        </view>
        <view class="status-description">
          {{ statusDescription }}
        </view>
      </view>
      
      <!-- AI智能模板快速入口 -->
      <view class="templates-section">
        <view class="section-title">AI Smart Templates</view>
        <view class="templates-grid">
          <!-- 单身用户模板 -->
          <view v-if="userStore.isSingle" class="template-category">
            <view class="category-title">For Singles</view>
            <u-card 
              :show-head="false" 
              :border="false" 
              box-shadow="0 4rpx 12rpx rgba(255, 107, 107, 0.1)"
              margin="0 0 32rpx 0"
              @click="goToTemplate('dating-management')"
            >
              <view class="template-item">
                <u-icon name="heart" size="24" color="#FF6B6B"></u-icon>
                <view class="template-info">
                  <view class="template-name">Dating Management</view>
                  <view class="template-desc">Track dates & AI compatibility</view>
                </view>
                <u-icon name="arrow-right" size="16" color="#7F8C8D"></u-icon>
              </view>
            </u-card>
            
            <u-card 
              :show-head="false" 
              :border="false" 
              box-shadow="0 4rpx 12rpx rgba(255, 107, 107, 0.1)"
              margin="0 0 32rpx 0"
              @click="goToTemplate('date-tracking')"
            >
              <view class="template-item">
                <u-icon name="calendar" size="24" color="#FF9AA2"></u-icon>
                <view class="template-info">
                  <view class="template-name">Date Tracking</view>
                  <view class="template-desc">Record & analyze dates</view>
                </view>
                <u-icon name="arrow-right" size="16" color="#7F8C8D"></u-icon>
              </view>
            </u-card>
            
            <u-card 
              :show-head="false" 
              :border="false" 
              box-shadow="0 4rpx 12rpx rgba(255, 107, 107, 0.1)"
              margin="0 0 32rpx 0"
              @click="goToTemplate('charm-enhancement')"
            >
              <view class="template-item">
                <u-icon name="star" size="24" color="#FFB3BA"></u-icon>
                <view class="template-info">
                  <view class="template-name">Charm Enhancement</view>
                  <view class="template-desc">Improve your appeal</view>
                </view>
                <u-icon name="arrow-right" size="16" color="#7F8C8D"></u-icon>
              </view>
            </u-card>
          </view>
          
          <!-- 情侣用户模板 -->
          <view v-else class="template-category">
            <view class="category-title">For Couples</view>
            <u-card 
              :show-head="false" 
              :border="false" 
              box-shadow="0 4rpx 12rpx rgba(255, 107, 107, 0.1)"
              margin="0 0 32rpx 0"
              @click="goToTemplate('relationship-dashboard')"
            >
              <view class="template-item">
                <u-icon name="heart-fill" size="24" color="#00B894"></u-icon>
                <view class="template-info">
                  <view class="template-name">Relationship Dashboard</view>
                  <view class="template-desc">Health score & insights</view>
                </view>
                <u-icon name="arrow-right" size="16" color="#7F8C8D"></u-icon>
              </view>
            </u-card>
            
            <u-card 
              :show-head="false" 
              :border="false" 
              box-shadow="0 4rpx 12rpx rgba(255, 107, 107, 0.1)"
              margin="0 0 32rpx 0"
              @click="goToTemplate('important-dates')"
            >
              <view class="template-item">
                <u-icon name="calendar-fill" size="24" color="#FF9AA2"></u-icon>
                <view class="template-info">
                  <view class="template-name">Important Dates</view>
                  <view class="template-desc">Never miss special moments</view>
                </view>
                <u-icon name="arrow-right" size="16" color="#7F8C8D"></u-icon>
              </view>
            </u-card>
            
            <u-card 
              :show-head="false" 
              :border="false" 
              box-shadow="0 4rpx 12rpx rgba(255, 107, 107, 0.1)"
              margin="0 0 32rpx 0"
              @click="goToTemplate('conflict-resolution')"
            >
              <view class="template-item">
                <u-icon name="chat-fill" size="24" color="#FFB3BA"></u-icon>
                <view class="template-info">
                  <view class="template-name">Conflict Resolution</view>
                  <view class="template-desc">Resolve issues together</view>
                </view>
                <u-icon name="arrow-right" size="16" color="#7F8C8D"></u-icon>
              </view>
            </u-card>
            
            <u-card 
              :show-head="false" 
              :border="false" 
              box-shadow="0 4rpx 12rpx rgba(255, 107, 107, 0.1)"
              margin="0 0 32rpx 0"
              @click="goToTemplate('growth-trajectory')"
            >
              <view class="template-item">
                <u-icon name="trending-up" size="24" color="#FFAAA5"></u-icon>
                <view class="template-info">
                  <view class="template-name">Growth Trajectory</view>
                  <view class="template-desc">Track relationship progress</view>
                </view>
                <u-icon name="arrow-right" size="16" color="#7F8C8D"></u-icon>
              </view>
            </u-card>
          </view>
        </view>
      </view>
      
      <!-- 今日建议 -->
      <view class="daily-tips">
        <view class="section-title">Today's Tip</view>
        <view class="tip-card">
          <u-icon name="lightbulb" size="24" color="#FF6B6B"></u-icon>
          <view class="tip-content">
            <view class="tip-title">Communication is Key</view>
            <view class="tip-text">
              Take time to really listen to your partner today. Active listening strengthens emotional bonds.
            </view>
          </view>
        </view>
      </view>
    </view>
    

  </view>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '@/store/modules/auth'
import { useUserStore } from '@/store/modules/user'
import { beforePageLoad } from '@/utils/router-guard'

export default {
  name: 'MainPage',
  setup() {
    // Store
    const authStore = useAuthStore()
    const userStore = useUserStore()
    
    // 响应式数据
    
    // 计算属性
    const relationshipStatusText = computed(() => {
      return userStore.isSingle ? 'Single' : 'In Relationship'
    })
    
    const statusColor = computed(() => {
      return userStore.isSingle ? '#FF6B6B' : '#00B894'
    })
    
    const statusDescription = computed(() => {
      return userStore.isSingle 
        ? 'Discover your perfect match with AI-powered insights'
        : 'Strengthen your relationship with personalized guidance'
    })
    
    // 方法
    const goToTemplate = (templateType) => {
      uni.navigateTo({
        url: `/pages/templates/${templateType}`
      })
    }
    

    
    const checkAuthStatus = () => {
      // 检查认证状态
      if (!authStore.isAuthenticated) {
        uni.reLaunch({
          url: '/pages/splash/index'
        })
        return
      }
      
      // 检查用户资料完整性
      if (!userStore.hasCompleteProfile) {
        uni.showModal({
          title: 'Complete Profile',
          content: 'Please complete your profile to continue',
          showCancel: false,
          success: () => {
            uni.navigateTo({
              url: '/pages/profile/edit'
            })
          }
        })
      }
    }
    
    // 生命周期
    onMounted(() => {
      // 初始化用户数据
      userStore.initUserProfile()
      
      // 应用路由守卫
      beforePageLoad()
      
      // 检查认证状态
      checkAuthStatus()
    })
    
    return {
      // Store
      userStore,
      
      // 计算属性
      relationshipStatusText,
      statusColor,
      statusDescription,
      
      // 方法
      goToTemplate
    }
  }
}
</script>

<style lang="scss" scoped>
.main-container {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #FFF5F5 0%, #FEFEFE 100%);
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  padding-bottom: 0;
}

.nav-header {
  background: #FFFFFF;
  box-shadow: 0 2px 8px rgba(255, 107, 107, 0.1);
  padding: 32rpx 64rpx 24rpx;
  flex-shrink: 0;
  
  .nav-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  
  .greeting-section {
    display: flex;
    align-items: center;
    gap: 24rpx;
  }
  
  .greeting-text {
    .greeting-title {
      font-size: 24rpx; /* 辅助文字：24-28rpx */
      color: #7F8C8D;
      margin-bottom: 4rpx;
    }
    
    .greeting-name {
      font-size: 30rpx; /* 正文内容：28-32rpx */
      font-weight: 600;
      color: #2C3E50;
    }
  }
}

.content {
  flex: 1;
  padding: 40rpx 64rpx 0;
  box-sizing: border-box;
  overflow-y: auto;
  min-height: 0;
}

.status-card {
  background: linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%);
  border-radius: 32rpx;
  padding: 40rpx;
  margin-bottom: 48rpx;
  box-shadow: 0 6px 20px rgba(255, 107, 107, 0.2);
  
  .status-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16rpx;
  }
  
  .status-title {
    font-size: 36rpx; /* 正文内容：28-32rpx */
    font-weight: 700;
    color: #FFFFFF;
  }
  
  .status-description {
    font-size: 28rpx; /* 辅助文字：24-28rpx */
    color: rgba(255, 255, 255, 0.9);
    line-height: 1.4;
  }
}

.templates-section {
  margin-bottom: 40rpx;
  
  .section-title {
    font-size: 36rpx; /* 正文内容：28-32rpx */
    font-weight: 600;
    color: #2C3E50;
    margin-bottom: 24rpx;
  }
  
  .category-title {
    font-size: 30rpx; /* 正文内容：28-32rpx */
    font-weight: 600;
    color: #FF6B6B;
    margin-bottom: 16rpx;
  }
  
  .template-item {
    display: flex;
    align-items: center;
    padding: 24rpx;
    gap: 20rpx;
    
    .template-info {
      flex: 1;
    }
    
    .template-name {
      font-size: 30rpx; /* 正文内容：28-32rpx */
      font-weight: 600;
      color: #2C3E50;
      margin-bottom: 4rpx;
    }
    
    .template-desc {
      font-size: 24rpx; /* 辅助文字：24-28rpx */
      color: #7F8C8D;
    }
  }
}

.daily-tips {
  margin-bottom: 40rpx;
  
  .section-title {
    font-size: 36rpx; /* 正文内容：28-32rpx */
    font-weight: 600;
    color: #2C3E50;
    margin-bottom: 24rpx;
  }
  
  .tip-card {
    background: #FFFFFF;
    border-radius: 24rpx;
    padding: 32rpx;
    display: flex;
    gap: 24rpx;
    box-shadow: 0 3px 10px rgba(255, 107, 107, 0.1);
  }
  
  .tip-content {
    flex: 1;
  }
  
  .tip-title {
    font-size: 30rpx; /* 正文内容：28-32rpx */
    font-weight: 600;
    color: #2C3E50;
    margin-bottom: 12rpx;
  }
  
  .tip-text {
    font-size: 26rpx; /* 辅助文字：24-28rpx */
    color: #7F8C8D;
    line-height: 1.4;
  }
}



/* 响应式适配 - iPhone 6/7/8（375px宽度，750rpx） */
@media screen and (max-width: 750rpx) {
  .nav-header {
    padding: 32rpx 48rpx 24rpx;
  }
  
  .content {
    padding: 40rpx 48rpx 0;
  }
  
  .status-card {
    padding: 40rpx;
    margin-bottom: 48rpx;
  }
  
  .templates-section {
    margin-bottom: 48rpx;
  }
}

/* 安全区域适配 */
.nav-header {
  padding-top: calc(40rpx + constant(safe-area-inset-top));
  padding-top: calc(40rpx + env(safe-area-inset-top));
}


</style>