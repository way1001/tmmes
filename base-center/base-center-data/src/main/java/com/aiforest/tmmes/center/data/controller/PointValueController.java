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
import com.aiforest.tmmes.center.data.entity.vo.query.PointValuePageQuery;
import com.aiforest.tmmes.center.data.service.PointValueService;
import com.aiforest.tmmes.common.constant.service.DataServiceConstant;
import com.aiforest.tmmes.common.entity.R;
import com.aiforest.tmmes.common.entity.point.PointValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

}