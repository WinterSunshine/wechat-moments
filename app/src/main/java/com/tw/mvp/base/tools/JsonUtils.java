package com.tw.mvp.base.tools;
import com.alibaba.fastjson.JSON;

import org.json.JSONException;

import java.util.List;

public class JsonUtils
{
//    private static Gson gson = new Gson();

    private JsonUtils()
    {
    }

    /**
     * 将JSON字符串转换为一个Bean对象
     */
    public static <T> T parserJSONObject(String jsonStr, Class<T> resultCls)
    {
        return JSON.parseObject(jsonStr, resultCls);
    }

    /**
     * 将JSON字符串转换为一个集合
     */
    public static <T> List<T> parserJSONArray(String jsonStr, Class<T> resultCls) throws JSONException
    {
        return JSON.parseArray(jsonStr, resultCls);
    }
}
