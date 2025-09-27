package com.heartplan.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 数据库配置类
 * 配置JPA、事务管理和数据库连接池
 * 
 * @author HeartPlan Team
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.heartplan.repository") // 启用JPA仓库，指定扫描包
@EnableTransactionManagement // 启用事务管理
@Slf4j // Lombok注解：自动生成日志对象
public class DatabaseConfig {

    /**
     * 数据库配置初始化
     * 在应用启动时记录数据库配置信息
     */
    public DatabaseConfig() {
        log.info("初始化数据库配置...");
        log.info("JPA仓库扫描包: com.heartplan.repository");
        log.info("事务管理已启用");
        log.info("数据库配置初始化完成");
    }
}