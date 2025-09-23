// HeartPlan 状态管理入口文件
import { useUserStore } from './modules/user'
import { useAuthStore } from './modules/auth'
import { useTemplateStore } from './modules/template'
import { useCommunityStore } from './modules/community'
import { useChatStore } from './modules/chat'

export {
  useUserStore,
  useAuthStore,
  useTemplateStore,
  useCommunityStore,
  useChatStore
}