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

package com.aiforest.tmmes.center.manager.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aiforest.tmmes.center.manager.service.PointAttributeConfigService;
import com.aiforest.tmmes.center.manager.service.PointAttributeService;
import com.aiforest.tmmes.common.model.PointAttribute;
import com.aiforest.tmmes.common.model.PointAttributeConfig;
import com.aiforest.tmmes.common.model.RoleResourceBind;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aiforest.tmmes.center.manager.entity.query.PointPageQuery;
import com.aiforest.tmmes.center.manager.service.PointService;
import com.aiforest.tmmes.common.constant.common.DefaultConstant;
import com.aiforest.tmmes.common.constant.common.RequestConstant;
import com.aiforest.tmmes.common.constant.service.ManagerServiceConstant;
import com.aiforest.tmmes.common.entity.R;
import com.aiforest.tmmes.common.model.Point;
import com.aiforest.tmmes.common.valid.Insert;
import com.aiforest.tmmes.common.valid.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 位号 Controller
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@RestController
@RequestMapping(ManagerServiceConstant.POINT_URL_PREFIX)
public class PointController {

    @Resource
    private PointService pointService;

    @Resource
    private PointAttributeConfigService pointAttributeConfigService;

    @Resource
    private PointAttributeService pointAttributeService;

    /**
     * 新增 Point
     *
     * @param point    Point
     * @param tenantId 租户ID
     * @return Point
     */
    @PostMapping("/add")
    public R<Point> add(@Validated(Insert.class) @RequestBody Point point,
                        @RequestHeader(value = RequestConstant.Header.X_AUTH_TENANT_ID, defaultValue = DefaultConstant.DEFAULT_ID) String tenantId) {
        try {
            point.setTenantId(tenantId);
            pointService.add(point);
            return R.ok();
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 根据 ID 删除 Point
     *
     * @param id 位号ID
     * @return 是否删除
     */
    @PostMapping("/delete/{id}")
    public R<String> delete(@NotNull @PathVariable(value = "id") String id) {
        try {
            pointService.delete(id);
            return R.ok();
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 修改 Point
     *
     * @param point    Point
     * @param tenantId 租户ID
     * @return Point
     */
    @PostMapping("/update")
    public R<String> update(@Validated(Update.class) @RequestBody Point point, @RequestHeader(value = RequestConstant.Header.X_AUTH_TENANT_ID, defaultValue = DefaultConstant.DEFAULT_ID) String tenantId) {
        try {
            point.setTenantId(tenantId);
            pointService.update(point);
            return R.ok();
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 根据 ID 查询 Point
     *
     * @param id 位号ID
     * @return Point
     */
    @GetMapping("/id/{id}")
    public R<Point> selectById(@NotNull @PathVariable(value = "id") String id) {
        try {
            Point select = pointService.selectById(id);
            if (ObjectUtil.isNotNull(select)) {
                return R.ok(select);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    /**
     * 根据 ID 集合查询 Point
     *
     * @param pointIds Point ID Set
     * @return Map String:Point
     */
    @PostMapping("/ids")
    public R<Map<String, Point>> selectByIds(@RequestBody Set<String> pointIds) {
        try {
            List<Point> points = pointService.selectByIds(pointIds);
            Map<String, Point> pointMap = points.stream().collect(Collectors.toMap(Point::getId, Function.identity()));
            return R.ok(pointMap);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 根据 模板 ID 查询 Point
     *
     * @param profileId Profile ID
     * @return Point Array
     */
    @GetMapping("/profile_id/{profileId}")
    public R<List<Point>> selectByProfileId(@NotNull @PathVariable(value = "profileId") String profileId) {
        try {
            List<Point> select = pointService.selectByProfileId(profileId);
            if (CollUtil.isNotEmpty(select)) {
                return R.ok(select);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    /**
     * 根据 设备 ID 查询 Point
     *
     * @param deviceId 设备ID
     * @return Point Array
     */
    @GetMapping("/device_id/{deviceId}")
    public R<List<Point>> selectByDeviceId(@NotNull @PathVariable(value = "deviceId") String deviceId) {
        try {
            List<Point> select = pointService.selectByDeviceId(deviceId);
            if (CollUtil.isNotEmpty(select)) {
                select.forEach(p -> {
                    List<PointAttributeConfig> pointAttributeConfigs = pointAttributeConfigService.selectByDeviceIdAndPointId(deviceId, p.getId());
                    pointAttributeConfigs.forEach(a -> {
                        PointAttribute  pointAttribute = pointAttributeService.selectById(a.getPointAttributeId());
                        if (ObjectUtil.isNotNull(pointAttribute)) {
                            a.setDisplayName(pointAttribute.getDisplayName());
                            a.setAttributeName(pointAttribute.getAttributeName());
                        }
                    });
                    p.setPointAttributeConfigList(pointAttributeConfigs);
                });
                return R.ok(select);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    /**
     * 模糊分页查询 Point
     *
     * @param pointPageQuery Point Dto
     * @param tenantId       租户ID
     * @return Page Of Point
     */
    @PostMapping("/list")
    public R<Page<Point>> list(@RequestBody(required = false) PointPageQuery pointPageQuery,
                               @RequestHeader(value = RequestConstant.Header.X_AUTH_TENANT_ID, defaultValue = DefaultConstant.DEFAULT_ID) String tenantId) {
        try {
            if (ObjectUtil.isEmpty(pointPageQuery)) {
                pointPageQuery = new PointPageQuery();
            }
            pointPageQuery.setTenantId(tenantId);
            Page<Point> page = pointService.list(pointPageQuery);
            if (ObjectUtil.isNotNull(page)) {
                return R.ok(page);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

    /**
     * 查询 位号单位
     *
     * @param pointIds Point ID Set
     * @return Map String:String
     */
    @PostMapping("/unit")
    public R<Map<String, String>> unit(@RequestBody Set<String> pointIds) {
        try {
            Map<String, String> units = pointService.unit(pointIds);
            if (ObjectUtil.isNotNull(units)) {
                Map<String, String> unitCodeMap = units.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                return R.ok(unitCodeMap);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail();
    }

}
