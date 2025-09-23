package com.heartplan.service;

import com.heartplan.dto.LoginRequest;
import com.heartplan.dto.LoginResponse;
import com.heartplan.dto.RegisterRequest;
import com.heartplan.dto.RegisterResponse;
import com.heartplan.entity.User;
import com.heartplan.entity.UserPreferences;
import com.heartplan.entity.PrivacySettings;
import com.heartplan.repository.UserRepository;
import com.heartplan.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 认证服务类
 * 使用Lombok注解简化代码，提供用户认证相关的业务逻辑
 * 包括登录验证、令牌生成和用户注册
 * 
 * @author HeartPlan Team
 */
@Service
@Slf4j // Lombok注解：自动生成日志对象
@RequiredArgsConstructor // Lombok注解：自动生成包含final字段的构造函数
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    /**
     * 用户登录方法
     * 验证用户凭据并生成JWT令牌
     * 
     * @param loginRequest 登录请求数据
     * @return 登录响应数据，包含JWT令牌和用户信息
     * @throws BadCredentialsException 当用户名或密码错误时抛出
     * @throws DisabledException 当用户账户被禁用时抛出
     */
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("用户尝试登录，邮箱: {}", loginRequest.getEmail());

        try {
            // 使用Spring Security进行身份验证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            // 获取认证成功的用户信息
            User user = (User) authentication.getPrincipal();
            log.info("用户登录成功，用户ID: {}, 用户名: {}", user.getId(), user.getUsername());

            // 更新用户最后登录时间
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);

            // 生成JWT令牌
            String accessToken = jwtTokenProvider.generateAccessToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

            // 构建登录响应
            LoginResponse response = LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(86400000L) // 24小时，与配置文件中的jwt.expiration保持一致
                    .user(LoginResponse.UserInfo.fromUser(user))
                    .loginTime(LocalDateTime.now())
                    .message("登录成功")
                    .build();

            log.info("为用户 {} 生成登录令牌成功", user.getEmail());
            return response;

        } catch (BadCredentialsException e) {
            log.warn("用户登录失败，邮箱: {}, 原因: 用户名或密码错误", loginRequest.getEmail());
            throw new BadCredentialsException("用户名或密码错误");
        } catch (DisabledException e) {
            log.warn("用户登录失败，邮箱: {}, 原因: 账户已被禁用", loginRequest.getEmail());
            throw new DisabledException("账户已被禁用，请联系管理员");
        } catch (AuthenticationException e) {
            log.error("用户登录失败，邮箱: {}, 原因: {}", loginRequest.getEmail(), e.getMessage());
            throw new BadCredentialsException("登录失败，请检查用户名和密码");
        }
    }

    /**
     * 刷新访问令牌
     * 使用刷新令牌生成新的访问令牌
     * 
     * @param refreshToken 刷新令牌
     * @return 新的登录响应数据
     * @throws BadCredentialsException 当刷新令牌无效时抛出
     */
    @Transactional(readOnly = true)
    public LoginResponse refreshToken(String refreshToken) {
        log.info("尝试刷新访问令牌");

        try {
            // 验证刷新令牌
            if (!jwtTokenProvider.validateToken(refreshToken) || !jwtTokenProvider.isRefreshToken(refreshToken)) {
                log.warn("刷新令牌无效或不是刷新令牌类型");
                throw new BadCredentialsException("无效的刷新令牌");
            }

            // 从刷新令牌中获取用户名
            String email = jwtTokenProvider.getUsernameFromToken(refreshToken);
            
            // 查找用户
            User user = userRepository.findByEmailAndEnabledTrue(email)
                    .orElseThrow(() -> {
                        log.warn("刷新令牌对应的用户不存在或已被禁用，邮箱: {}", email);
                        return new BadCredentialsException("用户不存在或已被禁用");
                    });

            // 创建新的认证对象
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());

            // 生成新的访问令牌
            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);

            // 构建响应
            LoginResponse response = LoginResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken) // 刷新令牌保持不变
                    .tokenType("Bearer")
                    .expiresIn(86400000L)
                    .user(LoginResponse.UserInfo.fromUser(user))
                    .loginTime(LocalDateTime.now())
                    .message("令牌刷新成功")
                    .build();

            log.info("为用户 {} 刷新访问令牌成功", email);
            return response;

        } catch (Exception e) {
            log.error("刷新访问令牌失败: {}", e.getMessage());
            throw new BadCredentialsException("刷新令牌失败");
        }
    }



    /**
     * 验证用户邮箱是否已存在
     * 用于注册时的邮箱重复检查
     * 
     * @param email 邮箱地址
     * @return 是否存在
     */
    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        boolean exists = userRepository.existsByEmail(email);
        log.debug("检查邮箱是否存在，邮箱: {}, 结果: {}", email, exists);
        return exists;
    }

    /**
     * 验证用户名是否已存在
     * 用于注册时的用户名重复检查
     * 
     * @param username 用户名
     * @return 是否存在
     */
    @Transactional(readOnly = true)
    public boolean isUsernameExists(String username) {
        boolean exists = userRepository.existsByUsername(username);
        log.debug("检查用户名是否存在，用户名: {}, 结果: {}", username, exists);
        return exists;
    }

    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱地址
     * @return 用户对象（可能为空）
     */
    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        log.debug("根据邮箱查找用户: {}", email);
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户对象（可能为空）
     */
    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        log.debug("根据用户名查找用户: {}", username);
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * 用户注册方法
     * 创建新用户账户并返回JWT令牌，直接进入主界面
     * 
     * @param registerRequest 注册请求数据
     * @return 注册响应数据，包含JWT令牌和用户信息
     * @throws IllegalArgumentException 当邮箱或用户名已存在时抛出
     */
    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        log.info("用户尝试注册，邮箱: {}, 用户名: {}", registerRequest.getEmail(), registerRequest.getUsername());

        // 验证邮箱是否已存在（查询真实数据库）
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("注册失败，邮箱已存在: {}", registerRequest.getEmail());
            throw new IllegalArgumentException("邮箱已被注册，请使用其他邮箱");
        }

        // 验证用户名是否已存在（查询真实数据库）
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("注册失败，用户名已存在: {}", registerRequest.getUsername());
            throw new IllegalArgumentException("用户名已被使用，请选择其他用户名");
        }

        // 验证年龄范围
        if (registerRequest.getAge() < 18 || registerRequest.getAge() > 35) {
            log.warn("注册失败，年龄不在允许范围内: {}", registerRequest.getAge());
            throw new IllegalArgumentException("年龄必须在18-35岁之间");
        }

        try {
            // 处理头像URL
            String avatarUrl = processAvatarUrl(registerRequest);

            // 创建用户偏好设置（使用默认值）
            UserPreferences preferences = UserPreferences.builder()
                    .language(UserPreferences.Language.ENGLISH)
                    .theme(UserPreferences.Theme.LIGHT)
                    .emailNotifications(true)
                    .pushNotifications(true)
                    .communityNotifications(true)
                    .chatNotifications(true)
                    .aiCoachNotifications(true)
                    .soundEnabled(true)
                    .vibrationEnabled(true)
                    .recommendationType(UserPreferences.RecommendationType.BALANCED)
                    .showOnlineStatus(true)
                    .autoSaveChat(true)
                    .build();

            // 创建隐私设置（使用默认值）
            PrivacySettings privacySettings = PrivacySettings.builder()
                    .profileVisibility(PrivacySettings.ProfileVisibility.PUBLIC)
                    .onlineStatusVisibility(PrivacySettings.OnlineStatusVisibility.FRIENDS)
                    .allowStrangerMessages(true)
                    .searchable(true)
                    .showLastActive(true)
                    .showReadReceipts(true)
                    .allowAnalytics(true)
                    .allowPersonalization(true)
                    .allowThirdPartySharing(false)
                    .dataRetentionDays(365)
                    .twoFactorEnabled(false)
                    .rememberDevices(true)
                    .build();

            // 根据年龄计算生日（简化处理，假设今年生日已过）
            LocalDate birthDate = LocalDate.now().minusYears(registerRequest.getAge());

            // 创建用户实体
            User newUser = User.builder()
                    .email(registerRequest.getEmail())
                    .username(registerRequest.getUsername())
                    .password(passwordEncoder.encode(registerRequest.getPassword())) // 密码加密存储
                    .age(registerRequest.getAge())
                    .gender(registerRequest.getGender())
                    .relationshipStatus(registerRequest.getRelationshipStatus())
                    .location(registerRequest.getLocation())
                    .avatarUrl(avatarUrl)
                    .birthDate(birthDate)
                    .lastLoginAt(LocalDateTime.now())
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .preferences(preferences)
                    .privacySettings(privacySettings)
                    .role(User.Role.USER)
                    .build();

            // 保存用户到数据库
            User savedUser = userRepository.save(newUser);
            log.info("用户注册成功，用户ID: {}, 邮箱: {}, 用户名: {}", 
                    savedUser.getId(), savedUser.getEmail(), savedUser.getUsername());

            // 创建认证对象用于生成JWT令牌
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser, null, savedUser.getAuthorities());

            // 生成JWT令牌
            String accessToken = jwtTokenProvider.generateAccessToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.getEmail());

            // 构建注册响应
            RegisterResponse response = RegisterResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(86400000L) // 24小时
                    .user(LoginResponse.UserInfo.fromUser(savedUser))
                    .registerTime(LocalDateTime.now())
                    .message("注册成功，欢迎使用HeartPlan！")
                    .isNewUser(true)
                    .nextAction("开始探索AI智能模板功能")
                    .build();

            log.info("为新用户 {} 生成注册令牌成功", savedUser.getEmail());
            return response;

        } catch (Exception e) {
            log.error("用户注册过程中发生错误，邮箱: {}, 错误: {}", registerRequest.getEmail(), e.getMessage(), e);
            throw new RuntimeException("注册失败，请稍后重试", e);
        }
    }

    /**
     * 处理头像URL
     * 根据用户选择的头像类型生成相应的头像URL
     * 
     * @param registerRequest 注册请求
     * @return 头像URL
     */
    private String processAvatarUrl(RegisterRequest registerRequest) {
        String avatarUrl;
        
        if (registerRequest.getAvatarType() == RegisterRequest.AvatarType.RANDOM) {
            // 生成随机头像
            avatarUrl = fileService.generateRandomAvatarUrl();
            log.debug("为用户 {} 生成随机头像: {}", registerRequest.getEmail(), avatarUrl);
        } else if (registerRequest.getAvatarType() == RegisterRequest.AvatarType.UPLOAD) {
            // 使用用户上传的自定义头像
            if (registerRequest.getCustomAvatarFileName() == null || registerRequest.getCustomAvatarFileName().trim().isEmpty()) {
                log.warn("用户选择自定义头像但未提供文件名，使用随机头像: {}", registerRequest.getEmail());
                avatarUrl = fileService.generateRandomAvatarUrl();
            } else {
                avatarUrl = fileService.getAvatarUrlByFileName(registerRequest.getCustomAvatarFileName());
                log.debug("为用户 {} 使用自定义头像: {}", registerRequest.getEmail(), avatarUrl);
            }
        } else {
            // 默认使用随机头像
            avatarUrl = fileService.generateRandomAvatarUrl();
            log.debug("为用户 {} 使用默认随机头像: {}", registerRequest.getEmail(), avatarUrl);
        }
        
        return avatarUrl;
    }
}