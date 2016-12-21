package com.tw.mvp.base.tools;
import com.tw.mvp.base.http.HttpProxyInvocation;
import com.tw.mvp.base.http.IHttpService;

import java.lang.reflect.Proxy;

public class ProxyUtils
{
	private static HttpProxyInvocation proxyHandler = new HttpProxyInvocation();
	
	public static IHttpService getHttpProxy()
	{
		return (IHttpService) Proxy.newProxyInstance(proxyHandler.getClass().getClassLoader(),
				new Class[] { IHttpService.class }, proxyHandler);
	}
}
