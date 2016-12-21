package com.tw.mvp.view.activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tw.mvp.R;
import com.tw.mvp.model.TweetsBean;
import com.tw.mvp.model.UserBean;
import com.tw.mvp.presenter.WeChatMomentsPresenter;
import com.tw.mvp.view.adapter.BaseQuickAdapter;
import com.tw.mvp.view.adapter.BaseViewHolder;
import com.tw.mvp.view.base.BaseActivity;
import com.tw.mvp.view.custom.SmartImageView;
import com.tw.mvp.view.custom.XListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tw.mvp.R.id.avatar;

public class WeChatMomentsActivity extends BaseActivity implements XListView.IXListViewListener
{
    private XListView mListView;
    private SmartImageView mProfileImage;
    private SmartImageView mAvatarImage;
    private TextView mNickNameTxt;
    private WeChatMomentsPresenter mWeChatPresenter;
    private BaseQuickAdapter<TweetsBean> mBeanBaseQuickAdapter;
    private AtomicInteger pageNum = new AtomicInteger(1);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.onCreateView(R.layout.activity_we_chat);
    }

    @Override
    protected void findWidgets()
    {
        mListView = findView(R.id.ll_main_listView);
        View headerView = LayoutInflater.from(this).inflate(R.layout.head_listview, null);
        mProfileImage = (SmartImageView) headerView.findViewById(R.id.profile_image);
        mAvatarImage = (SmartImageView) headerView.findViewById(avatar);
        mNickNameTxt = (TextView) headerView.findViewById(R.id.nickname);
        mListView.addHeaderView(headerView);
    }

    @Override
    protected void initComponent()
    {
        mListView.setPullLoadEnable(true);
        mListView.setPullLoadEnable(true);
        initAdapter();
    }

    @Override
    protected void initAdapter()
    {
        mBeanBaseQuickAdapter = new BaseQuickAdapter<TweetsBean>(this, mListView, R.layout.item_wechat_main)
        {
            @Override
            public void convert(int position, BaseViewHolder helper, TweetsBean item)
            {
                helper.setImageUrl(R.id.avatar, item.getSender().getAvatar(), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                helper.setText(R.id.nickname, item.getSender().getNick());
                helper.setText(R.id.content, item.getContent());
                helper.setImageGridView(R.id.images, item.getImages());
                helper.setCommentListView(R.id.comments, item.getComments());
            }
        };
        mListView.setAdapter(mBeanBaseQuickAdapter);
    }

    @Override
    protected void initPresenter()
    {
        mWeChatPresenter = new WeChatMomentsPresenter(this);
    }

    @Override
    protected void initListener()
    {
        mListView.setXListViewListener(this);
    }

    @Override
    protected void asyncRetrive()
    {
        mWeChatPresenter.loadAllData();
    }

    protected void refreshUser(UserBean bean)
    {
        mProfileImage.setImageUrl(bean.getProfileImage(), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        mAvatarImage.setImageUrl(bean.getAvatar(), R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        mNickNameTxt.setText(bean.getNick());
    }

    protected void refreshTweets(ArrayList<TweetsBean> bean)
    {
        mWeChatPresenter.filterInvalidData(bean);
        mWeChatPresenter.loadPageData(pageNum.intValue());
    }

    public void showTweetPage(List<TweetsBean> pageData)
    {
        if(pageNum.intValue() == 1)
        {
            mBeanBaseQuickAdapter.pullRefresh(pageData);
        }else
        {
            mBeanBaseQuickAdapter.pullLoad(pageData);
        }
    }

    @Override
    public void onRefresh()
    {
        pageNum.set(1);
        mWeChatPresenter.loadPageData(pageNum.intValue());
    }

    @Override
    public void onLoadMore()
    {
        mWeChatPresenter.loadPageData(pageNum.incrementAndGet());
    }
}
