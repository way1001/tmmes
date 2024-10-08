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

package com.aiforest.tmmes.common.entity.driver;

import com.aiforest.tmmes.common.enums.AttributeTypeFlagEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 属性配置
 *
 * @author way
 * @since 2024.5.20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeInfo {
    /**
     * 值，string，需要通过type确定真实的数据类型
     */
    private String value;

    /**
     * 类型，value type，用于确定value的真实类型
     */
    private AttributeTypeFlagEnum type;
}
