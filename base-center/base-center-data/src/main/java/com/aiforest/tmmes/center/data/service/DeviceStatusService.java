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

package com.aiforest.tmmes.center.data.service;

import com.aiforest.tmmes.center.data.entity.dto.DeviceStatusDTO;
import com.aiforest.tmmes.center.data.entity.dto.DriverStatusDTO;
import com.aiforest.tmmes.center.data.entity.vo.query.DevicePageQuery;
import com.aiforest.tmmes.center.data.entity.vo.query.DriverPageQuery;

import java.util.List;
import java.util.Map;

/**
 * Device Interface
 *
 * @author way
 * @since 2024.5.20
 */
public interface DeviceStatusService {

    /**
     * 模糊分页查询 Device 服务状态，同设备分页查询配套使用
     *
     * @param devicePageQuery 设备和分页参数
     * @return Map String:String
     */
    Map<String, String> device(DevicePageQuery devicePageQuery);

    List<DeviceStatusDTO> deviceS(DevicePageQuery pageQuery);

    /**
     * 根据 模板ID 查询 Device 服务状态
     *
     * @param profileId Profile ID
     * @return Map String:String
     */
    Map<String, String> deviceByProfileId(String profileId);
}
