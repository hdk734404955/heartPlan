package com.heartplan.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 配置静态资源访问路径
 * 
 * @author HeartPlan Team
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.path:uploads}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置上传文件的静态资源访问
        String absoluteUploadPath = System.getProperty("user.dir") + "/" + uploadPath + "/";
        log.info("配置静态资源路径: /uploads/** -> {}", absoluteUploadPath);
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absoluteUploadPath);
    }
}