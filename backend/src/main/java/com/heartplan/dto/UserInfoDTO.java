package com.heartplan.dto;

import com.heartplan.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

/**
 * 用户基本信息DTO
 * 只包含前端需要的基本用户信息，不包含敏感数据
 * 独立的DTO类，提高复用性和可维护性
 * 实现UserDetails接口以支持Spring Security认证
 * 
 * @author HeartPlan Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO implements UserDetails {
    
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
     * 账户是否启用
     */
    private Boolean enabled;

    /**
     * 注册时间
     */
    private LocalDateTime createdAt;

    // ========== UserDetails接口实现 ==========

    /**
     * 获取用户权限集合
     * 由于dating项目只需要验证是否登录，不需要权限管理，返回空集合
     * 
     * @return 空的权限集合
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * 获取用户密码
     * UserInfoDTO不包含密码信息，返回null
     * 
     * @return null
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * 获取用户名
     * 使用用户ID作为Spring Security的用户名
     * 
     * @return 用户ID的字符串形式
     */
    @Override
    public String getUsername() {
        return id != null ? id.toString() : null;
    }

    /**
     * 账户是否未过期
     * dating项目不需要账户过期功能，始终返回true
     * 
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     * dating项目不需要账户锁定功能，始终返回true
     * 
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭证是否未过期
     * dating项目不需要凭证过期功能，始终返回true
     * 
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否启用
     * 返回用户实体中的enabled字段值，支持账户启用/禁用功能
     * 
     * @return 账户启用状态
     */
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }
}