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

import com.aiforest.tmmes.common.enums.MetadataCommandTypeEnum;
import com.aiforest.tmmes.common.enums.MetadataTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 元数据
 *
 * @author way
 * @since 2024.5.20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverMetadataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 元数据类型
     */
    private MetadataTypeEnum type;

    /**
     * 元数据操作类型
     */
    private MetadataCommandTypeEnum metadataCommandType;

    /**
     * 元数据内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    public DriverMetadataDTO(MetadataTypeEnum type, MetadataCommandTypeEnum metadataCommandType, String content) {
        this.type = type;
        this.metadataCommandType = metadataCommandType;
        this.content = content;
        this.createTime = new Date();
    }
}
