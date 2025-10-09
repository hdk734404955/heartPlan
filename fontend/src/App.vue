<script>
import { useAuthStore } from '@/store/modules/auth'
import { useUserStore } from '@/store/modules/user'
import { autoLoginCheck } from '@/utils/router-guard'

export default {
  onLaunch: async function () {
    console.log('HeartPlan App Launch')
    
    try {
      // 执行自动登录检查
      const isLoggedIn = await autoLoginCheck()
      
      console.log('Auto login check result:', isLoggedIn)
      
      // 初始化状态管理
      const authStore = useAuthStore()
      const userStore = useUserStore()
      
      console.log('Auth initialized:', authStore.isAuthenticated)
      console.log('User profile loaded:', userStore.hasCompleteProfile)
    } catch (error) {
      console.error('App launch error:', error)
    } finally {
      // 应用初始化完成
      console.log('App initialization completed')
    }
  },
  onShow: function () {
    console.log('HeartPlan App Show')
  },
  onHide: function () {
    console.log('HeartPlan App Hide')
  },
}
</script>

<style lang="scss">
/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
@import "uview-plus/index.scss";
@import "@/styles/global.scss";

.app {
  width: 100%;
  height: 100%;
}

.page-content {
  width: 100%;
  height: 100%;
}
.publicPage{
  width: 100%;
  height: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
</style>
