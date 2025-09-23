<template>
  <view class="scroll-test-container">
    <view class="header">
      <u-navbar
        title="Scroll Test"
        border="none"
        bg-color="linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%)"
        title-color="#FFFFFF"
        :safe-area-inset-top="true"
        left-icon="arrow-left"
        @left-click="goBack"
      ></u-navbar>
    </view>
    
    <view class="content">
      <view class="test-section">
        <u-card
          v-for="i in 20"
          :key="i"
          :show-head="false"
          border="none"
          :box-shadow="true"
          margin="0 0 16px 0"
        >
          <view class="test-item">
            <view class="item-title">Test Item {{ i }}</view>
            <view class="item-content">
              This is test content for item {{ i }}. This page is designed to test scrolling functionality.
              The content should be scrollable when it exceeds the viewport height.
            </view>
          </view>
        </u-card>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'ScrollTestPage',
  setup() {
    const goBack = () => {
      uni.navigateBack()
    }
    
    return {
      goBack
    }
  }
}
</script>

<style lang="scss" scoped>
.scroll-test-container {
  width: 100vw;
  min-height: 100vh;
  background: linear-gradient(135deg, #FFF5F5 0%, #FEFEFE 100%);
  box-sizing: border-box;
}

.content {
  padding: 20px;
  padding-bottom: 100px; /* 为底部预留空间 */
}

.test-item {
  padding: 16px;
  
  .item-title {
    font-size: 16px;
    font-weight: 600;
    color: #2C3E50;
    margin-bottom: 8px;
  }
  
  .item-content {
    font-size: 14px;
    color: #7F8C8D;
    line-height: 1.5;
  }
}
</style>