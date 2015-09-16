package com.cloud.bug.util;

/**
 * User: Jack Wang
 * Date: 15-9-16
 * Time: 下午3:12
 */
public class DateUtils {

    public static String getMonthStartTime(int year, int month) {
        return year + "-" + StringUtils.toFixNumberString(month + "", 2) + "-01 00:00:00";
    }

    public static String getMonthEndTime(int year, int month) {
        return year + "-" + StringUtils.toFixNumberString(month + "", 2) + "-31 23:59:59";
    }
}
