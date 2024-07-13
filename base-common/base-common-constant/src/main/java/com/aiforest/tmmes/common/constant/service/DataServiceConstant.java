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

package com.aiforest.tmmes.common.constant.service;

import com.aiforest.tmmes.common.constant.common.ExceptionConstant;

/**
 * 数据服务 相关常量
 *
 * @author way
 * @since 2024.5.20
 */
public class DataServiceConstant {

    private DataServiceConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 服务名
     */
    public static final String SERVICE_NAME = "base-center-data";

    public static final String INDEX_URL_PREFIX = "/data";
    public static final String VALUE_URL_PREFIX = "/data/point_value";
    public static final String VALUE_COMMAND_URL_PREFIX = "/data/point_value_command";
    public static final String DRIVER_STATUS_URL_PREFIX = "/data/driver/status";
    public static final String DRIVER_EVENT_URL_PREFIX = "/data/driver/event";
    public static final String DEVICE_STATUS_URL_PREFIX = "/data/device/status";
    public static final String DEVICE_EVENT_URL_PREFIX = "/data/device/event";
}
