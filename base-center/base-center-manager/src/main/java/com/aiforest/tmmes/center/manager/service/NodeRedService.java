package com.aiforest.tmmes.center.manager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aiforest.tmmes.center.manager.entity.query.NodeRedFlowsPageQuery;
import com.aiforest.tmmes.common.model.NodeRedFlows;

public interface NodeRedService {
    Page<NodeRedFlows> flowsList(NodeRedFlowsPageQuery nodeRedFlowsPageQuery);
}