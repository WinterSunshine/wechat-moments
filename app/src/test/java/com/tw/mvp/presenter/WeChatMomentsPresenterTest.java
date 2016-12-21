package com.tw.mvp.presenter;
import com.tw.mvp.model.TweetsBean;
import com.tw.mvp.view.activity.WeChatMomentsActivity;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;

/**
 * Created by Edison on 2016/12/21.
 */
public class WeChatMomentsPresenterTest
{
    @Test
    public void loadPageData() throws Exception
    {
        WeChatMomentsActivity activity = PowerMockito.mock(WeChatMomentsActivity.class);
        WeChatMomentsPresenter weChatMomentsPresenter = new WeChatMomentsPresenter(activity);
        weChatMomentsPresenter.loadPageData(1);
    }

    @Test
    public void filterInvalidData() throws Exception
    {
        ArrayList<TweetsBean> testDatas = new ArrayList<TweetsBean>();
        TweetsBean testOne = new TweetsBean();
        TweetsBean testTwo = new TweetsBean();
        TweetsBean testThree = new TweetsBean();
        testOne.setError("error");
        testTwo.setUnknownError("unkonw error");
        testThree.setContent("content");
        testDatas.add(testOne);
        testDatas.add(testTwo);
        testDatas.add(testThree);
        WeChatMomentsPresenter weChatMomentsPresenter = new WeChatMomentsPresenter(null);
        weChatMomentsPresenter.filterInvalidData(testDatas);
        Assert.assertEquals(1, weChatMomentsPresenter.getAllData().size());
    }
}