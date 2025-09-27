package com.heartplan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * 极简化的用户实体类
 * 继承BaseEntity获得通用字段（id、创建时间、更新时间）
 * 使用Lombok注解简化代码
 * 只包含核心的用户信息字段
 * 
 * @author HeartPlan Team
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_email", columnList = "email", unique = true),
    @Index(name = "idx_user_username", columnList = "username", unique = true)
})
@Getter // Lombok注解：自动生成getter方法
@Setter // Lombok注解：自动生成setter方法
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true) // 不包含父类，只包含明确指定的字段
@ToString(onlyExplicitlyIncluded = true) // 只包含明确指定的字段，避免循环引用
@Builder // Lombok注解：自动生成建造者模式
@NoArgsConstructor // Lombok注解：自动生成无参构造函数
@AllArgsConstructor // Lombok注解：自动生成全参构造函数
public class User extends BaseEntity {

    // 核心用户字段

    /**
     * 用户邮箱（登录用户名）
     */
    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true, length = 100)
    @EqualsAndHashCode.Include // 明确包含在equals和hashCode中
    @ToString.Include // 明确包含在toString中
    private String email;

    /**
     * 用户显示名称
     */
    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 加密后的密码
     */
    @NotBlank
    @Size(min = 6)
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * 用户年龄
     */
    @Min(18)
    @Max(35)
    @Column(name = "age", nullable = false)
    private Integer age;

    // 枚举字段和可选字段

    /**
     * 用户性别
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

    /**
     * 恋爱状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_status", nullable = false, length = 20)
    private RelationshipStatus relationshipStatus;

    /**
     * 用户头像URL（可选字段）
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    /**
     * 用户所在位置（可选字段）
     */
    @Size(max = 100)
    @Column(name = "location", length = 100)
    private String location;

    /**
     * 账户是否启用
     */
    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    /**
     * 简化的性别枚举
     */
    public enum Gender {
        MALE, FEMALE, OTHER
    }

    /**
     * 简化的恋爱状态枚举
     */
    public enum RelationshipStatus {
        SINGLE, IN_RELATIONSHIP
    }
}