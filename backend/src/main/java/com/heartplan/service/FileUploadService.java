package com.heartplan.service;

import com.heartplan.util.ColorExtractionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传服务
 * 处理图片上传、验证、存储和URL生成
 * 
 * @author HeartPlan Team
 */
@Service
@Slf4j
public class FileUploadService {

    @Value("${app.upload.path:uploads}")
    private String uploadPath;

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${app.upload.max-size:5242880}") // 5MB
    private long maxFileSize;

    // 允许的图片格式
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif", "webp"};

    /**
     * 上传图片文件
     * 
     * @param file 图片文件
     * @param type 图片类型 (avatar, background, etc.)
     * @return Map<String, Object> 包含图片信息的结果
     */
    public Map<String, Object> uploadImage(MultipartFile file, String type) {
        try {
            // 验证文件
            validateImageFile(file);
            
            // 生成文件名和保存文件
            String fileName = generateFileName(file.getOriginalFilename(), type);
            String relativePath = saveFile(file, fileName);
            
            // 生成访问URL
            String imageUrl = generateImageUrl(relativePath);
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("url", imageUrl);
            result.put("fileName", fileName);
            result.put("fileSize", file.getSize());
            result.put("type", type);
            
            // 如果是背景图片，提取主色调
            if ("background".equals(type)) {
                try {
                    String mainColor = ColorExtractionUtil.extractDominantColor(imageUrl);
                    result.put("mainColor", mainColor);
                    log.info("背景图片主色调提取成功: {} -> {}", imageUrl, mainColor);
                } catch (Exception e) {
                    log.warn("背景图片主色调提取失败: {}, 使用默认颜色", e.getMessage());
                    // 不影响上传流程，使用默认颜色
                    result.put("mainColor", "#FF6B6B");
                }
            }
            
            log.info("图片上传成功: {} -> {}", file.getOriginalFilename(), imageUrl);
            return result;
            
        } catch (Exception e) {
            log.error("图片上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("图片上传失败: " + e.getMessage());
        }
    }

    /**
     * 验证图片文件
     */
    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        // 检查文件大小
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("文件大小不能超过" + (maxFileSize / 1024 / 1024) + "MB");
        }
        
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只支持图片文件");
        }
        
        // 检查文件扩展名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!isAllowedImageExtension(extension)) {
            throw new IllegalArgumentException("不支持的图片格式，只支持: jpg, jpeg, png, gif, webp");
        }
    }

    /**
     * 生成唯一文件名
     */
    private String generateFileName(String originalFilename, String type) {
        String extension = getFileExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        return String.format("%s_%s_%s.%s", type, timestamp, uuid, extension);
    }

    /**
     * 保存文件到本地存储
     */
    private String saveFile(MultipartFile file, String fileName) throws IOException {
        // 创建按日期分组的目录结构
        String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fullUploadPath = uploadPath + File.separator + dateFolder;
        
        Path uploadDir = Paths.get(fullUploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        
        // 保存文件
        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        log.info("文件保存成功: {}", filePath.toString());
        
        // 返回相对路径用于URL生成
        return dateFolder + "/" + fileName;
    }

    /**
     * 生成图片访问URL
     */
    private String generateImageUrl(String relativePath) {
        // 生成完整的访问URL，包含context-path
        return String.format("http://localhost:%s/api/uploads/%s", serverPort, relativePath);
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }

    /**
     * 检查是否为允许的图片扩展名
     */
    private boolean isAllowedImageExtension(String extension) {
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除文件
     * 
     * @param relativePath 相对路径
     * @return boolean 删除是否成功
     */
    public boolean deleteFile(String relativePath) {
        try {
            Path filePath = Paths.get(uploadPath, relativePath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("文件删除成功: {}", filePath.toString());
                return true;
            }
            return false;
        } catch (IOException e) {
            log.error("文件删除失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 检查文件是否存在
     * 
     * @param relativePath 相对路径
     * @return boolean 文件是否存在
     */
    public boolean fileExists(String relativePath) {
        Path filePath = Paths.get(uploadPath, relativePath);
        return Files.exists(filePath);
    }
}