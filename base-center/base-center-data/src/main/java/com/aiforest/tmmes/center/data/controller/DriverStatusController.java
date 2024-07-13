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

import com.aiforest.tmmes.center.data.entity.dto.DriverStatusDTO;
import com.aiforest.tmmes.center.data.entity.vo.query.DriverPageQuery;
import com.aiforest.tmmes.center.data.service.DriverStatusService;
import com.aiforest.tmmes.common.constant.common.DefaultConstant;
import com.aiforest.tmmes.common.constant.common.RequestConstant;
import com.aiforest.tmmes.common.constant.service.DataServiceConstant;
import com.aiforest.tmmes.common.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@RequestMapping(DataServiceConstant.DRIVER_STATUS_URL_PREFIX)
public class DriverStatusController {

    @Resource
    private DriverStatusService driverStatusService;

    /**
     * 查询 Driver 服务状态
     * ONLINE, OFFLINE
     *
     * @param driverPageQuery 驱动和分页参数
     * @return Map String:String
     */
    @PostMapping("/driver")
    public R<Map<String, String>> driverStatus(@RequestBody(required = false) DriverPageQuery driverPageQuery, @RequestHeader(value = RequestConstant.Header.X_AUTH_TENANT_ID, defaultValue = DefaultConstant.DEFAULT_ID) String tenantId) {
        try {
            driverPageQuery.setTenantId(tenantId);
            Map<String, String> statuses = driverStatusService.driver(driverPageQuery);
            return R.ok(statuses);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @PostMapping("/drivers")
    public R<List<DriverStatusDTO>> driverStatusS(@RequestBody(required = false) DriverPageQuery driverPageQuery, @RequestHeader(value = RequestConstant.Header.X_AUTH_TENANT_ID, defaultValue = DefaultConstant.DEFAULT_ID) String tenantId) {
        try {
            driverPageQuery.setTenantId(tenantId);
            List<DriverStatusDTO>  statuses = driverStatusService.driverS(driverPageQuery);
            return R.ok(statuses);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

}
