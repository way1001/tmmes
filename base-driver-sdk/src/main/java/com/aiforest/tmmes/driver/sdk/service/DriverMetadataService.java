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

package com.aiforest.tmmes.driver.sdk.service;

import com.aiforest.tmmes.common.dto.DriverMetadataDTO;

/**
 * 驱动元数据相关接口
 *
 * @author way
 * @since 2024.5.20
 */
public interface DriverMetadataService {

    /**
     * 模板元数据
     *
     * @param entityDTO DriverMetadataDTO
     */
    void profileMetadata(DriverMetadataDTO entityDTO);

    /**
     * 设备元数据
     *
     * @param entityDTO DriverMetadataDTO
     */
    void deviceMetadata(DriverMetadataDTO entityDTO);

    /**
     * 位号元数据
     *
     * @param entityDTO DriverMetadataDTO
     */
    void pointMetadata(DriverMetadataDTO entityDTO);

    /**
     * 驱动配置元数据
     *
     * @param entityDTO DriverMetadataDTO
     */
    void driverInfoMetadata(DriverMetadataDTO entityDTO);

    /**
     * 位号配置元数据
     *
     * @param entityDTO DriverMetadataDTO
     */
    void pointInfoMetadata(DriverMetadataDTO entityDTO);
}
