package com.aiforest.tmmes.center.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aiforest.tmmes.center.auth.entity.query.RoleUserBindPageQuery;
import com.aiforest.tmmes.center.auth.mapper.RoleMapper;
import com.aiforest.tmmes.center.auth.mapper.RoleUserBindMapper;
import com.aiforest.tmmes.center.auth.service.RoleUserBindService;
import com.aiforest.tmmes.common.entity.common.Pages;
import com.aiforest.tmmes.common.enums.EnableFlagEnum;
import com.aiforest.tmmes.common.exception.AddException;
import com.aiforest.tmmes.common.exception.DeleteException;
import com.aiforest.tmmes.common.exception.NotFoundException;
import com.aiforest.tmmes.common.exception.UpdateException;
import com.aiforest.tmmes.common.model.Role;
import com.aiforest.tmmes.common.model.RoleUserBind;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author linys
 * @since 2023.04.02
 */
@Slf4j
@Service
public class RoleUserBindServiceImpl implements RoleUserBindService {

    @Resource
    private RoleUserBindMapper roleUserBindMapper;

    @Resource
    private RoleMapper roleMapper;


    @Override
    public RoleUserBind selectById(String id) {
        RoleUserBind roleUserBind = roleUserBindMapper.selectById(id);
        if (ObjectUtil.isNull(roleUserBind)) {
            throw new NotFoundException();
        }
        return roleUserBind;
    }

    @Override
    public Page<RoleUserBind> list(RoleUserBindPageQuery pageQuery) {
        if (ObjectUtil.isNull(pageQuery.getPage())) {
            pageQuery.setPage(new Pages());
        }
        return roleUserBindMapper.selectPage(pageQuery.getPage().convert(), buildQueryWrapper(pageQuery));
    }

    @Override
    public void add(RoleUserBind entityDo) {
        //todo check if exists
        if (roleUserBindMapper.insert(entityDo) < 1) {
            throw new AddException("The role user bind add failed");
        }
    }

    @Override
    public void update(RoleUserBind entityDo) {
        selectById(entityDo.getId());
        if (roleUserBindMapper.updateById(entityDo) < 1) {
            throw new UpdateException("The role user bind update failed");
        }
    }

    @Override
    public void delete(String id) {
        selectById(id);
        if (roleUserBindMapper.deleteById(id) < 1) {
            throw new DeleteException("The role user bind delete failed");
        }
    }

    @Override
    public List<Role> listRoleByTenantIdAndUserId(String tenantId, String userId) {
        LambdaQueryWrapper<RoleUserBind> queryWrapper = Wrappers.<RoleUserBind>query().lambda();
        queryWrapper.eq(RoleUserBind::getUserId, userId);
        List<RoleUserBind> roleUserBinds = roleUserBindMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(roleUserBinds)) {
            List<Role> roles = roleMapper.selectBatchIds(roleUserBinds.stream().map(RoleUserBind::getRoleId)
                    .collect(Collectors.toList()));
            return roles.stream().filter(e -> EnableFlagEnum.ENABLE.equals(e.getEnableFlag()) && tenantId.equals(e.getTenantId()))
                    .collect(Collectors.toList());
        }

        return null;
    }

    public LambdaQueryWrapper<RoleUserBind> buildQueryWrapper(RoleUserBindPageQuery pageQuery) {
        LambdaQueryWrapper<RoleUserBind> queryWrapper = Wrappers.<RoleUserBind>query().lambda();
        if (ObjectUtil.isNotNull(pageQuery)) {
            queryWrapper.eq(CharSequenceUtil.isNotEmpty(pageQuery.getUserId()), RoleUserBind::getUserId, pageQuery.getUserId());
            queryWrapper.eq(CharSequenceUtil.isNotEmpty(pageQuery.getRoleId()), RoleUserBind::getRoleId, pageQuery.getRoleId());
        }
        return queryWrapper;
    }
}
