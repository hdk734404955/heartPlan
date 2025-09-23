package com.heartplan.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security安全配置类
 * 使用Lombok注解简化代码，配置认证、授权和安全策略
 * 
 * @author HeartPlan Team
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 启用方法级安全注解
@Slf4j // Lombok注解：自动生成日志对象
@RequiredArgsConstructor // Lombok注解：为final字段生成构造函数
public class SecurityConfig {

    /**
     * 配置密码编码器
     * 使用BCrypt算法进行密码加密
     * 
     * @return BCrypt密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("配置BCrypt密码编码器");
        return new BCryptPasswordEncoder(12); // 使用强度为12的BCrypt
    }

    /**
     * 配置认证管理器
     * 
     * @param config 认证配置
     * @return 认证管理器
     * @throws Exception 配置异常
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("配置认证管理器");
        return config.getAuthenticationManager();
    }

    /**
     * 配置安全过滤器链
     * 
     * @param http HTTP安全配置
     * @return 安全过滤器链
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("配置安全过滤器链");
        
        http
            // 禁用CSRF保护（因为使用JWT令牌）
            .csrf(AbstractHttpConfigurer::disable)
            
            // 禁用X-Frame-Options
            .headers(headers -> headers.frameOptions().disable())
            
            // 配置CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 配置会话管理为无状态
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 配置请求授权
            .authorizeHttpRequests(authz -> authz
                // 公开访问的端点（注意：由于context-path是/api，所以这里的路径是相对于/api的）
                .requestMatchers(
                    "/auth/**",               // 认证相关接口 (实际路径: /api/auth/**)
                    "/test/**",               // 测试接口 (实际路径: /api/test/**)
                    "/files/**",              // 文件上传接口 (实际路径: /api/files/**)
                    "/api-docs/**",           // API文档
                    "/swagger-ui/**",         // Swagger UI
                    "/swagger-ui.html",       // Swagger UI首页
                    "/v3/api-docs/**",        // OpenAPI文档
                    "/actuator/**",           // 监控端点
                    "/error",                 // 错误页面
                    "/favicon.ico"            // 网站图标
                ).permitAll()
                
                // 允许OPTIONS请求（预检请求）
                .requestMatchers("OPTIONS", "/**").permitAll()
                
                // 其他所有请求都需要认证
                .anyRequest().authenticated()
            )
            
            // 禁用HTTP Basic认证
            .httpBasic(AbstractHttpConfigurer::disable)
            
            // 禁用表单登录
            .formLogin(AbstractHttpConfigurer::disable);

        log.info("安全过滤器链配置完成 - 已允许认证接口公开访问");
        return http.build();
    }

    /**
     * 配置CORS（跨域资源共享）
     * 
     * @return CORS配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("配置CORS跨域设置");
        
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 允许的源 - 支持多种开发环境
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5100",    // uni-app开发服务器
            "http://127.0.0.1:5100",    // uni-app本地IP变体
            "http://localhost:3000",    // React开发服务器
            "http://localhost:8081",    // React Native开发服务器
            "http://localhost:19006",   // Expo开发服务器
            "http://localhost:8080",    // Flutter Web开发服务器
            "http://localhost:9000",    // Flutter Web备用端口
            "http://127.0.0.1:3000",    // 本地IP变体
            "http://127.0.0.1:8080",    // 本地IP变体
            "http://127.0.0.1:8081",    // 本地IP变体
            "http://127.0.0.1:9000",    // 本地IP变体
            "http://10.0.2.2:8080",     // Android模拟器访问宿主机
            "http://192.168.1.1:8080",  // 局域网访问（根据实际网络调整）
            "capacitor://localhost",    // Capacitor应用
            "ionic://localhost",        // Ionic应用
            "http://localhost",         // 通用localhost
            "https://localhost"         // HTTPS localhost
        ));
        
        // 允许的HTTP方法
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));
        
        // 允许的请求头
        configuration.setAllowedHeaders(Arrays.asList(
            "*",
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // 暴露的响应头
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Disposition",
            "X-Total-Count"
        ));
        
        // 允许携带凭证
        configuration.setAllowCredentials(true);
        
        // 预检请求的缓存时间（1小时）
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        log.info("CORS配置完成 - 支持Flutter、React Native、Web等多种开发环境");
        return source;
    }
}