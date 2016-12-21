package com.tw.mvp.base.http;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tw.mvp.base.tools.DialogUtils;
import com.tw.mvp.base.tools.JsonUtils;
import com.tw.mvp.base.tools.LogUtils;
import com.tw.mvp.base.tools.ReflectUtils;
import com.tw.mvp.base.tools.ToastUtils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class HttpResponseHandler extends JsonHttpResponseHandler
{
    private static final int HTTP_SUCCESS = 0;
    private static final int SESSION_INVALID = 6002;
    private static final String OPCODE = "opcode";
    private static final String RESULT = "result";
    private static final String REASON = "reason";
    private static final String ERROR = "showErrorMessage";
    private Object context;
    private Class<?> resultCls;
    private String refreshMethod;
    private int opcode;

    public HttpResponseHandler(Object context, Class<?> resultCls, String refreshMethod)
    {
        this.context = context;
        this.resultCls = resultCls;
        this.refreshMethod = refreshMethod;
    }

    @Override
    public void onStart()
    {
        DialogUtils.showLoading(context, refreshMethod);
    }

    @Override
    public void onFinish()
    {
        DialogUtils.dismissLoading(refreshMethod);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response)
    {
        try
        {

            Object bean = JsonUtils.parserJSONArray(response.toString(), resultCls);
            ReflectUtils.invokeMethod(context, refreshMethod, bean, bean.getClass());
        }catch(Exception e)
        {
            LogUtils.w("http response error:" + e.getMessage() + "  Json:" + response.toString());
            ReflectUtils.invokeMethod(context, ERROR, opcode, e.getMessage(), Integer.class, String.class);
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
    {
        try
        {
            Object bean = JsonUtils.parserJSONObject(response.toString(), resultCls);
            ReflectUtils.invokeMethod(context, refreshMethod, bean, bean.getClass());
        }catch(Exception e)
        {
            LogUtils.w("http response error:" + e.getMessage() + "  Json:" + response.toString());
            ReflectUtils.invokeMethod(context, ERROR, opcode, e.getMessage(), Integer.class, String.class);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
    {
        super.onFailure(statusCode, headers, responseString, throwable);
        doFailureAction(throwable);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
    {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        doFailureAction(throwable);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse)
    {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        doFailureAction(throwable);
    }

    private void doFailureAction(Throwable throwable)
    {
        if(throwable instanceof ConnectException)
        {
            ToastUtils.show("与服务器请求失败，请重试");
        }else if(throwable instanceof SocketTimeoutException||throwable instanceof SocketException)
        {
            ToastUtils.show("连接超时，请稍后重试");
        }
    }
}
