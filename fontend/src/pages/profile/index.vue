<template>
  <view class="profile-container">
    <!-- 顶部导航 -->
    <view class="nav-header">
      <view class="nav-title">Profile</view>
      <u-icon name="setting" size="24" color="#2C3E50" @click="goToSettings"></u-icon>
    </view>
    
    <!-- 用户信息卡片 -->
    <view class="user-info-section">
      <u-card 
        :show-head="false" 
        :border="false" 
        box-shadow="0 4rpx 12rpx rgba(255, 107, 107, 0.1)"
        margin="0 0 48rpx 0"
      >
        <view class="user-info">
          <u-avatar
            src=""
            size="80"
            shape="circle"
            bg-color="#FF6B6B"
            icon="account"
            icon-color="#FFFFFF"
          ></u-avatar>
          <view class="user-details">
            <view class="user-name">John Doe</view>
            <view class="user-status">
              <u-tag 
                text="Single" 
                bg-color="#FF6B6B" 
                color="#FFFFFF" 
                size="small"
                shape="circle"
              ></u-tag>
            </view>
            <view class="user-location">
              <u-icon name="map-pin" size="14" color="#7F8C8D"></u-icon>
              <text class="location-text">New York, NY</text>
            </view>
          </view>
          <u-button
            type="primary"
            size="small"
            :custom-style="editButtonStyle"
            @click="goToEditProfile"
          >
            Edit
          </u-button>
        </view>
      </u-card>
    </view>
    
    <!-- 功能菜单 -->
    <view class="menu-section">
      <u-card 
        :show-head="false" 
        :border="false" 
        box-shadow="0 4rpx 12rpx rgba(255, 107, 107, 0.1)"
        margin="0 0 32rpx 0"
      >
        <u-cell-group :border="false">
          <u-cell 
            title="My Data Insights" 
            :is-link="true"
            @click="goToInsights"
          >
            <template #icon>
              <u-icon name="chart-line" size="20" color="#FF6B6B"></u-icon>
            </template>
          </u-cell>
          <u-cell 
            title="My Activity" 
            :is-link="true"
            @click="goToActivity"
          >
            <template #icon>
              <u-icon name="clock" size="20" color="#FF9AA2"></u-icon>
            </template>
          </u-cell>
          <u-cell 
            title="Following" 
            :is-link="true"
            @click="goToFollowing"
          >
            <template #icon>
              <u-icon name="heart" size="20" color="#FFB3BA"></u-icon>
            </template>
          </u-cell>
        </u-cell-group>
      </u-card>
      
      <u-card 
        :show-head="false" 
        :border="false" 
        box-shadow="0 4rpx 12rpx rgba(255, 107, 107, 0.1)"
        margin="0 0 32rpx 0"
      >
        <u-cell-group :border="false">
          <u-cell 
            title="Privacy Settings" 
            :is-link="true"
            @click="goToPrivacy"
          >
            <template #icon>
              <u-icon name="lock" size="20" color="#FFAAA5"></u-icon>
            </template>
          </u-cell>
          <u-cell 
            title="Help Center" 
            :is-link="true"
            @click="goToHelp"
          >
            <template #icon>
              <u-icon name="question-circle" size="20" color="#00B894"></u-icon>
            </template>
          </u-cell>
          <u-cell 
            title="Export Data" 
            :is-link="true"
            @click="handleExportData"
          >
            <template #icon>
              <u-icon name="download" size="20" color="#7F8C8D"></u-icon>
            </template>
          </u-cell>
        </u-cell-group>
      </u-card>
      
      <!-- 危险操作 -->
      <u-card 
        :show-head="false" 
        :border="false" 
        box-shadow="0 4rpx 12rpx rgba(255, 107, 107, 0.1)"
        margin="0 0 32rpx 0"
      >
        <u-cell-group :border="false">
          <u-cell 
            title="Sign Out" 
            :is-link="true"
            @click="handleSignOut"
          >
            <template #icon>
              <u-icon name="logout" size="20" color="#E17055"></u-icon>
            </template>
          </u-cell>
          <u-cell 
            title="Delete Account" 
            :is-link="true"
            @click="handleDeleteAccount"
          >
            <template #icon>
              <u-icon name="trash" size="20" color="#E17055"></u-icon>
            </template>
          </u-cell>
        </u-cell-group>
      </u-card>
    </view>
    

    

  </view>
</template>

<script>
import { ref, onMounted } from 'vue'
import { beforePageLoad } from '@/utils/router-guard'
import { useAuthStore } from '@/store/modules/auth'
import { useUserStore } from '@/store/modules/user'

export default {
  name: 'ProfilePage',
  setup() {
    // Store
    const authStore = useAuthStore()
    const userStore = useUserStore()
    
    // 按钮样式
    const editButtonStyle = {
      background: 'linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%)',
      border: 'none',
      fontSize: '28rpx'
    }
    
    // 方法
    const goToSettings = () => {
      uni.navigateTo({
        url: '/pages/profile/settings'
      })
    }
    
    const goToEditProfile = () => {
      uni.navigateTo({
        url: '/pages/profile/edit'
      })
    }
    
    const goToInsights = () => {
      uni.navigateTo({
        url: '/pages/profile/insights'
      })
    }
    
    const goToActivity = () => {
      uni.navigateTo({
        url: '/pages/profile/activity'
      })
    }
    
    const goToFollowing = () => {
      uni.navigateTo({
        url: '/pages/profile/following'
      })
    }
    
    const goToPrivacy = () => {
      uni.navigateTo({
        url: '/pages/profile/privacy'
      })
    }
    
    const goToHelp = () => {
      uni.navigateTo({
        url: '/pages/profile/help'
      })
    }
    
    const handleExportData = () => {
      uni.showModal({
        title: 'Export Data',
        content: 'Your data will be prepared and sent to your email address.',
        confirmText: 'Export',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: 'Export started',
              icon: 'success'
            })
          }
        }
      })
    }
    
    const handleSignOut = () => {
      uni.showModal({
        title: 'Sign Out',
        content: 'Are you sure you want to sign out?',
        confirmText: 'Sign Out',
        confirmColor: '#E17055',
        success: (res) => {
          if (res.confirm) {
            // 调用store的登出方法
            authStore.logout()
          }
        }
      })
    }
    
    const handleDeleteAccount = () => {
      uni.showModal({
        title: 'Delete Account',
        content: 'This action cannot be undone. All your data will be permanently deleted.',
        confirmText: 'Delete',
        confirmColor: '#E17055',
        success: (res) => {
          if (res.confirm) {
            uni.showModal({
              title: 'Final Confirmation',
              content: 'Type "DELETE" to confirm account deletion',
              editable: true,
              success: (confirmRes) => {
                if (confirmRes.confirm && confirmRes.content === 'DELETE') {
                  uni.showToast({
                    title: 'Account deleted',
                    icon: 'success'
                  })
                  setTimeout(() => {
                    // 清除用户数据并跳转
                    authStore.clearTokens()
                    userStore.clearUserProfile()
                    uni.reLaunch({
                      url: '/pages/auth/login'
                    })
                  }, 1500)
                }
              }
            })
          }
        }
      })
    }
    
    // 生命周期
    onMounted(() => {
      // 应用路由守卫
      beforePageLoad()
    })
    
    return {
      // 样式
      editButtonStyle,
      
      // 方法
      goToSettings,
      goToEditProfile,
      goToInsights,
      goToActivity,
      goToFollowing,
      goToPrivacy,
      goToHelp,
      handleExportData,
      handleSignOut,
      handleDeleteAccount
    }
  }
}
</script>

<style lang="scss" scoped>
.profile-container {
  width: 100%;
  height: 100%;
  background: #FFF5F5;
  box-sizing: border-box;
  padding-bottom: 0;
  overflow-x: hidden;
}

.nav-header {
  background: #FFFFFF;
  padding: 40rpx 64rpx 32rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 4rpx 16rpx rgba(255, 107, 107, 0.1);
  
  .nav-title {
    font-size: 48rpx; /* 页面标题：48-64rpx */
    font-weight: 700;
    color: #2C3E50;
  }
}

.user-info-section {
  padding: 48rpx 64rpx 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 32rpx;
  padding: 40rpx;
  
  .user-details {
    flex: 1;
  }
  
  .user-name {
    font-size: 48rpx; /* 页面标题：48-64rpx */
    font-weight: 700;
    color: #2C3E50;
    margin-bottom: 16rpx;
  }
  
  .user-status {
    margin-bottom: 16rpx;
  }
  
  .user-location {
    display: flex;
    align-items: center;
    gap: 8rpx;
  }
  
  .location-text {
    font-size: 28rpx; /* 正文内容：28-32rpx */
    color: #7F8C8D;
  }
}

.menu-section {
  padding: 0 64rpx;
}

/* 安全区域适配 */
.nav-header {
  padding-top: calc(40rpx + constant(safe-area-inset-top));
  padding-top: calc(40rpx + env(safe-area-inset-top));
}

/* 响应式适配 - iPhone 6/7/8（375px宽度，750rpx） */
@media screen and (max-width: 750rpx) {
  .nav-header {
    padding: 32rpx 48rpx 24rpx;
  }
  
  .user-info-section {
    padding: 40rpx 48rpx 0;
  }
  
  .menu-section {
    padding: 0 48rpx;
  }
}
</style>