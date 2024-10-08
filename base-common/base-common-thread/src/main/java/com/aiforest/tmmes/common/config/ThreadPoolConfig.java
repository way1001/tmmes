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

import com.aiforest.tmmes.common.entity.property.ThreadProperty;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通用线程池配置
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "server")
public class ThreadPoolConfig {

    @Setter
    private ThreadProperty thread;

    private final AtomicInteger threadPoolAtomic = new AtomicInteger(1);
    private final AtomicInteger scheduledThreadPoolAtomic = new AtomicInteger(1);

    /**
     * LinkedBlockingQueue ThreadPoolExecutor
     *
     * @return ThreadPoolExecutor
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                thread.getCorePoolSize(),
                thread.getMaximumPoolSize(),
                thread.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(thread.getMaximumPoolSize() * 2),
                r -> new Thread(r, "[T]" + thread.getPrefix() + threadPoolAtomic.getAndIncrement()),
                (r, e) -> new BlockingRejectedExecutionHandler());
    }

    /**
     * ScheduledThreadPoolExecutor ThreadPoolExecutor
     *
     * @return ScheduledThreadPoolExecutor
     */
    @Bean
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(
                thread.getCorePoolSize(),
                r -> new Thread(r, "[S]" + thread.getPrefix() + scheduledThreadPoolAtomic.getAndIncrement()),
                (r, e) -> new BlockingRejectedExecutionHandler());
    }

    /**
     * 自定义 RejectedExecutionHandler
     *
     * @author way
     * @since 2024.5.20
     */
    private static class BlockingRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
            try {
                log.info("BlockingRejectedExecutionHandler: {}", executor.toString());

                if (!executor.isShutdown()) {
                    runnable.run();
                }
            } catch (Exception e) {
                log.error("BlockingRejectedExecutionHandler: {}", e.getMessage(), e);
            }
        }
    }

}
