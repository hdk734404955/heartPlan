<template>
  <view class="profile-container" @scroll="handleScroll">
    <view class="tab-box" :style="backgroundStyle">
      <u-avatar
        :src="userProfile.avatar"
        size="25"
        shape="circle"
        bg-color="#FF6B6B"
        @error="handleAvatarError"
      ></u-avatar>
    </view>
    <view class="background-section" :style="backgroundStyle">
      <view class="user-info">
        <!-- 用户头像 -->
        <u-avatar
          :src="userProfile.avatar"
          size="120"
          shape="circle"
          bg-color="#FF6B6B"
          @error="handleAvatarError"
        ></u-avatar>

        <!-- 用户信息 -->
        <view class="user-details">
          <view class="user-name">{{ userProfile.displayName }}</view>
          <view class="user-bio">{{ userProfile.bio }}</view>
          <view class="user-location">
            <u-icon name="map-pin" size="16" color="#FFFFFF"></u-icon>
            <text class="location-text">{{ userProfile.location }}</text>
          </view>
        </view>

        <!-- 操作按钮 -->
        <view class="action-buttons">
          <u-button
            type="primary"
            size="small"
            :custom-style="editButtonStyle"
            @click="goToEditProfile"
          >
            Edit Profile
          </u-button>
          <u-button
            type="primary"
            size="small"
            :custom-style="settingsButtonStyle"
            @click="goToSettings"
          >
            <u-icon
              name="setting"
              size="16"
              color="#FF6B6B"
              style="margin-right: 8rpx"
            ></u-icon>
            Settings
          </u-button>
        </view>
      </view>
    </view>

    <!-- 内容区域 - 白色背景，圆角设计，重叠效果 -->
    <scroll-view
      class="content-section"
      id="contentBox"
      scroll-y="true"
      :scroll-with-animation="true"
      :enable-back-to-top="true"
      :refresher-enabled="false"
      @scrolltoupper="handleScrollToUpper"
      @scrolltolower="handleScrollToLower"
    >
      <!-- 标签页导航 -->
      <u-tabs
        :list="tabList"
        v-model="activeTab"
        :active-color="'#FF6B6B'"
        line-color="#FF6B6B"
        :duration="300"
        @change="handleTabChange"
      ></u-tabs>

      <!-- 内容列表区域 - 添加过渡动画 -->
      <view class="content-list">
        <u-transition :show="showContent" mode="fade" :duration="300">
          <!-- Posts标签页内容 -->
          <view v-if="activeTab === 0" class="tab-content">
            <view class="empty-state">
              <u-icon name="edit-pen" size="80" color="#FFB3BA"></u-icon>
              <view class="empty-title">No Posts Yet</view>
              <view class="empty-subtitle"
                >Share your thoughts and moments with the community</view
              >
              <u-button
                type="primary"
                :custom-style="createButtonStyle"
                @click="handleCreateContent"
              >
                Create Post
              </u-button>
            </view>
          </view>

          <!-- Collections标签页内容 -->
          <view v-else-if="activeTab === 1" class="tab-content">
            <view class="empty-state">
              <u-icon name="heart" size="80" color="#FFB3BA"></u-icon>
              <view class="empty-title">No Collections Yet</view>
              <view class="empty-subtitle"
                >Save your favorite posts and create collections</view
              >
              <u-button
                type="primary"
                :custom-style="createButtonStyle"
                @click="handleCreateContent"
              >
                Browse Posts
              </u-button>
            </view>
          </view>

          <!-- Liked标签页内容 -->
          <view v-else-if="activeTab === 2" class="tab-content">
            <view class="empty-state">
              <u-icon name="thumb-up" size="80" color="#FFB3BA"></u-icon>
              <view class="empty-title">No Liked Posts Yet</view>
              <view class="empty-subtitle"
                >Like posts you enjoy to see them here</view
              >
              <u-button
                type="primary"
                :custom-style="createButtonStyle"
                @click="handleCreateContent"
              >
                Explore Posts
              </u-button>
            </view>
          </view>
        </u-transition>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, getCurrentInstance } from "vue";

// 定义组件名称（可选，用于调试）
defineOptions({
  name: "ProfilePage",
});

const instance = getCurrentInstance();
// 静态用户数据配置
const userProfile = ref({
  id: "user_001",
  username: "johndoe",
  displayName: "John Doe",
  avatar: "https://s.utui.cc/u/2025/09/29/rx6yrzOF.jpg",
  bio: "Passionate about life and sharing moments",
  location: "New York, NY",
  followersCount: 1234,
  followingCount: 567,
  postsCount: 89,
});

// 背景颜色配置
const backgroundConfig = ref({
  primaryColor: "#FF6B6B",
  secondaryColor: "#FF8E8E",
  gradientDirection: "135deg",
  opacity: 1.0,
});

// 温暖色系主题配置
const themeColors = {
  primary: "#FF6B6B",
  secondary: "#FF8E8E",
  accent: "#FFB3BA",
  success: "#00B894",
  warning: "#FFAAA5",
  neutral: "#7F8C8D",
};

// 标签页配置
const tabList = ref([
  { name: "Posts", key: "posts" },
  { name: "Collections", key: "collections" },
  { name: "Liked", key: "liked" },
]);
const contentRef = ref(null);
// 当前激活的标签页
const activeTab = ref(0);

// 内容显示状态 - 用于过渡动画
const showContent = ref(true);

// 滚动状态管理 - 用于优化滚动性能
const scrollState = ref({
  scrollY: 0,
  isScrolling: false,
  lastScrollTime: 0,
});

// 页面性能监控和优化
const performanceMetrics = ref({
  loadStartTime: 0,
  loadEndTime: 0,
  renderTime: 0,
  isOptimized: false,
});

// 计算背景样式
const backgroundStyle = computed(() => {
  const config = backgroundConfig.value;
  return {
    background: `linear-gradient(${config.gradientDirection}, ${config.primaryColor} 0%, ${config.secondaryColor} 100%)`,
    opacity: config.opacity,
  };
});

// 按钮样式配置
const editButtonStyle = {
  background: "linear-gradient(135deg, #FFFFFF 0%, #FFFFFF 100%)",
  color: "#FF6B6B",
  border: "none",
  fontSize: "28rpx",
  borderRadius: "24rpx",
  fontWeight: "600",
};

const settingsButtonStyle = {
  background: "linear-gradient(135deg, #FFFFFF 0%, #FFFFFF 100%)",
  color: "#FF6B6B",
  border: "none",
  fontSize: "28rpx",
  borderRadius: "24rpx",
  fontWeight: "600",
};

const createButtonStyle = {
  background: "linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%)",
  border: "none",
  fontSize: "32rpx",
  borderRadius: "48rpx",
  marginTop: "32rpx",
};

const tabItemStyle = {
  fontSize: "32rpx",
  fontWeight: "600",
};
// 获取当前标签页标签
const getCurrentTabLabel = () => {
  const currentTab = tabList.value[activeTab.value];
  return currentTab ? currentTab.name : "Content";
};

// 标签页切换处理 - 添加平滑过渡效果
const handleTabChange = (index) => {
  // 先隐藏内容
  showContent.value = false;

  // 延迟切换标签页，实现平滑过渡
  setTimeout(() => {
    activeTab.value = index;
    // 再显示新内容
    setTimeout(() => {
      showContent.value = true;
    }, 50);
  }, 150);
};

// 图片加载错误处理和默认头像显示
const handleAvatarError = () => {
  console.log("Avatar loading failed, switching to default avatar");

  // 设置默认头像
  userProfile.value.avatar = "/static/images/default-avatar.png";

  // 显示友好的错误提示
  uni.showToast({
    title: "Using default avatar",
    icon: "none",
    duration: 1500,
  });
};

// 通用图片加载错误处理
const handleImageError = (type = "image") => {
  console.log(`${type} loading failed`);

  const errorMessages = {
    avatar: "Avatar loading failed, using default",
    background: "Background loading failed, using gradient",
    content: "Image loading failed",
  };

  uni.showToast({
    title: errorMessages[type] || errorMessages.content,
    icon: "none",
    duration: 1500,
  });

  // 根据图片类型返回默认资源
  switch (type) {
    case "avatar":
      return "/static/images/default-avatar.png";
    case "background":
      return null; // 使用CSS渐变背景
    default:
      return "/static/images/placeholder.png";
  }
};

// 导航方法 - 增强错误处理和用户反馈
const goToEditProfile = () => {
  // 显示加载状态
  uni.showLoading({
    title: "Loading...",
    mask: true,
  });

  // 模拟检查页面是否存在
  setTimeout(() => {
    uni.hideLoading();

    uni.navigateTo({
      url: "/pages/profile/edit",
      success: () => {
        console.log("Navigate to edit profile success");
      },
      fail: (err) => {
        console.error("Navigate to edit profile failed:", err);
        uni.showToast({
          title: "Page not available yet",
          icon: "none",
          duration: 2000,
        });
      },
    });
  }, 300);
};

const goToSettings = () => {
  // 显示加载状态
  uni.showLoading({
    title: "Loading...",
    mask: true,
  });

  setTimeout(() => {
    uni.hideLoading();

    uni.navigateTo({
      url: "/pages/profile/settings",
      success: () => {
        console.log("Navigate to settings success");
      },
      fail: (err) => {
        console.error("Navigate to settings failed:", err);
        uni.showToast({
          title: "Settings page not available yet",
          icon: "none",
          duration: 2000,
        });
      },
    });
  }, 300);
};

const handleCreateContent = () => {
  const currentTab = tabList.value[activeTab.value];
  let targetUrl = "/pages/community/create";
  let loadingTitle = "Loading...";
  let errorMessage = "Page not available yet";

  // 根据当前标签页设置不同的导航目标和提示
  switch (currentTab.key) {
    case "posts":
      targetUrl = "/pages/community/create";
      loadingTitle = "Creating post...";
      errorMessage = "Create post page not available yet";
      break;
    case "collections":
      targetUrl = "/pages/community/collections";
      loadingTitle = "Loading collections...";
      errorMessage = "Collections page not available yet";
      break;
    case "liked":
      targetUrl = "/pages/community/explore";
      loadingTitle = "Exploring posts...";
      errorMessage = "Explore page not available yet";
      break;
  }

  // 显示加载状态
  uni.showLoading({
    title: loadingTitle,
    mask: true,
  });

  setTimeout(() => {
    uni.hideLoading();

    uni.navigateTo({
      url: targetUrl,
      success: () => {
        console.log(`Navigate to ${targetUrl} success`);
      },
      fail: (err) => {
        console.error(`Navigate to ${targetUrl} failed:`, err);
        uni.showToast({
          title: errorMessage,
          icon: "none",
          duration: 2000,
        });
      },
    });
  }, 300);
};

// 滚动性能优化 - 节流函数
const throttle = (func, delay) => {
  let timeoutId;
  let lastExecTime = 0;
  return function (...args) {
    const currentTime = Date.now();

    if (currentTime - lastExecTime > delay) {
      func.apply(this, args);
      lastExecTime = currentTime;
    } else {
      clearTimeout(timeoutId);
      timeoutId = setTimeout(() => {
        func.apply(this, args);
        lastExecTime = Date.now();
      }, delay - (currentTime - lastExecTime));
    }
  };
};

// 滚动事件处理 - 优化性能
const handleScroll = throttle((event) => {
  const query = uni.createSelectorQuery().in(instance);

  // 通过 ID 获取
  query
    .select("#contentBox")
    .boundingClientRect((data) => {
      console.log(data);

      console.log("top:", data.top);
    })
    .exec();

  const scrollTop = event.detail.scrollTop || 0;
  const currentTime = Date.now();

  // 更新滚动状态
  scrollState.value = {
    scrollY: scrollTop,
    isScrolling: true,
    lastScrollTime: currentTime,
  };

  // 滚动结束检测
  setTimeout(() => {
    if (currentTime === scrollState.value.lastScrollTime) {
      scrollState.value.isScrolling = false;
    }
  }, 150);
}, 16); // 60fps

// 页面加载性能优化
const optimizePagePerformance = () => {
  // 预加载关键资源
  const preloadImages = [
    "/static/images/default-avatar.png",
    "/static/images/placeholder.png",
  ];

  preloadImages.forEach((src) => {
    const img = new Image();
    img.src = src;
  });

  // 标记性能优化完成
  performanceMetrics.value.isOptimized = true;
  console.log("Page performance optimized");
};

// 滚动到顶部处理 - 增强用户体验
const handleScrollToUpper = () => {
  console.log("Scrolled to top");

  // 提供触觉反馈（如果设备支持）
  uni.vibrateShort({
    success: () => {
      console.log("Vibration feedback provided");
    },
    fail: () => {
      console.log("Vibration not supported");
    },
  });

  // 可以在这里添加下拉刷新逻辑
  // 暂时显示提示，表明功能可用
  uni.showToast({
    title: "Already at top",
    icon: "none",
    duration: 1000,
  });
};

// 滚动到底部处理 - 增强用户体验
const handleScrollToLower = () => {
  console.log("Scrolled to bottom");

  // 提供触觉反馈
  uni.vibrateShort({
    success: () => {
      console.log("Vibration feedback provided");
    },
    fail: () => {
      console.log("Vibration not supported");
    },
  });

  // 可以在这里添加加载更多逻辑
  // 暂时显示提示，表明功能可用
  uni.showToast({
    title: "No more content",
    icon: "none",
    duration: 1000,
  });
};

// 页面生命周期管理和性能监控
onMounted(() => {
  performanceMetrics.value.loadStartTime = Date.now();

  // 页面加载完成后的初始化
  console.log("Profile page mounted - responsive layout initialized");

  // 检测设备信息用于调试和适配
  uni.getSystemInfo({
    success: (res) => {
      console.log("Device info:", {
        screenWidth: res.screenWidth,
        screenHeight: res.screenHeight,
        pixelRatio: res.pixelRatio,
        safeArea: res.safeArea,
        safeAreaInsets: res.safeAreaInsets,
        platform: res.platform,
        brand: res.brand,
        model: res.model,
      });

      // 根据设备性能调整优化策略
      const isLowEndDevice = res.pixelRatio < 2 || res.screenWidth < 375;
      if (isLowEndDevice) {
        console.log(
          "Low-end device detected, applying performance optimizations"
        );
        // 可以在这里添加低端设备的性能优化
      }
    },
    fail: (err) => {
      console.error("Failed to get system info:", err);
    },
  });

  // 执行性能优化
  optimizePagePerformance();

  // 记录页面加载完成时间
  performanceMetrics.value.loadEndTime = Date.now();
  performanceMetrics.value.renderTime =
    performanceMetrics.value.loadEndTime -
    performanceMetrics.value.loadStartTime;

  console.log(`Page loaded in ${performanceMetrics.value.renderTime}ms`);

  // 页面加载完成提示
  if (performanceMetrics.value.renderTime < 1000) {
    console.log("Page loaded quickly - excellent performance");
  } else if (performanceMetrics.value.renderTime < 2000) {
    console.log("Page loaded normally - good performance");
  } else {
    console.log("Page loaded slowly - consider optimization");
  }
});

onUnmounted(() => {
  // 清理资源和事件监听器
  console.log("Profile page unmounted - cleaning up resources");

  // 清理性能监控数据
  performanceMetrics.value = {
    loadStartTime: 0,
    loadEndTime: 0,
    renderTime: 0,
    isOptimized: false,
  };
});
</script>


<style lang="scss" scoped>
* {
  box-sizing: border-box;
}
.profile-container {
  width: 100%;
  height: 100%;
  background: #fff5f5;
  box-sizing: border-box;
  overflow-x: hidden;
  position: relative;

  /* 安全区域适配 - 底部安全区域 */
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}
.tab-box {
  height: 100rpx;
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 12;
  display: flex;
  align-items: center;
  justify-content: center;
}
/* 背景区域 - 使用sticky定位实现吸附效果 */
.background-section {
  height: 600rpx;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff8e8e 100%);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); /* 更流畅的过渡动画 */
  padding: 80rpx 48rpx 48rpx;
  box-sizing: border-box;

  /* 安全区域适配 - 顶部安全区域 */
  padding-top: calc(80rpx + constant(safe-area-inset-top));
  padding-top: calc(80rpx + env(safe-area-inset-top));

  /* 硬件加速优化 */
  transform: translateZ(0);
  will-change: transform;

  /* 滚动性能优化 */
  contain: layout style paint;

  /* 图片加载优化 */
  background-attachment: local; /* 防止背景图片重绘 */

  /* 错误处理 - 渐变背景作为fallback */
  background-image: linear-gradient(135deg, #ff6b6b 0%, #ff8e8e 100%);

  /* 性能优化 - 减少重排重绘 */
  backface-visibility: hidden;
  perspective: 1000px;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  height: 100%;
  justify-content: space-between;

  .user-details {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    margin: 32rpx 0;
  }

  .user-name {
    font-size: 56rpx; /* 标题：48-64rpx */
    font-weight: 700;
    color: #ffffff;
    margin-bottom: 16rpx;
  }

  .user-bio {
    font-size: 32rpx; /* 正文：28-32rpx */
    color: #ffffff;
    opacity: 0.9;
    margin-bottom: 24rpx;
    line-height: 1.4;
  }

  .user-location {
    display: flex;
    align-items: center;
    gap: 8rpx;
    justify-content: center;
  }

  .location-text {
    font-size: 28rpx; /* 正文：28-32rpx */
    color: #ffffff;
    opacity: 0.8;
  }

  .action-buttons {
    display: flex;
    align-items: center;
    gap: 24rpx;
    width: 100%;
    justify-content: center;
    flex-wrap: wrap;
  }
}

/* 内容区域 - 白色背景，圆角设计，重叠效果 */
.content-section {
  background: #ffffff;
  border-radius: 48rpx 48rpx 0 0;
  margin-top: -48rpx; /* 重叠效果 */
  position: sticky;
  top: 0;
  z-index: 20;
  height: calc(100vh - 196rpx);
  padding: 48rpx 32rpx 32rpx;
  box-sizing: border-box;

  /* 硬件加速优化 */
  transform: translateZ(0);

  /* 滚动性能优化 */
  contain: layout style paint;

  /* scroll-view 特定样式 */
  overflow: hidden; /* 防止内容溢出 */

  /* 安全区域适配 - 底部内边距 */
  padding-bottom: calc(32rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(32rpx + env(safe-area-inset-bottom));

  /* 滚动条样式优化 */
  ::-webkit-scrollbar {
    display: none; /* 隐藏滚动条 */
  }

  /* 滚动平滑优化 */
  scroll-behavior: smooth;
  -webkit-overflow-scrolling: touch; /* iOS 滚动优化 */

  /* 性能优化 - 减少重排重绘 */
  backface-visibility: hidden;

  /* 错误处理 - 确保内容可见 */
  min-height: 400rpx;

  /* 用户体验优化 - 平滑过渡 */
  transition: opacity 0.3s ease, transform 0.3s ease;

  /* 加载状态优化 */
  &.loading {
    opacity: 0.7;
    pointer-events: none;
  }
}

.content-list {
  margin-top: 32rpx;
  position: relative;
  min-height: 400rpx;
}

/* 标签页内容容器 */
.tab-content {
  width: 100%;
  min-height: 400rpx;
  transition: all 0.3s ease;
}

/* 空状态样式 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80rpx 32rpx;
  text-align: center;
  min-height: 400rpx;

  .empty-title {
    font-size: 48rpx; /* 标题：48-64rpx */
    font-weight: 600;
    color: #2c3e50;
    margin: 32rpx 0 16rpx;
  }

  .empty-subtitle {
    font-size: 28rpx; /* 正文：28-32rpx */
    color: #7f8c8d;
    line-height: 1.5;
    margin-bottom: 48rpx;
    max-width: 500rpx;
  }
}

/* 响应式适配 - 小屏设备（iPhone SE, 小屏Android，宽度 < 375px，750rpx） */
@media screen and (max-width: 750rpx) {
  .background-section {
    padding: 60rpx 24rpx 32rpx;
    height: 480rpx;

    /* 安全区域适配 */
    padding-top: calc(60rpx + constant(safe-area-inset-top));
    padding-top: calc(60rpx + env(safe-area-inset-top));
  }

  .user-info {
    .user-name {
      font-size: 44rpx; /* 小屏适配 */
    }

    .user-bio {
      font-size: 26rpx; /* 小屏适配 */
      margin-bottom: 20rpx;
    }

    .location-text {
      font-size: 24rpx; /* 小屏适配 */
    }

    .action-buttons {
      gap: 16rpx; /* 小屏间距调整 */
      flex-direction: column; /* 小屏垂直排列 */
      width: 100%;

      :deep(.u-button) {
        width: 100%;
        max-width: 280rpx;
      }
    }
  }

  .content-section {
    height: calc(100vh - 432rpx); /* 固定高度用于scroll-view */
    padding: 24rpx 16rpx 24rpx;
    margin-top: -32rpx; /* 小屏重叠效果调整 */
    border-radius: 32rpx 32rpx 0 0; /* 小屏圆角调整 */

    /* 安全区域适配 */
    padding-bottom: calc(24rpx + constant(safe-area-inset-bottom));
    padding-bottom: calc(24rpx + env(safe-area-inset-bottom));
  }

  .content-list {
    margin-top: 16rpx;
    min-height: 280rpx;
  }

  .tab-content {
    min-height: 280rpx;
  }

  .empty-state {
    padding: 40rpx 16rpx;
    min-height: 280rpx;

    .empty-title {
      font-size: 36rpx; /* 小屏标题 */
      margin: 24rpx 0 12rpx;
    }

    .empty-subtitle {
      font-size: 24rpx; /* 小屏副标题 */
      max-width: 320rpx;
      margin-bottom: 32rpx;
    }
  }
}

/* 标准屏幕适配 - iPhone 6/7/8（375px宽度，750rpx） */
@media screen and (min-width: 751rpx) and (max-width: 828rpx) {
  .background-section {
    padding: 80rpx 32rpx 40rpx;
    height: 560rpx;

    /* 安全区域适配 */
    padding-top: calc(80rpx + constant(safe-area-inset-top));
    padding-top: calc(80rpx + env(safe-area-inset-top));
  }

  .user-info {
    .user-name {
      font-size: 52rpx; /* 标准屏标题 */
    }

    .user-bio {
      font-size: 30rpx; /* 标准屏正文 */
    }

    .location-text {
      font-size: 26rpx;
    }

    .action-buttons {
      gap: 20rpx;
      flex-direction: row; /* 标准屏水平排列 */
    }
  }

  .content-section {
    height: calc(100vh - 512rpx); /* 固定高度用于scroll-view */
    padding: 40rpx 28rpx 32rpx;

    /* 安全区域适配 */
    padding-bottom: calc(32rpx + constant(safe-area-inset-bottom));
    padding-bottom: calc(32rpx + env(safe-area-inset-bottom));
  }

  .content-list {
    margin-top: 28rpx;
    min-height: 360rpx;
  }

  .tab-content {
    min-height: 360rpx;
  }

  .empty-state {
    padding: 70rpx 28rpx;
    min-height: 360rpx;

    .empty-title {
      font-size: 44rpx;
    }

    .empty-subtitle {
      font-size: 28rpx;
      max-width: 480rpx;
    }
  }
}

/* 大屏适配 - iPhone X系列及以上（414px宽度，828rpx及以上） */
@media screen and (min-width: 829rpx) {
  .background-section {
    padding: 100rpx 48rpx 56rpx;
    height: 640rpx;

    /* 安全区域适配 */
    padding-top: calc(100rpx + constant(safe-area-inset-top));
    padding-top: calc(100rpx + env(safe-area-inset-top));
  }

  .user-info {
    .user-name {
      font-size: 60rpx; /* 大屏标题 */
    }

    .user-bio {
      font-size: 34rpx; /* 大屏正文 */
    }

    .location-text {
      font-size: 30rpx;
    }

    .action-buttons {
      gap: 32rpx;
      flex-direction: row;
    }
  }

  .content-section {
    height: calc(100vh - 592rpx); /* 固定高度用于scroll-view */
    padding: 56rpx 48rpx 48rpx;

    /* 安全区域适配 */
    padding-bottom: calc(48rpx + constant(safe-area-inset-bottom));
    padding-bottom: calc(48rpx + env(safe-area-inset-bottom));
  }

  .content-list {
    margin-top: 40rpx;
    min-height: 440rpx;
  }

  .tab-content {
    min-height: 440rpx;
  }

  .empty-state {
    padding: 100rpx 48rpx;
    min-height: 440rpx;

    .empty-title {
      font-size: 52rpx;
    }

    .empty-subtitle {
      font-size: 32rpx;
      max-width: 600rpx;
    }
  }
}

/* 横屏适配 */
@media screen and (orientation: landscape) {
  .background-section {
    height: 400rpx;
    padding: 40rpx 48rpx 32rpx;

    /* 横屏安全区域适配 */
    padding-top: calc(40rpx + constant(safe-area-inset-top));
    padding-top: calc(40rpx + env(safe-area-inset-top));
    padding-left: calc(48rpx + constant(safe-area-inset-left));
    padding-left: calc(48rpx + env(safe-area-inset-left));
    padding-right: calc(48rpx + constant(safe-area-inset-right));
    padding-right: calc(48rpx + env(safe-area-inset-right));
  }

  .user-info {
    flex-direction: row;
    align-items: center;
    text-align: left;

    .user-details {
      flex: 1;
      align-items: flex-start;
      margin: 0 32rpx;
    }

    .action-buttons {
      flex-direction: column;
      width: auto;
      min-width: 200rpx;
    }
  }

  .content-section {
    height: calc(100vh - 352rpx); /* 固定高度用于scroll-view */
    margin-top: -32rpx;

    /* 横屏安全区域适配 */
    padding-left: calc(32rpx + constant(safe-area-inset-left));
    padding-left: calc(32rpx + env(safe-area-inset-left));
    padding-right: calc(32rpx + constant(safe-area-inset-right));
    padding-right: calc(32rpx + env(safe-area-inset-right));
    padding-bottom: calc(32rpx + constant(safe-area-inset-bottom));
    padding-bottom: calc(32rpx + env(safe-area-inset-bottom));
  }
}

/* 超大屏适配（平板等，宽度 > 1024rpx） */
@media screen and (min-width: 1024rpx) {
  .profile-container {
    max-width: 1024rpx;
    margin: 0 auto;
  }

  .background-section {
    padding: 120rpx 80rpx 80rpx;
    height: 720rpx;
  }

  .content-section {
    padding: 80rpx 80rpx 80rpx;
    max-width: 960rpx;
    margin-left: auto;
    margin-right: auto;
  }

  .empty-state {
    padding: 120rpx 80rpx;

    .empty-subtitle {
      max-width: 720rpx;
    }
  }
}

/* 按钮交互优化 */
.u-button {
  /* 触摸反馈优化 */
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);

  /* 防止双击 */
  user-select: none;
  -webkit-user-select: none;

  /* 触摸优化 */
  -webkit-tap-highlight-color: transparent;

  &:active {
    transform: scale(0.98);
    opacity: 0.8;
  }

  &:disabled {
    opacity: 0.5;
    pointer-events: none;
  }
}

/* 文本选择优化 */
.user-name,
.user-bio,
.location-text,
.empty-title,
.empty-subtitle {
  user-select: text;
  -webkit-user-select: text;
}

/* 滚动性能优化 */
scroll-view {
  /* 滚动优化 */
  -webkit-overflow-scrolling: touch;
  overflow-scrolling: touch;

  /* 防止滚动穿透 */
  overscroll-behavior: contain;

  /* 滚动条优化 */
  scrollbar-width: none;
  -ms-overflow-style: none;
}

/* 动画性能优化 */
@media (prefers-reduced-motion: reduce) {
  * {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
  }
}

/* 低端设备优化 */
@media screen and (max-device-width: 480px) and (-webkit-max-device-pixel-ratio: 1) {
  .background-section {
    /* 降低复杂度 */
    background: #ff6b6b !important;
    transition: none !important;
  }

  .content-section {
    /* 简化动画 */
    transition: none !important;
  }

  .u-transition {
    /* 禁用过渡动画 */
    transition: none !important;
  }
}
</style>