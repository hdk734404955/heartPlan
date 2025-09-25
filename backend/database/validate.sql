-- HeartPlan 数据库结构验证脚本
-- 用于验证创建的数据库表结构是否符合需求
-- 创建时间: 2025-01-24

-- 设置字符集
SET NAMES utf8mb4;

-- 验证表是否存在
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN 'PASS: users table exists'
        ELSE 'FAIL: users table does not exist'
    END AS table_existence_check
FROM information_schema.tables 
WHERE table_schema = DATABASE() AND table_name = 'users';

-- 验证表结构
SELECT 'Table Structure Validation' AS validation_section;

-- 检查必填字段
SELECT 
    column_name,
    data_type,
    is_nullable,
    column_default,
    character_maximum_length,
    CASE 
        WHEN column_name IN ('id', 'email', 'username', 'password', 'age', 'gender', 'relationship_status', 'enabled', 'created_at', 'updated_at') 
             AND is_nullable = 'NO' THEN 'PASS'
        WHEN column_name IN ('avatar_url', 'location') 
             AND is_nullable = 'YES' THEN 'PASS'
        ELSE 'FAIL'
    END AS nullable_check
FROM information_schema.columns 
WHERE table_schema = DATABASE() AND table_name = 'users'
ORDER BY ordinal_position;

-- 验证字段长度约束
SELECT 'Field Length Validation' AS validation_section;

SELECT 
    column_name,
    character_maximum_length,
    CASE 
        WHEN column_name = 'email' AND character_maximum_length = 100 THEN 'PASS'
        WHEN column_name = 'username' AND character_maximum_length = 50 THEN 'PASS'
        WHEN column_name = 'password' AND character_maximum_length = 255 THEN 'PASS'
        WHEN column_name = 'gender' AND character_maximum_length = 10 THEN 'PASS'
        WHEN column_name = 'relationship_status' AND character_maximum_length = 20 THEN 'PASS'
        WHEN column_name = 'avatar_url' AND character_maximum_length = 500 THEN 'PASS'
        WHEN column_name = 'location' AND character_maximum_length = 100 THEN 'PASS'
        WHEN column_name NOT IN ('email', 'username', 'password', 'gender', 'relationship_status', 'avatar_url', 'location') THEN 'N/A'
        ELSE 'FAIL'
    END AS length_check
FROM information_schema.columns 
WHERE table_schema = DATABASE() 
  AND table_name = 'users' 
  AND data_type IN ('varchar', 'char');

-- 验证索引
SELECT 'Index Validation' AS validation_section;

SELECT 
    index_name,
    column_name,
    non_unique,
    CASE 
        WHEN index_name = 'idx_user_email' AND column_name = 'email' AND non_unique = 0 THEN 'PASS: Email unique index'
        WHEN index_name = 'idx_user_username' AND column_name = 'username' AND non_unique = 0 THEN 'PASS: Username unique index'
        WHEN index_name IN ('idx_user_created_at', 'idx_user_enabled', 'idx_user_age', 'idx_user_gender', 'idx_user_relationship_status', 'idx_user_location') THEN 'PASS: Query optimization index'
        WHEN index_name = 'PRIMARY' THEN 'PASS: Primary key'
        ELSE 'INFO: Additional index'
    END AS index_check
FROM information_schema.statistics 
WHERE table_schema = DATABASE() AND table_name = 'users'
ORDER BY index_name, seq_in_index;

-- 验证约束
SELECT 'Constraint Validation' AS validation_section;

-- 检查CHECK约束（MySQL 8.0+支持）
SELECT 
    constraint_name,
    check_clause,
    CASE 
        WHEN constraint_name LIKE '%age%' AND check_clause LIKE '%18%' AND check_clause LIKE '%35%' THEN 'PASS: Age constraint'
        WHEN constraint_name LIKE '%gender%' AND check_clause LIKE '%MALE%' AND check_clause LIKE '%FEMALE%' AND check_clause LIKE '%OTHER%' THEN 'PASS: Gender constraint'
        WHEN constraint_name LIKE '%relationship%' AND check_clause LIKE '%SINGLE%' AND check_clause LIKE '%IN_RELATIONSHIP%' THEN 'PASS: Relationship status constraint'
        WHEN constraint_name LIKE '%email%' AND check_clause LIKE '%@%' THEN 'PASS: Email format constraint'
        ELSE 'INFO: Other constraint'
    END AS constraint_check
FROM information_schema.check_constraints 
WHERE constraint_schema = DATABASE() 
  AND constraint_name LIKE '%users%'
ORDER BY constraint_name;

-- 验证唯一约束
SELECT 
    constraint_name,
    column_name,
    CASE 
        WHEN constraint_name LIKE '%email%' AND column_name = 'email' THEN 'PASS: Email unique constraint'
        WHEN constraint_name LIKE '%username%' AND column_name = 'username' THEN 'PASS: Username unique constraint'
        WHEN constraint_name = 'PRIMARY' AND column_name = 'id' THEN 'PASS: Primary key constraint'
        ELSE 'INFO: Other unique constraint'
    END AS unique_constraint_check
FROM information_schema.key_column_usage 
WHERE table_schema = DATABASE() 
  AND table_name = 'users'
  AND constraint_name IN (
      SELECT constraint_name 
      FROM information_schema.table_constraints 
      WHERE table_schema = DATABASE() 
        AND table_name = 'users' 
        AND constraint_type IN ('PRIMARY KEY', 'UNIQUE')
  )
ORDER BY constraint_name, ordinal_position;

-- 测试数据插入验证
SELECT 'Data Insertion Test' AS validation_section;

-- 测试有效数据插入
INSERT IGNORE INTO users (
    email, username, password, age, gender, relationship_status, location, enabled
) VALUES (
    'test.validation@example.com',
    'testvalidation',
    '$2a$10$test.hash.for.validation.only',
    25,
    'MALE',
    'SINGLE',
    'Test City',
    TRUE
);

-- 检查插入结果
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN 'PASS: Valid data insertion successful'
        ELSE 'FAIL: Valid data insertion failed'
    END AS insertion_test
FROM users 
WHERE email = 'test.validation@example.com';

-- 清理测试数据
DELETE FROM users WHERE email = 'test.validation@example.com';

-- 最终验证总结
SELECT 'Validation Summary' AS validation_section;

SELECT 
    COUNT(*) AS total_columns,
    COUNT(CASE WHEN is_nullable = 'NO' THEN 1 END) AS required_columns,
    COUNT(CASE WHEN is_nullable = 'YES' THEN 1 END) AS optional_columns
FROM information_schema.columns 
WHERE table_schema = DATABASE() AND table_name = 'users';

SELECT 
    COUNT(*) AS total_indexes,
    COUNT(CASE WHEN non_unique = 0 THEN 1 END) AS unique_indexes,
    COUNT(CASE WHEN non_unique = 1 THEN 1 END) AS non_unique_indexes
FROM information_schema.statistics 
WHERE table_schema = DATABASE() AND table_name = 'users';

SELECT 'Database structure validation completed' AS final_status;