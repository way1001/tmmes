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

package com.aiforest.tmmes.center.data.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.aiforest.tmmes.center.data.service.DriverEventService;
import com.aiforest.tmmes.common.constant.common.PrefixConstant;
import com.aiforest.tmmes.common.dto.DriverEventDTO;
import com.aiforest.tmmes.common.utils.JsonUtil;
import com.aiforest.tmmes.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * DriverService Impl
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Service
public class DriverEventServiceImpl implements DriverEventService {

    @Resource
    private RedisUtil redisUtil;


    @Override
    public void heartbeatEvent(DriverEventDTO entityDTO) {
        DriverEventDTO.DriverStatus driverStatus = JsonUtil.parseObject(entityDTO.getContent(), DriverEventDTO.DriverStatus.class);
        if (ObjectUtil.isNull(driverStatus)) {
            return;
        }
        redisUtil.setKey(PrefixConstant.DRIVER_STATUS_KEY_PREFIX + driverStatus.getDriverId(), driverStatus.getStatus(), 10, TimeUnit.SECONDS);
    }

}
