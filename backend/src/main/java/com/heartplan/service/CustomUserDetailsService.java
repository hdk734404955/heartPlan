package com.heartplan.service;

import com.heartplan.entity.User;
import com.heartplan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 自定义用户详情服务类
 * 使用Lombok注解简化代码，实现Spring Security的UserDetailsService接口
 * 负责根据用户名加载用户详情，与AuthService分离以避免循环依赖
 * 
 * @author HeartPlan Team
 */
@Service
@Slf4j // Lombok注解：自动生成日志对象
@RequiredArgsConstructor // Lombok注解：自动生成包含final字段的构造函数
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 实现UserDetailsService接口的方法
     * 根据用户名（邮箱）加载用户详情
     * 
     * @param username 用户名（实际为邮箱）
     * @return UserDetails对象
     * @throws UsernameNotFoundException 当用户不存在时抛出
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("加载用户详情，用户名: {}", username);

        User user = userRepository.findByEmailAndEnabledTrue(username)
                .orElseThrow(() -> {
                    log.warn("用户不存在或已被禁用，邮箱: {}", username);
                    return new UsernameNotFoundException("用户不存在或已被禁用: " + username);
                });

        log.debug("成功加载用户详情，用户ID: {}, 用户名: {}", user.getId(), user.getUsername());
        return user;
    }
}