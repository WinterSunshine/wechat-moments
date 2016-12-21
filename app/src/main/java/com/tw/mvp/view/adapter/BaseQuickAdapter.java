package com.tw.mvp.view.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tw.mvp.view.custom.XListView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseQuickAdapter<T> extends BaseAdapter
{
    protected final int mItemID;
    protected Context mContext;
    protected List<T> mDatas = new ArrayList<T>();
    private XListView mXListView;

    public BaseQuickAdapter(Context context, int itemID)
    {
        this.mContext = context;
        this.mItemID = itemID;
    }

    public BaseQuickAdapter(Context context, XListView xListView, int itemID)
    {
        this.mContext = context;
        this.mXListView = xListView;
        this.mItemID = itemID;
    }

    public abstract void convert(int position, BaseViewHolder helper, T item);

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public T getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    /**
     * 上拉加载
     */
    public void pullLoad(List<T> datas)
    {
        mXListView.closePullLoadMore(datas.size());
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 第一次刷新或下拉刷新
     */
    public void pullRefresh(List<T> datas)
    {
        mXListView.stopRefresh();
        mXListView.showDataView();
        mXListView.closePullLoadMore(datas.size());
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 第一次刷新或下拉刷新不分页
     */
    public void pullRefreshNoPage(List<T> datas)
    {
        mXListView.stopRefresh();
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 普通表格或列表刷新，不带上拉和下滑加载
     */
    public void refreshGridOrListViews(List<T> datas)
    {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 清空list
     */
    public void clearList()
    {
        mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        BaseViewHolder viewHolder = BaseViewHolder.get(mContext, convertView, parent, mItemID);
        convert(position, viewHolder, mDatas.get(position));
        return viewHolder.getConvertView();
    }
}
