package com.heartplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * HeartPlan应用程序主启动类
 * 
 * @author HeartPlan Team
 */
@SpringBootApplication
@EnableJpaAuditing // 启用JPA审计功能，自动管理@CreatedDate和@LastModifiedDate字段
public class HeartPlanApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeartPlanApplication.class, args);
    }
}