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
import com.aiforest.tmmes.center.data.service.RepositoryService;
import com.aiforest.tmmes.center.data.strategy.RepositoryStrategyFactory;
import com.aiforest.tmmes.common.constant.common.PrefixConstant;
import com.aiforest.tmmes.common.constant.common.SymbolConstant;
import com.aiforest.tmmes.common.constant.driver.StrategyConstant;
import com.aiforest.tmmes.common.entity.point.PointValue;
import com.aiforest.tmmes.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Service
public class RedisServiceImpl implements RepositoryService, InitializingBean {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public String getRepositoryName() {
        return StrategyConstant.Storage.REDIS;
    }

    @Override
    public void savePointValue(PointValue pointValue) {
        if (!CharSequenceUtil.isAllNotEmpty(pointValue.getDeviceId(), pointValue.getPointId())) {
            return;
        }

        final String prefix = PrefixConstant.REAL_TIME_VALUE_KEY_PREFIX + pointValue.getDeviceId() + SymbolConstant.DOT;
        redisUtil.setKey(prefix + pointValue.getPointId(), pointValue);
    }

    @Override
    public void savePointValues(String deviceId, List<PointValue> pointValues) {
        if (CharSequenceUtil.isEmpty(deviceId)) {
            return;
        }

        final String prefix = PrefixConstant.REAL_TIME_VALUE_KEY_PREFIX + deviceId + SymbolConstant.DOT;
        Map<String, PointValue> collect = pointValues.stream()
                .filter(pointValue -> CharSequenceUtil.isNotEmpty(pointValue.getPointId()))
                .collect(Collectors.toMap(pointValue -> prefix + pointValue.getPointId(), pointValue -> pointValue));
        redisUtil.setKey(collect);
    }

    @Override
    public void afterPropertiesSet() {
        RepositoryStrategyFactory.put(StrategyConstant.Storage.REDIS, this);
    }
}