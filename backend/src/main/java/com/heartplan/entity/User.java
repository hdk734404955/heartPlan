package com.heartplan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

/**
 * 用户实体类
 * 使用Lombok注解简化代码，实现Spring Security的UserDetails接口
 * 包含用户的基本信息、认证信息和偏好设置
 * 
 * @author HeartPlan Team
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_email", columnList = "email", unique = true),
    @Index(name = "idx_user_username", columnList = "username", unique = true)
})
@Data // Lombok注解：自动生成getter、setter、toString、equals、hashCode方法
@Builder // Lombok注解：自动生成建造者模式
@NoArgsConstructor // Lombok注解：自动生成无参构造函数
@AllArgsConstructor // Lombok注解：自动生成全参构造函数
@EqualsAndHashCode(callSuper = true) // 继承父类的equals和hashCode
public class User extends BaseEntity implements UserDetails {

    /**
     * 用户邮箱（登录用户名）
     */
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * 用户显示名称
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50个字符之间")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 加密后的密码
     */
    @NotBlank(message = "密码不能为空")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * 用户年龄
     */
    @Min(value = 18, message = "年龄必须大于等于18岁")
    @Max(value = 35, message = "年龄必须小于等于35岁")
    @Column(name = "age", nullable = false)
    private Integer age;

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
     * 用户头像URL
     */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    /**
     * 用户所在位置
     */
    @Size(max = 100, message = "位置信息长度不能超过100个字符")
    @Column(name = "location", length = 100)
    private String location;

    /**
     * 用户生日
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * 账户是否启用
     */
    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    /**
     * 账户是否未过期
     */
    @Column(name = "account_non_expired", nullable = false)
    @Builder.Default
    private Boolean accountNonExpired = true;

    /**
     * 账户是否未锁定
     */
    @Column(name = "account_non_locked", nullable = false)
    @Builder.Default
    private Boolean accountNonLocked = true;

    /**
     * 凭证是否未过期
     */
    @Column(name = "credentials_non_expired", nullable = false)
    @Builder.Default
    private Boolean credentialsNonExpired = true;

    /**
     * 用户偏好设置（嵌入式对象）
     */
    @Embedded
    private UserPreferences preferences;

    /**
     * 隐私设置（嵌入式对象）
     */
    @Embedded
    private PrivacySettings privacySettings;

    /**
     * 用户角色（默认为USER）
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.USER;

    // Spring Security UserDetails接口实现

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email; // 使用邮箱作为用户名
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 性别枚举
     */
    public enum Gender {
        MALE("男性"),
        FEMALE("女性"),
        OTHER("其他");

        private final String displayName;

        Gender(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * 恋爱状态枚举
     */
    public enum RelationshipStatus {
        SINGLE("单身"),
        IN_RELATIONSHIP("恋爱中");

        private final String displayName;

        RelationshipStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * 用户角色枚举
     */
    public enum Role {
        USER("普通用户"),
        ADMIN("管理员");

        private final String displayName;

        Role(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}