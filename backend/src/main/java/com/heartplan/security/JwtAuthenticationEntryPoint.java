package com.heartplan.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT认证入口点
 * 处理认证失败的情况，确保返回适当的CORS头部
 * 
 * @author HeartPlan Team
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String origin = request.getHeader("Origin");
        
        log.error("认证失败 - URI: {}, Method: {}, Origin: {}, 错误: {}", 
                 requestURI, method, origin, authException.getMessage());
        
        // 设置CORS响应头，确保前端能够接收到错误响应
        setCorsHeaders(response, origin);
        
        // 设置响应状态和内容类型
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        // 构建错误响应
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", "认证失败，请重新登录");
        errorResponse.put("errorCode", "AUTHENTICATION_FAILED");
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("path", requestURI);
        
        // 如果是CORS相关的错误，提供特殊处理
        if (isCorsRelatedError(request)) {
            errorResponse.put("message", "跨域请求认证失败，请检查CORS配置");
            errorResponse.put("errorCode", "CORS_AUTHENTICATION_FAILED");
            log.warn("检测到CORS相关的认证失败");
        }
        
        // 写入响应
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
    
    /**
     * 设置CORS响应头
     * 
     * @param response HTTP响应
     * @param origin 请求源
     */
    private void setCorsHeaders(HttpServletResponse response, String origin) {
        // 如果请求包含Origin头，设置相应的CORS头部
        if (origin != null && isAllowedOrigin(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH, HEAD");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With");
            response.setHeader("Access-Control-Max-Age", "3600");
        }
    }
    
    /**
     * 检查是否为允许的源
     * 
     * @param origin 请求源
     * @return 是否允许
     */
    private boolean isAllowedOrigin(String origin) {
        // 检查是否为开发环境的允许源
        return origin.startsWith("http://localhost:") ||
               origin.startsWith("http://127.0.0.1:") ||
               origin.startsWith("https://localhost:") ||
               origin.startsWith("https://127.0.0.1:") ||
               origin.startsWith("http://10.0.2.2:") ||
               origin.startsWith("http://192.168.") ||
               "capacitor://localhost".equals(origin) ||
               "ionic://localhost".equals(origin);
    }
    
    /**
     * 判断是否为CORS相关的错误
     * 
     * @param request HTTP请求
     * @return 是否为CORS相关错误
     */
    private boolean isCorsRelatedError(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        String accessControlRequestMethod = request.getHeader("Access-Control-Request-Method");
        
        // 如果请求包含Origin头或Access-Control-Request-Method头，可能是CORS相关
        return origin != null || accessControlRequestMethod != null;
    }
}