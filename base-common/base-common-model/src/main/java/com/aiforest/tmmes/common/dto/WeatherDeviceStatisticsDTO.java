package com.aiforest.tmmes.common.dto;


import com.aiforest.tmmes.common.model.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 高德天气设备数据统计DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDeviceStatisticsDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Device device;
    private String location;
    private String status;
    private String lastValue;
}
