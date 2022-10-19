package com.jayus.lx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ Description
 * @ Author jayus
 * @ Date 2022/10/19 13:52
 */
public class DateUtil {
    /**
     * @param date 指定日期
     * @return String 格式化后日期
     */
    public static String formatDate(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(date);
    }
}

