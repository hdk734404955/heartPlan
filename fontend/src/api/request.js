/**
 * HTTP请求工具类
 * 
 * 优化说明：
 * - 统一HTTP请求处理逻辑，支持统一ApiResponse格式
 * - 简化请求方法实现，减少重复代码
 * - 提供清晰的拦截器配置和错误处理机制
 */
import { requestInterceptor, responseInterceptor, errorHandler } from './config'

class HttpRequest {
  constructor() {
    // 配置统一的拦截器，实现请求/响应的统一处理
    this.interceptors = {
      request: requestInterceptor,    // 请求拦截器：添加认证头、基础URL等
      response: responseInterceptor,  // 响应拦截器：处理ApiResponse格式转换
      error: errorHandler            // 错误处理器：统一网络错误处理
    }
  }

  /**
   * 通用请求方法
   * 
   * 优化说明：
   * - 统一请求处理流程，应用所有拦截器
   * - 简化错误处理逻辑，依赖拦截器统一处理
   * - 支持统一ApiResponse格式的自动解析
   * 
   * @param {Object} options - 请求配置选项
   * @returns {Promise} 请求Promise，返回处理后的数据
   */
  request(options) {
    return new Promise((resolve, reject) => {
      // 应用请求拦截器：添加认证头、基础URL等配置
      const config = this.interceptors.request(options)
      
      uni.request({
        ...config,
        success: (response) => {
          try {
            // 应用响应拦截器：处理ApiResponse格式转换和错误处理
            const result = this.interceptors.response(response)
            resolve(result)
          } catch (error) {
            reject(error)
          }
        },
        fail: (error) => {
          // 应用错误处理器：统一网络错误处理和用户提示
          this.interceptors.error(error)
          reject(error)
        }
      })
    })
  }

  /**
   * GET请求方法
   * 
   * 优化说明：
   * - 简化查询参数构建逻辑，自动URL编码
   * - 统一请求配置合并，支持自定义选项
   * 
   * @param {string} url - 请求URL
   * @param {Object} params - 查询参数对象
   * @param {Object} options - 额外的请求选项
   * @returns {Promise} 请求Promise
   */
  get(url, params = {}, options = {}) {
    // 构建查询字符串，自动进行URL编码
    const queryString = Object.keys(params)
      .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
      .join('&')
    
    const fullUrl = queryString ? `${url}?${queryString}` : url
    
    return this.request({
      url: fullUrl,
      method: 'GET',
      ...options
    })
  }

  /**
   * POST请求方法
   * 
   * @param {string} url - 请求URL
   * @param {Object} data - 请求数据
   * @param {Object} options - 额外的请求选项
   * @returns {Promise} 请求Promise
   */
  post(url, data = {}, options = {}) {
    return this.request({
      url,
      method: 'POST',
      data,
      ...options
    })
  }

  /**
   * PUT请求方法
   * 
   * @param {string} url - 请求URL
   * @param {Object} data - 请求数据
   * @param {Object} options - 额外的请求选项
   * @returns {Promise} 请求Promise
   */
  put(url, data = {}, options = {}) {
    return this.request({
      url,
      method: 'PUT',
      data,
      ...options
    })
  }

  /**
   * DELETE请求方法
   * 
   * @param {string} url - 请求URL
   * @param {Object} options - 额外的请求选项
   * @returns {Promise} 请求Promise
   */
  delete(url, options = {}) {
    return this.request({
      url,
      method: 'DELETE',
      ...options
    })
  }

  /**
   * 文件上传方法
   * 
   * 优化说明：
   * - 统一文件上传响应处理，支持ApiResponse格式
   * - 简化响应解析逻辑，减少重复代码
   * - 自动处理Content-Type，避免上传冲突
   * 
   * @param {string} url - 上传URL
   * @param {string} filePath - 文件路径
   * @param {Object} formData - 表单数据
   * @param {Object} options - 上传选项
   * @returns {Promise} 上传Promise
   */
  upload(url, filePath, formData = {}, options = {}) {
    return new Promise((resolve, reject) => {
      // 应用请求拦截器获取认证头，标记为上传请求
      const config = this.interceptors.request({ 
        url, 
        method: 'POST',
        header: {}, 
        isUpload: true 
      })
      
      // 移除Content-Type，让uni.uploadFile自动设置multipart/form-data
      if (config.header['Content-Type']) {
        delete config.header['Content-Type']
      }
      
      uni.uploadFile({
        url: config.url,
        filePath,
        name: options.name || 'file',
        formData,
        header: config.header,
        success: (response) => {
          try {
            // 解析JSON响应数据
            const data = JSON.parse(response.data)
            
            // 检查是否为统一ApiResponse格式
            if (data && typeof data === 'object' && 'code' in data) {
              if (data.code >= 200 && data.code < 300) {
                // 成功响应：返回data字段
                resolve(data.data)
              } else {
                // 错误响应：使用message字段
                reject(new Error(data.message || 'Upload failed'))
              }
            } else {
              // 向后兼容：支持旧格式响应
              resolve(data)
            }
          } catch (error) {
            reject(new Error('Upload response parse error'))
          }
        },
        fail: (error) => {
          // 应用错误处理器
          this.interceptors.error(error)
          reject(error)
        }
      })
    })
  }

  /**
   * 文件下载方法
   * 
   * 优化说明：
   * - 简化下载响应处理逻辑
   * - 统一错误处理机制
   * 
   * @param {string} url - 下载URL
   * @param {Object} options - 下载选项
   * @returns {Promise} 下载Promise，返回临时文件路径
   */
  download(url, options = {}) {
    return new Promise((resolve, reject) => {
      // 应用请求拦截器获取认证头和完整URL
      const config = this.interceptors.request({ 
        url, 
        method: 'GET',
        header: {} 
      })
      
      uni.downloadFile({
        url: config.url,
        header: config.header,
        success: (response) => {
          if (response.statusCode === 200) {
            // 下载成功：返回临时文件路径
            resolve(response.tempFilePath)
          } else {
            // 下载失败：返回错误
            reject(new Error('Download failed'))
          }
        },
        fail: (error) => {
          // 应用错误处理器
          this.interceptors.error(error)
          reject(error)
        }
      })
    })
  }
}

/**
 * 创建HTTP请求实例
 * 
 * 优化说明：
 * - 提供统一的HTTP请求入口
 * - 支持统一ApiResponse格式处理
 * - 集成完整的拦截器和错误处理机制
 */
const http = new HttpRequest()

export default http