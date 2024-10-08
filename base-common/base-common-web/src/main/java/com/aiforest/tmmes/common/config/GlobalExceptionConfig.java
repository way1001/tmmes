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

package com.aiforest.tmmes.common.config;

import com.aiforest.tmmes.common.entity.R;
import com.aiforest.tmmes.common.exception.UnAuthorizedException;
import com.aiforest.tmmes.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;

/**
 * 全局异常处理
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig {

    /**
     * Global Exception
     *
     * @param exception Exception
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> globalException(Exception exception) {
        log.error("Global Exception Handler: {}", exception.getMessage(), exception);
        return R.fail(exception.getMessage());
    }

    /**
     * UnAuthorized Exception
     *
     * @param unAuthorizedException UnAuthorizedException
     * @return R
     */
    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R<String> unAuthorizedException(UnAuthorizedException unAuthorizedException) {
        log.warn("UnAuthorized Exception Handler: {}", unAuthorizedException.getMessage(), unAuthorizedException);
        return R.fail(unAuthorizedException.getMessage());
    }

    /**
     * Validation Exception
     *
     * @param exception MethodArgumentNotValidException
     * @return R
     */
    @ExceptionHandler({
            BindException.class,
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public R<String> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        HashMap<String, String> map = new HashMap<>(16);
        List<FieldError> errorList = exception.getBindingResult().getFieldErrors();
        errorList.forEach(error -> {
            log.warn("Method Argument Not Valid Exception Handler: {}({})", error.getField(), error.getDefaultMessage());
            map.put(error.getField(), error.getDefaultMessage());
        });
        return R.fail(JsonUtil.toJsonString(map));
    }

}
