package com.aiforest.tmmes.center.manager.entity.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.aiforest.tmmes.common.entity.common.Pages;
import com.aiforest.tmmes.common.model.NodeRedFlows;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NodeRedFlowsPageQuery extends NodeRedFlows {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Pages page;
}
