package com.gd.form.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        return time;
    }
    public static String getFileNameTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(new Date());
        return time;
    }
}
