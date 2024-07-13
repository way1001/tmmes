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

package com.aiforest.tmmes.center.manager.entity.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.aiforest.tmmes.common.entity.common.Pages;
import com.aiforest.tmmes.common.model.LabelBind;
import lombok.*;

/**
 * LabelBind DTO
 *
 * @author way
 * @since 2024.5.20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LabelBindPageQuery extends LabelBind {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Pages page;
}