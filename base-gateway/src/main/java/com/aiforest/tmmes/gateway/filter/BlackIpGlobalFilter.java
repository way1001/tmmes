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

package com.aiforest.tmmes.gateway.filter;

import com.aiforest.tmmes.api.center.auth.BlackIpApiGrpc;
import com.aiforest.tmmes.api.center.auth.IpQuery;
import com.aiforest.tmmes.api.center.auth.RBlackIpDTO;
import com.aiforest.tmmes.common.constant.service.AuthServiceConstant;
import com.aiforest.tmmes.gateway.utils.GatewayUtil;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义Ip黑名单过滤器
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
public class BlackIpGlobalFilter implements GlobalFilter, Ordered {

    @GrpcClient(AuthServiceConstant.SERVICE_NAME)
    private BlackIpApiGrpc.BlackIpApiBlockingStub blackIpApiBlockingStub;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String remoteIp = GatewayUtil.getRemoteIp(request);

        RBlackIpDTO rBlackIpDTO = blackIpApiBlockingStub.checkBlackIpValid(IpQuery.newBuilder().setIp(remoteIp).build());
        if (rBlackIpDTO.getResult().getOk()) {
            log.error("Forbidden Ip: {}", remoteIp);
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
