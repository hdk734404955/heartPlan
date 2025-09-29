# 设计文档

## 概述

本设计文档基于现有的HeartPlan应用代码结构，旨在统一客户端与后端的错误码系统，修复登录后底部tab导航显示问题，并优化注册登录流程。设计将充分利用现有的代码架构，包括已有的全局异常处理器、API响应拦截器、认证状态管理等组件。

## 架构

### 现有架构分析

**后端架构：**
- 已有`GlobalExceptionHandler`统一异常处理
- 使用`ApiResponse<T>`统一响应格式
- JWT认证机制已实现
- 错误码映射基本完善

**前端架构：**
- 使用Pinia进行状态管理（`useAuthStore`、`useUserStore`）
- 统一的API请求拦截器和响应拦截器
- uni-app + Vue 3 + uView组件库
- 页面路由配置在`pages.json`中
- **已实现路由鉴权**：`fontend/src/utils/router-guard.js`提供完整的路由守卫功能

### 设计原则

1. **基于现有代码修改**：不新增多余组件，充分利用现有架构
2. **统一错误处理**：基于现有的`ApiResponse`格式和响应拦截器
3. **状态驱动导航**：利用现有的认证状态控制tab显示
4. **英文用户界面**：所有用户可见文案使用英文

## 组件和接口

### 1. 错误码统一处理

**现有组件：**
- `backend/src/main/java/com/heartplan/config/GlobalExceptionHandler.java`
- `fontend/src/api/config.js` (响应拦截器)

**设计方案：**
- 后端：完善`GlobalExceptionHandler`中的错误码映射
- 前端：优化响应拦截器中的错误处理逻辑
- 确保错误提示使用英文文案

### 2. 底部Tab导航控制

**现有组件：**
- `fontend/src/pages.json` (tabBar配置)
- `fontend/src/store/modules/auth.js` (认证状态)
- `fontend/src/utils/router-guard.js` (路由鉴权已实现)

**设计方案：**
- 利用uni-app的`uni.hideTabBar()`和`uni.showTabBar()`API控制tabBar显示
- 基于`authStore.isAuthenticated`状态和当前页面路径动态显示/隐藏tab
- 在登录/注册/启动页面调用`uni.hideTabBar()`隐藏tabBar
- 在主功能页面调用`uni.showTabBar()`显示tabBar
- 利用现有的路由守卫确保页面访问权限

### 3. 注册流程优化

**现有组件：**
- `fontend/src/pages/auth/register.vue`
- `fontend/src/api/auth.js`

**设计方案：**
- 修改注册第二步，使头像上传变为可选
- 注册成功后直接保存认证信息（accessToken、refreshToken）和用户信息
- 注册成功后直接跳转主页面而非登录页
- 保持现有的两步注册流程
- 处理接口返回的完整用户信息结构

### 4. 令牌刷新机制

**现有组件：**
- `fontend/src/store/modules/auth.js` (已有`refreshAccessToken`方法)
- `backend/src/main/java/com/heartplan/util/JwtTokenProvider.java`

**设计方案：**
- 优化现有的令牌刷新逻辑
- 在响应拦截器中自动处理401错误并尝试刷新令牌
- 刷新失败时清除认证状态并跳转登录页

## 数据模型

### 错误响应格式（现有）

```java
// 后端ApiResponse格式
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
}
```

### 认证状态模型（现有）

```javascript
// 前端认证状态
const authStore = {
  accessToken: '',
  refreshToken: '',
  isLoggedIn: false,
  isAuthenticated: computed(() => isLoggedIn && accessToken)
}

// 登录/注册成功响应格式
const authResponse = {
  code: 200,
  data: {
    accessToken: "eyJhbGciOiJIUzI1NiJ9...",
    refreshToken: "eyJhbGciOiJIUzI1NiJ9...",
    expiresIn: 86400000,
    user: {
      id: 2,
      email: "user@example.com",
      username: "username",
      age: 18,
      gender: "MALE",
      relationshipStatus: "SINGLE",
      avatarUrl: null,
      location: null,
      enabled: true,
      createdAt: "2025-09-27T15:35:22.0609725"
    }
  },
  message: "Success"
}
```

### 错误码映射表

| 错误码 | 场景 | 英文提示 | 处理方式 |
|--------|------|----------|----------|
| 400 | 登录凭据错误、参数验证失败 | "Invalid credentials" / "Invalid request" | 显示错误提示，不跳转 |
| 401 | JWT令牌无效/过期 | "Session expired, please login again" | 清除认证信息，跳转登录页 |
| 403 | 权限不足 | "Access denied" | 显示权限错误提示 |
| 409 | 数据冲突（如邮箱重复） | "Email already exists" | 显示冲突错误提示 |
| 500+ | 服务器错误 | "Server error, please try again later" | 显示服务器错误提示 |

## 错误处理

### 后端错误处理（基于现有代码）

**现有GlobalExceptionHandler优化：**
- 确保所有异常返回统一的`ApiResponse`格式
- 完善错误消息的英文化
- 区分不同类型的400错误（登录失败 vs 参数验证失败）

### 前端错误处理（基于现有代码）

**响应拦截器优化：**
- 基于`ApiResponse.code`字段统一处理错误
- 401错误自动尝试刷新令牌
- 刷新令牌失败时自动跳转登录页
- 所有错误提示使用英文

## 导航控制策略

### Tab显示逻辑

```javascript
// 基于认证状态控制tab显示
const shouldShowTabBar = computed(() => {
  const currentPage = getCurrentPages().pop()?.route
  const authPages = ['/pages/auth/login', '/pages/auth/register', '/pages/splash/index']
  
  return authStore.isAuthenticated && !authPages.includes(currentPage)
})
```

### 页面跳转逻辑

1. **启动页面**：检查认证状态，已登录跳转主页，未登录跳转登录页
2. **登录成功**：跳转主页并显示tab
3. **注册成功**：直接跳转主页并显示tab
4. **登出**：清除状态，跳转登录页并隐藏tab
5. **认证失效**：自动跳转登录页并隐藏tab

## 实现细节

### 1. 注册流程修改

**第二步头像可选化：**
- 移除头像字段的必填验证
- 允许用户跳过头像上传
- 注册成功后直接保存认证信息并跳转主页

### 2. 响应拦截器增强

**自动令牌刷新：**
```javascript
// 在401错误时自动尝试刷新令牌
if (code === 401 && !isRefreshing) {
  const refreshSuccess = await authStore.refreshAccessToken()
  if (refreshSuccess) {
    // 重试原始请求
    return retryOriginalRequest(originalConfig)
  }
}
```

### 3. Tab控制实现

**API控制方案：**
- 利用uni-app的`uni.hideTabBar()`和`uni.showTabBar()`API
- 在页面`onShow`生命周期中根据认证状态和页面类型控制tab显示
- 在认证状态变化时自动更新tab显示状态
- 在路由守卫中根据页面类型自动控制tab显示

### 4. 错误提示优化

**统一错误提示格式：**
- 使用`uni.showToast()`显示简短错误信息
- 使用`uni.showModal()`显示需要用户确认的错误
- 所有错误文案使用英文，保持简洁明了

## 兼容性考虑

### 向后兼容

- 保持现有API接口不变
- 保持现有数据结构不变
- 保持现有页面路由不变

### 渐进式增强

- 基于现有代码逐步优化
- 不破坏现有功能
- 保持代码结构清晰

## 性能优化

### 减少重复请求

- 令牌刷新时避免重复刷新
- 使用请求队列管理并发请求
- 缓存认证状态避免重复检查

### 用户体验优化

- 错误提示及时且明确
- 页面跳转流畅无卡顿
- 加载状态清晰可见