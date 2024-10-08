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

package com.aiforest.tmmes.common.utils;

import com.aiforest.tmmes.common.constant.common.ExceptionConstant;
import com.aiforest.tmmes.common.constant.common.TimeConstant;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间相关工具类集合
 *
 * @author way
 * @since 2024.5.20
 */
@Slf4j
public class TimeUtil {

    /**
     * SimpleDateFormat ThreadLocal 保证线程安全
     */
    private static final ThreadLocal<SimpleDateFormat> DEFAULT_DATE_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat(TimeConstant.DEFAULT_DATE_FORMAT));
    private static final ThreadLocal<SimpleDateFormat> COMPLETE_DATE_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat(TimeConstant.COMPLETE_DATE_FORMAT));

    private TimeUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 推迟时间 HOUR/MINUTE/...
     *
     * @param amount Integer
     * @param field  Calendar field : {@link Calendar Calendar.HOUR/MINUTE/...}
     * @return Date
     */
    public static Date expireTime(int amount, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 使用 yyyy-MM-dd HH:mm:ss 格式化时间
     *
     * @param date Date
     * @return String
     */
    public static String defaultFormat(Date date) {
        return DEFAULT_DATE_FORMAT_THREAD_LOCAL.get().format(date);
    }

    /**
     * 使用 yyyy-MM-dd HH:mm:ss.SSS 格式化时间
     *
     * @param date Date
     * @return String
     */
    public static String completeFormat(Date date) {
        return COMPLETE_DATE_FORMAT_THREAD_LOCAL.get().format(date);
    }

    /**
     * 将时间字符串 yyyy-MM-dd HH:mm:ss 转为时间类型
     *
     * @param dateString Date String
     * @return Date
     */
    public static Date defaultDate(String dateString) {
        try {
            return DEFAULT_DATE_FORMAT_THREAD_LOCAL.get().parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将时间字符串 yyyy-MM-dd HH:mm:ss.SSS 转为时间类型
     *
     * @param dateString Date String
     * @return Date
     */
    public static Date completeDate(String dateString) {
        try {
            return COMPLETE_DATE_FORMAT_THREAD_LOCAL.get().parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

}
