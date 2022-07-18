package com.zhh.crm.commons.util;

import java.text.SimpleDateFormat;
import java.util.Date;
//日期格式化
public class DateFormat {
    /**
     * 格式化成：yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String formatYMDHms(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }
    /**
     * 格式化成：yyyy-MM-dd
     * @param date
     * @return
     */
    public static String formatYMD(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return dateStr;
    }
    /**
     * 格式化成：HH:mm:ss
     * @param date
     * @return
     */
    public static String formatHms(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }
}
