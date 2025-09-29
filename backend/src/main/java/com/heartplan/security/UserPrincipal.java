package com.heartplan.security;

import com.heartplan.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 用户认证主体类
 * 实现UserDetails接口，专门用于Spring Security认证
 * 与UserInfoDTO分离，避免职责混乱
 * 
 * @author HeartPlan Team
 */
@Data
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    
    private Long id;
    private String email;
    private String username;
    private Boolean enabled;

    /**
     * 从User实体创建UserPrincipal
     * 
     * @param user 用户实体
     * @return UserPrincipal实例
     */
    public static UserPrincipal create(User user) {
        return new UserPrincipal(
            user.getId(),
            user.getEmail(),
            user.getUsername(),
            user.getEnabled()
        );
    }

    /**
     * 获取用户权限集合
     * Dating项目只需要验证是否登录，不需要权限管理
     * 
     * @return 空的权限集合
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * 获取用户密码
     * UserPrincipal不存储密码信息
     * 
     * @return null
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * 获取用户名
     * 返回邮箱作为Spring Security的认证标识符
     * 
     * @return 用户邮箱
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 账户是否未过期
     * 
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     * 
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭证是否未过期
     * 
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否启用
     * 
     * @return 账户启用状态
     */
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }
}