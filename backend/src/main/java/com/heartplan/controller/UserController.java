package com.heartplan.controller;

import com.heartplan.config.ApiResponseAnnotations.*;
import com.heartplan.dto.UserInfoDTO;
import com.heartplan.security.UserPrincipal;
import com.heartplan.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息管理控制器
 * 提供用户信息查询和更新的REST API接口
 * 需要用户认证才能访问
 * 
 * @author HeartPlan Team
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User information management API endpoints")
@SecurityRequirement(name = "bearerAuth") // 需要JWT认证
public class UserController {

    private final UserService userService;

    /**
     * 获取当前用户信息
     * 
     * @param userPrincipal 当前认证用户信息
     * @return UserInfoDTO 用户信息
     */
    @GetMapping("/profile")
    @Operation(summary = "Get Current User Profile", description = "Get current authenticated user's profile information")
    @ProfileResponse
    public UserInfoDTO getCurrentUserProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal userPrincipal) {
        
        log.info("获取用户信息请求，用户ID: {}", userPrincipal.getId());
        
        UserInfoDTO userInfo = userService.getUserById(userPrincipal.getId());
        
        log.info("用户信息获取成功，用户: {}", userInfo.getUsername());
        
        return userInfo;
    }

    /**
     * 更新当前用户信息
     * 复用RegisterRequest作为更新请求DTO，忽略email和password字段
     * 
     * @param userPrincipal 当前认证用户信息
     * @param updateRequest 更新请求数据
     * @return UserInfoDTO 更新后的用户信息
     */
    @PutMapping("/profile")
    @Operation(summary = "Update Current User Profile", 
               description = "Update current authenticated user's profile information. Email and password fields are ignored.")
    @ProfileResponse
    public UserInfoDTO updateCurrentUserProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "User profile update data (id, email, enabled, createdAt fields will be ignored)", required = true)
            @Valid @RequestBody UserInfoDTO updateRequest) {
        
        log.info("更新用户信息请求，用户ID: {}, 新用户名: {}, 年龄: {}, 性别: {}, 关系状态: {}", 
                userPrincipal.getId(), updateRequest.getUsername(), updateRequest.getAge(),
                updateRequest.getGender(), updateRequest.getRelationshipStatus());
        
        UserInfoDTO updatedUserInfo = userService.updateUserInfo(userPrincipal.getId(), updateRequest);
        
        log.info("用户信息更新成功，用户ID: {}, 用户名: {}", 
                userPrincipal.getId(), updatedUserInfo.getUsername());
        
        return updatedUserInfo;
    }

    /**
     * 检查用户名是否可用
     * 用于更新用户信息时的用户名重复检查
     * 
     * @param userPrincipal 当前认证用户信息
     * @param username 要检查的用户名
     * @return 检查结果
     */
    @GetMapping("/check-username")
    @Operation(summary = "Check Username Availability", 
               description = "Check if username is available for current user (excludes current user's username)")
    @ValidationResponse
    public Map<String, Object> checkUsernameAvailability(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "Username to check", required = true)
            @RequestParam String username) {
        
        log.info("检查用户名可用性，用户ID: {}, 用户名: {}", userPrincipal.getId(), username);

        // 参数验证
        if (username == null || username.trim().length() < 2 || username.trim().length() > 50) {
            throw new IllegalArgumentException("Username length must be between 2-50 characters");
        }

        boolean available = userService.isUsernameAvailable(username.trim(), userPrincipal.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("available", available);
        result.put("username", username.trim());
        
        log.info("用户名可用性检查完成，用户名: {}, 可用: {}", username.trim(), available);
        
        return result;
    }

    /**
     * 根据用户ID获取用户公开信息
     * 用于查看其他用户的基本信息（不包含敏感信息）
     * 
     * @param userId 要查询的用户ID
     * @return UserInfoDTO 用户公开信息
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Get User Public Profile", description = "Get public profile information of specified user")
    @ProfileResponse
    public UserInfoDTO getUserProfile(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId) {
        
        log.info("获取用户公开信息请求，用户ID: {}", userId);
        
        UserInfoDTO userInfo = userService.getUserById(userId);
        
        // 移除敏感信息（如果需要的话，这里可以创建一个公开信息的DTO）
        // 目前UserInfoDTO已经不包含密码等敏感信息，所以可以直接返回
        
        log.info("用户公开信息获取成功，用户: {}", userInfo.getUsername());
        
        return userInfo;
    }
}