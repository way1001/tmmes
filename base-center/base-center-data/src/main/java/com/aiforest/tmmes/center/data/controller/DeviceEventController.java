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

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aiforest.tmmes.center.data.entity.vo.query.DeviceEventPageQuery;
import com.aiforest.tmmes.center.data.service.EventService;
import com.aiforest.tmmes.common.constant.service.DataServiceConstant;
import com.aiforest.tmmes.common.entity.DeviceEvent;
import com.aiforest.tmmes.common.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 设备事件 Controller
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@RestController
@RequestMapping(DataServiceConstant.DEVICE_EVENT_URL_PREFIX)
public class DeviceEventController {

    @Resource
    private EventService eventService;

    /**
     * 模糊分页查询 DeviceEvent
     *
     * @param deviceEventPageQuery DeviceEventDto
     * @return Page Of DeviceEvent
     */
    @PostMapping("/device")
    public R<Page<DeviceEvent>> deviceEvent(@RequestBody(required = false) DeviceEventPageQuery deviceEventPageQuery) {
        try {
            if (ObjectUtil.isEmpty(deviceEventPageQuery)) {
                deviceEventPageQuery = new DeviceEventPageQuery();
            }
            Page<DeviceEvent> page = eventService.deviceEvent(deviceEventPageQuery);
            if (ObjectUtil.isNotNull(page)) {
                return R.ok(page);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

}