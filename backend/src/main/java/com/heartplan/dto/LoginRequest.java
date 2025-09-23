package com.heartplan.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录请求DTO
 * 使用Lombok注解简化代码，用于接收用户登录请求数据
 * 包含必要的验证注解确保数据有效性
 * 
 * @author HeartPlan Team
 */
@Data // Lombok注解：自动生成getter、setter、toString、equals、hashCode方法
@Builder // Lombok注解：自动生成建造者模式
@NoArgsConstructor // Lombok注解：自动生成无参构造函数
@AllArgsConstructor // Lombok注解：自动生成全参构造函数
public class LoginRequest {

    /**
     * 用户邮箱（登录用户名）
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度必须在6-50个字符之间")
    private String password;

    /**
     * 是否记住登录状态
     * 可选字段，默认为false
     */
    @Builder.Default
    private Boolean rememberMe = false;
}