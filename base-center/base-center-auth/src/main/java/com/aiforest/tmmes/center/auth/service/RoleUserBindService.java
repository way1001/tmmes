package com.aiforest.tmmes.center.auth.service;

import com.aiforest.tmmes.center.auth.entity.query.RoleUserBindPageQuery;
import com.aiforest.tmmes.common.base.Service;
import com.aiforest.tmmes.common.model.Role;
import com.aiforest.tmmes.common.model.RoleUserBind;

import java.util.List;

/**
 * role user mapper service
 *
 * @author linys
 * @since 2023.04.02
 */
public interface RoleUserBindService extends Service<RoleUserBind, RoleUserBindPageQuery> {

    /**
     * 根据 租户id 和 用户id 查询
     *
     * @param tenantId 租户id
     * @param userId   用户id
     * @return Role list
     */
    List<Role> listRoleByTenantIdAndUserId(String tenantId, String userId);
}
