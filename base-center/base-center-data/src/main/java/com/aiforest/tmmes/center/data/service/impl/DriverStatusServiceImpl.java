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
import com.aiforest.tmmes.api.center.manager.DriverApiGrpc;
import com.aiforest.tmmes.api.center.manager.DriverDTO;
import com.aiforest.tmmes.api.center.manager.PageDriverQueryDTO;
import com.aiforest.tmmes.api.center.manager.RPageDriverDTO;
import com.aiforest.tmmes.api.common.DriverTypeFlagDTOEnum;
import com.aiforest.tmmes.api.common.EnableFlagDTOEnum;
import com.aiforest.tmmes.api.common.PageDTO;
import com.aiforest.tmmes.center.data.entity.dto.DriverStatusDTO;
import com.aiforest.tmmes.center.data.entity.vo.query.DriverPageQuery;
import com.aiforest.tmmes.center.data.service.DriverStatusService;
import com.aiforest.tmmes.common.constant.common.DefaultConstant;
import com.aiforest.tmmes.common.constant.common.PrefixConstant;
import com.aiforest.tmmes.common.constant.service.ManagerServiceConstant;
import com.aiforest.tmmes.common.enums.DriverStatusEnum;
import com.aiforest.tmmes.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DriverService Impl
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Service
public class DriverStatusServiceImpl implements DriverStatusService {

    @GrpcClient(ManagerServiceConstant.SERVICE_NAME)
    private DriverApiGrpc.DriverApiBlockingStub driverApiBlockingStub;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public Map<String, String> driver(DriverPageQuery pageQuery) {
        PageDTO.Builder page = PageDTO.newBuilder()
                .setSize(pageQuery.getPage().getSize())
                .setCurrent(pageQuery.getPage().getCurrent());
        DriverDTO.Builder builder = buildDTOByQuery(pageQuery);
        PageDriverQueryDTO.Builder query = PageDriverQueryDTO.newBuilder()
                .setPage(page)
                .setDriver(builder);
        RPageDriverDTO rPageDriverDTO = driverApiBlockingStub.list(query.build());

        if (!rPageDriverDTO.getResult().getOk()) {
            return new HashMap<>();
        }

        List<DriverDTO> drivers = rPageDriverDTO.getData().getDataList();
        return getStatusMap(drivers);
    }

    @Override
    public List<DriverStatusDTO> driverS(DriverPageQuery pageQuery) {
        PageDTO.Builder page = PageDTO.newBuilder()
                .setSize(pageQuery.getPage().getSize())
                .setCurrent(pageQuery.getPage().getCurrent());
        DriverDTO.Builder builder = buildDTOByQuery(pageQuery);
        PageDriverQueryDTO.Builder query = PageDriverQueryDTO.newBuilder()
                .setPage(page)
                .setDriver(builder);
        RPageDriverDTO rPageDriverDTO = driverApiBlockingStub.list(query.build());

        if (!rPageDriverDTO.getResult().getOk()) {
            return new ArrayList<>(16);
        }

        List<DriverDTO> drivers = rPageDriverDTO.getData().getDataList();
        return getStatusList(drivers);
    }

    /**
     * Query to DTO
     *
     * @param pageQuery DriverPageQuery
     * @return DriverDTO Builder
     */
    private static DriverDTO.Builder buildDTOByQuery(DriverPageQuery pageQuery) {
        DriverDTO.Builder builder = DriverDTO.newBuilder();
        if (CharSequenceUtil.isNotEmpty(pageQuery.getDriverName())) {
            builder.setDriverName(pageQuery.getDriverName());
        }
        if (CharSequenceUtil.isNotEmpty(pageQuery.getServiceName())) {
            builder.setServiceName(pageQuery.getServiceName());
        }
        if (CharSequenceUtil.isNotEmpty(pageQuery.getServiceHost())) {
            builder.setServiceHost(pageQuery.getServiceHost());
        }
        if (ObjectUtil.isNotNull(pageQuery.getDriverTypeFlag())) {
            builder.setDriverTypeFlag(DriverTypeFlagDTOEnum.valueOf(pageQuery.getDriverTypeFlag().name()));
        } else {
            builder.setDriverTypeFlagValue(DefaultConstant.DEFAULT_INT);
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
     * @param drivers DriverDTO Array
     * @return Status Map
     */
    private Map<String, String> getStatusMap(List<DriverDTO> drivers) {
        Map<String, String> statusMap = new HashMap<>(16);
        Set<String> driverIds = drivers.stream().map(d -> d.getBase().getId()).collect(Collectors.toSet());
        driverIds.forEach(id -> {
            String key = PrefixConstant.DRIVER_STATUS_KEY_PREFIX + id;
            String status = redisUtil.getKey(key);
            status = ObjectUtil.isNotNull(status) ? status : DriverStatusEnum.OFFLINE.getCode();
            statusMap.put(id, status);
        });
        return statusMap;
    }

    private List<DriverStatusDTO> getStatusList(List<DriverDTO> drivers) {
        List<DriverStatusDTO> driverStatusDTOList = new ArrayList<>(16);
        Set<String> driverIds = drivers.stream().map(d -> d.getBase().getId()).collect(Collectors.toSet());
        driverIds.forEach(id -> {
            String key = PrefixConstant.DRIVER_STATUS_KEY_PREFIX + id;
            String status = redisUtil.getKey(key);
            status = ObjectUtil.isNotNull(status) ? status : DriverStatusEnum.OFFLINE.getCode();
            DriverStatusDTO driverStatusDTO = new DriverStatusDTO();
            driverStatusDTO.setId(id);
            driverStatusDTO.setStatus(status);
            driverStatusDTOList.add(driverStatusDTO);
        });
        return driverStatusDTOList;
    }

}
