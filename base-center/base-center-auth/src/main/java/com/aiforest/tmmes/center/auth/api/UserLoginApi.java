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

package com.aiforest.tmmes.center.auth.api;


import cn.hutool.core.util.ObjectUtil;
import com.aiforest.tmmes.api.center.auth.NameQuery;
import com.aiforest.tmmes.api.center.auth.RUserLoginDTO;
import com.aiforest.tmmes.api.center.auth.UserLoginApiGrpc;
import com.aiforest.tmmes.api.center.auth.UserLoginDTO;
import com.aiforest.tmmes.api.common.BaseDTO;
import com.aiforest.tmmes.api.common.EnableFlagDTOEnum;
import com.aiforest.tmmes.api.common.RDTO;
import com.aiforest.tmmes.center.auth.service.UserLoginService;
import com.aiforest.tmmes.common.utils.BuilderUtil;
import com.aiforest.tmmes.common.enums.ResponseEnum;
import com.aiforest.tmmes.common.model.UserLogin;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import javax.annotation.Resource;

/**
 * UserLogin Api
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@GrpcService
public class UserLoginApi extends UserLoginApiGrpc.UserLoginApiImplBase {

    @Resource
    private UserLoginService userLoginService;

    @Override
    public void selectByName(NameQuery request, StreamObserver<RUserLoginDTO> responseObserver) {
        RUserLoginDTO.Builder builder = RUserLoginDTO.newBuilder();
        RDTO.Builder rBuilder = RDTO.newBuilder();
        UserLogin select = userLoginService.selectByLoginName(request.getName(), false);
        if (ObjectUtil.isNull(select)) {
            rBuilder.setOk(false);
            rBuilder.setCode(ResponseEnum.NO_RESOURCE.getCode());
            rBuilder.setMessage(ResponseEnum.NO_RESOURCE.getMessage());
        } else {
            rBuilder.setOk(true);
            rBuilder.setCode(ResponseEnum.OK.getCode());
            rBuilder.setMessage(ResponseEnum.OK.getMessage());
            UserLoginDTO user = buildDTOByDO(select);
            builder.setData(user);
        }

        builder.setResult(rBuilder);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }


    /**
     * DO to DTO
     *
     * @param entityDO UserLogin
     * @return UserLoginDTO
     */
    private UserLoginDTO buildDTOByDO(UserLogin entityDO) {
        UserLoginDTO.Builder builder = UserLoginDTO.newBuilder();
        BaseDTO baseDTO = BuilderUtil.buildBaseDTOByDO(entityDO);
        builder.setBase(baseDTO);
        builder.setLoginName(entityDO.getLoginName());
        builder.setUserId(entityDO.getUserId());
        builder.setUserPasswordId(entityDO.getUserPasswordId());
        builder.setEnableFlag(EnableFlagDTOEnum.valueOf(entityDO.getEnableFlag().name()));
        return builder.build();
    }
}
