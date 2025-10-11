package com.heartplan.service;

import com.heartplan.dto.UserInfoDTO;
import com.heartplan.entity.User;
import com.heartplan.mapper.UserMapper;
import com.heartplan.repository.UserRepository;
import com.heartplan.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import com.heartplan.util.ColorExtractionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 根据用户ID获取用户信息
     * 
     * @param userId 用户ID
     * @return UserInfoDTO 用户信息DTO
     * @throws UsernameNotFoundException 当用户不存在时抛出
     */
    public UserInfoDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + userId));
        
        return userMapper.toUserInfo(user);
    }

    /**
     * 更新用户信息
     * 使用UserInfoDTO作为更新请求DTO
     * 只更新允许修改的字段，不包括id、email、enabled、createdAt等字段
     * 
     * @param userId 用户ID
     * @param updateRequest 更新请求数据（忽略id、email、enabled、createdAt字段）
     * @return UserInfoDTO 更新后的用户信息
     * @throws UsernameNotFoundException 当用户不存在时抛出
     * @throws IllegalArgumentException 当用户名已被其他用户使用时抛出
     */
    @Transactional
    public UserInfoDTO updateUserInfo(Long userId, UserInfoDTO updateRequest) {
        // 查找要更新的用户
        User existingUser = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + userId));

        // 检查用户名是否被其他用户使用
        if (!existingUser.getUsername().equals(updateRequest.getUsername())) {
            boolean usernameExists = userRepository.findByUsername(updateRequest.getUsername())
                .filter(user -> !user.getId().equals(userId)) // 排除当前用户
                .isPresent();
            
            if (usernameExists) {
                throw new IllegalArgumentException("用户名已被使用: " + updateRequest.getUsername());
            }
        }

        // 检查背景图是否发生变化（需要特殊处理）
        String oldBgcUrl = existingUser.getBgcUrl();
        String newBgcUrl = updateRequest.getBgcUrl();
        boolean bgcUrlChanged = !java.util.Objects.equals(oldBgcUrl, newBgcUrl);
        
        if (bgcUrlChanged && newBgcUrl != null && !newBgcUrl.trim().isEmpty()) {
            log.info("用户背景图发生变化，开始提取主要颜色，用户ID: {}, 新URL: {}", 
                    existingUser.getId(), newBgcUrl);
            
            // 同步提取颜色，然后一起更新所有字段
            String dominantColor = ColorExtractionUtil.extractDominantColor(newBgcUrl);
            updateRequest.setBgcMainColor(dominantColor);
            log.info("背景图主要颜色提取成功，用户ID: {}, 颜色: {}", userId, dominantColor);
           
        } else if (bgcUrlChanged && (newBgcUrl == null || newBgcUrl.trim().isEmpty())) {
            // 如果背景图被清空，也清空主要颜色
            updateRequest.setBgcMainColor(null);
            log.info("背景图被清空，同时清空主要颜色，用户ID: {}", userId);
        }
        
        // 使用MapStruct更新所有字段（包括提取到的主要颜色）
        userMapper.updateUserFromUserInfoDTO(updateRequest, existingUser);
        
        // 保存更新后的用户
        User updatedUser = userRepository.save(existingUser);
        
        log.info("用户信息更新成功，用户ID: {}, 用户名: {}", userId, updatedUser.getUsername());
        
        // 返回更新后的用户信息
        return userMapper.toUserInfo(updatedUser);
    }

    /**
     * 检查用户名是否可用（排除指定用户ID）
     * 用于更新用户信息时检查用户名冲突
     * 
     * @param username 要检查的用户名
     * @param excludeUserId 要排除的用户ID（当前用户）
     * @return boolean 用户名是否可用
     */
    public boolean isUsernameAvailable(String username, Long excludeUserId) {
        return userRepository.findByUsername(username)
            .filter(user -> !user.getId().equals(excludeUserId))
            .isEmpty();
    }




}