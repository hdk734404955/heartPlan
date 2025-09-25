# 后端代码简化重构实施任务

- [x] 1. 添加MapStruct依赖和配置






  - 在pom.xml中添加MapStruct依赖
  - 配置MapStruct编译插件
  - 确保编译时能正确生成映射器实现类
  - _需求: 1.1, 1.5, 7.1_

- [x] 2. 更新ApiResponse类使用Lombok





  - 修改ApiResponse类，使用Lombok注解简化代码
  - 添加静态方法用于创建成功和错误响应
  - 确保格式为{code, data, message}
  - _需求: 2.1, 2.2, 2.3_

- [x] 3. 全局响应处理器已存在







  - GlobalResponseHandler类已存在并正常工作
  - 自动包装所有Controller的返回值为统一格式
  - _需求: 2.4_

- [x] 4. 更新全局异常处理器





  - 更新GlobalExceptionHandler类，移除对ResponseBuilder的依赖
  - 直接使用ApiResponse构造方法创建响应
  - 确保所有异常都返回统一的响应格式
  - 使用英文错误消息
  - _需求: 5.1, 5.2, 5.3, 5.4, 5.5_

- [x] 5. 删除文件上传相关功能





  - [x] 5.1 删除文件相关类


    - 删除FileService.java文件
    - 删除FileController.java文件
    - 删除FileUploadConfig.java文件
    - _需求: 3.1, 3.2, 3.3_
  
  - [x] 5.2 清理AuthService中的文件依赖


    - 移除AuthService中对FileService的依赖
    - 简化头像处理逻辑，直接使用用户提供的URL
    - 移除随机头像生成功能
    - _需求: 3.4, 3.5_

- [x] 6. 创建BaseEntity基础实体类





  - [x] 6.1 创建BaseEntity类


    - 创建BaseEntity抽象类，实现Serializable接口
    - 使用@MappedSuperclass注解标记为JPA映射超类
    - 定义id主键字段，使用@GeneratedValue自增策略
    - 添加createdAt和updatedAt时间字段，配置JPA审计注解
    - 最大化使用Lombok注解
    - _需求: 6.1, 6.2, 6.3_
  
  - [x] 6.2 更新User实体继承BaseEntity


    - 修改User类继承BaseEntity
    - 移除User中重复的id、createdAt、updatedAt字段定义
    - 更新@EqualsAndHashCode注解为callSuper = true
    - 移除@EntityListeners注解（由BaseEntity处理）
    - _需求: 6.4, 6.5_

- [x] 7. 进一步简化User实体类






  - 移除验证消息中的冗长文本，使用默认消息
  - 简化枚举类，移除displayName方法
  - 将getAuthorities()方法返回空集合，移除权限系统
  - 确保UserDetails实现极简化
  - _需求: 7.1, 7.2, 7.3, 7.4, 7.5, 4.1, 4.2, 4.3, 4.4, 4.5_

- [x] 8. 创建MapStruct映射器





  - [x] 8.1 创建UserMapper接口


    - 定义User实体和DTO之间的映射方法
    - 使用@Mapper注解配置为Spring组件
    - 配置字段映射规则，忽略不需要映射的字段
    - _需求: 1.2, 1.3, 1.4_
  


  - [x] 8.2 更新Service层使用MapStruct





    - 在AuthService中注入UserMapper
    - 使用MapStruct替换手动的对象映射代码
    - 简化注册和登录方法中的对象转换逻辑
    - _需求: 1.3_

- [x] 9. 更新Controller返回统一响应格式





  - 修改AuthController中的所有方法
  - 确保返回值会被全局响应处理器自动包装
  - 移除手动创建响应格式的代码
  - _需求: 2.5_

- [x] 10. 删除无用文件和清理代码





  - [x] 10.1 删除无用工具类


    - 删除ResponseBuilder.java和ResponseCode.java工具类
    - 删除所有.gitkeep文件
    - 删除DataInitializer.java配置文件
    - _需求: 11.1, 11.2, 11.3, 11.4_
  

  - [x] 10.2 清理未使用的导入和代码

    - 移除所有未使用的import语句
    - 删除不再需要的工具类（保留Redis和WebSocket相关配置）
    - 确保代码编译无警告
    - _需求: 10.5, 11.5_

- [x] 11. 简化Spring Security配置









  - 更新SecurityConfig类
  - 移除复杂的权限配置
  - 简化JWT过滤器配置
  - 确保只保留基本的认证功能
  - _需求: 4.4, 4.5_

- [x] 12. 最大化使用Lombok优化所有类





  - [x] 12.1 优化DTO类的Lombok使用


    - 确保所有DTO类使用完整的Lombok注解
    - 检查@Data、@Builder、@NoArgsConstructor、@AllArgsConstructor的使用
    - _需求: 8.1, 8.2, 8.3, 8.5_
  

  - [x] 12.2 优化Service和Controller类的Lombok使用

    - 将所有手动创建的Logger替换为@Slf4j注解
    - 使用@RequiredArgsConstructor替换构造函数注入
    - 检查所有类是否充分使用Lombok注解
    - _需求: 8.4, 8.5_

- [ ] 13. 编译验证和功能测试
  - 编译项目确保MapStruct正确生成映射器
  - 测试登录和注册接口的统一响应格式
  - 验证全局异常处理是否正常工作
  - 确保所有功能正常且代码简洁
  - 验证Lombok注解是否正确工作
  - _需求: 所有需求的验证_