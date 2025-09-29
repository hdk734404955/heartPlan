package com.heartplan.util;

import com.heartplan.entity.User;
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
 * 提供JWT令牌的生成、验证和解析功能，支持访问令牌和刷新令牌
 * 
 * @author HeartPlan Team
 */
@Component
@Slf4j
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
        
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        } else {
            username = principal.toString();
        }
        
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 生成刷新令牌
     * 
     * @param username 用户邮箱（认证标识符）
     * @return JWT刷新令牌
     */
    public String generateRefreshToken(String username) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtRefreshExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .claim("type", "refresh")
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从令牌中获取用户邮箱
     * 
     * @param token JWT令牌
     * @return 用户邮箱（认证标识符）
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
            log.warn("JWT令牌签名无效");
        } catch (MalformedJwtException ex) {
            log.warn("JWT令牌格式错误");
        } catch (ExpiredJwtException ex) {
            log.info("JWT令牌已过期");
        } catch (UnsupportedJwtException ex) {
            log.warn("不支持的JWT令牌");
        } catch (IllegalArgumentException ex) {
            log.warn("JWT令牌为空或无效");
        } catch (Exception ex) {
            log.error("JWT令牌验证失败: {}", ex.getMessage());
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
            
            Object tokenType = claims.get("type");
            return "refresh".equals(tokenType);
        } catch (ExpiredJwtException ex) {
            log.info("令牌已过期");
            return false;
        } catch (JwtException ex) {
            log.warn("解析令牌失败");
            return false;
        } catch (Exception ex) {
            log.error("检查令牌类型失败: {}", ex.getMessage());
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
     * 生成访问令牌（基于用户实体）
     * 
     * @param user 用户实体对象
     * @return JWT访问令牌
     */
    public String generateAccessToken(User user) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationMs);
        
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


}