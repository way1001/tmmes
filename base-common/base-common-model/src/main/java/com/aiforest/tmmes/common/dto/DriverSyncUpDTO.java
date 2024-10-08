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

package com.aiforest.tmmes.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.aiforest.tmmes.common.model.DriverAttribute;
import com.aiforest.tmmes.common.model.DriverDO;
import com.aiforest.tmmes.common.model.PointAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author way
 * @since 2024.5.20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverSyncUpDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tenant;
    private String client;
    private DriverDO driver;
    private List<DriverAttribute> driverAttributes;
    private List<PointAttribute> pointAttributes;

}
