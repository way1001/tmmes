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
import com.aiforest.tmmes.api.center.manager.DeviceDTO;
import com.aiforest.tmmes.api.center.manager.RPageDeviceDTO;
import com.aiforest.tmmes.center.data.entity.dto.DevicePointValueDTO;
import com.aiforest.tmmes.center.data.entity.dto.DeviceStatusDTO;
import com.aiforest.tmmes.center.data.entity.vo.query.DevicePageQuery;
import com.aiforest.tmmes.center.data.service.DeviceStatusService;
import com.aiforest.tmmes.common.entity.common.Pages;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aiforest.tmmes.center.data.entity.vo.query.PointValuePageQuery;
import com.aiforest.tmmes.center.data.service.PointValueService;
import com.aiforest.tmmes.common.constant.service.DataServiceConstant;
import com.aiforest.tmmes.common.entity.R;
import com.aiforest.tmmes.common.entity.point.PointValue;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * PointValue Controller
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@RestController
@RequestMapping(DataServiceConstant.VALUE_URL_PREFIX)
public class PointValueController {

    @Resource
    private PointValueService pointValueService;

    @Resource
    private DeviceStatusService deviceStatusService;
    /**
     * 查询最新 PointValue 集合
     *
     * @param pointValuePageQuery 位号值和分页参数
     * @return 带分页的 {@link com.aiforest.tmmes.common.entity.point.PointValue}
     */
    @PostMapping("/latest")
    public R<Page<PointValue>> latest(@RequestBody PointValuePageQuery pointValuePageQuery) {
        try {
            if (ObjectUtil.isEmpty(pointValuePageQuery)) {
                pointValuePageQuery = new PointValuePageQuery();
            }
            Page<PointValue> page = pointValueService.latest(pointValuePageQuery);
            if (ObjectUtil.isNotNull(page)) {
                return R.ok(page);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    /**
     * 模糊分页查询 PointValue
     *
     * @param pointValuePageQuery 位号值和分页参数
     * @return 带分页的 {@link com.aiforest.tmmes.common.entity.point.PointValue}
     */
    @PostMapping("/list")
    public R<Page<PointValue>> list(@RequestBody(required = false) PointValuePageQuery pointValuePageQuery) {
        try {
            if (ObjectUtil.isEmpty(pointValuePageQuery)) {
                pointValuePageQuery = new PointValuePageQuery();
            }
            Page<PointValue> page = pointValueService.list(pointValuePageQuery);
            if (ObjectUtil.isNotNull(page)) {
                return R.ok(page);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    /**
     * 模糊设备号分页查询 PointValue
     *
     * @param devicePageQuery 设备页查询
     * @return 带分页的 {@link com.aiforest.tmmes.common.entity.point.PointValue}
     */
    @PostMapping("/idslatest")
    public R<Page<DevicePointValueDTO>> idslatest(@RequestBody(required = false) DevicePageQuery devicePageQuery) {
        try {
            Page<DevicePointValueDTO> devicePointValueDTOPage = new Page<>();
            if (ObjectUtil.isEmpty(devicePageQuery.getPage())) devicePageQuery.setPage(new Pages());
            devicePointValueDTOPage.setCurrent(devicePageQuery.getPage().getCurrent()).setSize(devicePageQuery.getPage().getSize());

            RPageDeviceDTO rPageDeviceDTO = deviceStatusService.devices(devicePageQuery);
            List<DevicePointValueDTO> devicePointValueDTOS = getDevicePointValueDTOS(rPageDeviceDTO);
            devicePointValueDTOPage.setCurrent(rPageDeviceDTO.getData().getPage().getCurrent()).setSize(rPageDeviceDTO.getData().getPage().getSize()).setTotal(rPageDeviceDTO.getData().getPage().getTotal()).setRecords(devicePointValueDTOS);

            if (ObjectUtil.isNotNull(devicePointValueDTOPage)) {
                return R.ok(devicePointValueDTOPage);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    @NotNull
    private List<DevicePointValueDTO> getDevicePointValueDTOS(RPageDeviceDTO rPageDeviceDTO) {
        List<DeviceDTO> deviceDTOList = rPageDeviceDTO.getData().getDataList();
//            Set<String> deviceIds = rPageDeviceDTO.getData().getDataList().stream().map(d -> d.getBase().getId()).collect(Collectors.toSet());
        List<DevicePointValueDTO> devicePointValueDTOS = new ArrayList<>();
        deviceDTOList.forEach(device -> {
            DevicePointValueDTO devicePointValueDTO = new DevicePointValueDTO();
            devicePointValueDTO.setDeviceId(device.getBase().getId());
            devicePointValueDTO.setDeviceCode(device.getDeviceCode());
            devicePointValueDTO.setDeviceName(device.getDeviceName());
            devicePointValueDTO.setGroupId(device.getGroupId());
            devicePointValueDTO.setRealTimePointValueDTOS(pointValueService.list(device.getBase().getId()));
            devicePointValueDTOS.add(devicePointValueDTO);
        });
        return devicePointValueDTOS;
    }

}