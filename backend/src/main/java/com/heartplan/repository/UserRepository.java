package com.heartplan.repository;

import com.heartplan.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 * 继承JpaRepository，提供基础的CRUD操作
 * 定义用户相关的数据查询方法
 * 
 * @author HeartPlan Team
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据邮箱查找用户
     * 用于登录验证和邮箱唯一性检查
     * 
     * @param email 用户邮箱
     * @return 用户对象（可能为空）
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据用户名查找用户
     * 用于用户名唯一性检查
     * 
     * @param username 用户名
     * @return 用户对象（可能为空）
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找启用的用户
     * 用于登录时确保用户账户是启用状态
     * 
     * @param email 用户邮箱
     * @return 启用的用户对象（可能为空）
     */
    Optional<User> findByEmailAndEnabledTrue(String email);

    /**
     * 检查邮箱是否已存在
     * 用于注册时的邮箱重复检查
     * 
     * @param email 用户邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 检查用户名是否已存在
     * 用于注册时的用户名重复检查
     * 
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 根据恋爱状态查找用户
     * 用于统计和分析不同恋爱状态的用户
     * 
     * @param relationshipStatus 恋爱状态
     * @return 用户列表
     */
    List<User> findByRelationshipStatus(User.RelationshipStatus relationshipStatus);

    /**
     * 查找指定时间之后登录的用户
     * 用于统计活跃用户
     * 
     * @param dateTime 指定时间
     * @return 用户列表
     */
    List<User> findByLastLoginAtAfter(LocalDateTime dateTime);

    /**
     * 根据年龄范围查找用户
     * 用于用户匹配和推荐
     * 
     * @param minAge 最小年龄
     * @param maxAge 最大年龄
     * @return 用户列表
     */
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);

    /**
     * 根据性别和恋爱状态查找用户
     * 用于用户推荐和匹配
     * 
     * @param gender 性别
     * @param relationshipStatus 恋爱状态
     * @return 用户列表
     */
    List<User> findByGenderAndRelationshipStatus(User.Gender gender, User.RelationshipStatus relationshipStatus);

    /**
     * 查找启用的用户，按创建时间降序排列
     * 用于获取最新注册的用户
     * 
     * @return 用户列表
     */
    List<User> findByEnabledTrueOrderByCreatedAtDesc();

    /**
     * 根据位置模糊查找用户
     * 用于基于地理位置的用户推荐
     * 
     * @param location 位置关键词
     * @return 用户列表
     */
    List<User> findByLocationContainingIgnoreCase(String location);

    /**
     * 统计指定恋爱状态的用户数量
     * 用于数据统计和分析
     * 
     * @param relationshipStatus 恋爱状态
     * @return 用户数量
     */
    long countByRelationshipStatus(User.RelationshipStatus relationshipStatus);

    /**
     * 统计启用的用户总数
     * 用于平台用户统计
     * 
     * @return 启用用户数量
     */
    long countByEnabledTrue();

    /**
     * 使用自定义查询查找相似兴趣的用户
     * 用于智能推荐系统
     * 
     * @param userId 当前用户ID
     * @param relationshipStatus 目标恋爱状态
     * @param pageable 分页参数（包含限制数量）
     * @return 推荐用户列表
     */
    @Query("SELECT u FROM User u WHERE u.id != :userId " +
           "AND u.relationshipStatus = :relationshipStatus " +
           "AND u.enabled = true " +
           "AND u.deleted = false " +
           "ORDER BY u.createdAt DESC")
    List<User> findRecommendedUsers(@Param("userId") Long userId, 
                                   @Param("relationshipStatus") User.RelationshipStatus relationshipStatus,
                                   Pageable pageable);

    /**
     * 查找在指定时间范围内活跃的用户
     * 用于活跃度分析
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 活跃用户列表
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt BETWEEN :startTime AND :endTime " +
           "AND u.enabled = true AND u.deleted = false")
    List<User> findActiveUsersBetween(@Param("startTime") LocalDateTime startTime, 
                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 根据用户ID列表批量查找用户
     * 用于批量操作和关系查询
     * 
     * @param userIds 用户ID列表
     * @return 用户列表
     */
    List<User> findByIdInAndEnabledTrue(List<Long> userIds);
}