package com.heartplan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 文件服务类
 * 使用Lombok注解简化代码，提供文件上传、头像处理等功能
 * 包括随机头像生成和自定义头像上传功能
 * 
 * @author HeartPlan Team
 */
@Service
@Slf4j // Lombok注解：自动生成日志对象
@RequiredArgsConstructor // Lombok注解：自动生成包含final字段的构造函数
public class FileService {

    /**
     * 文件上传根目录
     */
    @Value("${app.file.upload-dir:uploads}")
    private String uploadDir;

    /**
     * 文件访问基础URL
     */
    @Value("${app.file.base-url:http://localhost:8080/files}")
    private String baseUrl;

    /**
     * 允许的图片文件类型
     */
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    /**
     * 允许的图片文件扩展名
     */
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(
        ".jpg", ".jpeg", ".png", ".gif", ".webp"
    );

    /**
     * 最大文件大小（5MB）
     */
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 预定义的随机头像列表
     * 这些是系统内置的头像文件名，实际部署时需要将对应的图片文件放在static/avatars目录下
     */
    private static final List<String> RANDOM_AVATARS = Arrays.asList(
        "avatar_1.png", "avatar_2.png", "avatar_3.png", "avatar_4.png", "avatar_5.png",
        "avatar_6.png", "avatar_7.png", "avatar_8.png", "avatar_9.png", "avatar_10.png",
        "avatar_11.png", "avatar_12.png", "avatar_13.png", "avatar_14.png", "avatar_15.png",
        "avatar_16.png", "avatar_17.png", "avatar_18.png", "avatar_19.png", "avatar_20.png"
    );

    private final Random random = new Random();

    /**
     * 生成随机头像URL
     * 从预定义的头像列表中随机选择一个
     * 
     * @return 随机头像的URL
     */
    public String generateRandomAvatarUrl() {
        String randomAvatar = RANDOM_AVATARS.get(random.nextInt(RANDOM_AVATARS.size()));
        String avatarUrl = baseUrl + "/avatars/" + randomAvatar;
        
        log.info("生成随机头像URL: {}", avatarUrl);
        return avatarUrl;
    }

    /**
     * 上传用户自定义头像
     * 验证文件类型和大小，保存到指定目录
     * 
     * @param file 上传的头像文件
     * @param userId 用户ID（用于生成唯一文件名）
     * @return 上传成功后的头像URL
     * @throws IOException 文件操作异常
     * @throws IllegalArgumentException 文件验证失败异常
     */
    public String uploadCustomAvatar(MultipartFile file, Long userId) throws IOException {
        log.info("开始上传用户头像，用户ID: {}, 文件名: {}, 文件大小: {} bytes", 
                userId, file.getOriginalFilename(), file.getSize());

        // 验证文件
        validateImageFile(file);

        // 创建上传目录
        Path uploadPath = createUploadDirectory("avatars");

        // 生成唯一文件名
        String fileName = generateUniqueFileName(file.getOriginalFilename(), userId);
        Path filePath = uploadPath.resolve(fileName);

        // 保存文件
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 生成访问URL
        String avatarUrl = baseUrl + "/avatars/" + fileName;
        
        log.info("用户头像上传成功，用户ID: {}, 文件路径: {}, 访问URL: {}", 
                userId, filePath.toString(), avatarUrl);
        
        return avatarUrl;
    }

    /**
     * 根据文件名获取头像URL
     * 用于处理前端传递的自定义头像文件名
     * 
     * @param fileName 文件名
     * @return 头像URL
     */
    public String getAvatarUrlByFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            log.warn("文件名为空，返回随机头像");
            return generateRandomAvatarUrl();
        }

        String avatarUrl = baseUrl + "/avatars/" + fileName.trim();
        log.debug("根据文件名生成头像URL: {} -> {}", fileName, avatarUrl);
        return avatarUrl;
    }

    /**
     * 验证上传的图片文件
     * 检查文件类型、大小和扩展名
     * 
     * @param file 上传的文件
     * @throws IllegalArgumentException 验证失败时抛出
     */
    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 检查文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小不能超过5MB");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("不支持的文件类型，只允许上传图片文件（JPG、PNG、GIF、WebP）");
        }

        // 检查文件扩展名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("不支持的文件扩展名，只允许：" + String.join(", ", ALLOWED_IMAGE_EXTENSIONS));
        }

        log.debug("文件验证通过，文件名: {}, 类型: {}, 大小: {} bytes", 
                originalFilename, contentType, file.getSize());
    }

    /**
     * 创建上传目录
     * 如果目录不存在则创建
     * 
     * @param subDir 子目录名
     * @return 目录路径
     * @throws IOException 目录创建失败时抛出
     */
    private Path createUploadDirectory(String subDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir, subDir);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("创建上传目录: {}", uploadPath.toString());
        }
        
        return uploadPath;
    }

    /**
     * 生成唯一文件名
     * 使用用户ID、时间戳和UUID确保文件名唯一性
     * 
     * @param originalFilename 原始文件名
     * @param userId 用户ID
     * @return 唯一文件名
     */
    private String generateUniqueFileName(String originalFilename, Long userId) {
        String extension = getFileExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        String uniqueFileName = String.format("user_%d_%s_%s%s", userId, timestamp, uuid, extension);
        
        log.debug("生成唯一文件名: {} -> {}", originalFilename, uniqueFileName);
        return uniqueFileName;
    }

    /**
     * 获取文件扩展名
     * 
     * @param filename 文件名
     * @return 文件扩展名（包含点号）
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        
        return filename.substring(lastDotIndex);
    }

    /**
     * 删除用户头像文件
     * 用于用户更换头像时清理旧文件
     * 
     * @param avatarUrl 头像URL
     * @return 是否删除成功
     */
    public boolean deleteAvatarFile(String avatarUrl) {
        if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
            return false;
        }

        try {
            // 从URL中提取文件名
            String fileName = avatarUrl.substring(avatarUrl.lastIndexOf('/') + 1);
            
            // 跳过随机头像的删除（它们是系统内置的）
            if (RANDOM_AVATARS.contains(fileName)) {
                log.debug("跳过删除系统内置头像: {}", fileName);
                return true;
            }

            Path filePath = Paths.get(uploadDir, "avatars", fileName);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("成功删除头像文件: {}", filePath.toString());
                return true;
            } else {
                log.warn("要删除的头像文件不存在: {}", filePath.toString());
                return false;
            }
            
        } catch (Exception e) {
            log.error("删除头像文件失败，URL: {}, 错误: {}", avatarUrl, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 检查头像URL是否为随机头像
     * 
     * @param avatarUrl 头像URL
     * @return 是否为随机头像
     */
    public boolean isRandomAvatar(String avatarUrl) {
        if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
            return false;
        }

        String fileName = avatarUrl.substring(avatarUrl.lastIndexOf('/') + 1);
        return RANDOM_AVATARS.contains(fileName);
    }
}