// 认证相关API
import http from './request'

export const authAPI = {
  // 用户登录
  login(credentials) {
    return http.post('/auth/login', {
      email: credentials.email,
      password: credentials.password
    })
  },

  // 用户注册 - 第一步
  registerStep1(userData) {
    return http.post('/auth/register/step1', {
      email: userData.email,
      password: userData.password,
      age: userData.age,
      gender: userData.gender
    })
  },

  // 用户注册 - 第二步
  registerStep2(userData) {
    return http.post('/auth/register/step2', {
      username: userData.username,
      relationshipStatus: userData.relationshipStatus,
      avatarUrl: userData.avatarUrl,
      location: userData.location
    })
  },

  // 完整注册（一步完成）
  register(userData) {
    return http.post('/auth/register', {
      email: userData.email,
      password: userData.password,
      username: userData.username,
      age: userData.age,
      gender: userData.gender,
      relationshipStatus: userData.relationshipStatus,
      avatarUrl: userData.avatarUrl,
      location: userData.location
    })
  },

  // 刷新访问令牌
  refreshToken(refreshToken) {
    return http.post('/auth/refresh', {
      refreshToken
    })
  },

  // 用户登出
  logout() {
    return http.post('/auth/logout')
  },

  // 验证邮箱是否已注册
  checkEmail(email) {
    return http.get('/auth/check-email', { email })
  },

  // 发送验证码（如果需要）
  sendVerificationCode(email) {
    return http.post('/auth/send-verification', { email })
  },

  // 验证验证码（如果需要）
  verifyCode(email, code) {
    return http.post('/auth/verify-code', { email, code })
  },

  // 重置密码
  resetPassword(email) {
    return http.post('/auth/reset-password', { email })
  },

  // 修改密码
  changePassword(oldPassword, newPassword) {
    return http.put('/auth/change-password', {
      oldPassword,
      newPassword
    })
  }
}