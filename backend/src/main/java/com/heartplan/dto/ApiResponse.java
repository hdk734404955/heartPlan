package com.heartplan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一API响应格式类
 * 所有API接口都应该返回此格式的响应数据
 * 
 * @param <T> 响应数据的类型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    /**
     * 响应状态码
     * 200: 成功
     * 400: 客户端错误
     * 401: 未授权
     * 403: 禁止访问
     * 404: 资源不存在
     * 409: 资源冲突
     * 500: 服务器内部错误
     */
    private Integer code;
    
    /**
     * 响应数据
     * 成功时包含业务数据，失败时为null
     */
    private T data;
    
    /**
     * 响应消息
     * 英文描述信息，用于说明操作结果
     */
    private String message;
    
    /**
     * 创建成功响应（带数据）
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .data(data)
                .message("Success")
                .build();
    }
    
    /**
     * 创建成功响应（带数据和自定义消息）
     * 
     * @param data 响应数据
     * @param message 成功消息
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .code(200)
                .data(data)
                .message(message)
                .build();
    }
    
    /**
     * 创建成功响应（无数据）
     * 
     * @param message 成功消息
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .code(200)
                .data(null)
                .message(message)
                .build();
    }
    
    /**
     * 创建错误响应
     * 
     * @param code 错误状态码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .data(null)
                .message(message)
                .build();
    }
    
    /**
     * 创建错误响应（默认400状态码）
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .code(400)
                .data(null)
                .message(message)
                .build();
    }
}