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
 * 消息 相关常量
 *
 * @author way
 * @since 2024.5.20
 */
public class RabbitConstant {

    private RabbitConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    // Arguments
    public static final String MESSAGE_TTL = "x-message-ttl";
    public static final String AUTO_DELETE = "x-auto-delete";

    // Sync
    public static String TOPIC_EXCHANGE_SYNC = "tmmes.e.sync";
    public static final String ROUTING_SYNC_UP_PREFIX = "tmmes.r.sync.up.";
    public static String QUEUE_SYNC_UP = "tmmes.q.sync.up";
    public static final String ROUTING_SYNC_DOWN_PREFIX = "tmmes.r.sync.down.";
    public static String QUEUE_SYNC_DOWN_PREFIX = "tmmes.q.sync.down.";

    // Event
    public static String TOPIC_EXCHANGE_EVENT = "tmmes.e.event";
    public static final String ROUTING_DRIVER_EVENT_PREFIX = "tmmes.r.event.driver.";
    public static String QUEUE_DRIVER_EVENT = "tmmes.q.event.driver";
    public static final String ROUTING_DEVICE_EVENT_PREFIX = "tmmes.r.event.device.";
    public static String QUEUE_DEVICE_EVENT = "tmmes.q.event.device";

    // Metadata
    public static String TOPIC_EXCHANGE_METADATA = "tmmes.e.metadata";
    public static final String ROUTING_DRIVER_METADATA_PREFIX = "tmmes.r.metadata.driver.";
    public static String QUEUE_DRIVER_METADATA_PREFIX = "tmmes.q.metadata.driver.";

    // Command
    public static String TOPIC_EXCHANGE_COMMAND = "tmmes.e.command";
    public static final String ROUTING_DRIVER_COMMAND_PREFIX = "tmmes.r.command.driver.";
    public static String QUEUE_DRIVER_COMMAND_PREFIX = "tmmes.q.command.driver.";
    public static final String ROUTING_DEVICE_COMMAND_PREFIX = "tmmes.r.command.device.";
    public static String QUEUE_DEVICE_COMMAND_PREFIX = "tmmes.q.command.device.";

    // Value
    public static String TOPIC_EXCHANGE_VALUE = "tmmes.e.value";
    public static final String ROUTING_POINT_VALUE_PREFIX = "tmmes.r.value.point.";
    public static String QUEUE_POINT_VALUE = "tmmes.q.value.point";

    // Mqtt
    public static String TOPIC_EXCHANGE_MQTT = "tmmes.e.mqtt";
    public static final String ROUTING_MQTT_PREFIX = "tmmes.r.mqtt.";
    public static String QUEUE_MQTT = "tmmes.q.mqtt";

}
