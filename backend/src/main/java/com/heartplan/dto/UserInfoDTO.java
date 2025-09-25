package com.heartplan.dto;

import com.heartplan.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户基本信息DTO
 * 只包含前端需要的基本用户信息，不包含敏感数据
 * 独立的DTO类，提高复用性和可维护性
 * 
 * @author HeartPlan Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 用户性别
     */
    private User.Gender gender;

    /**
     * 恋爱状态
     */
    private User.RelationshipStatus relationshipStatus;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 所在位置
     */
    private String location;

    /**
     * 账户是否启用
     */
    private Boolean enabled;

    /**
     * 注册时间
     */
    private LocalDateTime createdAt;
}