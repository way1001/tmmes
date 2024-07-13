package com.aiforest.tmmes.center.auth.service;

import com.aiforest.tmmes.common.entity.auth.Login;
import com.aiforest.tmmes.common.model.AuthUser;
import com.aiforest.tmmes.common.model.UserLogin;

/**
 * User Manage Service
 *
 * @author: linys
 * @since: 2023.04.02
 */
public interface AuthService {

    /**
     * 鉴定用户, 并返回token
     *
     * @param login login info
     * @return UserLogin userLogin
     */
    UserLogin authenticateUser(Login login);

    /**
     * 用户登录
     *
     * @param login 登录参数
     * @return AuthUser
     */
    AuthUser login(Login login);

    /**
     * 当前用户退出登录
     */
    void logout();
}
