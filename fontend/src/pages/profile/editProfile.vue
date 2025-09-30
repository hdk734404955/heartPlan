<template>
  <view class="edit-profile">
    <!-- 导航栏 -->
    <up-navbar
      title="Edit Profile"
      :border="false"
      bg-color="#fff"
      title-color="#333"
      :autoBack="true"
      :fixed="false"
    >
    </up-navbar>

    <!-- 内容区域 -->
    <view class="content">
      <!-- 头像 -->
      <view class="avatar-section">
        <view class="avatar-container" @click="chooseAvatar">
          <up-avatar
            :src="userInfo.avatar || 'https://picsum.photos/200/200?random=1'"
            size="100"
            shape="circle"
          ></up-avatar>
          <view class="camera-icon">
            <up-icon name="camera" color="#fff" size="16"></up-icon>
          </view>
        </view>
      </view>

      <!-- 表单项 -->
      <view class="form-section">
        <!-- 名字 -->
        <up-cell-group :border="false">
          <up-cell
            title="Name"
            :value="userInfo.nickname"
            is-link
            @click="editName"
          ></up-cell>
        </up-cell-group>

        <!-- 简介 -->
        <up-cell-group :border="false">
          <up-cell
            title="Bio"
            :value="userInfo.bio || 'An interesting bio can attract followers'"
            is-link
            @click="editBio"
          ></up-cell>
        </up-cell-group>

        <!-- 性别 -->
        <up-cell-group :border="false">
          <up-cell
            title="Gender"
            :value="userInfo.gender"
            is-link
            @click="selectGender"
          ></up-cell>
        </up-cell-group>

        <!-- 背景图 -->
        <up-cell-group :border="false">
          <up-cell title="Background" is-link @click="chooseBackground">
            <template #right-icon>
              <view class="bg-preview">
                <image
                  v-if="userInfo.backgroundImage"
                  :src="userInfo.backgroundImage"
                  class="bg-thumb"
                  mode="aspectFill"
                />
                <up-icon name="arrow-right" color="#909399" size="16"></up-icon>
              </view>
            </template>
          </up-cell>
        </up-cell-group>
      </view>
    </view>

    <!-- 弹窗 -->
    <!-- 编辑名字弹窗 -->
    <up-popup
      :show="showNameModal"
      mode="center"
      border-radius="12"
      :custom-style="{ width: '80%' }"
    >
      <view class="modal-content">
        <view class="modal-title">Edit Name</view>
        <up-input
          v-model="tempName"
          placeholder="Please enter your name"
          :maxlength="20"
          class="modal-input"
        ></up-input>
        <view class="modal-buttons">
          <up-button
            text="Cancel"
            type="info"
            plain
            size="small"
            @click="showNameModal = false"
          ></up-button>
          <up-button
            text="Confirm"
            type="primary"
            size="small"
            @click="saveName"
          ></up-button>
        </view>
      </view>
    </up-popup>

    <!-- 编辑简介弹窗 -->
    <up-popup
      :show="showBioModal"
      mode="center"
      border-radius="12"
      :custom-style="{ width: '80%' }"
    >
      <view class="modal-content">
        <view class="modal-title">Edit Bio</view>
        <up-textarea
          v-model="tempBio"
          placeholder="Please enter your bio"
          :maxlength="100"
          height="120"
          class="modal-textarea"
        ></up-textarea>
        <view class="modal-buttons">
          <up-button
            text="Cancel"
            type="info"
            plain
            size="small"
            @click="showBioModal = false"
          ></up-button>
          <up-button
            text="Confirm"
            type="primary"
            size="small"
            @click="saveBio"
          ></up-button>
        </view>
      </view>
    </up-popup>

    <!-- 性别选择器 -->
    <up-action-sheet
      :show="showGenderPicker"
      :actions="genderOptions"
      title="Select Gender"
      @select="onGenderSelect"
      @close="closeGenderPicker"
      @cancel="closeGenderPicker"
      :close-on-click-overlay="true"
      :close-on-click-action="false"
    ></up-action-sheet>
  </view>
</template>

<script setup>
import { ref, reactive } from "vue";

// 用户信息
const userInfo = reactive({
  avatar: "https://picsum.photos/200/200?random=1",
  nickname: "K",
  bio: "",
  gender: "Male",
  backgroundImage: "https://picsum.photos/800/600?random=2",
});

// 弹窗控制
const showNameModal = ref(false);
const showBioModal = ref(false);
const showGenderPicker = ref(false);

// 临时数据
const tempName = ref("");
const tempBio = ref("");

// 性别选项
const genderOptions = [
  { name: "Male", value: "Male" },
  { name: "Female", value: "Female" },
  { name: "Private", value: "Private" },
];

// 选择头像
const chooseAvatar = () => {
  uni.chooseImage({
    count: 1,
    sizeType: ["compressed"],
    sourceType: ["album", "camera"],
    success: (res) => {
      userInfo.avatar = res.tempFilePaths[0];
      // 这里可以上传到服务器
      uploadImage(res.tempFilePaths[0], "avatar");
    },
  });
};

// 选择背景图
const chooseBackground = () => {
  uni.chooseImage({
    count: 1,
    sizeType: ["compressed"],
    sourceType: ["album", "camera"],
    success: (res) => {
      userInfo.backgroundImage = res.tempFilePaths[0];
      // 这里可以上传到服务器
      uploadImage(res.tempFilePaths[0], "background");
    },
  });
};

// 编辑名字
const editName = () => {
  tempName.value = userInfo.nickname;
  showNameModal.value = true;
};

// 保存名字
const saveName = () => {
  if (tempName.value.trim()) {
    userInfo.nickname = tempName.value.trim();
    showNameModal.value = false;
    // 这里可以调用API保存
    saveUserInfo();
  }
};

// 编辑简介
const editBio = () => {
  tempBio.value = userInfo.bio;
  showBioModal.value = true;
};

// 保存简介
const saveBio = () => {
  userInfo.bio = tempBio.value.trim();
  showBioModal.value = false;
  // 这里可以调用API保存
  saveUserInfo();
};

// 选择性别
const selectGender = () => {
  showGenderPicker.value = true;
};

// 性别选择回调
const onGenderSelect = (item) => {
  userInfo.gender = item.value;
  showGenderPicker.value = false;
  // 这里可以调用API保存
  saveUserInfo();
};

// 关闭性别选择器
const closeGenderPicker = () => {
  showGenderPicker.value = false;
};

// 上传图片
const uploadImage = (filePath, type) => {
  uni.showLoading({ title: "Uploading..." });

  // 模拟上传
  setTimeout(() => {
    uni.hideLoading();
    uni.showToast({
      title: "Upload Success",
      icon: "success",
    });
    // 实际项目中这里调用上传API
    // uploadToServer(filePath, type);
  }, 1500);
};

// 保存用户信息
const saveUserInfo = () => {
  // 这里调用API保存用户信息
  console.log("Save user info:", userInfo);
  uni.showToast({
    title: "Save Success",
    icon: "success",
  });
};
</script>

<style lang="scss" scoped>
.edit-profile {
  height: 100%;
  background: #f8f8f8;
}

.avatar-section {
  display: flex;
  justify-content: center;
  padding: 60rpx 0;
  background: #fff;
  margin-bottom: 20rpx;

  .avatar-container {
    position: relative;
    cursor: pointer;

    .camera-icon {
      position: absolute;
      bottom: 0;
      right: 0;
      width: 42rpx;
      height: 42rpx;
      background: #333;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
}

.form-section {
  .up-cell-group {
    margin-bottom: 20rpx;
  }

  .bg-preview {
    display: flex;
    align-items: center;
    gap: 16rpx;

    .bg-thumb {
      width: 60rpx;
      height: 40rpx;
      border-radius: 8rpx;
    }
  }
}

.modal-content {
  padding: 40rpx;

  .modal-title {
    font-size: 32rpx;
    font-weight: bold;
    text-align: center;
    margin-bottom: 40rpx;
    color: #333;
  }

  .modal-input {
    margin-bottom: 40rpx;
  }

  .modal-textarea {
    margin-bottom: 40rpx;
  }

  .modal-buttons {
    display: flex;
    gap: 20rpx;
    justify-content: center;

    .up-button {
      flex: 1;
    }
  }
}
</style>