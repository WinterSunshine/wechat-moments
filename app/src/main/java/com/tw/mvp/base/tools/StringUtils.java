package com.tw.mvp.base.tools;
/**
 * Created by Edison on 2016/12/21.
 */
public class StringUtils
{
    public static boolean isEmpty(String str)
    {
        return (str == null)||(str.length() == 0);
    }

    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    public static String clean(String str)
    {
        return str == null ? "" : str.trim();
    }

    public static String trim(String str)
    {
        return str == null ? null : str.trim();
    }
}
