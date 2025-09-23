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
 * 隐私设置嵌入式对象
 * 使用Lombok注解简化代码，存储用户的隐私和安全相关设置
 * 作为嵌入式对象存储在User实体中
 * 
 * @author HeartPlan Team
 */
@Embeddable // JPA注解：标记为可嵌入对象
@Data // Lombok注解：自动生成getter、setter、toString、equals、hashCode方法
@Builder // Lombok注解：自动生成建造者模式
@NoArgsConstructor // Lombok注解：自动生成无参构造函数
@AllArgsConstructor // Lombok注解：自动生成全参构造函数
public class PrivacySettings {

    /**
     * 个人资料可见性
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_profile_visibility", length = 15)
    @Builder.Default
    private ProfileVisibility profileVisibility = ProfileVisibility.PUBLIC;

    /**
     * 在线状态可见性
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_online_status_visibility", length = 15)
    @Builder.Default
    private OnlineStatusVisibility onlineStatusVisibility = OnlineStatusVisibility.FRIENDS;

    /**
     * 是否允许陌生人发送消息
     */
    @Column(name = "privacy_allow_stranger_messages")
    @Builder.Default
    private Boolean allowStrangerMessages = true;

    /**
     * 是否允许搜索到个人资料
     */
    @Column(name = "privacy_searchable")
    @Builder.Default
    private Boolean searchable = true;

    /**
     * 是否显示最后活跃时间
     */
    @Column(name = "privacy_show_last_active")
    @Builder.Default
    private Boolean showLastActive = true;

    /**
     * 是否显示阅读回执
     */
    @Column(name = "privacy_show_read_receipts")
    @Builder.Default
    private Boolean showReadReceipts = true;

    /**
     * 是否允许数据分析
     */
    @Column(name = "privacy_allow_analytics")
    @Builder.Default
    private Boolean allowAnalytics = true;

    /**
     * 是否允许个性化推荐
     */
    @Column(name = "privacy_allow_personalization")
    @Builder.Default
    private Boolean allowPersonalization = true;

    /**
     * 是否允许第三方数据共享
     */
    @Column(name = "privacy_allow_third_party_sharing")
    @Builder.Default
    private Boolean allowThirdPartySharing = false;

    /**
     * 数据保留期限（天数）
     */
    @Column(name = "privacy_data_retention_days")
    @Builder.Default
    private Integer dataRetentionDays = 365;

    /**
     * 是否启用两步验证
     */
    @Column(name = "privacy_two_factor_enabled")
    @Builder.Default
    private Boolean twoFactorEnabled = false;

    /**
     * 是否记住登录设备
     */
    @Column(name = "privacy_remember_devices")
    @Builder.Default
    private Boolean rememberDevices = true;

    /**
     * 个人资料可见性枚举
     */
    public enum ProfileVisibility {
        PUBLIC("公开"),
        FRIENDS("仅好友"),
        PRIVATE("私密");

        private final String displayName;

        ProfileVisibility(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * 在线状态可见性枚举
     */
    public enum OnlineStatusVisibility {
        EVERYONE("所有人"),
        FRIENDS("仅好友"),
        NOBODY("不显示");

        private final String displayName;

        OnlineStatusVisibility(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}