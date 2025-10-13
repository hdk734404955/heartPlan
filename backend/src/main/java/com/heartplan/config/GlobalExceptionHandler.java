package com.heartplan.config;

import com.heartplan.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理所有异常，返回ApiResponse格式的错误响应
 * 实现清晰的异常类型到HTTP状态码的映射规则
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理认证失败异常
     * BadCredentialsException - 用户名或密码错误
     * 注意：登录时的用户名密码错误应该返回400，而不是401
     * 401应该用于token无效/过期的情况
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("认证失败: {}", e.getMessage());
        // 登录凭据错误应该返回400 Bad Request，而不是401 Unauthorized
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, "Invalid email or password"));
    }

    /**
     * 处理账户被禁用异常
     * DisabledException - 账户已被禁用
     */
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<Object>> handleDisabledException(DisabledException e) {
        log.warn("账户被禁用: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(403, "Account has been disabled"));
    }

    /**
     * 处理通用认证异常
     * AuthenticationException - 其他认证相关异常（主要是JWT token相关）
     * 这里的401是合适的，因为通常是token无效/过期导致的
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException e) {
        log.warn("认证异常: {}", e.getMessage());
        // JWT token相关的认证失败才应该返回401
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Session expired, please login again"));
    }

    /**
     * 处理参数验证异常
     * MethodArgumentNotValidException - @Valid注解验证失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("参数验证失败: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, errorMessage));
    }

    /**
     * 处理绑定异常
     * BindException - 数据绑定失败
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Object>> handleBindException(BindException e) {
        String errorMessage = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("数据绑定失败: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, errorMessage));
    }

    /**
     * 处理约束违反异常
     * ConstraintViolationException - Bean Validation约束违反
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("约束违反: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, errorMessage));
    }

    /**
     * 处理参数类型不匹配异常
     * MethodArgumentTypeMismatchException - 请求参数类型错误
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String errorMessage = String.format("Invalid parameter '%s': expected %s", 
                e.getName(), e.getRequiredType().getSimpleName());
        
        log.warn("参数类型不匹配: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, errorMessage));
    }

    /**
     * 处理HTTP请求方法不支持异常
     * HttpRequestMethodNotSupportedException - 请求方法不允许
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String errorMessage = String.format("Method '%s' not supported", e.getMethod());
        
        log.warn("请求方法不支持: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(405, errorMessage));
    }

    /**
     * 处理HTTP媒体类型不支持异常
     * HttpMediaTypeNotSupportedException - 请求内容类型不支持
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        String errorMessage = String.format("Content type '%s' not supported. Please use 'application/json'", 
                e.getContentType());
        
        log.warn("媒体类型不支持: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(ApiResponse.error(415, "Content type not supported. Please use 'application/json'"));
    }

    /**
     * 处理数据完整性违反异常
     * DataIntegrityViolationException - 数据库约束违反（如邮箱重复）
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String errorMessage = "Data conflict occurred";
        
        // 检查是否是邮箱重复错误
        if (e.getMessage() != null && e.getMessage().contains("email")) {
            errorMessage = "Email already exists";
        }
        // 检查是否是用户名重复错误
        else if (e.getMessage() != null && e.getMessage().contains("username")) {
            errorMessage = "Username already exists";
        }
        
        log.warn("数据完整性违反: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(409, errorMessage));
    }

    /**
     * 处理用户不存在异常
     * UsernameNotFoundException - 用户名不存在
     * 注意：用户不存在也应该返回400，因为这是客户端提供了错误的参数
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.warn("用户不存在: {}", e.getMessage());
        // 用户不存在应该返回400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, "Invalid email or password"));
    }

    /**
     * 处理文件操作异常
     * IOException - 文件读写异常
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<Object>> handleIOException(IOException e) {
        log.error("文件操作失败: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "File operation failed"));
    }

    /**
     * 处理业务逻辑异常
     * IllegalArgumentException - 非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("业务参数异常: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, e.getMessage()));
    }

    /**
     * 处理文件上传大小超限异常
     * MaxUploadSizeExceededException - 文件上传大小超限
     */
    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleMaxUploadSizeExceededException(
            org.springframework.web.multipart.MaxUploadSizeExceededException e) {
        log.warn("文件上传大小超限: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, "File size exceeds the maximum limit of 5MB"));
    }

    /**
     * 处理运行时异常
     * RuntimeException - 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常发生: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "An unexpected error occurred"));
    }

    /**
     * 处理所有其他异常
     * Exception - 兜底异常处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception e) {
        log.error("系统发生未预期的错误", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Internal server error"));
    }
}