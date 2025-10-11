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
    introduction: '',
    bgcMainColor: '',
    bgcUrl: '',
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
      introduction: '',
      bgcMainColor: '',
      bgcUrl: '',
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

  const fetchUserProfile = async () => {
    try {
      setProfileLoading(true)
      
      // 动态导入API
      const { userAPI } = await import('@/api/user')
      
      // 从服务器获取最新用户信息
      const profile = await userAPI.getProfile()
      
      // 更新store中的用户信息
      setUserProfile(profile)
      
      console.log('用户信息获取成功:', profile)
      return profile
    } catch (error) {
      console.error('获取用户信息失败:', error)
      throw error
    } finally {
      setProfileLoading(false)
    }
  }

  const saveUserProfile = async (profileData) => {
    try {
      setUpdateLoading(true)
      
      // 动态导入API
      const { userAPI } = await import('@/api/user')
      
      // 保存到服务器
      const updatedProfile = await userAPI.updateProfile(profileData)
      
      // 更新store中的用户信息
      setUserProfile(updatedProfile)
      
      uni.showToast({
        title: 'Profile updated successfully',
        icon: 'success',
        duration: 2000
      })
      
      console.log('用户信息保存成功:', updatedProfile)
      return updatedProfile
    } catch (error) {
      console.error('保存用户信息失败:', error)
      uni.showToast({
        title: 'Failed to update profile',
        icon: 'none',
        duration: 2000
      })
      throw error
    } finally {
      setUpdateLoading(false)
    }
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
    updateAvatar,
    saveUserProfile
  }
})