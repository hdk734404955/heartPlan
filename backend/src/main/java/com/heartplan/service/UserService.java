package com.heartplan.service;

import com.heartplan.entity.User;
import com.heartplan.mapper.UserMapper;
import com.heartplan.repository.UserRepository;
import com.heartplan.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

/**
 * 用户服务类
 * 实现UserDetailsService接口，为Spring Security提供用户详情加载功能
 * 
 * @author HeartPlan Team
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * 根据用户邮箱加载用户详情
     * Spring Security调用此方法获取用户信息进行认证
     * 
     * @param email 用户邮箱（认证标识符）
     * @return UserDetails 用户详情对象
     * @throws UsernameNotFoundException 当用户不存在时抛出
     * @throws DisabledException 当用户账户被禁用时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.trim().isEmpty()) {
            throw new UsernameNotFoundException("邮箱不能为空");
        }
        
        User user = userRepository.findByEmailAndEnabledTrue(email)
            .orElseThrow(() -> {
                boolean userExists = userRepository.findByEmail(email).isPresent();
                if (userExists) {
                    log.warn("用户账户已禁用: {}", email);
                    return new DisabledException("用户账户已禁用: " + email);
                } else {
                    log.warn("用户不存在: {}", email);
                    return new UsernameNotFoundException("用户不存在: " + email);
                }
            });
        
        return userMapper.toUserPrincipal(user);
    }
}