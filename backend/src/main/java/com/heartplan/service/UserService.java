package com.heartplan.service;

import com.heartplan.dto.UserInfoDTO;
import com.heartplan.entity.User;
import com.heartplan.mapper.UserMapper;
import com.heartplan.repository.UserRepository;
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
 * 通过用户ID查找用户并返回UserInfoDTO
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
     * 根据用户名（用户ID）加载用户详情
     * Spring Security会调用此方法来获取用户信息进行认证
     * 
     * @param username 用户名（实际为用户ID的字符串形式）
     * @return UserDetails 用户详情对象
     * @throws UsernameNotFoundException 当用户不存在时抛出
     * @throws DisabledException 当用户账户被禁用时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("尝试加载用户详情，用户ID: {}", username);
        
        try {
            // 将用户名转换为用户ID
            Long userId = Long.parseLong(username);
            
            // 通过用户ID查找用户
            User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("用户不存在，用户ID: {}", userId);
                    return new UsernameNotFoundException("用户不存在: " + userId);
                });
            
            // 检查用户账户是否启用
            if (!user.getEnabled()) {
                log.warn("用户账户已禁用，用户ID: {}", userId);
                throw new DisabledException("用户账户已禁用: " + userId);
            }
            
            // 将User实体转换为UserInfoDTO
            UserInfoDTO userDetails = userMapper.toUserInfo(user);
            
            log.debug("成功加载用户详情，用户ID: {}, 用户名: {}", userId, user.getUsername());
            return userDetails;
            
        } catch (NumberFormatException e) {
            log.error("无效的用户ID格式: {}", username);
            throw new UsernameNotFoundException("无效的用户ID格式: " + username);
        } catch (Exception e) {
            log.error("加载用户详情时发生异常，用户ID: {}, 异常: {}", username, e.getMessage());
            throw new UsernameNotFoundException("加载用户详情失败: " + username, e);
        }
    }
}