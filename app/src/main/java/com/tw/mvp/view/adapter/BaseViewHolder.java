package com.tw.mvp.view.adapter;
import android.content.Context;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tw.mvp.R;
import com.tw.mvp.model.CommentsBean;
import com.tw.mvp.model.ImagesBean;
import com.tw.mvp.view.custom.SmartImageView;

import java.util.List;

public class BaseViewHolder
{
    private final SparseArray<View> mViews = new SparseArray<View>();
    private View mConvertView;
    private Context mContext;

    private BaseViewHolder(Context context, ViewGroup parent, int layoutID)
    {
        this.mContext = context;
        mConvertView = LayoutInflater.from(context).inflate(layoutID, parent, false);
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     */
    public static BaseViewHolder get(Context context, View convertView, ViewGroup parent, int layoutID)
    {
        if(convertView == null)
        {
            return new BaseViewHolder(context, parent, layoutID);
        }
        return (BaseViewHolder) convertView.getTag();
    }

    /**
     * 通过控件的ID获取对于的控件，如果没有则加入views
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewID)
    {
        View view = mViews.get(viewID);
        if(view == null)
        {
            view = mConvertView.findViewById(viewID);
            mViews.put(viewID, view);
        }
        return (T) view;
    }

    public View getConvertView()
    {
        return mConvertView;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewID
     * @param text
     * @return
     */
    public void setText(int viewID, String text)
    {
        TextView view = getView(viewID);
        view.setText(text);
    }

    /**
     * 为TextView设置中划线
     *
     * @param viewID
     */
    public void setTextFlags(int viewID)
    {
        TextView view = getView(viewID);
        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * 为LinearLayout 动态添加内容
     *
     * @param viewID
     * @param
     * @return
     */
    public void addViewsInLinearLayout(int viewID, View child)
    {
        LinearLayout layout = getView(viewID);
        layout.addView(child);
    }

    /**
     * 为LinearLayout 清除内容
     *
     * @param viewID
     * @param
     * @return
     */
    public void removeViewsInLinearLayout(int viewID)
    {
        LinearLayout layout = getView(viewID);
        layout.removeAllViews();
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewID
     * @param drawableID
     * @return
     */
    public void setImageResource(int viewID, int drawableID)
    {
        ImageView view = getView(viewID);
        view.setImageResource(drawableID);
    }

    /**
     * 隐藏ImageView
     *
     * @param viewID
     * @return
     */
    public void showOrHideView(int viewID, boolean isShowView)
    {
        View view = getView(viewID);
        if(isShowView)
        {
            view.setVisibility(View.VISIBLE);
        }else
        {
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 隐藏TextView
     *
     * @param viewID
     * @return
     */
    public void showOrHideTextView(int viewID, boolean isShowView)
    {
        View view = getView(viewID);
        if(isShowView)
        {
            view.setVisibility(View.VISIBLE);
        }else
        {
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置组件隐藏或显示
     *
     * @param viewID
     * @return
     */
    public void showOrGoneView(int viewID, boolean isShowView)
    {
        View view = getView(viewID);
        if(isShowView)
        {
            view.setVisibility(View.VISIBLE);
        }else
        {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 设置 点击事件
     *
     * @param viewID
     * @param onClickListener
     */
    public void setOnClickListener(int viewID, OnClickListener onClickListener)
    {
        View view = getView(viewID);
        view.setOnClickListener(onClickListener);
    }

    /**
     * 设置字体颜色
     *
     * @param viewID
     * @param color
     */
    public void setTextColor(int viewID, int color)
    {
        TextView view = getView(viewID);
        view.setTextColor(color);
    }

    /**
     * 设置字体背景颜色
     *
     * @param viewID
     * @param color
     */
    public void setTextBackgroundColor(int viewID, int color)
    {
        TextView view = getView(viewID);
        view.setBackgroundColor(color);
    }

    /**
     * 设置字体背景图片
     *
     * @param viewID
     * @param drawable
     */
    public void setTextBackgroundDrawable(int viewID, int drawable)
    {
        TextView view = getView(viewID);
        view.setBackgroundResource(drawable);
    }

    /**
     * 设置RatingBar星星个数
     *
     * @param viewID
     * @param score
     */
    public void setRatingBar(int viewID, String score)
    {
        RatingBar view = getView(viewID);
        view.setRating(Float.valueOf(score));
    }

    /**
     * 为SmratImageView设置图片、加载中图片、加载失败图片
     *
     * @param viewID
     * @param fallbackResource
     * @param loadingResource
     * @return
     */
    public void setImageUrl(int viewID, String url, Integer fallbackResource, Integer loadingResource)
    {
        SmartImageView view = getView(viewID);
        view.setImageUrl(url, fallbackResource, loadingResource);
    }

    public void setImageGridView(int viewID, List<ImagesBean> images)
    {
        GridView view = getView(viewID);
        BaseQuickAdapter<ImagesBean> adapter = new BaseQuickAdapter<ImagesBean>(mContext, R.layout.item_wechat_img)
        {
            @Override
            public void convert(int position, BaseViewHolder helper, ImagesBean item)
            {
                helper.setImageUrl(R.id.url, item.getUrl(), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            }
        };
        view.setAdapter(adapter);
        adapter.refreshGridOrListViews(images);
    }

    public void setCommentListView(int viewID, List<CommentsBean> comments)
    {
        ListView view = getView(viewID);
        BaseQuickAdapter<CommentsBean> adapter = new BaseQuickAdapter<CommentsBean>(mContext, R.layout.item_wechat_comment)
        {
            @Override
            public void convert(int position, BaseViewHolder helper, CommentsBean item)
            {
                helper.setText(R.id.nickname, item.getSender().getNick());
                helper.setText(R.id.content, item.getContent());
            }
        };
        view.setAdapter(adapter);
        adapter.refreshGridOrListViews(comments);
    }
}
