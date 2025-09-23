package com.heartplan.controller;

import com.heartplan.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件管理控制器
 * 使用Lombok注解简化代码，提供文件上传相关的REST API接口
 * 主要用于头像上传和文件管理功能
 * 
 * @author HeartPlan Team
 */
@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "*", maxAge = 3600) // 允许所有源的跨域请求
@Slf4j // Lombok注解：自动生成日志对象
@RequiredArgsConstructor // Lombok注解：自动生成包含final字段的构造函数
@Tag(name = "文件管理", description = "文件上传和管理相关API接口")
public class FileController {

    private final FileService fileService;

    /**
     * 上传头像文件接口
     * 用于用户注册或更新头像时上传自定义头像
     * 
     * @param file 上传的头像文件
     * @param userId 用户ID（可选，用于生成唯一文件名）
     * @return 上传结果，包含头像URL
     */
    @PostMapping("/avatar/upload")
    @Operation(summary = "上传头像", description = "上传用户自定义头像文件，支持JPG、PNG、GIF、WebP格式，最大5MB")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "上传成功"),
        @ApiResponse(responseCode = "400", description = "文件格式不支持或文件过大"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public ResponseEntity<Map<String, Object>> uploadAvatar(
            @Parameter(description = "头像文件", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "用户ID（可选）")
            @RequestParam(value = "userId", required = false) Long userId) {
        
        log.info("收到头像上传请求，文件名: {}, 文件大小: {} bytes, 用户ID: {}", 
                file.getOriginalFilename(), file.getSize(), userId);

        try {
            // 如果没有提供用户ID，使用临时ID
            Long actualUserId = userId != null ? userId : System.currentTimeMillis();
            
            // 上传头像文件
            String avatarUrl = fileService.uploadCustomAvatar(file, actualUserId);
            
            // 构建成功响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "头像上传成功");
            response.put("avatarUrl", avatarUrl);
            response.put("fileName", avatarUrl.substring(avatarUrl.lastIndexOf('/') + 1));
            response.put("timestamp", System.currentTimeMillis());
            
            log.info("头像上传成功，用户ID: {}, 头像URL: {}", actualUserId, avatarUrl);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("头像上传失败，用户ID: {}, 原因: {}", userId, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage(), "INVALID_FILE"));

        } catch (Exception e) {
            log.error("头像上传过程中发生未知错误，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("头像上传失败，请稍后重试", "UPLOAD_ERROR"));
        }
    }

    /**
     * 获取随机头像接口
     * 为用户生成随机头像URL
     * 
     * @return 随机头像URL
     */
    @GetMapping("/avatar/random")
    @Operation(summary = "获取随机头像", description = "生成一个随机头像URL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "获取成功")
    })
    public ResponseEntity<Map<String, Object>> getRandomAvatar() {
        log.debug("收到获取随机头像请求");

        try {
            String avatarUrl = fileService.generateRandomAvatarUrl();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "随机头像生成成功");
            response.put("avatarUrl", avatarUrl);
            response.put("fileName", avatarUrl.substring(avatarUrl.lastIndexOf('/') + 1));
            response.put("timestamp", System.currentTimeMillis());
            
            log.debug("随机头像生成成功: {}", avatarUrl);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("生成随机头像时发生错误: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("生成随机头像失败", "RANDOM_AVATAR_ERROR"));
        }
    }

    /**
     * 验证头像文件接口
     * 在实际上传前验证文件是否符合要求
     * 
     * @param file 要验证的文件
     * @return 验证结果
     */
    @PostMapping("/avatar/validate")
    @Operation(summary = "验证头像文件", description = "验证头像文件格式和大小是否符合要求")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "验证通过"),
        @ApiResponse(responseCode = "400", description = "文件不符合要求")
    })
    public ResponseEntity<Map<String, Object>> validateAvatarFile(
            @Parameter(description = "要验证的头像文件", required = true)
            @RequestParam("file") MultipartFile file) {
        
        log.debug("收到头像文件验证请求，文件名: {}, 文件大小: {} bytes", 
                file.getOriginalFilename(), file.getSize());

        try {
            // 这里我们可以调用FileService的私有验证方法
            // 为了简化，我们在这里重复一些验证逻辑
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("文件不能为空");
            }

            if (file.getSize() > 5 * 1024 * 1024) { // 5MB
                throw new IllegalArgumentException("文件大小不能超过5MB");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("只允许上传图片文件");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "文件验证通过");
            response.put("fileName", file.getOriginalFilename());
            response.put("fileSize", file.getSize());
            response.put("contentType", contentType);
            response.put("timestamp", System.currentTimeMillis());
            
            log.debug("头像文件验证通过: {}", file.getOriginalFilename());
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("头像文件验证失败，文件名: {}, 原因: {}", file.getOriginalFilename(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(e.getMessage(), "VALIDATION_FAILED"));

        } catch (Exception e) {
            log.error("头像文件验证过程中发生错误，文件名: {}, 错误: {}", 
                    file.getOriginalFilename(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("文件验证失败", "VALIDATION_ERROR"));
        }
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