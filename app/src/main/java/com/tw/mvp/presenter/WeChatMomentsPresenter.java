package com.tw.mvp.presenter;
import com.tw.mvp.base.tools.ProxyUtils;
import com.tw.mvp.model.TweetsBean;
import com.tw.mvp.view.activity.WeChatMomentsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edison on 2016/12/20.
 */
public class WeChatMomentsPresenter
{
    private final WeChatMomentsActivity mWeChatActivity;
    private List<TweetsBean> allDatas = new ArrayList<TweetsBean>();

    public WeChatMomentsPresenter(WeChatMomentsActivity weChatActivity)
    {
        this.mWeChatActivity = weChatActivity;
    }

    public void loadAllData()
    {
        ProxyUtils.getHttpProxy().getUser(mWeChatActivity);
        ProxyUtils.getHttpProxy().getTweets(mWeChatActivity);
    }

    public void loadPageData(int pageNum)
    {
        //内存分页，1页5条
        List<TweetsBean> pageData = loadNextPageData(pageNum);
        mWeChatActivity.showTweetPage(pageData);
    }

    public void filterInvalidData(ArrayList<TweetsBean> datas)
    {
        for(TweetsBean item : datas)
        {
            if(item.isValid())
            {
                allDatas.add(item);
            }
        }
    }

    private List<TweetsBean> loadNextPageData(int pageNum)
    {
        List<TweetsBean> pageData;
        int start = pageNum * 5 - 5;
        int end = pageNum * 5;
        if(isPageEnd(end))
        {
            pageData = allDatas.subList(start, allDatas.size());
        }else
        {
            pageData = allDatas.subList(start, end);
        }
        return pageData;
    }

    private boolean isPageEnd(int end)
    {
        return end >= allDatas.size();
    }

    public List<TweetsBean> getAllData()
    {
        return allDatas;
    }
}
