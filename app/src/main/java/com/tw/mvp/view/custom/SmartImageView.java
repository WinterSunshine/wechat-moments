package com.tw.mvp.view.custom;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tw.mvp.WeChatMomentsApplication;

public class SmartImageView extends ImageView
{
    public SmartImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public SmartImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SmartImageView(Context context)
    {
        super(context);
    }

    public void setImageUrl(String url)
    {
        ImageLoader.getInstance().displayImage(url, this);
    }

    public void setImageUrl(String url, Integer fallbackResource, Integer loadingResource)
    {
        ImageLoader.getInstance().displayImage(url, this, WeChatMomentsApplication.getSimpleOptions(fallbackResource, loadingResource));
    }
}
