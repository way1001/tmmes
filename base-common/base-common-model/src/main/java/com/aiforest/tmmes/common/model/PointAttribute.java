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
import com.aiforest.tmmes.common.enums.AttributeTypeFlagEnum;
import com.aiforest.tmmes.common.enums.EnableFlagEnum;
import com.aiforest.tmmes.common.valid.Insert;
import com.aiforest.tmmes.common.valid.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 模板配置信息表
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
@TableName("mes_point_attribute")
public class PointAttribute extends Base {

    /**
     * 显示名称
     */
    @NotBlank(message = "Display name can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_#@/.|]{1,31}$",
            message = "Invalid display name",
            groups = {Insert.class, Update.class})
    private String displayName;

    /**
     * 属性名称
     */
    @NotBlank(message = "Attribute name can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9-_#@/.|]{1,31}$",
            message = "Invalid attribute name",
            groups = {Insert.class, Update.class})
    private String attributeName;

    /**
     * 属性类型标识
     */
    private AttributeTypeFlagEnum attributeTypeFlag;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 驱动ID
     */
    @NotNull(message = "Driver id can't be empty"
            , groups = {Insert.class, Update.class})
    private String driverId;

    /**
     * 使能标识
     */
    private EnableFlagEnum enableFlag;

    /**
     * 租户ID
     */
    private String tenantId;
}
