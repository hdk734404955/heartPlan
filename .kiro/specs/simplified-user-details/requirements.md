# 需求文档

## 介绍

为HeartPlan恋爱规划应用创建一个极简化的UserDetails实现，专门适配dating项目的需求。由于dating项目只需要验证用户是否已登录，不需要任何权限管理，因此需要实现一个最简单的Spring Security UserDetails接口，使用最少的代码实现基本的用户认证功能。

## 需求

### 需求1：简化的UserDetails实现

**用户故事：** 作为系统开发者，我希望有一个简化的UserDetails实现类，这样我就可以与Spring Security集成而不需要处理复杂的权限管理逻辑。

#### 验收标准

1. 当系统需要UserDetails实现时，系统应该提供一个SimpleUserDetails类实现UserDetails接口
2. 当调用getAuthorities()方法时，系统应该返回一个空的权限集合，因为只需要验证是否登录
3. 当调用getPassword()方法时，系统应该返回用户的加密密码
4. 当调用getUsername()方法时，系统应该返回用户的ID作为字符串
5. 当调用isAccountNonExpired()方法时，系统应该始终返回true，因为dating项目不需要账户过期功能
6. 当调用isAccountNonLocked()方法时，系统应该始终返回true，因为dating项目不需要账户锁定功能
7. 当调用isCredentialsNonExpired()方法时，系统应该始终返回true，因为dating项目不需要凭证过期功能
8. 当调用isEnabled()方法时，系统应该返回用户实体中的enabled字段值，支持账户启用/禁用功能

### 需求2：UserDetailsService实现

**用户故事：** 作为系统开发者，我希望有一个UserDetailsService实现，这样Spring Security就可以通过用户ID加载用户详情。

#### 验收标准

1. 当Spring Security需要加载用户详情时，系统应该提供一个SimpleUserDetailsService类实现UserDetailsService接口
2. 当调用loadUserByUsername()方法时，系统应该通过用户ID查找用户
3. 当用户存在且启用时，系统应该返回包装在SimpleUserDetails中的用户信息
4. 当用户不存在时，系统应该抛出UsernameNotFoundException异常
5. 当用户存在但被禁用时，系统应该抛出DisabledException异常
6. 当数据库查询出现异常时，系统应该记录错误日志并抛出适当的认证异常

### 需求3：最简化的认证配置

**用户故事：** 作为系统开发者，我希望有最简单的认证配置，只验证用户是否已登录，这样系统就可以保持最小的复杂度。

#### 验收标准

1. 当系统启动时，SecurityConfig应该配置最简单的认证方式，只区分已登录和未登录用户
2. 当用户访问受保护资源时，系统应该只检查JWT令牌是否有效，不进行权限验证
3. 当JWT令牌有效时，系统应该允许访问所有受保护的资源
4. 当JWT令牌无效或不存在时，系统应该拒绝访问并返回401未授权状态
5. 当系统需要获取当前用户信息时，应该能够从SecurityContext中获取用户ID

### 需求4：保持现有AuthService简单性

**用户故事：** 作为系统开发者，我希望保持现有AuthService的简单性，只添加必要的UserDetails支持，这样系统就可以保持最小的改动。

#### 验收标准

1. 当用户登录时，AuthService应该继续使用现有的手动验证方式，保持代码简单
2. 当生成JWT令牌时，系统应该在令牌中包含用户ID而不是邮箱
3. 当验证成功时，系统应该返回包含用户信息的响应，无需复杂的Authentication对象
4. 当处理异常时，系统应该保持现有的简单异常处理方式
5. 当记录日志时，系统应该保持现有的日志记录方式

### 需求5：简化的JWT令牌处理

**用户故事：** 作为系统开发者，我希望JWT令牌处理尽可能简单，只需要验证令牌有效性和获取用户ID，这样系统就可以保持最小的复杂度。

#### 验收标准

1. 当JWT过滤器验证令牌时，系统应该从令牌中提取用户ID
2. 当令牌有效时，系统应该使用SimpleUserDetailsService通过用户ID加载用户信息
3. 当用户存在且启用时，系统应该创建包含SimpleUserDetails的Authentication对象
4. 当设置SecurityContext时，系统应该使用最简单的Authentication实现
5. 当令牌无效或用户不存在时，系统应该直接拒绝访问，无需复杂的异常处理

### 需求6：错误处理和日志记录

**用户故事：** 作为系统维护者，我希望有完善的错误处理和日志记录，这样我就可以快速诊断和解决认证相关的问题。

#### 验收标准

1. 当认证过程中出现异常时，系统应该记录详细的错误日志包含用户邮箱和异常原因
2. 当用户认证成功时，系统应该记录成功日志包含用户ID和用户名
3. 当用户认证失败时，系统应该记录警告日志但不暴露敏感信息
4. 当系统抛出异常时，应该使用适当的Spring Security异常类型
5. 当处理数据库异常时，系统应该将其转换为认证异常并记录原始错误
6. 当记录日志时，系统应该避免记录密码等敏感信息
