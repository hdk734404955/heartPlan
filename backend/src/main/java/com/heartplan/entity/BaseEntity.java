package com.heartplan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 基础实体类
 * 使用Lombok注解简化代码，提供通用的实体字段
 * 所有实体类都应该继承此类
 * 
 * @author HeartPlan Team
 */
@MappedSuperclass // 标记为映射超类，不会创建对应的数据库表
@EntityListeners(AuditingEntityListener.class) // 启用JPA审计监听器
@Data // Lombok注解：自动生成getter、setter、toString、equals、hashCode方法
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // 只包含明确标记的字段进行equals和hashCode计算
public abstract class BaseEntity {

    /**
     * 主键ID
     * 使用自增策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // 包含在equals和hashCode计算中
    private Long id;

    /**
     * 创建时间
     * 由JPA审计功能自动填充
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     * 由JPA审计功能自动填充
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除标记
     * false表示未删除，true表示已删除
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    /**
     * 版本号，用于乐观锁
     */
    @Version
    @Column(name = "version")
    private Long version;
}