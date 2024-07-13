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
import com.aiforest.tmmes.center.auth.entity.query.TenantBindPageQuery;
import com.aiforest.tmmes.center.auth.mapper.TenantBindMapper;
import com.aiforest.tmmes.center.auth.service.TenantBindService;
import com.aiforest.tmmes.common.entity.common.Pages;
import com.aiforest.tmmes.common.exception.AddException;
import com.aiforest.tmmes.common.exception.DeleteException;
import com.aiforest.tmmes.common.exception.NotFoundException;
import com.aiforest.tmmes.common.exception.UpdateException;
import com.aiforest.tmmes.common.model.TenantBind;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TenantBindService Impl
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@Service
public class TenantBindServiceImpl implements TenantBindService {

    @Resource
    private TenantBindMapper tenantBindMapper;

    @Override
    public void add(TenantBind entityDO) {
        if (tenantBindMapper.insert(entityDO) < 1) {
            throw new AddException("The tenant bind add failed");
        }
    }

    @Override
    public void delete(String id) {
        TenantBind tenantBind = selectById(id);
        if (ObjectUtil.isNull(tenantBind)) {
            throw new NotFoundException("The tenant bind does not exist");
        }

        if (tenantBindMapper.deleteById(id) < 1) {
            throw new DeleteException("The tenant bind delete failed");
        }
    }

    @Override
    public void update(TenantBind entityDO) {
        selectById(entityDO.getId());
        entityDO.setOperateTime(null);
        if (tenantBindMapper.updateById(entityDO) < 1) {
            throw new UpdateException("The tenant bind update failed");
        }
    }

    @Override
    public TenantBind selectById(String id) {
        TenantBind tenantBind = tenantBindMapper.selectById(id);
        if (ObjectUtil.isNull(tenantBind)) {
            throw new NotFoundException();
        }
        return tenantBind;
    }

    @Override
    public TenantBind selectByTenantIdAndUserId(String tenantId, String userId) {
        LambdaQueryWrapper<TenantBind> queryWrapper = Wrappers.<TenantBind>query().lambda();
        queryWrapper.eq(TenantBind::getTenantId, tenantId);
        queryWrapper.eq(TenantBind::getUserId, userId);
        queryWrapper.last("limit 1");
        return tenantBindMapper.selectOne(queryWrapper);
    }

    @Override
    public Page<TenantBind> list(TenantBindPageQuery queryDTO) {
        if (ObjectUtil.isNull(queryDTO.getPage())) {
            queryDTO.setPage(new Pages());
        }
        return tenantBindMapper.selectPage(queryDTO.getPage().convert(), fuzzyQuery(queryDTO));
    }

    private LambdaQueryWrapper<TenantBind> fuzzyQuery(TenantBindPageQuery query) {
        LambdaQueryWrapper<TenantBind> queryWrapper = Wrappers.<TenantBind>query().lambda();
        if (ObjectUtil.isNotNull(query)) {
            queryWrapper.eq(CharSequenceUtil.isNotEmpty(query.getTenantId()), TenantBind::getTenantId, query.getTenantId());
            queryWrapper.eq(CharSequenceUtil.isNotEmpty(query.getUserId()), TenantBind::getUserId, query.getUserId());
        }
        return queryWrapper;
    }

}
