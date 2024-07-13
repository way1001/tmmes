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

package com.aiforest.tmmes.center.auth.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aiforest.tmmes.center.auth.entity.query.TenantPageQuery;
import com.aiforest.tmmes.center.auth.service.TenantService;
import com.aiforest.tmmes.common.constant.service.AuthServiceConstant;
import com.aiforest.tmmes.common.entity.R;
import com.aiforest.tmmes.common.enums.ResponseEnum;
import com.aiforest.tmmes.common.model.Tenant;
import com.aiforest.tmmes.common.valid.Insert;
import com.aiforest.tmmes.common.valid.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 用户 Controller
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@RestController
@RequestMapping(AuthServiceConstant.TENANT_URL_PREFIX)
public class TenantController {

    @Resource
    private TenantService tenantService;

    /**
     * 新增租户
     *
     * @param tenant 租户
     * @return {@link Tenant}
     */
    @PostMapping("/add")
    public R<String> add(@Validated(Insert.class) @RequestBody Tenant tenant) {
        try {
            tenantService.add(tenant);
            return R.ok();
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 根据 ID 删除租户
     *
     * @param id 租户ID
     * @return 是否删除
     */
    @PostMapping("/delete/{id}")
    public R<String> delete(@NotNull @PathVariable(value = "id") String id) {
        try {
            tenantService.delete(id);
            return R.ok();
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 根据 ID 修改租户
     * <ol>
     * <li>支持修改: Enable</li>
     * <li>不支持修改: Name</li>
     * </ol>
     *
     * @param tenant Tenant
     * @return {@link Tenant}
     */
    @PostMapping("/update")
    public R<String> update(@Validated(Update.class) @RequestBody Tenant tenant) {
        try {
            tenant.setTenantName(null);
            tenantService.update(tenant);
            return R.ok();
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 根据 ID 查询租户
     *
     * @param id 租户ID
     * @return {@link Tenant}
     */
    @GetMapping("/id/{id}")
    public R<Tenant> selectById(@NotNull @PathVariable(value = "id") String id) {
        try {
            Tenant select = tenantService.selectById(id);
            if (ObjectUtil.isNotNull(select)) {
                return R.ok(select);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail(ResponseEnum.NO_RESOURCE.getMessage());
    }

    /**
     * 根据 Code 查询租户
     *
     * @param code 租户Code
     * @return {@link Tenant}
     */
    @GetMapping("/code/{code}")
    public R<Tenant> selectByCode(@NotNull @PathVariable(value = "code") String code) {
        try {
            Tenant select = tenantService.selectByCode(code);
            if (ObjectUtil.isNotNull(select)) {
                return R.ok(select);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail(ResponseEnum.NO_RESOURCE.getMessage());
    }

    /**
     * 模糊分页查询租户
     *
     * @param tenantPageQuery 租户和分页参数
     * @return 带分页的 {@link Tenant}
     */
    @PostMapping("/list")
    public R<Page<Tenant>> list(@RequestBody(required = false) TenantPageQuery tenantPageQuery) {
        try {
            if (ObjectUtil.isEmpty(tenantPageQuery)) {
                tenantPageQuery = new TenantPageQuery();
            }
            Page<Tenant> page = tenantService.list(tenantPageQuery);
            if (ObjectUtil.isNotNull(page)) {
                return R.ok(page);
            }
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
        return R.fail(ResponseEnum.NO_RESOURCE.getMessage());
    }

}
