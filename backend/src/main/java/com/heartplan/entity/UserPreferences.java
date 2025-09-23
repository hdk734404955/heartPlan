package com.heartplan.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户偏好设置嵌入式对象
 * 使用Lombok注解简化代码，存储用户的个性化偏好配置
 * 作为嵌入式对象存储在User实体中
 * 
 * @author HeartPlan Team
 */
@Embeddable // JPA注解：标记为可嵌入对象
@Data // Lombok注解：自动生成getter、setter、toString、equals、hashCode方法
@Builder // Lombok注解：自动生成建造者模式
@NoArgsConstructor // Lombok注解：自动生成无参构造函数
@AllArgsConstructor // Lombok注解：自动生成全参构造函数
public class UserPreferences {

    /**
     * 语言偏好
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "pref_language", length = 10)
    @Builder.Default
    private Language language = Language.ENGLISH;

    /**
     * 主题偏好
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "pref_theme", length = 10)
    @Builder.Default
    private Theme theme = Theme.LIGHT;

    /**
     * 是否接收推送通知
     */
    @Column(name = "pref_push_notifications")
    @Builder.Default
    private Boolean pushNotifications = true;

    /**
     * 是否接收邮件通知
     */
    @Column(name = "pref_email_notifications")
    @Builder.Default
    private Boolean emailNotifications = true;

    /**
     * 是否接收社区通知
     */
    @Column(name = "pref_community_notifications")
    @Builder.Default
    private Boolean communityNotifications = true;

    /**
     * 是否接收聊天通知
     */
    @Column(name = "pref_chat_notifications")
    @Builder.Default
    private Boolean chatNotifications = true;

    /**
     * 是否接收AI教练通知
     */
    @Column(name = "pref_ai_coach_notifications")
    @Builder.Default
    private Boolean aiCoachNotifications = true;

    /**
     * 是否启用声音提醒
     */
    @Column(name = "pref_sound_enabled")
    @Builder.Default
    private Boolean soundEnabled = true;

    /**
     * 是否启用震动提醒
     */
    @Column(name = "pref_vibration_enabled")
    @Builder.Default
    private Boolean vibrationEnabled = true;

    /**
     * 推荐内容偏好
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "pref_recommendation_type", length = 20)
    @Builder.Default
    private RecommendationType recommendationType = RecommendationType.BALANCED;

    /**
     * 是否显示在线状态
     */
    @Column(name = "pref_show_online_status")
    @Builder.Default
    private Boolean showOnlineStatus = true;

    /**
     * 是否自动保存聊天记录
     */
    @Column(name = "pref_auto_save_chat")
    @Builder.Default
    private Boolean autoSaveChat = true;

    /**
     * 语言偏好枚举
     */
    public enum Language {
        ENGLISH("English"),
        CHINESE("中文");

        private final String displayName;

        Language(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * 主题偏好枚举
     */
    public enum Theme {
        LIGHT("浅色主题"),
        DARK("深色主题"),
        AUTO("自动切换");

        private final String displayName;

        Theme(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * 推荐类型枚举
     */
    public enum RecommendationType {
        CONSERVATIVE("保守推荐"),
        BALANCED("平衡推荐"),
        AGGRESSIVE("积极推荐");

        private final String displayName;

        RecommendationType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}