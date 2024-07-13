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

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aiforest.tmmes.center.auth.entity.query.TenantPageQuery;
import com.aiforest.tmmes.center.auth.mapper.TenantMapper;
import com.aiforest.tmmes.center.auth.service.TenantService;
import com.aiforest.tmmes.common.entity.common.Pages;
import com.aiforest.tmmes.common.enums.EnableFlagEnum;
import com.aiforest.tmmes.common.exception.*;
import com.aiforest.tmmes.common.model.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 租户服务接口实现类
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Service
public class TenantServiceImpl implements TenantService {

    @Resource
    private TenantMapper tenantMapper;

    @Override
    public void add(Tenant entityDO) {
        Tenant select = selectByCode(entityDO.getTenantName());
        if (ObjectUtil.isNotNull(select)) {
            throw new DuplicateException("The tenant already exists");
        }

        if (tenantMapper.insert(entityDO) < 1) {
            throw new AddException("The tenant {} add failed", entityDO.getTenantName());
        }
    }

    @Override
    public void delete(String id) {
        Tenant tenant = selectById(id);
        if (ObjectUtil.isNull(tenant)) {
            throw new NotFoundException("The tenant does not exist");
        }

        if (tenantMapper.deleteById(id) < 1) {
            throw new DeleteException("The tenant delete failed");
        }
    }

    @Override
    public void update(Tenant entityDO) {
        entityDO.setTenantName(null);
        entityDO.setOperateTime(null);
        if (tenantMapper.updateById(entityDO) < 1) {
            throw new UpdateException("The tenant update failed");
        }
    }

    @Override
    public Tenant selectById(String id) {
        return tenantMapper.selectById(id);
    }

    @Override
    public Tenant selectByCode(String code) {
        LambdaQueryWrapper<Tenant> queryWrapper = Wrappers.<Tenant>query().lambda();
        queryWrapper.eq(Tenant::getTenantCode, code);
        queryWrapper.eq(Tenant::getEnableFlag, EnableFlagEnum.ENABLE);
        queryWrapper.last("limit 1");
        return tenantMapper.selectOne(queryWrapper);
    }

    @Override
    public Page<Tenant> list(TenantPageQuery queryDTO) {
        if (ObjectUtil.isNull(queryDTO.getPage())) {
            queryDTO.setPage(new Pages());
        }
        return tenantMapper.selectPage(queryDTO.getPage().convert(), fuzzyQuery(queryDTO));
    }

    private LambdaQueryWrapper<Tenant> fuzzyQuery(TenantPageQuery query) {
        LambdaQueryWrapper<Tenant> queryWrapper = Wrappers.<Tenant>query().lambda();
        if (ObjectUtil.isNotNull(query)) {
            queryWrapper.like(CharSequenceUtil.isNotEmpty(query.getTenantName()), Tenant::getTenantName, query.getTenantName());
        }
        return queryWrapper;
    }

}
