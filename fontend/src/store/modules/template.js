// AI智能模板状态管理
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useTemplateStore = defineStore('template', () => {
  // 状态
  const datingRecords = ref([])
  const relationshipDashboard = ref({
    healthScore: 0,
    communicationQuality: 0,
    trends: [],
    alerts: []
  })
  const importantDates = ref([])
  const conflictRecords = ref([])
  const charmEnhancementList = ref([])
  const growthTrajectory = ref([])
  
  const templatesLoading = ref(false)
  const aiAnalysisLoading = ref(false)

  // 计算属性
  const totalDatingRecords = computed(() => {
    return datingRecords.value.length
  })

  const averageCompatibilityScore = computed(() => {
    if (datingRecords.value.length === 0) return 0
    const total = datingRecords.value.reduce((sum, record) => sum + (record.compatibilityScore || 0), 0)
    return Math.round(total / datingRecords.value.length)
  })

  const upcomingImportantDates = computed(() => {
    const now = new Date()
    return importantDates.value.filter(date => new Date(date.date) > now)
      .sort((a, b) => new Date(a.date) - new Date(b.date))
      .slice(0, 3)
  })

  const charmEnhancementProgress = computed(() => {
    if (charmEnhancementList.value.length === 0) return 0
    const completed = charmEnhancementList.value.filter(item => item.completed).length
    return Math.round((completed / charmEnhancementList.value.length) * 100)
  })

  // 操作方法
  const setDatingRecords = (records) => {
    datingRecords.value = records
  }

  const addDatingRecord = (record) => {
    datingRecords.value.unshift(record)
  }

  const updateDatingRecord = (id, updates) => {
    const index = datingRecords.value.findIndex(record => record.id === id)
    if (index !== -1) {
      datingRecords.value[index] = { ...datingRecords.value[index], ...updates }
    }
  }

  const deleteDatingRecord = (id) => {
    datingRecords.value = datingRecords.value.filter(record => record.id !== id)
  }

  const setRelationshipDashboard = (dashboard) => {
    relationshipDashboard.value = dashboard
  }

  const updateHealthScore = (score) => {
    relationshipDashboard.value.healthScore = score
  }

  const addAlert = (alert) => {
    relationshipDashboard.value.alerts.unshift(alert)
  }

  const setImportantDates = (dates) => {
    importantDates.value = dates
  }

  const addImportantDate = (date) => {
    importantDates.value.push(date)
  }

  const updateImportantDate = (id, updates) => {
    const index = importantDates.value.findIndex(date => date.id === id)
    if (index !== -1) {
      importantDates.value[index] = { ...importantDates.value[index], ...updates }
    }
  }

  const deleteImportantDate = (id) => {
    importantDates.value = importantDates.value.filter(date => date.id !== id)
  }

  const setConflictRecords = (records) => {
    conflictRecords.value = records
  }

  const addConflictRecord = (record) => {
    conflictRecords.value.unshift(record)
  }

  const setCharmEnhancementList = (list) => {
    charmEnhancementList.value = list
  }

  const updateCharmEnhancementItem = (id, completed) => {
    const index = charmEnhancementList.value.findIndex(item => item.id === id)
    if (index !== -1) {
      charmEnhancementList.value[index].completed = completed
    }
  }

  const setGrowthTrajectory = (trajectory) => {
    growthTrajectory.value = trajectory
  }

  const addGrowthDataPoint = (dataPoint) => {
    growthTrajectory.value.push(dataPoint)
  }

  const setTemplatesLoading = (loading) => {
    templatesLoading.value = loading
  }

  const setAiAnalysisLoading = (loading) => {
    aiAnalysisLoading.value = loading
  }

  const clearTemplateData = () => {
    datingRecords.value = []
    relationshipDashboard.value = {
      healthScore: 0,
      communicationQuality: 0,
      trends: [],
      alerts: []
    }
    importantDates.value = []
    conflictRecords.value = []
    charmEnhancementList.value = []
    growthTrajectory.value = []
  }

  return {
    // 状态
    datingRecords,
    relationshipDashboard,
    importantDates,
    conflictRecords,
    charmEnhancementList,
    growthTrajectory,
    templatesLoading,
    aiAnalysisLoading,
    
    // 计算属性
    totalDatingRecords,
    averageCompatibilityScore,
    upcomingImportantDates,
    charmEnhancementProgress,
    
    // 方法
    setDatingRecords,
    addDatingRecord,
    updateDatingRecord,
    deleteDatingRecord,
    setRelationshipDashboard,
    updateHealthScore,
    addAlert,
    setImportantDates,
    addImportantDate,
    updateImportantDate,
    deleteImportantDate,
    setConflictRecords,
    addConflictRecord,
    setCharmEnhancementList,
    updateCharmEnhancementItem,
    setGrowthTrajectory,
    addGrowthDataPoint,
    setTemplatesLoading,
    setAiAnalysisLoading,
    clearTemplateData
  }
})