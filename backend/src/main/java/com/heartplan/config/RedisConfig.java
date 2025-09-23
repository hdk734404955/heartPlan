package com.heartplan.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 * 配置Redis连接和序列化方式
 * 
 * @author HeartPlan Team
 */
@Configuration
@Slf4j // Lombok注解：自动生成日志对象
public class RedisConfig {

    /**
     * 配置RedisTemplate
     * 设置键值序列化方式，确保数据存储和读取的一致性
     * 
     * @param connectionFactory Redis连接工厂
     * @return 配置好的RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        log.info("正在配置Redis模板...");
        
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // 使用String序列化器处理键
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        
        // 使用JSON序列化器处理值
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        
        // 启用事务支持
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        
        log.info("Redis模板配置完成");
        return template;
    }
}