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

package com.aiforest.tmmes.driver.sdk.config;

import cn.hutool.core.text.CharSequenceUtil;
import com.aiforest.tmmes.common.constant.common.EnvironmentConstant;
import com.aiforest.tmmes.common.utils.EnvironmentUtil;
import com.aiforest.tmmes.common.utils.HostUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment Config
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class DriverEnvironmentConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String node = environment.getProperty(EnvironmentConstant.DRIVER_NODE, String.class);
        if (CharSequenceUtil.isEmpty(node)) {
            node = EnvironmentUtil.getNodeId();
        }

        String tenant = environment.getProperty(EnvironmentConstant.DRIVER_TENANT, String.class);
        String name = environment.getProperty(EnvironmentConstant.SPRING_APPLICATION_NAME, String.class);
        String client = CharSequenceUtil.format("{}/{}_{}", tenant, name, node);
        String service = CharSequenceUtil.format("{}/{}", tenant, name);

        Map<String, Object> source = new HashMap<>(2);
        source.put(EnvironmentConstant.DRIVER_NODE, node);
        source.put(EnvironmentConstant.DRIVER_SERVICE, service);
        source.put(EnvironmentConstant.DRIVER_HOST, HostUtil.localHost());
        source.put(EnvironmentConstant.DRIVER_CLIENT, client);
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new MapPropertySource("driver", source));
    }

}
