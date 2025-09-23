# 设计文档

## 概述

HeartPlan是一款面向18-35岁用户的AI驱动恋爱规划应用。系统为寻求恋爱关系的单身用户和希望加强感情纽带的情侣提供智能模板、数据分析和个性化指导。应用具有基于uView组件库的现代化、充满活力的UI界面，支持英文本地化，提供24/7 AI教练服务、用于跟踪约会进展和关系健康的智能模板，以及一个支持性的社区平台。

## 核心设计原则

### 数据处理规范
- **严格禁止模拟数据**：前端和后端都不得使用任何形式的模拟数据（Mock Data）
- **真实数据要求**：所有数据必须来自真实的API接口响应和数据库记录
- **数据流规范**：MySQL数据库 → 后端API → uni-app前端界面，确保数据的真实性和一致性
- **AI数据处理**：AI生成的内容必须先保存到数据库，然后通过API返回真实数据

### UI设计规范
- **uView组件库强制使用**：所有UI元素必须基于uView组件库实现，严禁编写自定义CSS样式
- **温暖信任色系**：使用#FF6B6B到#FF8E8E的温暖橙粉渐变作为主色调
- **辅助色彩系统**：柔和粉色系（#FFB3BA柔和粉、#FF9AA2珊瑚色、#FFAAA5温暖橙）
- **中性色彩**：#2C3E50深蓝灰、#7F8C8D浅灰、#FEFEFE纯白、#FFF5F5淡粉背景
- **功能色彩**：#00B894匹配成功、#00D68F在线状态、#636E72离线状态
- **英文UI文案**：所有界面文案必须使用英文，体现国际化品质
- **现代化设计语言**：采用uView卡片式设计、合理留白、优雅渐变效果

### 技术栈约束
- **移动端**：uni-app + Vue 3 + JavaScript（严禁使用TypeScript）
- **后端**：Java 17 + Spring Boot + Lombok + MySQL + Redis
- **UI组件**：强制使用uView组件库，组件优先级：u-button > u-card > u-list > u-form > u-input
- **uView组件规范**：所有交互元素必须使用uView提供的动画效果和交互反馈

## uView组件库设计规范

### 强制使用uView组件
根据需求1的验收标准，所有UI元素必须基于uView组件库实现，严禁编写自定义CSS样式。

#### 基础组件规范
- **u-button**：所有按钮必须使用此组件，配置温暖橙粉色系主题
- **u-icon**：所有图标使用uView图标库，保持视觉统一性
- **u-image**：图片展示统一使用此组件，支持懒加载和错误处理
- **u-text**：文本展示使用此组件，支持主题色彩配置

#### 表单组件规范
- **u-form**：表单容器，配置验证规则和温暖色系主题
- **u-input**：输入框组件，配置温暖色系聚焦效果
- **u-textarea**：多行文本输入，保持与input样式一致
- **u-radio**：单选框使用温暖橙粉色系
- **u-checkbox**：复选框使用主题色彩配置
- **u-switch**：开关组件使用品牌主色调
- **u-picker**：选择器组件使用统一主题

#### 数据展示组件规范
- **u-card**：卡片容器，配置温暖背景和圆角阴影
- **u-list**：列表容器，支持下拉刷新和上拉加载
- **u-list-item**：列表项，配置统一的交互效果
- **u-avatar**：头像组件，配置温暖边框效果
- **u-badge**：徽标组件，使用主题色彩
- **u-tag**：标签组件，使用品牌色彩系统
- **u-rate**：评分组件，使用温暖橙色星星

#### 反馈组件规范
- **u-modal**：模态框，配置温暖色系按钮和圆角
- **u-toast**：轻提示，使用品牌色彩
- **u-loading**：加载组件，配置温暖色系动画
- **u-alert**：警告提示，使用温暖色系配置
- **u-action-sheet**：操作菜单，配置温暖色系

#### 导航组件规范
- **u-navbar**：导航栏，配置渐变背景
- **u-tabbar**：底部导航，使用温暖色系激活状态
- **u-tabs**：标签页，配置品牌色彩指示器

#### uView主题配置
- **全局主题**：在main.js中配置uView全局主题色彩
- **组件级配置**：通过custom-style属性配置单个组件样式
- **温暖色系应用**：所有组件必须应用温暖信任系色彩方案
- **统一视觉风格**：确保所有uView组件保持一致的视觉风格
- **响应式适配**：利用uView的响应式能力适配不同屏幕尺寸
- **动画效果**：使用uView内置动画，不得自定义动画样式

## 架构设计

### 系统架构概览
```
uni-app + Vue 3 前端
        ↓
Spring Boot API服务
        ↓
MySQL数据库 + Redis缓存
        ↓
AI API服务集成
```

### 核心模块
- **用户认证**: 两步注册、JWT令牌管理
- **AI智能模板**: 单身/情侣模板、AI评分系统
- **社区功能**: 四个Tab分类、互动功能、AI内容审核
- **聊天系统**: AI教练、私聊功能、实时消息
- **个人中心**: 资料管理、数据统计、隐私设置

## 功能模块设计

### 用户认证系统设计（需求2）

#### 登录界面设计
- **技术实现**：基于uView组件库的u-form、u-input、u-button组件
- **UI设计**：温暖橙粉渐变背景，使用英文文案，严格遵循uView组件设计规范
- **验证逻辑**：邮箱格式验证，3秒内完成登录响应（需求2.1, 2.2）
- **状态管理**：使用Pinia管理登录状态和用户信息
- **uView组件应用**：使用u-form进行表单验证，u-input配置温暖色系聚焦效果，u-button配置温暖橙粉渐变主题

#### 注册流程设计
- **两步骤注册**：
  - 第一步：邮箱、年龄、性别、密码（需求2.3, 2.4）
  - 第二步：恋爱状态、用户名、头像、位置信息（需求2.5, 2.6）
- **头像系统**：随机生成头像选项 + 自定义上传功能（需求2.7）
- **完成流程**：直接进入产品主界面（需求2.8）
- **uView组件集成**：使用u-picker选择年龄和性别，u-radio选择恋爱状态，u-avatar展示头像选项

### 主页面导航设计（需求3）

#### 底部导航栏设计
- **uView组件**：使用u-tabbar组件实现底部导航，配置温暖橙粉色系主题
- **导航结构**：AI智能模板、社区功能、聊天功能、我的（需求3.3）
- **默认页面**：登录后默认进入AI智能模板功能模块（需求3.1）
- **交互设计**：2秒内切换响应，高亮显示当前活跃选项（需求3.4, 3.5）
- **持久显示**：底部导航栏在所有功能模块中始终可见（需求3.6）
- **uView样式配置**：使用u-icon组件显示导航图标，活跃状态使用温暖橙粉渐变色
- **英文标签**：所有导航标签使用英文文案（AI Templates、Community、Chat、Profile）

### AI智能模板系统设计（需求4）

#### 模板系统架构
- **状态驱动**：根据用户恋爱状态自动显示相应模板集合（需求4.1）
- **模板导航**：使用u-card组件创建清晰的模板导航界面（需求4.2）
- **状态切换**：支持恋爱状态改变时的模板集合切换和历史数据保留（需求4.12）
- **uView组件集成**：使用u-list展示模板列表，u-badge显示模板状态

#### 单身用户模板设计
- **约会管理表**：
  - 字段：姓名、认识日期、AI兼容性评分、最后联系、状态
  - AI评分：基于输入信息生成0-100%兼容性评分（需求4.4）
  - **uView实现**：使用u-table展示约会记录，u-rate组件显示兼容性评分，u-tag显示状态
- **约会记录追踪器**：
  - 记录：地点、活动、对话亮点、约会后评估
  - AI分析：24小时内提供表现分析和改进建议（需求4.5）
  - **uView实现**：使用u-form收集约会信息，u-textarea记录详细内容，u-alert显示AI建议
- **个人魅力提升清单**：
  - 四个类别：外表、沟通、兴趣、个人成长
  - AI建议：生成改进建议并跟踪完成进度（需求4.6）
  - **uView实现**：使用u-collapse展示分类，u-checkbox跟踪完成状态，u-progress显示进度

#### 情侣用户模板设计
- **关系管理仪表板**：
  - 10分制关系健康评分、沟通质量评估
  - 警报系统：数据下降趋势时生成改进建议（需求4.8）
  - **uView实现**：使用u-circle-progress显示健康评分，u-notice-bar显示警报信息
- **重要日期提醒表**：AI生成庆祝建议和提前提醒功能（需求4.9）
  - **uView实现**：使用u-calendar选择日期，u-card展示庆祝建议，u-badge显示提醒数量
- **冲突解决记录**：
  - 记录：详情、情绪、解决过程
  - AI分析：模式分析和预防建议（需求4.10）
  - **uView实现**：使用u-form记录冲突信息，u-slider评估情绪强度，u-time-line展示解决过程
- **恋爱成长轨迹图**：可视化关系发展进展并预测未来趋势（需求4.11）
  - **uView实现**：使用u-chart组件（如果可用）或自定义图表展示成长轨迹

### 社区功能设计（需求5）

#### Tab导航设计
- **uView组件**：使用u-tabs组件实现顶部tab导航，配置温暖橙粉色系主题
- **四个分类**：推荐、成功故事、问题求助、每周话题（需求5.2）
- **切换响应**：2秒内切换到相应内容分类（需求5.3）
- **英文标签**：Recommended、Success Stories、Help Forum、Weekly Topics

#### 推荐系统设计
- **智能推荐**：基于用户恋爱状态、兴趣和互动历史（需求5.1, 5.4）
- **混合内容**：成功故事、热门问题、话题讨论等
- **刷新机制**：使用u-list的下拉刷新功能重新加载个性化推荐内容（需求5.18）
- **uView实现**：使用u-list展示推荐内容，u-loading显示加载状态

#### 内容分类设计
- **成功故事**：按相关性过滤的匿名成功故事，AI审查质量并分类（需求5.5, 5.6）
  - **uView实现**：使用u-card展示故事卡片，u-tag标识故事类别
- **问题求助**：AI回应 + 社区成员回复，推荐类似已解决问题（需求5.7, 5.8, 5.9）
  - **uView实现**：使用u-collapse展示问题详情，u-badge显示回复数量
- **每周话题**：AI基于热门主题和用户统计创建讨论提示（需求5.10）
  - **uView实现**：使用u-notice-bar展示话题提示，u-card展示讨论内容

#### 社区互动设计
- **基础互动**：点赞、评论、分享功能（需求5.11）
  - **uView实现**：使用u-icon显示互动按钮，u-button配置温暖色系点击效果
- **实时更新**：点赞数量实时更新，记录用户点赞历史（需求5.12）
  - **uView实现**：使用u-badge显示点赞数量，u-toast提供操作反馈
- **评论系统**：文本评论 + 回复评论形成讨论串（需求5.13）
  - **uView实现**：使用u-input收集评论，u-list展示评论列表，u-divider分隔回复层级
- **用户关注**：关注功能，优先显示关注用户的新内容（需求5.14）
  - **uView实现**：使用u-button配置关注按钮样式，u-avatar显示用户头像
- **私聊集成**：发送私聊消息，集成到聊天功能模块（需求5.15, 5.16, 5.17）
  - **uView实现**：使用u-modal弹出私聊选项，u-action-sheet提供操作菜单

### 聊天功能系统设计（需求6）

#### 聊天架构设计
- **联系人列表**：AI教练 + 所有私聊用户（需求6.1）
- **AI教练优先**：始终显示在列表顶部，标识为特殊联系人（需求6.2）
- **uView实现**：使用u-list展示联系人，u-avatar显示头像，u-badge显示未读消息数量

#### AI教练系统设计
- **响应时间**：5秒内AI可用（需求6.3）
- **个性化回应**：基于用户恋爱状态和模板数据
- **咨询模式**：危机支持、约会建议、关系指导、进展回顾（需求6.4）
- **uView实现**：使用u-tag标识AI教练身份，u-loading显示AI思考状态

#### 私聊功能设计
- **聊天界面**：显示完整聊天历史（需求6.5）
  - **uView实现**：使用u-list展示消息列表，u-card包装消息气泡
- **消息类型**：支持文本和图片消息，实时传递（需求6.6）
  - **uView实现**：使用u-text显示文本消息，u-image展示图片消息
- **消息状态**：发送中、已送达、已读状态指示（需求6.8）
  - **uView实现**：使用u-icon显示状态图标，u-loading显示发送中状态
- **消息操作**：长按提供删除、复制等操作选项（需求6.9）
  - **uView实现**：使用u-action-sheet弹出操作菜单
- **图片功能**：图片压缩和预览功能（需求6.10）
  - **uView实现**：使用u-upload处理图片上传，u-modal预览图片
- **通知系统**：未读消息数量和最新消息预览（需求6.7）
  - **uView实现**：使用u-badge显示未读数量，u-text显示消息预览

### 个人中心设计（需求7）

#### 基本信息展示
- **用户信息**：头像、用户名、恋爱状态、注册时间（需求7.1）
  - **uView实现**：使用u-avatar显示头像，u-text展示用户信息，u-tag显示恋爱状态
- **资料编辑**：修改用户名、头像、恋爱状态、所在地（需求7.2）
  - **uView实现**：使用u-form创建编辑表单，u-input收集信息，u-upload处理头像上传
- **状态切换**：提示模板集合影响，确认后自动切换功能（需求7.3）
  - **uView实现**：使用u-modal显示确认对话框，u-alert提示影响说明

#### 数据分析功能
- **恋爱洞察报告**：模板使用统计、AI对话次数、社区互动数据（需求7.4）
  - **uView实现**：使用u-card展示统计卡片，u-progress显示使用进度
- **活动记录**：社区帖子、评论历史、点赞记录、关注用户列表（需求7.5）
  - **uView实现**：使用u-list展示活动列表，u-time-line显示时间轴
- **关注管理**：关注用户列表，快速访问其最新内容（需求7.6）
  - **uView实现**：使用u-list展示关注列表，u-avatar显示用户头像，u-button提供快速访问
- **使用统计**：AI对话次数、模板使用情况、社区互动频率（需求7.8）
  - **uView实现**：使用u-circle-progress显示统计数据，u-chart展示趋势图表

#### 隐私和安全功能
- **隐私设置**：个人信息可见性、消息接收偏好、数据使用授权（需求7.7）
  - **uView实现**：使用u-switch控制隐私开关，u-radio选择偏好设置
- **帮助中心**：常见问题解答、使用教程、客服联系方式（需求7.9）
  - **uView实现**：使用u-collapse展示FAQ，u-cell-group组织帮助内容
- **数据管理**：下载个人数据功能，包括模板记录、聊天历史（需求7.10）
  - **uView实现**：使用u-button触发数据导出，u-loading显示处理状态
- **账户注销**：安全的账户删除流程，说明数据保留政策（需求7.11）
  - **uView实现**：使用u-modal显示注销确认，u-alert展示政策说明

### 后端架构设计

#### Spring Boot单体架构
```
HeartPlan Spring Boot Application
├── Controller Layer - REST API接口
├── Service Layer - 业务逻辑处理
├── Repository Layer - 数据访问
└── Configuration Layer - 系统配置
```

#### 核心模块
- **认证模块**: 用户注册登录、JWT令牌管理
- **用户管理**: 资料管理、恋爱状态、隐私设置
- **模板功能**: 单身/情侣模板、AI评分算法
- **社区功能**: 帖子管理、推荐算法、互动功能
- **聊天功能**: 实时消息、AI教练对话
- **AI集成**: AI服务集成、内容审核
- **文件处理**: 图片上传、头像管理

## 功能模块设计

### 用户认证系统
- **两步注册流程**: 基础信息 → 个人资料
- **登录验证**: 邮箱密码认证，3秒内响应
- **JWT令牌管理**: 安全的身份验证

### 主页面导航
- **底部导航**: AI智能模板、社区功能、聊天功能、我的
- **默认首页**: AI智能模板模块
- **2秒切换响应**: 高亮显示当前活跃模块

### AI智能模板系统
#### 单身用户模板
- **约会管理表**: AI兼容性评分（0-100%）
- **约会记录追踪器**: 24小时内AI分析和建议
- **个人魅力提升清单**: 四个类别的改进建议

#### 情侣用户模板
- **关系管理仪表板**: 10分制健康评分
- **重要日期提醒表**: AI生成庆祝建议
- **冲突解决记录**: AI模式分析
- **恋爱成长轨迹图**: 关系发展可视化

### 社区功能系统
- **四个Tab**: 推荐、成功故事、问题求助、每周话题
- **智能推荐**: 基于用户状态和互动历史
- **社区互动**: 点赞、评论、分享、关注
- **私聊集成**: 从社区直接发起私聊

### 聊天功能系统
- **联系人列表**: AI教练置顶显示
- **AI教练**: 5秒内响应，个性化建议
- **私聊功能**: 文本图片消息，实时传递
- **消息状态**: 发送中、已送达、已读

### 个人中心
- **基本信息**: 头像、用户名、恋爱状态
- **数据洞察**: 模板使用统计、AI对话次数
- **隐私设置**: 信息可见性、消息偏好
- **数据管理**: 导出功能、账户注销

## 数据模型设计

### 核心实体
- **User**: 用户基本信息、恋爱状态、偏好设置
- **DatingRecord**: 约会记录、AI兼容性评分、分析结果
- **CommunityPost**: 社区帖子、类型分类、互动统计
- **ChatMessage**: 聊天消息、消息状态、AI标识
- **RelationshipDashboard**: 关系仪表板、健康评分、趋势分析

### 关系设计
- **UserFollow**: 用户关注关系
- **PostLike**: 帖子点赞关系
- **Comment**: 评论和回复关系

### 数据访问层
- **Repository接口**: 继承JpaRepository，提供基础CRUD操作
- **自定义查询**: 基于业务需求的特定查询方法
- **分页支持**: 列表数据的分页查询

## 接口设计

### RESTful API规范
- **认证接口**: `/api/auth` - 注册、登录、令牌刷新
- **模板接口**: `/api/templates` - 约会管理、关系仪表板
- **社区接口**: `/api/community` - 帖子管理、推荐内容、互动功能
- **聊天接口**: `/api/chat` - 消息发送、联系人管理、AI教练
- **用户接口**: `/api/users` - 用户资料、隐私设置、数据导出

### WebSocket实时通信
- **聊天消息**: 实时消息传递和状态更新
- **在线状态**: 用户在线状态同步
- **通知推送**: 实时通知和提醒

## 组件和接口

### 移动端组件架构（uni-app + Vue 3 + uView）

#### 导航结构设计
基于uni-app + Vue 3 + uView组件库技术栈，使用uni-app内置路由系统和uView导航组件构建的现代化导航系统：
```javascript
// uni-app + Vue 3 + uView 应用架构
App.vue (使用Vue 3 Composition API + uView主题配置)
├── AuthFlow（认证流程 - 基于uView表单组件）
│   ├── LoginPage（登录页面 - u-form + u-input + u-button，英文UI）
│   └── RegisterPage（两步注册 - u-form + u-picker + u-radio，温暖色系）
└── MainApp（u-tabbar底部导航 - 四个主要功能模块）
    ├── AITemplatesFlow（AI智能模板流程 - u-card + u-list）
    │   ├── TemplateHomePage（u-card模板首页 - 根据恋爱状态显示）
    │   ├── SinglesTemplatesPage（u-list单身模板 - 三个核心模板）
    │   └── CouplesTemplatesPage（u-card情侣模板 - 四个核心模板）
    ├── CommunityFlow（社区功能流程 - u-tabs导航设计）
    │   ├── CommunityHomePage（u-tabs社区首页 - 四个Tab标签）
    │   │   ├── RecommendationTab（u-list推荐Tab - 智能推荐内容）
    │   │   ├── SuccessStoriesTab（u-card成功故事Tab - 匿名故事）
    │   │   ├── HelpForumTab（u-collapse问题求助Tab - AI回应+社区回复）
    │   │   └── WeeklyTopicsTab（u-notice-bar每周话题Tab - AI生成话题）
    │   ├── PostDetailPage（u-card帖子详情 - u-icon点赞评论分享）
    │   ├── CreatePostPage（u-form发布帖子 - AI审核分类）
    │   └── UserProfilePage（u-avatar用户资料 - u-button关注功能）
    ├── ChatFlow（聊天功能流程 - u-list + u-card统一聊天系统）
    │   ├── ChatListPage（u-list联系人列表 - u-badge AI教练置顶）
    │   ├── AICoachPage（u-card AI教练对话 - u-loading 5秒内响应）
    │   └── PrivateChatPage（u-list私聊界面 - u-text + u-image支持）
    └── ProfileFlow（个人中心流程 - u-card + u-cell-group我的功能）
        ├── ProfilePage（u-avatar个人资料 - u-text基本信息展示）
        ├── EditProfilePage（u-form编辑资料 - u-switch恋爱状态切换）
        ├── SettingsPage（u-cell-group设置页面 - u-switch隐私设置）
        └── DataInsightsPage（u-progress数据洞察 - u-chart个性化报告）
```

#### 核心uView组件应用

##### 关键组件配置
- **u-button**: 配置温暖橙粉渐变主题，支持加载状态和按下缩放效果
- **u-card**: 现代化卡片布局，温暖色调阴影，点击反馈效果
- **u-form + u-input**: 表单系统，浮动标签动画，温暖色系聚焦效果
- **u-tabs**: 胶囊式Tab设计，活跃状态渐变背景，滑动指示器动画
- **u-list**: 列表容器，支持下拉刷新和上拉加载，配置温暖主题
- **u-avatar**: 头像组件，温暖边框效果，在线状态指示器
- **u-modal**: 模态框，温暖色系按钮，圆角设计
- **u-tabbar**: 底部导航，温暖色系激活状态，徽章数字显示

### 后端API接口

#### 认证服务API
- **登录接口**: POST `/api/auth/login` - 邮箱密码验证，返回JWT令牌
- **注册接口**: POST `/api/auth/register` - 两步注册流程，创建用户账户
- **令牌刷新**: POST `/api/auth/refresh` - 刷新访问令牌

#### 模板功能API
- **约会管理**: GET/POST `/api/templates/dating-management` - 约会记录CRUD
- **AI评分**: POST `/api/templates/dating-management/{id}/compatibility-score` - 兼容性评分
- **关系仪表板**: GET/PUT `/api/templates/relationship-dashboard` - 关系健康评分

#### 社区功能API
- **推荐内容**: GET `/api/community/recommendations` - 个性化推荐
- **帖子管理**: GET/POST `/api/community/posts` - 帖子CRUD操作
- **互动功能**: POST `/api/community/posts/{id}/like` - 点赞评论分享

#### 聊天功能API
- **联系人列表**: GET `/api/chat/contacts` - 获取聊天联系人
- **消息发送**: POST `/api/chat/conversations/{userId}/messages` - 发送消息
- **AI教练**: POST `/api/chat/ai-coach/conversation` - AI对话接口 "Username cannot be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2-50 characters")
    private String username;
    
    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 35, message = "Age must be at most 35")
    private Integer age;
    
    @NotBlank(message = "Gender is required")
    private String gender;
    
    @NotBlank(message = "Relationship status is required")
    private String relationshipStatus; // SINGLE 或 IN_RELATIONSHIP
    
    private String avatarUrl; // 可选，如果不提供则使用默认头像
    
    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;
}

// 注册响应DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserProfile userProfile;
    private String message = "Registration successful";
}
```

#### 用户服务API
```java
@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {
    
    private final UserService userService;
    
    // 获取用户资料
    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getProfile(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        log.debug("获取用户资料: userId={}", userId);
        UserProfile profile = userService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }
    
    // 更新用户资料
    @PutMapping("/profile")
    public ResponseEntity<UserProfile> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        log.info("更新用户资料: userId={}", userId);
        UserProfile profile = userService.updateProfile(userId, request);
        return ResponseEntity.ok(profile);
    }
    
    // 更新恋爱状态
    @PutMapping("/relationship-status")
    public ResponseEntity<Void> updateRelationshipStatus(
            @Valid @RequestBody RelationshipStatusRequest request,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        log.info("更新恋爱状态: userId={}, status={}", userId, request.getStatus());
        userService.updateRelationshipStatus(userId, request);
        return ResponseEntity.ok().build();
    }
    
    // 获取用户洞察数据
    @GetMapping("/insights")
    public ResponseEntity<UserInsights> getUserInsights(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        log.debug("获取用户洞察数据: userId={}", userId);
        UserInsights insights = userService.getUserInsights(userId);
        return ResponseEntity.ok(insights);
    }
    
    private Long getUserIdFromAuth(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId();
    }
}

// 用户资料DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    private Long id;
    private String email;
    private String username;
    private Integer age;
    private String gender;
    private String relationshipStatus;
    private String avatarUrl;
    private String location;
    private String bio;
    private LocalDateTime createdAt;
    private UserPreferences preferences;
    private PrivacySettings privacySettings;
}

// 更新资料请求DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {
    @Size(min = 2, max = 50, message = "Username must be between 2-50 characters")
    private String username;
    
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be at most 100")
    private Integer age;
    
    private String avatarUrl;
    
    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;
    
    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;
    
    private UserPreferences preferences;
    private PrivacySettings privacySettings;
}
```

#### 模板服务API
```java
@RestController
@RequestMapping("/api/templates")
public class TemplateController {
    
    // ========== 单身用户模板API ==========
    
    // 获取约会管理记录列表
    @GetMapping("/dating-management")
    public ResponseEntity<PagedResponse<DatingRecord>> getDatingRecords(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String status
    );
    
    // 创建约会记录
    @PostMapping("/dating-management")
    public ResponseEntity<DatingRecord> createDatingRecord(@RequestBody DatingRecordRequest request);
    
    // 更新约会记录
    @PutMapping("/dating-management/{id}")
    public ResponseEntity<DatingRecord> updateDatingRecord(
        @PathVariable Long id, 
        @RequestBody DatingRecordRequest request
    );
    
    // 删除约会记录
    @DeleteMapping("/dating-management/{id}")
    public ResponseEntity<Void> deleteDatingRecord(@PathVariable Long id);
    
    // 获取AI兼容性评分
    @PostMapping("/dating-management/{id}/compatibility-score")
    public ResponseEntity<CompatibilityScoreResponse> calculateCompatibilityScore(
        @PathVariable Long id,
        @RequestBody CompatibilityRequest request
    );
    
    // 获取约会记录追踪器数据
    @GetMapping("/dating-tracker")
    public ResponseEntity<List<DateTrackingRecord>> getDateTrackingRecords();
    
    // 创建约会追踪记录
    @PostMapping("/dating-tracker")
    public ResponseEntity<DateTrackingRecord> createDateTrackingRecord(
        @RequestBody DateTrackingRequest request
    );
    
    // 获取AI约会分析和建议
    @GetMapping("/dating-tracker/{id}/analysis")
    public ResponseEntity<DateAnalysisResponse> getDateAnalysis(@PathVariable Long id);
    
    // 获取个人魅力提升清单
    @GetMapping("/charm-enhancement")
    public ResponseEntity<CharmEnhancementList> getCharmEnhancementList();
    
    // 更新魅力提升项目完成状态
    @PutMapping("/charm-enhancement/{itemId}/complete")
    public ResponseEntity<Void> completeCharmEnhancementItem(@PathVariable Long itemId);
    
    // 获取AI生成的魅力提升建议
    @PostMapping("/charm-enhancement/suggestions")
    public ResponseEntity<List<CharmSuggestion>> getCharmSuggestions(
        @RequestBody CharmSuggestionRequest request
    );
    
    // ========== 情侣用户模板API ==========
    
    // 获取关系管理仪表板
    @GetMapping("/relationship-dashboard")
    public ResponseEntity<RelationshipDashboard> getRelationshipDashboard();
    
    // 更新关系健康评分
    @PostMapping("/relationship-dashboard/health-score")
    public ResponseEntity<HealthScoreResponse> updateHealthScore(
        @RequestBody HealthScoreRequest request
    );
    
    // 获取关系趋势分析
    @GetMapping("/relationship-dashboard/trends")
    public ResponseEntity<RelationshipTrendsResponse> getRelationshipTrends(
        @RequestParam(defaultValue = "30") int days
    );
    
    // 获取重要日期提醒列表
    @GetMapping("/important-dates")
    public ResponseEntity<List<ImportantDate>> getImportantDates();
    
    // 添加重要日期
    @PostMapping("/important-dates")
    public ResponseEntity<ImportantDate> addImportantDate(@RequestBody ImportantDateRequest request);
    
    // 更新重要日期
    @PutMapping("/important-dates/{id}")
    public ResponseEntity<ImportantDate> updateImportantDate(
        @PathVariable Long id,
        @RequestBody ImportantDateRequest request
    );
    
    // 删除重要日期
    @DeleteMapping("/important-dates/{id}")
    public ResponseEntity<Void> deleteImportantDate(@PathVariable Long id);
    
    // 获取AI庆祝建议
    @GetMapping("/important-dates/{id}/celebration-suggestions")
    public ResponseEntity<List<CelebrationSuggestion>> getCelebrationSuggestions(@PathVariable Long id);
    
    // 获取冲突解决记录
    @GetMapping("/conflict-resolution")
    public ResponseEntity<List<ConflictRecord>> getConflictRecords();
    
    // 记录冲突解决
    @PostMapping("/conflict-resolution")
    public ResponseEntity<ConflictRecord> recordConflict(@RequestBody ConflictRequest request);
    
    // 获取AI冲突分析和预防建议
    @GetMapping("/conflict-resolution/{id}/analysis")
    public ResponseEntity<ConflictAnalysisResponse> getConflictAnalysis(@PathVariable Long id);
    
    // 获取恋爱成长轨迹图数据
    @GetMapping("/growth-trajectory")
    public ResponseEntity<GrowthTrajectoryResponse> getGrowthTrajectory(
        @RequestParam(defaultValue = "365") int days
    );
    
    // 更新成长轨迹数据点
    @PostMapping("/growth-trajectory/data-point")
    public ResponseEntity<Void> addGrowthDataPoint(@RequestBody GrowthDataPointRequest request);
}
```

#### 社区服务API
```java
@RestController
@RequestMapping("/api/community")
public class CommunityController {
    
    // ========== 帖子管理API ==========
    
    // 获取社区帖子列表
    @GetMapping("/posts")
    public ResponseEntity<PagedResponse<CommunityPost>> getPosts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String sortBy
    );
    
    // 获取单个帖子详情
    @GetMapping("/posts/{id}")
    public ResponseEntity<CommunityPostDetail> getPost(@PathVariable Long id);
    
    // 发布新帖子
    @PostMapping("/posts")
    public ResponseEntity<CommunityPost> createPost(@RequestBody CreatePostRequest request);
    
    // 更新帖子
    @PutMapping("/posts/{id}")
    public ResponseEntity<CommunityPost> updatePost(
        @PathVariable Long id,
        @RequestBody UpdatePostRequest request
    );
    
    // 删除帖子
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id);
    
    // ========== 互动功能API ==========
    
    // 点赞/取消点赞帖子
    @PostMapping("/posts/{id}/like")
    public ResponseEntity<LikeResponse> toggleLike(@PathVariable Long id);
    
    // 收藏/取消收藏帖子
    @PostMapping("/posts/{id}/favorite")
    public ResponseEntity<FavoriteResponse> toggleFavorite(@PathVariable Long id);
    
    // 分享帖子
    @PostMapping("/posts/{id}/share")
    public ResponseEntity<ShareResponse> sharePost(@PathVariable Long id);
    
    // 举报帖子
    @PostMapping("/posts/{id}/report")
    public ResponseEntity<Void> reportPost(
        @PathVariable Long id,
        @RequestBody ReportRequest request
    );
    
    // ========== 评论系统API ==========
    
    // 获取帖子评论列表
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<PagedResponse<Comment>> getComments(
        @PathVariable Long postId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    );
    
    // 发表评论
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> createComment(
        @PathVariable Long postId,
        @RequestBody CreateCommentRequest request
    );
    
    // 回复评论
    @PostMapping("/comments/{commentId}/replies")
    public ResponseEntity<Comment> replyToComment(
        @PathVariable Long commentId,
        @RequestBody ReplyCommentRequest request
    );
    
    // 点赞评论
    @PostMapping("/comments/{id}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Long id);
    
    // 删除评论
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id);
    
    // ========== 用户关系API ==========
    
    // 关注用户
    @PostMapping("/users/{userId}/follow")
    public ResponseEntity<FollowResponse> followUser(@PathVariable Long userId);
    
    // 取消关注用户
    @DeleteMapping("/users/{userId}/follow")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long userId);
    
    // 获取关注列表
    @GetMapping("/users/{userId}/following")
    public ResponseEntity<PagedResponse<UserProfile>> getFollowing(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );
    
    // 获取粉丝列表
    @GetMapping("/users/{userId}/followers")
    public ResponseEntity<PagedResponse<UserProfile>> getFollowers(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );
    
    // ========== Tab导航和推荐API ==========
    
    // 获取推荐Tab内容（默认首页）
    @GetMapping("/recommendations")
    public ResponseEntity<PagedResponse<CommunityPost>> getRecommendations(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String refreshToken
    );
    
    // 刷新推荐内容
    @PostMapping("/recommendations/refresh")
    public ResponseEntity<PagedResponse<CommunityPost>> refreshRecommendations();
    
    // 获取成功故事Tab内容
    @GetMapping("/success-stories")
    public ResponseEntity<PagedResponse<CommunityPost>> getSuccessStories(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String filterBy
    );
    
    // 获取问题求助Tab内容
    @GetMapping("/help-forum")
    public ResponseEntity<PagedResponse<CommunityPost>> getHelpForumPosts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String status
    );
    
    // 获取每周话题Tab内容
    @GetMapping("/weekly-topics")
    public ResponseEntity<PagedResponse<WeeklyTopic>> getWeeklyTopics(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    );
    
    // 搜索帖子（跨所有Tab）
    @GetMapping("/search/posts")
    public ResponseEntity<PagedResponse<CommunityPost>> searchPosts(
        @RequestParam String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) String tabType
    );
    
    // 获取热门话题标签
    @GetMapping("/trending/topics")
    public ResponseEntity<List<TrendingTopic>> getTrendingTopics();
    
    // 记录用户互动行为（用于推荐算法优化）
    @PostMapping("/interactions")
    public ResponseEntity<Void> recordUserInteraction(@RequestBody UserInteractionRequest request);
}
```

#### 聊天服务API
```java
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    // ========== 聊天列表管理API ==========
    
    // 获取聊天联系人列表
    @GetMapping("/contacts")
    public ResponseEntity<List<ChatContact>> getChatContacts();
    
    // 获取与特定用户的聊天历史
    @GetMapping("/conversations/{userId}/messages")
    public ResponseEntity<PagedResponse<ChatMessage>> getChatHistory(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size,
        @RequestParam(required = false) Long beforeMessageId
    );
    
    // 发送消息
    @PostMapping("/conversations/{userId}/messages")
    public ResponseEntity<ChatMessage> sendMessage(
        @PathVariable Long userId,
        @RequestBody SendMessageRequest request
    );
    
    // 标记消息为已读
    @PutMapping("/messages/{messageId}/read")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Long messageId);
    
    // 删除消息
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId);
    
    // 获取未读消息数量
    @GetMapping("/unread-count")
    public ResponseEntity<UnreadCountResponse> getUnreadCount();
    
    // ========== AI教练对话API ==========
    
    // 与AI教练开始对话
    @PostMapping("/ai-coach/conversation")
    public ResponseEntity<AICoachResponse> chatWithAICoach(@RequestBody AICoachRequest request);
    
    // 获取AI教练对话历史
    @GetMapping("/ai-coach/history")
    public ResponseEntity<List<AICoachMessage>> getAICoachHistory(
        @RequestParam(defaultValue = "50") int limit
    );
    
    // 获取AI教练建议模式
    @GetMapping("/ai-coach/modes")
    public ResponseEntity<List<AICoachMode>> getAICoachModes();
    
    // 设置AI教练对话模式
    @PostMapping("/ai-coach/mode")
    public ResponseEntity<Void> setAICoachMode(@RequestBody AICoachModeRequest request);
    
    // ========== 实时通信API ==========
    
    // WebSocket连接端点
    @MessageMapping("/chat.sendMessage")
    @SendToUser("/queue/messages")
    public ChatMessage sendRealtimeMessage(ChatMessage message);
    
    // 用户上线通知
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage message);
    
    // 正在输入状态
    @MessageMapping("/chat.typing")
    @SendToUser("/queue/typing")
    public TypingIndicator sendTypingIndicator(TypingIndicator indicator);
}
```

## 数据模型

### Lombok使用规范

在后端开发中，我们使用Lombok来简化Java代码，减少样板代码的编写：

#### Lombok核心注解使用指南
- **@Data**: 自动生成getter、setter、toString、equals、hashCode方法
- **@NoArgsConstructor**: 生成无参构造函数
- **@AllArgsConstructor**: 生成包含所有字段的构造函数
- **@Builder**: 生成建造者模式，便于对象创建
- **@Builder.Default**: 为Builder模式中的字段设置默认值
- **@Slf4j**: 自动生成日志对象，无需手动创建Logger
- **@RequiredArgsConstructor**: 为final字段和@NonNull字段生成构造函数
- **@EqualsAndHashCode**: 自定义equals和hashCode方法的生成规则

#### 实体类标准模板
```java
@Entity
@Table(name = "table_name")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class EntityName {
    // 字段定义
    // 使用@Builder.Default为需要默认值的字段设置默认值
}
```

#### Service类标准模板
```java
@Service
@Slf4j // 自动生成log对象
@RequiredArgsConstructor // 为final字段生成构造函数，实现依赖注入
public class ServiceName {
    private final RepositoryName repository;
    
    // 业务方法实现
    // 可以直接使用log.info(), log.error()等方法
}
```

### 核心实体模型

#### 用户实体（基于需求文档设计）
```java
@Entity
@Table(name = "users")
@Data // Lombok注解：自动生成getter/setter/toString/equals/hashCode
@NoArgsConstructor // Lombok注解：生成无参构造函数
@AllArgsConstructor // Lombok注解：生成全参构造函数
@Builder // Lombok注解：生成建造者模式
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 用户ID
    
    // 需求2：注册第一步 - 邮箱、年龄、性别、密码
    @Column(unique = true, nullable = false)
    private String email; // 邮箱地址，需要格式验证和重复检查
    
    @Column(nullable = false)
    private String passwordHash; // 密码哈希，使用PasswordEncoder加密
    
    @Column(nullable = false)
    @Min(18) @Max(35) // 需求2：年龄范围18-35岁
    private Integer age; // 年龄，需要范围验证
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender; // 性别：MALE, FEMALE, OTHER
    
    // 需求2：注册第二步 - 用户名、恋爱状态、头像、位置
    @Column(nullable = false)
    private String username; // 用户名
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationshipStatus relationshipStatus; // 需求2：恋爱状态 - "单身"和"恋爱中"两个选项
    
    private String avatarUrl; // 头像URL，支持随机生成或自定义上传
    private String location; // 位置信息
    
    // 需求7：个人中心功能 - 显示注册时间
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime registrationTime; // 注册时间，在个人中心显示
    
    // 需求7：隐私设置和偏好管理
    @Embedded
    private UserPreferences preferences; // 用户偏好设置
    
    @Embedded
    private PrivacySettings privacySettings; // 需求7：隐私设置 - 控制个人信息可见性、消息接收偏好
    
    // 系统统计信息
    @Builder.Default
    private Integer loginCount = 0; // 登录次数
    private LocalDateTime lastLoginAt; // 最后登录时间
    @Builder.Default
    private Boolean isActive = true; // 账户是否激活
    @Builder.Default
    private Boolean isVerified = false; // 邮箱是否验证
    
    @UpdateTimestamp
    private LocalDateTime updatedAt; // 更新时间
    
    // 关联关系 - 支持各功能模块
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DatingRecord> datingRecords; // 需求4：约会管理表
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<CommunityPost> communityPosts; // 需求5：社区帖子
    
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<ChatMessage> sentMessages; // 需求6：聊天消息
    
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<ChatMessage> receivedMessages; // 需求6：接收的消息
    
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<UserFollow> following; // 需求5：关注功能
    
    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL)
    private List<UserFollow> followers; // 需求5：粉丝列表
}

// 需求2：性别枚举
public enum Gender {
    MALE("男性"),
    FEMALE("女性"),
    OTHER("其他");
    
    @Getter
    private final String description;
    
    Gender(String description) {
        this.description = description;
    }
}

// 需求2：恋爱状态枚举 - 两个选项："单身"和"恋爱中"
public enum RelationshipStatus {
    SINGLE("单身"), // 需求4：单身用户使用三个核心模板
    IN_RELATIONSHIP("恋爱中"); // 需求4：情侣用户使用四个核心模板
    
    @Getter
    private final String description;
    
    RelationshipStatus(String description) {
        this.description = description;
    }
}

// 用户偏好设置嵌入式对象
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferences {
    @Builder.Default
    private Boolean receiveNotifications = true; // 接收通知
    @Builder.Default
    private Boolean showOnlineStatus = true; // 显示在线状态
    @Builder.Default
    private String language = "en"; // 界面语言
    @Builder.Default
    private String theme = "light"; // 主题模式
}

// 需求7：隐私设置嵌入式对象 - 控制个人信息可见性、消息接收偏好和数据使用授权
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivacySettings {
    @Builder.Default
    private Boolean profileVisible = true; // 需求7：个人信息可见性控制
    @Builder.Default
    private Boolean allowMessages = true; // 需求7：消息接收偏好 - 允许私信
    @Builder.Default
    private Boolean showActivity = true; // 需求7：显示活动状态
    @Builder.Default
    private Boolean dataUsageAuthorized = false; // 需求7：数据使用授权
    @Builder.Default
    private Boolean showOnlineStatus = true; // 是否显示在线状态
    @Builder.Default
    private Boolean allowFollowing = true; // 是否允许被关注
}
```

#### 约会记录实体（需求4：单身用户约会管理表）
```java
@Entity
@Table(name = "dating_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class DatingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 所属用户（必须是单身状态）
    
    // 需求4：约会管理表字段 - 姓名、认识日期、AI兼容性评分、最后联系和状态
    @Column(nullable = false)
    private String name; // 约会对象姓名
    
    @Column(nullable = false)
    private LocalDate metDate; // 认识日期
    
    @Column(precision = 5, scale = 2)
    private BigDecimal compatibilityScore; // 需求4：AI兼容性评分 (0-100%)，基于输入信息生成
    
    private LocalDateTime lastContact; // 最后联系时间
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DatingStatus status = DatingStatus.ACTIVE; // 状态字段
    
    // 扩展信息
    private String notes; // 备注信息
    private String phoneNumber; // 联系方式
    private String socialMedia; // 社交媒体信息
    private String interests; // 兴趣爱好（用于AI兼容性评分）
    private String personality; // 性格特点（用于AI兼容性评分）
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // 关联约会追踪记录
    @OneToMany(mappedBy = "datingRecord", cascade = CascadeType.ALL)
    private List<DateTrackingRecord> trackingRecords; // 需求4：约会记录追踪器
}

// 约会状态枚举
public enum DatingStatus {
    ACTIVE("进行中"),
    PAUSED("暂停"),
    ENDED("结束");
    
    @Getter
    private final String description;
    
    DatingStatus(String description) {
        this.description = description;
    }
}
```

#### 约会追踪记录实体（需求4：约会记录追踪器）
```java
@Entity
@Table(name = "date_tracking_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateTrackingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dating_record_id", nullable = false)
    private DatingRecord datingRecord; // 关联的约会记录
    
    // 需求4：约会记录追踪器字段 - 地点、活动、对话亮点和约会后评估
    @Column(nullable = false)
    private LocalDateTime dateTime; // 约会时间
    
    @Column(nullable = false)
    private String location; // 需求4：约会地点
    
    @Column(nullable = false)
    private String activity; // 需求4：约会活动
    
    @Column(columnDefinition = "TEXT")
    private String conversationHighlights; // 需求4：对话亮点
    
    @Column(columnDefinition = "TEXT")
    private String postDateEvaluation; // 需求4：约会后评估
    
    @Column(precision = 3, scale = 1)
    private BigDecimal personalRating; // 个人评分 (1-10)
    
    // 需求4：AI应该在24小时内提供表现分析和改进建议
    @Column(columnDefinition = "TEXT")
    private String aiAnalysis; // AI表现分析结果
    
    @Column(columnDefinition = "TEXT")
    private String aiSuggestions; // AI改进建议
    
    @Builder.Default
    private Boolean aiAnalysisCompleted = false; // AI分析是否完成
    
    private LocalDateTime aiAnalysisGeneratedAt; // AI分析生成时间
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

#### 个人魅力提升清单实体（需求4：单身用户模板）
```java
@Entity
@Table(name = "charm_enhancement_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharmEnhancementItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 所属用户（必须是单身状态）
    
    // 需求4：四个类别 - 外表、沟通、兴趣和个人成长
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CharmCategory category; // 类别
    
    @Column(nullable = false)
    private String title; // 改进项目标题
    
    @Column(columnDefinition = "TEXT")
    private String description; // 详细描述
    
    @Column(columnDefinition = "TEXT")
    private String aiSuggestion; // 需求4：AI生成的改进建议
    
    @Builder.Default
    private Boolean isCompleted = false; // 需求4：跟踪完成进度
    
    private LocalDateTime completedAt; // 完成时间
    
    @Column(precision = 3, scale = 1)
    private BigDecimal priority; // 优先级评分
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

// 需求4：魅力提升四个类别
public enum CharmCategory {
    APPEARANCE("外表"),
    COMMUNICATION("沟通"),
    INTERESTS("兴趣"),
    PERSONAL_GROWTH("个人成长");
    
    @Getter
    private final String description;
    
    CharmCategory(String description) {
        this.description = description;
    }
}
```

#### 关系管理仪表板实体（需求4：情侣用户模板）
```java
@Entity
@Table(name = "relationship_dashboards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelationshipDashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 所属用户（必须是恋爱中状态）
    
    // 需求4：10分制的关系健康评分
    @Column(precision = 3, scale = 1)
    private BigDecimal healthScore; // 关系健康评分 (1-10)
    
    // 需求4：沟通质量评估
    @Column(precision = 3, scale = 1)
    private BigDecimal communicationQuality; // 沟通质量评分 (1-10)
    
    @Column(precision = 3, scale = 1)
    private BigDecimal intimacyLevel; // 亲密度评分
    
    @Column(precision = 3, scale = 1)
    private BigDecimal conflictResolution; // 冲突解决能力评分
    
    @Column(precision = 3, scale = 1)
    private BigDecimal sharedGoals; // 共同目标一致性评分
    
    // 需求4：数据显示下降趋势时生成警报和改进建议
    @Builder.Default
    private Boolean hasAlert = false; // 是否有警报
    
    @Column(columnDefinition = "TEXT")
    private String alertMessage; // 警报信息
    
    @Column(columnDefinition = "TEXT")
    private String aiRecommendations; // AI改进建议
    
    private LocalDateTime lastUpdated; // 最后更新时间
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

#### 重要日期提醒实体（需求4：情侣用户模板）
```java
@Entity
@Table(name = "important_dates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportantDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 所属用户（必须是恋爱中状态）
    
    @Column(nullable = false)
    private String title; // 日期标题
    
    @Column(nullable = false)
    private LocalDate date; // 重要日期
    
    @Enumerated(EnumType.STRING)
    private DateType type; // 日期类型
    
    @Column(columnDefinition = "TEXT")
    private String description; // 描述
    
    // 需求4：AI生成的庆祝建议
    @Column(columnDefinition = "TEXT")
    private String aiCelebrationSuggestion; // AI庆祝建议
    
    // 需求4：提前提醒功能
    @Builder.Default
    private Integer reminderDaysBefore = 7; // 提前提醒天数
    
    @Builder.Default
    private Boolean reminderSent = false; // 是否已发送提醒
    
    @Builder.Default
    private Boolean isRecurring = false; // 是否循环
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

public enum DateType {
    ANNIVERSARY("纪念日"),
    BIRTHDAY("生日"),
    SPECIAL_EVENT("特殊事件"),
    MILESTONE("里程碑");
    
    @Getter
    private final String description;
    
    DateType(String description) {
        this.description = description;
    }
}
```

#### 冲突解决记录实体（需求4：情侣用户模板）
```java
@Entity
@Table(name = "conflict_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConflictRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 所属用户（必须是恋爱中状态）
    
    // 需求4：冲突解决记录字段 - 详情、情绪、解决过程
    @Column(nullable = false)
    private String title; // 冲突标题
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String details; // 需求4：冲突详情
    
    @Column(columnDefinition = "TEXT")
    private String emotions; // 需求4：情绪记录
    
    @Column(columnDefinition = "TEXT")
    private String resolutionProcess; // 需求4：解决过程
    
    @Enumerated(EnumType.STRING)
    private ConflictStatus status; // 冲突状态
    
    @Column(precision = 3, scale = 1)
    private BigDecimal severityLevel; // 严重程度 (1-10)
    
    // 需求4：AI模式分析和预防建议
    @Column(columnDefinition = "TEXT")
    private String aiPatternAnalysis; // AI模式分析
    
    @Column(columnDefinition = "TEXT")
    private String aiPreventionSuggestions; // AI预防建议
    
    private LocalDateTime resolvedAt; // 解决时间
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

public enum ConflictStatus {
    ONGOING("进行中"),
    RESOLVED("已解决"),
    RECURRING("反复出现");
    
    @Getter
    private final String description;
    
    ConflictStatus(String description) {
        this.description = description;
    }
}
```

#### 恋爱成长轨迹实体（需求4：情侣用户模板）
```java
@Entity
@Table(name = "growth_trajectory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrowthTrajectory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 所属用户（必须是恋爱中状态）
    
    @Column(nullable = false)
    private LocalDate recordDate; // 记录日期
    
    // 需求4：关系发展进展记录
    @Column(precision = 3, scale = 1)
    private BigDecimal relationshipScore; // 关系评分
    
    @Column(precision = 3, scale = 1)
    private BigDecimal happinessLevel; // 幸福度
    
    @Column(precision = 3, scale = 1)
    private BigDecimal communicationScore; // 沟通评分
    
    @Column(precision = 3, scale = 1)
    private BigDecimal intimacyScore; // 亲密度评分
    
    @Column(columnDefinition = "TEXT")
    private String milestones; // 里程碑事件
    
    @Column(columnDefinition = "TEXT")
    private String challenges; // 面临的挑战
    
    // 需求4：AI预测未来发展趋势
    @Column(columnDefinition = "TEXT")
    private String aiTrendPrediction; // AI趋势预测
    
    @Column(columnDefinition = "TEXT")
    private String aiGrowthSuggestions; // AI成长建议
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

#### 社区帖子实体（需求5：社区功能和用户互动）
```java
@Entity
@Table(name = "community_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class CommunityPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author; // 作者
    
    @Column(nullable = false, length = 200)
    private String title; // 标题
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 内容
    
    // 需求5：四个分类标签 - "推荐"、"成功故事"、"问题求助"、"每周话题"
    @Enumerated(EnumType.STRING)
    private PostType type; // 帖子类型
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PostStatus status = PostStatus.PUBLISHED; // 状态
    
    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>(); // 标签
    
    // 需求5：点赞、评论和分享功能
    @Builder.Default
    private Integer likeCount = 0; // 需求5：点赞数，实时更新
    @Builder.Default
    private Integer commentCount = 0; // 需求5：评论数
    @Builder.Default
    private Integer shareCount = 0; // 需求5：分享数
    @Builder.Default
    private Integer viewCount = 0; // 浏览数
    
    // 需求5：AI审查质量并在发布前适当分类
    @Column(precision = 3, scale = 2)
    private BigDecimal qualityScore; // AI内容质量评分
    
    @Builder.Default
    private Boolean aiReviewed = false; // AI是否已审核
    
    @Column(columnDefinition = "TEXT")
    private String aiCategory; // AI自动分类结果
    
    // 需求5：成功故事的匿名发布功能
    @Builder.Default
    private Boolean isAnonymous = false; // 是否匿名发布
    
    @Builder.Default
    private Boolean allowComments = true; // 允许评论
    
    // 推荐系统相关字段
    @Column(precision = 5, scale = 4)
    private BigDecimal relevanceScore; // 相关性评分（用于推荐算法）
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // 关联关系
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments; // 需求5：评论系统
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLike> likes; // 需求5：点赞记录
}

// 帖子类型枚举
public enum PostType {
    SUCCESS_STORY("成功故事"),
    HELP_REQUEST("问题求助"),
    WEEKLY_TOPIC("每周话题"),
    GENERAL("一般讨论");
    
    @Getter
    private final String description;
    
    PostType(String description) {
        this.description = description;
    }
}

// 帖子状态枚举
public enum PostStatus {
    DRAFT("草稿"),
    PUBLISHED("已发布"),
    HIDDEN("隐藏"),
    DELETED("已删除");
    
    @Getter
    private final String description;
    
    PostStatus(String description) {
        this.description = description;
    }
}
```

#### 聊天消息实体
```java
@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // 发送者
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver; // 接收者（AI消息时为null）
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 消息内容
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MessageType type = MessageType.TEXT; // 消息类型：TEXT, IMAGE, AI_RESPONSE
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MessageStatus status = MessageStatus.SENT; // 消息状态：SENT, DELIVERED, READ
    
    @Builder.Default
    private Boolean isAIMessage = false; // 是否为AI消息
    
    private String imageUrl; // 图片URL（图片消息时使用）
    
    @Builder.Default
    private Boolean isDeleted = false; // 是否已删除
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    private LocalDateTime readAt; // 已读时间
}

// 消息类型枚举
public enum MessageType {
    TEXT("文本"),
    IMAGE("图片"),
    AI_RESPONSE("AI回复");
    
    @Getter
    private final String description;
    
    MessageType(String description) {
        this.description = description;
    }
}

// 消息状态枚举
public enum MessageStatus {
    SENT("已发送"),
    DELIVERED("已送达"),
    READ("已读");
    
    @Getter
    private final String description;
    
    MessageStatus(String description) {
        this.description = description;
    }

## 推荐算法设计

### 智能推荐系统架构

社区功能的核心是智能推荐系统，参考小红书的推荐逻辑，为用户提供个性化的内容推荐。

#### 推荐算法策略

##### 1. 协同过滤算法 (Collaborative Filtering)
- **用户基础协同过滤**: 基于相似用户的行为推荐内容
- **物品基础协同过滤**: 基于用户历史互动的相似内容推荐
- **矩阵分解**: 使用SVD等技术处理稀疏数据

##### 2. 内容基础推荐 (Content-Based Filtering)
- **文本相似度**: 基于帖子标题、内容、标签的TF-IDF相似度
- **用户画像匹配**: 根据用户恋爱状态、年龄、兴趣等属性推荐
- **主题建模**: 使用LDA主题模型识别用户兴趣主题

##### 3. 深度学习推荐
- **嵌入向量**: 将用户、帖子、标签转换为向量表示
- **神经网络**: 使用深度神经网络学习复杂的用户-内容交互模式
- **序列建模**: 考虑用户行为的时间序列特征

#### 推荐特征工程

##### 用户特征
```java
public class UserFeatures {
    // 基础特征
    private Integer age; // 年龄
    private String gender; // 性别
    private String relationshipStatus; // 恋爱状态
    private String location; // 地理位置
    
    // 行为特征
    private Double avgSessionDuration; // 平均会话时长
    private Integer dailyActiveHours; // 日活跃小时数
    private Map<String, Integer> categoryPreferences; // 分类偏好
    private Map<String, Double> tagAffinities; // 标签亲和度
    
    // 互动特征
    private Double likeRate; // 点赞率
    private Double commentRate; // 评论率
    private Double shareRate; // 分享率
    private List<Long> followingUsers; // 关注的用户
    
    // 时间特征
    private List<Integer> activeHours; // 活跃时间段
    private Map<String, Integer> weeklyActivity; // 周活跃模式
}
```

##### 内容特征
```java
public class ContentFeatures {
    // 基础特征
    private String title; // 标题
    private String content; // 内容
    private List<String> tags; // 标签
    private String category; // 分类
    private PostType type; // 帖子类型
    
    // 质量特征
    private Double qualityScore; // 内容质量评分
    private Integer wordCount; // 字数
    private Boolean hasImages; // 是否包含图片
    private Double readabilityScore; // 可读性评分
    
    // 互动特征
    private Integer likeCount; // 点赞数
    private Integer commentCount; // 评论数
    private Integer shareCount; // 分享数
    private Integer viewCount; // 浏览数
    private Double engagementRate; // 互动率
    
    // 时间特征
    private LocalDateTime publishedAt; // 发布时间
    private Integer hoursSincePublished; // 发布后小时数
    private Boolean isTrending; // 是否热门
}
```

#### 推荐算法实现

##### RecommendationService核心逻辑
```java
@Service
@Slf4j // Lombok注解：自动生成日志对象
@RequiredArgsConstructor // Lombok注解：为final字段生成构造函数
public class RecommendationService {
    
    // 使用final字段配合@RequiredArgsConstructor实现依赖注入
    private final UserInteractionRepository interactionRepository;
    private final CommunityPostRepository postRepository;
    private final UserRepository userRepository;
    private final AIService aiService;
    
    /**
     * 获取用户推荐内容
     */
    public List<CommunityPost> getRecommendations(Long userId, int limit) {
        User user = userRepository.findById(userId).orElseThrow();
        
        // 1. 获取用户特征
        UserFeatures userFeatures = buildUserFeatures(user);
        
        // 2. 候选内容生成
        List<CommunityPost> candidates = generateCandidates(user);
        
        // 3. 多策略推荐评分
        Map<Long, Double> scores = new HashMap<>();
        
        // 协同过滤评分
        Map<Long, Double> cfScores = collaborativeFiltering(user, candidates);
        
        // 内容基础评分
        Map<Long, Double> cbScores = contentBasedFiltering(userFeatures, candidates);
        
        // AI增强评分
        Map<Long, Double> aiScores = aiEnhancedScoring(user, candidates);
        
        // 4. 融合多种评分
        for (CommunityPost post : candidates) {
            double finalScore = 0.4 * cfScores.getOrDefault(post.getId(), 0.0) +
                              0.3 * cbScores.getOrDefault(post.getId(), 0.0) +
                              0.3 * aiScores.getOrDefault(post.getId(), 0.0);
            scores.put(post.getId(), finalScore);
        }
        
        // 5. 排序和多样性处理
        List<CommunityPost> recommendations = diversifyRecommendations(candidates, scores);
        
        // 6. 记录推荐结果
        recordRecommendations(userId, recommendations, scores);
        
        return recommendations.stream().limit(limit).collect(Collectors.toList());
    }
    
    /**
     * 协同过滤推荐
     */
    private Map<Long, Double> collaborativeFiltering(User user, List<CommunityPost> candidates) {
        log.debug("执行协同过滤推荐: userId={}", user.getId());
        // 实现协同过滤算法逻辑
        return new HashMap<>();
    }
    
    /**
     * 内容基础推荐
     */
    private Map<Long, Double> contentBasedFiltering(UserFeatures userFeatures, List<CommunityPost> candidates) {
        log.debug("执行内容基础推荐");
        // 实现内容基础推荐算法逻辑
        return new HashMap<>();
    }
    
    /**
     * AI增强评分
     */
    private Map<Long, Double> aiEnhancedScoring(User user, List<CommunityPost> candidates) {
        log.debug("执行AI增强评分: userId={}", user.getId());
        // 调用AI服务进行评分
        return new HashMap<>();
    }
}

// 用户服务示例
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    
    /**
     * 获取用户资料
     */
    @Transactional(readOnly = true)
    public UserProfile getUserProfile(Long userId) {
        log.debug("获取用户资料: userId={}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        return UserProfile.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .age(user.getAge())
                .gender(user.getGender().name())
                .relationshipStatus(user.getRelationshipStatus().name())
                .avatarUrl(user.getAvatarUrl())
                .location(user.getLocation())
                .bio(user.getBio())
                .createdAt(user.getCreatedAt())
                .preferences(user.getPreferences())
                .privacySettings(user.getPrivacySettings())
                .build();
    }
    
    /**
     * 更新用户资料
     */
    public UserProfile updateProfile(Long userId, UpdateProfileRequest request) {
        log.info("更新用户资料: userId={}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        // 使用Lombok的builder模式更新用户信息
        User updatedUser = user.toBuilder()
                .username(request.getUsername() != null ? request.getUsername() : user.getUsername())
                .age(request.getAge() != null ? request.getAge() : user.getAge())
                .avatarUrl(request.getAvatarUrl() != null ? request.getAvatarUrl() : user.getAvatarUrl())
                .location(request.getLocation() != null ? request.getLocation() : user.getLocation())
                .bio(request.getBio() != null ? request.getBio() : user.getBio())
                .preferences(request.getPreferences() != null ? request.getPreferences() : user.getPreferences())
                .privacySettings(request.getPrivacySettings() != null ? request.getPrivacySettings() : user.getPrivacySettings())
                .build();
        
        User savedUser = userRepository.save(updatedUser);
        log.info("用户资料更新成功: userId={}", userId);
        
        return getUserProfile(savedUser.getId());
    }
    
    /**
     * 更新恋爱状态
     */
    public void updateRelationshipStatus(Long userId, RelationshipStatusRequest request) {
        log.info("更新恋爱状态: userId={}, newStatus={}", userId, request.getStatus());
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        
        RelationshipStatus newStatus = RelationshipStatus.valueOf(request.getStatus());
        
        User updatedUser = user.toBuilder()
                .relationshipStatus(newStatus)
                .build();
        
        userRepository.save(updatedUser);
        log.info("恋爱状态更新成功: userId={}", userId);
    }
}

// 认证服务示例
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request) {
        log.info("用户登录: email={}", request.getEmail());
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("邮箱或密码错误"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("邮箱或密码错误");
        }
        
        if (!user.getIsActive()) {
            throw new AccountStatusException("账户已被禁用");
        }
        
        // 生成JWT令牌
        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);
        
        // 更新登录统计
        User updatedUser = user.toBuilder()
                .loginCount(user.getLoginCount() + 1)
                .lastLoginAt(LocalDateTime.now())
                .build();
        userRepository.save(updatedUser);
        
        // 构建响应
        UserProfile userProfile = UserProfile.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .relationshipStatus(user.getRelationshipStatus().name())
                .avatarUrl(user.getAvatarUrl())
                .build();
        
        log.info("用户登录成功: userId={}", user.getId());
        
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(tokenProvider.getAccessTokenValidityInSeconds())
                .userProfile(userProfile)
                .build();
    }
    
    /**
     * 注册第一步
     */
    public RegisterStep1Response registerStep1(RegisterStep1Request request) {
        log.info("用户注册第一步: email={}", request.getEmail());
        
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("该邮箱已被注册");
        }
        
        // 验证年龄范围
        if (request.getAge() < 18 || request.getAge() > 100) {
            throw new IllegalArgumentException("年龄必须在18-100岁之间");
        }
        
        // 临时存储注册信息到Redis（有效期10分钟）
        String tempKey = "register_step1:" + request.getEmail();
        RegisterStep1Data tempData = RegisterStep1Data.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .gender(Gender.valueOf(request.getGender()))
                .build();
        
        redisTemplate.opsForValue().set(tempKey, tempData, Duration.ofMinutes(10));
        
        log.info("注册第一步完成: email={}", request.getEmail());
        
        return RegisterStep1Response.builder()
                .success(true)
                .message("第一步注册信息已保存，请继续完成第二步")
                .tempToken(tempKey)
                .build();
    }
}t<User> similarUsers = findSimilarUsers(user);
        
        Map<Long, Double> scores = new HashMap<>();
        
        for (CommunityPost post : candidates) {
            double score = 0.0;
            int count = 0;
            
            for (User similarUser : similarUsers) {
                Double userScore = getUserPostScore(similarUser.getId(), post.getId());
                if (userScore != null) {
                    score += userScore;
                    count++;
                }
            }
            
            if (count > 0) {
                scores.put(post.getId(), score / count);
            }
        }
        
        return scores;
    }
    
    /**
     * 内容基础推荐
     */
    private Map<Long, Double> contentBasedFiltering(UserFeatures userFeatures, List<CommunityPost> candidates) {
        Map<Long, Double> scores = new HashMap<>();
        
        for (CommunityPost post : candidates) {
            ContentFeatures contentFeatures = buildContentFeatures(post);
            double similarity = calculateContentSimilarity(userFeatures, contentFeatures);
            scores.put(post.getId(), similarity);
        }
        
        return scores;
    }
    
    /**
     * AI增强评分
     */
    private Map<Long, Double> aiEnhancedScoring(User user, List<CommunityPost> candidates) {
        Map<Long, Double> scores = new HashMap<>();
        
        // 使用AI分析用户偏好和内容匹配度
        String userProfile = buildUserProfileForAI(user);
        
        for (CommunityPost post : candidates) {
            String contentSummary = buildContentSummaryForAI(post);
            double aiScore = aiService.calculateContentRelevance(userProfile, contentSummary);
            scores.put(post.getId(), aiScore);
        }
        
        return scores;
    }
    
    /**
     * 推荐多样性处理
     */
    private List<CommunityPost> diversifyRecommendations(List<CommunityPost> candidates, Map<Long, Double> scores) {
        // 按评分排序
        candidates.sort((a, b) -> Double.compare(
            scores.getOrDefault(b.getId(), 0.0),
            scores.getOrDefault(a.getId(), 0.0)
        ));
        
        List<CommunityPost> diversified = new ArrayList<>();
        Set<String> usedCategories = new HashSet<>();
        Set<PostType> usedTypes = new HashSet<>();
        
        // 确保推荐结果的多样性
        for (CommunityPost post : candidates) {
            boolean shouldAdd = true;
            
            // 限制同一分类的数量
            if (usedCategories.contains(post.getCategory()) && usedCategories.size() < 3) {
                shouldAdd = false;
            }
            
            // 确保不同类型的平衡
            if (usedTypes.contains(post.getType()) && usedTypes.size() < 2) {
                shouldAdd = false;
            }
            
            if (shouldAdd) {
                diversified.add(post);
                usedCategories.add(post.getCategory());
                usedTypes.add(post.getType());
            }
            
            if (diversified.size() >= 20) break; // 限制推荐数量
        }
        
        return diversified;
    }
}
    
    @CreationTimestamp
    private LocalDateTime createdAt; // 创建时间
    
    @UpdateTimestamp
    private LocalDateTime updatedAt; // 更新时间
    
    // 关联关系
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DatingRecord> datingRecords; // 约会记录
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<CommunityPost> posts; // 发布的帖子
    
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<UserFollow> following; // 关注的用户
    
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private List<UserFollow> followers; // 粉丝
}

// 用户偏好设置嵌入类
@Embeddable
public class UserPreferences {
    private Boolean receiveEmailNotifications = true; // 接收邮件通知
    private Boolean receivePushNotifications = true; // 接收推送通知
    private Boolean showOnlineStatus = true; // 显示在线状态
    private String language = "zh-CN"; // 语言偏好
    private String timezone = "Asia/Shanghai"; // 时区设置
    private Boolean enableAICoach = true; // 启用AI教练
    private String aiCoachPersonality = "FRIENDLY"; // AI教练个性：FRIENDLY, PROFESSIONAL, CASUAL
}

// 隐私设置嵌入类
@Embeddable
public class PrivacySettings {
    private Boolean profileVisible = true; // 资料是否可见
    private Boolean allowDirectMessages = true; // 允许私信
    private Boolean showAge = true; // 显示年龄
    private Boolean showLocation = true; // 显示位置
    private Boolean allowFollowing = true; // 允许被关注
}
```

#### 约会记录实体（单身用户）
```java
@Entity
@Table(name = "dating_records")
public class DatingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 所属用户
    
    // 基本信息
    private String personName; // 对方姓名
    private LocalDate meetDate; // 认识日期
    private String meetLocation; // 认识地点
    private String meetMethod; // 认识方式：APP, FRIEND_INTRO, WORK, SOCIAL_EVENT, OTHER
    
    // AI评分和分析
    private Integer aiCompatibilityScore; // AI兼容性评分 0-100
    private String compatibilityAnalysis; // 兼容性分析详情
    private LocalDateTime lastScoreUpdate; // 最后评分更新时间
    
    // 联系信息
    private LocalDateTime lastContact; // 最后联系时间
    private String contactMethod; // 联系方式：WECHAT, PHONE, APP, EMAIL
    private Integer contactFrequency; // 联系频率（天）
    
    // 状态管理
    @Enumerated(EnumType.STRING)
    private DatingStatus status; // 状态：INTERESTED, DATING, FRIEND, BLOCKED, ENDED
    
    private String statusNote; // 状态备注
    private LocalDateTime statusUpdatedAt; // 状态更新时间
    
    // 个人信息
    private Integer age; // 年龄
    private String occupation; // 职业
    private String education; // 教育背景
    private String interests; // 兴趣爱好
    private String personality; // 性格特点
    
    // 评价和备注
    private Integer attractivenessRating; // 外貌评分 1-10
    private Integer personalityRating; // 性格评分 1-10
    private Integer communicationRating; // 沟通评分 1-10
    private String personalNotes; // 个人备注
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // 关联的约会追踪记录
    @OneToMany(mappedBy = "datingRecord", cascade = CascadeType.ALL)
    private List<DateTrackingRecord> dateRecords;
}

// 约会状态枚举
public enum DatingStatus {
    INTERESTED("有兴趣"),
    DATING("约会中"),
    FRIEND("朋友"),
    BLOCKED("已拉黑"),
    ENDED("已结束");
    
    private final String description;
    
    DatingStatus(String description) {
        this.description = description;
    }
}
```

#### 约会追踪记录实体
```java
@Entity
@Table(name = "date_tracking_records")
public class DateTrackingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dating_record_id")
    private DatingRecord datingRecord; // 关联的约会记录
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 用户
    
    // 约会基本信息
    private LocalDateTime dateTime; // 约会时间
    private String location; // 约会地点
    private String venue; // 具体场所
    private BigDecimal cost; // 花费金额
    private Integer duration; // 约会时长（分钟）
    
    // 活动详情
    @ElementCollection
    @CollectionTable(name = "date_activities")
    private List<String> activities; // 活动列表
    
    private String mainActivity; // 主要活动
    private String atmosphere; // 氛围描述
    
    // 对话和互动
    @Column(columnDefinition = "TEXT")
    private String conversationHighlights; // 对话亮点
    
    @Column(columnDefinition = "TEXT")
    private String commonTopics; // 共同话题
    
    private Boolean hadPhysicalContact; // 是否有身体接触
    private String physicalContactLevel; // 身体接触程度
    
    // 约会后评估
    private Integer overallRating; // 整体评分 1-10
    private Integer chemistryRating; // 化学反应评分 1-10
    private Integer conversationRating; // 对话质量评分 1-10
    private Integer attractionRating; // 吸引力评分 1-10
    
    @Column(columnDefinition = "TEXT")
    private String positiveAspects; // 积极方面
    
    @Column(columnDefinition = "TEXT")
    private String negativeAspects; // 消极方面
    
    @Column(columnDefinition = "TEXT")
    private String personalReflection; // 个人反思
    
    // AI分析结果
    @Column(columnDefinition = "TEXT")
    private String aiAnalysis; // AI分析结果
    
    @Column(columnDefinition = "TEXT")
    private String aiSuggestions; // AI改进建议
    
    private LocalDateTime aiAnalysisGeneratedAt; // AI分析生成时间
    
    // 后续计划
    private Boolean planNextDate; // 是否计划下次约会
    private String nextDateIdeas; // 下次约会想法
    private LocalDateTime suggestedNextDate; // 建议下次约会时间
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

#### 关系管理仪表板实体（情侣用户）
```java
@Entity
@Table(name = "relationship_dashboards")
public class RelationshipDashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 用户
    
    // 关系基本信息
    private String partnerName; // 伴侣姓名
    private LocalDate relationshipStartDate; // 恋爱开始日期
    private Integer relationshipDuration; // 恋爱天数（自动计算）
    
    // 健康评分系统
    private Integer overallHealthScore; // 总体健康评分 1-10
    private Integer communicationScore; // 沟通质量评分 1-10
    private Integer intimacyScore; // 亲密度评分 1-10
    private Integer trustScore; // 信任度评分 1-10
    private Integer supportScore; // 支持度评分 1-10
    private Integer conflictResolutionScore; // 冲突解决能力评分 1-10
    
    // 评分历史和趋势
    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL)
    private List<HealthScoreHistory> scoreHistory; // 评分历史
    
    // 关系状态
    @Enumerated(EnumType.STRING)
    private RelationshipPhase currentPhase; // 当前阶段：HONEYMOON, ADJUSTMENT, STABLE, CHALLENGING, GROWTH
    
    private String phaseDescription; // 阶段描述
    private LocalDateTime phaseStartDate; // 阶段开始时间
    
    // AI分析和建议
    @Column(columnDefinition = "TEXT")
    private String aiAnalysis; // AI关系分析
    
    @Column(columnDefinition = "TEXT")
    private String aiRecommendations; // AI改进建议
    
    private LocalDateTime lastAiAnalysis; // 最后AI分析时间
    
    // 警报和提醒
    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL)
    private List<RelationshipAlert> alerts; // 关系警报
    
    // 目标和里程碑
    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL)
    private List<RelationshipGoal> goals; // 关系目标
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // 关联数据
    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL)
    private List<ImportantDate> importantDates; // 重要日期
    
    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL)
    private List<ConflictRecord> conflictRecords; // 冲突记录
}

// 关系阶段枚举
public enum RelationshipPhase {
    HONEYMOON("蜜月期"),
    ADJUSTMENT("磨合期"),
    STABLE("稳定期"),
    CHALLENGING("挑战期"),
    GROWTH("成长期");
    
    private final String description;
    
    RelationshipPhase(String description) {
        this.description = description;
    }
}
```

#### Dating Record Entity (Singles)
```java
@Entity
@Table(name = "dating_records")
public class DatingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private String personName;
    private LocalDate meetDate;
    private Integer aiCompatibilityScore;
    private LocalDateTime lastContact;
    private String status;
    private String location;
    private String activities;
    private String conversationHighlights;
    private String postDateEvaluation;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

#### 社区帖子实体
```java
@Entity
@Table(name = "community_posts")
public class CommunityPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author; // 作者
    
    // 帖子基本信息
    @Column(nullable = false, length = 200)
    private String title; // 标题
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 内容
    
    @Enumerated(EnumType.STRING)
    private PostType type; // 帖子类型：SUCCESS_STORY, HELP_REQUEST, WEEKLY_TOPIC, GENERAL
    
    @Enumerated(EnumType.STRING)
    private PostStatus status; // 状态：DRAFT, PUBLISHED, HIDDEN, DELETED
    
    // 分类和标签
    private String category; // 分类
    
    @ElementCollection
    @CollectionTable(name = "post_tags")
    private List<String> tags; // 标签列表
    
    // 互动统计
    private Integer likeCount = 0; // 点赞数
    private Integer commentCount = 0; // 评论数
    private Integer shareCount = 0; // 分享数
    private Integer viewCount = 0; // 浏览数
    
    // AI审核和推荐
    private Boolean aiApproved = false; // AI审核通过
    private String aiModerationNote; // AI审核备注
    private Double recommendationScore = 0.0; // 推荐评分
    
    // 时间戳
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private LocalDateTime publishedAt; // 发布时间
    
    // 关联关系
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments; // 评论列表
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLike> likes; // 点赞记录
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostRecommendation> recommendations; // 推荐记录
}

// 帖子类型枚举
public enum PostType {
    SUCCESS_STORY("成功故事"),
    HELP_REQUEST("问题求助"),
    WEEKLY_TOPIC("每周话题"),
    GENERAL("一般讨论");
    
    private final String description;
    
    PostType(String description) {
        this.description = description;
    }
}

// 帖子状态枚举
public enum PostStatus {
    DRAFT("草稿"),
    PUBLISHED("已发布"),
    HIDDEN("已隐藏"),
    DELETED("已删除");
    
    private final String description;
    
    PostStatus(String description) {
        this.description = description;
    }
}
```

#### 推荐记录实体
```java
@Entity
@Table(name = "post_recommendations")
public class PostRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 推荐给的用户
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private CommunityPost post; // 推荐的帖子
    
    // 推荐算法相关
    private Double score; // 推荐评分
    private String algorithm; // 使用的算法：COLLABORATIVE, CONTENT_BASED, HYBRID
    private String reason; // 推荐原因
    
    // 用户行为
    private Boolean viewed = false; // 是否已查看
    private Boolean clicked = false; // 是否已点击
    private Boolean liked = false; // 是否已点赞
    private Boolean shared = false; // 是否已分享
    
    private LocalDateTime viewedAt; // 查看时间
    private LocalDateTime clickedAt; // 点击时间
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

#### 用户互动记录实体
```java
@Entity
@Table(name = "user_interactions")
public class UserInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 用户
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private CommunityPost post; // 相关帖子
    
    // 互动类型和详情
    @Enumerated(EnumType.STRING)
    private InteractionType type; // 互动类型：VIEW, LIKE, COMMENT, SHARE, FOLLOW
    
    private String details; // 互动详情（如评论内容）
    private Integer duration; // 停留时长（秒）
    
    // 上下文信息
    private String source; // 来源：RECOMMENDATION, SEARCH, PROFILE, FOLLOWING
    private String tabType; // Tab类型：RECOMMENDATION, SUCCESS_STORY, HELP_FORUM, WEEKLY_TOPIC
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}

// 互动类型枚举
public enum InteractionType {
    VIEW("浏览"),
    LIKE("点赞"),
    COMMENT("评论"),
    SHARE("分享"),
    FOLLOW("关注");
    
    private final String description;
    
    InteractionType(String description) {
        this.description = description;
    }
}
```

#### Relationship Dashboard Entity (Couples)
```java
@Entity
@Table(name = "relationship_dashboards")
public class RelationshipDashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private Integer healthScore;
    private Integer communicationQuality;
    private String aiAnalysis;
    private LocalDateTime lastUpdated;
    
    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL)
    private List<ImportantDate> importantDates;
    
    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL)
    private List<ConflictRecord> conflictRecords;
}
```

#### Community Post Entity
```java
@Entity
@Table(name = "community_posts")
public class CommunityPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    
    @Enumerated(EnumType.STRING)
    private PostType type; // SUCCESS_STORY, HELP_REQUEST, WEEKLY_TOPIC
    
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private Integer likesCount = 0;
    private Integer commentsCount = 0;
    private Boolean isAnonymous = false;
    private Boolean aiReviewed = false;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

#### Chat Message Entity
```java
@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    
    @Enumerated(EnumType.STRING)
    private MessageType type; // TEXT, IMAGE, AI_RESPONSE
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String imageUrl;
    
    @Enumerated(EnumType.STRING)
    private MessageStatus status; // SENT, DELIVERED, READ
    
    private Boolean isAiMessage = false;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}
```

### 数据库架构设计

#### MySQL表结构
```sql
-- 用户表
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL COMMENT '邮箱地址',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    username VARCHAR(100) NOT NULL COMMENT '用户名',
    age INT NOT NULL COMMENT '年龄',
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL COMMENT '性别',
    relationship_status ENUM('SINGLE', 'IN_RELATIONSHIP') NOT NULL COMMENT '恋爱状态',
    avatar_url VARCHAR(500) COMMENT '头像URL',
    location VARCHAR(255) COMMENT '位置信息',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_email (email),
    INDEX idx_relationship_status (relationship_status)
);

-- 约会记录表（单身用户）
CREATE TABLE dating_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    person_name VARCHAR(255) NOT NULL COMMENT '对方姓名',
    meet_date DATE COMMENT '认识日期',
    ai_compatibility_score INT CHECK (ai_compatibility_score >= 0 AND ai_compatibility_score <= 100) COMMENT 'AI兼容性评分',
    last_contact TIMESTAMP COMMENT '最后联系时间',
    status VARCHAR(50) COMMENT '状态',
    location VARCHAR(255) COMMENT '约会地点',
    activities TEXT COMMENT '活动内容',
    conversation_highlights TEXT COMMENT '对话亮点',
    post_date_evaluation TEXT COMMENT '约会后评估',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_meet_date (meet_date)
);

-- 社区帖子表
CREATE TABLE community_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    author_id BIGINT NOT NULL COMMENT '作者ID',
    type ENUM('SUCCESS_STORY', 'HELP_REQUEST', 'WEEKLY_TOPIC') NOT NULL COMMENT '帖子类型',
    title VARCHAR(255) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    likes_count INT DEFAULT 0 COMMENT '点赞数',
    comments_count INT DEFAULT 0 COMMENT '评论数',
    is_anonymous BOOLEAN DEFAULT FALSE COMMENT '是否匿名',
    ai_reviewed BOOLEAN DEFAULT FALSE COMMENT 'AI是否已审核',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_author_id (author_id),
    INDEX idx_type (type),
    INDEX idx_created_at (created_at)
);

-- 聊天消息表
CREATE TABLE chat_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT COMMENT '发送者ID',
    receiver_id BIGINT COMMENT '接收者ID',
    type ENUM('TEXT', 'IMAGE', 'AI_RESPONSE') NOT NULL COMMENT '消息类型',
    content TEXT COMMENT '消息内容',
    image_url VARCHAR(500) COMMENT '图片URL',
    status ENUM('SENT', 'DELIVERED', 'READ') DEFAULT 'SENT' COMMENT '消息状态',
    is_ai_message BOOLEAN DEFAULT FALSE COMMENT '是否为AI消息',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_sender_receiver (sender_id, receiver_id),
    INDEX idx_created_at (created_at)
);
```

### Redis Data Structures

#### Session Management
```
Key: "session:{userId}"
Value: {
    "token": "jwt_token",
    "refreshToken": "refresh_token",
    "lastActivity": "timestamp",
    "deviceInfo": "device_details"
}
TTL: 24 hours
```

#### Real-time Chat
```
Key: "chat:active:{userId}"
Value: {
    "status": "online|offline",
    "lastSeen": "timestamp",
    "currentChat": "receiverId"
}
TTL: 30 minutes

Key: "chat:unread:{userId}"
Value: Set of message IDs
TTL: 7 days
```

#### AI Cache
```
Key: "ai:response:{userId}:{queryHash}"
Value: {
    "response": "ai_generated_content",
    "timestamp": "creation_time",
    "context": "user_context"
}
TTL: 1 hour
```

## 错误处理

### 前端错误处理策略

#### 网络错误处理
```javascript
// 带重试逻辑的API服务
class ApiService {
    async request(endpoint, options = {}) {
        const maxRetries = 3; // 最大重试次数
        let attempt = 0;
        
        while (attempt < maxRetries) {
            try {
                const response = await fetch(endpoint, {
                    ...options,
                    timeout: 10000 // 10秒超时
                });
                
                if (!response.ok) {
                    throw new Error(`HTTP ${response.status}: ${response.statusText}`);
                }
                
                return await response.json();
            } catch (error) {
                attempt++;
                if (attempt >= maxRetries) {
                    throw new NetworkError('多次尝试后连接失败');
                }
                await this.delay(1000 * attempt); // 指数退避
            }
        }
    }
}
```

#### 用户友好的错误消息
```javascript
const ErrorMessages = {
    NETWORK_ERROR: '请检查网络连接后重试',
    AUTH_FAILED: '邮箱或密码错误，请重试',
    VALIDATION_ERROR: '请检查输入信息后重试',
    SERVER_ERROR: '服务器出现问题，请稍后重试',
    AI_UNAVAILABLE: 'AI教练暂时不可用，请几分钟后重试'
};
```

### Backend Error Handling

#### Global Exception Handler
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse("VALIDATION_ERROR", ex.getMessage()));
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse("AUTH_FAILED", "Invalid credentials"));
    }
    
    @ExceptionHandler(AIServiceException.class)
    public ResponseEntity<ErrorResponse> handleAIService(AIServiceException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ErrorResponse("AI_UNAVAILABLE", "AI service temporarily unavailable"));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        logger.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("SERVER_ERROR", "Internal server error"));
    }
}
```

#### Circuit Breaker for AI Service
```java
@Component
public class AIServiceClient {
    
    @CircuitBreaker(name = "ai-service", fallbackMethod = "fallbackResponse")
    @Retry(name = "ai-service")
    @TimeLimiter(name = "ai-service")
    public CompletableFuture<String> getAIResponse(String query, String context) {
        return CompletableFuture.supplyAsync(() -> {
            // Call external AI API
            return aiApiClient.generateResponse(query, context);
        });
    }
    
    public CompletableFuture<String> fallbackResponse(String query, String context, Exception ex) {
        return CompletableFuture.completedFuture(
            "I'm temporarily unavailable. Please try again in a few minutes."
        );
    }
}
```

## AI服务集成设计

### AI功能模块

#### 1. AI教练对话系统
```java
@Service
public class AICoachService {
    
    // AI教练对话处理
    public AICoachResponse processConversation(AICoachRequest request) {
        // 获取用户上下文信息
        UserContext context = buildUserContext(request.getUserId());
        
        // 构建AI提示词
        String prompt = buildPrompt(request.getMessage(), context);
        
        // 调用AI API
        String aiResponse = aiApiClient.generateResponse(prompt);
        
        // 后处理和个性化
        return personalizeResponse(aiResponse, context);
    }
    
    // 构建用户上下文
    private UserContext buildUserContext(Long userId) {
        User user = userService.findById(userId);
        List<DatingRecord> recentDates = templateService.getRecentDatingRecords(userId);
        RelationshipDashboard dashboard = templateService.getRelationshipDashboard(userId);
        
        return UserContext.builder()
            .relationshipStatus(user.getRelationshipStatus())
            .recentActivity(recentDates)
            .relationshipHealth(dashboard)
            .preferences(user.getPreferences())
            .build();
    }
}
```

#### 2. 兼容性评分算法
```java
@Service
public class CompatibilityScoreService {
    
    // 计算AI兼容性评分
    public CompatibilityScore calculateScore(Long datingRecordId) {
        DatingRecord record = datingRecordRepository.findById(datingRecordId);
        
        // 收集评分因子
        CompatibilityFactors factors = CompatibilityFactors.builder()
            .ageCompatibility(calculateAgeCompatibility(record))
            .interestAlignment(calculateInterestAlignment(record))
            .communicationQuality(record.getCommunicationRating())
            .personalityMatch(calculatePersonalityMatch(record))
            .lifestyleCompatibility(calculateLifestyleCompatibility(record))
            .build();
        
        // AI评分计算
        int aiScore = aiCompatibilityEngine.calculateScore(factors);
        
        // 生成详细分析
        String analysis = aiCompatibilityEngine.generateAnalysis(factors, aiScore);
        
        return CompatibilityScore.builder()
            .score(aiScore)
            .analysis(analysis)
            .factors(factors)
            .confidence(calculateConfidence(factors))
            .build();
    }
}
```

### 实时通信架构

#### WebSocket配置
```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(), "/ws/chat")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 用户上线处理
        Long userId = getUserIdFromSession(session);
        onlineUserService.addOnlineUser(userId, session);
        
        // 通知好友用户上线
        notifyFriendsUserOnline(userId);
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 处理实时消息
        ChatMessage chatMessage = parseMessage(message.getPayload());
        
        // 保存消息到数据库
        chatService.saveMessage(chatMessage);
        
        // 转发消息给接收者
        forwardMessageToReceiver(chatMessage);
        
        // 发送推送通知
        pushNotificationService.sendMessageNotification(chatMessage);
    }
}
```

## 性能优化策略

### 数据库优化

#### 1. 索引策略
```sql
-- 用户表索引
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_relationship_status ON users(relationship_status);
CREATE INDEX idx_users_created_at ON users(created_at);

-- 约会记录表索引
CREATE INDEX idx_dating_records_user_id ON dating_records(user_id);
CREATE INDEX idx_dating_records_status ON dating_records(status);
CREATE INDEX idx_dating_records_meet_date ON dating_records(meet_date);
CREATE INDEX idx_dating_records_last_contact ON dating_records(last_contact);

-- 社区帖子表索引
CREATE INDEX idx_community_posts_author_id ON community_posts(author_id);
CREATE INDEX idx_community_posts_type ON community_posts(type);
CREATE INDEX idx_community_posts_created_at ON community_posts(created_at);
CREATE INDEX idx_community_posts_likes_count ON community_posts(likes_count);

-- 聊天消息表索引
CREATE INDEX idx_chat_messages_sender_receiver ON chat_messages(sender_id, receiver_id);
CREATE INDEX idx_chat_messages_created_at ON chat_messages(created_at);
CREATE INDEX idx_chat_messages_status ON chat_messages(status);

-- 复合索引
CREATE INDEX idx_posts_type_created_at ON community_posts(type, created_at DESC);
CREATE INDEX idx_messages_conversation ON chat_messages(sender_id, receiver_id, created_at DESC);
```

#### 2. 分库分表策略
```java
// 聊天消息分表策略
@Component
public class ChatMessageShardingStrategy {
    
    // 按用户ID哈希分表
    public String getTableName(Long userId) {
        int shardIndex = (int) (userId % 16); // 16个分表
        return "chat_messages_" + String.format("%02d", shardIndex);
    }
    
    // 按时间分表（月表）
    public String getTableNameByTime(LocalDateTime dateTime) {
        String yearMonth = dateTime.format(DateTimeFormatter.ofPattern("yyyyMM"));
        return "chat_messages_" + yearMonth;
    }
}
```

### 缓存策略

#### Redis缓存设计
```java
@Service
public class CacheService {
    
    // 用户信息缓存
    @Cacheable(value = "user", key = "#userId", unless = "#result == null")
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    
    // 热门帖子缓存
    @Cacheable(value = "hotPosts", key = "'hot_posts_' + #page + '_' + #size")
    public List<CommunityPost> getHotPosts(int page, int size) {
        return communityPostRepository.findHotPosts(PageRequest.of(page, size));
    }
    
    // AI响应缓存
    @Cacheable(value = "aiResponse", key = "#request.hashCode()", unless = "#result == null")
    public String getAIResponse(AIRequest request) {
        return aiApiClient.generateResponse(request);
    }
    
    // 实时在线用户缓存
    public void setUserOnline(Long userId) {
        redisTemplate.opsForValue().set("online:user:" + userId, "true", Duration.ofMinutes(30));
    }
    
    // 未读消息计数缓存
    public void incrementUnreadCount(Long userId) {
        redisTemplate.opsForValue().increment("unread:count:" + userId);
    }
}
```

### 消息队列设计

#### RabbitMQ消息处理
```java
@Configuration
public class RabbitMQConfig {
    
    // AI处理队列
    @Bean
    public Queue aiProcessingQueue() {
        return QueueBuilder.durable("ai.processing.queue")
                .withArgument("x-message-ttl", 300000) // 5分钟TTL
                .build();
    }
    
    // 推送通知队列
    @Bean
    public Queue pushNotificationQueue() {
        return QueueBuilder.durable("push.notification.queue")
                .build();
    }
    
    // 邮件发送队列
    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable("email.queue")
                .build();
    }
}

@Component
public class MessageProducer {
    
    // 发送AI处理任务
    public void sendAIProcessingTask(AIProcessingTask task) {
        rabbitTemplate.convertAndSend("ai.processing.queue", task);
    }
    
    // 发送推送通知任务
    public void sendPushNotification(PushNotificationTask task) {
        rabbitTemplate.convertAndSend("push.notification.queue", task);
    }
}

@RabbitListener(queues = "ai.processing.queue")
@Component
public class AIProcessingConsumer {
    
    public void processAITask(AIProcessingTask task) {
        try {
            // 处理AI任务
            String result = aiService.processTask(task);
            
            // 保存结果
            aiResultService.saveResult(task.getTaskId(), result);
            
            // 通知用户
            notificationService.notifyAITaskComplete(task.getUserId(), result);
            
        } catch (Exception e) {
            // 错误处理和重试
            handleAIProcessingError(task, e);
        }
    }
}
```

## 安全设计

### 认证和授权
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/ws/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}

// JWT工具类
@Component
public class JwtTokenProvider {
    
    private final String jwtSecret = "heartplan_secret_key";
    private final int jwtExpirationInMs = 86400000; // 24小时
    
    public String generateToken(UserPrincipal userPrincipal) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);
        
        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        
        return Long.parseLong(claims.getSubject());
    }
}
```

### 数据加密和隐私保护
```java
@Service
public class EncryptionService {
    
    private final AESUtil aesUtil;
    
    // 敏感信息加密
    public String encryptSensitiveData(String data) {
        return aesUtil.encrypt(data);
    }
    
    // 敏感信息解密
    public String decryptSensitiveData(String encryptedData) {
        return aesUtil.decrypt(encryptedData);
    }
    
    // 密码哈希
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
    
    // 密码验证
    public boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}

// 数据脱敏工具
@Component
public class DataMaskingUtil {
    
    // 手机号脱敏
    public String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 7) {
            return phoneNumber;
        }
        return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7);
    }
    
    // 邮箱脱敏
    public String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String username = parts[0];
        if (username.length() <= 2) {
            return email;
        }
        return username.substring(0, 2) + "***@" + parts[1];
    }
}
```

## 部署架构

### 单体应用部署
```yaml
# docker-compose.yml
version: '3.8'
services:
  # HeartPlan后端应用
  heartplan-backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - MYSQL_HOST=mysql
      - MYSQL_PORT=3306
      - MYSQL_DATABASE=heartplan
      - MYSQL_USERNAME=heartplan
      - MYSQL_PASSWORD=heartplan123
      - REDIS_HOST=redis
      - REDIS_PORT=6379
      - OPENAI_API_KEY=${OPENAI_API_KEY}
      - FCM_SERVER_KEY=${FCM_SERVER_KEY}
      - FILE_UPLOAD_PATH=/app/uploads
    volumes:
      - ./uploads:/app/uploads
      - ./logs:/app/logs
    depends_on:
      - mysql
      - redis

  # MySQL数据库
  mysql:
    image: mysql:5.7.26
    environment:
      - MYSQL_ROOT_PASSWORD=heartplan123
      - MYSQL_DATABASE=heartplan
      - MYSQL_USER=heartplan
      - MYSQL_PASSWORD=heartplan123
      - MYSQL_CHARACTER_SET_SERVER=utf8mb4
      - MYSQL_COLLATION_SERVER=utf8mb4_unicode_ci
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./database/init.sql:/docker-entrypoint-initdb.d/init.sql
      - ./database/my.cnf:/etc/mysql/conf.d/my.cnf
    command: --default-authentication-plugin=mysql_native_password

  # Redis缓存
  redis:
    image: redis:7.0-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
      - ./redis/redis.conf:/usr/local/etc/redis/redis.conf
    command: redis-server /usr/local/etc/redis/redis.conf

  # Nginx静态文件服务和反向代理
  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf
      - ./nginx/ssl:/etc/nginx/ssl
      - ./uploads:/usr/share/nginx/html/uploads
    depends_on:
      - heartplan-backend

volumes:
  mysql_data:
  redis_data:
```

### Nginx配置
```nginx
# nginx/nginx.conf
events {
    worker_connections 1024;
}

http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;
    
    # 日志格式
    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for"';
    
    access_log /var/log/nginx/access.log main;
    error_log /var/log/nginx/error.log;
    
    # 基本设置
    sendfile on;
    tcp_nopush on;
    tcp_nodelay on;
    keepalive_timeout 65;
    types_hash_max_size 2048;
    client_max_body_size 50M;
    
    # Gzip压缩
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;
    
    # 上游服务器
    upstream heartplan_backend {
        server heartplan-backend:8080;
    }
    
    server {
        listen 80;
        server_name localhost;
        
        # API代理
        location /api/ {
            proxy_pass http://heartplan_backend;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            
            # WebSocket支持
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
        }
        
        # WebSocket代理
        location /ws/ {
            proxy_pass http://heartplan_backend;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
        
        # 静态文件服务
        location /uploads/ {
            alias /usr/share/nginx/html/uploads/;
            expires 30d;
            add_header Cache-Control "public, immutable";
        }
        
        # 健康检查
        location /health {
            proxy_pass http://heartplan_backend/actuator/health;
        }
    }
}
```

### 监控和日志系统
```yaml
  # Prometheus监控
  prometheus:
    image: prom/prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./monitoring/prometheus.yml:/etc/prometheus/prometheus.yml

  # Grafana可视化
  grafana:
    image: grafana/grafana
    ports:
      - "3001:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin123
    volumes:
      - grafana_data:/var/lib/grafana

  # ELK日志系统
  logstash:
    image: logstash:8.8.0
    volumes:
      - ./logging/logstash.conf:/usr/share/logstash/pipeline/logstash.conf

  kibana:
    image: kibana:8.8.0
    ports:
      - "5601:5601"
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
```

## 项目结构

### 前端项目结构
```
heartplan-mobile/
├── src/
│   ├── components/           # 可重用组件
│   │   ├── common/          # 通用组件
│   │   ├── forms/           # 表单组件
│   │   └── charts/          # 图表组件
│   ├── screens/             # 页面组件
│   │   ├── auth/            # 认证相关页面
│   │   ├── templates/       # 模板功能页面
│   │   ├── community/       # 社区功能页面
│   │   ├── chat/            # 聊天功能页面
│   │   └── profile/         # 个人中心页面
│   ├── navigation/          # 导航配置
│   ├── services/            # API服务
│   ├── utils/               # 工具函数
│   ├── hooks/               # 自定义Hooks
│   ├── store/               # 状态管理（Redux）
│   ├── styles/              # 样式文件
│   └── assets/              # 静态资源
├── android/                 # Android原生代码
├── ios/                     # iOS原生代码
├── package.json
└── metro.config.js
```

### 后端项目结构（单体应用）
```
heartplan-backend/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── heartplan/
│       │           ├── HeartPlanApplication.java    # 主启动类
│       │           ├── config/                      # 配置类
│       │           │   ├── SecurityConfig.java
│       │           │   ├── WebSocketConfig.java
│       │           │   ├── RedisConfig.java
│       │           │   └── SwaggerConfig.java
│       │           ├── controller/                  # 控制器层
│       │           │   ├── AuthController.java
│       │           │   ├── UserController.java
│       │           │   ├── TemplateController.java
│       │           │   ├── CommunityController.java
│       │           │   ├── ChatController.java
│       │           │   └── FileController.java
│       │           ├── service/                     # 业务逻辑层
│       │           │   ├── AuthService.java
│       │           │   ├── UserService.java
│       │           │   ├── TemplateService.java
│       │           │   ├── CommunityService.java
│       │           │   ├── ChatService.java
│       │           │   ├── AIService.java
│       │           │   ├── NotificationService.java
│       │           │   └── FileService.java
│       │           ├── repository/                  # 数据访问层
│       │           │   ├── UserRepository.java
│       │           │   ├── DatingRecordRepository.java
│       │           │   ├── CommunityPostRepository.java
│       │           │   ├── ChatMessageRepository.java
│       │           │   └── CommentRepository.java
│       │           ├── entity/                      # 实体类
│       │           │   ├── User.java
│       │           │   ├── DatingRecord.java
│       │           │   ├── CommunityPost.java
│       │           │   ├── ChatMessage.java
│       │           │   └── Comment.java
│       │           ├── dto/                         # 数据传输对象
│       │           │   ├── request/
│       │           │   └── response/
│       │           ├── util/                        # 工具类
│       │           │   ├── JwtTokenProvider.java
│       │           │   ├── EncryptionUtil.java
│       │           │   ├── ImageProcessingUtil.java
│       │           │   └── DateUtil.java
│       │           ├── exception/                   # 异常处理
│       │           │   ├── GlobalExceptionHandler.java
│       │           │   └── CustomExceptions.java
│       │           └── websocket/                   # WebSocket处理
│       │               ├── ChatWebSocketHandler.java
│       │               └── WebSocketSessionManager.java
│       └── resources/
│           ├── application.yml                      # 主配置文件
│           ├── application-dev.yml                  # 开发环境配置
│           ├── application-prod.yml                 # 生产环境配置
│           ├── application-docker.yml               # Docker环境配置
│           └── static/                              # 静态资源
├── database/                                        # 数据库相关
│   ├── init.sql                                    # 初始化脚本
│   ├── schema.sql                                  # 表结构
│   └── data.sql                                    # 初始数据
├── docker/                                         # Docker配置
│   ├── Dockerfile
│   └── docker-compose.yml
├── nginx/                                          # Nginx配置
│   └── nginx.conf
├── logs/                                           # 日志目录
├── uploads/                                        # 文件上传目录
├── pom.xml                                         # Maven配置
└── README.md
```

## 开发规范

### 代码规范
```java
// Java代码规范示例
@RestController
@RequestMapping("/api/v1/users")
@Validated
@Slf4j
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息")
    public ResponseEntity<ApiResponse<UserProfileDto>> getUserProfile(
            @PathVariable @Min(1) Long id) {
        
        log.info("获取用户信息请求: userId={}", id);
        
        try {
            UserProfileDto profile = userService.getUserProfile(id);
            return ResponseEntity.ok(ApiResponse.success(profile));
        } catch (UserNotFoundException e) {
            log.warn("用户不存在: userId={}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("获取用户信息失败: userId={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("服务器内部错误"));
        }
    }
}
```

### React Native代码规范（JavaScript + JSX）
```javascript
// React Native组件规范示例 - 使用JavaScript和JSX
import React, { useState, useEffect, useCallback } from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { useSelector, useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

/**
 * 现代化按钮组件
 * 支持多种样式变体和交互状态
 */
const ModernButton = ({ title, onPress, variant, disabled }) => {
  const [isPressed, setIsPressed] = useState(false);
  
  const handlePressIn = useCallback(() => {
    setIsPressed(true);
  }, []);
  
  const handlePressOut = useCallback(() => {
    setIsPressed(false);
  }, []);
  
  const buttonStyle = [
    styles.button,
    styles[variant],
    isPressed && styles.pressed,
    disabled && styles.disabled
  ];
  
  return (
    <TouchableOpacity
      style={buttonStyle}
      onPress={onPress}
      onPressIn={handlePressIn}
      onPressOut={handlePressOut}
      disabled={disabled}
      activeOpacity={0.8}
    >
      <Text style={[styles.text, styles[`${variant}Text`]]}>{title}</Text>
    </TouchableOpacity>
  );
};

// PropTypes类型检查（替代TypeScript）
ModernButton.propTypes = {
  title: PropTypes.string.isRequired,
  onPress: PropTypes.func.isRequired,
  variant: PropTypes.oneOf(['primary', 'secondary', 'outline']),
  disabled: PropTypes.bool
};

ModernButton.defaultProps = {
  variant: 'primary',
  disabled: false
};

const styles = StyleSheet.create({
  button: {
    paddingHorizontal: 24,
    paddingVertical: 12,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: 48,
  },
  primary: {
    backgroundColor: '#FF6B6B',
    shadowColor: '#FF6B6B',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 8,
  },
  secondary: {
    backgroundColor: '#4ECDC4',
    shadowColor: '#4ECDC4',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 8,
  },
  outline: {
    backgroundColor: 'transparent',
    borderWidth: 2,
    borderColor: '#FF6B6B',
  },
  pressed: {
    transform: [{ scale: 0.95 }],
  },
  disabled: {
    opacity: 0.5,
  },
  text: {
    fontSize: 16,
    fontWeight: '600',
  },
  primaryText: {
    color: '#FFFFFF',
  },
  secondaryText: {
    color: '#FFFFFF',
  },
  outlineText: {
    color: '#FF6B6B',
  },
});

export default ModernButton;
```

### API服务层（JavaScript）
```javascript
// API服务类 - 使用JavaScript
import AsyncStorage from '@react-native-async-storage/async-storage';

class ApiService {
  constructor() {
    this.baseURL = 'http://localhost:8080/api';
    this.timeout = 10000;
  }

  // 获取认证令牌
  async getAuthToken() {
    try {
      return await AsyncStorage.getItem('auth_token');
    } catch (error) {
      console.error('获取认证令牌失败:', error);
      return null;
    }
  }

  // 通用请求方法
  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const token = await this.getAuthToken();
    
    const defaultOptions = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        ...(token && { 'Authorization': `Bearer ${token}` }),
      },
      timeout: this.timeout,
    };

    const finalOptions = {
      ...defaultOptions,
      ...options,
      headers: {
        ...defaultOptions.headers,
        ...options.headers,
      },
    };

    try {
      const response = await fetch(url, finalOptions);
      
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }
      
      const data = await response.json();
      return data;
    } catch (error) {
      console.error('API请求失败:', error);
      throw error;
    }
  }

  // 用户认证API
  async login(email, password) {
    return this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
  }

  async register(userData) {
    return this.request('/auth/register', {
      method: 'POST',
      body: JSON.stringify(userData),
    });
  }

  // 用户管理API
  async getUserProfile() {
    return this.request('/users/profile');
  }

  async updateUserProfile(profileData) {
    return this.request('/users/profile', {
      method: 'PUT',
      body: JSON.stringify(profileData),
    });
  }

  // 模板功能API
  async getDatingRecords() {
    return this.request('/templates/dating-management');
  }

  async createDatingRecord(recordData) {
    return this.request('/templates/dating-management', {
      method: 'POST',
      body: JSON.stringify(recordData),
    });
  }

  // 社区功能API
  async getCommunityPosts(page = 0, size = 20) {
    return this.request(`/community/posts?page=${page}&size=${size}`);
  }

  async createPost(postData) {
    return this.request('/community/posts', {
      method: 'POST',
      body: JSON.stringify(postData),
    });
  }

  // 聊天功能API
  async getChatContacts() {
    return this.request('/chat/contacts');
  }

  async sendMessage(userId, messageData) {
    return this.request(`/chat/conversations/${userId}/messages`, {
      method: 'POST',
      body: JSON.stringify(messageData),
    });
  }
}

export default new ApiService();
```

本设计文档为使用指定技术栈实现HeartPlan AI恋爱应用提供了全面的技术基础，确保系统的可扩展性、可维护性和优秀的用户体验。

### 设计亮点

1. **简洁单体架构**: 传统Spring Boot单体应用，开发简单、部署方便、维护成本低
2. **高性能优化**: Redis缓存、数据库索引优化、连接池配置，确保快速响应
3. **跨平台用户体验**: React Native + JavaScript/JSX，提供原生应用体验，无需TypeScript复杂性
4. **智能AI集成**: GPT-4驱动的教练系统、兼容性评分算法、个性化建议引擎
5. **实时通信系统**: Spring WebSocket，支持实时聊天、在线状态、消息推送
6. **企业级安全**: JWT认证、Spring Security、数据加密、隐私保护
7. **容器化部署**: Docker + Docker Compose，支持一键部署，易于运维
8. **本地文件存储**: 使用本地文件系统 + Nginx静态服务，降低外部依赖
9. **数据驱动决策**: 完整的用户行为分析、关系健康评分、AI洞察报告
10. **清晰架构分层**: Controller-Service-Repository经典三层架构，便于理解和维护
11. **统一技术栈**: Java后端 + JavaScript前端，技术栈统一，学习成本低
12. **快速开发**: 单体应用便于调试，开发效率高，适合快速迭代
13. **Lombok代码简化**: 使用Lombok注解减少样板代码，提高开发效率和代码可读性

## 错误处理策略

### 全局异常处理机制

使用Spring Boot的全局异常处理，结合Lombok简化异常类定义：

```java
@RestControllerAdvice
@Slf4j // Lombok注解：自动生成日志对象
public class GlobalExceptionHandler {
    
    /**
     * 处理资源不存在异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("资源不存在: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
                .code("RESOURCE_NOT_FOUND")
                .message("Resource not found") // 英文错误消息
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("参数验证失败: {}", ex.getMessage());
        
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        
        ErrorResponse error = ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message("Validation failed") // 英文错误消息
                .details(errors)
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    /**
     * 处理认证异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        log.warn("认证失败: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
                .code("AUTHENTICATION_FAILED")
                .message("Invalid email or password") // 英文错误消息
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
    
    /**
     * 处理业务逻辑异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("业务异常: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage()) // 业务异常消息应该已经是英文
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("系统异常", ex);
        
        ErrorResponse error = ErrorResponse.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("Internal server error") // 英文错误消息
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

// 错误响应DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private List<String> details;
    private LocalDateTime timestamp;
}

// 自定义异常类
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;
    
    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}

@Getter
public class BusinessException extends RuntimeException {
    private final String errorCode;
    
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

@Getter
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
```

### 前端错误处理

```javascript
// 错误处理工具类
class ErrorHandler {
  static handleApiError(error) {
    console.error('API Error:', error);
    
    if (error.response) {
      // Server response error
      const { status, data } = error.response;
      
      switch (status) {
        case 400:
          return 'Invalid request parameters';
        case 401:
          return 'Authentication failed, please login again';
        case 403:
          return 'Access denied';
        case 404:
          return 'Resource not found';
        case 500:
          return 'Internal server error';
        default:
          return data?.message || 'Network request failed';
      }
    } else if (error.request) {
      // Network error
      return 'Network connection failed, please check your connection';
    } else {
      // Other errors
      return error.message || 'Unknown error';
    }
  }
  
  static showError(message) {
    // Show error message (using Toast or Alert)
    Alert.alert('Error', message);
  }
}

export default ErrorHandler;
```

## 代码简洁性规范

### 核心原则
**所有代码必须简洁明了，能使用插件或工具简化的代码都必须使用相应的简化方案。**

### 英文界面规范（强制）
**⚠️ 重要：凡是用户能够感知到的信息，包括但不限于UI界面文案、错误提示、按钮文字、提示信息、通知消息等，都必须使用英文。**

#### 适用范围
- **前端UI界面**：所有按钮、标签、标题、提示文字必须使用英文
- **错误消息**：所有用户可见的错误提示必须使用英文
- **通知消息**：推送通知、系统消息、成功提示等必须使用英文
- **表单验证**：表单验证错误信息必须使用英文
- **API响应**：返回给前端的错误消息和提示信息必须使用英文
- **邮件内容**：发送给用户的邮件内容必须使用英文

#### 示例对比
```java
// ❌ 错误：使用中文错误消息
@NotBlank(message = "邮箱不能为空")
private String email;

// ✅ 正确：使用英文错误消息
@NotBlank(message = "Email cannot be empty")
private String email;
```

```javascript
// ❌ 错误：使用中文UI文案
<Text>登录</Text>

// ✅ 正确：使用英文UI文案
<Text>Login</Text>
```

### 后端代码简化规范

#### 1. Lombok使用规范（强制）
- **实体类**：必须使用@Data、@Builder、@NoArgsConstructor、@AllArgsConstructor
- **Service类**：必须使用@Slf4j、@RequiredArgsConstructor
- **Controller类**：必须使用@Slf4j、@RequiredArgsConstructor
- **DTO类**：必须使用@Data、@Builder、@NoArgsConstructor、@AllArgsConstructor
- **异常类**：必须使用@Getter简化getter方法

#### 2. Spring Boot注解简化（强制）
- **依赖注入**：使用@RequiredArgsConstructor替代@Autowired
- **配置类**：使用@ConfigurationProperties替代@Value
- **缓存**：使用@Cacheable、@CacheEvict等注解
- **事务**：使用@Transactional注解
- **验证**：使用@Valid、@Validated等注解

#### 3. JPA简化（强制）
- **Repository**：继承JpaRepository，使用方法命名查询
- **实体关系**：使用@OneToMany、@ManyToOne等注解
- **审计**：使用@CreationTimestamp、@UpdateTimestamp

### 前端代码简化规范

#### 1. React Native Hooks（强制）
- **状态管理**：使用useState、useEffect、useCallback、useMemo
- **自定义Hooks**：封装重复逻辑为自定义Hooks
- **导航**：使用useNavigation、useRoute

#### 2. 工具库使用（强制）
- **日期处理**：使用moment.js或date-fns
- **表单验证**：使用formik或react-hook-form
- **状态管理**：使用Redux Toolkit简化Redux代码
- **网络请求**：使用axios或fetch的封装

#### 3. 组件简化（强制）
- **样式**：使用StyleSheet.create统一管理样式
- **类型检查**：使用PropTypes进行类型检查
- **默认属性**：使用defaultProps设置默认值

### 代码示例对比

#### ❌ 不推荐的冗长写法
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private String username;
    
    public User() {}
    
    public User(Long id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    // toString, equals, hashCode方法...
}
```

#### ✅ 推荐的简洁写法
```java
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private String username;
}
```

### 开发工具配置要求

#### IDE插件（必装）
- **IntelliJ IDEA**：Lombok Plugin、Spring Boot Assistant
- **VS Code**：ES7+ React/Redux/React-Native snippets、Prettier

#### 代码格式化（强制）
- **Java**：使用Google Java Style或阿里巴巴Java规范
- **JavaScript**：使用Prettier进行代码格式化
- **统一配置**：项目根目录必须包含.editorconfig文件

### 重要规则声明

**⚠️ 重要：本项目及后续所有设计文档都不需要包含测试用例和测试策略相关内容。**

**专注于功能实现和业务逻辑，所有代码都必须遵循简洁性原则，最大化使用工具和插件来减少样板代码。**

本设计文档全面覆盖了HeartPlan AI恋爱应用的技术架构、实现细节和开发规范，特别强调了Lombok的使用来简化Java代码开发，为项目的成功实施提供了坚实的技术基础。