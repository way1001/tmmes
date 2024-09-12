package com.aiforest.tmmes.center.data.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class DevicePointValueDTO
{
    private String deviceId;
    private String deviceName;
    private String deviceCode;
    private String groupId;
    private List<RealTimePointValueDTO> realTimePointValueDTOS;
}
