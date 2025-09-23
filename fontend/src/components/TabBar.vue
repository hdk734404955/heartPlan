<template>
  <view class="tabbar-container">
    <u-tabbar
      :list="tabbarList"
      :current="currentTab"
      active-color="#FF6B6B"
      inactive-color="#7F8C8D"
      bg-color="#FFFFFF"
      :safe-area-inset-bottom="true"
      :border="false"
      :placeholder="false"
      @change="handleTabChange"
    >
      <template #default="{ item, index }">
        <view 
          class="tabbar-item"
          :class="{ 'tabbar-item-active': currentTab === index }"
        >
          <view class="icon-wrapper">
            <u-icon
              :name="currentTab === index ? item.activeIcon : item.icon"
              :size="24"
              :color="currentTab === index ? '#FF6B6B' : '#7F8C8D'"
              :custom-style="getIconStyle(index)"
            ></u-icon>
            <!-- 活跃指示器 -->
            <view 
              v-if="currentTab === index" 
              class="active-indicator"
            ></view>
          </view>
          <view 
            class="tabbar-text"
            :style="{ color: currentTab === index ? '#FF6B6B' : '#7F8C8D' }"
          >
            {{ item.text }}
          </view>
        </view>
      </template>
    </u-tabbar>
  </view>
</template>

<script>
import { ref, computed, onMounted } from 'vue'

export default {
  name: 'TabBar',
  setup() {
    // 响应式数据
    const currentTab = ref(0)
    
    // 底部导航配置 - 使用英文标签，配置温暖橙粉色系主题
    const tabbarList = ref([
      {
        pagePath: '/pages/main/index',
        icon: 'grid',
        activeIcon: 'grid-fill',
        text: 'AI Templates'
      },
      {
        pagePath: '/pages/community/index',
        icon: 'account-circle',
        activeIcon: 'account-circle-fill',
        text: 'Community'
      },
      {
        pagePath: '/pages/chat/index',
        icon: 'chat',
        activeIcon: 'chat-fill',
        text: 'Chat'
      },
      {
        pagePath: '/pages/profile/index',
        icon: 'account',
        activeIcon: 'account-fill',
        text: 'Profile'
      }
    ])
    
    // 方法
    const handleTabChange = (index) => {
      if (currentTab.value === index) return
      
      // 添加切换动画效果
      const oldTab = currentTab.value
      currentTab.value = index
      const targetPage = tabbarList.value[index].pagePath
      
      // 使用uView的过渡动画
      uni.switchTab({
        url: targetPage,
        success: () => {
          // 切换成功后的反馈
          uni.vibrateShort({
            type: 'light'
          })
        },
        fail: (err) => {
          console.error('Tab switch failed:', err)
          // 恢复原来的tab状态
          currentTab.value = oldTab
          
          // 如果switchTab失败，尝试使用reLaunch
          uni.reLaunch({
            url: targetPage,
            success: () => {
              currentTab.value = index
            }
          })
        }
      })
    }
    
    // 获取图标样式 - 活跃状态添加缩放动画
    const getIconStyle = (index) => {
      return {
        transform: currentTab.value === index ? 'scale(1.1)' : 'scale(1)',
        transition: 'all 0.3s ease'
      }
    }
    
    // 获取当前页面路径并设置对应的tab
    const getCurrentTab = () => {
      const pages = getCurrentPages()
      if (pages.length > 0) {
        const currentPage = pages[pages.length - 1]
        const currentRoute = '/' + currentPage.route
        
        const tabIndex = tabbarList.value.findIndex(item => item.pagePath === currentRoute)
        if (tabIndex !== -1) {
          currentTab.value = tabIndex
        }
      }
    }
    
    // 生命周期
    onMounted(() => {
      getCurrentTab()
    })
    
    return {
      // 响应式数据
      currentTab,
      tabbarList,
      
      // 方法
      handleTabChange,
      getIconStyle
    }
  }
}
</script>

<style lang="scss" scoped>
.tabbar-container {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: #FFFFFF;
  box-shadow: 0 -2px 12px rgba(255, 107, 107, 0.15);
  border-top: 1px solid rgba(255, 107, 107, 0.1);
  height: 120rpx;
  box-sizing: border-box;
  
  /* 确保TabBar不影响页面滚动 */
  &::before {
    content: '';
    position: absolute;
    top: -40rpx;
    left: 0;
    right: 0;
    height: 40rpx;
    background: linear-gradient(to top, rgba(255, 255, 255, 0.8), transparent);
    pointer-events: none;
  }
}

.tabbar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 16rpx 0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  
  &:active {
    transform: scale(0.95);
  }
  
  &.tabbar-item-active {
    .icon-wrapper {
      transform: translateY(-4rpx);
    }
  }
}

.icon-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.active-indicator {
  position: absolute;
  bottom: -12rpx;
  left: 50%;
  transform: translateX(-50%);
  width: 8rpx;
  height: 8rpx;
  background: linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%);
  border-radius: 50%;
  animation: indicatorPulse 1.5s ease-in-out infinite;
}

.tabbar-text {
  font-size: 22rpx; /* 辅助文字：24-28rpx */
  font-weight: 500;
  margin-top: 8rpx;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  text-align: center;
  line-height: 1.2;
}

/* 活跃指示器动画 */
@keyframes indicatorPulse {
  0%, 100% {
    opacity: 1;
    transform: translateX(-50%) scale(1);
  }
  50% {
    opacity: 0.7;
    transform: translateX(-50%) scale(1.2);
  }
}

/* Tab切换时的图标缩放动画 */
.tabbar-item-active .icon-wrapper {
  animation: iconBounce 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

@keyframes iconBounce {
  0% {
    transform: translateY(-4rpx) scale(1);
  }
  50% {
    transform: translateY(-8rpx) scale(1.15);
  }
  100% {
    transform: translateY(-4rpx) scale(1.1);
  }
}

/* 安全区域适配 */
.tabbar-container {
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}

/* 响应式适配 - iPhone 6/7/8（375px宽度，750rpx） */
@media screen and (max-width: 375px) {
  .tabbar-text {
    font-size: 20rpx; /* 辅助文字：24-28rpx */
  }
  
  .tabbar-item {
    padding: 12rpx 0;
  }
}
</style>