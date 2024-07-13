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

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aiforest.tmmes.common.constant.driver.RabbitConstant;
import com.aiforest.tmmes.common.dto.DriverSyncDownDTO;
import com.aiforest.tmmes.common.dto.DriverSyncUpDTO;
import com.aiforest.tmmes.common.entity.driver.DriverMetadata;
import com.aiforest.tmmes.common.enums.DriverStatusEnum;
import com.aiforest.tmmes.common.model.DriverDO;
import com.aiforest.tmmes.common.utils.JsonUtil;
import com.aiforest.tmmes.driver.sdk.DriverContext;
import com.aiforest.tmmes.driver.sdk.entity.property.DriverProperty;
import com.aiforest.tmmes.driver.sdk.service.DriverSenderService;
import com.aiforest.tmmes.driver.sdk.service.DriverSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 驱动同步相关接口实现
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Service
public class DriverSyncServiceImpl implements DriverSyncService {

    @Resource
    private DriverContext driverContext;
    @Resource
    private DriverProperty driverProperty;

    @Resource
    private DriverSenderService driverSenderService;

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void up() {
        try {
            DriverSyncUpDTO entityDTO = buildRegisterDTOByProperty();
            log.info("The driver {} is initializing", entityDTO.getClient());
            log.debug("The driver {} initialization information is: {}", driverProperty.getService(), JsonUtil.toPrettyJsonString(entityDTO));
            rabbitTemplate.convertAndSend(
                    RabbitConstant.TOPIC_EXCHANGE_SYNC,
                    RabbitConstant.ROUTING_SYNC_UP_PREFIX + driverProperty.getClient(),
                    entityDTO
            );

            threadPoolExecutor.submit(() -> {
                while (!DriverStatusEnum.ONLINE.equals(driverContext.getDriverStatus())) {
                    ThreadUtil.sleep(500);
                }
            }).get(15, TimeUnit.SECONDS);

            log.info("The driver {} is initialized successfully.", entityDTO.getClient());
        } catch (Exception ignored) {
            log.error("The driver initialization failed, registration response timed out.");
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }

    @Override
    public void down(DriverSyncDownDTO entityDTO) {
        if (ObjectUtil.isNull(entityDTO.getContent())) {
            return;
        }

        if (CharSequenceUtil.isEmpty(entityDTO.getContent())) {
            return;
        }
        DriverMetadata driverMetadata = JsonUtil.parseObject(entityDTO.getContent(), DriverMetadata.class);
        if (ObjectUtil.isNull(driverMetadata)) {
            driverMetadata = new DriverMetadata();
        }
        driverContext.setDriverMetadata(driverMetadata);
        driverContext.setDriverStatus(DriverStatusEnum.ONLINE);
        driverMetadata.getDriverAttributeMap().values().forEach(driverAttribute -> log.info("Syncing driver attribute[{}] metadata: {}", driverAttribute.getAttributeName(), JsonUtil.toPrettyJsonString(driverAttribute)));
        driverMetadata.getPointAttributeMap().values().forEach(pointAttribute -> log.info("Syncing point attribute[{}] metadata: {}", pointAttribute.getAttributeName(), JsonUtil.toPrettyJsonString(pointAttribute)));
        driverMetadata.getDeviceMap().values().forEach(device -> log.info("Syncing device[{}] metadata: {}", device.getDeviceName(), JsonUtil.toPrettyJsonString(device)));
        log.info("The metadata synced successfully.");
    }

    /**
     * Property To DriverRegisterDTO
     *
     * @return DriverRegisterDTO
     */
    private DriverSyncUpDTO buildRegisterDTOByProperty() {
        DriverSyncUpDTO driverSyncUpDTO = new DriverSyncUpDTO();
        driverSyncUpDTO.setDriver(buildDriverByProperty());
        driverSyncUpDTO.setTenant(driverProperty.getTenant());
        driverSyncUpDTO.setClient(driverProperty.getClient());
        driverSyncUpDTO.setDriverAttributes(driverProperty.getDriverAttribute());
        driverSyncUpDTO.setPointAttributes(driverProperty.getPointAttribute());
        return driverSyncUpDTO;
    }

    /**
     * Property To Driver
     *
     * @return Driver
     */
    private DriverDO buildDriverByProperty() {
        DriverDO entityDO = new DriverDO();
        entityDO.setDriverName(driverProperty.getName());
        entityDO.setDriverCode(driverProperty.getCode());
        entityDO.setServiceName(driverProperty.getService());
        entityDO.setServiceHost(driverProperty.getHost());
        entityDO.setDriverTypeFlag(driverProperty.getType());
        entityDO.setRemark(driverProperty.getRemark());
        return entityDO;
    }

}
