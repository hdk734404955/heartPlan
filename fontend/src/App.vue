<script>
import { useAuthStore } from '@/store/modules/auth'
import { useUserStore } from '@/store/modules/user'
import { autoLoginCheck } from '@/utils/router-guard'

export default {
  onLaunch: async function () {
    console.log('HeartPlan App Launch')
    
    try {
      // 执行自动登录检查
      const isLoggedIn = await autoLoginCheck()
      
      console.log('Auto login check result:', isLoggedIn)
      
      // 初始化状态管理
      const authStore = useAuthStore()
      const userStore = useUserStore()
      
      console.log('Auth initialized:', authStore.isAuthenticated)
      console.log('User profile loaded:', userStore.hasCompleteProfile)
    } catch (error) {
      console.error('App launch error:', error)
    }
  },
  onShow: function () {
    console.log('HeartPlan App Show')
  },
  onHide: function () {
    console.log('HeartPlan App Hide')
  },
}
</script>

<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "uview-plus/index.scss";
* {
  box-sizing: border-box;
}
/* HeartPlan全局样式 */
/* 设置全局字体 */
page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
  background-color: #FFF5F5;
  color: #2C3E50;
}

/* 全局容器样式 */
.container {
  padding: 32rpx;
  min-height: 100vh;
}

/* 全局页面标题样式 - 响应式字体大小 */
.page-title {
  font-size: 56rpx;
  font-weight: 600;
  color: #2C3E50;
  text-align: center;
  margin-bottom: 48rpx;
}

/* 全局副标题样式 - 响应式字体大小 */
.page-subtitle {
  font-size: 30rpx;
  color: #7F8C8D;
  text-align: center;
  margin-bottom: 64rpx;
  line-height: 1.6;
}

/* 全局按钮样式 */
.btn-primary {
  background: linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%) !important;
  border: none !important;
  border-radius: 48rpx !important;
  color: #FEFEFE !important;
  font-weight: 500 !important;
  font-size: 32rpx !important;
  height: 88rpx !important;
  line-height: 88rpx !important;
  text-align: center !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
}

.btn-secondary {
  background: #FEFEFE !important;
  border: 2rpx solid #FF6B6B !important;
  border-radius: 48rpx !important;
  color: #FF6B6B !important;
  font-weight: 500 !important;
  font-size: 32rpx !important;
  height: 88rpx !important;
  line-height: 88rpx !important;
  text-align: center !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
}

/* 全局卡片样式 - 使用px单位的阴影 */
.card {
  background: #FEFEFE;
  border-radius: 24rpx;
  box-shadow: 0 4px 16px 0 rgba(255, 107, 107, 0.15);
  padding: 48rpx 32rpx;
  margin-bottom: 32rpx;
}

/* 全局输入框样式 - 响应式字体大小 */
.input-wrapper {
  margin-bottom: 32rpx;
}

.input-label {
  font-size: 30rpx;
  color: #2C3E50;
  margin-bottom: 16rpx;
  font-weight: 500;
}

/* 全局文本样式 */
.text-primary {
  color: #FF6B6B;
}

.text-secondary {
  color: #7F8C8D;
}

.text-success {
  color: #00B894;
}

.text-warm {
  color: #FFAAA5;
}

/* 全局间距样式 */
.mt-16 { margin-top: 16rpx; }
.mt-32 { margin-top: 32rpx; }
.mt-48 { margin-top: 48rpx; }
.mt-64 { margin-top: 64rpx; }

.mb-16 { margin-bottom: 16rpx; }
.mb-32 { margin-bottom: 32rpx; }
.mb-48 { margin-bottom: 48rpx; }
.mb-64 { margin-bottom: 64rpx; }

.p-16 { padding: 16rpx; }
.p-32 { padding: 32rpx; }
.p-48 { padding: 48rpx; }

/* 全局布局样式 */
.flex-center {
  display: flex;
  justify-content: center;
  align-items: center;
}

.flex-between {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.flex-column {
  display: flex;
  flex-direction: column;
}

/* 全局动画样式 */
.fade-in {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* uView输入框聚焦动画效果 - 使用px单位的阴影 */
:deep(.u-input) {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

:deep(.u-input--focus) {
  transform: translateY(-4rpx);
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.2);
}

:deep(.u-input__input) {
  transition: all 0.3s ease;
}

:deep(.u-textarea) {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

:deep(.u-textarea--focus) {
  transform: translateY(-4rpx);
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.2);
}

/* uView表单项动画效果 */
:deep(.u-form-item) {
  transition: all 0.3s ease;
}

:deep(.u-form-item__label) {
  transition: all 0.3s ease;
}

/* 页面切换动画 */
.page-enter-active,
.page-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.page-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

.page-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}

/* 卡片悬停动画效果 */
:deep(.u-card) {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

:deep(.u-card:hover) {
  transform: translateY(-4rpx);
  box-shadow: 0 8px 25px rgba(255, 107, 107, 0.15);
}

/* 按钮点击波纹效果 */
:deep(.u-button) {
  position: relative;
  overflow: hidden;
}

:deep(.u-button::before) {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  transform: translate(-50%, -50%);
  transition: width 0.6s, height 0.6s;
}

:deep(.u-button:active::before) {
  width: 600rpx;
  height: 600rpx;
}

/* 温暖渐变背景 */
.gradient-bg {
  background: linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%);
}

.gradient-secondary-bg {
  background: linear-gradient(135deg, #FFB3BA 0%, #FF9AA2 100%);
}

/* uView按钮组件样式修复 - 确保按钮文案居中显示 */
:deep(.u-button) {
  text-align: center !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  position: relative !important;
}

:deep(.u-button__text) {
  text-align: center !important;
  width: 100% !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  line-height: 1 !important;
}

:deep(.u-button__loading-text) {
  text-align: center !important;
  width: 100% !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
}

/* 修复uView按钮内部文本居中问题 */
:deep(.u-button .u-text) {
  text-align: center !important;
  width: 100% !important;
  display: block !important;
}

/* 确保按钮在不同屏幕尺寸下都能正确居中 */
@media screen and (max-width: 768px) {
  :deep(.u-button) {
    text-align: center !important;
    justify-content: center !important;
  }
  
  :deep(.u-button__text) {
    text-align: center !important;
    justify-content: center !important;
  }
}

/* 修复页面滚动条问题 - 响应式适配 */
page {
  height: 100%;
  overflow-x: hidden;
}

.page-container {
  min-height: 100vh;
  box-sizing: border-box;
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}

/* 修复底部导航栏占用空间问题 - 响应式间距 */
.main-content {
  padding-bottom: 140rpx; /* 为底部导航栏预留空间 */
}

/* 响应式字体大小规范 */
.text-title {
  font-size: 56rpx; /* 页面标题：48-64rpx */
}

.text-subtitle {
  font-size: 48rpx; /* 副标题：48-64rpx */
}

.text-body {
  font-size: 30rpx; /* 正文内容：28-32rpx */
}

.text-caption {
  font-size: 26rpx; /* 辅助文字：24-28rpx */
}

.text-button {
  font-size: 32rpx; /* 按钮文字：32rpx */
}

/* 安全区域适配 */
.safe-area-inset-bottom {
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}

.safe-area-inset-top {
  padding-top: constant(safe-area-inset-top);
  padding-top: env(safe-area-inset-top);
}
</style>
