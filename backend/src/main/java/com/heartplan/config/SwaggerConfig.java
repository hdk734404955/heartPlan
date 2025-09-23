package com.heartplan.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 3.0 配置类
 * 用于生成API文档和提供在线API测试界面
 * 
 * @author HeartPlan Team
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置OpenAPI文档信息
     * 
     * @return OpenAPI配置对象
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HeartPlan API")
                        .description("HeartPlan AI恋爱规划应用后端API文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("HeartPlan Team")
                                .email("support@heartplan.com")
                                .url("https://heartplan.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", 
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT认证令牌")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}