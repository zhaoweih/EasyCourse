package com.zhaoweihao.architechturesample.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateFormatUtil {

    private DateFormatUtil() {
        throw new AssertionError("No construction for constant class");
    }

    public static String formatZhihuDailyDateLongToString(long date) {
        String sDate;
        Date d = new Date(date + 24*60*60*1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        sDate = format.format(d);

        return sDate;
    }

    public static long formatZhihuDailyDateStringToLong(String date) {
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyyMMdd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d == null ? 0 : d.getTime();
    }
}
