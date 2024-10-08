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
 * 驱动状态枚举
 *
 * @author way
 * @since 2024.5.20
 */
@Getter
@AllArgsConstructor
public enum DriverStatusEnum {
    ONLINE("ONLINE", "在线"),
    OFFLINE("OFFLINE", "离线"),
    MAINTAIN("MAINTAIN", "维护"),
    FAULT("FAULT", "故障"),
    ;


    /**
     * 状态编码
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
     * @return StatusEnum
     */
    public static DriverStatusEnum ofCode(String code) {
        Optional<DriverStatusEnum> any = Arrays.stream(DriverStatusEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return DriverStatusEnum
     */
    public static DriverStatusEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
