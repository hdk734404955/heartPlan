// AI智能模板相关API
import http from './request'

export const templateAPI = {
  // ========== 单身用户模板API ==========
  
  // 获取约会管理记录列表
  getDatingRecords(page = 0, size = 10, status = null) {
    const params = { page, size }
    if (status) params.status = status
    return http.get('/templates/dating-management', params)
  },

  // 创建约会记录
  createDatingRecord(recordData) {
    return http.post('/templates/dating-management', recordData)
  },

  // 更新约会记录
  updateDatingRecord(id, recordData) {
    return http.put(`/templates/dating-management/${id}`, recordData)
  },

  // 删除约会记录
  deleteDatingRecord(id) {
    return http.delete(`/templates/dating-management/${id}`)
  },

  // 获取AI兼容性评分
  calculateCompatibilityScore(id, requestData) {
    return http.post(`/templates/dating-management/${id}/compatibility-score`, requestData)
  },

  // 获取约会记录追踪器数据
  getDateTrackingRecords() {
    return http.get('/templates/dating-tracker')
  },

  // 创建约会追踪记录
  createDateTrackingRecord(recordData) {
    return http.post('/templates/dating-tracker', recordData)
  },

  // 获取AI约会分析和建议
  getDateAnalysis(id) {
    return http.get(`/templates/dating-tracker/${id}/analysis`)
  },

  // 获取个人魅力提升清单
  getCharmEnhancementList() {
    return http.get('/templates/charm-enhancement')
  },

  // 更新魅力提升项目完成状态
  completeCharmEnhancementItem(itemId) {
    return http.put(`/templates/charm-enhancement/${itemId}/complete`)
  },

  // 获取AI生成的魅力提升建议
  getCharmSuggestions(requestData) {
    return http.post('/templates/charm-enhancement/suggestions', requestData)
  },

  // ========== 情侣用户模板API ==========
  
  // 获取关系管理仪表板
  getRelationshipDashboard() {
    return http.get('/templates/relationship-dashboard')
  },

  // 更新关系健康评分
  updateHealthScore(requestData) {
    return http.post('/templates/relationship-dashboard/health-score', requestData)
  },

  // 获取关系趋势分析
  getRelationshipTrends(days = 30) {
    return http.get('/templates/relationship-dashboard/trends', { days })
  },

  // 获取重要日期提醒列表
  getImportantDates() {
    return http.get('/templates/important-dates')
  },

  // 添加重要日期
  addImportantDate(dateData) {
    return http.post('/templates/important-dates', dateData)
  },

  // 更新重要日期
  updateImportantDate(id, dateData) {
    return http.put(`/templates/important-dates/${id}`, dateData)
  },

  // 删除重要日期
  deleteImportantDate(id) {
    return http.delete(`/templates/important-dates/${id}`)
  },

  // 获取AI庆祝建议
  getCelebrationSuggestions(id) {
    return http.get(`/templates/important-dates/${id}/celebration-suggestions`)
  },

  // 获取冲突解决记录
  getConflictRecords() {
    return http.get('/templates/conflict-resolution')
  },

  // 记录冲突解决
  recordConflict(conflictData) {
    return http.post('/templates/conflict-resolution', conflictData)
  },

  // 获取AI冲突分析和预防建议
  getConflictAnalysis(id) {
    return http.get(`/templates/conflict-resolution/${id}/analysis`)
  },

  // 获取恋爱成长轨迹图数据
  getGrowthTrajectory(days = 365) {
    return http.get('/templates/growth-trajectory', { days })
  },

  // 更新成长轨迹数据点
  addGrowthDataPoint(dataPointData) {
    return http.post('/templates/growth-trajectory/data-point', dataPointData)
  },

  // ========== 通用模板API ==========
  
  // 获取模板使用统计
  getTemplateStats() {
    return http.get('/templates/stats')
  },

  // 获取AI分析历史
  getAIAnalysisHistory(page = 0, size = 20) {
    return http.get('/templates/ai-analysis-history', { page, size })
  },

  // 导出模板数据
  exportTemplateData(templateType) {
    return http.get(`/templates/export/${templateType}`)
  }
}