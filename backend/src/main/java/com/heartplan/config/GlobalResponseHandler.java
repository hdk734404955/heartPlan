package com.heartplan.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartplan.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应处理器
 * 使用@RestControllerAdvice拦截所有控制器响应
 * 自动将原始返回值包装为统一的ApiResponse格式
 * 
 * 处理逻辑：
 * 1. 如果返回值已经是ApiResponse类型，直接返回
 * 2. 如果返回值是其他类型，自动包装为ApiResponse格式
 * 3. 特殊处理String类型返回值，避免类型转换问题
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    /**
     * 判断是否需要处理响应
     * 
     * @param returnType 返回类型
     * @param converterType 消息转换器类型
     * @return true表示需要处理，false表示不处理
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 排除Swagger相关的接口
        String className = returnType.getDeclaringClass().getName();
        if (className.contains("springdoc") || className.contains("swagger")) {
            return false;
        }
        
        // 排除Spring Boot Actuator相关的接口
        if (className.contains("actuator")) {
            return false;
        }
        
        // 排除错误页面相关的接口
        if (className.contains("BasicErrorController")) {
            return false;
        }
        
        // 处理所有其他的REST控制器响应
        return true;
    }

    /**
     * 在响应体写入之前进行处理
     * 将原始返回值包装为统一的ApiResponse格式
     * 
     * @param body 原始响应体
     * @param returnType 返回类型
     * @param selectedContentType 选择的内容类型
     * @param selectedConverterType 选择的转换器类型
     * @param request HTTP请求
     * @param response HTTP响应
     * @return 处理后的响应体
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {
        
        // 如果返回值已经是ApiResponse类型，直接返回
        if (body instanceof ApiResponse) {
            log.debug("响应已经是ApiResponse格式，直接返回");
            return body;
        }
        
        // 如果返回值为null，返回成功响应
        if (body == null) {
            log.debug("响应体为null，返回成功响应");
            return ApiResponse.success("Success");
        }
        
        // 特殊处理String类型返回值
        // 因为Spring的StringHttpMessageConverter会直接处理String类型
        // 需要将ApiResponse转换为JSON字符串
        if (returnType.getGenericParameterType().equals(String.class)) {
            try {
                ApiResponse<Object> apiResponse = ApiResponse.success(body);
                String jsonResponse = objectMapper.writeValueAsString(apiResponse);
                log.debug("String类型响应包装完成: {}", jsonResponse);
                return jsonResponse;
            } catch (JsonProcessingException e) {
                log.error("转换String响应为JSON时发生错误: {}", e.getMessage(), e);
                // 如果转换失败，返回原始字符串
                return body;
            }
        }
        
        // 对于其他类型，包装为ApiResponse
        ApiResponse<Object> apiResponse = ApiResponse.success(body);
        log.debug("响应包装完成，原始类型: {}, 包装后: ApiResponse", 
                body.getClass().getSimpleName());
        
        return apiResponse;
    }
}