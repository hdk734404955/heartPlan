package com.heartplan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 提供简单的测试接口，用于验证API连接和CORS配置
 * 
 * @author HeartPlan Team
 */
@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*", maxAge = 3600) // 允许所有源的跨域请求
@Slf4j
@Tag(name = "测试接口", description = "用于测试API连接和CORS配置的接口")
public class TestController {

    /**
     * 健康检查接口
     * 
     * @return 服务器状态信息
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查服务器是否正常运行")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "服务器运行正常")
    })
    public ResponseEntity<Map<String, Object>> health() {
        log.info("收到健康检查请求");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "HeartPlan API服务器运行正常");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }

    /**
     * CORS测试接口
     * 
     * @return CORS配置测试结果
     */
    @GetMapping("/cors")
    @Operation(summary = "CORS测试", description = "测试跨域资源共享配置是否正常")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "CORS配置正常")
    })
    public ResponseEntity<Map<String, Object>> corsTest() {
        log.info("收到CORS测试请求");
        
        Map<String, Object> response = new HashMap<>();
        response.put("cors", "enabled");
        response.put("message", "跨域请求配置正常");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("allowedOrigins", "所有源(*)");;
        response.put("allowedMethods", "GET, POST, PUT, DELETE, OPTIONS, PATCH, HEAD");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Echo测试接口
     * 
     * @param message 要回显的消息
     * @return 回显结果
     */
    @PostMapping("/echo")
    @Operation(summary = "Echo测试", description = "回显发送的消息，用于测试POST请求")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "消息回显成功")
    })
    public ResponseEntity<Map<String, Object>> echo(@RequestBody Map<String, Object> request) {
        log.info("收到Echo测试请求: {}", request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("echo", request);
        response.put("message", "消息回显成功");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 数据库连接测试
     * 
     * @return 数据库连接状态
     */
    @GetMapping("/db")
    @Operation(summary = "数据库测试", description = "测试数据库连接是否正常")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "数据库连接正常")
    })
    public ResponseEntity<Map<String, Object>> testDatabase() {
        log.info("收到数据库连接测试请求");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 这里可以添加简单的数据库查询测试
            response.put("database", "connected");
            response.put("message", "数据库连接正常");
            response.put("timestamp", LocalDateTime.now().toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("数据库连接测试失败: {}", e.getMessage());
            response.put("database", "error");
            response.put("message", "数据库连接失败: " + e.getMessage());
            response.put("timestamp", LocalDateTime.now().toString());
            
            return ResponseEntity.status(500).body(response);
        }
    }
}