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

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.aiforest.tmmes.common.constant.common.ExceptionConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;

/**
 * 字段名称相关工具类
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
public class FieldUtil {

    private FieldUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 获取字段映射
     *
     * @param column 需要解析的 lambda 对象
     * @param <T>    对象的类型
     * @return Field Name
     */
    public static <T> String getField(SFunction<T, ?> column) {
        LambdaMeta meta = LambdaUtils.extract(column);
        return PropertyNamer.methodToProperty(meta.getImplMethodName());
    }
}
