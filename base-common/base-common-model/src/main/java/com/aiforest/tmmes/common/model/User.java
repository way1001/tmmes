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
import com.aiforest.tmmes.common.valid.Auth;
import com.aiforest.tmmes.common.valid.Insert;
import com.aiforest.tmmes.common.valid.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * User
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
@TableName("mes_user")
public class User extends Base {

    /**
     * 用户昵称
     */
    @NotBlank(message = "Nick name can't be empty",
            groups = {Insert.class, Auth.class})
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_#@/.|]{1,31}$",
            message = "Invalid nick name",
            groups = {Insert.class, Update.class})
    private String nickName;

    /**
     * 用户名称
     */
    @NotBlank(message = "User name can't be empty",
            groups = {Insert.class, Auth.class})
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9-_#@/.|]{1,31}$",
            message = "Invalid user name",
            groups = {Insert.class, Update.class})
    private String userName;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1([3-9])\\d{9}$",
            message = "Invalid phone",
            groups = {Insert.class, Update.class})
    private String phone;

    /**
     * 邮箱
     */
    @Pattern(regexp = "^[A-Za-z0-9_.-]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+$",
            message = "Invalid email",
            groups = {Insert.class, Update.class})
    private String email;

    /**
     * 社交相关拓展信息
     */
    private String socialExt;

    /**
     * 身份相关拓展信息
     */
    private String identityExt;
}
