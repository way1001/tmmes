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

package com.aiforest.tmmes.common.constant.cache;

import com.aiforest.tmmes.common.constant.common.ExceptionConstant;

/**
 * 超时 相关常量
 *
 * @author way
 * @since 2024.5.20
 */
public class TimeoutConstant {

    private TimeoutConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * salt 在 redis 中的失效时间，分钟
     */
    public static final int SALT_CACHE_TIMEOUT = 5;
    /**
     * user 登陆限制失效时间，分钟
     */
    public static final int USER_LIMIT_TIMEOUT = 5;
    /**
     * token 在 redis 中的失效时间，小时
     */
    public static final int TOKEN_CACHE_TIMEOUT = 12;
}
