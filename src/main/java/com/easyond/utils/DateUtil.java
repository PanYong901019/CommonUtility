package com.easyond.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Pan on 2018/1/10.
 */
public class DateUtil {

    public static Date getDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String d1 = sdf.format(date);
        Date result = null;
        try {
            result = sdf.parse(d1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Date getDate(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date result = null;
        try {
            result = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDateString(Date date, String format) {
        String result = null;
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(date);
        }
        return result;
    }

    public static Date getBirthdayFromIdcard(String idcard) {
        String substring = idcard.substring(6, 14);
        return getDate(substring, "yyyyMMdd");
    }

    public static Date[] getWeekStartAndEnd(Date date) {
        Date[] result = new Date[2];
        SimpleDateFormat startSDF = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat endSDF = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        result[0] = getDate(startSDF.format(calendar.getTime()), "yyyy-MM-dd HH:mm:ss");
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        result[1] = getDate(endSDF.format(calendar.getTime()), "yyyy-MM-dd HH:mm:ss");
        return result;
    }

    public static String[] getWeekStartAndEndString(Date date) {
        String[] result = new String[2];
        SimpleDateFormat startSDF = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat endSDF = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        result[0] = startSDF.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        result[1] = endSDF.format(calendar.getTime());
        return result;
    }

    public static Date[] getNextWeekStartAndEnd(Date date) {
        Date[] result = new Date[2];
        SimpleDateFormat startSDF = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat endSDF = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 7);
        Date nextWeekDate = calendar.getTime();
        calendar.setTime(nextWeekDate);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        result[0] = getDate(startSDF.format(calendar.getTime()), "yyyy-MM-dd HH:mm:ss");
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        result[1] = getDate(endSDF.format(calendar.getTime()), "yyyy-MM-dd HH:mm:ss");
        return result;
    }

    public static Date[] getMonthStartAndEnd(Date date) {
        Date[] result = new Date[2];
        SimpleDateFormat startSDF = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat endSDF = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        result[0] = getDate(startSDF.format(calendar.getTime()), "yyyy-MM-dd HH:mm:ss");
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        result[1] = getDate(endSDF.format(calendar.getTime()), "yyyy-MM-dd HH:mm:ss");
        return result;
    }

    public static String[] getMonthStartAndEndString(Date date) {
        String[] result = new String[2];
        SimpleDateFormat startSDF = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat endSDF = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        result[0] = startSDF.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        result[1] = endSDF.format(calendar.getTime());
        return result;
    }

    public static Date[] getYearMonthStartAndEnd(String ymDateString) {
        Date[] result = new Date[2];
        String[] ymDate = ymDateString.split(",");
        Date start = getDate(ymDate[0], "yyyy-MM");
        Date end = getDate(ymDate[1], "yyyy-MM");
        SimpleDateFormat startSDF = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat endSDF = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(start);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        result[0] = getDate(startSDF.format(calendar.getTime()), "yyyy-MM-dd HH:mm:ss");
        calendar.setTime(end);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        result[1] = getDate(endSDF.format(calendar.getTime()), "yyyy-MM-dd HH:mm:ss");
        return result;
    }

    public static String getWeekString(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        if (date != null) {
            calendar.setTime(date);
        }
        String weekName = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                weekName = "周日";
                break;
            case 2:
                weekName = "周一";
                break;
            case 3:
                weekName = "周二";
                break;
            case 4:
                weekName = "周三";
                break;
            case 5:
                weekName = "周四";
                break;
            case 6:
                weekName = "周五";
                break;
            case 7:
                weekName = "周六";
                break;
        }
        return weekName;
    }


    public static Integer daysBetween(Date startDate, Date endDate) throws ParseException {
        return daysBetween(getDateString(startDate, "yyyy-MM-dd"), getDateString(endDate, "yyyy-MM-dd"), "yyyy-MM-dd");
    }

    public static Integer daysBetween(String startDate, String endDate, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(startDate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(endDate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static String daysBetweenString(Date startDate, Date endDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        long diff = endDate.getTime() - startDate.getTime();
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
        long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
    }

    public static Date getDateAfter(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    public static String[] getAfterAllDate(Date date, int day) {
        String[] result = new String[7];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < Math.abs(day); i++) {
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            if (date != null) {
                calendar.setTime(date);
            }
            if (day > 0) {
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + i);
            } else {
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - i);
            }
            result[i] = sdf.format(calendar.getTime());
        }
        for (int start = 0, end = result.length - 1; start < end; start++, end--) {
            String temp = result[end];
            result[end] = result[start];
            result[start] = temp;
        }
        return result;
    }


    public static Integer getWeekOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        if (date != null) {
            calendar.setTime(date);
        }
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public static Integer getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        if (date != null) {
            calendar.setTime(date);
        }
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取年
     *
     * @param date
     * @return
     */
    public static String getYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        String[] split = format.split("-");
        return split[0];
    }

    /**
     * 获取月
     *
     * @param date
     * @return
     */
    public static String getMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        String[] split = format.split("-");
        return split[1];
    }

    /**
     * 获取日
     *
     * @param date
     * @return
     */
    public static String getDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        String[] split = format.split("-");
        return split[2];
    }
}