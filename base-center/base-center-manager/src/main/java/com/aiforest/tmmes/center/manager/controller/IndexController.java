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

import com.aiforest.tmmes.center.manager.service.IndexService;
import com.aiforest.tmmes.common.constant.service.ManagerServiceConstant;
import com.aiforest.tmmes.common.dto.DataStatisticsDTO;
import com.aiforest.tmmes.common.dto.WeatherDeviceStatisticsDTO;
import com.aiforest.tmmes.common.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Index Controller
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@RestController
@RequestMapping(value = ManagerServiceConstant.INDEX_URL_PREFIX)
public class IndexController {

    @Resource
    IndexService indexService;

    /**
     * ping
     *
     * @return DataTime
     */
    /*@GetMapping("/ping")
    public R<String> ping() {
        return R.ok(TimeUtil.defaultFormat(new Date()));
    }*/

    /**
     * 数据统计
     */
    @GetMapping("statistics")
    public R<DataStatisticsDTO> statistics() {
        return R.ok(indexService.dataStatistics());
    }

    /**
     * 高德天气驱动设备列表
     * TODO 此接口包含过多固定值,后期应该优化掉
     */
    @GetMapping("weatherDeviceList")
    public R<List<WeatherDeviceStatisticsDTO>> weatherDeviceList() {
        return R.ok(indexService.weatherDeviceList());
    }

}
