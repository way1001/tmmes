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

package com.aiforest.tmmes.center.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.aiforest.tmmes.center.auth.mapper.BlackIpMapper;
import com.aiforest.tmmes.center.auth.mapper.TenantMapper;
import com.aiforest.tmmes.center.auth.mapper.UserLoginMapper;
import com.aiforest.tmmes.center.auth.service.DictionaryService;
import com.aiforest.tmmes.common.entity.common.Dictionary;
import com.aiforest.tmmes.common.model.BlackIp;
import com.aiforest.tmmes.common.model.Tenant;
import com.aiforest.tmmes.common.model.UserLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Resource
    private TenantMapper tenantMapper;
    @Resource
    private UserLoginMapper userLoginMapper;
    @Resource
    private BlackIpMapper blackIpMapper;

    @Override
    public List<Dictionary> tenantDictionary() {
        List<Dictionary> dictionaryList = new ArrayList<>(16);
        LambdaQueryWrapper<Tenant> queryWrapper = Wrappers.<Tenant>query().lambda();
        List<Tenant> tenantList = tenantMapper.selectList(queryWrapper);
        for (Tenant tenant : tenantList) {
            Dictionary driverDictionary = new Dictionary();
            driverDictionary.setLabel(tenant.getTenantName());
            driverDictionary.setValue(tenant.getId());
            dictionaryList.add(driverDictionary);
        }
        return dictionaryList;
    }

    @Override
    public List<Dictionary> userDictionary(String tenantId) {
        List<Dictionary> dictionaryList = new ArrayList<>(16);
        LambdaQueryWrapper<UserLogin> queryWrapper = Wrappers.<UserLogin>query().lambda();
        List<UserLogin> userLoginList = userLoginMapper.selectList(queryWrapper);
        for (UserLogin userLogin : userLoginList) {
            Dictionary driverDictionary = new Dictionary();
            driverDictionary.setLabel(userLogin.getLoginName());
            driverDictionary.setValue(userLogin.getId());
            dictionaryList.add(driverDictionary);
        }
        return dictionaryList;
    }

    @Override
    public List<Dictionary> blackIpDictionary(String tenantId) {
        List<Dictionary> dictionaryList = new ArrayList<>(16);
        LambdaQueryWrapper<BlackIp> queryWrapper = Wrappers.<BlackIp>query().lambda();
        List<BlackIp> blackIpList = blackIpMapper.selectList(queryWrapper);
        for (BlackIp blackIp : blackIpList) {
            Dictionary driverDictionary = new Dictionary();
            driverDictionary.setLabel(blackIp.getIp());
            driverDictionary.setValue(blackIp.getId());
            dictionaryList.add(driverDictionary);
        }
        return dictionaryList;
    }

}
