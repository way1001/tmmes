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

package com.aiforest.tmmes.common.utils;

import cn.hutool.core.util.RandomUtil;
import com.aiforest.tmmes.common.constant.common.EnvironmentConstant;
import com.aiforest.tmmes.common.constant.common.ExceptionConstant;
import com.aiforest.tmmes.common.constant.common.SymbolConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * 环境相关的工具类集合
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
public class EnvironmentUtil {

    private EnvironmentUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 获取节点 ID
     *
     * @return String Suffix
     */
    public static String getNodeId() {
        String random = RandomUtil.randomString(8);
        return random;
    }

    /**
     * 获取 Topic Tag
     * 开发环境用于区分多人开发
     *
     * @param env   环境类型
     * @param group 分组
     * @return String Tag
     */
    public static String getTag(String env, String group) {
        String exchangeTag = "";
        if (isDev(env)) {
            exchangeTag = env.toLowerCase() + SymbolConstant.UNDERSCORE + group.toLowerCase() + SymbolConstant.DOT;
        }
        return exchangeTag;
    }

    /**
     * 是否为开发环境
     *
     * @param env 环境类型
     * @return 是/否
     */
    public static boolean isDev(String env) {
        return EnvironmentConstant.ENV_DEV.equals(env);
    }

}
