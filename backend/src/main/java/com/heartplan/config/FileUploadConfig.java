package com.heartplan.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;
import java.io.File;

/**
 * 文件上传配置类
 * 使用Lombok注解简化代码，配置文件上传相关的参数和静态资源访问
 * 
 * @author HeartPlan Team
 */
@Configuration
@Slf4j // Lombok注解：自动生成日志对象
public class FileUploadConfig implements WebMvcConfigurer {

    /**
     * 文件上传根目录
     */
    @Value("${app.file.upload-dir:uploads}")
    private String uploadDir;

    /**
     * 配置文件上传参数
     * 设置最大文件大小和请求大小限制
     * 
     * @return MultipartConfigElement配置
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        
        // 设置单个文件最大大小为5MB
        factory.setMaxFileSize(DataSize.ofMegabytes(5));
        
        // 设置总请求最大大小为10MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
        
        // 设置文件大小阈值，超过这个大小的文件将被写入磁盘
        factory.setFileSizeThreshold(DataSize.ofKilobytes(512));
        
        log.info("文件上传配置已加载 - 最大文件大小: 5MB, 最大请求大小: 10MB");
        
        return factory.createMultipartConfig();
    }

    /**
     * 配置静态资源处理器
     * 允许通过HTTP访问上传的文件
     * 
     * @param registry 资源处理器注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保上传目录存在
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            boolean created = uploadDirectory.mkdirs();
            if (created) {
                log.info("创建上传目录: {}", uploadDirectory.getAbsolutePath());
            } else {
                log.warn("无法创建上传目录: {}", uploadDirectory.getAbsolutePath());
            }
        }

        // 配置文件访问路径映射
        String resourcePath = "file:" + uploadDirectory.getAbsolutePath() + "/";
        
        registry.addResourceHandler("/files/**")
                .addResourceLocations(resourcePath)
                .setCachePeriod(3600); // 缓存1小时

        // 配置系统内置头像访问路径
        registry.addResourceHandler("/files/avatars/**")
                .addResourceLocations("classpath:/static/avatars/", resourcePath + "avatars/")
                .setCachePeriod(86400); // 缓存24小时

        log.info("静态资源处理器已配置 - 上传目录: {}, 访问路径: /files/**", resourcePath);
    }
}