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

package com.aiforest.tmmes.common.constant.driver;

import com.aiforest.tmmes.common.constant.common.ExceptionConstant;

/**
 * 事件 相关常量
 *
 * @author way
 * @since 2024.5.20
 */
public class EventConstant {

    private EventConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 驱动事件 相关常量
     *
     * @author way
     * @since 2024.5.20
     */
    public static class Driver {

        private Driver() {
            throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
        }

        /**
         * 驱动状态事件，该事件用于向 dc3-center-manager 发送驱动的当前状态
         */
        public static final String STATUS = "driver_status";

        /**
         * 驱动注册事件，该事件用于向 base-center-manager 注册驱动配置信息
         */
        public static final String REGISTER = "driver_register";
        public static final String REGISTER_BACK = "driver_register_back";

    }

    /**
     * 设备事件 相关常量
     *
     * @author way
     * @since 2024.5.20
     */
    public static class Device {

        private Device() {
            throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
        }

        /**
         * 设备状态事件
         */
        public static final String STATUS = "device_status";

        /**
         * 用于记录错误事件类型
         */
        public static final String ERROR = "device_error";

    }

}
