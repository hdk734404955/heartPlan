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
 * 包含完整的验证注解确保数据有效性，简化版本去掉确认密码和简介字段
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
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度必须在6-50个字符之间")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{6,}$", 
             message = "密码必须包含至少一个大写字母、一个小写字母和一个数字")
    private String password;

    /**
     * 用户显示名称
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$", message = "用户名只能包含字母、数字、下划线和中文字符")
    private String username;

    /**
     * 用户年龄
     */
    @NotNull(message = "年龄不能为空")
    @Min(value = 18, message = "年龄必须大于等于18岁")
    @Max(value = 35, message = "年龄必须小于等于35岁")
    private Integer age;

    /**
     * 用户性别
     */
    @NotNull(message = "性别不能为空")
    private User.Gender gender;

    /**
     * 恋爱状态
     */
    @NotNull(message = "恋爱状态不能为空")
    private User.RelationshipStatus relationshipStatus;

    /**
     * 用户所在位置
     */
    @Size(max = 100, message = "位置信息长度不能超过100个字符")
    private String location;

    /**
     * 头像类型：RANDOM（随机生成）或 UPLOAD（用户上传）
     */
    @NotNull(message = "头像类型不能为空")
    private AvatarType avatarType;

    /**
     * 自定义头像文件名（当avatarType为UPLOAD时必填）
     */
    private String customAvatarFileName;

    /**
     * 头像类型枚举
     */
    public enum AvatarType {
        RANDOM("随机头像"),
        UPLOAD("自定义头像");

        private final String displayName;

        AvatarType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * 验证自定义头像文件名
     * 当头像类型为UPLOAD时，自定义头像文件名不能为空
     */
    @AssertTrue(message = "选择自定义头像时必须提供头像文件名")
    public boolean isCustomAvatarValid() {
        if (avatarType == AvatarType.UPLOAD) {
            return customAvatarFileName != null && !customAvatarFileName.trim().isEmpty();
        }
        return true;
    }
}