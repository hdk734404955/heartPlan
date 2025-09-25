package com.heartplan.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartplan.dto.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 极简化JWT认证入口点
 * 处理认证失败的情况，返回统一的错误响应格式
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
        
        log.warn("认证失败 - URI: {}, 错误: {}", requestURI, authException.getMessage());
        
        // 设置响应状态和内容类型
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        // 使用统一的ApiResponse格式返回错误信息
        ApiResponse<Object> errorResponse = ApiResponse.error(401, "Authentication failed");
        
        // 写入响应
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}