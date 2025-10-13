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

  // 上传图片（通用）
  uploadImage(filePath, type = 'image') {
    console.log('API上传图片:', { filePath, type })
    return http.upload('/upload/image', filePath, { type })
  },

}