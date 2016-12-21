package com.tw.mvp.base.http;
import com.tw.mvp.base.constant.Constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HttpRequest
{
    String[] arguments();
    Class<?> resultClass();
    String baseURL() default Constant.API_URL;
    String requestName();
    String refreshMethod();
}
