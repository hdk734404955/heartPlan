package com.heartplan.config;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 常用API响应注解集合
 * 用于简化Controller中重复的@ApiResponses注解
 * 
 * @author HeartPlan Team
 */
public class ApiResponseAnnotations {

    /**
     * 标准成功响应 - 用于大多数GET请求
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operation successful"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public @interface StandardGetResponse {}

    /**
     * 标准CRUD响应 - 用于需要查找资源的请求
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operation successful"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
        @ApiResponse(responseCode = "404", description = "Resource not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public @interface StandardCrudResponse {}

    /**
     * 标准创建/更新响应 - 用于POST/PUT请求
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operation successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
        @ApiResponse(responseCode = "409", description = "Resource conflict (e.g., duplicate data)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public @interface StandardCreateUpdateResponse {}

    /**
     * 认证相关响应 - 用于登录、注册等认证接口
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "403", description = "Account disabled or access denied"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public @interface AuthApiResponse {}

    /**
     * 用户资料相关响应 - 用于用户信息管理接口
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile operation successful"),
        @ApiResponse(responseCode = "400", description = "Invalid profile data or username already exists"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public @interface ProfileResponse {}

    /**
     * 验证相关响应 - 用于检查邮箱、用户名等验证接口
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validation completed"),
        @ApiResponse(responseCode = "400", description = "Invalid format or parameters"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public @interface ValidationResponse {}

    /**
     * 删除操作响应 - 用于DELETE请求
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deletion successful"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
        @ApiResponse(responseCode = "404", description = "Resource not found"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public @interface DeleteResponse {}

    /**
     * 文件上传响应 - 用于文件上传接口
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file format or size"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
        @ApiResponse(responseCode = "413", description = "File too large"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public @interface FileUploadResponse {}
}