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

import com.aiforest.tmmes.common.dto.DriverEventDTO;
import com.aiforest.tmmes.common.entity.DeviceEvent;
import com.aiforest.tmmes.common.entity.point.PointValue;
import com.aiforest.tmmes.common.enums.DeviceStatusEnum;

import java.util.List;

/**
 * @author way
 * @since 2024.5.20
 */
public interface DriverSenderService {

    /**
     * 发送驱动事件
     *
     * @param entityDTO DriverEventDTO
     */
    void driverEventSender(DriverEventDTO entityDTO);

    /**
     * 发送设备事件
     *
     * @param deviceEvent Device Event
     */
    void deviceEventSender(DeviceEvent deviceEvent);

    /**
     * 发送设备状态事件
     *
     * @param deviceId 设备ID
     * @param status   StatusEnum
     */
    void deviceStatusSender(String deviceId, DeviceStatusEnum status);

    /**
     * 发送位号值到消息组件
     *
     * @param pointValue PointValue
     */
    void pointValueSender(PointValue pointValue);

    /**
     * 批量发送位号值到消息组件
     *
     * @param pointValues PointValue Array
     */
    void pointValueSender(List<PointValue> pointValues);

}
