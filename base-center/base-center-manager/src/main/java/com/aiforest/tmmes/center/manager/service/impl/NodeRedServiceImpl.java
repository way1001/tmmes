package com.aiforest.tmmes.center.manager.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aiforest.tmmes.center.manager.entity.query.NodeRedFlowsPageQuery;
import com.aiforest.tmmes.center.manager.mapper.NodeRedFlowsMapper;
import com.aiforest.tmmes.center.manager.service.NodeRedService;
import com.aiforest.tmmes.common.entity.common.Pages;
import com.aiforest.tmmes.common.model.NodeRedFlows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NodeRedServiceImpl implements NodeRedService {
    private static final Logger log = LoggerFactory.getLogger(NodeRedServiceImpl.class);
    @Resource
    NodeRedFlowsMapper nodeRedFlowsMapper;

    public NodeRedServiceImpl() {
    }

    public Page<NodeRedFlows> flowsList(NodeRedFlowsPageQuery nodeRedFlowsPageQuery) {
        if (ObjectUtil.isNotNull(nodeRedFlowsPageQuery.getPage())) {
            nodeRedFlowsPageQuery.setPage(new Pages());
        }

        return nodeRedFlowsMapper.selectPage(nodeRedFlowsPageQuery.getPage().convert(), fuzzyFlowsQuery(nodeRedFlowsPageQuery));
    }

    private LambdaQueryWrapper<NodeRedFlows> fuzzyFlowsQuery(NodeRedFlowsPageQuery query) {
        LambdaQueryWrapper<NodeRedFlows> queryWrapper = Wrappers.<NodeRedFlows>query().lambda();
        queryWrapper.eq(NodeRedFlows::getFlowType, "tab");
        return queryWrapper;
    }
}