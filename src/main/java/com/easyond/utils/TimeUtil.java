package com.easyond.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Deprecated
public class TimeUtil {

    private TimeUtil() {
        throw new RuntimeException("创建工具类异常!");
    }

    /**
     * 将Date日期转换成yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String wholeTimeTsf(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

}
