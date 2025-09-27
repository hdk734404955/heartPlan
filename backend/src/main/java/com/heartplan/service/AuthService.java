package com.heartplan.service;

import com.heartplan.dto.AuthResponse;
import com.heartplan.dto.LoginRequest;
import com.heartplan.dto.RegisterRequest;
import com.heartplan.entity.User;
import com.heartplan.mapper.UserMapper;
import com.heartplan.repository.UserRepository;

import com.heartplan.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 简化的认证服务类
 * 提供用户认证相关的核心业务逻辑
 * 包括登录验证、令牌生成和用户注册
 * 
 * @author HeartPlan Team
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

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
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("User attempting login, email: {}", loginRequest.getEmail());

        try {
            // 查找用户
            User user = userRepository.findByEmailAndEnabledTrue(loginRequest.getEmail())
                    .orElseThrow(() -> {
                        log.warn("User login failed, email: {}, reason: User not found or disabled", loginRequest.getEmail());
                        return new BadCredentialsException("Invalid email or password");
                    });

            // 验证密码
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                log.warn("User login failed, email: {}, reason: Invalid password", loginRequest.getEmail());
                throw new BadCredentialsException("Invalid email or password");
            }

            log.info("User login successful, ID: {}, Username: {}", user.getId(), user.getUsername());

            // 生成JWT令牌，使用新的generateAccessToken(User)方法
            String accessToken = jwtTokenProvider.generateAccessToken(user);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

            // 构建登录响应，使用MapStruct简化构建过程
            AuthResponse response = userMapper.toAuthResponse(
                    accessToken, refreshToken, userMapper.toUserInfo(user));

            log.info("Generated login token for user: {}", user.getEmail());
            return response;

        } catch (BadCredentialsException e) {
            throw e; // 重新抛出已处理的异常
        } catch (Exception e) {
            log.error("User login failed, email: {}, reason: {}", loginRequest.getEmail(), e.getMessage());
            throw new BadCredentialsException("Login failed, please check your credentials");
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
    public AuthResponse refreshToken(String refreshToken) {
        log.info("Attempting to refresh access token");

        try {
            // 验证刷新令牌
            if (!jwtTokenProvider.validateToken(refreshToken) || !jwtTokenProvider.isRefreshToken(refreshToken)) {
                log.warn("Invalid refresh token or not a refresh token type");
                throw new BadCredentialsException("Invalid refresh token");
            }

            // 从刷新令牌中获取用户名
            String email = jwtTokenProvider.getUsernameFromToken(refreshToken);
            
            // 查找用户
            User user = userRepository.findByEmailAndEnabledTrue(email)
                    .orElseThrow(() -> {
                        log.warn("User not found or disabled for refresh token, email: {}", email);
                        return new BadCredentialsException("User not found or disabled");
                    });

            // 生成新的访问令牌，使用新的generateAccessToken(User)方法
            String newAccessToken = jwtTokenProvider.generateAccessToken(user);

            // 构建刷新令牌响应，使用MapStruct简化构建过程
            AuthResponse response = userMapper.toAuthResponse(
                    newAccessToken, refreshToken, userMapper.toUserInfo(user));

            log.info("Successfully refreshed access token for user: {}", email);
            return response;

        } catch (Exception e) {
            log.error("Failed to refresh access token: {}", e.getMessage());
            throw new BadCredentialsException("Token refresh failed");
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
        log.debug("Checking if email exists: {}, result: {}", email, exists);
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
        log.debug("Checking if username exists: {}, result: {}", username, exists);
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
        log.debug("Finding user by email: {}", email);
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
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * 用户注册方法
     * 创建新用户账户并返回JWT令牌，适应简化后的User实体
     * 
     * @param registerRequest 注册请求数据
     * @return 注册响应数据，包含JWT令牌和用户信息
     * @throws IllegalArgumentException 当邮箱或用户名已存在时抛出
     */
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        log.info("User attempting registration, email: {}, username: {}", 
                registerRequest.getEmail(), registerRequest.getUsername());

        // 验证邮箱是否已存在
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Registration failed, email already exists: {}", registerRequest.getEmail());
            throw new IllegalArgumentException("Email is already registered, please use another email");
        }

        // 验证用户名是否已存在
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("Registration failed, username already exists: {}", registerRequest.getUsername());
            throw new IllegalArgumentException("Username is already taken, please choose another username");
        }

        // 验证年龄范围
        if (registerRequest.getAge() < 18 || registerRequest.getAge() > 35) {
            log.warn("Registration failed, age out of range: {}", registerRequest.getAge());
            throw new IllegalArgumentException("Age must be between 18 and 35 years old");
        }

        try {
            // 使用MapStruct将注册请求转换为User实体
            User newUser = userMapper.toEntity(registerRequest);
            // 单独处理密码加密（MapStruct映射中忽略了password字段）
            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

            // 保存用户到数据库
            User savedUser = userRepository.save(newUser);
            log.info("User registration successful, ID: {}, email: {}, username: {}", 
                    savedUser.getId(), savedUser.getEmail(), savedUser.getUsername());

            // 生成JWT令牌，使用新的generateAccessToken(User)方法
            String accessToken = jwtTokenProvider.generateAccessToken(savedUser);
            String refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.getEmail());

            // 构建注册响应，使用MapStruct简化构建过程
            AuthResponse response = userMapper.toAuthResponse(
                    accessToken, refreshToken, userMapper.toUserInfo(savedUser));

            log.info("Generated registration token for new user: {}", savedUser.getEmail());
            return response;

        } catch (Exception e) {
            log.error("Error occurred during user registration, email: {}, error: {}", 
                    registerRequest.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Registration failed, please try again later", e);
        }
    }


}