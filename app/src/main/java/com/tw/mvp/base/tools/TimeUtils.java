package com.tw.mvp.base.tools;
import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils
{
    private static final int MILL = 1000;
    private static final int MINUTE = 60 * MILL;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private TimeUtils()
    {
    }

    public static String getCurrentTime()
    {
        return Long.toString(System.currentTimeMillis());
    }

    public static String getCurrentDate()
    {
        return simpleDateFormat.format(new Date());
    }

    public static Date getScheduleTime(long timeMillis)
    {
        return new Date(timeMillis);
    }

    /**
     * 查询当前日期前(后)x天的日期，负数为前多少天，正数为后多少天
     */
    public static String getBeforNumDay(int day)
    {
        Date date = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, day);
        return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
    }

    public static String getCurrentDay()
    {
        Date date = new Date(System.currentTimeMillis());
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static long calcTimeDiff(String tragetTime, String currentTime)
    {
        long diffInMs = 0;
        try
        {
            Date targetDate = simpleDateFormat.parse(tragetTime);
            Date currentDate = simpleDateFormat.parse(currentTime);
            diffInMs = targetDate.getTime() - currentDate.getTime();
        }catch(ParseException e)
        {
            ToastUtils.show(e.getMessage());
        }
        return diffInMs;
    }

    public static String getDeadDay(long timeDistance)
    {
        String day = (int) (timeDistance / DAY) + "";
        if(day.length() == 1)
        {
            day = "0" + day;
        }
        return day;
    }

    public static String getDeadHour(long timeDistance)
    {
        String hour = (int) (timeDistance % DAY) / HOUR + "";
        if(hour.length() == 1)
        {
            hour = "0" + hour;
        }
        return hour;
    }

    public static String getDeadMinute(long timeDistance)
    {
        String minute = (int) (timeDistance % HOUR) / MINUTE + "";
        if(minute.length() == 1)
        {
            minute = "0" + minute;
        }
        return minute;
    }

    public static String getDeadSecond(long timeDistance)
    {
        String mills = (int) (timeDistance % HOUR % MINUTE) / MILL + "";
        if(mills.length() == 1)
        {
            mills = "0" + mills;
        }
        return mills;
    }
}
