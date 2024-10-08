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

package com.aiforest.tmmes.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用实体类型标识枚举
 *
 * @author way
 * @since 2024.5.20
 */
@Getter
@AllArgsConstructor
public enum EntityTypeFlagEnum {
    /**
     * 系统
     */
    SYSTEM("system", "系统"),

    /**
     * 用户
     */
    USER("user", "用户"),

    /**
     * 分组
     */
    GROUP("group", "分组"),

    /**
     * 驱动
     */
    DRIVER("driver", "驱动"),

    /**
     * 模板
     */
    PROFILE("profile", "模板"),

    /**
     * 位号
     */
    POINT("point", "位号"),

    /**
     * 设备
     */
    DEVICE("device", "设备"),
    ;

    /**
     * 编码
     */
    private final String code;

    /**
     * 备注
     */
    private final String remark;

    /**
     * 根据 Code 获取枚举
     *
     * @param code Code
     * @return EntityTypeFlagEnum
     */
    public static EntityTypeFlagEnum ofCode(String code) {
        Optional<EntityTypeFlagEnum> any = Arrays.stream(EntityTypeFlagEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return EntityTypeFlagEnum
     */
    public static EntityTypeFlagEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
