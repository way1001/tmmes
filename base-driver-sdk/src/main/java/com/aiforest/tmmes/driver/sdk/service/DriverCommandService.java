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

import com.aiforest.tmmes.common.dto.DeviceCommandDTO;
import com.aiforest.tmmes.common.entity.point.PointValue;

/**
 * 驱动指令服务
 *
 * @author way
 * @since 2024.5.20
 */
public interface DriverCommandService {

    /**
     * 读取位号值
     *
     * @param deviceId 设备ID
     * @param pointId  位号ID
     * @return 位号值
     */
    PointValue read(String deviceId, String pointId);

    /**
     * 指令读取位号值
     *
     * @param commandDTO {@link DeviceCommandDTO}
     */
    void read(DeviceCommandDTO commandDTO);

    /**
     * 写取位号值
     *
     * @param deviceId 设备ID
     * @param pointId  位号ID
     * @param value    位号值
     * @return 是否写成功
     */
    Boolean write(String deviceId, String pointId, String value);

    /**
     * 指令写取位号值
     *
     * @param commandDTO {@link  DeviceCommandDTO}
     */
    void write(DeviceCommandDTO commandDTO);

}
