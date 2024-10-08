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

package com.aiforest.tmmes.common.config;

import com.aiforest.tmmes.common.constant.driver.RabbitConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Data
@Configuration
public class ExchangeConfig {

    /**
     * 驱动同步相关，平台端、驱动端可负载
     *
     * @return TopicExchange
     */
    @Bean
    TopicExchange syncExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE_SYNC, true, false);
    }

    /**
     * 事件相关，平台端可负载
     *
     * @return TopicExchange
     */
    @Bean
    TopicExchange eventExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE_EVENT, true, false);
    }

    /**
     * 元数据相关，平台端广播，驱动端订阅
     *
     * @return FanoutExchange
     */
    @Bean
    TopicExchange metadataExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE_METADATA, true, false);
    }

    /**
     * 指令相关，驱动端可负载
     *
     * @return TopicExchange
     */
    @Bean
    TopicExchange commandExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE_COMMAND, true, false);
    }

    /**
     * 数据相关，平台端可负载
     *
     * @return TopicExchange
     */
    @Bean
    TopicExchange valueExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE_VALUE, true, false);
    }

    /**
     * MQTT 相关，平台端可负载
     *
     * @return TopicExchange
     */
    @Bean
    TopicExchange mqttExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE_MQTT, true, false);
    }

}
