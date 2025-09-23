# 实施计划

## 重要开发规范

### uni-app项目开发规范
**基于fontend文件夹开发：**
- **项目基础**：基于用户提供的fontend文件夹中的uni-app项目进行开发
- **项目结构**：确保项目包含官方标准的目录结构和配置文件
- **Vue 3要求**：确保使用Vue 3模板和最新的Vue 3 + Vite构建工具
- **项目完善**：在现有基础上完善项目结构和配置
- **标准验证**：验证项目符合uni-app官方开发标准
- **版本要求**：确保使用uni-app 3.0.x或更高版本

### 技术栈要求
**移动端开发要求：**
- 使用 **uni-app + Vue 3**，采用最新稳定版本
- **官方脚手架要求**：必须使用uni-app官方脚手架创建项目
  - 使用命令：`npx @dcloudio/uvm create project-name`
  - 或使用HBuilderX IDE新建uni-app项目
  - 选择Vue 3模板，确保项目结构符合官方标准
- **严格要求：客户端只能使用JavaScript，严禁使用TypeScript**
- **JavaScript强制要求**：所有.vue文件中的script标签必须使用JavaScript，不得使用TypeScript语法
- **禁用TypeScript**：项目配置中不得包含TypeScript相关依赖、配置文件和类型定义
- 所有uni-app页面文件使用 `.vue` 扩展名
- 使用Vue 3的Composition API和纯JavaScript语法
- 遵循uni-app官方开发规范和Vue 3最佳实践

### 数据处理规范
**严禁使用模拟数据：**
- 移动端和后端都**严格禁止**使用任何形式的模拟数据（Mock Data）
- 所有数据必须来自真实的API接口响应和数据库记录
- 后端接口必须返回真实的数据库数据，不得进行数据模拟
- uni-app应用必须通过HTTP客户端（如uni.request或axios）直接调用真实API接口
- 即使在开发和测试阶段，也必须使用真实的数据库数据
- AI生成的内容必须先保存到数据库，然后通过API返回真实数据

### uView组件库使用规范
**强制使用uView组件库：**
- **严格要求**：所有UI组件必须基于uView组件库实现，严禁自定义样式
- **uView组件优先**：使用u-button、u-card、u-list、u-form等uView组件
- **uView动画效果**：使用uView提供的动画组件和过渡效果
- **uView主题定制**：通过uView的主题配置实现品牌色彩
- **设计品质**：基于uView组件库实现现代化、美观的界面设计
- **温暖信任色彩系统**：
  - **主色调**：温暖橙粉渐变（#FF6B6B到#FF8E8E）
  - **辅助色**：柔和粉色系（#FFB3BA柔和粉、#FF9AA2珊瑚色、#FFAAA5温暖橙）
  - **中性色**：#2C3E50深蓝灰、#7F8C8D浅灰、#FEFEFE纯白、#FFF5F5淡粉背景
  - **功能色彩**：#00B894匹配成功、#00D68F在线状态、#636E72离线状态
- **英文UI**：所有界面文案使用英文，体现国际化品质

### 响应式布局和单位规范
**uni-app响应式设计要求：**
- **主要单位使用rpx**：所有布局尺寸、间距、字体大小等必须使用rpx相对单位
- **特殊情况使用px**：边框宽度、阴影偏移等细节效果使用px固定单位
- **主流机型适配**：参考iPhone 6/7/8（375px宽度）、iPhone X系列（414px宽度）、Android主流机型进行设计
- **响应式布局原则**：
  - 容器宽度使用100%或rpx单位
  - 字体大小：标题48-64rpx，正文28-32rpx，辅助文字24-28rpx
  - 间距规范：小间距16rpx，中间距32rpx，大间距48rpx，超大间距64rpx
  - 按钮高度：88-100rpx，圆角24-48rpx
  - 卡片间距：16-32rpx，内边距32-48rpx
- **边框和阴影使用px**：
  - 边框宽度：1px、2px等固定值
  - 阴影偏移：使用px确保在不同设备上显示一致
  - 分割线：1px固定宽度
- **图片和头像尺寸**：使用rpx单位，确保在不同屏幕密度下等比缩放
- **安全区域适配**：使用constant()和env()函数适配刘海屏和底部安全区域

## 当前实现状态

### ✅ 已完成的基础架构
- [x] uni-app + Vue 3 + uView组件库项目配置
- [x] Pinia状态管理和网络请求配置
- [x] 温暖信任系色彩主题配置
- [x] 登录页面（基于uView组件库）
- [x] 两步注册页面（基于uView组件库）
- [x] 底部导航TabBar（基于uView组件库）
- [x] 主要页面框架（AI Templates、Community、Chat、Profile）
- [x] 认证状态管理和用户状态管理
- [x] API请求封装和路由守卫
- [x] Spring Boot 3.1.x + Lombok + MySQL + Redis + Security + JWT配置
- [x] 用户实体类（User）和相关嵌入式对象（UserPreferences、PrivacySettings）
- [x] 认证控制器（AuthController）和服务（AuthService）
- [x] JWT令牌生成和验证机制
- [x] 文件上传服务和头像管理
- [x] 数据库配置和安全配置

### 🚧 待实现的功能模块

## 阶段1：前端响应式布局优化

- [x] 1. 前端响应式布局和单位规范实施





  - **全局样式单位转换**：将现有页面的固定px单位转换为响应式rpx单位
    - 登录页面：转换字体大小、间距、按钮尺寸为rpx单位
    - 注册页面：转换表单元素、步骤指示器、按钮尺寸为rpx单位
    - 主页面：转换卡片尺寸、图标大小、文字大小为rpx单位
    - 社区页面：转换Tab导航、内容区域、浮动按钮尺寸为rpx单位
    - 聊天页面：转换消息气泡、头像尺寸、输入框高度为rpx单位
    - 个人中心页面：转换用户信息卡片、菜单项高度为rpx单位
  - **边框和阴影优化**：保持边框宽度和阴影偏移使用px固定单位
    - 卡片阴影：使用px单位确保视觉效果一致
    - 输入框边框：使用1px或2px固定宽度
    - 分割线：统一使用1px宽度
  - **主流机型适配验证**：
    - iPhone 6/7/8（375px宽度，750rpx）适配验证
    - iPhone X系列（414px宽度，828rpx）适配验证
    - Android主流机型（360px-428px宽度）适配验证
  - **字体大小规范化**：
    - 页面标题：48-64rpx
    - 正文内容：28-32rpx
    - 辅助文字：24-28rpx
    - 按钮文字：32rpx
  - **间距规范化**：
    - 小间距：16rpx（元素内部间距）
    - 中间距：32rpx（组件间距）
    - 大间距：48rpx（区块间距）
    - 超大间距：64rpx（页面区域间距）
  - **安全区域适配**：使用constant()和env()函数适配刘海屏和底部安全区域
  - _需求: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 1.10, 1.11, 1.12, 1.13, 1.14, 1.15_

## 阶段2：后端业务实体和数据库设计

- [ ] 2. 创建AI智能模板相关实体类
  - **DatingRecord实体**：使用Lombok创建约会记录实体类
    - 包含姓名、认识日期、AI兼容性评分、最后联系、状态等字段
    - 建立与User实体的关联关系
    - 添加必要的验证注解和索引
  - **DateTrackingRecord实体**：创建约会追踪记录实体
    - 包含地点、活动、对话亮点、约会后评估等字段
    - 关联到DatingRecord实体
  - **CharmEnhancementItem实体**：创建魅力提升项目实体
    - 包含类别、建议内容、完成状态、创建时间等字段
    - 支持四个类别：外表、沟通、兴趣、个人成长
  - **RelationshipDashboard实体**：创建关系仪表板实体
    - 包含健康评分、沟通质量评估、趋势分析等字段
    - 建立与User实体的关联关系
    - 支持警报系统和改进建议
  - **ImportantDate实体**：创建重要日期实体
    - 包含日期、类型、描述、AI庆祝建议等字段
    - 支持提前提醒功能
  - **ConflictRecord实体**：创建冲突解决记录实体
    - 包含冲突详情、情绪强度、解决过程、AI分析结果等字段
    - 支持模式分析和预防建议
  - **GrowthTrajectory实体**：创建成长轨迹实体
    - 支持关系发展可视化和趋势预测
    - 包含时间节点、发展阶段、里程碑等字段
  - _需求: 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 4.10, 4.11, 4.12_

- [ ] 2.1 创建社区功能相关实体类
  - **CommunityPost实体**：使用Lombok创建社区帖子实体
    - 包含标题、内容、类型、状态、发布时间等字段
    - 建立与User实体的关联关系（作者）
    - 支持四种帖子类型：推荐内容、成功故事、问题求助、每周话题
    - 添加点赞数、评论数、分享数等统计字段
  - **Comment实体**：创建评论实体
    - 包含评论内容、发布时间、点赞数等字段
    - 建立与CommunityPost和User的关联关系
    - 支持评论回复功能（自关联）
  - **UserFollow实体**：创建用户关注关系实体
    - 建立用户之间的关注关系
    - 包含关注时间、状态等字段
  - **PostLike实体**：创建帖子点赞关系实体
    - 记录用户对帖子的点赞行为
    - 防止重复点赞
  - _需求: 5.1, 5.2, 5.3, 5.4, 5.11, 5.12, 5.13, 5.14_

- [ ] 2.2 创建聊天功能相关实体类
  - **ChatMessage实体**：使用Lombok创建聊天消息实体
    - 支持文本和图片消息类型
    - 包含发送者、接收者、消息内容、发送时间等字段
    - 支持消息状态（发送中、已送达、已读）
    - 建立与User实体的关联关系
  - **ChatConversation实体**：创建聊天会话实体
    - 管理两个用户之间的聊天会话
    - 包含最后消息、未读数量等字段
    - 支持会话置顶和静音功能
  - **AICoachMessage实体**：创建AI教练消息实体
    - 专门处理用户与AI教练的对话
    - 支持不同的咨询模式（危机支持、约会建议等）
    - 记录AI回应的生成时间和模式
  - _需求: 6.1, 6.2, 6.5, 6.6_

## 阶段3：后端Repository和Service层实现

- [ ] 3. 实现数据访问层Repository接口
  - **模板相关Repository**：创建模板相关的数据访问层
    - DatingRecordRepository、DateTrackingRecordRepository
    - RelationshipDashboardRepository、ImportantDateRepository
    - ConflictRecordRepository、GrowthTrajectoryRepository
    - CharmEnhancementItemRepository
  - **社区相关Repository**：创建社区相关的数据访问层
    - CommunityPostRepository、CommentRepository
    - UserFollowRepository、PostLikeRepository
    - 实现复杂查询和统计功能
  - **聊天相关Repository**：创建聊天相关的数据访问层
    - ChatMessageRepository、ChatConversationRepository
    - AICoachMessageRepository
    - 实现消息查询和统计功能
  - _需求: 4.1-4.12, 5.1-5.18, 6.1-6.10_

- [ ] 3.1 实现AI服务集成
  - **AIService创建**：使用@Slf4j和@RequiredArgsConstructor创建AI服务类
    - 集成OpenAI GPT-4 API，实现AI客户端配置
    - 实现兼容性评分算法，基于输入信息生成0-100%评分
    - 实现约会分析算法，24小时内提供表现分析和改进建议
    - 实现关系健康评分算法，为情侣用户提供关系评估
  - **AI教练系统**：实现AI教练对话功能
    - 基于用户恋爱状态提供个性化回应
    - 实现多种咨询模式：危机支持、约会建议、关系指导、进展回顾
    - 实现5秒内响应的性能优化
  - **AI内容审核系统**：集成AI服务进行内容质量审核
    - 实现成功故事的AI质量审查
    - 自动分类和标签化帖子内容
    - 检测和过滤不当内容
    - 生成内容质量评分
  - **数据规范**：AI生成的内容必须保存到数据库，接口返回数据库中的真实记录
  - _需求: 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 4.10, 4.11, 4.12, 5.5, 5.6, 5.10, 5.18, 6.4, 6.8, 6.10_

## 阶段4：后端Controller层和API接口实现

- [ ] 4. 实现AI智能模板Controller和API
  - **TemplateController**：使用@Slf4j和@RequiredArgsConstructor创建模板控制器
    - 实现约会管理相关API接口（GET/POST/PUT/DELETE /api/templates/dating-management）
    - 实现约会追踪相关API接口（GET/POST /api/templates/dating-tracker）
    - 实现魅力提升相关API接口（GET/PUT /api/templates/charm-enhancement）
    - 实现关系仪表板API接口（GET/POST /api/templates/relationship-dashboard）
    - 实现重要日期管理API接口（GET/POST/PUT/DELETE /api/templates/important-dates）
    - 实现冲突解决记录API接口（GET/POST /api/templates/conflict-resolution）
    - 实现成长轨迹数据API接口（GET/POST /api/templates/growth-trajectory）
  - **TemplateService**：使用@Slf4j和@RequiredArgsConstructor创建模板服务
    - 实现约会记录CRUD业务逻辑
    - 实现AI兼容性评分计算逻辑
    - 实现约会分析和建议生成逻辑
    - 实现关系健康评分和趋势分析逻辑
  - **数据规范**：所有接口必须返回数据库中的真实模板数据，严禁使用模拟数据
  - _需求: 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 4.10, 4.11, 4.12_

- [ ] 4.1 实现社区功能Controller和API
  - **CommunityController**：使用@Slf4j和@RequiredArgsConstructor创建社区控制器
    - 实现帖子CRUD接口（GET/POST/PUT/DELETE /api/community/posts）
    - 实现四个Tab的内容分类接口（GET /api/community/recommendations等）
    - 实现推荐算法接口（GET /api/community/recommendations）
    - 实现互动功能接口（POST /api/community/posts/{id}/like等）
    - 实现评论系统API（GET/POST /api/community/posts/{postId}/comments）
    - 实现用户关系API（POST /api/community/users/{userId}/follow）
    - 实现搜索功能API（GET /api/community/search/posts）
  - **CommunityService**：使用@Slf4j和@RequiredArgsConstructor创建社区服务
    - 实现帖子管理业务逻辑
    - 实现推荐算法逻辑（基于用户恋爱状态、兴趣和互动历史）
    - 实现AI内容审核逻辑
    - 实现用户互动业务逻辑
    - 实现每周话题生成（AI基于热门恋爱主题和用户统计创建讨论提示）
  - **数据规范**：所有社区帖子必须来自数据库真实记录，严禁返回模拟帖子内容
  - _需求: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 5.8, 5.9, 5.10, 5.11, 5.12, 5.13, 5.14, 5.15, 5.16, 5.17, 5.18_

- [ ] 4.2 实现聊天功能Controller和API
  - **ChatController**：使用@Slf4j和@RequiredArgsConstructor创建聊天控制器
    - 实现联系人列表接口（GET /api/chat/contacts），AI教练显示在顶部
    - 实现聊天历史接口（GET /api/chat/conversations/{userId}/messages），支持分页加载
    - 实现消息发送接口（POST /api/chat/conversations/{userId}/messages），支持文本和图片
    - 实现消息状态更新接口（PUT /api/chat/messages/{messageId}/read）
    - 实现AI教练对话接口（POST /api/chat/ai-coach/conversation）
    - 实现AI教练历史记录接口（GET /api/chat/ai-coach/history）
  - **ChatService**：使用@Slf4j和@RequiredArgsConstructor创建聊天服务
    - 实现消息发送和接收业务逻辑
    - 实现消息状态管理
    - 实现会话管理功能
    - 实现AI教练对话逻辑（5秒内响应，个性化回应）
  - **WebSocket配置**：配置Spring WebSocket和消息代理
    - 配置WebSocket端点和消息代理
    - 实现用户认证和会话管理
    - 实现实时消息传递处理器
    - 管理用户在线状态
  - **数据规范**：所有聊天消息必须存储到数据库，严禁使用模拟聊天数据
  - _需求: 6.1, 6.2, 6.3, 6.4, 6.5, 6.6, 6.7, 6.8, 6.9, 6.10_

- [ ] 4.3 完善用户管理Controller和API
  - **UserController扩展**：扩展用户控制器，添加完整的资料管理接口
    - 用户资料接口（GET /api/users/profile）
    - 更新资料接口（PUT /api/users/profile）
    - 恋爱状态更新接口（PUT /api/users/relationship-status）
    - 头像上传接口（POST /api/users/avatar）
    - 用户洞察数据接口（GET /api/users/insights）
    - 隐私设置更新接口（PUT /api/users/privacy-settings）
    - 数据导出接口（GET /api/users/export-data）
    - 账户注销接口（DELETE /api/users/account）
  - **UserService扩展**：扩展用户服务，实现完整的用户管理业务逻辑
    - 用户资料管理、统计数据计算
    - 隐私设置管理、数据导出处理
    - 账户注销和数据清理逻辑
  - **数据规范**：所有用户数据必须来自数据库真实记录，严禁使用模拟用户信息
  - _需求: 7.1, 7.2, 7.3, 7.4, 7.5, 7.6, 7.7, 7.8, 7.9, 7.10, 7.11_

## 阶段5：前端功能页面实现

- [ ] 5. 实现AI智能模板前端页面
  - **单身用户模板页面**：创建单身用户专用的模板页面
    - DatingManagementPage：约会管理页面（使用u-list、u-card、u-rate组件）
    - DateTrackingPage：约会追踪页面（使用u-form、u-textarea、u-alert组件）
    - CharmEnhancementPage：魅力提升页面（使用u-collapse、u-checkbox、u-progress组件）
  - **情侣用户模板页面**：创建情侣用户专用的模板页面
    - RelationshipDashboardPage：关系管理仪表板页面（使用u-circle-progress、u-notice-bar组件）
    - ImportantDatesPage：重要日期管理页面（使用u-calendar、u-card、u-badge组件）
    - ConflictResolutionPage：冲突解决记录页面（使用u-form、u-slider、u-time-line组件）
    - GrowthTrajectoryPage：恋爱成长轨迹页面（使用图表组件展示成长轨迹）
  - **模板切换功能**：实现恋爱状态改变时的模板集合切换
  - **TemplateStore状态管理**：完善模板相关的状态管理
  - **数据规范**：所有模板数据必须从API获取真实数据，不得使用硬编码示例数据
  - _需求: 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 4.10, 4.11, 4.12_

- [ ] 5.1 实现社区功能前端页面
  - **CommunityHomePage完善**：完善社区主页面，实现四个Tab的具体内容
    - 推荐Tab：使用u-list展示智能推荐内容，支持下拉刷新和上拉加载
    - 成功故事Tab：使用u-card展示匿名成功故事，u-tag标识故事类别
    - 问题求助Tab：使用u-collapse展示问题详情，u-badge显示回复数量
    - 每周话题Tab：使用u-notice-bar展示AI生成的话题提示
  - **社区互动界面**：创建帖子互动功能
    - InteractionBar组件：创建帖子互动栏组件（使用u-icon实现点赞、评论、分享按钮）
    - CommentSection组件：创建评论区域组件（使用u-list展示嵌套评论和回复）
    - UserProfileModal组件：创建用户资料弹窗组件（使用u-modal显示用户详细资料）
    - PostDetailPage：创建帖子详情页面
    - CreatePostPage：创建发布帖子页面
  - **搜索功能**：使用u-search实现跨Tab内容搜索
  - **CommunityStore状态管理**：完善社区相关的状态管理
  - **数据规范**：所有社区内容必须从API获取真实数据，严禁使用硬编码内容
  - _需求: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 5.8, 5.9, 5.10, 5.11, 5.12, 5.13, 5.14, 5.15, 5.16, 5.17, 5.18_

- [ ] 4.2 实现聊天功能前端页面
  - **ChatListPage完善**：完善聊天联系人列表页面
    - 使用u-list展示联系人列表，AI教练置顶显示
    - 使用u-avatar显示用户头像，u-badge显示未读消息数量
    - 使用u-tag标识AI教练的特殊身份
  - **PrivateChatPage**：创建私聊对话页面
    - 使用u-list展示消息列表，支持分页加载历史消息
    - 使用u-card包装消息气泡，区分发送和接收样式
    - 使用u-text显示文本消息，u-image展示图片消息
    - 使用u-icon显示消息状态（发送中、已送达、已读）
  - **AICoachPage**：创建AI教练对话页面
    - 使用u-loading显示AI思考状态（确保5秒内响应）
    - 使用u-tag区分AI消息和用户消息
    - 支持多种咨询模式选择（危机支持、约会建议等）
  - **MessageInput组件**：创建消息输入组件
    - 使用u-input实现消息文本输入
    - 使用u-upload处理图片选择和上传
  - **实时通信集成**：集成WebSocket实现实时消息传递
  - **ChatStore状态管理**：完善聊天相关的状态管理
  - **数据规范**：所有聊天数据必须从API获取真实数据，严禁使用模拟聊天记录
  - _需求: 6.1, 6.2, 6.3, 6.4, 6.5, 6.6, 6.7, 6.8, 6.9, 6.10_

- [ ] 4.3 完善个人中心功能页面
  - **ProfilePage完善**：完善个人中心主页面
    - 使用u-avatar显示头像，支持点击更换
    - 使用u-text展示用户基本信息
    - 使用u-tag显示恋爱状态，配置温暖色系
    - 使用u-cell-group组织功能入口布局
  - **EditProfilePage**：创建资料编辑页面
    - 使用u-form创建完整的资料编辑表单
    - 使用u-input收集用户信息（用户名、年龄、位置等）
    - 使用u-upload处理头像上传功能
    - 使用u-modal显示恋爱状态切换确认对话框
  - **DataInsightsPage**：创建数据洞察页面
    - 使用u-card展示个性化统计卡片
    - 使用u-progress显示模板使用进度
    - 使用u-circle-progress显示AI对话次数等统计数据
  - **MyActivityPage**：创建用户活动记录页面
    - 使用u-list展示用户活动历史
    - 使用u-time-line显示活动时间轴
  - **SettingsPage**：创建设置页面
    - 使用u-cell-group创建设置选项布局
    - 使用u-switch控制隐私开关
    - 使用u-radio选择偏好设置
  - **状态管理完善**：完善用户相关的状态管理
  - **数据规范**：所有个人数据必须从API获取真实数据，严禁使用模拟个人信息
  - _需求: 7.1, 7.2, 7.3, 7.4, 7.5, 7.6, 7.7, 7.8, 7.9_

## 阶段5：系统优化和完善

- [ ] 5. 性能优化和错误处理
  - **Redis缓存策略**：实现系统缓存优化
    - 用户会话缓存、JWT令牌黑名单管理
    - 推荐算法结果缓存、热门内容缓存
    - AI评分结果缓存、用户统计数据缓存
    - 实现缓存失效和更新策略
  - **数据库查询优化**：优化数据库性能
    - 添加必要的索引（用户邮箱、帖子时间、消息查询等）
    - 优化复杂查询和分页查询
    - 实现数据库连接池优化
  - **全局异常处理**：实现统一的错误处理机制
    - 使用@Slf4j创建GlobalExceptionHandler
    - 处理认证异常、业务异常、系统异常
    - 统一错误响应格式和错误码
  - **uni-app错误处理**：实现前端错误处理
    - 使用Vue 3实现前端ErrorHandler工具类
    - 网络错误处理、API错误处理
    - 用户友好的错误提示和重试机制
  - **图片处理优化**：优化文件上传和处理
    - 实现图片压缩和格式转换
    - 文件大小限制和类型验证
    - 图片存储和CDN集成准备
  - _需求: 系统性能和稳定性_

- [ ] 5.1 最终集成和部署准备
  - **功能完整性验证**：验证所有功能模块的完整性
    - 认证流程端到端验证
    - 模板功能完整性验证
    - 社区功能交互验证
    - 聊天功能实时通信验证
    - 个人中心功能验证
  - **数据流验证**：验证前后端数据流的正确性
    - API接口数据格式验证
    - 数据库数据一致性检查
    - 缓存数据同步验证
  - **跨平台兼容性验证**：确保uni-app跨平台兼容性
    - H5平台功能验证
    - 微信小程序平台验证
    - APP平台基础功能验证
  - **性能基准建立**：建立系统性能基准
    - API响应时间基准
    - 数据库查询性能基准
    - 前端页面加载性能基准
  - **安全性检查**：进行基础安全性验证
    - JWT令牌安全性验证
    - API接口权限验证
    - 数据输入验证和SQL注入防护
  - **部署配置准备**：准备生产环境部署配置
    - 环境变量配置模板
    - 数据库初始化脚本
    - 应用配置文件模板
  - _需求: 系统部署和质量保证_

## 阶段2：AI智能模板系统后端实现

- [ ] 2. 单身用户模板后端实现
  - **DatingRecord实体**：使用Lombok创建约会记录实体类
    - 包含姓名、认识日期、AI兼容性评分、最后联系、状态等字段
    - 建立与User实体的关联关系
    - 添加必要的验证注解和索引
  - **DateTrackingRecord实体**：创建约会追踪记录实体
    - 包含地点、活动、对话亮点、约会后评估等字段
    - 关联到DatingRecord实体
  - **CharmEnhancementItem实体**：创建魅力提升项目实体
    - 包含类别、建议内容、完成状态、创建时间等字段
    - 支持四个类别：外表、沟通、兴趣、个人成长
  - **TemplateController**：使用@Slf4j和@RequiredArgsConstructor创建模板控制器
    - 实现约会管理相关API接口
    - 实现约会追踪相关API接口
    - 实现魅力提升相关API接口
  - **TemplateService**：使用@Slf4j和@RequiredArgsConstructor创建模板服务
    - 实现约会记录CRUD业务逻辑
    - 实现AI兼容性评分计算逻辑
    - 实现约会分析和建议生成逻辑
  - **数据规范**：所有接口必须返回数据库中的真实模板数据，严禁使用模拟数据
  - _需求: 4.1, 4.2, 4.3, 4.4, 4.5, 4.6_

- [ ] 2.1 情侣用户模板后端实现
  - **RelationshipDashboard实体**：创建关系仪表板实体
    - 包含健康评分、沟通质量评估、趋势分析等字段
    - 建立与User实体的关联关系
    - 支持警报系统和改进建议
  - **ImportantDate实体**：创建重要日期实体
    - 包含日期、类型、描述、AI庆祝建议等字段
    - 支持提前提醒功能
  - **ConflictRecord实体**：创建冲突解决记录实体
    - 包含冲突详情、情绪强度、解决过程、AI分析结果等字段
    - 支持模式分析和预防建议
  - **GrowthTrajectory实体**：创建成长轨迹实体
    - 支持关系发展可视化和趋势预测
    - 包含时间节点、发展阶段、里程碑等字段
  - **情侣模板API扩展**：扩展TemplateController实现情侣模板接口
    - 实现关系仪表板CRUD接口
    - 实现重要日期管理接口
    - 实现冲突解决记录接口
    - 实现成长轨迹数据接口
  - _需求: 4.7, 4.8, 4.9, 4.10, 4.11, 4.12_

- [ ] 2.2 AI服务集成和模板业务逻辑
  - **AIService创建**：使用@Slf4j和@RequiredArgsConstructor创建AI服务类
    - 集成OpenAI GPT-4 API，实现AI客户端配置
    - 实现兼容性评分算法，基于输入信息生成0-100%评分
    - 实现约会分析算法，24小时内提供表现分析和改进建议
    - 实现关系健康评分算法，为情侣用户提供关系评估
  - **DTO类创建**：创建模板相关的数据传输对象
    - CompatibilityScoreRequest和CompatibilityScoreResponse
    - DateAnalysisRequest和DateAnalysisResponse
    - RelationshipHealthRequest和RelationshipHealthResponse
    - CharmSuggestionRequest和CharmSuggestionResponse
  - **Repository层实现**：创建模板相关的数据访问层
    - DatingRecordRepository、DateTrackingRecordRepository
    - RelationshipDashboardRepository、ImportantDateRepository
    - ConflictRecordRepository、GrowthTrajectoryRepository
  - **数据规范**：AI生成的内容必须保存到数据库，接口返回数据库中的真实记录
  - _需求: 4.4, 4.5, 4.6, 4.7, 4.8, 4.9, 4.10, 4.11, 4.12_

## 阶段3：AI智能模板前端界面

- [ ] 3. uni-app单身用户模板界面（基于uView组件库）
  - **DatingManagementPage**：创建约会管理页面
    - 使用u-list和u-card展示约会记录列表
    - 使用u-form创建约会记录添加/编辑表单
    - 使用u-rate组件显示AI兼容性评分
    - 使用u-tag显示约会状态，配置温暖色系
    - 集成约会管理API，实现CRUD操作
  - **DateTrackingPage**：创建约会追踪页面
    - 使用u-form收集约会追踪信息（地点、活动、对话亮点等）
    - 使用u-textarea记录详细内容和约会后评估
    - 使用u-alert显示AI分析建议和改进提示
    - 集成约会分析API，获取AI生成的建议
  - **CharmEnhancementPage**：创建魅力提升页面
    - 使用u-collapse展示四个魅力提升分类
    - 使用u-checkbox跟踪完成状态
    - 使用u-progress显示各类别完成进度
    - 集成魅力建议API，获取AI生成的改进建议
  - **TemplateStore状态管理**：完善模板相关的状态管理
    - 管理约会记录、追踪数据、魅力提升项目状态
    - 实现AI评分和建议的缓存机制
    - 处理加载状态和错误状态
  - **数据规范**：所有模板数据必须从API获取真实数据，不得使用硬编码示例数据
  - _需求: 4.4, 4.5, 4.6_

- [ ] 3.1 uni-app情侣用户模板界面（基于uView组件库）
  - **RelationshipDashboardPage**：创建关系管理仪表板页面
    - 使用u-circle-progress显示关系健康评分（10分制）
    - 使用u-notice-bar显示警报信息和改进建议
    - 使用u-card展示沟通质量评估和趋势分析
    - 集成关系仪表板API，获取健康评分和趋势数据
  - **ImportantDatesPage**：创建重要日期管理页面
    - 使用u-calendar选择和管理重要日期
    - 使用u-card展示AI生成的庆祝建议
    - 使用u-badge显示提醒数量和即将到来的日期
    - 集成重要日期API，实现日期管理和提醒功能
  - **ConflictResolutionPage**：创建冲突解决记录页面
    - 使用u-form记录冲突详情和情绪信息
    - 使用u-slider评估情绪强度
    - 使用u-time-line展示解决过程和进展
    - 集成冲突分析API，获取AI模式分析和预防建议
  - **GrowthTrajectoryPage**：创建恋爱成长轨迹页面
    - 使用图表组件展示关系发展轨迹
    - 显示关系里程碑和发展阶段
    - 提供未来发展趋势预测
    - 集成成长轨迹API，获取可视化数据
  - **模板切换功能**：实现恋爱状态改变时的模板集合切换
    - 在用户更改恋爱状态时自动切换可用模板
    - 保留相关历史数据，提供数据迁移提示
  - **数据规范**：所有关系数据必须从API获取真实数据，严禁使用模拟数据
  - _需求: 4.7, 4.8, 4.9, 4.10, 4.11, 4.12_

## 阶段4：社区功能后端实现

- [ ] 4. 社区基础后端API
  - **CommunityPost实体**：使用Lombok创建社区帖子实体
    - 包含标题、内容、类型、状态、发布时间等字段
    - 建立与User实体的关联关系（作者）
    - 支持四种帖子类型：推荐内容、成功故事、问题求助、每周话题
    - 添加点赞数、评论数、分享数等统计字段
  - **Comment实体**：创建评论实体
    - 包含评论内容、发布时间、点赞数等字段
    - 建立与CommunityPost和User的关联关系
    - 支持评论回复功能（自关联）
  - **UserFollow实体**：创建用户关注关系实体
    - 建立用户之间的关注关系
    - 包含关注时间、状态等字段
  - **PostLike实体**：创建帖子点赞关系实体
    - 记录用户对帖子的点赞行为
    - 防止重复点赞
  - **CommunityController**：使用@Slf4j和@RequiredArgsConstructor创建社区控制器
    - 实现帖子CRUD接口
    - 实现四个Tab的内容分类接口
    - 实现推荐算法接口
    - 实现互动功能接口（点赞、评论、关注）
  - **CommunityService**：使用@Slf4j和@RequiredArgsConstructor创建社区服务
    - 实现帖子管理业务逻辑
    - 实现推荐算法逻辑
    - 实现AI内容审核逻辑
    - 实现用户互动业务逻辑
  - **数据规范**：所有社区帖子必须来自数据库真实记录，严禁返回模拟帖子内容
  - _需求: 5.1, 5.2, 5.3, 5.4, 5.11, 5.12, 5.13, 5.14_

- [ ] 4.1 社区推荐算法和AI审核系统
  - **推荐算法实现**：基于用户恋爱状态、兴趣和互动历史的智能推荐
    - 实现基于协同过滤的推荐算法
    - 考虑用户恋爱状态匹配相关内容
    - 基于用户互动历史优化推荐结果
    - 实现推荐内容的实时更新机制
  - **AI内容审核系统**：集成AI服务进行内容质量审核
    - 实现成功故事的AI质量审查
    - 自动分类和标签化帖子内容
    - 检测和过滤不当内容
    - 生成内容质量评分
  - **每周话题生成**：AI基于热门恋爱主题和用户统计创建讨论提示
    - 分析用户人口统计数据
    - 生成个性化话题讨论
    - 定期更新话题内容
  - **Repository层实现**：创建社区相关的数据访问层
    - CommunityPostRepository、CommentRepository
    - UserFollowRepository、PostLikeRepository
    - 实现复杂查询和统计功能
  - **数据规范**：所有推荐和审核结果必须保存到数据库，严禁使用模拟数据
  - _需求: 5.1, 5.4, 5.5, 5.6, 5.10, 5.18_

## 阶段5：社区功能前端界面

- [ ] 5. uni-app社区Tab导航界面（基于uView组件库）
  - **CommunityHomePage**：创建社区主页面，使用u-tabs组件实现四个Tab导航
    - 推荐Tab：使用u-list展示智能推荐内容，支持下拉刷新和上拉加载
    - 成功故事Tab：使用u-card展示匿名成功故事，u-tag标识故事类别
    - 问题求助Tab：使用u-collapse展示问题详情，u-badge显示回复数量
    - 每周话题Tab：使用u-notice-bar展示AI生成的话题提示
  - **PostCard组件**：创建帖子卡片组件
    - 使用u-card实现帖子展示，配置温暖色系阴影
    - 显示作者信息、发布时间、内容预览
    - 集成点赞、评论、分享功能按钮
  - **Tab切换和动画**：实现Tab切换动画效果
    - 使用u-transition实现Tab指示器滑动动画
    - 配置温暖橙粉色系的活跃状态样式
  - **内容加载和状态管理**：完善内容加载和状态处理
    - 使用u-loading显示加载状态
    - 使用u-empty显示空状态和无网络状态
    - 实现内容缓存和离线浏览功能
  - **搜索功能**：使用u-search实现跨Tab内容搜索
    - 支持关键词搜索和标签筛选
    - 实现搜索历史和热门搜索
  - **CommunityStore状态管理**：完善社区相关的状态管理
    - 管理Tab状态、帖子列表、用户互动状态
    - 实现推荐内容的缓存和更新机制
    - 处理加载状态和错误状态
  - **数据规范**：所有社区内容必须从API获取真实数据，严禁使用硬编码内容
  - _需求: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7, 5.8, 5.9, 5.10_

- [ ] 5.1 uni-app社区互动界面（基于uView组件库）
  - **InteractionBar组件**：创建帖子互动栏组件
    - 使用u-icon实现点赞、评论、分享按钮
    - 点赞动画：心形图标填充动画，使用温暖橙粉渐变
    - 实时显示点赞数、评论数、分享数
    - 集成点赞API，实现点赞状态切换
  - **CommentSection组件**：创建评论区域组件
    - 使用u-list展示嵌套评论和回复
    - 使用u-input收集新评论内容
    - 使用u-divider分隔不同层级的回复
    - 支持评论点赞和回复功能
    - 集成评论API，实现评论CRUD操作
  - **UserProfileModal组件**：创建用户资料弹窗组件
    - 使用u-modal显示用户详细资料
    - 使用u-button实现关注/取消关注功能
    - 提供发起私聊的入口
    - 显示用户的帖子历史和互动统计
  - **PostDetailPage**：创建帖子详情页面
    - 显示完整的帖子内容和评论
    - 支持深度互动和讨论
    - 实现相关帖子推荐
  - **CreatePostPage**：创建发布帖子页面
    - 使用u-form收集帖子内容
    - 支持选择帖子类型和添加标签
    - 集成AI内容审核提示
  - **私聊集成**：从用户资料发起私聊功能
    - 集成到聊天功能模块
    - 自动创建聊天会话
  - **实时更新和反馈**：实现互动的实时更新
    - 点赞数量实时更新
    - 使用u-toast提供操作反馈
    - 实现乐观更新机制
  - **数据规范**：所有互动操作必须调用真实API，严禁使用模拟互动逻辑
  - _需求: 5.11, 5.12, 5.13, 5.14, 5.15, 5.16, 5.17, 5.18_

## 阶段6：聊天功能后端实现

- [ ] 6. 聊天系统基础后端
  - **ChatMessage实体**：使用Lombok创建聊天消息实体
    - 支持文本和图片消息类型
    - 包含发送者、接收者、消息内容、发送时间等字段
    - 支持消息状态（发送中、已送达、已读）
    - 建立与User实体的关联关系
  - **ChatConversation实体**：创建聊天会话实体
    - 管理两个用户之间的聊天会话
    - 包含最后消息、未读数量等字段
    - 支持会话置顶和静音功能
  - **AICoachMessage实体**：创建AI教练消息实体
    - 专门处理用户与AI教练的对话
    - 支持不同的咨询模式（危机支持、约会建议等）
    - 记录AI回应的生成时间和模式
  - **ChatController**：使用@Slf4j和@RequiredArgsConstructor创建聊天控制器
    - 实现联系人列表接口，AI教练显示在顶部
    - 实现聊天历史接口，支持分页加载
    - 实现消息发送接口，支持文本和图片
    - 实现消息状态更新接口
  - **ChatService**：使用@Slf4j和@RequiredArgsConstructor创建聊天服务
    - 实现消息发送和接收业务逻辑
    - 实现消息状态管理
    - 实现会话管理功能
  - **数据规范**：所有聊天消息必须存储到数据库，严禁使用模拟聊天数据
  - _需求: 6.1, 6.2, 6.5, 6.6_

- [ ] 6.1 WebSocket实时通信和AI教练系统
  - **WebSocket配置**：配置Spring WebSocket和消息代理
    - 配置WebSocket端点和消息代理
    - 实现用户认证和会话管理
    - 配置跨域和安全策略
  - **ChatWebSocketHandler**：实现实时消息传递处理器
    - 处理WebSocket连接和断开
    - 实现实时消息广播
    - 管理用户在线状态
  - **消息状态管理**：实现消息状态的实时更新
    - 消息发送状态（发送中、已送达、已读）
    - 用户在线状态同步
    - 消息送达确认机制
  - **AI教练系统**：实现AI教练对话功能
    - AI教练对话接口（POST /api/chat/ai-coach/conversation）
    - AI教练历史记录接口（GET /api/chat/ai-coach/history）
    - 基于用户恋爱状态提供个性化回应
    - 实现多种咨询模式：危机支持、约会建议、关系指导、进展回顾
  - **OpenAI集成扩展**：扩展AI服务以支持聊天功能
    - 集成OpenAI GPT-4 API用于AI教练对话
    - 实现上下文记忆和个性化回应
    - 配置不同咨询模式的AI提示词
    - 实现5秒内响应的性能优化
  - **Repository层实现**：创建聊天相关的数据访问层
    - ChatMessageRepository、ChatConversationRepository
    - AICoachMessageRepository
    - 实现消息查询和统计功能
  - **数据规范**：AI对话内容必须保存到数据库，严禁使用模拟AI回应
  - _需求: 6.3, 6.4, 6.6, 6.8, 6.10_

## 阶段7：聊天功能前端界面

- [ ] 7. uni-app聊天界面（基于uView组件库）
  - **ChatListPage**：创建聊天联系人列表页面
    - 使用u-list展示联系人列表，AI教练置顶显示
    - 使用u-avatar显示用户头像，u-badge显示未读消息数量
    - 使用u-tag标识AI教练的特殊身份
    - 显示最后消息预览和时间
    - 支持联系人搜索和会话管理
  - **PrivateChatPage**：创建私聊对话页面
    - 使用u-list展示消息列表，支持分页加载历史消息
    - 使用u-card包装消息气泡，区分发送和接收样式
    - 使用u-text显示文本消息，u-image展示图片消息
    - 使用u-icon显示消息状态（发送中、已送达、已读）
    - 实现消息长按操作菜单（删除、复制等）
  - **AICoachPage**：创建AI教练对话页面
    - 使用u-loading显示AI思考状态（确保5秒内响应）
    - 使用u-tag区分AI消息和用户消息
    - 支持多种咨询模式选择（危机支持、约会建议等）
    - 实现AI教练的个性化欢迎和引导
    - 显示AI教练的专业建议和分析
  - **MessageInput组件**：创建消息输入组件
    - 使用u-input实现消息文本输入
    - 使用u-upload处理图片选择和上传
    - 使用u-button发送消息，支持按下反馈动画
    - 支持表情选择和快捷回复
  - **实时通信集成**：集成WebSocket实现实时消息传递
    - 实现消息的实时接收和发送
    - 处理连接断开和重连逻辑
    - 实现消息状态的实时更新
    - 处理用户在线状态显示
  - **ChatStore状态管理**：完善聊天相关的状态管理
    - 管理联系人列表、消息历史、AI教练对话状态
    - 实现消息缓存和离线消息处理
    - 处理WebSocket连接状态和错误处理
  - **图片消息功能**：实现图片消息的完整功能
    - 图片选择、压缩和上传
    - 图片预览和保存功能
    - 图片加载失败的处理
  - **数据规范**：所有聊天数据必须从API获取真实数据，严禁使用模拟聊天记录
  - _需求: 6.1, 6.2, 6.3, 6.5, 6.7, 6.8, 6.9, 6.10_

## 阶段8：个人中心功能完善

- [ ] 8. 个人中心后端API完善
  - **UserController扩展**：扩展用户控制器，添加完整的资料管理接口
    - 用户资料接口（GET /api/users/profile）
    - 更新资料接口（PUT /api/users/profile）
    - 恋爱状态更新接口（PUT /api/users/relationship-status）
    - 头像上传接口（POST /api/users/avatar）
  - **用户统计和洞察**：实现用户数据分析功能
    - 用户洞察数据接口（GET /api/users/insights）
    - 模板使用统计、AI对话次数、社区互动数据
    - 活动记录和成长轨迹分析
  - **用户活动记录**：实现用户活动跟踪
    - 社区帖子历史、评论记录、点赞统计
    - 关注用户列表和粉丝列表
    - 模板使用历史和AI互动记录
  - **隐私和安全功能**：实现隐私设置和数据管理
    - 隐私设置更新接口（PUT /api/users/privacy-settings）
    - 数据导出接口（GET /api/users/export-data）
    - 账户注销接口（DELETE /api/users/account）
  - **UserService扩展**：扩展用户服务，实现完整的用户管理业务逻辑
    - 用户资料管理、统计数据计算
    - 隐私设置管理、数据导出处理
    - 账户注销和数据清理逻辑
  - **数据规范**：所有用户数据必须来自数据库真实记录，严禁使用模拟用户信息
  - _需求: 7.1, 7.2, 7.3, 7.4, 7.5, 7.6, 7.7, 7.8, 7.9, 7.10, 7.11_

- [ ] 8.1 uni-app个人中心界面完善（基于uView组件库）
  - **ProfilePage完善**：完善个人中心主页面
    - 使用u-avatar显示头像，支持点击更换
    - 使用u-text展示用户基本信息
    - 使用u-tag显示恋爱状态，配置温暖色系
    - 使用u-cell-group组织功能入口布局
    - 添加数据洞察、活动记录、设置等功能入口
  - **EditProfilePage**：创建资料编辑页面
    - 使用u-form创建完整的资料编辑表单
    - 使用u-input收集用户信息（用户名、年龄、位置等）
    - 使用u-upload处理头像上传功能
    - 使用u-modal显示恋爱状态切换确认对话框
    - 集成用户资料更新API
  - **DataInsightsPage**：创建数据洞察页面
    - 使用u-card展示个性化统计卡片
    - 使用u-progress显示模板使用进度
    - 使用u-circle-progress显示AI对话次数等统计数据
    - 展示社区互动数据和成长轨迹
    - 集成用户洞察API获取真实统计数据
  - **MyActivityPage**：创建用户活动记录页面
    - 使用u-list展示用户活动历史
    - 使用u-time-line显示活动时间轴
    - 展示社区帖子、评论历史、点赞记录
    - 显示关注用户列表和互动统计
  - **SettingsPage**：创建设置页面
    - 使用u-cell-group创建设置选项布局
    - 使用u-switch控制隐私开关
    - 使用u-radio选择偏好设置
    - 实现数据导出和账户注销功能
    - 集成隐私设置API
  - **状态管理完善**：完善用户相关的状态管理
    - 扩展UserStore管理更多用户数据
    - 实现用户统计数据的缓存和更新
    - 处理隐私设置和偏好配置
  - **数据规范**：所有个人数据必须从API获取真实数据，严禁使用模拟个人信息
  - _需求: 7.1, 7.2, 7.3, 7.4, 7.5, 7.6, 7.7, 7.8, 7.9_

## 阶段9：系统优化和完善

- [ ] 9. 性能优化和错误处理
  - **Redis缓存策略**：实现系统缓存优化
    - 用户会话缓存、JWT令牌黑名单管理
    - 推荐算法结果缓存、热门内容缓存
    - AI评分结果缓存、用户统计数据缓存
    - 实现缓存失效和更新策略
  - **数据库查询优化**：优化数据库性能
    - 添加必要的索引（用户邮箱、帖子时间、消息查询等）
    - 优化复杂查询和分页查询
    - 实现数据库连接池优化
  - **全局异常处理**：实现统一的错误处理机制
    - 使用@Slf4j创建GlobalExceptionHandler
    - 处理认证异常、业务异常、系统异常
    - 统一错误响应格式和错误码
  - **uni-app错误处理**：实现前端错误处理
    - 使用Vue 3实现前端ErrorHandler工具类
    - 网络错误处理、API错误处理
    - 用户友好的错误提示和重试机制
  - **图片处理优化**：优化文件上传和处理
    - 实现图片压缩和格式转换
    - 文件大小限制和类型验证
    - 图片存储和CDN集成准备
  - **API性能监控**：实现性能监控和分析
    - API响应时间监控
    - 数据库查询性能分析
    - 系统资源使用监控
  - _需求: 系统性能和稳定性_

- [ ] 9.1 最终集成测试和部署准备
  - **功能完整性验证**：验证所有功能模块的完整性
    - 认证流程端到端测试
    - 模板功能完整性测试
    - 社区功能交互测试
    - 聊天功能实时通信测试
    - 个人中心功能测试
  - **数据流验证**：验证前后端数据流的正确性
    - API接口数据格式验证
    - 数据库数据一致性检查
    - 缓存数据同步验证
  - **跨平台兼容性测试**：确保uni-app跨平台兼容性
    - H5平台功能测试
    - 微信小程序平台测试
    - APP平台基础功能验证
  - **性能基准测试**：建立系统性能基准
    - API响应时间基准
    - 数据库查询性能基准
    - 前端页面加载性能基准
  - **安全性检查**：进行基础安全性验证
    - JWT令牌安全性验证
    - API接口权限验证
    - 数据输入验证和SQL注入防护
  - **部署配置准备**：准备生产环境部署配置
    - 环境变量配置模板
    - 数据库初始化脚本
    - 应用配置文件模板
  - _需求: 系统部署和质量保证_

## 重要提醒

### 🚫 严禁使用模拟数据
在执行任何任务时，必须严格遵守以下规范：

1. **后端开发**：
   - 所有API接口必须返回数据库中的真实数据
   - 不得使用硬编码的示例数据或模拟响应
   - AI生成的内容必须先保存到数据库再返回

2. **uni-app前端开发**：
   - 所有组件必须绑定API返回的真实数据
   - 不得使用硬编码的测试数据或占位符数据
   - 列表、表单、图表等都必须展示真实的业务数据

3. **开发测试**：
   - 即使在开发阶段也必须使用真实数据库
   - 通过数据库脚本或管理界面创建测试数据
   - 不得通过代码模拟数据来绕过数据库操作

### ✅ 正确的数据流程
```
数据库 → Repository → Service → Controller → API → uni-app组件 → 用户界面
```

### 🎨 uView组件库质量检查清单
在完成任何uni-app前端任务时，必须确保：

- [ ] 所有UI元素基于uView组件库实现，严禁自定义样式
- [ ] 使用温暖橙粉渐变主色调作为主要品牌色
- [ ] 所有按钮使用u-button组件配置温暖渐变背景
- [ ] 所有表单使用u-form和u-input组件
- [ ] 所有列表使用u-list和u-list-item组件
- [ ] 所有卡片使用u-card组件配置温暖色系阴影
- [ ] 所有动画使用uView内置动画组件
- [ ] 所有文案使用英文，体现国际化品质
- [ ] 确保跨平台适配和响应式设计
- [ ] 所有数据来自真实API，严禁使用模拟数据

违反以上规范的代码将不被接受，必须重新实现。