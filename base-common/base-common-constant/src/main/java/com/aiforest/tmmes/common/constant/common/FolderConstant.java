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

package com.aiforest.tmmes.common.constant.common;

/**
 * 文件夹 相关常量
 *
 * @author way
 * @since 2024.5.20
 */
public class FolderConstant {

    private FolderConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 用户主目录
     */
    public static final String USER_HOME_PATH = System.getProperty("user.home") + "/.tmmes/";

    /**
     * 默认上传文件的缓存位置
     */
    public static final String TEMP_FILE_PATH = System.getProperty("java.io.tmpdir") + "/tmmes/";
}
