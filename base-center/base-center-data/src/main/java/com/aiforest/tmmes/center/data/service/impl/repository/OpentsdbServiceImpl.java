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

package com.aiforest.tmmes.center.data.service.impl.repository;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.aiforest.tmmes.center.data.service.RepositoryService;
import com.aiforest.tmmes.center.data.strategy.RepositoryStrategyFactory;
import com.aiforest.tmmes.common.constant.driver.StorageConstant;
import com.aiforest.tmmes.common.constant.driver.StrategyConstant;
import com.aiforest.tmmes.common.entity.point.PointValue;
import com.aiforest.tmmes.common.entity.point.TsPointValue;
import com.aiforest.tmmes.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "data.point.sava.opentsdb.enable", havingValue = "true")
public class OpentsdbServiceImpl implements RepositoryService, InitializingBean {

    @Value("${data.point.sava.opentsdb.host}")
    private String host;
    @Value("${data.point.sava.opentsdb.port}")
    private Integer port;

    @Resource
    private OkHttpClient okHttpClient;

    @Override
    public String getRepositoryName() {
        return StrategyConstant.Storage.STRATEGY_OPENTSDB;
    }

    @Override
    public void savePointValue(PointValue pointValue) {
        if (!CharSequenceUtil.isAllNotEmpty(pointValue.getDeviceId(), pointValue.getPointId())) {
            return;
        }

        savePointValues(pointValue.getDeviceId(), Collections.singletonList(pointValue));
    }

    @Override
    public void savePointValues(String deviceId, List<PointValue> pointValues) {
        if (CharSequenceUtil.isEmpty(deviceId)) {
            return;
        }

        List<TsPointValue> tsPointValues = pointValues.stream()
                .filter(pointValue -> CharSequenceUtil.isNotEmpty(pointValue.getPointId()))
                .map(pointValue -> convertPointValues(StorageConstant.POINT_VALUE_PREFIX + deviceId, pointValue))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<List<TsPointValue>> partition = Lists.partition(tsPointValues, 100);
        partition.forEach(this::putPointValues);
    }

    @Override
    public void afterPropertiesSet() {
        RepositoryStrategyFactory.put(StrategyConstant.Storage.STRATEGY_OPENTSDB, this);
    }

    private List<TsPointValue> convertPointValues(String metric, PointValue pointValue) {
        String point = pointValue.getPointId();
        String value = pointValue.getValue();
        long timestamp = pointValue.getOriginTime().getTime();

        List<TsPointValue> tsPointValues = new ArrayList<>(2);

        TsPointValue tsValue = new TsPointValue(metric, value);
        tsValue.setTimestamp(timestamp);
        tsValue.addTag("point", point).addTag("valueType", "value");
        tsPointValues.add(tsValue);

        TsPointValue tsRawValue = new TsPointValue(metric, value);
        tsRawValue.setTimestamp(timestamp);
        tsValue.addTag("point", point).addTag("valueType", "rawValue");
        tsPointValues.add(tsRawValue);

        return tsPointValues;
    }

    private void putPointValues(List<TsPointValue> tsPointValues) {
        String putUrl = String.format("http://%s:%s/api/put?details", host, port);
        RequestBody requestBody = RequestBody.create(JsonUtil.toJsonString(tsPointValues), MediaType.parse(ContentType.APPLICATION_JSON.toString()));
        Request request = new Request.Builder()
                .url(putUrl)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error("Send pointValues to opentsdb error: {}", e.getMessage(), e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (ObjectUtil.isNotNull(response.body())) {
                    log.debug("Send pointValues to opentsdb, Response: {}", response.message());
                }
            }
        });
    }
}
