package com.aiforest.tmmes.center.auth.service;

import com.aiforest.tmmes.center.auth.entity.query.RoleResourceBindPageQuery;
import com.aiforest.tmmes.common.base.Service;
import com.aiforest.tmmes.common.model.Resource;
import com.aiforest.tmmes.common.model.RoleResourceBind;

import java.util.List;

/**
 * role resource bind service
 *
 * @author linys
 * @since 2023.04.02
 */
public interface RoleResourceBindService extends Service<RoleResourceBind, RoleResourceBindPageQuery> {

    /**
     * 根据TenantId与UserId查询资源
     *
     * @param roleId 角色id
     * @return 资源列表
     */
    List<Resource> listResourceByRoleId(String roleId);
}
