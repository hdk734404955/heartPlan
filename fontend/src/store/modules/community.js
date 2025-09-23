// 社区功能状态管理
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useCommunitySt = defineStore('community', () => {
  // 状态
  const currentTab = ref('recommended') // recommended, success-stories, help-forum, weekly-topics
  const recommendedPosts = ref([])
  const successStories = ref([])
  const helpForumPosts = ref([])
  const weeklyTopics = ref([])
  const userPosts = ref([])
  const followingUsers = ref([])
  const followers = ref([])
  
  const postsLoading = ref(false)
  const postDetailLoading = ref(false)
  const interactionLoading = ref(false)
  const refreshing = ref(false)

  // 计算属性
  const currentTabPosts = computed(() => {
    switch (currentTab.value) {
      case 'recommended':
        return recommendedPosts.value
      case 'success-stories':
        return successStories.value
      case 'help-forum':
        return helpForumPosts.value
      case 'weekly-topics':
        return weeklyTopics.value
      default:
        return []
    }
  })

  const totalUserPosts = computed(() => {
    return userPosts.value.length
  })

  const totalFollowing = computed(() => {
    return followingUsers.value.length
  })

  const totalFollowers = computed(() => {
    return followers.value.length
  })

  // 操作方法
  const setCurrentTab = (tab) => {
    currentTab.value = tab
  }

  const setRecommendedPosts = (posts) => {
    recommendedPosts.value = posts
  }

  const setSuccessStories = (stories) => {
    successStories.value = stories
  }

  const setHelpForumPosts = (posts) => {
    helpForumPosts.value = posts
  }

  const setWeeklyTopics = (topics) => {
    weeklyTopics.value = topics
  }

  const addPost = (post) => {
    // 根据帖子类型添加到相应列表
    switch (post.type) {
      case 'SUCCESS_STORY':
        successStories.value.unshift(post)
        break
      case 'HELP_REQUEST':
        helpForumPosts.value.unshift(post)
        break
      default:
        recommendedPosts.value.unshift(post)
    }
    
    // 添加到用户帖子列表
    userPosts.value.unshift(post)
  }

  const updatePost = (postId, updates) => {
    // 更新所有相关列表中的帖子
    const updatePostInArray = (array) => {
      const index = array.findIndex(post => post.id === postId)
      if (index !== -1) {
        array[index] = { ...array[index], ...updates }
      }
    }

    updatePostInArray(recommendedPosts.value)
    updatePostInArray(successStories.value)
    updatePostInArray(helpForumPosts.value)
    updatePostInArray(userPosts.value)
  }

  const deletePost = (postId) => {
    // 从所有列表中删除帖子
    recommendedPosts.value = recommendedPosts.value.filter(post => post.id !== postId)
    successStories.value = successStories.value.filter(post => post.id !== postId)
    helpForumPosts.value = helpForumPosts.value.filter(post => post.id !== postId)
    userPosts.value = userPosts.value.filter(post => post.id !== postId)
  }

  const toggleLike = (postId) => {
    const updateLikeInArray = (array) => {
      const post = array.find(p => p.id === postId)
      if (post) {
        post.isLiked = !post.isLiked
        post.likesCount += post.isLiked ? 1 : -1
      }
    }

    updateLikeInArray(recommendedPosts.value)
    updateLikeInArray(successStories.value)
    updateLikeInArray(helpForumPosts.value)
    updateLikeInArray(userPosts.value)
  }

  const addComment = (postId, comment) => {
    const updateCommentsInArray = (array) => {
      const post = array.find(p => p.id === postId)
      if (post) {
        post.commentsCount += 1
        if (!post.comments) post.comments = []
        post.comments.unshift(comment)
      }
    }

    updateCommentsInArray(recommendedPosts.value)
    updateCommentsInArray(successStories.value)
    updateCommentsInArray(helpForumPosts.value)
    updateCommentsInArray(userPosts.value)
  }

  const setFollowingUsers = (users) => {
    followingUsers.value = users
  }

  const setFollowers = (users) => {
    followers.value = users
  }

  const followUser = (userId) => {
    const user = followingUsers.value.find(u => u.id === userId)
    if (!user) {
      // 这里应该从API获取用户信息，暂时使用占位符
      followingUsers.value.push({ id: userId, isFollowing: true })
    }
  }

  const unfollowUser = (userId) => {
    followingUsers.value = followingUsers.value.filter(user => user.id !== userId)
  }

  const setPostsLoading = (loading) => {
    postsLoading.value = loading
  }

  const setPostDetailLoading = (loading) => {
    postDetailLoading.value = loading
  }

  const setInteractionLoading = (loading) => {
    interactionLoading.value = loading
  }

  const setRefreshing = (refreshing_) => {
    refreshing.value = refreshing_
  }

  const refreshCurrentTab = async () => {
    setRefreshing(true)
    // 这里应该调用API刷新当前Tab的内容
    // 暂时模拟刷新延迟
    setTimeout(() => {
      setRefreshing(false)
    }, 1000)
  }

  const clearCommunityData = () => {
    recommendedPosts.value = []
    successStories.value = []
    helpForumPosts.value = []
    weeklyTopics.value = []
    userPosts.value = []
    followingUsers.value = []
    followers.value = []
  }

  return {
    // 状态
    currentTab,
    recommendedPosts,
    successStories,
    helpForumPosts,
    weeklyTopics,
    userPosts,
    followingUsers,
    followers,
    postsLoading,
    postDetailLoading,
    interactionLoading,
    refreshing,
    
    // 计算属性
    currentTabPosts,
    totalUserPosts,
    totalFollowing,
    totalFollowers,
    
    // 方法
    setCurrentTab,
    setRecommendedPosts,
    setSuccessStories,
    setHelpForumPosts,
    setWeeklyTopics,
    addPost,
    updatePost,
    deletePost,
    toggleLike,
    addComment,
    setFollowingUsers,
    setFollowers,
    followUser,
    unfollowUser,
    setPostsLoading,
    setPostDetailLoading,
    setInteractionLoading,
    setRefreshing,
    refreshCurrentTab,
    clearCommunityData
  }
})