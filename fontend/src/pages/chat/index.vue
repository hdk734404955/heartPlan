<template>
  <view class="chat-container">
    <!-- 顶部导航 -->
    <view class="nav-header">
      <view class="nav-title">Chat</view>
      <u-icon name="plus-circle" size="24" color="#2C3E50" @click="handleNewChat"></u-icon>
    </view>
    
    <!-- 聊天列表 -->
    <view class="chat-list">
      <!-- AI教练 - 置顶显示 -->
      <u-card 
        :show-head="false" 
        border="none" 
        :box-shadow="true"
        margin="0 0 16px 0"
        @click="goToAICoach"
      >
        <view class="chat-item ai-coach">
          <u-avatar
            src=""
            size="48"
            shape="circle"
            bg-color="#FF6B6B"
            icon="robot"
            icon-color="#FFFFFF"
          ></u-avatar>
          <view class="chat-info">
            <view class="chat-header">
              <view class="chat-name">AI Love Coach</view>
              <u-tag 
                text="AI" 
                bg-color="#FF6B6B" 
                color="#FFFFFF" 
                size="mini"
                shape="circle"
              ></u-tag>
            </view>
            <view class="chat-preview">Your personal relationship advisor</view>
            <view class="chat-time">Always available</view>
          </view>
          <view class="chat-status">
            <u-badge :count="0" :show-zero="false"></u-badge>
            <u-icon name="arrow-right" size="16" color="#7F8C8D"></u-icon>
          </view>
        </view>
      </u-card>
      
      <!-- 私聊列表 -->
      <view class="private-chats">
        <view class="section-title">Private Messages</view>
        
        <!-- 空状态 -->
        <u-empty 
          mode="message" 
          icon="chat"
          text="No conversations yet"
          text-color="#7F8C8D"
          icon-color="#FF6B6B"
        >
          <template #bottom>
            <u-button
              type="primary"
              size="small"
              :custom-style="buttonStyle"
              @click="goToCommunity"
            >
              Find People in Community
            </u-button>
          </template>
        </u-empty>
      </view>
    </view>
    
    <!-- 底部导航 -->
    <TabBar />
  </view>
</template>

<script>
import { ref, onMounted } from 'vue'
import { beforePageLoad } from '@/utils/router-guard'
import TabBar from '@/components/TabBar.vue'

export default {
  name: 'ChatPage',
  components: {
    TabBar
  },
  setup() {
    // 按钮样式
    const buttonStyle = {
      background: 'linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%)',
      border: 'none'
    }
    
    // 方法
    const handleNewChat = () => {
      uni.showActionSheet({
        itemList: ['Start New Chat', 'Scan QR Code'],
        success: (res) => {
          if (res.tapIndex === 0) {
            uni.navigateTo({
              url: '/pages/chat/new'
            })
          } else if (res.tapIndex === 1) {
            uni.scanCode({
              success: (scanRes) => {
                console.log('Scan result:', scanRes)
              }
            })
          }
        }
      })
    }
    
    const goToAICoach = () => {
      uni.navigateTo({
        url: '/pages/chat/ai-coach'
      })
    }
    
    const goToCommunity = () => {
      uni.switchTab({
        url: '/pages/community/index'
      })
    }
    
    // 生命周期
    onMounted(() => {
      // 应用路由守卫
      beforePageLoad()
    })
    
    return {
      // 样式
      buttonStyle,
      
      // 方法
      handleNewChat,
      goToAICoach,
      goToCommunity
    }
  }
}
</script>

<style lang="scss" scoped>
.chat-container {
  width: 100vw;
  min-height: 100vh;
  background: #FFF5F5;
  padding-bottom: 240rpx; /* 为底部导航栏预留空间 */
  box-sizing: border-box;
  overflow-x: hidden;
}

.nav-header {
  background: #FFFFFF;
  padding: 40rpx 64rpx 32rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(255, 107, 107, 0.1);
  
  .nav-title {
    font-size: 48rpx; /* 页面标题：48-64rpx */
    font-weight: 700;
    color: #2C3E50;
  }
}

.chat-list {
  padding: 48rpx 64rpx;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 32rpx;
  gap: 24rpx;
  
  &.ai-coach {
    background: linear-gradient(135deg, rgba(255, 107, 107, 0.05) 0%, rgba(255, 142, 142, 0.05) 100%);
    border-radius: 24rpx;
  }
  
  .chat-info {
    flex: 1;
  }
  
  .chat-header {
    display: flex;
    align-items: center;
    gap: 16rpx;
    margin-bottom: 8rpx;
  }
  
  .chat-name {
    font-size: 32rpx; /* 正文内容：28-32rpx */
    font-weight: 600;
    color: #2C3E50;
  }
  
  .chat-preview {
    font-size: 28rpx; /* 正文内容：28-32rpx */
    color: #7F8C8D;
    margin-bottom: 8rpx;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .chat-time {
    font-size: 24rpx; /* 辅助文字：24-28rpx */
    color: #B2B2B2;
  }
  
  .chat-status {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 16rpx;
  }
}

.private-chats {
  margin-top: 64rpx;
  
  .section-title {
    font-size: 36rpx; /* 正文内容：28-32rpx */
    font-weight: 600;
    color: #2C3E50;
    margin-bottom: 32rpx;
  }
}

/* 安全区域适配 */
.nav-header {
  padding-top: calc(40rpx + constant(safe-area-inset-top));
  padding-top: calc(40rpx + env(safe-area-inset-top));
}

/* 响应式适配 - iPhone 6/7/8（375px宽度，750rpx） */
@media screen and (max-width: 375px) {
  .nav-header {
    padding: 32rpx 48rpx 24rpx;
  }
  
  .chat-list {
    padding: 40rpx 48rpx;
  }
}
</style>