<template>
  <view class="profile-container">
    <!-- 顶部导航栏 -->
    <view
      class="navigator"
      :style="{
        paddingTop: statusBarHeight + 'px',
        backgroundColor: `rgba(${hexToRgb(themeColor)}, ${navOpacity})`,
      }"
    >
      <view class="nav-avatar" :class="{ show: showNavAvatar }">
        <up-avatar
          :src="userInfo.avatarUrl || 'https://picsum.photos/200/200?random=1'"
          size="30"
          shape="circle"
        ></up-avatar>
      </view>
    </view>

    <!-- 主要滚动区域 -->
    <scroll-view
      class="out-scroll"
      scroll-y
      :style="{ height: scrollViewHeight + 'px' }"
      :refresher-background="themeColor"
      :refresher-enabled="true"
      :refresher-triggered="data.isTrigger"
      @scroll="handleScroll"
      @refresherrefresh="refresherrefresh"
      @scrolltolower="scrolltolower"
    >
      <!-- 个人信息区域 -->
      <view
        class="info-box"
        :style="{
          height: infoBoxHeight + 'px',
          backgroundImage: userInfo.bgcUrl ? `url(${userInfo.bgcUrl})` : 'none',
        }"
      >
        <!-- 渐变蒙层 -->
        <view
          class="gradient-mask"
          :style="{
            background: `linear-gradient(to bottom, transparent 0%, ${themeColor} 100%)`,
          }"
        ></view>

        <view class="user-content">
          <!-- 用户信息 -->
          <view class="user-container">
            <view class="user-avatar-container">
              <up-avatar
                :src="
                  userInfo.avatarUrl || 'https://picsum.photos/200/200?random=1'
                "
                size="80"
                shape="circle"
              ></up-avatar>
              <!-- 用户名 -->
              <view class="username">{{ userInfo.username }}</view>
            </view>
            <!-- 用户简介 -->
            <view class="user-bio">{{
              userInfo.introduction || "No introduction yet"
            }}</view>
          </view>
          <view class="option">
            <view class="option-item" @click="goToEditProfile"
              ><up-icon name="edit-pen" color="#fff"></up-icon
            ></view>
            <view class="option-item"
              ><up-icon name="setting" color="#fff"></up-icon
            ></view>
          </view>
        </view>
      </view>

      <!-- 内容区域 -->
      <view class="content">
        <!-- 标签栏 - 使用sticky定位 -->
        <view class="tab-bar" :style="{ backgroundColor: themeColor }">
          <view class="tab-container">
            <!-- 使用 up-tabs 组件替换 tab-item -->
            <up-tabs :list="data.tabList" @click="handleTabClick"></up-tabs>
          </view>
        </view>

        <!-- 内容主体 -->
        <view class="main">
          <!-- 您的具体内容 -->
          <view class="demo-content">
            <view class="demo-item" v-for="i in 20" :key="i">
              内容项 {{ i }}
            </view>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from "vue";
import { useUserStore } from "@/store/modules/user";

// 使用用户store
const userStore = useUserStore();

// 响应式数据
const data = reactive({
  isTrigger: false,
  isLoading: false,
  hasMore: true,
  activeTab: 0,
  // up-tabs 需要的数据格式
  tabList: [{ name: "笔记" }, { name: "收藏" }],
});

// 计算属性
const userInfo = computed(() => userStore.userProfile);

// 计算主题颜色 - 使用背景图主色调
const themeColor = computed(() => {
  return userInfo.value.bgcMainColor || data.themeColor;
});


// 系统信息
const statusBarHeight = ref(0);
const scrollViewHeight = ref(0);
const infoBoxHeight = ref(0);
const navOpacity = ref(0);
const showNavAvatar = ref(false);

// 滚动相关变量
const navigatorHeight = ref(80); // navigator高度(rpx)
const halfScrollDistance = ref(0); // 头像出现的滚动距离

// 工具函数：将hex颜色转换为rgb
const hexToRgb = (hex) => {
  // 移除 # 号
  hex = hex.replace("#", "");

  // 解析RGB值
  const r = parseInt(hex.substring(0, 2), 16);
  const g = parseInt(hex.substring(2, 4), 16);
  const b = parseInt(hex.substring(4, 6), 16);

  return `${r}, ${g}, ${b}`;
};

// 获取系统信息
const getSystemInfo = () => {
  const systemInfo = uni.getSystemInfoSync();
  statusBarHeight.value = systemInfo.statusBarHeight || 0;

  // 计算scroll-view高度
  const windowHeight = systemInfo.windowHeight;
  const navHeightPx = uni.upx2px(navigatorHeight.value);
  scrollViewHeight.value = windowHeight - statusBarHeight.value;

  // info-box 占 scroll-view 高度的一半
  infoBoxHeight.value = scrollViewHeight.value / 2;

  // 头像出现的滚动距离 = info-box高度的一半
  halfScrollDistance.value = infoBoxHeight.value / 2;
};

// 处理滚动事件
const handleScroll = (e) => {
  const scrollTop = e.detail.scrollTop;

  // 透明度根据滚动距离渐变，但最大值在头像出现时就达到1
  navOpacity.value = Math.min(scrollTop / halfScrollDistance.value, 1);

  // 控制头像显示 (滚动距离达到一半时显示头像)
  showNavAvatar.value = scrollTop >= halfScrollDistance.value;
};

// 处理标签点击事件
const handleTabClick = (item) => {
  data.activeTab = item.index;
  console.log("切换到标签:", item.name, "索引:", item.index);
};

// 滚动到底部
const scrolltolower = () => {
  getUserPostList();
};

// 下拉刷新
const refresherrefresh = () => {
  data.isTrigger = true;
  getUserPostList();
};

// 获取用户帖子列表
const getUserPostList = () => {
  // 您的接口请求逻辑
  setTimeout(() => {
    data.isTrigger = false;
  }, 1000);
};

onMounted(() => {
  // 初始化用户store
  userStore.initUserProfile();

  getSystemInfo();
  getUserPostList();
});

const goToEditProfile = () => {
  uni.navigateTo({
    url: "/pages/profile/editProfile",
  });
};
</script>

<style lang="scss" scoped>
* {
  box-sizing: border-box;
}
.profile-container {
  height: 100%;
  background: #f8f8f8;
  overflow: hidden;
}

.navigator {
  height: 80rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1001;

  .nav-avatar {
    opacity: 0;
    transform: translateY(20rpx);
    transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

    &.show {
      opacity: 1;
      transform: translateY(0);
    }
  }
}

.out-scroll {
  .info-box {
    position: relative;
    overflow: hidden;
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;

    .gradient-mask {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      z-index: 2;
    }

    .user-content {
      position: relative;
      z-index: 3;
      height: 100%;
      color: #fff;
      padding: 48rpx;
      padding-top: 80rpx;
      display: flex;
      flex-direction: column;
      justify-content: space-between;

      .user-container {
        .user-avatar-container {
          margin-bottom: 32rpx;
          display: flex;
          align-items: center;
        }

        .username {
          font-size: 48rpx;
          font-weight: bold;
          margin-left: 16rpx;
        }

        .user-bio {
          font-size: 28rpx;
          opacity: 0.9;
          line-height: 1.4;
          max-width: 500rpx;
        }
      }
      .option {
        display: flex;
        justify-content: flex-end;
        gap: 20rpx;
        .option-item {
          font-size: 24rpx;
          border: 1px solid #fff;
          border-radius: 24rpx;
          background-color: transparent;
          padding: 4rpx 12rpx;
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }
    }
  }

  .content {
    .tab-bar {
      position: sticky;
      top: 80rpx;
      z-index: 1000;
      // 背景色通过内联样式动态设置
      height: 104rpx;

      .tab-container {
        height: 104rpx;
        border-radius: 52rpx 52rpx 0 0;
        background-color: #f8f8f8;
        position: relative;
        display: flex;
        justify-content: center;
        box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.1);

        // 删除了原来的 .tab-item 和 .tab-indicator 样式
        // up-tabs 组件会处理自己的样式
      }
    }

    .main {
      padding: 20rpx;
      background: #f8f8f8;

      .demo-content {
        .demo-item {
          height: 100rpx;
          background: #fff;
          margin-bottom: 20rpx;
          border-radius: 16rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 28rpx;
          color: #333;
        }
      }
    }
  }
}

// 针对不同平台的兼容性处理
/* #ifdef H5 */
.tab-bar {
  position: -webkit-sticky;
  position: sticky;
}
/* #endif */

/* #ifdef MP-WEIXIN */
.tab-bar {
  position: sticky;
}
/* #endif */

/* #ifdef APP-PLUS */
.tab-bar {
  position: sticky;
}
/* #endif */

// 适配安全区域
@supports (bottom: env(safe-area-inset-bottom)) {
  .main {
    padding-bottom: calc(200rpx + env(safe-area-inset-bottom));
  }
}
</style>