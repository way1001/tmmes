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

import com.aiforest.tmmes.center.data.entity.dto.RealTimePointValueDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aiforest.tmmes.api.center.data.PointValueQuery;
import com.aiforest.tmmes.center.data.entity.vo.query.PointValuePageQuery;
import com.aiforest.tmmes.common.entity.point.PointValue;

import java.util.List;

/**
 * @author way
 * @since 2024.5.20
 */
public interface PointValueService {

    /**
     * 新增 PointValue
     *
     * @param pointValue PointValue
     */
    void savePointValue(PointValue pointValue);

    /**
     * 批量新增 PointValue
     *
     * @param pointValues PointValue Array
     */
    void savePointValues(List<PointValue> pointValues);

    /**
     * 获取带分页、排序
     *
     * @param pointValuePageQuery PointValueDto
     * @return Page Of PointValue
     */
    Page<PointValue> latest(PointValuePageQuery pointValuePageQuery);

    /**
     * 获取带分页、排序
     *
     * @param pointValuePageQuery PointValueDto
     * @return Page Of PointValue
     */
    Page<PointValue> list(PointValuePageQuery pointValuePageQuery);

    PointValue latest(PointValueQuery request);

    List<RealTimePointValueDTO> list(String deviceId);

    List<RealTimePointValueDTO> specifiedList(String deviceId);
}
