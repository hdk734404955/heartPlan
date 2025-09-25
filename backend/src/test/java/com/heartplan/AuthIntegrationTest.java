package com.heartplan;

import com.heartplan.entity.User;
import com.heartplan.repository.UserRepository;
import com.heartplan.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 认证功能集成测试
 * 测试简化后的用户实体、认证服务和相关功能
 * 
 * @author HeartPlan Team
 */
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@Transactional
public class AuthIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 测试简化后的用户实体的创建和保存
     */
    @Test
    public void testUserEntityCreationAndSave() {
        log.info("开始测试简化后的用户实体的创建和保存");

        // 创建简化的用户实体
        User user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .password(passwordEncoder.encode("password123"))
                .age(25)
                .gender(User.Gender.MALE)
                .relationshipStatus(User.RelationshipStatus.SINGLE)
                .location("Beijing")
                .avatarUrl("https://example.com/avatar.jpg")
                .enabled(true)
                .build();

        // 保存用户
        User savedUser = userRepository.save(user);

        // 验证保存结果
        assertNotNull(savedUser.getId());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("testuser", savedUser.getUsername());
        assertEquals(25, savedUser.getAge());
        assertEquals(User.Gender.MALE, savedUser.getGender());
        assertEquals(User.RelationshipStatus.SINGLE, savedUser.getRelationshipStatus());
        assertEquals("Beijing", savedUser.getLocation());
        assertEquals("https://example.com/avatar.jpg", savedUser.getAvatarUrl());
        assertTrue(savedUser.getEnabled());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());

        log.info("简化后的用户实体创建和保存测试通过，用户ID: {}", savedUser.getId());
    }

    /**
     * 测试UserRepository的查询方法
     */
    @Test
    public void testUserRepositoryQueries() {
        log.info("开始测试UserRepository的查询方法");

        // 创建测试用户
        User user = User.builder()
                .email("query@example.com")
                .username("queryuser")
                .password(passwordEncoder.encode("password123"))
                .age(28)
                .gender(User.Gender.FEMALE)
                .relationshipStatus(User.RelationshipStatus.IN_RELATIONSHIP)
                .location("Shanghai")
                .avatarUrl("https://example.com/query-avatar.jpg")
                .enabled(true)
                .build();

        userRepository.save(user);

        // 测试根据邮箱查找用户
        assertTrue(userRepository.findByEmail("query@example.com").isPresent());
        assertFalse(userRepository.findByEmail("nonexistent@example.com").isPresent());

        // 测试根据用户名查找用户
        assertTrue(userRepository.findByUsername("queryuser").isPresent());
        assertFalse(userRepository.findByUsername("nonexistentuser").isPresent());

        // 测试邮箱和用户名存在性检查
        assertTrue(userRepository.existsByEmail("query@example.com"));
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
        assertTrue(userRepository.existsByUsername("queryuser"));
        assertFalse(userRepository.existsByUsername("nonexistentuser"));

        // 测试根据恋爱状态查找用户
        assertFalse(userRepository.findByRelationshipStatus(User.RelationshipStatus.IN_RELATIONSHIP).isEmpty());

        // 测试根据年龄范围查找用户
        assertFalse(userRepository.findByAgeBetween(25, 30).isEmpty());

        // 测试根据性别和恋爱状态查找用户
        assertFalse(userRepository.findByGenderAndRelationshipStatus(
                User.Gender.FEMALE, User.RelationshipStatus.IN_RELATIONSHIP).isEmpty());

        log.info("UserRepository查询方法测试通过");
    }

    /**
     * 测试AuthService的邮箱和用户名检查功能
     */
    @Test
    public void testAuthServiceValidation() {
        log.info("开始测试AuthService的验证功能");

        // 创建测试用户
        User user = User.builder()
                .email("validation@example.com")
                .username("validationuser")
                .password(passwordEncoder.encode("password123"))
                .age(30)
                .gender(User.Gender.MALE)
                .relationshipStatus(User.RelationshipStatus.SINGLE)
                .location("Guangzhou")
                .enabled(true)
                .build();

        userRepository.save(user);

        // 测试邮箱存在性检查
        assertTrue(authService.isEmailExists("validation@example.com"));
        assertFalse(authService.isEmailExists("nonexistent@example.com"));

        // 测试用户名存在性检查
        assertTrue(authService.isUsernameExists("validationuser"));
        assertFalse(authService.isUsernameExists("nonexistentuser"));

        // 测试查找用户功能
        assertNotNull(authService.findUserByEmail("validation@example.com"));
        assertNull(authService.findUserByEmail("nonexistent@example.com"));
        assertNotNull(authService.findUserByUsername("validationuser"));
        assertNull(authService.findUserByUsername("nonexistentuser"));

        log.info("AuthService验证功能测试通过");
    }

    /**
     * 测试密码编码功能
     */
    @Test
    public void testPasswordEncoding() {
        log.info("开始测试密码编码功能");

        String rawPassword = "testPassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 验证密码编码
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        assertFalse(passwordEncoder.matches("wrongPassword", encodedPassword));

        log.info("密码编码功能测试通过");
    }
}