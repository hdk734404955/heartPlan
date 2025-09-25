package com.heartplan.dto;

import com.heartplan.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册请求DTO
 * 使用Lombok注解简化代码，用于接收用户注册请求数据
 * 简化版本，只包含核心必要字段，所有验证消息使用英文
 * 
 * @author HeartPlan Team
 */
@Data // Lombok注解：自动生成getter、setter、toString、equals、hashCode方法
@Builder // Lombok注解：自动生成建造者模式
@NoArgsConstructor // Lombok注解：自动生成无参构造函数
@AllArgsConstructor // Lombok注解：自动生成全参构造函数
public class RegisterRequest {

    /**
     * 用户邮箱（登录用户名）
     */
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    /**
     * 用户密码
     */
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    /**
     * 用户显示名称
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
     * 用户所在位置（可选字段）
     */
    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;

    /**
     * 头像URL（可选字段）
     */
    @Size(max = 500, message = "Avatar URL cannot exceed 500 characters")
    private String avatarUrl;
}