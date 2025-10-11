<template>
  <view class="publicPage">
    <!-- 导航栏 -->
    <up-navbar
      title="Edit Profile"
      :border="false"
      bg-color="#fff"
      title-color="#333"
      :autoBack="false"
      :fixed="false"
      @leftClick="handleBack"
    >
    </up-navbar>
    <!-- 头像 -->
    <view class="avatar-section">
      <view class="avatar-container" @click="chooseAvatar">
        <up-avatar
          :src="userInfo.avatarUrl || '/static/images/default-avatar.png'"
          size="100"
          shape="circle"
        ></up-avatar>
        <view class="camera-icon">
          <up-icon name="camera" color="#fff" size="16"></up-icon>
        </view>
      </view>
    </view>
    <!-- 内容区域 -->

    <!-- 表单项 -->
    <view class="form-section">
      <!-- 名字 -->
      <up-cell-group :border="false">
        <up-cell
          title="Name"
          :value="userInfo.username || 'Tap to set name'"
          is-link
          @click="openEditModal('username', 'Name')"
        ></up-cell>
      </up-cell-group>

      <!-- 简介 -->
      <up-cell-group :border="false">
        <up-cell
          title="Introduction"
          :value="userInfo.introduction || 'Add your introduction'"
          is-link
          @click="openEditModal('introduction', 'Introduction', 'textarea')"
        ></up-cell>
      </up-cell-group>

      <!-- 性别 -->
      <up-cell-group :border="false">
        <up-cell
          title="Gender"
          :value="getGenderDisplay(userInfo.gender)"
          is-link
          @click="openPicker('gender')"
        ></up-cell>
      </up-cell-group>

      <!-- 年龄 -->
      <up-cell-group :border="false">
        <up-cell
          title="Age"
          :value="userInfo.age ? userInfo.age.toString() : 'Set your age'"
          is-link
          @click="openEditModal('age', 'Age', 'number')"
        ></up-cell>
      </up-cell-group>

      <!-- 感情状态 -->
      <up-cell-group :border="false">
        <up-cell
          title="Relationship Status"
          :value="getRelationshipDisplay(userInfo.relationshipStatus)"
          is-link
          @click="openPicker('relationshipStatus')"
        ></up-cell>
      </up-cell-group>

      <!-- 位置 -->
      <up-cell-group :border="false">
        <up-cell
          title="Location"
          :value="userInfo.location || 'Add your location'"
          is-link
          @click="openEditModal('location', 'Location')"
        ></up-cell>
      </up-cell-group>

      <!-- 背景图 -->
      <up-cell-group :border="false">
        <up-cell title="Background" is-link @click="chooseBackground">
          <template #right-icon>
            <view class="bg-preview">
              <image
                v-if="userInfo.bgcUrl"
                :src="userInfo.bgcUrl"
                class="bg-thumb"
                mode="aspectFill"
              />
              <up-icon name="arrow-right" color="#909399" size="16"></up-icon>
            </view>
          </template>
        </up-cell>
      </up-cell-group>
    </view>

    <!-- 统一编辑弹窗 -->
    <up-popup
      :show="editModal.show"
      mode="center"
      border-radius="12"
      :custom-style="{ width: '80%' }"
    >
      <view class="modal-content">
        <view class="modal-title">Edit {{ editModal.title }}</view>

        <!-- 普通输入框 -->
        <up-input
          v-if="editModal.type === 'input' || editModal.type === 'number'"
          v-model="tempData.value"
          :placeholder="`Please enter your ${editModal.title.toLowerCase()}`"
          :type="editModal.type === 'number' ? 'number' : 'text'"
          :maxlength="editModal.maxlength"
          class="modal-input"
        ></up-input>

        <!-- 文本域 -->
        <up-textarea
          v-if="editModal.type === 'textarea'"
          v-model="tempData.value"
          :placeholder="`Please enter your ${editModal.title.toLowerCase()}`"
          :maxlength="editModal.maxlength"
          height="120"
          class="modal-textarea"
        ></up-textarea>

        <view class="modal-buttons">
          <up-button
            text="Cancel"
            type="info"
            plain
            size="small"
            @click="closeEditModal"
          ></up-button>
          <up-button
            text="Confirm"
            type="primary"
            size="small"
            @click="saveField"
          ></up-button>
        </view>
      </view>
    </up-popup>

    <!-- 性别选择器 -->
    <up-action-sheet
      :show="pickerModal.show && pickerModal.type === 'gender'"
      :actions="genderOptions"
      title="Select Gender"
      @select="onPickerSelect"
      @close="closePickerModal"
      @cancel="closePickerModal"
      :close-on-click-overlay="true"
      :close-on-click-action="false"
    ></up-action-sheet>

    <!-- 感情状态选择器 -->
    <up-action-sheet
      :show="pickerModal.show && pickerModal.type === 'relationshipStatus'"
      :actions="relationshipOptions"
      title="Select Relationship Status"
      @select="onPickerSelect"
      @close="closePickerModal"
      @cancel="closePickerModal"
      :close-on-click-overlay="true"
      :close-on-click-action="false"
    ></up-action-sheet>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { useUserStore } from "@/store/modules/user";

// 使用userStore
const userStore = useUserStore();

// 用户信息 - 从userStore获取
const userInfo = reactive(userStore.userProfile);

// 加载状态
const loading = ref(false);

// 统一的编辑弹窗状态
const editModal = reactive({
  show: false,
  field: "",
  title: "",
  type: "input", // input, textarea, number
  maxlength: 50,
});

// 统一的选择器弹窗状态
const pickerModal = reactive({
  show: false,
  type: "", // gender, relationshipStatus
  field: "",
});

// 临时数据对象
const tempData = reactive({
  value: "",
});

// 字段配置
const fieldConfig = {
  username: { title: "Name", type: "input", maxlength: 20 },
  introduction: { title: "Introduction", type: "textarea", maxlength: 100 },
  age: { title: "Age", type: "number", maxlength: 3 },
  location: { title: "Location", type: "input", maxlength: 50 },
};

// 性别选项
const genderOptions = [
  { name: "Male", value: "MALE" },
  { name: "Female", value: "FEMALE" },
  { name: "Other", value: "OTHER" },
];

// 感情状态选项
const relationshipOptions = [
  { name: "Single", value: "SINGLE" },
  { name: "In a relationship", value: "IN_RELATIONSHIP" },
  { name: "Married", value: "MARRIED" },
  { name: "Complicated", value: "COMPLICATED" },
];

// 选择器配置
const pickerConfig = {
  gender: genderOptions,
  relationshipStatus: relationshipOptions,
};

// 打开编辑弹窗
const openEditModal = (field, title, type = "input") => {
  const config = fieldConfig[field];

  editModal.show = true;
  editModal.field = field;
  editModal.title = title || config.title;
  editModal.type = type || config.type;
  editModal.maxlength = config.maxlength;

  // 设置当前值
  tempData.value = userInfo[field] ? userInfo[field].toString() : "";
};

// 关闭编辑弹窗
const closeEditModal = () => {
  editModal.show = false;
  tempData.value = "";
};

// 保存字段
const saveField = () => {
  const field = editModal.field;
  let value = tempData.value.trim();

  // 特殊处理
  if (field === "age") {
    const age = parseInt(value);
    if (!age || age <= 0 || age >= 150) {
      uni.showToast({
        title: "Please enter a valid age",
        icon: "none",
      });
      return;
    }
    value = age;
  }

  if (field === "username" && !value) {
    uni.showToast({
      title: "Name cannot be empty",
      icon: "none",
    });
    return;
  }

  // 更新用户信息
  userInfo[field] = value;
  closeEditModal();
  saveUserInfo();
};

// 打开选择器
const openPicker = (field) => {
  pickerModal.show = true;
  pickerModal.type = field;
  pickerModal.field = field;
};

// 关闭选择器
const closePickerModal = () => {
  pickerModal.show = false;
  pickerModal.type = "";
  pickerModal.field = "";
};

// 选择器选择回调
const onPickerSelect = (item) => {
  const field = pickerModal.field;
  userInfo[field] = item.value;
  closePickerModal();
  saveUserInfo();
};

// 选择头像
const chooseAvatar = () => {
  uni.chooseImage({
    count: 1,
    sizeType: ["compressed"],
    sourceType: ["album", "camera"],
    success: (res) => {
      userInfo.avatarUrl = res.tempFilePaths[0];
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
      userInfo.bgcUrl = res.tempFilePaths[0];
      uploadImage(res.tempFilePaths[0], "background");
    },
  });
};

// 上传图片
const uploadImage = (filePath, type) => {
  uni.showLoading({ title: "Uploading..." });

  // 暂时直接使用本地路径，实际项目中需要上传到服务器
  setTimeout(() => {
    uni.hideLoading();
    uni.showToast({
      title: "Upload Success",
      icon: "success",
    });

    saveUserInfo();
  }, 1000);
};

const handleBack = () => {
  uni.navigateBack();
};

// 保存用户信息
const saveUserInfo = async () => {
  try {
    loading.value = true;

    const updateData = {
      username: userInfo.username,
      age: userInfo.age,
      gender: userInfo.gender,
      relationshipStatus: userInfo.relationshipStatus,
      avatarUrl: userInfo.avatarUrl,
      introduction: userInfo.introduction,
      location: userInfo.location,
      bgcUrl: userInfo.bgcUrl,
      bgcMainColor: userInfo.bgcMainColor,
    };

    await userStore.saveUserProfile(updateData);

    uni.showToast({
      title: "Saved successfully",
      icon: "success",
    });
  } catch (error) {
    console.error("保存失败:", error);
    uni.showToast({
      title: "Save failed",
      icon: "none",
    });
  } finally {
    loading.value = false;
  }
};

// 获取性别显示文本
const getGenderDisplay = (gender) => {
  const option = genderOptions.find((opt) => opt.value === gender);
  return option ? option.name : "Select Gender";
};

// 获取感情状态显示文本
const getRelationshipDisplay = (status) => {
  const option = relationshipOptions.find((opt) => opt.value === status);
  return option ? option.name : "Select Status";
};

// 页面加载时初始化
onMounted(() => {
  userStore.initUserProfile();
});
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
  overflow: auto;
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