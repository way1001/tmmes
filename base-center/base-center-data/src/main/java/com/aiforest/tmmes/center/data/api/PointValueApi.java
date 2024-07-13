package com.aiforest.tmmes.center.data.api;

import cn.hutool.core.util.ObjectUtil;
import com.aiforest.tmmes.api.center.data.PointValueApiGrpc;
import com.aiforest.tmmes.api.center.data.PointValueDTO;
import com.aiforest.tmmes.api.center.data.PointValueQuery;
import com.aiforest.tmmes.api.center.data.RPointValueDTO;
import com.aiforest.tmmes.api.common.RDTO;
import com.aiforest.tmmes.center.data.service.PointValueService;
import com.aiforest.tmmes.common.entity.point.PointValue;
import com.aiforest.tmmes.common.enums.ResponseEnum;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import javax.annotation.Resource;

@Slf4j
@GrpcService
public class PointValueApi extends PointValueApiGrpc.PointValueApiImplBase {

    @Resource
    PointValueService pointValueService;

    @Override
    public void lastValue(PointValueQuery request, StreamObserver<RPointValueDTO> responseObserver) {
        RPointValueDTO.Builder builder = RPointValueDTO.newBuilder();
        RDTO.Builder rBuilder = RDTO.newBuilder();
        PointValue pointValue = pointValueService.latest(request);
        if (ObjectUtil.isNull(pointValue)) {
            rBuilder.setOk(false);
            rBuilder.setCode(ResponseEnum.NO_RESOURCE.getCode());
            rBuilder.setMessage(ResponseEnum.NO_RESOURCE.getMessage());
        } else {
            rBuilder.setOk(true);
            rBuilder.setCode(ResponseEnum.OK.getCode());
            rBuilder.setMessage(ResponseEnum.OK.getMessage());
            builder.setData(buildDTOByDO(pointValue));
        }
        builder.setResult(rBuilder);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    private PointValueDTO buildDTOByDO(PointValue pointValue) {
        PointValueDTO.Builder builder = PointValueDTO.newBuilder();
        builder.setDeviceId(pointValue.getDeviceId());
        builder.setPointId(pointValue.getPointId());
        builder.setValue(pointValue.getValue());
        builder.setRawValue(pointValue.getRawValue());
        builder.setCreateTime(pointValue.getCreateTime().getTime());
        builder.setOriginTime(pointValue.getOriginTime().getTime());
        return builder.build();
    }

}
