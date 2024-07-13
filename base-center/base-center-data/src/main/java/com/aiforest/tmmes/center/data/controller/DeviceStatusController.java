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

package com.aiforest.tmmes.center.data.controller;

import com.aiforest.tmmes.center.data.entity.dto.DeviceStatusDTO;
import com.aiforest.tmmes.center.data.entity.dto.DriverStatusDTO;
import com.aiforest.tmmes.center.data.entity.vo.query.DevicePageQuery;
import com.aiforest.tmmes.center.data.service.DeviceStatusService;
import com.aiforest.tmmes.common.constant.common.DefaultConstant;
import com.aiforest.tmmes.common.constant.common.RequestConstant;
import com.aiforest.tmmes.common.constant.service.DataServiceConstant;
import com.aiforest.tmmes.common.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 设备 Controller
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@RestController
@RequestMapping(DataServiceConstant.DEVICE_STATUS_URL_PREFIX)
public class DeviceStatusController {

    @Resource
    private DeviceStatusService deviceStatusService;

    /**
     * 查询 Device 服务状态
     * ONLINE, OFFLINE, MAINTAIN, FAULT
     *
     * @param devicePageQuery Device Dto
     * @return Map String:String
     */
    @PostMapping("/device")
    public R<Map<String, String>> deviceStatus(@RequestBody(required = false) DevicePageQuery devicePageQuery, @RequestHeader(value = RequestConstant.Header.X_AUTH_TENANT_ID, defaultValue = DefaultConstant.DEFAULT_ID) String tenantId) {
        try {
            devicePageQuery.setTenantId(tenantId);
            Map<String, String> statuses = deviceStatusService.device(devicePageQuery);
            return R.ok(statuses);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @PostMapping("/devices")
    public R<List<DeviceStatusDTO>> deviceStatusS(@RequestBody(required = false) DevicePageQuery devicePageQuery, @RequestHeader(value = RequestConstant.Header.X_AUTH_TENANT_ID, defaultValue = DefaultConstant.DEFAULT_ID) String tenantId) {
        try {
            devicePageQuery.setTenantId(tenantId);
            List<DeviceStatusDTO> statuses = deviceStatusService.deviceS(devicePageQuery);
            return R.ok(statuses);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 根据 驱动ID 查询 Device 服务状态
     * ONLINE, OFFLINE, MAINTAIN, FAULT
     *
     * @param driverId Driver ID
     * @return Map String:String
     */
    @GetMapping("/device/driver_id/{driverId}")
    public R<Map<String, String>> deviceStatusByDriverId(@NotNull @PathVariable(value = "driverId") String driverId) {
        try {
            DevicePageQuery devicePageQuery = new DevicePageQuery();
            devicePageQuery.setDriverId(driverId);
            Map<String, String> statuses = deviceStatusService.device(devicePageQuery);
            return R.ok(statuses);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 根据 模板ID 查询 Device 服务状态
     * ONLINE, OFFLINE, MAINTAIN, FAULT
     *
     * @param profileId Profile ID
     * @return Map String:String
     */
    @GetMapping("/device/profile_id/{profileId}")
    public R<Map<String, String>> deviceStatusByProfileId(@NotNull @PathVariable(value = "profileId") String profileId) {
        try {
            Map<String, String> statuses = deviceStatusService.deviceByProfileId(profileId);
            return R.ok(statuses);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

}
