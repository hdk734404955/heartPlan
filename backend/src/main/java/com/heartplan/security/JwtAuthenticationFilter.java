package com.heartplan.security;

import com.heartplan.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 处理JWT令牌验证，确保与CORS配置正确配合
 * 
 * @author HeartPlan Team
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 记录请求信息用于调试
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String origin = request.getHeader("Origin");
        
        log.debug("JWT过滤器处理请求: {} {}, Origin: {}", method, requestURI, origin);
        
        // 对于OPTIONS预检请求，直接放行，不进行JWT验证
        if ("OPTIONS".equalsIgnoreCase(method)) {
            log.debug("OPTIONS预检请求，跳过JWT验证");
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // 从请求中提取JWT令牌
            String jwt = getJwtFromRequest(request);
            
            // 如果存在有效的JWT令牌，设置认证信息
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                String username = jwtTokenProvider.getUsernameFromToken(jwt);
                
                // 加载用户详情
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // 创建认证令牌
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities()
                    );
                
                // 设置认证详情
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 设置安全上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("用户 {} 通过JWT认证成功", username);
            } else if (StringUtils.hasText(jwt)) {
                log.debug("JWT令牌无效或已过期");
            }
            
        } catch (Exception ex) {
            log.error("JWT认证过程中发生错误: {}", ex.getMessage());
            // 清除安全上下文
            SecurityContextHolder.clearContext();
        }
        
        // 继续过滤器链
        filterChain.doFilter(request, response);
    }
    
    /**
     * 从请求中提取JWT令牌
     * 
     * @param request HTTP请求
     * @return JWT令牌，如果不存在则返回null
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            log.debug("从Authorization头中提取JWT令牌");
            return token;
        }
        
        return null;
    }
    
    /**
     * 判断是否应该跳过此过滤器
     * 对于公开端点，可以跳过JWT验证
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // 对于认证相关的端点，跳过JWT验证
        boolean shouldSkip = path.startsWith("/api/auth/") || 
                           path.startsWith("/api/test/") ||
                           path.equals("/api/auth/login") ||
                           path.equals("/api/auth/register") ||
                           path.equals("/api/auth/refresh");
        
        if (shouldSkip) {
            log.debug("跳过JWT验证的路径: {}", path);
        }
        
        return shouldSkip;
    }
}