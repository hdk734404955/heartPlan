// HTTP请求工具类
import { requestInterceptor, responseInterceptor, errorHandler } from './config'

class HttpRequest {
  constructor() {
    this.interceptors = {
      request: requestInterceptor,
      response: responseInterceptor,
      error: errorHandler
    }
  }

  // 通用请求方法
  request(options) {
    return new Promise((resolve, reject) => {
      // 应用请求拦截器
      const config = this.interceptors.request(options)
      
      uni.request({
        ...config,
        success: (response) => {
          try {
            // 应用响应拦截器
            const result = this.interceptors.response(response)
            resolve(result)
          } catch (error) {
            reject(error)
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

  // GET请求
  get(url, params = {}, options = {}) {
    // 构建查询字符串
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

  // POST请求
  post(url, data = {}, options = {}) {
    return this.request({
      url,
      method: 'POST',
      data,
      ...options
    })
  }

  // PUT请求
  put(url, data = {}, options = {}) {
    return this.request({
      url,
      method: 'PUT',
      data,
      ...options
    })
  }

  // DELETE请求
  delete(url, options = {}) {
    return this.request({
      url,
      method: 'DELETE',
      ...options
    })
  }

  // 文件上传
  upload(url, filePath, formData = {}, options = {}) {
    return new Promise((resolve, reject) => {
      // 应用请求拦截器获取认证头，标记为上传请求
      const config = this.interceptors.request({ 
        url, 
        method: 'POST',
        header: {}, 
        isUpload: true 
      })
      
      // 移除Content-Type，让uni.uploadFile自动设置
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
            // 解析响应数据
            const data = JSON.parse(response.data)
            resolve(data)
          } catch (error) {
            reject(new Error('Upload response parse error'))
          }
        },
        fail: (error) => {
          this.interceptors.error(error)
          reject(error)
        }
      })
    })
  }

  // 文件下载
  download(url, options = {}) {
    return new Promise((resolve, reject) => {
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
            resolve(response.tempFilePath)
          } else {
            reject(new Error('Download failed'))
          }
        },
        fail: (error) => {
          this.interceptors.error(error)
          reject(error)
        }
      })
    })
  }
}

// 创建HTTP请求实例
const http = new HttpRequest()

export default http