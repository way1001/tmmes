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

package com.aiforest.tmmes.driver.sdk.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.aiforest.tmmes.common.constant.common.ExceptionConstant;
import com.aiforest.tmmes.common.entity.driver.AttributeInfo;
import com.aiforest.tmmes.common.enums.PointTypeFlagEnum;
import com.aiforest.tmmes.common.utils.DecodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @author way
 * @since 2024.5.20
 */
@Slf4j
public class DriverUtil {

    private DriverUtil() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 获取 属性值
     *
     * @param infoMap   Attribute Info
     * @param attribute String Attribute Name
     * @param <T>       T
     * @return T
     */
    public static <T> T attribute(Map<String, AttributeInfo> infoMap, String attribute) {
        return value(infoMap.get(attribute).getType().getCode(), infoMap.get(attribute).getValue());
    }

    /**
     * 通过类型转换数据
     *
     * @param type  String Type, byte/short/int/long/float/double/boolean/string
     * @param value String Value
     * @param <T>   T
     * @return T
     */
    public static <T> T value(String type, String value) {
        return Convert.convertByClassName(getTypeClassName(type), value);
    }

    /**
     * Base 64 解码
     *
     * @param content string
     * @return string
     */
    public static String base64Encode(String content) {
        byte[] bytes = DecodeUtil.stringToByte(content);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Base 64 编码
     *
     * @param content string
     * @return string
     */
    public static String base64Decode(String content) {
        byte[] bytes = DecodeUtil.stringToByte(content);
        return new String(Base64.getDecoder().decode(bytes));
    }

    /**
     * 将 BCD byte[] 转成十进制字符串
     *
     * @param bytes Byte Array
     * @return String
     */
    public static String bcdBytesToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append((byte) ((bytes[i] & 0xf0) >>> 4));
            sb.append((byte) (bytes[i] & 0x0f));
        }
        return sb.toString().substring(0, 1).equalsIgnoreCase("0") ? sb
                .toString().substring(1) : sb.toString();
    }

    /**
     * 将十进制字符串转成 BCD byte[]
     *
     * @param decimalString decimal string
     * @return byte array
     */
    public static byte[] strToBcdBytes(String decimalString) {
        int length = decimalString.length();
        int mod = length % 2;
        if (mod != 0) {
            decimalString = "0" + decimalString;
            length = decimalString.length();
        }
        if (length >= 2) {
            length = length / 2;
        }
        byte[] bcdBytes = new byte[length];
        byte[] decimalBytes = DecodeUtil.stringToByte(decimalString);
        int j;
        int k;
        for (int i = 0; i < decimalString.length() / 2; i++) {
            if ((decimalBytes[2 * i] >= '0') && (decimalBytes[2 * i] <= '9')) {
                j = decimalBytes[2 * i] - '0';
            } else if ((decimalBytes[2 * i] >= 'a') && (decimalBytes[2 * i] <= 'z')) {
                j = decimalBytes[2 * i] - 'a' + 0x0a;
            } else {
                j = decimalBytes[2 * i] - 'A' + 0x0a;
            }
            if ((decimalBytes[2 * i + 1] >= '0') && (decimalBytes[2 * i + 1] <= '9')) {
                k = decimalBytes[2 * i + 1] - '0';
            } else if ((decimalBytes[2 * i + 1] >= 'a') && (decimalBytes[2 * i + 1] <= 'z')) {
                k = decimalBytes[2 * i + 1] - 'a' + 0x0a;
            } else {
                k = decimalBytes[2 * i + 1] - 'A' + 0x0a;
            }
            bcdBytes[i] = (byte) ((j << 4) + k);
        }
        return bcdBytes;
    }

    /**
     * 将byte[]转成十六进制字符串
     *
     * @param bytes Byte Array
     * @return String
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * byte数组到int的转换(大端)
     *
     * @param bytes Byte Array
     * @return Integer
     */
    public static int bytesToInt(byte[] bytes) {
        byte[] temp = new byte[4];
        int length = bytes.length;
        System.arraycopy(bytes, 0, temp, 0, length);
        for (int i = length; i < 4; i++) {
            temp[i] = 0x00;
        }
        int int1 = temp[3] & 0xff;
        int int2 = (temp[2] & 0xff) << 8;
        int int3 = (temp[1] & 0xff) << 16;
        int int4 = (temp[0] & 0xff) << 24;

        return int1 | int2 | int3 | int4;
    }

    /**
     * byte数组到int的转换(小端)
     *
     * @param bytes Byte Array
     * @return Integer
     */
    public static int bytesToIntLE(byte[] bytes) {
        byte[] temp = new byte[4];
        int length = bytes.length;
        System.arraycopy(bytes, 0, temp, 0, length);
        for (int i = length; i < 4; i++) {
            temp[i] = 0x00;
        }
        int int1 = temp[0] & 0xff;
        int int2 = (temp[1] & 0xff) << 8;
        int int3 = (temp[2] & 0xff) << 16;
        int int4 = (temp[3] & 0xff) << 24;
        return int1 | int2 | int3 | int4;
    }

    /**
     * 将byte[]转成Ascii码
     *
     * @param bytes Byte Array
     * @return String
     */
    public static String bytesToAscii(byte[] bytes) {
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    /**
     * 将byte[]颠倒
     *
     * @param bytes Byte Array
     * @return Byte Array
     */
    public static byte[] byteReverse(byte[] bytes) {
        int length = bytes.length;
        byte[] reverse = new byte[length];
        for (int i = 0; i < length; i++) {
            reverse[length - 1 - i] = bytes[i];
        }
        return reverse;
    }

    /**
     * 合并byte[]
     *
     * @param bytes Byte Array
     * @return Byte Array
     */
    public static byte[] mergerBytes(byte[]... bytes) {
        int lengthByte = 0;
        for (byte[] value : bytes) {
            lengthByte += value.length;
        }
        byte[] allByte = new byte[lengthByte];
        int countLength = 0;
        for (byte[] b : bytes) {
            System.arraycopy(b, 0, allByte, countLength, b.length);
            countLength += b.length;
        }
        return allByte;
    }

    /**
     * 获取字节间的异或值
     *
     * @param bytes Byte Array
     * @return Byte
     */
    public static byte xorBytes(byte[]... bytes) {
        byte xor = 0x00;
        for (byte[] value : bytes) {
            for (byte b : value) {
                xor ^= b;
            }
        }
        return xor;
    }

    /**
     * 获取字节间的累加值
     *
     * @param bytes Byte Array
     * @return Byte
     */
    public static byte sumBytes(byte[]... bytes) {
        byte xor = 0x00;
        for (byte[] value : bytes) {
            for (byte b : value) {
                xor += b;
            }
        }
        return xor;
    }

    /**
     * 获取基本类型 Class Name，默认：java.lang.String
     *
     * @param type String Type, byte/short/int/long/float/double/boolean/string
     * @return Class Name
     */
    public static String getTypeClassName(String type) {
        String className = String.class.getName();

        PointTypeFlagEnum valueType = PointTypeFlagEnum.ofCode(type);
        if (ObjectUtil.isNull(valueType)) {
            throw new IllegalArgumentException("Unsupported type of " + type);
        }

        switch (valueType) {
            case BYTE:
                className = Byte.class.getName();
                break;
            case SHORT:
                className = Short.class.getName();
                break;
            case INT:
                className = Integer.class.getName();
                break;
            case LONG:
                className = Long.class.getName();
                break;
            case FLOAT:
                className = Float.class.getName();
                break;
            case DOUBLE:
                className = Double.class.getName();
                break;
            case BOOLEAN:
                className = Boolean.class.getName();
                break;
            default:
                break;
        }
        return className;
    }

}
