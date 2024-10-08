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

package com.aiforest.tmmes.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.aiforest.tmmes.common.entity.base.Base;
import com.aiforest.tmmes.common.enums.EnableFlagEnum;
import com.aiforest.tmmes.common.enums.PointTypeFlagEnum;
import com.aiforest.tmmes.common.enums.RwFlagEnum;
import com.aiforest.tmmes.common.valid.Insert;
import com.aiforest.tmmes.common.valid.Update;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;

/**
 * 设备变量表
 *
 * @author way
 * @since 2024.5.20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@TableName("mes_point")
public class Point extends Base {

    /**
     * 位号名称
     */
    @NotBlank(message = "Point name can't be empty",
            groups = {Insert.class})
    @Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_#@/.|]{1,31}$",
            message = "Invalid point name",
            groups = {Insert.class, Update.class})
    private String pointName;

    /**
     * 位号编号
     */
    private String pointCode;

    /**
     * 位号类型标识
     */
    private PointTypeFlagEnum pointTypeFlag;

    /**
     * 读写标识
     */
    private RwFlagEnum rwFlag;

    /**
     * 基础值
     */
    private BigDecimal baseValue;

    /**
     * 比例系数
     */
    private BigDecimal multiple;

    /**
     * 数据精度
     */
    private Byte valueDecimal;

    /**
     * 单位
     */
    private String unit;

    /**
     * 模板ID
     */
    @NotBlank(message = "Profile id can't be empty",
            groups = {Insert.class, Update.class})
    private String profileId;

    /**
     * 分组ID
     */
    private String groupId;

    /**
     * 使能标识
     */
    private EnableFlagEnum enableFlag;

    /**
     * 租户ID
     */
    private String tenantId;


    @TableField(exist = false)
    private List<PointAttributeConfig> pointAttributeConfigList;

    /**
     * 设置默认值
     */
    public void setDefault() {
        this.pointTypeFlag = PointTypeFlagEnum.STRING;
        this.rwFlag = RwFlagEnum.R;
        this.baseValue = BigDecimal.valueOf(0);
        this.multiple = BigDecimal.valueOf(1);
        this.valueDecimal = 6;
        this.unit = "";
    }

}
