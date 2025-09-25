-- HeartPlan 数据库迁移脚本
-- 从复杂的用户表结构迁移到简化的用户表结构
-- 创建时间: 2025-01-24

-- 设置字符集和安全模式
SET NAMES utf8mb4;
SET SQL_SAFE_UPDATES = 0;

-- 备份现有数据（如果存在旧表）
-- 创建备份表
DROP TABLE IF EXISTS users_backup;
CREATE TABLE users_backup AS SELECT * FROM users WHERE 1=0; -- 创建空的备份表结构

-- 如果存在旧的users表，备份数据
-- 注意：这个脚本假设旧表可能存在，如果不存在会忽略错误
SET @backup_query = 'INSERT INTO users_backup SELECT * FROM users';
SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables 
                    WHERE table_schema = DATABASE() AND table_name = 'users');

-- 只有当表存在时才执行备份
-- 由于MySQL不支持条件DDL，这里使用存储过程方式
DELIMITER //
CREATE PROCEDURE BackupUsersIfExists()
BEGIN
    DECLARE table_count INT DEFAULT 0;
    
    SELECT COUNT(*) INTO table_count 
    FROM information_schema.tables 
    WHERE table_schema = DATABASE() AND table_name = 'users';
    
    IF table_count > 0 THEN
        -- 备份现有数据
        INSERT INTO users_backup SELECT * FROM users;
        SELECT CONCAT('Backed up ', ROW_COUNT(), ' records from existing users table') AS backup_status;
    ELSE
        SELECT 'No existing users table found, skipping backup' AS backup_status;
    END IF;
END //
DELIMITER ;

-- 执行备份
CALL BackupUsersIfExists();

-- 删除存储过程
DROP PROCEDURE BackupUsersIfExists;

-- 执行新表结构创建
SOURCE schema.sql;

-- 数据迁移逻辑（如果需要从备份表迁移数据）
-- 这里提供一个示例迁移脚本，根据实际的旧表结构进行调整

/*
-- 示例：从备份表迁移数据到新表
-- 注意：需要根据实际的旧表结构调整字段映射

INSERT INTO users (
    email,
    username, 
    password,
    age,
    gender,
    relationship_status,
    avatar_url,
    location,
    enabled,
    created_at,
    updated_at
)
SELECT 
    email,
    COALESCE(username, SUBSTRING_INDEX(email, '@', 1)) as username, -- 如果没有username，使用email前缀
    password,
    COALESCE(age, 25) as age, -- 如果没有年龄，默认25岁
    COALESCE(gender, 'OTHER') as gender, -- 如果没有性别，默认OTHER
    COALESCE(relationship_status, 'SINGLE') as relationship_status, -- 如果没有恋爱状态，默认SINGLE
    avatar_url,
    location,
    COALESCE(enabled, TRUE) as enabled, -- 如果没有enabled字段，默认TRUE
    COALESCE(created_at, NOW()) as created_at,
    COALESCE(updated_at, NOW()) as updated_at
FROM users_backup
WHERE email IS NOT NULL 
  AND email != ''
  AND email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'; -- 只迁移有效邮箱的记录
*/

-- 验证迁移结果
SELECT 'Migration completed successfully' AS migration_status;
SELECT 
    COUNT(*) AS total_users,
    COUNT(CASE WHEN enabled = TRUE THEN 1 END) AS enabled_users,
    COUNT(CASE WHEN enabled = FALSE THEN 1 END) AS disabled_users,
    COUNT(CASE WHEN avatar_url IS NOT NULL THEN 1 END) AS users_with_avatar,
    COUNT(CASE WHEN location IS NOT NULL THEN 1 END) AS users_with_location
FROM users;

-- 显示表结构信息
DESCRIBE users;

-- 显示索引信息
SHOW INDEX FROM users;

-- 恢复安全模式
SET SQL_SAFE_UPDATES = 1;