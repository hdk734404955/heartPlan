# 设计文档

## 概述

本设计文档旨在解决HeartPlan项目中CORS配置冲突和Spring Security过滤器链问题。通过分析日志和代码，发现主要问题是CORS配置中同时使用了`allowCredentials: true`和`allowedOrigins: "*"`，这在Spring Security中是不被允许的。

## 架构

### 当前问题分析

1. **CORS配置冲突**：
   - SecurityConfig.java中使用了`setAllowedOrigins`和`setAllowCredentials(true)`
   - CorsConfig.java中使用了通配符"*"作为允许的源
   - 这两种配置冲突导致Spring抛出IllegalArgumentException

2. **重复CORS配置**：
   - SecurityConfig.java和CorsConfig.java都配置了CORS
   - 可能导致配置冲突和不一致的行为

3. **前端API配置**：
   - 前端使用代理配置，但可能存在请求头配置问题
   - 错误处理不够完善

### 解决方案架构

```
前端 (localhost:5100)
    ↓ 代理请求 (/api/*)
Vite开发服务器代理
    ↓ 转发到后端
后端 (localhost:8080/api)
    ↓ 通过统一CORS配置
Spring Security过滤器链
    ↓ 允许认证接口匿名访问
业务控制器
```

## 组件和接口

### 1. CORS配置组件

**统一CORS配置策略**：
- 移除重复的CORS配置
- 使用`allowedOriginPatterns`替代`allowedOrigins`
- 在SecurityConfig中统一管理CORS配置

**配置参数**：
```java
// 允许的源模式
allowedOriginPatterns: ["http://localhost:*", "http://127.0.0.1:*"]
// 允许的方法
allowedMethods: ["GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"]
// 允许携带凭证
allowCredentials: true
// 预检请求缓存时间
maxAge: 3600
```

### 2. Spring Security配置组件

**安全过滤器链优化**：
- 确保OPTIONS请求被正确处理
- 认证接口允许匿名访问
- 受保护接口要求JWT认证

**路径匹配规则**：
```java
// 公开访问路径
permitAll: ["/auth/**", "/test/**", "/files/**", "/api-docs/**", "/swagger-ui/**"]
// OPTIONS请求
permitAll: ["OPTIONS", "/**"]
// 其他请求需要认证
authenticated: ["/**"]
```

### 3. 前端API配置组件

**请求拦截器增强**：
- 添加必要的请求头
- 处理认证令牌
- 改进错误处理

**响应拦截器优化**：
- 统一错误处理
- CORS错误特殊处理
- 认证失败处理

## 数据模型

### API响应模型

```javascript
// 成功响应
{
  success: true,
  data: any,
  message: string,
  timestamp: number
}

// 错误响应
{
  success: false,
  message: string,
  errorCode: string,
  timestamp: number
}
```

### CORS错误处理模型

```javascript
// CORS错误信息
{
  type: 'CORS_ERROR',
  message: string,
  suggestions: string[],
  debugInfo: {
    requestUrl: string,
    requestMethod: string,
    origin: string
  }
}
```

## 错误处理

### 1. CORS错误处理

**后端处理**：
- 统一CORS配置，避免配置冲突
- 正确处理预检请求
- 返回适当的CORS响应头

**前端处理**：
- 检测CORS错误
- 提供调试信息
- 显示用户友好的错误提示

### 2. 认证错误处理

**401未授权**：
- 清除本地令牌
- 跳转到登录页面
- 显示重新登录提示

**403禁止访问**：
- 区分CORS错误和权限错误
- 提供相应的错误提示

### 3. 网络错误处理

**超时错误**：
- 显示超时提示
- 提供重试选项

**网络连接错误**：
- 检查网络连接
- 提供网络诊断建议

## 实现策略

### 阶段1：修复CORS配置
1. 移除CorsConfig.java中的重复配置
2. 在SecurityConfig.java中使用allowedOriginPatterns
3. 统一CORS配置参数

### 阶段2：优化Spring Security配置
1. 确保OPTIONS请求正确处理
2. 验证路径匹配规则
3. 测试认证流程

### 阶段3：改进前端API配置
1. 优化请求拦截器
2. 增强错误处理
3. 添加调试功能

### 阶段4：集成测试
1. 测试所有API接口
2. 验证CORS配置
3. 测试错误处理流程

## 配置示例

### 后端CORS配置
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    // 使用allowedOriginPatterns替代allowedOrigins
    configuration.setAllowedOriginPatterns(Arrays.asList(
        "http://localhost:*",
        "http://127.0.0.1:*"
    ));
    
    configuration.setAllowedMethods(Arrays.asList(
        "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
    ));
    
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

### 前端代理配置
```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
    secure: false,
    configure: (proxy, options) => {
      proxy.on('proxyReq', (proxyReq, req, res) => {
        proxyReq.setHeader('Origin', 'http://localhost:5100');
      });
    }
  }
}
```