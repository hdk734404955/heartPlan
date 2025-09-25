package com.heartplan.controller;

import com.heartplan.dto.AuthResponse;
import com.heartplan.dto.LoginRequest;
import com.heartplan.dto.RegisterRequest;
import com.heartplan.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 使用Lombok注解简化代码，提供用户认证相关的REST API接口
 * 包括登录、注册、令牌刷新和邮箱/用户名检查等功能
 * 
 * @author HeartPlan Team
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600) // 允许所有源的跨域请求
@Slf4j // Lombok注解：自动生成日志对象
@RequiredArgsConstructor // Lombok注解：自动生成包含final字段的构造函数
@Tag(name = "Authentication Management", description = "User authentication related API endpoints")
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录接口
     * 验证用户凭据并返回JWT令牌
     * 
     * @param loginRequest 登录请求数据
     * @return 登录响应数据，由GlobalResponseHandler自动包装为统一ApiResponse格式
     */
    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Validate user email and password, return JWT access token and refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "401", description = "Invalid username or password"),
        @ApiResponse(responseCode = "403", description = "Account disabled"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public AuthResponse login(
            @Parameter(description = "Login request data", required = true)
            @Valid @RequestBody LoginRequest loginRequest) {
        
        log.info("收到登录请求，邮箱: {}", loginRequest.getEmail());

        // 调用认证服务进行登录
        AuthResponse response = authService.login(loginRequest);
        
        log.info("用户登录成功，邮箱: {}, 用户ID: {}", 
                loginRequest.getEmail(), response.getUser().getId());
        
        return response;
    }

    /**
     * 刷新访问令牌接口
     * 使用刷新令牌获取新的访问令牌
     * 
     * @param refreshTokenRequest 包含刷新令牌的请求
     * @return 新的登录响应数据，由GlobalResponseHandler自动包装为统一ApiResponse格式
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh Access Token", description = "Use refresh token to get new access token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refresh successful"),
        @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
        @ApiResponse(responseCode = "401", description = "Refresh token expired"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public AuthResponse refreshToken(
            @Parameter(description = "Refresh token request", required = true)
            @RequestBody Map<String, String> refreshTokenRequest) {
        
        String refreshToken = refreshTokenRequest.get("refreshToken");
        
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            log.warn("刷新令牌请求缺少refreshToken参数");
            throw new IllegalArgumentException("Refresh token cannot be empty");
        }

        log.info("收到刷新令牌请求");

        // 调用认证服务刷新令牌
        AuthResponse response = authService.refreshToken(refreshToken);
        
        log.info("令牌刷新成功，用户: {}", response.getUser().getEmail());
        
        return response;
    }

    /**
     * 检查邮箱是否已存在
     * 用于注册时的邮箱重复检查
     * 
     * @param email 邮箱地址
     * @return 检查结果，由GlobalResponseHandler自动包装为统一ApiResponse格式
     */
    @GetMapping("/check-email")
    @Operation(summary = "Check Email Existence", description = "Check if the specified email has been registered")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed"),
        @ApiResponse(responseCode = "400", description = "Invalid email format")
    })
    public Map<String, Object> checkEmail(
            @Parameter(description = "Email address to check", required = true)
            @RequestParam String email) {
        
        log.info("检查邮箱是否存在: {}", email);

        // 参数验证 - 异常将由GlobalExceptionHandler统一处理
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // 调用服务层检查邮箱
        boolean exists = authService.isEmailExists(email);
        
        // 返回简单数据结构，由GlobalResponseHandler自动包装为统一格式
        Map<String, Object> result = new HashMap<>();
        result.put("exists", exists);
        result.put("email", email);
        
        log.info("邮箱检查完成: {}, 结果: {}", email, exists);
        return result;
    }

    /**
     * 检查用户名是否已存在
     * 用于注册时的用户名重复检查
     * 
     * @param username 用户名
     * @return 检查结果，由GlobalResponseHandler自动包装为统一ApiResponse格式
     */
    @GetMapping("/check-username")
    @Operation(summary = "Check Username Existence", description = "Check if the specified username has been used")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed"),
        @ApiResponse(responseCode = "400", description = "Invalid username format")
    })
    public Map<String, Object> checkUsername(
            @Parameter(description = "Username to check", required = true)
            @RequestParam String username) {
        
        log.debug("检查用户名是否存在: {}", username);

        // 参数验证 - 异常将由GlobalExceptionHandler统一处理
        if (username == null || username.trim().length() < 2 || username.trim().length() > 50) {
            throw new IllegalArgumentException("Username length must be between 2-50 characters");
        }

        boolean exists = authService.isUsernameExists(username.trim());
        
        // 返回简单数据结构，由GlobalResponseHandler自动包装为统一格式
        Map<String, Object> result = new HashMap<>();
        result.put("exists", exists);
        result.put("username", username.trim());
        
        return result;
    }

    /**
     * 用户注册接口
     * 统一注册接口，完成用户账户创建并返回JWT令牌，直接进入主界面
     * 
     * @param registerRequest 注册请求数据
     * @return 注册响应数据，由GlobalResponseHandler自动包装为统一ApiResponse格式
     */
    @PostMapping("/register")
    @Operation(summary = "User Registration", description = "Create new user account, validate email and username uniqueness, return JWT token for direct access to main interface")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters or email/username already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public AuthResponse register(
            @Parameter(description = "Registration request data", required = true)
            @Valid @RequestBody RegisterRequest registerRequest) {
        
        log.info("收到注册请求，邮箱: {}, 用户名: {}, 年龄: {}, 性别: {}, 关系状态: {}", 
                registerRequest.getEmail(), registerRequest.getUsername(), registerRequest.getAge(),
                registerRequest.getGender(), registerRequest.getRelationshipStatus());

        // 调用认证服务进行注册
        AuthResponse response = authService.register(registerRequest);
        
        log.info("用户注册成功，邮箱: {}, 用户ID: {}, 用户名: {}", 
                registerRequest.getEmail(), response.getUser().getId(), response.getUser().getUsername());
        
        return response;
    }

    /**
     * 用户登出接口
     * 目前只是一个占位符，实际的令牌失效逻辑可以在后续实现
     * 
     * @return 登出响应，由GlobalResponseHandler自动包装为统一ApiResponse格式
     */
    @PostMapping("/logout")
    @Operation(summary = "User Logout", description = "User logout from system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    public String logout() {
        log.info("用户登出请求");
        
        // 返回简单字符串，由GlobalResponseHandler自动包装为统一格式
        return "Logout successful";
    }
}