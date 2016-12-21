package com.tw.mvp.base.tools;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tw.mvp.R;

import java.util.concurrent.ConcurrentHashMap;

public class DialogUtils
{
    private static ConcurrentHashMap<String, Dialog> loadingCache = new ConcurrentHashMap<String, Dialog>();

    private DialogUtils()
    {
    }

    public static void showLoading(Object context, String refreshMethod)
    {
        Dialog loadingDialog;
        if(isFragment(context))
        {
            loadingDialog = createDialog(((Fragment) context).getActivity(), R.style.Dialog_Loading);
        }else
        {
            loadingDialog = createDialog(((FragmentActivity) context), R.style.Dialog_Loading);
        }
        loadingCache.put(refreshMethod, loadingDialog);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCanceledOnTouchOutside(false);
        if(!loadingDialog.isShowing())
        {
            loadingDialog.show();
        }
    }

    public static void dismissLoading(String refreshMethod)
    {
        Dialog dialog = loadingCache.get(refreshMethod);
        if(dialog != null)
        {
            loadingCache.remove(refreshMethod);
            dialog.dismiss();
        }
    }

    public static Dialog createDialog(Context context, int styleRes)
    {
        return new Dialog(context, styleRes);
    }

    private static boolean isFragment(Object context)
    {
        return context instanceof Fragment;
    }
}
