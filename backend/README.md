# HeartPlan Backend

HeartPlan AI恋爱规划应用后端服务，基于Spring Boot 3.1.x构建。

## 技术栈

- **Java**: 17 LTS
- **Spring Boot**: 3.1.5
- **Spring Security**: JWT认证
- **Spring Data JPA**: 数据持久化
- **MySQL**: 5.7.26 数据库
- **Redis**: 7.0.x 缓存
- **Lombok**: 1.18.30 代码简化
- **Swagger/OpenAPI**: 3.0 API文档

## 项目结构

```
backend/
├── src/main/java/com/heartplan/
│   ├── HeartPlanApplication.java     # 主启动类
│   ├── controller/                   # 控制器层
│   ├── service/                      # 业务逻辑层
│   ├── repository/                   # 数据访问层
│   ├── entity/                       # 实体类
│   ├── dto/                          # 数据传输对象
│   ├── config/                       # 配置类
│   ├── exception/                    # 异常处理
│   └── util/                         # 工具类
├── src/main/resources/
│   ├── application.yml               # 应用配置
│   └── static/                       # 静态资源
└── pom.xml                          # Maven配置
```

## Lombok配置

### IDE配置要求

#### IntelliJ IDEA
1. 安装Lombok插件：
   - File → Settings → Plugins → 搜索"Lombok" → 安装
2. 启用注解处理器：
   - File → Settings → Build → Compiler → Annotation Processors
   - 勾选"Enable annotation processing"

#### Eclipse
1. 下载lombok.jar：https://projectlombok.org/download
2. 运行安装程序：`java -jar lombok.jar`
3. 选择Eclipse安装目录并安装

### 常用Lombok注解

- `@Data`: 自动生成getter、setter、toString、equals、hashCode
- `@NoArgsConstructor`: 生成无参构造函数
- `@AllArgsConstructor`: 生成全参构造函数
- `@RequiredArgsConstructor`: 为final字段生成构造函数
- `@Builder`: 生成建造者模式代码
- `@Slf4j`: 自动生成日志对象
- `@EqualsAndHashCode`: 自定义equals和hashCode生成规则

## 快速开始

### 环境要求
- Java 17+
- Maven 3.6+
- MySQL 5.7.26+
- Redis 7.0+

### 数据库配置
1. 创建数据库：
```sql
CREATE DATABASE heartplan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'heartplan'@'localhost' IDENTIFIED BY 'heartplan123';
GRANT ALL PRIVILEGES ON heartplan.* TO 'heartplan'@'localhost';
FLUSH PRIVILEGES;
```

2. 配置环境变量（可选）：
```bash
export DB_USERNAME=heartplan
export DB_PASSWORD=heartplan123
export REDIS_HOST=localhost
export REDIS_PORT=6379
export JWT_SECRET=your-jwt-secret-key
```

### 启动应用
```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java -jar target/heartplan-backend-0.0.1-SNAPSHOT.jar
```

### 访问地址
- 应用服务：http://localhost:8080/api
- API文档：http://localhost:8080/api/swagger-ui.html
- 健康检查：http://localhost:8080/api/actuator/health

## 开发规范

### 代码规范
1. 实体类直接实现必要接口（如UserDetails），使用JPA审计功能管理时间字段
2. 使用Lombok注解简化代码
3. 控制器类使用`@Slf4j`和`@RequiredArgsConstructor`
4. 服务类使用`@Slf4j`和`@RequiredArgsConstructor`
5. DTO类使用`@Data`、`@Builder`等注解

### 日志规范
- 使用`@Slf4j`注解自动生成日志对象
- 重要操作记录INFO级别日志
- 调试信息记录DEBUG级别日志
- 异常信息记录ERROR级别日志

### 安全规范
- 所有API接口默认需要JWT认证
- 公开接口在SecurityConfig中明确配置
- 密码使用BCrypt加密存储
- 敏感信息不记录在日志中

## API文档

启动应用后访问 http://localhost:8080/api/swagger-ui.html 查看完整的API文档。

## 故障排除

### 常见问题

1. **Lombok注解不生效**
   - 确认IDE已安装Lombok插件
   - 确认启用了注解处理器
   - 重启IDE

2. **数据库连接失败**
   - 检查MySQL服务是否启动
   - 确认数据库用户名密码正确
   - 检查防火墙设置

3. **Redis连接失败**
   - 检查Redis服务是否启动
   - 确认Redis配置正确
   - 检查网络连接

4. **JWT令牌问题**
   - 确认JWT密钥配置正确
   - 检查令牌是否过期
   - 验证请求头格式：`Authorization: Bearer <token>`