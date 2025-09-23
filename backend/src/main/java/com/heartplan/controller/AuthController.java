package com.heartplan.controller;

import com.heartplan.dto.LoginRequest;
import com.heartplan.dto.LoginResponse;
import com.heartplan.dto.RegisterRequest;
import com.heartplan.dto.RegisterResponse;
import com.heartplan.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 使用Lombok注解简化代码，提供用户认证相关的REST API接口
 * 包括登录、令牌刷新和邮箱/用户名检查等功能
 * 
 * @author HeartPlan Team
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600) // 允许所有源的跨域请求
@Slf4j // Lombok注解：自动生成日志对象
@RequiredArgsConstructor // Lombok注解：自动生成包含final字段的构造函数
@Tag(name = "认证管理", description = "用户认证相关API接口")
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录接口
     * 验证用户凭据并返回JWT令牌
     * 
     * @param loginRequest 登录请求数据
     * @return 登录响应数据，包含JWT令牌和用户信息
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "验证用户邮箱和密码，返回JWT访问令牌和刷新令牌")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "登录成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效"),
        @ApiResponse(responseCode = "401", description = "用户名或密码错误"),
        @ApiResponse(responseCode = "403", description = "账户已被禁用"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<?> login(
            @Parameter(description = "登录请求数据", required = true)
            @Valid @RequestBody LoginRequest loginRequest) {
        
        log.info("收到登录请求，邮箱: {}", loginRequest.getEmail());

        try {
            // 调用认证服务进行登录
            LoginResponse response = authService.login(loginRequest);
            
            log.info("用户登录成功，邮箱: {}, 用户ID: {}", 
                    loginRequest.getEmail(), response.getUser().getId());
            
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            log.warn("登录失败，邮箱: {}, 原因: {}", loginRequest.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("用户名或密码错误", "INVALID_CREDENTIALS"));

        } catch (DisabledException e) {
            log.warn("登录失败，邮箱: {}, 原因: 账户已被禁用", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse("账户已被禁用，请联系管理员", "ACCOUNT_DISABLED"));

        } catch (Exception e) {
            log.error("登录过程中发生未知错误，邮箱: {}, 错误: {}", loginRequest.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("登录失败，请稍后重试", "INTERNAL_ERROR"));
        }
    }

    /**
     * 刷新访问令牌接口
     * 使用刷新令牌获取新的访问令牌
     * 
     * @param refreshTokenRequest 包含刷新令牌的请求
     * @return 新的登录响应数据
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新访问令牌", description = "使用刷新令牌获取新的访问令牌")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "令牌刷新成功"),
        @ApiResponse(responseCode = "400", description = "刷新令牌无效"),
        @ApiResponse(responseCode = "401", description = "刷新令牌已过期"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<?> refreshToken(
            @Parameter(description = "刷新令牌请求", required = true)
            @RequestBody Map<String, String> refreshTokenRequest) {
        
        String refreshToken = refreshTokenRequest.get("refreshToken");
        
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            log.warn("刷新令牌请求缺少refreshToken参数");
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("刷新令牌不能为空", "MISSING_REFRESH_TOKEN"));
        }

        log.info("收到刷新令牌请求");

        try {
            // 调用认证服务刷新令牌
            LoginResponse response = authService.refreshToken(refreshToken);
            
            log.info("令牌刷新成功，用户: {}", response.getUser().getEmail());
            
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            log.warn("刷新令牌失败，原因: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("刷新令牌无效或已过期", "INVALID_REFRESH_TOKEN"));

        } catch (Exception e) {
            log.error("刷新令牌过程中发生未知错误: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("令牌刷新失败，请稍后重试", "INTERNAL_ERROR"));
        }
    }

    /**
     * 检查邮箱是否已存在
     * 用于注册时的邮箱重复检查
     * 
     * @param email 邮箱地址
     * @return 检查结果
     */
    @GetMapping("/check-email")
    @Operation(summary = "检查邮箱是否存在", description = "检查指定邮箱是否已被注册")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "检查完成"),
        @ApiResponse(responseCode = "400", description = "邮箱格式无效")
    })
    public ResponseEntity<Map<String, Object>> checkEmail(
            @Parameter(description = "要检查的邮箱地址", required = true)
            @RequestParam String email) {
        
        log.info("检查邮箱是否存在: {}", email);

        try {
            // 简单的邮箱格式验证
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("邮箱不能为空", "EMPTY_EMAIL"));
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("邮箱格式不正确", "INVALID_EMAIL_FORMAT"));
            }

            // 调用服务层检查邮箱
            boolean exists = authService.isEmailExists(email);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("exists", exists);
            response.put("email", email);
            response.put("message", exists ? "邮箱已存在" : "邮箱可用");
            response.put("timestamp", System.currentTimeMillis());
            
            log.info("邮箱检查完成: {}, 结果: {}", email, exists);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("检查邮箱时发生错误: {}, 错误: {}", email, e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(createErrorResponse("服务器内部错误: " + e.getMessage(), "INTERNAL_ERROR"));
        }
    }

    /**
     * 检查用户名是否已存在
     * 用于注册时的用户名重复检查
     * 
     * @param username 用户名
     * @return 检查结果
     */
    @GetMapping("/check-username")
    @Operation(summary = "检查用户名是否存在", description = "检查指定用户名是否已被使用")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "检查完成"),
        @ApiResponse(responseCode = "400", description = "用户名格式无效")
    })
    public ResponseEntity<Map<String, Object>> checkUsername(
            @Parameter(description = "要检查的用户名", required = true)
            @RequestParam String username) {
        
        log.debug("检查用户名是否存在: {}", username);

        // 简单的用户名格式验证
        if (username == null || username.trim().length() < 2 || username.trim().length() > 50) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("用户名长度必须在2-50个字符之间", "INVALID_USERNAME_FORMAT"));
        }

        boolean exists = authService.isUsernameExists(username.trim());
        
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("username", username.trim());
        response.put("message", exists ? "用户名已存在" : "用户名可用");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 用户注册接口
     * 统一注册接口，完成用户账户创建并返回JWT令牌，直接进入主界面
     * 
     * @param registerRequest 注册请求数据
     * @return 注册响应数据，包含JWT令牌和用户信息
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "创建新用户账户，验证邮箱和用户名唯一性，返回JWT令牌直接进入主界面")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "注册成功"),
        @ApiResponse(responseCode = "400", description = "请求参数无效或邮箱/用户名已存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<?> register(
            @Parameter(description = "注册请求数据", required = true)
            @Valid @RequestBody RegisterRequest registerRequest) {
        
        log.info("收到注册请求，邮箱: {}, 用户名: {}, 年龄: {}, 性别: {}, 恋爱状态: {}", 
                registerRequest.getEmail(), registerRequest.getUsername(), registerRequest.getAge(),
                registerRequest.getGender(), registerRequest.getRelationshipStatus());

        try {
            // 调用认证服务进行注册
            RegisterResponse response = authService.register(registerRequest);
            
            log.info("用户注册成功，邮箱: {}, 用户ID: {}, 用户名: {}", 
                    registerRequest.getEmail(), response.getUser().getId(), response.getUser().getUsername());
            
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("注册失败，邮箱: {}, 原因: {}", registerRequest.getEmail(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage(), "REGISTRATION_VALIDATION_ERROR"));

        } catch (Exception e) {
            log.error("注册过程中发生未知错误，邮箱: {}, 错误: {}", registerRequest.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("注册失败，请稍后重试", "INTERNAL_ERROR"));
        }
    }

    /**
     * 用户登出接口
     * 目前只是一个占位符，实际的令牌失效逻辑可以在后续实现
     * 
     * @return 登出响应
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出系统")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "登出成功")
    })
    public ResponseEntity<Map<String, Object>> logout() {
        log.info("用户登出请求");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "登出成功");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 创建错误响应的辅助方法
     * 
     * @param message 错误消息
     * @param errorCode 错误代码
     * @return 错误响应Map
     */
    private Map<String, Object> createErrorResponse(String message, String errorCode) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", message);
        errorResponse.put("errorCode", errorCode);
        errorResponse.put("timestamp", System.currentTimeMillis());
        return errorResponse;
    }
}