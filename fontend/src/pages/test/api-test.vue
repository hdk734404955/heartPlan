<template>
  <view class="api-test-container">
    <view class="header">
      <u-navbar
        title="API Connection Test"
        border="none"
        bg-color="linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%)"
        title-color="#FFFFFF"
        :safe-area-inset-top="true"
        left-icon="arrow-left"
        @left-click="goBack"
      ></u-navbar>
    </view>
    
    <view class="content">
      <view class="test-section">
        <u-card
          :show-head="false"
          border="none"
          :box-shadow="true"
          margin="0 0 20px 0"
        >
          <view class="test-item">
            <view class="test-title">Backend Connection Status</view>
            <view class="test-status" :class="connectionStatus.class">
              {{ connectionStatus.text }}
            </view>
            <u-button
              text="Test Connection"
              type="primary"
              :custom-style="buttonStyle"
              :loading="testing"
              @click="testConnection"
            ></u-button>
          </view>
        </u-card>
        
        <u-card
          :show-head="false"
          border="none"
          :box-shadow="true"
          margin="0 0 20px 0"
        >
          <view class="test-item">
            <view class="test-title">API Configuration</view>
            <view class="config-info">
              <view class="config-item">
                <text class="config-label">Base URL:</text>
                <text class="config-value">{{ apiConfig.BASE_URL }}</text>
              </view>
              <view class="config-item">
                <text class="config-label">Timeout:</text>
                <text class="config-value">{{ apiConfig.TIMEOUT }}ms</text>
              </view>
            </view>
          </view>
        </u-card>
        
        <u-card
          :show-head="false"
          border="none"
          :box-shadow="true"
          margin="0 0 20px 0"
          v-if="testResults.length > 0"
        >
          <view class="test-item">
            <view class="test-title">Test Results</view>
            <view class="results-list">
              <view 
                v-for="(result, index) in testResults" 
                :key="index"
                class="result-item"
                :class="result.success ? 'success' : 'error'"
              >
                <view class="result-time">{{ result.time }}</view>
                <view class="result-message">{{ result.message }}</view>
              </view>
            </view>
          </view>
        </u-card>
      </view>
    </view>
  </view>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { API_CONFIG } from '@/api/config'
import http from '@/api/request'

export default {
  name: 'ApiTestPage',
  setup() {
    // 响应式数据
    const testing = ref(false)
    const connectionStatus = ref({
      text: 'Not tested',
      class: 'status-unknown'
    })
    const testResults = ref([])
    
    // 计算属性
    const apiConfig = computed(() => API_CONFIG)
    
    const buttonStyle = computed(() => ({
      background: 'linear-gradient(135deg, #FF6B6B 0%, #FF8E8E 100%)',
      borderRadius: '24px',
      height: '44px',
      border: 'none'
    }))
    
    // 方法
    const goBack = () => {
      uni.navigateBack()
    }
    
    const addTestResult = (message, success = true) => {
      const now = new Date()
      const time = `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}:${now.getSeconds().toString().padStart(2, '0')}`
      
      testResults.value.unshift({
        time,
        message,
        success
      })
      
      // 只保留最近10条记录
      if (testResults.value.length > 10) {
        testResults.value = testResults.value.slice(0, 10)
      }
    }
    
    const testConnection = async () => {
      testing.value = true
      
      try {
        // 测试基础连接
        addTestResult('Testing backend connection...', true)
        
        const response = await http.get('/test/health')
        
        if (response && (response.status === 'UP' || response.message)) {
          connectionStatus.value = {
            text: 'Connected',
            class: 'status-success'
          }
          addTestResult(`Backend connection successful: ${response.message || 'Health check passed'}`, true)
          
          // 测试CORS配置
          try {
            const corsResponse = await http.get('/test/cors')
            if (corsResponse && corsResponse.cors === 'enabled') {
              addTestResult('CORS configuration verified', true)
            }
          } catch (corsError) {
            addTestResult('CORS test failed, but basic connection works', false)
          }
        } else {
          connectionStatus.value = {
            text: 'Connection failed',
            class: 'status-error'
          }
          addTestResult('Backend returned unexpected response', false)
        }
      } catch (error) {
        console.error('Connection test failed:', error)
        
        connectionStatus.value = {
          text: 'Connection failed',
          class: 'status-error'
        }
        
        if (error.errMsg && error.errMsg.includes('timeout')) {
          addTestResult('Connection timeout - check if backend is running', false)
        } else if (error.errMsg && error.errMsg.includes('fail')) {
          addTestResult('Network error - check CORS configuration', false)
        } else {
          addTestResult(`Connection error: ${error.message || 'Unknown error'}`, false)
        }
      } finally {
        testing.value = false
      }
    }
    
    // 生命周期
    onMounted(() => {
      addTestResult('API test page loaded', true)
    })
    
    return {
      // 响应式数据
      testing,
      connectionStatus,
      testResults,
      
      // 计算属性
      apiConfig,
      buttonStyle,
      
      // 方法
      goBack,
      testConnection
    }
  }
}
</script>

<style lang="scss" scoped>
.api-test-container {
  width: 100vw;
  min-height: 100vh;
  background: linear-gradient(135deg, #FFF5F5 0%, #FEFEFE 100%);
  box-sizing: border-box;
}

.content {
  padding: 20px;
}

.test-section {
  .test-item {
    padding: 20px;
    
    .test-title {
      font-size: 18px;
      font-weight: 600;
      color: #2C3E50;
      margin-bottom: 16px;
    }
    
    .test-status {
      font-size: 16px;
      font-weight: 500;
      margin-bottom: 20px;
      padding: 8px 16px;
      border-radius: 20px;
      text-align: center;
      
      &.status-unknown {
        background: #E5E7EB;
        color: #6B7280;
      }
      
      &.status-success {
        background: #D1FAE5;
        color: #065F46;
      }
      
      &.status-error {
        background: #FEE2E2;
        color: #991B1B;
      }
    }
  }
}

.config-info {
  .config-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    
    .config-label {
      font-size: 14px;
      color: #7F8C8D;
      font-weight: 500;
    }
    
    .config-value {
      font-size: 14px;
      color: #2C3E50;
      font-family: monospace;
    }
  }
}

.results-list {
  .result-item {
    padding: 12px;
    border-radius: 8px;
    margin-bottom: 8px;
    
    &.success {
      background: #F0FDF4;
      border-left: 4px solid #22C55E;
    }
    
    &.error {
      background: #FEF2F2;
      border-left: 4px solid #EF4444;
    }
    
    .result-time {
      font-size: 12px;
      color: #6B7280;
      margin-bottom: 4px;
    }
    
    .result-message {
      font-size: 14px;
      color: #374151;
    }
  }
}
</style>