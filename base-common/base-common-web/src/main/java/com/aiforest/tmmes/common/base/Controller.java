/*
 * Copyright 2016-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aiforest.tmmes.common.base;

import com.aiforest.tmmes.common.entity.bo.AuthInfoBO;
import com.aiforest.tmmes.common.utils.RequestUtil;

/**
 * 基础 Controller 类接口
 *
 * @author way
 * @since 2024.5.20
 */
public interface Controller {

    /**
     * Get Auth Info
     *
     * @return Auth Info {@link AuthInfoBO}
     */
    default AuthInfoBO getAuthInfo() {
        return RequestUtil.getAuthInfo();
    }

    /**
     * Get Tenant Id
     *
     * @return Tenant ID
     */
    default String getTenantId() {
        return getAuthInfo().getTenantId();
    }

    /**
     * Get Tenant Name
     *
     * @return Tenant Name
     */
    default String getTenantName() {
        return RequestUtil.getAuthInfo().getTenantName();
    }

    /**
     * Get User Id
     *
     * @return User ID
     */
    default String getUserId() {
        return RequestUtil.getAuthInfo().getUserId();
    }

    /**
     * Get Nick Name
     *
     * @return Nick Name
     */
    default String getNickName() {
        return RequestUtil.getAuthInfo().getNickName();
    }

    /**
     * Get User Name
     *
     * @return User Name
     */
    default String getUserName() {
        return RequestUtil.getAuthInfo().getUserName();
    }
}
