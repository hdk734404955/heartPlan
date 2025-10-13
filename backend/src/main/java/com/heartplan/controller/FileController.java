package com.heartplan.controller;

import com.heartplan.config.ApiResponseAnnotations.FileUploadResponse;
import com.heartplan.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件上传控制器
 * 提供图片上传功能，不需要JWT认证
 * 
 * @author HeartPlan Team
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "File Upload", description = "File upload API endpoints")
public class FileController {

    private final FileUploadService fileUploadService;

    /**
     * 上传图片
     * 
     * @param file 图片文件
     * @param type 图片类型 (avatar, background, etc.)
     * @return Map<String, Object> 包含图片URL的响应
     */
    @PostMapping("/image")
    @Operation(summary = "Upload Image", description = "Upload image file and return URL")
    @FileUploadResponse
    public Map<String, Object> uploadImage(
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Image type (avatar, background, etc.)")
            @RequestParam(value = "type", defaultValue = "image") String type) {

        log.info("接收到图片上传请求: 文件名={}, 类型={}, 大小={}KB", 
                file.getOriginalFilename(), type, file.getSize() / 1024);
        
        return fileUploadService.uploadImage(file, type);
    }
}