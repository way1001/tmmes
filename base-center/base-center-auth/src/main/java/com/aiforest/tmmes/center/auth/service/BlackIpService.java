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

package com.aiforest.tmmes.center.auth.service;

import com.aiforest.tmmes.center.auth.entity.query.BlackIpPageQuery;
import com.aiforest.tmmes.common.base.Service;
import com.aiforest.tmmes.common.model.BlackIp;

/**
 * User Interface
 *
 * @author way
 * @since 2024.5.20
 */
public interface BlackIpService extends Service<BlackIp, BlackIpPageQuery> {
    /**
     * 根据 Ip 查询 BlackIp
     *
     * @param ip             IP
     * @param throwException Throw Exception
     * @return BlackIp
     */
    BlackIp selectByIp(String ip, boolean throwException);

    /**
     * 根据 Ip 是否在Ip黑名单列表
     *
     * @param ip IP
     * @return boolean
     */
    Boolean checkBlackIpValid(String ip);
}
