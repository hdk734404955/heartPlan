package com.heartplan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 * 包含所有实体的通用字段：主键ID、创建时间、更新时间
 * 使用JPA审计功能自动管理时间字段
 * 最大化使用Lombok注解简化代码
 * 
 * @author HeartPlan Team
 */
@MappedSuperclass // JPA: 标记为映射超类，不会创建对应的数据库表
@Data // Lombok: 自动生成getter、setter、toString、equals、hashCode方法
@NoArgsConstructor // Lombok: 自动生成无参构造函数
@AllArgsConstructor // Lombok: 自动生成全参构造函数
@EntityListeners(AuditingEntityListener.class) // JPA: 启用审计监听器，自动管理时间字段
public abstract class BaseEntity implements Serializable {

    /**
     * 主键ID，使用自增策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间（审计字段）
     * 在实体首次保存时自动设置
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间（审计字段）
     * 在实体每次更新时自动设置
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}