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
import com.aiforest.tmmes.common.valid.Auth;
import com.aiforest.tmmes.common.valid.Insert;
import com.aiforest.tmmes.common.valid.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 角色表
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
@TableName("mes_role")
public class Role extends Base {

    /**
     * 角色父级ID
     */
    @NotBlank(message = "Role parent id can't be empty",
            groups = {Insert.class, Update.class})
    private String parentRoleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "Role name can't be empty",
            groups = {Insert.class, Auth.class})
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9-_#@/.|]{1,31}$",
            message = "Invalid role name",
            groups = {Insert.class, Update.class})
    private String roleName;

    /**
     * 角色编号
     */
    private String roleCode;

    /**
     * 使能标识
     */
    private EnableFlagEnum enableFlag;

    /**
     * 租户ID
     */
    private String tenantId;
}
