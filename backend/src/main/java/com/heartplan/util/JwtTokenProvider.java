package com.heartplan.util;

import com.heartplan.dto.UserInfoDTO;
import com.heartplan.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * JWT令牌提供者工具类
 * 使用Lombok简化代码，提供JWT令牌的生成、验证和解析功能
 * 
 * @author HeartPlan Team
 */
@Component
@Slf4j // Lombok注解：自动生成日志对象
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpirationMs;

    /**
     * 获取签名密钥
     * 
     * @return 签名密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * 生成访问令牌
     * 
     * @param authentication 认证信息
     * @return JWT访问令牌
     */
    public String generateAccessToken(Authentication authentication) {
        String username;
        Object principal = authentication.getPrincipal();
        
        // 处理不同类型的principal
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            // 假设是User实体，获取email作为用户名
            username = principal.toString();
        }
        
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationMs);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        log.debug("为用户 {} 生成访问令牌，过期时间: {}", username, expiryDate);
        return token;
    }

    /**
     * 生成刷新令牌
     * 
     * @param username 用户名
     * @return JWT刷新令牌
     */
    public String generateRefreshToken(String username) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtRefreshExpirationMs);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .claim("type", "refresh")
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        log.debug("为用户 {} 生成刷新令牌，过期时间: {}", username, expiryDate);
        return token;
    }

    /**
     * 从令牌中获取用户名
     * 
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * 验证令牌是否有效
     * 
     * @param token JWT令牌
     * @return 令牌是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException ex) {
            log.error("JWT令牌签名无效: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("JWT令牌格式错误: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("JWT令牌已过期: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("不支持的JWT令牌: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT令牌为空: {}", ex.getMessage());
        }
        return false;
    }

    /**
     * 检查令牌是否为刷新令牌
     * 
     * @param token JWT令牌
     * @return 是否为刷新令牌
     */
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return "refresh".equals(claims.get("type"));
        } catch (Exception ex) {
            log.error("解析令牌类型失败: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * 获取令牌过期时间
     * 
     * @param token JWT令牌
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    /**
     * 生成包含完整用户信息的访问令牌
     * 
     * @param user 用户实体对象
     * @return JWT访问令牌
     */
    public String generateAccessToken(User user) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationMs);
        
        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("username", user.getUsername())
                .claim("age", user.getAge())
                .claim("gender", user.getGender().toString())
                .claim("relationshipStatus", user.getRelationshipStatus().toString())
                .claim("avatarUrl", user.getAvatarUrl())
                .claim("location", user.getLocation())
                .claim("enabled", user.getEnabled())
                .claim("createdAt", user.getCreatedAt() != null ? 
                    user.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : null)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        log.debug("为用户 {} (ID: {}) 生成包含完整信息的访问令牌，过期时间: {}", 
                user.getUsername(), user.getId(), expiryDate);
        return token;
    }

    /**
     * 从JWT令牌中提取完整的用户详情信息
     * 
     * @param token JWT令牌
     * @return UserInfoDTO对象，包含完整的用户信息
     * @throws JwtException 当令牌解析失败时抛出异常
     */
    public UserInfoDTO getUserDetailsFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            // 处理创建时间
            LocalDateTime createdAt = null;
            Object createdAtClaim = claims.get("createdAt");
            if (createdAtClaim instanceof Long) {
                createdAt = LocalDateTime.ofInstant(
                    java.time.Instant.ofEpochMilli((Long) createdAtClaim), 
                    ZoneId.systemDefault()
                );
            }
            
            return UserInfoDTO.builder()
                    .id(Long.parseLong(claims.getSubject()))
                    .email(claims.get("email", String.class))
                    .username(claims.get("username", String.class))
                    .age(claims.get("age", Integer.class))
                    .gender(claims.get("gender") != null ? 
                        User.Gender.valueOf(claims.get("gender", String.class)) : null)
                    .relationshipStatus(claims.get("relationshipStatus") != null ? 
                        User.RelationshipStatus.valueOf(claims.get("relationshipStatus", String.class)) : null)
                    .avatarUrl(claims.get("avatarUrl", String.class))
                    .location(claims.get("location", String.class))
                    .enabled(claims.get("enabled", Boolean.class))
                    .createdAt(createdAt)
                    .build();
                    
        } catch (NumberFormatException e) {
            log.error("令牌中的用户ID格式无效: {}", e.getMessage());
            throw new JwtException("令牌中的用户ID格式无效", e);
        } catch (IllegalArgumentException e) {
            log.error("令牌中的枚举值无效: {}", e.getMessage());
            throw new JwtException("令牌中的枚举值无效", e);
        } catch (Exception e) {
            log.error("从令牌提取用户详情失败: {}", e.getMessage());
            throw new JwtException("从令牌提取用户详情失败", e);
        }
    }
}