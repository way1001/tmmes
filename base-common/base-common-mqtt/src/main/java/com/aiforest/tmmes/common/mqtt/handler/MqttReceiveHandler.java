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

package com.aiforest.tmmes.common.mqtt.handler;

import cn.hutool.core.text.CharSequenceUtil;
import com.aiforest.tmmes.common.mqtt.entity.MessageHeader;
import com.aiforest.tmmes.common.mqtt.entity.MqttMessage;
import com.aiforest.tmmes.common.mqtt.job.MqttScheduleJob;
import com.aiforest.tmmes.common.mqtt.service.MqttReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class MqttReceiveHandler {

    @Value("${driver.mqtt.batch.speed}")
    private Integer batchSpeed;

    @Resource
    private MqttReceiveService mqttReceiveService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 此处用于接收 MQTT 发送过来的数据，订阅的主题为 application.yml 中 mqtt.receive-topics 配置的 Topic 列表
     * +（加号）：可以（只能）匹配一个单词
     * #（井号）：可以匹配多个单词（或者零个）
     *
     * @return MessageHandler
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler mqttInboundReceive() {
        return message -> {
            try {
                MessageHeader messageHeader = new MessageHeader(message.getHeaders());
                String payload = message.getPayload().toString();
                if (CharSequenceUtil.isEmpty(payload)) {
                    log.error("Invalid mqtt inbound, From: {}, Qos: {}, Payload: null", messageHeader.getMqttReceivedTopic(), messageHeader.getMqttReceivedQos());
                    return;
                }
                MqttScheduleJob.messageCount.getAndIncrement();
                MqttMessage mqttMessage = new MqttMessage(messageHeader, payload);
                log.debug("Mqtt inbound, From: {}, Qos: {}, Payload: {}", messageHeader.getMqttReceivedTopic(), messageHeader.getMqttReceivedQos(), payload);

                // Judge whether to process data in batch according to the data transmission speed
                if (MqttScheduleJob.messageSpeed.get() < batchSpeed) {
                    threadPoolExecutor.execute(() ->
                            // Receive single mqtt message
                            mqttReceiveService.receiveValue(mqttMessage)
                    );
                } else {
                    // Save point value to schedule
                    MqttScheduleJob.messageLock.writeLock().lock();
                    MqttScheduleJob.addMqttMessages(mqttMessage);
                    MqttScheduleJob.messageLock.writeLock().unlock();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        };
    }
}
