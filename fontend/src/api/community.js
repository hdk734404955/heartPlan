// 社区功能相关API
import http from './request'

export const communityAPI = {
  // ========== 帖子管理API ==========
  
  // 获取社区帖子列表
  getPosts(page = 0, size = 20, type = null, sortBy = 'latest') {
    const params = { page, size, sortBy }
    if (type) params.type = type
    return http.get('/community/posts', params)
  },

  // 获取单个帖子详情
  getPost(id) {
    return http.get(`/community/posts/${id}`)
  },

  // 发布新帖子
  createPost(postData) {
    return http.post('/community/posts', postData)
  },

  // 更新帖子
  updatePost(id, postData) {
    return http.put(`/community/posts/${id}`, postData)
  },

  // 删除帖子
  deletePost(id) {
    return http.delete(`/community/posts/${id}`)
  },

  // ========== 互动功能API ==========
  
  // 点赞/取消点赞帖子
  toggleLike(id) {
    return http.post(`/community/posts/${id}/like`)
  },

  // 收藏/取消收藏帖子
  toggleFavorite(id) {
    return http.post(`/community/posts/${id}/favorite`)
  },

  // 分享帖子
  sharePost(id) {
    return http.post(`/community/posts/${id}/share`)
  },

  // 举报帖子
  reportPost(id, reason, description) {
    return http.post(`/community/posts/${id}/report`, {
      reason,
      description
    })
  },

  // ========== 评论系统API ==========
  
  // 获取帖子评论列表
  getComments(postId, page = 0, size = 10) {
    return http.get(`/community/posts/${postId}/comments`, { page, size })
  },

  // 发表评论
  createComment(postId, content) {
    return http.post(`/community/posts/${postId}/comments`, {
      content
    })
  },

  // 回复评论
  replyToComment(commentId, content) {
    return http.post(`/community/comments/${commentId}/replies`, {
      content
    })
  },

  // 点赞评论
  likeComment(id) {
    return http.post(`/community/comments/${id}/like`)
  },

  // 删除评论
  deleteComment(id) {
    return http.delete(`/community/comments/${id}`)
  },

  // ========== 用户关系API ==========
  
  // 关注用户
  followUser(userId) {
    return http.post(`/community/users/${userId}/follow`)
  },

  // 取消关注用户
  unfollowUser(userId) {
    return http.delete(`/community/users/${userId}/follow`)
  },

  // 获取关注列表
  getFollowing(userId, page = 0, size = 20) {
    return http.get(`/community/users/${userId}/following`, { page, size })
  },

  // 获取粉丝列表
  getFollowers(userId, page = 0, size = 20) {
    return http.get(`/community/users/${userId}/followers`, { page, size })
  },

  // ========== Tab导航和推荐API ==========
  
  // 获取推荐Tab内容（默认首页）
  getRecommendations(page = 0, size = 20, refreshToken = null) {
    const params = { page, size }
    if (refreshToken) params.refreshToken = refreshToken
    return http.get('/community/recommendations', params)
  },

  // 刷新推荐内容
  refreshRecommendations() {
    return http.post('/community/recommendations/refresh')
  },

  // 获取成功故事Tab内容
  getSuccessStories(page = 0, size = 20, filterBy = null) {
    const params = { page, size }
    if (filterBy) params.filterBy = filterBy
    return http.get('/community/success-stories', params)
  },

  // 获取问题求助Tab内容
  getHelpForumPosts(page = 0, size = 20, status = null) {
    const params = { page, size }
    if (status) params.status = status
    return http.get('/community/help-forum', params)
  },

  // 获取每周话题Tab内容
  getWeeklyTopics(page = 0, size = 20) {
    return http.get('/community/weekly-topics', { page, size })
  },

  // 搜索帖子（跨所有Tab）
  searchPosts(keyword, page = 0, size = 20, tabType = null) {
    const params = { keyword, page, size }
    if (tabType) params.tabType = tabType
    return http.get('/community/search/posts', params)
  },

  // 获取热门话题标签
  getTrendingTopics() {
    return http.get('/community/trending/topics')
  },

  // 记录用户互动行为（用于推荐算法优化）
  recordUserInteraction(interactionData) {
    return http.post('/community/interactions', interactionData)
  },

  // ========== 用户内容管理API ==========
  
  // 获取用户发布的帖子
  getUserPosts(userId, page = 0, size = 20) {
    return http.get(`/community/users/${userId}/posts`, { page, size })
  },

  // 获取用户点赞的帖子
  getUserLikedPosts(page = 0, size = 20) {
    return http.get('/community/users/liked-posts', { page, size })
  },

  // 获取用户收藏的帖子
  getUserFavoritePosts(page = 0, size = 20) {
    return http.get('/community/users/favorite-posts', { page, size })
  },

  // 获取用户评论历史
  getUserComments(page = 0, size = 20) {
    return http.get('/community/users/comments', { page, size })
  }
}