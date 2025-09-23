<template>
  <view class="global-loading">
    <!-- 渐变背景 -->
    <view class="loading-bg"></view>
    
    <!-- 动画元素 -->
    <view class="loading-content">
      <!-- 心形图标动画 -->
      <view class="heart-icon">💖</view>
      
      <!-- 波纹效果 -->
      <view class="ripple-container">
        <view class="ripple ripple-1"></view>
        <view class="ripple ripple-2"></view>
        <view class="ripple ripple-3"></view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'GlobalLoading'
}
</script>

<style lang="scss" scoped>
.global-loading {
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  right: 0 !important;
  bottom: 0 !important;
  width: 100vw !important;
  height: 100vh !important;
  z-index: 2147483647 !important;
  pointer-events: auto !important;
  transform: translateZ(0) !important;
  will-change: transform !important;
  isolation: isolate !important;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.loading-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 50%, #FFB3BA 100%);
  opacity: 0;
  animation: fadeInBg 0.3s ease-out forwards;
}

.loading-content {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transform: scale(0.8);
  animation: fadeInContent 0.4s ease-out 0.1s forwards;
}

.heart-icon {
  font-size: 120rpx;
  margin-bottom: 32rpx;
  animation: heartBeat 1.5s ease-in-out infinite;
}

.ripple-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 200rpx;
  height: 200rpx;
}

.ripple {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border-radius: 50%;
  border: 2px solid rgba(255, 255, 255, 0.3);
  animation: rippleEffect 2s ease-out infinite;
}

.ripple-1 {
  animation-delay: 0s;
}

.ripple-2 {
  animation-delay: 0.5s;
}

.ripple-3 {
  animation-delay: 1s;
}

/* 动画定义 */
@keyframes fadeInBg {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes fadeInContent {
  from {
    opacity: 0;
    transform: scale(0.8) translateY(40rpx);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

@keyframes heartBeat {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

@keyframes rippleEffect {
  0% {
    width: 0;
    height: 0;
    opacity: 1;
  }
  100% {
    width: 200rpx;
    height: 200rpx;
    opacity: 0;
  }
}

/* 响应式适配 */
@media screen and (max-width: 375px) {
  .heart-icon {
    font-size: 100rpx;
    margin-bottom: 24rpx;
  }
  
  .ripple-container {
    width: 160rpx;
    height: 160rpx;
  }
}
</style>