package com.aiforest.tmmes.center.data.entity.dto;

import com.aiforest.tmmes.common.constant.common.TimeConstant;
import com.aiforest.tmmes.common.enums.RwFlagEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
//@NoArgsConstructor
public class RealTimePointValueDTO {
    /**
     * 设备ID，同MySQl中等 设备ID 一致
     */
    private String deviceId;

    /**
     * 位号ID，同MySQl中等 位号ID 一致
     */
    private String pointId;

    /**
     * 处理值，进行过缩放、格式化等操作
     */
    private String value;

    /**
     * 原始值
     */
    private String rawValue;

    private String pointName;

    /**
     * 基础值
     */
    private double baseValue;

    /**
     * 比例系数
     */
    private double multiple;

    /**
     * 读写标识
     */
    private RwFlagEnum rwFlag;

    /**
     * 单位
     */
    private String unit;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = TimeConstant.COMPLETE_DATE_FORMAT, timezone = TimeConstant.TIMEZONE)
    private Date originTime;

//    public RealTimePointValueDTO(String deviceId, String pointId, String rawValue, String value) {
//        this.deviceId = deviceId;
//        this.pointId = pointId;
//        this.rawValue = rawValue;
//        this.value = value;
//        this.originTime = new Date();
//    }
}
