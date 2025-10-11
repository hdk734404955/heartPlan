package com.heartplan.dto;

import com.heartplan.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户基本信息DTO
 * 纯净的数据传输对象，只包含前端需要的基本用户信息
 * 不包含敏感数据和认证相关逻辑
 * 
 * @author HeartPlan Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    
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
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    /**
     * 用户年龄
     */
    @NotNull(message = "Age cannot be null")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 35, message = "Age must be at most 35")
    private Integer age;

    /**
     * 用户性别
     */
    @NotNull(message = "Gender cannot be null")
    private User.Gender gender;

    /**
     * 恋爱状态
     */
    @NotNull(message = "Relationship status cannot be null")
    private User.RelationshipStatus relationshipStatus;

    /**
     * 头像URL
     */
    @Size(max = 500, message = "Avatar URL cannot exceed 500 characters")
    private String avatarUrl;

    private String bgcUrl;

    @Size(max = 500, message = "Introduction cannot exceed 500 characters")
    private String introduction;

    private String bgcMainColor;

    /**
     * 所在位置
     */
    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;

    /**
     * 账户是否启用
     */
    private Boolean enabled;

    /**
     * 注册时间
     */
    private LocalDateTime createdAt;
}