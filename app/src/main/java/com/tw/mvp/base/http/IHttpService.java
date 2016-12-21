package com.tw.mvp.base.http;
import com.tw.mvp.model.TweetsBean;
import com.tw.mvp.model.UserBean;

public interface IHttpService
{
    /**
     * 获取用户信息
     */
    @HttpRequest(requestName = "jsmith", arguments = {}, resultClass = UserBean.class, refreshMethod = "refreshUser")
    public void getUser(Object context);
    /**
     * 获取Tweets列表
     */
    @HttpRequest(requestName = "jsmith/tweets", arguments = {}, resultClass = TweetsBean.class, refreshMethod = "refreshTweets")
    public void getTweets(Object context);
}
