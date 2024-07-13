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

package com.aiforest.tmmes.common.constant.driver;

import com.aiforest.tmmes.common.constant.common.ExceptionConstant;
import com.aiforest.tmmes.common.constant.common.PrefixConstant;
import com.aiforest.tmmes.common.constant.common.SuffixConstant;
import com.aiforest.tmmes.common.constant.common.SymbolConstant;

/**
 * 存储 相关常量
 *
 * @author way
 * @since 2024.5.20
 */
public class StorageConstant {

    private StorageConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 设备数据存储集合前缀
     */
    public static final String POINT_VALUE_PREFIX = PrefixConstant.POINT + SuffixConstant.VALUE + SymbolConstant.UNDERSCORE;
}
