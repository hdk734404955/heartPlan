// 聊天功能状态管理
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useChatStore = defineStore('chat', () => {
  // 状态
  const contacts = ref([])
  const currentChatUserId = ref(null)
  const messages = ref({}) // 按用户ID存储消息 { userId: [messages] }
  const aiCoachMessages = ref([])
  const unreadCounts = ref({}) // 按用户ID存储未读数量 { userId: count }
  const onlineUsers = ref(new Set()) // 在线用户ID集合
  const typingUsers = ref(new Set()) // 正在输入的用户ID集合
  
  const chatLoading = ref(false)
  const messageLoading = ref(false)
  const aiCoachLoading = ref(false)
  const sendingMessage = ref(false)

  // 计算属性
  const totalUnreadCount = computed(() => {
    return Object.values(unreadCounts.value).reduce((total, count) => total + count, 0)
  })

  const currentChatMessages = computed(() => {
    if (!currentChatUserId.value) return []
    return messages.value[currentChatUserId.value] || []
  })

  const sortedContacts = computed(() => {
    return [...contacts.value].sort((a, b) => {
      // AI教练始终在顶部
      if (a.isAICoach) return -1
      if (b.isAICoach) return 1
      
      // 按最后消息时间排序
      const aTime = new Date(a.lastMessageTime || 0)
      const bTime = new Date(b.lastMessageTime || 0)
      return bTime - aTime
    })
  })

  const aiCoachContact = computed(() => {
    return contacts.value.find(contact => contact.isAICoach)
  })

  // 操作方法
  const setContacts = (contactList) => {
    contacts.value = contactList
  }

  const addContact = (contact) => {
    const existingIndex = contacts.value.findIndex(c => c.id === contact.id)
    if (existingIndex !== -1) {
      contacts.value[existingIndex] = { ...contacts.value[existingIndex], ...contact }
    } else {
      contacts.value.push(contact)
    }
  }

  const updateContact = (userId, updates) => {
    const index = contacts.value.findIndex(contact => contact.id === userId)
    if (index !== -1) {
      contacts.value[index] = { ...contacts.value[index], ...updates }
    }
  }

  const setCurrentChatUser = (userId) => {
    currentChatUserId.value = userId
    
    // 清除该用户的未读数量
    if (unreadCounts.value[userId]) {
      unreadCounts.value[userId] = 0
    }
  }

  const setMessages = (userId, messageList) => {
    if (!messages.value[userId]) {
      messages.value[userId] = []
    }
    messages.value[userId] = messageList
  }

  const addMessage = (userId, message) => {
    if (!messages.value[userId]) {
      messages.value[userId] = []
    }
    messages.value[userId].push(message)
    
    // 更新联系人最后消息信息
    updateContact(userId, {
      lastMessage: message.content,
      lastMessageTime: message.createdAt
    })
    
    // 如果不是当前聊天用户，增加未读数量
    if (currentChatUserId.value !== userId && !message.isSentByMe) {
      if (!unreadCounts.value[userId]) {
        unreadCounts.value[userId] = 0
      }
      unreadCounts.value[userId]++
    }
  }

  const updateMessage = (userId, messageId, updates) => {
    if (messages.value[userId]) {
      const index = messages.value[userId].findIndex(msg => msg.id === messageId)
      if (index !== -1) {
        messages.value[userId][index] = { ...messages.value[userId][index], ...updates }
      }
    }
  }

  const deleteMessage = (userId, messageId) => {
    if (messages.value[userId]) {
      messages.value[userId] = messages.value[userId].filter(msg => msg.id !== messageId)
    }
  }

  const setAiCoachMessages = (messageList) => {
    aiCoachMessages.value = messageList
  }

  const addAiCoachMessage = (message) => {
    aiCoachMessages.value.push(message)
  }

  const setUnreadCount = (userId, count) => {
    unreadCounts.value[userId] = count
  }

  const clearUnreadCount = (userId) => {
    unreadCounts.value[userId] = 0
  }

  const setUserOnline = (userId) => {
    onlineUsers.value.add(userId)
  }

  const setUserOffline = (userId) => {
    onlineUsers.value.delete(userId)
  }

  const isUserOnline = (userId) => {
    return onlineUsers.value.has(userId)
  }

  const setUserTyping = (userId) => {
    typingUsers.value.add(userId)
    
    // 3秒后自动清除输入状态
    setTimeout(() => {
      typingUsers.value.delete(userId)
    }, 3000)
  }

  const clearUserTyping = (userId) => {
    typingUsers.value.delete(userId)
  }

  const isUserTyping = (userId) => {
    return typingUsers.value.has(userId)
  }

  const setChatLoading = (loading) => {
    chatLoading.value = loading
  }

  const setMessageLoading = (loading) => {
    messageLoading.value = loading
  }

  const setAiCoachLoading = (loading) => {
    aiCoachLoading.value = loading
  }

  const setSendingMessage = (sending) => {
    sendingMessage.value = sending
  }

  const markMessageAsRead = (userId, messageId) => {
    updateMessage(userId, messageId, { isRead: true })
  }

  const markAllMessagesAsRead = (userId) => {
    if (messages.value[userId]) {
      messages.value[userId].forEach(message => {
        if (!message.isSentByMe) {
          message.isRead = true
        }
      })
    }
    clearUnreadCount(userId)
  }

  const clearChatData = () => {
    contacts.value = []
    currentChatUserId.value = null
    messages.value = {}
    aiCoachMessages.value = []
    unreadCounts.value = {}
    onlineUsers.value.clear()
    typingUsers.value.clear()
  }

  const initAICoach = () => {
    // 初始化AI教练联系人
    const aiCoach = {
      id: 'ai-coach',
      name: 'AI Coach',
      avatar: '/static/ai-coach-avatar.png',
      isAICoach: true,
      isOnline: true,
      lastMessage: 'Hi! I\'m here to help with your relationship journey.',
      lastMessageTime: new Date().toISOString()
    }
    
    addContact(aiCoach)
  }

  return {
    // 状态
    contacts,
    currentChatUserId,
    messages,
    aiCoachMessages,
    unreadCounts,
    onlineUsers,
    typingUsers,
    chatLoading,
    messageLoading,
    aiCoachLoading,
    sendingMessage,
    
    // 计算属性
    totalUnreadCount,
    currentChatMessages,
    sortedContacts,
    aiCoachContact,
    
    // 方法
    setContacts,
    addContact,
    updateContact,
    setCurrentChatUser,
    setMessages,
    addMessage,
    updateMessage,
    deleteMessage,
    setAiCoachMessages,
    addAiCoachMessage,
    setUnreadCount,
    clearUnreadCount,
    setUserOnline,
    setUserOffline,
    isUserOnline,
    setUserTyping,
    clearUserTyping,
    isUserTyping,
    setChatLoading,
    setMessageLoading,
    setAiCoachLoading,
    setSendingMessage,
    markMessageAsRead,
    markAllMessagesAsRead,
    clearChatData,
    initAICoach
  }
})