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

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aiforest.tmmes.api.center.manager.*;
import com.aiforest.tmmes.api.common.EnableFlagDTOEnum;
import com.aiforest.tmmes.api.common.PageDTO;
import com.aiforest.tmmes.center.data.entity.dto.DeviceStatusDTO;
import com.aiforest.tmmes.center.data.entity.dto.DriverStatusDTO;
import com.aiforest.tmmes.center.data.entity.vo.query.DevicePageQuery;
import com.aiforest.tmmes.center.data.entity.vo.query.DriverPageQuery;
import com.aiforest.tmmes.center.data.service.DeviceStatusService;
import com.aiforest.tmmes.common.constant.common.DefaultConstant;
import com.aiforest.tmmes.common.constant.common.PrefixConstant;
import com.aiforest.tmmes.common.constant.service.ManagerServiceConstant;
import com.aiforest.tmmes.common.enums.DeviceStatusEnum;
import com.aiforest.tmmes.common.enums.DriverStatusEnum;
import com.aiforest.tmmes.common.utils.RedisUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DeviceService Impl
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Service
public class DeviceStatusServiceImpl implements DeviceStatusService {

    @GrpcClient(ManagerServiceConstant.SERVICE_NAME)
    private DeviceApiGrpc.DeviceApiBlockingStub deviceApiBlockingStub;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public Map<String, String> device(DevicePageQuery pageQuery) {
        PageDTO.Builder page = PageDTO.newBuilder()
                .setSize(pageQuery.getPage().getSize())
                .setCurrent(pageQuery.getPage().getCurrent());
        DeviceDTO.Builder builder = buildDTOByQuery(pageQuery);
        PageDeviceQueryDTO.Builder query = PageDeviceQueryDTO.newBuilder()
                .setPage(page)
                .setDevice(builder);
        if (CharSequenceUtil.isNotEmpty(pageQuery.getProfileId())) {
            query.setProfileId(pageQuery.getProfileId());
        }
        RPageDeviceDTO rPageDeviceDTO = deviceApiBlockingStub.list(query.build());

        if (!rPageDeviceDTO.getResult().getOk()) {
            return new HashMap<>();
        }

        List<DeviceDTO> devices = rPageDeviceDTO.getData().getDataList();
        return getStatusMap(devices);
    }

    @Override
    public List<DeviceStatusDTO> deviceS(DevicePageQuery pageQuery) {
        PageDTO.Builder page = PageDTO.newBuilder()
                .setSize(pageQuery.getPage().getSize())
                .setCurrent(pageQuery.getPage().getCurrent());
        DeviceDTO.Builder builder = buildDTOByQuery(pageQuery);
        PageDeviceQueryDTO.Builder query = PageDeviceQueryDTO.newBuilder()
                .setPage(page)
                .setDevice(builder);
        RPageDeviceDTO rPageDeviceDTO = deviceApiBlockingStub.list(query.build());

        if (!rPageDeviceDTO.getResult().getOk()) {
            return new ArrayList<>(16);
        }

        List<DeviceDTO> drivers = rPageDeviceDTO.getData().getDataList();
        return getStatusList(drivers);
    }

    @Override
    public RPageDeviceDTO devices(DevicePageQuery pageQuery) {
        PageDTO.Builder page = PageDTO.newBuilder()
                .setSize(pageQuery.getPage().getSize())
                .setCurrent(pageQuery.getPage().getCurrent());
        DeviceDTO.Builder builder = buildDTOByQuery(pageQuery);
        PageDeviceQueryDTO.Builder query = PageDeviceQueryDTO.newBuilder()
                .setPage(page)
                .setDevice(builder);
        RPageDeviceDTO rPageDeviceDTO = deviceApiBlockingStub.list(query.build());

        if (!rPageDeviceDTO.getResult().getOk()) {
            return rPageDeviceDTO;
        }

        return rPageDeviceDTO;
    }

    @Override
    public Map<String, String> deviceByProfileId(String profileId) {
        ByProfileQueryDTO query = ByProfileQueryDTO.newBuilder()
                .setProfileId(profileId)
                .build();
        RDeviceListDTO rDeviceListDTO = deviceApiBlockingStub.selectByProfileId(query);
        if (!rDeviceListDTO.getResult().getOk()) {
            return new HashMap<>();
        }

        List<DeviceDTO> devices = rDeviceListDTO.getDataList();
        return getStatusMap(devices);
    }

    /**
     * Query to DTO
     *
     * @param pageQuery DevicePageQuery
     * @return DeviceDTO Builder
     */
    private static DeviceDTO.Builder buildDTOByQuery(DevicePageQuery pageQuery) {
        DeviceDTO.Builder builder = DeviceDTO.newBuilder();
        if (CharSequenceUtil.isNotEmpty(pageQuery.getDeviceName())) {
            builder.setDeviceName(pageQuery.getDeviceName());
        }
        if (CharSequenceUtil.isNotEmpty(pageQuery.getDriverId())) {
            builder.setDriverId(pageQuery.getDriverId());
        }
        if (ObjectUtil.isNotNull(pageQuery.getEnableFlag())) {
            builder.setEnableFlag(EnableFlagDTOEnum.valueOf(pageQuery.getEnableFlag().name()));
        } else {
            builder.setEnableFlagValue(DefaultConstant.DEFAULT_INT);
        }
        if (CharSequenceUtil.isNotEmpty(pageQuery.getTenantId())) {
            builder.setTenantId(pageQuery.getTenantId());
        }
        return builder;
    }

    /**
     * Get status map
     *
     * @param devices DeviceDTO Array
     * @return Status Map
     */
    private Map<String, String> getStatusMap(List<DeviceDTO> devices) {
        Map<String, String> statusMap = new HashMap<>(16);
        Set<String> deviceIds = devices.stream().map(d -> d.getBase().getId()).collect(Collectors.toSet());
        deviceIds.forEach(id -> {
            String key = PrefixConstant.DEVICE_STATUS_KEY_PREFIX + id;
            String status = redisUtil.getKey(key);
            status = ObjectUtil.isNotNull(status) ? status : DeviceStatusEnum.OFFLINE.getCode();
            statusMap.put(id, status);
        });
        return statusMap;
    }

    private List<DeviceStatusDTO> getStatusList(List<DeviceDTO> devices) {
        List<DeviceStatusDTO> deviceStatusDTOList = new ArrayList<>(16);
        Set<String> deviceIds = devices.stream().map(d -> d.getBase().getId()).collect(Collectors.toSet());
        deviceIds.forEach(id -> {
            String key = PrefixConstant.DEVICE_STATUS_KEY_PREFIX + id;
            String status = redisUtil.getKey(key);
            status = ObjectUtil.isNotNull(status) ? status : DeviceStatusEnum.OFFLINE.getCode();
            DeviceStatusDTO deviceStatusDTO = new DeviceStatusDTO();
            deviceStatusDTO.setId(id);
            deviceStatusDTO.setStatus(status);
            deviceStatusDTOList.add(deviceStatusDTO);
        });
        return deviceStatusDTOList;
    }

}
