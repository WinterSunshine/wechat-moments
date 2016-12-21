package com.tw.mvp.base.tools;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Edison on 2016/10/24.
 */
public class ToastUtils
{
    private static Context context;
    private static Toast toast;

    private ToastUtils()
    {
    }
    public static void init(Context context)
    {
        ToastUtils.context = context;
    }
    public static void show(CharSequence text)
    {
        if (toast == null)
        {
            toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        } else
        {
            toast.setText(text);
        }
        toast.show();
    }
}
