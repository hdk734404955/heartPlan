# HeartPlan 数据库脚本说明

## 概述

本目录包含HeartPlan后端项目的数据库相关脚本，基于简化后的User实体类设计。

## 文件说明

### 1. schema.sql
- **用途**: 创建简化的用户表结构
- **内容**: 
  - 删除旧的users表（如果存在）
  - 创建新的users表，包含所有简化后的字段
  - 创建必要的索引和约束
- **特点**:
  - 包含完整的字段约束和验证
  - 设置email和username的唯一索引
  - 包含年龄、性别、恋爱状态等业务约束
  - 使用utf8mb4字符集支持emoji等特殊字符

### 2. init.sql
- **用途**: 数据库初始化脚本
- **内容**:
  - 执行schema.sql创建表结构
  - 插入初始测试数据
  - 验证创建结果
- **适用场景**: 全新部署或开发环境初始化

### 3. migration.sql
- **用途**: 数据库迁移脚本
- **内容**:
  - 备份现有数据
  - 创建新表结构
  - 提供数据迁移模板
  - 验证迁移结果
- **适用场景**: 从旧版本升级到简化版本

## 数据库表结构

### users表字段说明

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 用户ID，主键 |
| email | VARCHAR(100) | NOT NULL, UNIQUE | 用户邮箱，登录用户名 |
| username | VARCHAR(50) | NOT NULL, UNIQUE | 用户显示名称 |
| password | VARCHAR(255) | NOT NULL | 加密后的密码 |
| age | INT | NOT NULL, CHECK(18-35) | 用户年龄 |
| gender | VARCHAR(10) | NOT NULL, ENUM | 性别：MALE/FEMALE/OTHER |
| relationship_status | VARCHAR(20) | NOT NULL, ENUM | 恋爱状态：SINGLE/IN_RELATIONSHIP |
| avatar_url | VARCHAR(500) | NULL | 用户头像URL |
| location | VARCHAR(100) | NULL | 用户位置 |
| enabled | BOOLEAN | NOT NULL, DEFAULT TRUE | 账户启用状态 |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE | 更新时间 |

### 索引说明

- `idx_user_email`: 邮箱唯一索引
- `idx_user_username`: 用户名唯一索引
- `idx_user_created_at`: 创建时间索引
- `idx_user_enabled`: 启用状态索引
- `idx_user_age`: 年龄索引（用于匹配查询）
- `idx_user_gender`: 性别索引（用于匹配查询）
- `idx_user_relationship_status`: 恋爱状态索引（用于匹配查询）
- `idx_user_location`: 地理位置索引

## 使用方法

### 全新部署
```bash
# 连接到MySQL数据库
mysql -u username -p database_name

# 执行初始化脚本
source /path/to/backend/database/init.sql;
```

### 从旧版本迁移
```bash
# 连接到MySQL数据库
mysql -u username -p database_name

# 执行迁移脚本
source /path/to/backend/database/migration.sql;
```

### 只创建表结构
```bash
# 连接到MySQL数据库
mysql -u username -p database_name

# 执行表结构脚本
source /path/to/backend/database/schema.sql;
```

## Docker环境使用

如果使用Docker部署，可以将init.sql脚本挂载到MySQL容器的初始化目录：

```yaml
# docker-compose.yml示例
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: your_password
      MYSQL_DATABASE: heartplan
    volumes:
      - ./backend/database/init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "3306:3306"
```

## 注意事项

1. **字符集**: 所有表使用utf8mb4字符集，支持emoji等特殊字符
2. **密码安全**: 示例数据中的密码已使用BCrypt加密
3. **约束验证**: 包含完整的业务约束，确保数据完整性
4. **索引优化**: 针对常用查询场景创建了相应索引
5. **备份重要**: 在执行迁移前请务必备份现有数据

## 验证脚本

执行完脚本后，可以使用以下SQL验证结果：

```sql
-- 检查表结构
DESCRIBE users;

-- 检查索引
SHOW INDEX FROM users;

-- 检查数据
SELECT COUNT(*) FROM users;
SELECT * FROM users LIMIT 5;

-- 检查约束
SELECT * FROM users WHERE age < 18 OR age > 35; -- 应该返回空结果
```

## 相关需求

本数据库设计满足以下需求：
- 需求7.1: 基于简化后的User实体创建新的表结构
- 需求7.2: 只包含必要的字段和索引
- 需求7.3: 保持email和username的唯一索引
- 需求7.4: 将location设为可选字段，其他核心字段设为必填