-- HeartPlan 数据库初始化脚本
-- 包含表结构创建和初始数据插入
-- 创建时间: 2025-01-24

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 执行表结构创建
SOURCE schema.sql;

-- 插入初始测试数据（可选）
-- 注意：密码字段应该是加密后的值，这里使用明文仅用于测试
-- 在实际生产环境中，密码应该通过BCrypt等方式加密

INSERT INTO users (
    email, 
    username, 
    password, 
    age, 
    gender, 
    relationship_status, 
    avatar_url, 
    location, 
    enabled
) VALUES 
(
    'john.doe@example.com',
    'johndoe',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyU4VmDMbvO', -- 加密后的 "password123"
    25,
    'MALE',
    'SINGLE',
    'https://example.com/avatars/john.jpg',
    'New York',
    TRUE
),
(
    'jane.smith@example.com',
    'janesmith',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyU4VmDMbvO', -- 加密后的 "password123"
    28,
    'FEMALE',
    'SINGLE',
    'https://example.com/avatars/jane.jpg',
    'Los Angeles',
    TRUE
),
(
    'alex.wilson@example.com',
    'alexwilson',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyU4VmDMbvO', -- 加密后的 "password123"
    30,
    'OTHER',
    'IN_RELATIONSHIP',
    NULL,
    'Chicago',
    TRUE
),
(
    'emily.brown@example.com',
    'emilybrown',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyU4VmDMbvO', -- 加密后的 "password123"
    22,
    'FEMALE',
    'SINGLE',
    'https://example.com/avatars/emily.jpg',
    NULL,
    TRUE
),
(
    'michael.davis@example.com',
    'michaeldavis',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaLyU4VmDMbvO', -- 加密后的 "password123"
    35,
    'MALE',
    'SINGLE',
    'https://example.com/avatars/michael.jpg',
    'Miami',
    FALSE -- 禁用状态的用户示例
);

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 显示创建结果
SELECT 'Users table created successfully with initial data' AS status;
SELECT COUNT(*) AS total_users FROM users;
SELECT COUNT(*) AS enabled_users FROM users WHERE enabled = TRUE;