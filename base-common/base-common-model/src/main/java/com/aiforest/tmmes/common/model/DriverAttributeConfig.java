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

package com.aiforest.tmmes.common.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.aiforest.tmmes.common.entity.base.Base;
import com.aiforest.tmmes.common.enums.EnableFlagEnum;
import com.aiforest.tmmes.common.valid.Insert;
import com.aiforest.tmmes.common.valid.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 驱动属性配置表
 *
 * @author way
 * @since 2024.5.20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@TableName("mes_driver_attribute_config")
public class DriverAttributeConfig extends Base {

    /**
     * 连接配置ID
     */
    @NotBlank(message = "Driver attribute id can't be empty",
            groups = {Insert.class, Update.class})
    private String driverAttributeId;

    /**
     * 连接配置值
     */
    @NotNull(message = "Driver config value can't be empty")
    private String configValue;

    /**
     * 设备ID
     */
    @NotBlank(message = "Device id can't be empty",
            groups = {Insert.class, Update.class})
    private String deviceId;

    /**
     * 使能标识
     */
    private EnableFlagEnum enableFlag;

    /**
     * 租户ID
     */
    private String tenantId;
}
