package com.heartplan.config;

import com.heartplan.security.JwtAuthenticationEntryPoint;
import com.heartplan.security.JwtAuthenticationFilter;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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
     * 优化CORS和OPTIONS预检请求处理，确保认证接口正确匹配
     * 
     * @param http HTTP安全配置
     * @return 安全过滤器链
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("配置安全过滤器链 - 优化CORS和OPTIONS预检请求处理");
        
        http
            // 禁用CSRF保护（因为使用JWT令牌）
            .csrf(AbstractHttpConfigurer::disable)
            
            // 禁用X-Frame-Options，允许嵌入iframe
            .headers(headers -> headers.frameOptions().disable())
            
            // 配置CORS - 必须在授权配置之前
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 配置会话管理为无状态（适用于JWT认证）
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 配置请求授权规则
            .authorizeHttpRequests(authz -> authz
                // 首先处理OPTIONS预检请求 - 必须放在最前面
                .requestMatchers("OPTIONS").permitAll()
                
                // 公开访问的认证相关端点（注意：context-path是/api，这里的路径相对于/api）
                .requestMatchers(
                    "/auth/**",               // 认证相关接口：登录、注册、刷新令牌等
                    "/auth/login",            // 明确指定登录接口
                    "/auth/register",         // 明确指定注册接口
                    "/auth/refresh",          // 明确指定令牌刷新接口
                    "/auth/logout"            // 明确指定登出接口
                ).permitAll()
                
                // 公开访问的其他端点
                .requestMatchers(
                    "/test/**",               // 测试接口
                    "/files/**",              // 文件上传下载接口
                    "/api-docs/**",           // API文档
                    "/swagger-ui/**",         // Swagger UI
                    "/swagger-ui.html",       // Swagger UI首页
                    "/v3/api-docs/**",        // OpenAPI文档
                    "/actuator/**",           // 监控端点
                    "/error",                 // 错误页面
                    "/favicon.ico"            // 网站图标
                ).permitAll()
                
                // 其他所有请求都需要认证
                .anyRequest().authenticated()
            )
            
            // 配置异常处理
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            
            // 添加JWT认证过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            // 禁用HTTP Basic认证
            .httpBasic(AbstractHttpConfigurer::disable)
            
            // 禁用表单登录
            .formLogin(AbstractHttpConfigurer::disable);

        log.info("安全过滤器链配置完成:");
        log.info("- OPTIONS预检请求：允许所有");
        log.info("- 认证接口(/auth/**)：允许匿名访问");
        log.info("- CORS配置：已启用并优化");
        log.info("- 会话管理：无状态模式");
        log.info("- JWT认证过滤器：已添加");
        log.info("- 认证异常处理：已配置");
        
        return http.build();
    }

    /**
     * 配置CORS（跨域资源共享）
     * 优化CORS配置以正确处理OPTIONS预检请求和跨域认证
     * 使用allowedOriginPatterns替代allowedOrigins解决通配符问题
     * 
     * @return CORS配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("配置CORS跨域设置 - 优化OPTIONS预检请求处理");
        
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 使用allowedOriginPatterns替代allowedOrigins，支持通配符模式
        // 这样可以避免allowCredentials=true与allowedOrigins="*"的冲突
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",       // 所有localhost端口（包括前端开发服务器5100）
            "http://127.0.0.1:*",       // 所有127.0.0.1端口
            "https://localhost:*",      // HTTPS localhost
            "https://127.0.0.1:*",      // HTTPS 127.0.0.1
            "http://10.0.2.2:*",        // Android模拟器访问宿主机
            "http://192.168.*.*:*",     // 局域网访问
            "capacitor://localhost",    // Capacitor应用
            "ionic://localhost"         // Ionic应用
        ));
        
        // 允许的HTTP方法 - 确保包含OPTIONS用于预检请求
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));
        
        // 允许的请求头 - 使用通配符允许所有头部
        configuration.setAllowedHeaders(List.of("*"));
        
        // 暴露的响应头 - 让前端能够访问这些头部
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",                    // JWT令牌
            "Content-Disposition",              // 文件下载
            "X-Total-Count",                   // 分页总数
            "Access-Control-Allow-Origin",      // CORS源
            "Access-Control-Allow-Credentials", // CORS凭证
            "Content-Type",                     // 内容类型
            "Content-Length"                    // 内容长度
        ));
        
        // 允许携带凭证（Cookie、Authorization头等）
        // 与allowedOriginPatterns配合使用，避免安全问题
        configuration.setAllowCredentials(true);
        
        // 预检请求的缓存时间（1小时）
        // 减少OPTIONS请求的频率，提高性能
        configuration.setMaxAge(3600L);
        
        // 应用CORS配置到所有路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        log.info("CORS配置完成:");
        log.info("- 使用allowedOriginPatterns支持通配符模式");
        log.info("- 允许携带凭证(allowCredentials=true)");
        log.info("- OPTIONS预检请求缓存时间: 3600秒");
        log.info("- 支持的HTTP方法: GET, POST, PUT, DELETE, OPTIONS, PATCH, HEAD");
        
        return source;
    }
}