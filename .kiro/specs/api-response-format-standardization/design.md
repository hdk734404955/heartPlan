# 设计文档

## 概述

本设计文档描述了如何统一后端API接口的响应数据格式，将现有的不一致响应格式标准化为 `{code: xxx, data: [] || {}, message}` 的统一格式，并相应地更新前端客户端的响应处理逻辑。

通过分析现有代码，发现当前系统存在以下问题：
- 后端API响应格式不统一，有些直接返回数据对象，有些包含success字段
- 前端响应拦截器假设成功响应直接返回data，但实际响应结构不一致
- 错误处理逻辑分散，缺乏统一的错误响应格式

## 架构

### 当前架构分析

**后端当前响应格式：**
- 成功响应：直接返回DTO对象（如LoginResponse、RegisterResponse）
- 错误响应：返回包含success、message、errorCode、timestamp的Map对象
- 部分接口：返回包含success字段的Map对象

**前端当前处理逻辑：**
- 响应拦截器假设200状态码直接返回response.data
- 业务代码中检查response.data.success字段判断业务成功/失败
- 错误处理分散在各个API调用中

### 目标架构

**统一响应格式：**
```json
{
  "code": 200,           // HTTP状态码或业务状态码
  "data": {} || [],      // 业务数据，对象或数组
  "message": "Success"   // 英文描述信息
}
```

**响应处理流程：**
1. 后端统一响应包装器处理所有API响应
2. 前端响应拦截器统一解析新格式
3. 业务代码通过code字段判断成功/失败状态

## 组件和接口

### 后端组件

#### 1. 统一响应包装器 (ApiResponse)
```java
public class ApiResponse<T> {
    private Integer code;
    private T data;
    private String message;
}
```

#### 2. 响应构建工具类 (ResponseBuilder)
```java
public class ResponseBuilder {
    public static <T> ApiResponse<T> success(T data);
    public static <T> ApiResponse<T> success(T data, String message);
    public static <T> ApiResponse<T> error(Integer code, String message);
}
```

#### 3. 全局响应处理器 (GlobalResponseHandler)
- 使用@RestControllerAdvice拦截所有控制器响应
- 自动包装返回值为统一格式
- 处理异常并返回统一错误格式

#### 4. 全局异常处理器 (GlobalExceptionHandler)
- 捕获各种异常类型
- 转换为统一的错误响应格式
- 记录错误日志

### 前端组件

#### 1. 响应拦截器更新 (responseInterceptor)
- 解析新的统一响应格式
- 根据code字段判断成功/失败
- 统一错误处理逻辑

#### 2. API调用适配
- 更新所有API调用以适配新的响应格式
- 移除业务代码中的success字段检查
- 统一使用code字段进行状态判断

## 数据模型

### 统一响应模型
```typescript
interface ApiResponse<T> {
  code: number;           // 状态码：200成功，400客户端错误，500服务器错误等
  data: T | null;         // 业务数据，成功时包含数据，失败时为null
  message: string;        // 英文描述信息
}
```

### 状态码规范
- **200**: 操作成功
- **201**: 创建成功
- **400**: 请求参数错误
- **401**: 未授权/认证失败
- **403**: 禁止访问/权限不足
- **404**: 资源不存在
- **409**: 资源冲突（如邮箱已存在）
- **500**: 服务器内部错误

### 响应示例

**成功响应：**
```json
{
  "code": 200,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "username": "testuser"
    }
  },
  "message": "Login successful"
}
```

**错误响应：**
```json
{
  "code": 401,
  "data": null,
  "message": "Invalid credentials"
}
```

**列表数据响应：**
```json
{
  "code": 200,
  "data": [
    {"id": 1, "name": "Item 1"},
    {"id": 2, "name": "Item 2"}
  ],
  "message": "Data retrieved successfully"
}
```

## 错误处理

### 后端错误处理策略

#### 1. 异常类型映射
- `BadCredentialsException` → 401 "Invalid credentials"
- `DisabledException` → 403 "Account disabled"
- `IllegalArgumentException` → 400 "Invalid request parameters"
- `DataIntegrityViolationException` → 409 "Resource conflict"
- `Exception` → 500 "Internal server error"

#### 2. 错误响应格式
所有错误都返回统一格式，包含：
- code: HTTP状态码
- data: null
- message: 英文错误描述

#### 3. 日志记录
- 记录所有错误的详细信息
- 包含请求参数、用户信息、异常堆栈
- 区分业务错误和系统错误的日志级别

### 前端错误处理策略

#### 1. 响应拦截器处理
- 统一检查code字段
- code >= 400时触发错误处理
- 显示message中的错误信息

#### 2. 错误显示策略
- 401错误：自动跳转登录页面
- 403错误：显示权限不足提示
- 400错误：显示参数错误提示
- 500错误：显示服务器错误提示
- 网络错误：显示网络连接错误提示

#### 3. 错误恢复机制
- 401错误：清除本地认证信息，跳转登录
- 网络错误：提供重试选项
- 服务器错误：记录错误日志，提供反馈渠道

## 实现策略

### 阶段1：后端响应格式统一
1. 创建ApiResponse类和ResponseBuilder工具类
2. 实现GlobalResponseHandler拦截器
3. 更新GlobalExceptionHandler异常处理
4. 逐步迁移现有控制器接口

### 阶段2：前端适配新格式
1. 更新响应拦截器逻辑
2. 修改API调用处理逻辑
3. 更新错误处理机制
4. 测试所有API调用功能

### 阶段3：向后兼容处理
1. 保留旧格式支持一段时间
2. 添加格式检测逻辑
3. 逐步移除旧格式支持
4. 完成全面迁移

### 兼容性考虑
- 实现渐进式迁移，避免破坏现有功能
- 添加响应格式检测，支持新旧格式并存
- 提供配置开关控制格式转换
- 确保移动端和Web端同步更新

### 性能影响
- 响应包装器增加的性能开销可忽略
- 统一处理减少重复代码，提高维护效率
- 错误处理集中化，减少内存占用
- 响应格式一致性提高缓存效率