package com.heartplan.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

/**
 * CORS跨域配置类
 * 专门处理跨域资源共享配置，支持Flutter、React Native、Web等多种开发环境
 * 
 * @author HeartPlan Team
 */
@Configuration
@Slf4j
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 全局CORS配置
     * 通过WebMvcConfigurer接口配置跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("配置全局CORS映射");
        
        registry.addMapping("/**")
                // 允许的源
                .allowedOriginPatterns(
                    "http://localhost:*",      // 所有localhost端口
                    "http://127.0.0.1:*",     // 所有127.0.0.1端口
                    "http://10.0.2.2:*",      // Android模拟器访问宿主机
                    "http://192.168.*.*:*",   // 局域网访问
                    "capacitor://localhost",   // Capacitor应用
                    "ionic://localhost",       // Ionic应用
                    "https://localhost:*",     // HTTPS localhost
                    "https://127.0.0.1:*"     // HTTPS 127.0.0.1
                )
                // 允许的HTTP方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                // 允许的请求头
                .allowedHeaders("*")
                // 暴露的响应头
                .exposedHeaders(
                    "Authorization",
                    "Content-Disposition",
                    "X-Total-Count",
                    "Access-Control-Allow-Origin",
                    "Access-Control-Allow-Credentials"
                )
                // 允许携带凭证
                .allowCredentials(true)
                // 预检请求缓存时间
                .maxAge(3600);
        
        log.info("全局CORS映射配置完成");
    }

    /**
     * CORS过滤器Bean
     * 提供更细粒度的CORS控制
     */
    @Bean
    public CorsFilter corsFilter() {
        log.info("创建CORS过滤器");
        
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许所有源（开发环境）
        config.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",
            "http://127.0.0.1:*", 
            "http://10.0.2.2:*",
            "http://192.168.*.*:*",
            "capacitor://localhost",
            "ionic://localhost",
            "https://localhost:*",
            "https://127.0.0.1:*"
        ));
        
        // 允许的HTTP方法
        config.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));
        
        // 允许的请求头
        config.setAllowedHeaders(Collections.singletonList("*"));
        
        // 暴露的响应头
        config.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Disposition", 
            "X-Total-Count",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        
        // 允许携带凭证
        config.setAllowCredentials(true);
        
        // 预检请求缓存时间
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        log.info("CORS过滤器创建完成");
        return new CorsFilter(source);
    }
}