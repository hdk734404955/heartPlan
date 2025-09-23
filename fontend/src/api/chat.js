// 聊天功能相关API
import http from './request'

export const chatAPI = {
  // ========== 聊天列表管理API ==========
  
  // 获取聊天联系人列表
  getChatContacts() {
    return http.get('/chat/contacts')
  },

  // 获取与特定用户的聊天历史
  getChatHistory(userId, page = 0, size = 50, beforeMessageId = null) {
    const params = { page, size }
    if (beforeMessageId) params.beforeMessageId = beforeMessageId
    return http.get(`/chat/conversations/${userId}/messages`, params)
  },

  // 发送消息
  sendMessage(userId, messageData) {
    return http.post(`/chat/conversations/${userId}/messages`, messageData)
  },

  // 标记消息为已读
  markMessageAsRead(messageId) {
    return http.put(`/chat/messages/${messageId}/read`)
  },

  // 删除消息
  deleteMessage(messageId) {
    return http.delete(`/chat/messages/${messageId}`)
  },

  // 获取未读消息数量
  getUnreadCount() {
    return http.get('/chat/unread-count')
  },

  // ========== AI教练对话API ==========
  
  // 与AI教练开始对话
  chatWithAICoach(messageData) {
    return http.post('/chat/ai-coach/conversation', messageData)
  },

  // 获取AI教练对话历史
  getAICoachHistory(limit = 50) {
    return http.get('/chat/ai-coach/history', { limit })
  },

  // 获取AI教练建议模式
  getAICoachModes() {
    return http.get('/chat/ai-coach/modes')
  },

  // 设置AI教练对话模式
  setAICoachMode(modeData) {
    return http.post('/chat/ai-coach/mode', modeData)
  },

  // ========== 文件和媒体API ==========
  
  // 上传聊天图片
  uploadChatImage(filePath, userId) {
    return http.upload(`/chat/conversations/${userId}/upload-image`, filePath)
  },

  // 上传聊天文件
  uploadChatFile(filePath, userId) {
    return http.upload(`/chat/conversations/${userId}/upload-file`, filePath)
  },

  // 下载聊天文件
  downloadChatFile(fileId) {
    return http.download(`/chat/files/${fileId}`)
  },

  // ========== 聊天设置API ==========
  
  // 获取聊天设置
  getChatSettings() {
    return http.get('/chat/settings')
  },

  // 更新聊天设置
  updateChatSettings(settings) {
    return http.put('/chat/settings', settings)
  },

  // 屏蔽用户聊天
  blockUserChat(userId) {
    return http.post(`/chat/users/${userId}/block`)
  },

  // 取消屏蔽用户聊天
  unblockUserChat(userId) {
    return http.delete(`/chat/users/${userId}/block`)
  },

  // 获取屏蔽的聊天用户列表
  getBlockedChatUsers() {
    return http.get('/chat/blocked-users')
  },

  // ========== 聊天统计API ==========
  
  // 获取聊天统计信息
  getChatStats() {
    return http.get('/chat/stats')
  },

  // 获取AI教练使用统计
  getAICoachStats() {
    return http.get('/chat/ai-coach/stats')
  },

  // ========== 实时通信相关API ==========
  
  // 发送输入状态
  sendTypingStatus(userId, isTyping) {
    return http.post(`/chat/conversations/${userId}/typing`, {
      isTyping
    })
  },

  // 获取在线用户列表
  getOnlineUsers() {
    return http.get('/chat/online-users')
  },

  // 更新用户在线状态
  updateOnlineStatus(isOnline) {
    return http.put('/chat/online-status', {
      isOnline
    })
  }
}

// WebSocket连接管理类
export class ChatWebSocket {
  constructor() {
    this.socket = null
    this.isConnected = false
    this.reconnectAttempts = 0
    this.maxReconnectAttempts = 5
    this.reconnectInterval = 3000
    this.messageHandlers = new Map()
  }

  // 连接WebSocket
  connect(token) {
    const wsUrl = `ws://localhost:8080/ws/chat?token=${token}`
    
    this.socket = uni.connectSocket({
      url: wsUrl,
      success: () => {
        console.log('WebSocket connecting...')
      }
    })

    this.socket.onOpen(() => {
      console.log('WebSocket connected')
      this.isConnected = true
      this.reconnectAttempts = 0
    })

    this.socket.onMessage((event) => {
      try {
        const message = JSON.parse(event.data)
        this.handleMessage(message)
      } catch (error) {
        console.error('WebSocket message parse error:', error)
      }
    })

    this.socket.onClose(() => {
      console.log('WebSocket disconnected')
      this.isConnected = false
      this.attemptReconnect(token)
    })

    this.socket.onError((error) => {
      console.error('WebSocket error:', error)
      this.isConnected = false
    })
  }

  // 断开连接
  disconnect() {
    if (this.socket) {
      this.socket.close()
      this.socket = null
      this.isConnected = false
    }
  }

  // 发送消息
  sendMessage(message) {
    if (this.isConnected && this.socket) {
      this.socket.send({
        data: JSON.stringify(message)
      })
    } else {
      console.error('WebSocket not connected')
    }
  }

  // 处理接收到的消息
  handleMessage(message) {
    const handler = this.messageHandlers.get(message.type)
    if (handler) {
      handler(message)
    }
  }

  // 注册消息处理器
  onMessage(type, handler) {
    this.messageHandlers.set(type, handler)
  }

  // 移除消息处理器
  offMessage(type) {
    this.messageHandlers.delete(type)
  }

  // 尝试重连
  attemptReconnect(token) {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++
      console.log(`Attempting to reconnect... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`)
      
      setTimeout(() => {
        this.connect(token)
      }, this.reconnectInterval)
    } else {
      console.error('Max reconnection attempts reached')
    }
  }
}

// 创建WebSocket实例
export const chatWebSocket = new ChatWebSocket()