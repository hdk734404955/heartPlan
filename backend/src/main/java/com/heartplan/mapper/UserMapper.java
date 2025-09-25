package com.heartplan.mapper;

import com.heartplan.dto.AuthResponse;
import com.heartplan.dto.RegisterRequest;
import com.heartplan.dto.UserInfoDTO;
import com.heartplan.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * 用户映射器接口
 * 使用MapStruct自动生成User实体和DTO之间的映射代码
 * 简化对象转换逻辑，减少手动映射代码
 * 
 * @author HeartPlan Team
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * 将注册请求DTO转换为User实体
     * 用于用户注册时创建新的User对象
     * 
     * @param registerRequest 注册请求DTO
     * @return User实体对象
     */
    @Mapping(target = "password", ignore = true) // 密码需要单独加密处理
    @Mapping(target = "enabled", constant = "true") // 新注册用户默认启用
    User toEntity(RegisterRequest registerRequest);

    /**
     * 将User实体转换为UserInfoDTO
     * 用于登录和注册响应时返回用户基本信息
     * 
     * @param user User实体对象
     * @return UserInfoDTO
     */
    UserInfoDTO toUserInfo(User user);

    /**
     * 更新现有User实体的字段（排除敏感字段）
     * 用于用户信息更新时的部分字段映射
     * 
     * @param registerRequest 包含更新数据的请求DTO
     * @param user 要更新的User实体
     */
    @Mapping(target = "id", ignore = true) // 不更新ID
    @Mapping(target = "password", ignore = true) // 不更新密码
    @Mapping(target = "email", ignore = true) // 不更新邮箱
    @Mapping(target = "enabled", ignore = true) // 不更新启用状态
    @Mapping(target = "createdAt", ignore = true) // 不更新创建时间
    @Mapping(target = "updatedAt", ignore = true) // 不更新修改时间
    void updateUserFromRequest(RegisterRequest registerRequest, @MappingTarget User user);

    /**
     * 构建认证响应对象
     * 简化AuthResponse的创建过程，替代AuthResponseBuilder工具类
     * 
     * @param accessToken 访问令牌
     * @param refreshToken 刷新令牌
     * @param user 用户信息
     * @return 认证响应对象
     */
    @Mapping(target = "expiresIn", constant = "86400000L") // 24小时过期时间
    AuthResponse toAuthResponse(String accessToken, String refreshToken, UserInfoDTO user);
}