package com.heartplan.security;

import com.heartplan.service.UserService;
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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 集成UserDetailsService，优先从JWT令牌获取完整用户信息
 * 失败时通过UserService加载用户详情
 * 
 * @author HeartPlan Team
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // 从请求中提取JWT令牌
            String jwt = getJwtFromRequest(request);
            
            // 如果存在有效的JWT令牌，设置认证信息
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                UserDetails userDetails = null;
                
                try {
                    // 优先从JWT令牌获取完整用户信息
                    userDetails = jwtTokenProvider.getUserDetailsFromToken(jwt);
                    log.debug("成功从JWT令牌获取用户详情，用户ID: {}", userDetails.getUsername());
                    
                } catch (Exception e) {
                    // 失败时使用UserService通过用户ID加载用户
                    log.debug("从JWT令牌获取用户详情失败，尝试通过UserService加载: {}", e.getMessage());
                    String userId = jwtTokenProvider.getUsernameFromToken(jwt);
                    userDetails = userService.loadUserByUsername(userId);
                    log.debug("成功通过UserService加载用户详情，用户ID: {}", userId);
                }
                
                // 创建包含UserDetails的Authentication对象
                if (userDetails != null && userDetails.isEnabled()) {
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                        );
                    
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    log.debug("成功设置用户认证信息到SecurityContext，用户ID: {}", userDetails.getUsername());
                }
            }
            
        } catch (Exception ex) {
            log.error("JWT认证失败: {}", ex.getMessage());
            SecurityContextHolder.clearContext();
        }
        
        // 继续过滤器链
        filterChain.doFilter(request, response);
    }
    
    /**
     * 从请求中提取JWT令牌
     * 简化的令牌提取逻辑
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }
}