package com.aiforest.tmmes.center.auth.entity.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.aiforest.tmmes.common.entity.common.Pages;
import com.aiforest.tmmes.common.model.Resource;
import lombok.*;

/**
 * @author linys
 * @since 2023.04.02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResourcePageQuery extends Resource {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Pages page;
}
