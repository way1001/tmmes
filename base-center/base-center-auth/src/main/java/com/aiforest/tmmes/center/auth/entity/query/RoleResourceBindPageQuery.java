package com.aiforest.tmmes.center.auth.entity.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.aiforest.tmmes.common.entity.common.Pages;
import com.aiforest.tmmes.common.model.RoleResourceBind;
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
public class RoleResourceBindPageQuery extends RoleResourceBind {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Pages page;
}
