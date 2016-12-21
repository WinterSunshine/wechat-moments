package com.tw.mvp.view.base;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tw.mvp.WeChatMomentsApplication;
import com.tw.mvp.base.tools.ReflectUtils;
import com.tw.mvp.base.tools.ToastUtils;

public abstract class BaseActivity extends AppCompatActivity
{
    protected abstract void findWidgets();

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    protected void onCreateView(int layoutResID)
    {
        super.setContentView(layoutResID);
        findWidgets();
        initComponent();
        initAdapter();
        initListener();
        initPresenter();
        initHandler();
        excuteOther();
        asyncRetrive();
        pushAtyToStack();
    }

    /**
     * 后退按钮响应事件
     */
    public void onBackClick(View v)
    {
        finish();
    }

    @SuppressWarnings("unchecked")
    protected <T> T findView(int id)
    {
        return (T) findViewById(id);
    }

    /**
     * 初始化组件，子类根据需要自行重写
     */
    protected void initComponent()
    {
        return;
    }

    /**
     * 初始化Adapter，子类根据需要自行重写
     */
    protected void initAdapter()
    {
        return;
    }

    /**
     * 初始化Listener，子类根据需要自行重写
     */
    protected void initListener()
    {
        return;
    }

    /**
     * initPresenter，子类根据需要自行重写
     */
    protected void initPresenter()
    {
        return;
    }

    /**
     * 初始化Handler，子类根据需要自行重写
     */
    protected void initHandler()
    {
        return;
    }

    /**
     * 做一些其他的事情，如开启一个定时器或者线程、getIntentExtra、显示一个WebPage等等..
     */
    protected void excuteOther()
    {
        return;
    }

    /**
     * 异步查询网络数据，子类根据需要自行重写
     */
    protected void asyncRetrive()
    {
        return;
    }

    /**
     * 重试查询网络数据，子类根据需要自行重写
     */
    protected void retryRetrive()
    {
        ReflectUtils.invokeMethod(this, "asyncRetrive");
    }

    /**
     * 显示失败信息，默认显示吐司，子类有需要显示界面可自行重写
     */
    protected void showErrorMessage(Integer errorCode, String errorMessage)
    {
        ToastUtils.show(errorMessage);
    }

    /**
     * 把当前Activity Push栈中
     */
    private void pushAtyToStack()
    {
        WeChatMomentsApplication.push(this);
    }

    /**
     * 把当前Activity Pop出栈
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        WeChatMomentsApplication.pop(this);
    }
}
