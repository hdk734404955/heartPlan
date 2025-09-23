package com.heartplan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 注册响应DTO
 * 使用Lombok注解简化代码，用于返回用户注册成功后的响应数据
 * 包含JWT令牌和用户基本信息，直接进入主界面
 * 
 * @author HeartPlan Team
 */
@Data // Lombok注解：自动生成getter、setter、toString、equals、hashCode方法
@Builder // Lombok注解：自动生成建造者模式
@NoArgsConstructor // Lombok注解：自动生成无参构造函数
@AllArgsConstructor // Lombok注解：自动生成全参构造函数
public class RegisterResponse {

    /**
     * JWT访问令牌
     */
    private String accessToken;

    /**
     * JWT刷新令牌
     */
    private String refreshToken;

    /**
     * 令牌类型（通常为"Bearer"）
     */
    @Builder.Default
    private String tokenType = "Bearer";

    /**
     * 访问令牌过期时间（毫秒）
     */
    private Long expiresIn;

    /**
     * 用户基本信息
     */
    private LoginResponse.UserInfo user;

    /**
     * 注册时间
     */
    private LocalDateTime registerTime;

    /**
     * 注册成功消息
     */
    @Builder.Default
    private String message = "注册成功，欢迎使用HeartPlan！";

    /**
     * 是否为新用户（用于前端判断是否显示引导页面）
     */
    @Builder.Default
    private Boolean isNewUser = true;

    /**
     * 推荐的下一步操作
     */
    @Builder.Default
    private String nextAction = "开始探索AI智能模板功能";
}