-- HeartPlan 后端数据库表结构
-- 基于简化后的User实体类创建的数据库表结构
-- 创建时间: 2025-01-24

-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS users;

-- 创建简化的用户表
CREATE TABLE users (
    -- 主键字段
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID，主键',
    
    -- 核心用户字段
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '用户邮箱，用作登录用户名，必填且唯一',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户显示名称，必填且唯一',
    password VARCHAR(255) NOT NULL COMMENT '加密后的密码，必填，最少6位',
    age INT NOT NULL COMMENT '用户年龄，必填，18-35岁',
    
    -- 枚举字段
    gender VARCHAR(10) NOT NULL COMMENT '用户性别枚举：MALE/FEMALE/OTHER',
    relationship_status VARCHAR(20) NOT NULL COMMENT '恋爱状态枚举：SINGLE/IN_RELATIONSHIP',
    
    -- 可选字段
    avatar_url VARCHAR(500) NULL COMMENT '用户头像URL，可选',
    location VARCHAR(100) NULL COMMENT '用户所在位置，可选，最大100字符',
    
    -- 账户状态字段
    enabled BOOLEAN NOT NULL DEFAULT TRUE COMMENT '账户是否启用，默认为true',
    
    -- 审计字段
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 约束检查
    CONSTRAINT chk_age CHECK (age >= 18 AND age <= 35),
    CONSTRAINT chk_gender CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    CONSTRAINT chk_relationship_status CHECK (relationship_status IN ('SINGLE', 'IN_RELATIONSHIP')),
    CONSTRAINT chk_email_format CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'),
    CONSTRAINT chk_username_length CHECK (CHAR_LENGTH(username) >= 2 AND CHAR_LENGTH(username) <= 50),
    CONSTRAINT chk_location_length CHECK (location IS NULL OR CHAR_LENGTH(location) <= 100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='简化的用户表';

-- 创建索引
-- 邮箱唯一索引（已通过UNIQUE约束创建）
CREATE UNIQUE INDEX idx_user_email ON users(email) COMMENT '用户邮箱唯一索引';

-- 用户名唯一索引（已通过UNIQUE约束创建）
CREATE UNIQUE INDEX idx_user_username ON users(username) COMMENT '用户名唯一索引';

-- 创建时间索引（用于查询优化）
CREATE INDEX idx_user_created_at ON users(created_at) COMMENT '创建时间索引';

-- 启用状态索引（用于查询活跃用户）
CREATE INDEX idx_user_enabled ON users(enabled) COMMENT '启用状态索引';

-- 年龄索引（用于匹配查询）
CREATE INDEX idx_user_age ON users(age) COMMENT '年龄索引';

-- 性别索引（用于匹配查询）
CREATE INDEX idx_user_gender ON users(gender) COMMENT '性别索引';

-- 恋爱状态索引（用于匹配查询）
CREATE INDEX idx_user_relationship_status ON users(relationship_status) COMMENT '恋爱状态索引';

-- 地理位置索引（用于地理位置匹配）
CREATE INDEX idx_user_location ON users(location) COMMENT '地理位置索引';