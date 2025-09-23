// 用户信息状态管理
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  // 状态
  const userProfile = ref({
    id: null,
    email: '',
    username: '',
    age: null,
    gender: '',
    relationshipStatus: '', // SINGLE 或 IN_RELATIONSHIP
    avatarUrl: '',
    location: '',
    bio: '',
    createdAt: null,
    preferences: {},
    privacySettings: {}
  })
  
  const profileLoading = ref(false)
  const updateLoading = ref(false)

  // 计算属性
  const isSingle = computed(() => {
    return userProfile.value.relationshipStatus === 'SINGLE'
  })

  const isInRelationship = computed(() => {
    return userProfile.value.relationshipStatus === 'IN_RELATIONSHIP'
  })

  const displayName = computed(() => {
    return userProfile.value.username || userProfile.value.email
  })

  const hasCompleteProfile = computed(() => {
    const profile = userProfile.value
    return profile.username && profile.age && profile.gender && profile.relationshipStatus
  })

  // 操作方法
  const setUserProfile = (profile) => {
    userProfile.value = { ...userProfile.value, ...profile }
    
    // 存储到本地
    uni.setStorageSync('userProfile', userProfile.value)
  }

  const updateUserProfile = (updates) => {
    userProfile.value = { ...userProfile.value, ...updates }
    
    // 更新本地存储
    uni.setStorageSync('userProfile', userProfile.value)
  }

  const clearUserProfile = () => {
    userProfile.value = {
      id: null,
      email: '',
      username: '',
      age: null,
      gender: '',
      relationshipStatus: '',
      avatarUrl: '',
      location: '',
      bio: '',
      createdAt: null,
      preferences: {},
      privacySettings: {}
    }
    
    // 清除本地存储
    uni.removeStorageSync('userProfile')
  }

  const initUserProfile = () => {
    // 从本地存储恢复用户信息
    const storedProfile = uni.getStorageSync('userProfile')
    if (storedProfile) {
      userProfile.value = storedProfile
    }
  }

  const setProfileLoading = (loading) => {
    profileLoading.value = loading
  }

  const setUpdateLoading = (loading) => {
    updateLoading.value = loading
  }

  const updateRelationshipStatus = (status) => {
    userProfile.value.relationshipStatus = status
    
    // 更新本地存储
    uni.setStorageSync('userProfile', userProfile.value)
    
    // 显示状态切换提示
    const statusText = status === 'SINGLE' ? 'Single' : 'In Relationship'
    uni.showToast({
      title: `Status updated to ${statusText}`,
      icon: 'success',
      duration: 2000
    })
  }

  const updateAvatar = (avatarUrl) => {
    userProfile.value.avatarUrl = avatarUrl
    
    // 更新本地存储
    uni.setStorageSync('userProfile', userProfile.value)
  }

  return {
    // 状态
    userProfile,
    profileLoading,
    updateLoading,
    
    // 计算属性
    isSingle,
    isInRelationship,
    displayName,
    hasCompleteProfile,
    
    // 方法
    setUserProfile,
    updateUserProfile,
    clearUserProfile,
    initUserProfile,
    setProfileLoading,
    setUpdateLoading,
    updateRelationshipStatus,
    updateAvatar
  }
})