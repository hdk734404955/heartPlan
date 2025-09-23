package com.heartplan.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
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
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationMs);

        String token = Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        log.debug("为用户 {} 生成访问令牌，过期时间: {}", userPrincipal.getUsername(), expiryDate);
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
}