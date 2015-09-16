package com.cloud.bug.util;

/**
 * User: Jack Wang
 * Date: 15-9-16
 * Time: 下午3:14
 */
public class StringUtils {

    public static String toFixNumberString(String value, int length) {
        if (value == null) {
            value = "";
        }
        while (value.length() < length) {
            value = "0" + value;
        }
        return value;
    }
}
