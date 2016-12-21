package com.tw.mvp.base.http;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.tw.mvp.base.tools.LogUtils;
import com.tw.mvp.base.tools.ReflectUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HttpProxyInvocation implements InvocationHandler
{
    private AsyncHttpClient client = new AsyncHttpClient();

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable
    {
        //参数
        String[] arguments = method.getAnnotation(HttpRequest.class).arguments();
        //返回类型
        Class<?> resultCls = method.getAnnotation(HttpRequest.class).resultClass();
        //刷新方法
        String refreshMethod = method.getAnnotation(HttpRequest.class).refreshMethod();
        //baseUrl
        String baseURL = method.getAnnotation(HttpRequest.class).baseURL();
        //请求接口名
        String requestName = method.getAnnotation(HttpRequest.class).requestName();
        HttpResponseHandler response = ReflectUtils.constructHttpResponse(params[0], resultCls, refreshMethod);
        client.get(baseURL + requestName, new RequestParams(), response);
        LogUtils.w(baseURL + requestName);
        return new Object();
    }

    private RequestParams createRequestParams(String[] arguments, Object[] params)
    {
        RequestParams requestParams = new RequestParams();
        if(arguments.length != 0)
        {
            for(int pos = 0; pos < arguments.length; pos++)
            {
                requestParams.put(arguments[pos], params[pos + 1].toString());
            }
        }
        return requestParams;
    }
}
