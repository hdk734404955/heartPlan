package com.heartplan;

import com.heartplan.entity.User;
import com.heartplan.entity.UserPreferences;
import com.heartplan.entity.PrivacySettings;
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
 * 测试用户实体、认证服务和相关功能
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
     * 测试用户实体的创建和保存
     */
    @Test
    public void testUserEntityCreationAndSave() {
        log.info("开始测试用户实体的创建和保存");

        // 创建用户偏好设置
        UserPreferences preferences = UserPreferences.builder()
                .language(UserPreferences.Language.ENGLISH)
                .theme(UserPreferences.Theme.LIGHT)
                .pushNotifications(true)
                .emailNotifications(true)
                .build();

        // 创建隐私设置
        PrivacySettings privacySettings = PrivacySettings.builder()
                .profileVisibility(PrivacySettings.ProfileVisibility.PUBLIC)
                .allowStrangerMessages(true)
                .searchable(true)
                .build();

        // 创建用户
        User user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .password(passwordEncoder.encode("password123"))
                .age(25)
                .gender(User.Gender.MALE)
                .relationshipStatus(User.RelationshipStatus.SINGLE)
                .location("北京")
                .preferences(preferences)
                .privacySettings(privacySettings)
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
        assertEquals("北京", savedUser.getLocation());
        assertTrue(savedUser.getEnabled());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());

        // 验证嵌入式对象
        assertNotNull(savedUser.getPreferences());
        assertEquals(UserPreferences.Language.ENGLISH, savedUser.getPreferences().getLanguage());
        assertTrue(savedUser.getPreferences().getPushNotifications());

        assertNotNull(savedUser.getPrivacySettings());
        assertEquals(PrivacySettings.ProfileVisibility.PUBLIC, savedUser.getPrivacySettings().getProfileVisibility());
        assertTrue(savedUser.getPrivacySettings().getAllowStrangerMessages());

        log.info("用户实体创建和保存测试通过，用户ID: {}", savedUser.getId());
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
                .location("上海")
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