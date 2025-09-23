package com.heartplan.dto;

import com.heartplan.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 登录响应DTO
 * 使用Lombok注解简化代码，用于返回用户登录成功后的响应数据
 * 包含JWT令牌和用户基本信息
 * 
 * @author HeartPlan Team
 */
@Data // Lombok注解：自动生成getter、setter、toString、equals、hashCode方法
@Builder // Lombok注解：自动生成建造者模式
@NoArgsConstructor // Lombok注解：自动生成无参构造函数
@AllArgsConstructor // Lombok注解：自动生成全参构造函数
public class LoginResponse {

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
    private UserInfo user;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 登录成功消息
     */
    @Builder.Default
    private String message = "登录成功";

    /**
     * 用户基本信息内部类
     * 只包含前端需要的基本用户信息，不包含敏感数据
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        
        /**
         * 用户ID
         */
        private Long id;

        /**
         * 用户邮箱
         */
        private String email;

        /**
         * 用户名
         */
        private String username;

        /**
         * 用户年龄
         */
        private Integer age;

        /**
         * 用户性别
         */
        private User.Gender gender;

        /**
         * 恋爱状态
         */
        private User.RelationshipStatus relationshipStatus;

        /**
         * 头像URL
         */
        private String avatarUrl;

        /**
         * 所在位置
         */
        private String location;

        /**
         * 用户角色
         */
        private User.Role role;

        /**
         * 账户是否启用
         */
        private Boolean enabled;

        /**
         * 注册时间
         */
        private LocalDateTime createdAt;

        /**
         * 最后登录时间
         */
        private LocalDateTime lastLoginAt;

        /**
         * 从User实体创建UserInfo对象的静态方法
         * 
         * @param user 用户实体
         * @return UserInfo对象
         */
        public static UserInfo fromUser(User user) {
            return UserInfo.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .age(user.getAge())
                    .gender(user.getGender())
                    .relationshipStatus(user.getRelationshipStatus())
                    .avatarUrl(user.getAvatarUrl())
                    .location(user.getLocation())
                    .role(user.getRole())
                    .enabled(user.getEnabled())
                    .createdAt(user.getCreatedAt())
                    .lastLoginAt(user.getLastLoginAt())
                    .build();
        }
    }
}