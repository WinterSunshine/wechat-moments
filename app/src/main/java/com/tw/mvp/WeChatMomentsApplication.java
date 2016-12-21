package com.tw.mvp;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Process;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tw.mvp.base.tools.ToastUtils;

import java.util.Stack;

public class WeChatMomentsApplication extends Application
{
    private static Stack<Activity> atyStack = new Stack<Activity>();
    private static DisplayImageOptions options;
    private static DisplayImageOptions.Builder builder;

    @Override
    public void onCreate()
    {
        super.onCreate();
        ToastUtils.init(this);
        initImageLoader(getApplicationContext());
        initSimpleOption();
    }

    public static void initImageLoader(Context context)
    {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.threadPoolSize(4);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.memoryCache(new WeakMemoryCache());
        config.tasksProcessingOrder(QueueProcessingType.FIFO);
        ImageLoader.getInstance().init(config.build());
    }

    public static DisplayImageOptions getSimpleOptions(Integer fallbackResource, Integer loadingResource)
    {
        options = builder.showImageOnLoading(loadingResource).
                showImageForEmptyUri(fallbackResource).showImageOnFail(fallbackResource).build();
        return options;
    }

    private void initSimpleOption()
    {
        builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)// 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .resetViewBeforeLoading(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565);// 设置图片的解码类型//
    }

    public static void push(Activity aty)
    {
        atyStack.push(aty);
    }

    public static void pop(Activity aty)
    {
        atyStack.remove(aty);
    }

    public static void closeSeries(Class<? extends Activity> cls)
    {
        for(int pos = findPos(cls); pos < atyStack.size() - 1; )
        {
            atyStack.pop().finish();
        }
    }

    private static int findPos(Class<? extends Activity> cls)
    {
        for(int pos = 0; pos < atyStack.size(); pos++)
        {
            if(atyStack.get(pos).getClass() == cls)
            {
                return pos;
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        while(!atyStack.empty())
        {
            atyStack.pop().finish();
        }
        Process.killProcess(Process.myPid());
    }
}
