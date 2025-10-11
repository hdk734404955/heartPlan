// 用户相关API
import http from './request'

export const userAPI = {
  // 获取当前用户资料
  getProfile() {
    return http.get('/user/profile')
  },

  // 更新当前用户资料
  updateProfile(profileData) {
    return http.put('/user/profile', profileData)
  },

  // 检查用户名可用性
  checkUsernameAvailability(username) {
    return http.get('/user/check-username', { username })
  },

  // 获取指定用户的公开信息
  getUserById(userId) {
    return http.get(`/user/${userId}`)
  },



  // 更新恋爱状态
  updateRelationshipStatus(status) {
    return http.put('/users/relationship-status', {
      status
    })
  },

  // 获取用户洞察数据
  getUserInsights() {
    return http.get('/users/insights')
  },

  // 上传头像
  uploadAvatar(filePath) {
    return http.upload('/users/avatar', filePath)
  },

  // 获取用户活动记录
  getUserActivity(page = 0, size = 20) {
    return http.get('/users/activity', { page, size })
  },

  // 获取关注列表
  getFollowing(page = 0, size = 20) {
    return http.get('/users/following', { page, size })
  },

  // 获取粉丝列表
  getFollowers(page = 0, size = 20) {
    return http.get('/users/followers', { page, size })
  },

  // 更新隐私设置
  updatePrivacySettings(settings) {
    return http.put('/users/privacy-settings', settings)
  },

  // 更新用户偏好
  updatePreferences(preferences) {
    return http.put('/users/preferences', preferences)
  },

  // 导出用户数据
  exportUserData() {
    return http.get('/users/export-data')
  },

  // 注销账户
  deleteAccount(password) {
    return http.delete('/users/account', {
      data: { password }
    })
  },

  // 获取用户统计信息
  getUserStats() {
    return http.get('/users/stats')
  },

  // 搜索用户
  searchUsers(keyword, page = 0, size = 20) {
    return http.get('/users/search', { keyword, page, size })
  },

  // 获取用户详情（其他用户）
  getUserDetail(userId) {
    return http.get(`/users/${userId}`)
  },

  // 举报用户
  reportUser(userId, reason, description) {
    return http.post(`/users/${userId}/report`, {
      reason,
      description
    })
  },

  // 屏蔽用户
  blockUser(userId) {
    return http.post(`/users/${userId}/block`)
  },

  // 取消屏蔽用户
  unblockUser(userId) {
    return http.delete(`/users/${userId}/block`)
  },

  // 获取屏蔽列表
  getBlockedUsers(page = 0, size = 20) {
    return http.get('/users/blocked', { page, size })
  }
}