# 实现计划

- [x] 1. 修改UserInfoDTO实现UserDetails接口





  - 在现有UserInfoDTO类中添加UserDetails接口实现
  - 添加必要的UserDetails方法，返回简化的值
  - getUsername()方法返回用户ID的字符串形式
  - getAuthorities()返回空集合，其他状态方法返回简化值
  - _需求: 1.1, 1.2, 1.3, 1.4_

- [x] 2. 创建UserService实现UserDetailsService





  - 创建新的UserService类实现UserDetailsService接口
  - 通过用户ID查找用户并返回UserInfoDTO
  - 使用现有的UserRepository和UserMapper
  - 添加UsernameNotFoundException和DisabledException异常处理
  - _需求: 2.1, 2.2, 2.3, 2.4_

- [x] 3. 修改JwtTokenProvider支持完整用户信息





  - 添加新的generateAccessToken方法接收User对象
  - 在JWT令牌中存储用户ID作为subject和完整用户信息作为claims
  - 添加getUserDetailsFromToken方法从令牌提取UserInfoDTO
  - 保持现有方法向后兼容
  - _需求: 5.1, 5.2, 5.3_

- [x] 4. 修改JwtAuthenticationFilter集成UserDetailsService





  - 添加UserService依赖注入
  - 优先从JWT令牌获取完整用户信息
  - 失败时使用UserService通过用户ID加载用户
  - 创建包含UserDetails的Authentication对象设置到SecurityContext
  - _需求: 5.1, 5.2, 5.4_

- [x] 5. 修改AuthService使用新的令牌生成方法





  - 在login方法中使用新的generateAccessToken(User)方法
  - 在register方法中使用新的generateAccessToken(User)方法  
  - 在refreshToken方法中使用新的generateAccessToken(User)方法
  - 保持所有现有业务逻辑和异常处理不变
  - _需求: 4.1, 4.2, 4.3, 4.4_