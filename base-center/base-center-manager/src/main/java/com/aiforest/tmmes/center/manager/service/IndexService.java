package com.aiforest.tmmes.center.manager.service;

import com.aiforest.tmmes.common.dto.DataStatisticsDTO;
import com.aiforest.tmmes.common.dto.WeatherDeviceStatisticsDTO;

import java.util.List;

public interface IndexService {

    DataStatisticsDTO dataStatistics();

    List<WeatherDeviceStatisticsDTO> weatherDeviceList();
}
