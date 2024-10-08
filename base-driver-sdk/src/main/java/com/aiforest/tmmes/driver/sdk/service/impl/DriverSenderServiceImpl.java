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

package com.aiforest.tmmes.driver.sdk.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.aiforest.tmmes.common.constant.driver.EventConstant;
import com.aiforest.tmmes.common.constant.driver.RabbitConstant;
import com.aiforest.tmmes.common.dto.DriverEventDTO;
import com.aiforest.tmmes.common.entity.DeviceEvent;
import com.aiforest.tmmes.common.entity.point.PointValue;
import com.aiforest.tmmes.common.enums.DeviceStatusEnum;
import com.aiforest.tmmes.common.utils.JsonUtil;
import com.aiforest.tmmes.driver.sdk.entity.property.DriverProperty;
import com.aiforest.tmmes.driver.sdk.service.DriverSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Service
public class DriverSenderServiceImpl implements DriverSenderService {

    @Resource
    private DriverProperty driverProperty;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void driverEventSender(DriverEventDTO entityDTO) {
        if (ObjectUtil.isNull(entityDTO)) {
            return;
        }

        rabbitTemplate.convertAndSend(
                RabbitConstant.TOPIC_EXCHANGE_EVENT,
                RabbitConstant.ROUTING_DRIVER_EVENT_PREFIX + driverProperty.getService(),
                entityDTO
        );
    }

    @Override
    public void deviceEventSender(DeviceEvent deviceEvent) {
        if (ObjectUtil.isNotNull(deviceEvent)) {
            rabbitTemplate.convertAndSend(
                    RabbitConstant.TOPIC_EXCHANGE_EVENT,
                    RabbitConstant.ROUTING_DEVICE_EVENT_PREFIX + driverProperty.getService(),
                    deviceEvent
            );
        }
    }

    @Override
    public void deviceStatusSender(String deviceId, DeviceStatusEnum status) {
        deviceEventSender(new DeviceEvent(deviceId, EventConstant.Device.STATUS, status));
    }

    @Override
    public void pointValueSender(PointValue pointValue) {
        if (ObjectUtil.isNotNull(pointValue)) {
            log.debug("Send point value: {}", JsonUtil.toJsonString(pointValue));
            rabbitTemplate.convertAndSend(
                    RabbitConstant.TOPIC_EXCHANGE_VALUE,
                    RabbitConstant.ROUTING_POINT_VALUE_PREFIX + driverProperty.getService(),
                    pointValue
            );
        }
    }

    @Override
    public void pointValueSender(List<PointValue> pointValues) {
        if (ObjectUtil.isNotNull(pointValues)) {
            pointValues.forEach(this::pointValueSender);
        }
    }

}
