<template>
  <view class="community-container">
    <!-- 顶部导航 -->
    <view class="nav-header">
      <view class="nav-title">Community</view>
      <u-icon name="search" size="24" color="#2C3E50" @click="handleSearch"></u-icon>
    </view>
    
    <!-- Tab导航 -->
    <view class="tab-section">
      <u-tabs
        :list="tabList"
        :current="currentTab"
        active-color="#FF6B6B"
        inactive-color="#7F8C8D"
        :scroll="false"
        @change="handleTabChange"
      ></u-tabs>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-area">
      <!-- 推荐Tab -->
      <view v-if="currentTab === 0" class="tab-content">
        <u-empty 
          mode="data" 
          icon="account-circle"
          text="Personalized recommendations coming soon"
          text-color="#7F8C8D"
          icon-color="#FF6B6B"
        ></u-empty>
      </view>
      
      <!-- 成功故事Tab -->
      <view v-if="currentTab === 1" class="tab-content">
        <u-empty 
          mode="data" 
          icon="heart"
          text="Success stories coming soon"
          text-color="#7F8C8D"
          icon-color="#FF6B6B"
        ></u-empty>
      </view>
      
      <!-- 问题求助Tab -->
      <view v-if="currentTab === 2" class="tab-content">
        <u-empty 
          mode="data" 
          icon="chat"
          text="Help forum coming soon"
          text-color="#7F8C8D"
          icon-color="#FF6B6B"
        ></u-empty>
      </view>
      
      <!-- 每周话题Tab -->
      <view v-if="currentTab === 3" class="tab-content">
        <u-empty 
          mode="data" 
          icon="calendar"
          text="Weekly topics coming soon"
          text-color="#7F8C8D"
          icon-color="#FF6B6B"
        ></u-empty>
      </view>
    </view>
    
    <!-- 浮动发布按钮 -->
    <view class="fab-button">
      <u-button
        type="primary"
        shape="circle"
        size="large"
        :custom-style="fabStyle"
        @click="handleCreatePost"
      >
        <u-icon name="plus" size="24" color="#FFFFFF"></u-icon>
      </u-button>
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
  name: 'CommunityPage',
  components: {
    TabBar
  },
  setup() {
    // 响应式数据
    const currentTab = ref(0)
    
    // Tab配置 - 使用英文标签
    const tabList = ref([
      { name: 'Recommended' },
      { name: 'Success Stories' },
      { name: 'Help Forum' },
      { name: 'Weekly Topics' }
    ])
    
    // 浮动按钮样式
    const fabStyle = {
      background: 'linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%)',
      border: 'none',
      boxShadow: '0 4px 12px rgba(255, 107, 107, 0.3)'
    }
    
    // 方法
    const handleTabChange = (index) => {
      currentTab.value = index
    }
    
    const handleSearch = () => {
      uni.navigateTo({
        url: '/pages/community/search'
      })
    }
    
    const handleCreatePost = () => {
      uni.navigateTo({
        url: '/pages/community/create'
      })
    }
    
    // 生命周期
    onMounted(() => {
      // 应用路由守卫
      beforePageLoad()
    })
    
    return {
      // 响应式数据
      currentTab,
      tabList,
      fabStyle,
      
      // 方法
      handleTabChange,
      handleSearch,
      handleCreatePost
    }
  }
}
</script>

<style lang="scss" scoped>
.community-container {
  width: 100vw;
  min-height: 100vh;
  background: #FFF5F5;
  padding-bottom: 120px; /* 为底部导航栏预留空间 */
  box-sizing: border-box;
  overflow-x: hidden;
}

.nav-header {
  background: #FFFFFF;
  padding: 20px 32px 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(255, 107, 107, 0.1);
  
  .nav-title {
    font-size: 20px;
    font-weight: 700;
    color: #2C3E50;
  }
}

.tab-section {
  background: #FFFFFF;
  padding: 0 32px;
  border-bottom: 1px solid #F0F0F0;
}

.content-area {
  flex: 1;
  padding: 24px 32px;
}

.tab-content {
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.fab-button {
  position: fixed;
  bottom: 120px;
  right: 32px;
  z-index: 999;
}

/* 安全区域适配 */
.nav-header {
  padding-top: calc(20px + constant(safe-area-inset-top));
  padding-top: calc(20px + env(safe-area-inset-top));
}

/* 响应式适配 */
@media screen and (max-width: 375px) {
  .nav-header {
    padding: 16px 24px 12px;
  }
  
  .tab-section {
    padding: 0 24px;
  }
  
  .content-area {
    padding: 20px 24px;
  }
  
  .fab-button {
    right: 24px;
  }
}
</style>