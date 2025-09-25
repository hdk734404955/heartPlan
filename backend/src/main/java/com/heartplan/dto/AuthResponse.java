package com.heartplan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 认证响应基类
 * 包含JWT令牌相关的公共字段
 * 使用SuperBuilder支持继承的建造者模式
 * 
 * @author HeartPlan Team
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /**
     * JWT访问令牌
     */
    private String accessToken;

    /**
     * JWT刷新令牌
     */
    private String refreshToken;

    /**
     * 访问令牌过期时间（毫秒）
     */
    private Long expiresIn;

    /**
     * 用户基本信息
     */
    private UserInfoDTO user;
}