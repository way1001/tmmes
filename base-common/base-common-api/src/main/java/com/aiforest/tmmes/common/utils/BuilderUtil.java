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

package com.aiforest.tmmes.common.utils;

import com.aiforest.tmmes.api.common.BaseDTO;
import com.aiforest.tmmes.common.constant.common.ExceptionConstant;
import com.aiforest.tmmes.common.entity.base.Base;

/**
 * Builder 工具类
 *
 * @author way
 * @since 2024.5.20
 */
public class BuilderUtil {

    private BuilderUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    public static BaseDTO buildBaseDTOByDO(Base entityDO) {
        BaseDTO.Builder builder = BaseDTO.newBuilder();
        builder.setId(entityDO.getId());
        builder.setRemark(entityDO.getRemark());
        builder.setCreatorId(entityDO.getCreatorId());
        builder.setCreatorName(entityDO.getCreatorName());
        builder.setCreateTime(entityDO.getCreateTime().getTime());
        builder.setOperatorId(entityDO.getOperatorId());
        builder.setOperatorName(entityDO.getOperatorName());
        builder.setUpdateTime(entityDO.getOperateTime().getTime());
        return builder.build();
    }
}
