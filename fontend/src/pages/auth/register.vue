<template>
  <view class="register-container">
    <!-- 自定义导航栏 -->
    <up-navbar
      :border="false"
      title="Create Account"
      :fixed="false"
      @leftClick="handleBack"
    />
    <!-- 背景装饰 -->
    <view class="bg-decoration">
      <view class="circle circle-1"></view>
      <view class="circle circle-2"></view>
      <view class="circle circle-3"></view>
    </view>

    <!-- 主要内容 -->
    <view class="content">
      <!-- 进度指示器 -->
      <view class="progress-section">
        <!-- 简单的步骤指示器作为备用 -->
        <view class="simple-steps">
          <view class="step-item" :class="{ active: currentStep >= 0 }">
            <view class="step-number">1</view>
            <view class="step-name">Basic Info</view>
          </view>
          <view class="step-line" :class="{ active: currentStep >= 1 }"></view>
          <view class="step-item" :class="{ active: currentStep >= 1 }">
            <view class="step-number">2</view>
            <view class="step-name">Profile</view>
          </view>
        </view>

        <!-- uView步骤条 -->
        <u-steps
          :current="currentStep"
          :list="stepsList"
          mode="number"
          direction="row"
          active-color="#FF6B6B"
          inactive-color="#E5E7EB"
          :dot="false"
          style="display: none"
        ></u-steps>
      </view>

      <!-- 步骤内容 -->
      <view class="step-content">
        <!-- 第一步：基本信息 -->
        <view v-if="currentStep === 0" class="step-one" key="step1">
          <view class="step-header">
            <view class="step-title">Basic Information</view>
            <view class="step-subtitle">Tell us about yourself</view>
          </view>

          <u-form
            :model="registerForm"
            :rules="step1Rules"
            ref="step1FormRef"
            label-position="top"
            border="none"
            :label-width="'auto'"
          >
            <!-- 邮箱输入 -->
            <u-form-item prop="email" label="Email Address">
              <u-input
                v-model="registerForm.email"
                placeholder="Enter your email"
                type="email"
                border="none"
                class="warm-input"
                prefix-icon="email"
                prefix-icon-style="color: #FF6B6B"
                @blur="checkEmailAvailability"
              ></u-input>
            </u-form-item>

            <!-- 密码输入 -->
            <u-form-item prop="password" label="Password">
              <u-input
                v-model="registerForm.password"
                placeholder="(min 6 characters)"
                :password="!showPassword"
                border="none"
                class="warm-input"
                prefix-icon="lock"
                prefix-icon-style="color: #FF6B6B"
                @suffix-icon-click="togglePassword"
              ></u-input>
            </u-form-item>

            <!-- 年龄选择 -->
            <u-form-item prop="age" label="Age">
              <u-input
                v-model="registerForm.age"
                placeholder="Select your age"
                border="none"
                class="warm-input"
                prefix-icon="calendar"
                prefix-icon-style="color: #FF6B6B"
                suffix-icon="arrow-down"
                suffix-icon-style="color: #7F8C8D"
                readonly
                @click="showAgePicker = true"
              ></u-input>
            </u-form-item>

            <!-- 性别选择 -->
            <u-form-item prop="gender" label="Gender">
              <u-radio-group v-model="registerForm.gender" placement="row">
                <u-radio
                  v-for="item in genderOptions"
                  :key="item.value"
                  :label="item.value"
                  :name="item.value"
                  active-color="#FF6B6B"
                  style="margin-right: 32rpx"
                >
                  {{ item.label }}
                </u-radio>
              </u-radio-group>
            </u-form-item>
          </u-form>
        </view>

        <!-- 第二步：个人资料 -->
        <view v-if="currentStep === 1" class="step-two" key="step2">
          <view class="step-header">
            <view class="step-title">Personal Profile</view>
            <view class="step-subtitle">Complete your profile</view>
          </view>

          <u-form
            :model="registerForm"
            :rules="step2Rules"
            ref="step2FormRef"
            label-position="top"
            border="none"
          >
            <!-- 用户名输入 -->
            <u-form-item prop="username" label="Username">
              <u-input
                v-model="registerForm.username"
                placeholder="Choose a username"
                border="none"
                class="warm-input"
                prefix-icon="account"
                prefix-icon-style="color: #FF6B6B"
              ></u-input>
            </u-form-item>

            <!-- 恋爱状态选择 -->
            <u-form-item prop="relationshipStatus" label="Relationship Status">
              <u-radio-group
                v-model="registerForm.relationshipStatus"
                placement="column"
              >
                <u-radio
                  v-for="item in relationshipOptions"
                  :key="item.value"
                  :label="item.value"
                  :name="item.value"
                  active-color="#FF6B6B"
                  style="margin-bottom: 32rpx"
                >
                  <view class="relationship-option">
                    <view class="option-title">{{ item.label }}</view>
                    <view class="option-desc">{{ item.description }}</view>
                  </view>
                </u-radio>
              </u-radio-group>
            </u-form-item>

            <!-- 头像选择 (可选) -->
            <u-form-item label="Profile Picture (Optional)">
              <view class="avatar-section">
                <view class="current-avatar">
                  <u-avatar
                    :src="registerForm.avatarUrl"
                    size="80"
                    shape="circle"
                    bg-color="#FFF5F5"
                    icon="camera"
                    icon-color="#FF6B6B"
                  ></u-avatar>
                </view>
                <view class="avatar-options">
                  <u-button
                    text="Choose Avatar"
                    type="primary"
                    size="small"
                    class="secondary-button"
                    @click="showAvatarPicker = true"
                  ></u-button>
                  <u-button
                    text="Upload Photo"
                    type="primary"
                    size="small"
                    class="secondary-button"
                    @click="uploadAvatar"
                  ></u-button>
                </view>
              </view>
            </u-form-item>

            <!-- 位置输入（可选） -->
            <u-form-item label="Location (Optional)">
              <u-input
                v-model="registerForm.location"
                placeholder="Enter your city"
                border="none"
                class="warm-input"
                prefix-icon="map"
                prefix-icon-style="color: #FF6B6B"
              ></u-input>
            </u-form-item>
          </u-form>
        </view>
      </view>

      <!-- 底部按钮 -->
      <view class="button-section">
        <u-button
          v-if="currentStep === 0"
          :loading="false"
          loading-icon="spinner-cycle"
          class="primary-button"
          text="Continue"
          @click="nextStep"
        >
        </u-button>

        <u-button
          v-if="currentStep === 1"
          :loading="authStore.registerLoading"
          loading-text="Creating Account..."
          loading-icon="spinner-cycle"
          class="primary-button"
          text="Create Account"
          @click="handleRegister"
        >
        </u-button>
      </view>

      <!-- 登录链接 -->
      <view class="login-section">
        <u-text
          text="Already have an account? "
          color="#7F8C8D"
          size="14"
        ></u-text>
        <u-text
          text="Sign In"
          color="#FF6B6B"
          size="14"
          bold
          @click="goToLogin"
        ></u-text>
      </view>
    </view>

    <!-- 年龄选择器 -->
    <u-picker
      :show="showAgePicker"
      :columns="ageColumns"
      @confirm="confirmAge"
      @cancel="showAgePicker = false"
      confirm-color="#FF6B6B"
    ></u-picker>

    <!-- 头像选择器 -->
    <u-action-sheet
      :show="showAvatarPicker"
      :actions="avatarActions"
      @select="selectAvatar"
      @close="showAvatarPicker = false"
      cancel-text="Cancel"
    ></u-action-sheet>
  </view>
</template>

<script>
import { ref, reactive, computed, onMounted } from "vue";
import { useAuthStore } from "@/store/modules/auth";
import { useUserStore } from "@/store/modules/user";
import { authAPI } from "@/api/auth";

export default {
  name: "RegisterPage",
  setup() {
    // Store
    const authStore = useAuthStore();
    const userStore = useUserStore();

    // 响应式数据
    const currentStep = ref(0);
    const step1FormRef = ref(null);
    const step2FormRef = ref(null);
    const showPassword = ref(false);
    const showAgePicker = ref(false);
    const showAvatarPicker = ref(false);

    // 表单数据
    const registerForm = reactive({
      email: "",
      password: "",
      age: "",
      gender: "",
      username: "",
      relationshipStatus: "",
      avatarUrl: "",
      location: "",
    });

    // 步骤列表
    const stepsList = ref([{ name: "Basic Info" }, { name: "Profile" }]);

    // 性别选项
    const genderOptions = ref([
      { label: "Male", value: "MALE" },
      { label: "Female", value: "FEMALE" },
      { label: "Other", value: "OTHER" },
    ]);

    // 恋爱状态选项
    const relationshipOptions = ref([
      {
        label: "Single",
        value: "SINGLE",
        description: "Looking for love and dating opportunities",
      },
      {
        label: "In a Relationship",
        value: "IN_RELATIONSHIP",
        description: "Want to strengthen existing relationship",
      },
    ]);

    // 年龄选项
    const ageColumns = computed(() => {
      const ages = [];
      for (let i = 18; i <= 35; i++) {
        ages.push(i.toString());
      }
      return [ages];
    });

    // 头像选项
    const avatarActions = ref([
      { name: "Random Avatar 1", value: "avatar1" },
      { name: "Random Avatar 2", value: "avatar2" },
      { name: "Random Avatar 3", value: "avatar3" },
      { name: "Random Avatar 4", value: "avatar4" },
      { name: "Random Avatar 5", value: "avatar5" },
    ]);

    // 第一步验证规则
    const step1Rules = reactive({
      email: [
        {
          required: true,
          message: "Email is required",
          trigger: ["blur", "change"],
        },
        {
          type: "email",
          message: "Please enter a valid email address",
          trigger: ["blur", "change"],
        },
      ],
      password: [
        {
          required: true,
          message: "Password is required",
          trigger: ["blur", "change"],
        },
        {
          min: 6,
          message: "Password must be at least 6 characters",
          trigger: ["blur", "change"],
        },
      ],
      age: [
        {
          required: true,
          message: "Age is required",
          trigger: ["blur", "change"],
        },
      ],
      gender: [
        {
          required: true,
          message: "Gender is required",
          trigger: ["blur", "change"],
        },
      ],
    });

    // 第二步验证规则
    const step2Rules = reactive({
      username: [
        {
          required: true,
          message: "Username is required",
          trigger: ["blur", "change"],
        },
        {
          min: 2,
          max: 50,
          message: "Username must be between 2-50 characters",
          trigger: ["blur", "change"],
        },
      ],
      relationshipStatus: [
        {
          required: true,
          message: "Relationship status is required",
          trigger: ["blur", "change"],
        },
      ],
    });

    // 方法
    const handleBack = () => {
      if (currentStep.value > 0) {
        // 第二步：返回第一步
        currentStep.value--;
      } else {
        // 第一步：返回登录页面
        uni.reLaunch({
          url: "/pages/auth/login",
        });
      }
    };

    const togglePassword = () => {
      showPassword.value = !showPassword.value;
    };

    const checkEmailAvailability = async () => {
      if (
        registerForm.email &&
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(registerForm.email)
      ) {
        try {
          const responseData = await authAPI.checkEmail(registerForm.email);
          if (responseData.exists) {
            uni.showToast({
              title: "Email already registered",
              icon: "error",
              duration: 2000,
            });
          }
        } catch (error) {
          console.error("Check email error:", error);
          // 响应拦截器已处理错误信息显示
        }
      }
    };

    const confirmAge = (value) => {
      registerForm.age = value.value[0];
      showAgePicker.value = false;
    };

    const selectAvatar = (item) => {
      // 这里可以设置预设头像URL
      registerForm.avatarUrl = `/static/avatars/${item.value}.png`;
      showAvatarPicker.value = false;
    };

    const uploadAvatar = () => {
      uni.chooseImage({
        count: 1,
        sizeType: ["compressed"],
        sourceType: ["album", "camera"],
        success: (res) => {
          // 这里应该上传到服务器，现在先使用本地路径
          registerForm.avatarUrl = res.tempFilePaths[0];
          uni.showToast({
            title: "Avatar uploaded",
            icon: "success",
            duration: 1500,
          });
        },
        fail: (error) => {
          console.error("Upload avatar error:", error);
          uni.showToast({
            title: "Upload failed",
            icon: "error",
            duration: 2000,
          });
        },
      });
    };

    const nextStep = async () => {
      try {
        // 验证第一步表单
        const valid = await step1FormRef.value.validate();
        if (!valid) return;

        // 进入第二步
        currentStep.value = 1;
      } catch (error) {
        console.error("Step 1 validation error:", error);
      }
    };

    const handleRegister = async () => {
      try {
        // 验证第二步表单
        const valid = await step2FormRef.value.validate();
        if (!valid) return;

        // 设置加载状态
        authStore.setRegisterLoading(true);

        // 调用注册API - 响应拦截器已处理统一格式，直接获取data
        const responseData = await authAPI.register(registerForm);

        // 保存认证信息
        authStore.setTokens({
          accessToken: responseData.accessToken,
          refreshToken: responseData.refreshToken,
        });

        // 保存用户信息
        userStore.setUserProfile(responseData.user);

        // 显示成功提示
        uni.showToast({
          title: "Account Created Successfully",
          icon: "success",
          duration: 2000,
        });

        // 跳转到主页面
        uni.reLaunch({
          url: "/pages/main/index",
        });
      } catch (error) {
        console.error("Register error:", error);

        // 显示错误提示 - 响应拦截器已处理错误信息显示
        // 这里只需要记录日志，用户已经看到错误提示
      } finally {
        // 清除加载状态
        authStore.setRegisterLoading(false);
      }
    };

    const goToLogin = () => {
      uni.reLaunch({
        url: "/pages/auth/login",
      });
    };

    // 生命周期
    onMounted(() => {
      // 隐藏底部tab（如果是tabBar页面）
      try {
        uni.hideTabBar();
      } catch (error) {
        // 非tabBar页面会报错，忽略即可
      }

      // 初始化默认头像
      if (!registerForm.avatarUrl) {
        registerForm.avatarUrl = "/static/avatars/default.png";
      }
    });

    return {
      // Store
      authStore,

      // 响应式数据
      currentStep,
      step1FormRef,
      step2FormRef,
      showPassword,
      showAgePicker,
      showAvatarPicker,

      registerForm,
      stepsList,
      genderOptions,
      relationshipOptions,
      ageColumns,
      avatarActions,
      step1Rules,
      step2Rules,

      // 方法
      handleBack,
      togglePassword,
      checkEmailAvailability,
      confirmAge,
      selectAvatar,
      uploadAvatar,
      nextStep,
      handleRegister,
      goToLogin,
    };
  },
};
</script>

<style lang="scss" scoped>
.register-container {
  background: linear-gradient(135deg, #fff5f5 0%, #fefefe 100%);
  position: relative;
  overflow: hidden;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}

.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;

  .circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.1;

    &.circle-1 {
      width: 360rpx;
      height: 360rpx;
      background: linear-gradient(135deg, #ff6b6b, #ff8e8e);
      top: -180rpx;
      left: -180rpx;
    }

    &.circle-2 {
      width: 240rpx;
      height: 240rpx;
      background: linear-gradient(135deg, #ffb3ba, #ff9aa2);
      bottom: 30%;
      right: -120rpx;
    }

    &.circle-3 {
      width: 160rpx;
      height: 160rpx;
      background: linear-gradient(135deg, #ffaaa5, #ffb3ba);
      top: 40%;
      right: 30%;
    }
  }
}

.content {
  position: relative;
  z-index: 1;
  padding: 40rpx 64rpx 40rpx;
  flex: 1;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  overflow-y: auto;
  min-height: 0;
}

.nav-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 64rpx;

  .nav-title {
    font-size: 40rpx; /* 页面标题：48-64rpx */
    font-weight: 600;
    color: #2c3e50;
  }

  .nav-placeholder {
    width: 48rpx;
  }
}

.progress-section {
  margin-bottom: 48rpx;
  padding: 0 32rpx;

  .simple-steps {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 32rpx;

    .step-item {
      display: flex;
      flex-direction: column;
      align-items: center;

      .step-number {
        width: 64rpx;
        height: 64rpx;
        border-radius: 50%;
        background-color: #e5e7eb;
        color: #7f8c8d;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 28rpx;
        font-weight: 600;
        margin-bottom: 16rpx;
        transition: all 0.3s ease;
      }

      .step-name {
        font-size: 24rpx;
        color: #7f8c8d;
        transition: all 0.3s ease;
      }

      &.active {
        .step-number {
          background-color: #ff6b6b;
          color: #ffffff;
        }

        .step-name {
          color: #ff6b6b;
          font-weight: 600;
        }
      }
    }

    .step-line {
      width: 120rpx;
      height: 4rpx;
      background-color: #e5e7eb;
      margin: 0 32rpx;
      margin-bottom: 40rpx;
      transition: all 0.3s ease;

      &.active {
        background-color: #ff6b6b;
      }
    }
  }

  :deep(.u-steps) {
    display: flex !important;
    visibility: visible !important;
  }

  :deep(.u-steps-item) {
    display: flex !important;
    visibility: visible !important;
    flex: 1;
  }

  :deep(.u-steps-item__line) {
    background-color: #e5e7eb !important;
    height: 2px !important;
  }

  :deep(.u-steps-item__line--active) {
    background-color: #ff6b6b !important;
  }

  :deep(.u-steps-item__icon) {
    background-color: #e5e7eb !important;
    border-color: #e5e7eb !important;
    color: #7f8c8d !important;
  }

  :deep(.u-steps-item__icon--active) {
    background-color: #ff6b6b !important;
    border-color: #ff6b6b !important;
    color: #ffffff !important;
  }

  :deep(.u-steps-item__title) {
    color: #7f8c8d !important;
    font-size: 24rpx !important;
  }

  :deep(.u-steps-item__title--active) {
    color: #ff6b6b !important;
    font-weight: 600 !important;
  }
}

.step-content {
  flex: 1;
  margin-bottom: 40rpx;
  overflow-y: auto;
  min-height: 0;
}

.step-header {
  text-align: center;
  margin-bottom: 48rpx;

  .step-title {
    font-size: 48rpx; /* 页面标题：48-64rpx */
    font-weight: 700;
    color: #2c3e50;
    margin-bottom: 16rpx;
  }

  .step-subtitle {
    font-size: 26rpx; /* 辅助文字：24-28rpx */
    color: #7f8c8d;
  }
}

.step-one,
.step-two {
  animation: slideInRight 0.5s ease-out;

  :deep(.u-form-item__label) {
    font-size: 30rpx; /* 正文内容：28-32rpx */
    font-weight: 600;
    color: #2c3e50;
    margin-bottom: 16rpx;
  }

  :deep(.u-form-item) {
    margin-bottom: 64rpx; /* 增加间距为错误提示预留空间 */
    position: relative;
    display: block !important;
    visibility: visible !important;
  }

  :deep(.u-input) {
    display: block !important;
    visibility: visible !important;
  }

  :deep(.u-radio-group) {
    display: flex !important;
    visibility: visible !important;
  }
}

.relationship-option {
  .option-title {
    font-size: 32rpx; /* 正文内容：28-32rpx */
    font-weight: 600;
    color: #2c3e50;
    margin-bottom: 8rpx;
  }

  .option-desc {
    font-size: 24rpx; /* 辅助文字：24-28rpx */
    color: #7f8c8d;
    line-height: 1.4;
  }
}

.avatar-section {
  display: flex;
  align-items: center;
  gap: 40rpx;

  .current-avatar {
    flex-shrink: 0;
  }

  .avatar-options {
    display: flex;
    flex-direction: column;
    gap: 24rpx;
    flex: 1;
  }
}

.button-section {
  margin-bottom: 32rpx;
  flex-shrink: 0;
}

.login-section {
  text-align: center;
  margin-bottom: 32rpx;
  flex-shrink: 0;
}

/* 动画定义 */
@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(60rpx);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 响应式适配 - iPhone 6/7/8（375px宽度，750rpx） */
@media screen and (max-width: 375px) {
  .content {
    padding: 40rpx 48rpx 48rpx;
  }

  .step-header {
    margin-bottom: 48rpx;

    .step-title {
      font-size: 48rpx; /* 页面标题：48-64rpx */
    }
  }

  .avatar-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 32rpx;
  }
}

/* iPhone X系列适配（414px宽度，828rpx） */
@media screen and (min-width: 376px) and (max-width: 414px) {
  .content {
    padding: 40rpx 64rpx 40rpx;
  }

  .step-header {
    .step-title {
      font-size: 56rpx; /* 页面标题：48-64rpx */
    }
  }
}

/* Android主流机型适配（360px-428px宽度） */
@media screen and (min-width: 360px) and (max-width: 428px) {
  .register-container {
    font-size: 30rpx; /* 确保基础字体大小适配 */
  }
}

</style>