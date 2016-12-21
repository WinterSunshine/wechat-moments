/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.tw.mvp.view.custom;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.tw.mvp.R;
import com.tw.mvp.base.tools.ScreenUtils;
import com.tw.mvp.view.adapter.BaseQuickAdapter;

@SuppressLint("ClickableViewAccessibility")
public class XListView extends ListView implements OnScrollListener
{
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;
    private final static int SCROLL_DURATION = 400; // scroll back duration
    private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
    private int pageItemCount = 5;
    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener
    // the interface to trigger refresh and load more.
    private IXListViewListener mListViewListener;
    // -- header view
    private XListViewHeader mHeaderView;
    // header view content, use it to calculate the Header's height. And dismiss
    // it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    private int mHeaderViewHeight; // header view's height
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.
    // -- footer view
    private XListViewFooter mFooterLoadMoreView;
    private boolean mInitEnablePullLoad;
    private boolean mEnablePullLoad;
    private boolean mPullLoading;
    private boolean mIsFooterReady = false;
    // -- footer view for no data
    private View mFooterNoDataView;
    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;
    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private ImageView mNodataImg;
    private TextView mNodataText;
    @SuppressLint("UseSparseArrays")
    private SparseArray<Integer> drawables = new SparseArray<Integer>();
    // feature.

    /**
     * @param context
     */
    public XListView(Context context)
    {
        super(context);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context)
    {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);
        // init header view
        mHeaderView = new XListViewHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
        mHeaderView.setBackgroundColor(getResources().getColor(R.color.bg_content));
        addHeaderView(mHeaderView);
        // init footer view
        mFooterLoadMoreView = new XListViewFooter(context);
        // init footer view for no data
        mFooterNoDataView = View.inflate(context, R.layout.xlistview_nodata, null);
        //        mFooterNoDataView = LayoutInflater.from(context).inflate(R.layout.xlistview_nodata, this, false);
        mNodataText = (TextView) mFooterNoDataView.findViewById(R.id.tv_nodata_content);
        mNodataImg = (ImageView) mFooterNoDataView.findViewById(R.id.iv_nodata_img);
        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
        {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout()
            {
                mHeaderViewHeight = mHeaderViewContent.getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));
        //添加ImageLoader滑动加载图片
        mFooterNoDataView.setOnClickListener(null);
    }

    @Override
    public void setAdapter(ListAdapter adapter)
    {
        // make sure XListViewFooter is the last footer view, and only add once.
        if(!mIsFooterReady)
        {
            addFooterView(mFooterLoadMoreView);
            mIsFooterReady = true;
        }
        super.setAdapter(adapter);
    }

    /**
     * enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable)
    {
        mEnablePullRefresh = enable;
        if(!mEnablePullRefresh)
        { // disable, dismiss the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        }else
        {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable)
    {
        mInitEnablePullLoad = enable;
        mEnablePullLoad = enable;
        if(!mEnablePullLoad)
        {
            mFooterLoadMoreView.setOnClickListener(null);
            mFooterLoadMoreView.hide();
        }else
        {
            mPullLoading = false;
            mFooterLoadMoreView.show();
            mFooterLoadMoreView.setState(XListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterLoadMoreView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullLoadEnableInitialized(boolean enable)
    {
        if(mInitEnablePullLoad)
        {
            mEnablePullLoad = enable;
            if(!mEnablePullLoad)
            {
                mFooterLoadMoreView.setOnClickListener(null);
                mFooterLoadMoreView.hide();
            }else
            {
                mPullLoading = false;
                mFooterLoadMoreView.show();
                mFooterLoadMoreView.setState(XListViewFooter.STATE_NORMAL);
                // both "pull up" and "click" will invoke load more.
                mFooterLoadMoreView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        startLoadMore();
                    }
                });
            }
        }
    }

    /**
     * 上啦加载 ：开/关
     * <p/>
     * 根据当前列表中的数据条数，决定是否打开PullLoadMore。
     *
     * @param size
     */
    public void closePullLoadMore(int size)
    {
        if(size < pageItemCount)
        {
            setPullLoadEnableInitialized(false);
        }else
        {
            setPullLoadEnableInitialized(true);
        }
    }

    /**
     * 显示无数据界面
     *
     * @param errorCode
     * @param errorMsg
     */
    public void showNoDataView(Integer errorCode, String errorMsg)
    {
        View footView = getChildAt(getChildCount() - 1);
        if(footView != mFooterLoadMoreView)
        {
            addFooterView(mFooterLoadMoreView);
        }
        mNodataText.setText(errorMsg);
        mNodataImg.setImageResource(drawables.get(errorCode));
        int phoneHeight = ScreenUtils.getPhoneHeight(getContext());
        int toolbarHeight = (int) ScreenUtils.dpToPx(getContext(), 48);
        mFooterNoDataView.setMinimumHeight(phoneHeight - toolbarHeight);
        removeFooterView(mFooterLoadMoreView);
        addFooterView(mFooterNoDataView);
    }

    /**
     * 无数据界面恢复至有数据界面
     */
    public void showDataView()
    {
        View footView = getChildAt(getChildCount() - 1);
        if(footView == mFooterNoDataView)
        {
            removeFooterView(mFooterNoDataView);
            addFooterView(mFooterLoadMoreView);
        }
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh()
    {
        if(mPullRefreshing)
        {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore()
    {
        if(mPullLoading)
        {
            mPullLoading = false;
            mFooterLoadMoreView.setState(XListViewFooter.STATE_NORMAL);
            mFooterLoadMoreView.hide();
        }
    }

    private void invokeOnScrolling()
    {
        if(mScrollListener instanceof OnXScrollListener)
        {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta)
    {
        mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
        if(mEnablePullRefresh&&!mPullRefreshing)
        { // 未处于刷新状态，更新箭头
            if(mHeaderView.getVisiableHeight() > mHeaderViewHeight)
            {
                mHeaderView.setState(XListViewHeader.STATE_READY);
            }else
            {
                mHeaderView.setState(XListViewHeader.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight()
    {
        int height = mHeaderView.getVisiableHeight();
        if(height == 0) // not visible.
        {
            return;
        }
        // refreshing and header isn't shown fully. do nothing.
        if(mPullRefreshing&&height <= mHeaderViewHeight)
        {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if(mPullRefreshing&&height > mHeaderViewHeight)
        {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    private void updateFooterHeight(float delta)
    {
        int height = mFooterLoadMoreView.getBottomMargin() + (int) delta;
        if(mEnablePullLoad&&!mPullLoading)
        {
            if(height > PULL_LOAD_MORE_DELTA)
            { // height enough to invoke load
                // more.
                mFooterLoadMoreView.setState(XListViewFooter.STATE_READY);
            }else
            {
                mFooterLoadMoreView.setState(XListViewFooter.STATE_NORMAL);
            }
            mFooterLoadMoreView.setBottomMargin(height);
        }
        // setSelection(mTotalItemCount - 1); // scroll to bottom
    }

    private void resetFooterHeight()
    {
        int bottomMargin = mFooterLoadMoreView.getBottomMargin();
        if(bottomMargin > 0)
        {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore()
    {
        mPullLoading = true;
        mFooterLoadMoreView.setState(XListViewFooter.STATE_LOADING);
        if(mListViewListener != null)
        {
            mListViewListener.onLoadMore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if(mLastY == -1)
        {
            mLastY = ev.getRawY();
        }
        switch(ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if(getFirstVisiblePosition() == 0&&(mHeaderView.getVisiableHeight() > 0||deltaY > 0))
                {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                }else if(getLastVisiblePosition() == mTotalItemCount - 1&&(mFooterLoadMoreView.getBottomMargin() > 0||deltaY < 0))
                {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
                mLastY = -1; // reset
                if(getFirstVisiblePosition() == 0)
                {
                    // invoke refresh
                    if(mEnablePullRefresh&&mHeaderView.getVisiableHeight() > mHeaderViewHeight)
                    {
                        mPullRefreshing = true;
                        mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
                        if(mListViewListener != null)
                        {
                            mListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                }else if(getLastVisiblePosition() == mTotalItemCount - 1)
                {
                    // invoke load more.
                    if(mEnablePullLoad&&mFooterLoadMoreView.getBottomMargin() > PULL_LOAD_MORE_DELTA&&!mPullLoading)
                    {
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll()
    {
        if(mScroller.computeScrollOffset())
        {
            if(mScrollBack == SCROLLBACK_HEADER)
            {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            }else
            {
                mFooterLoadMoreView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l)
    {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        if(mScrollListener != null)
        {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if(mScrollListener != null)
        {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void setXListViewListener(IXListViewListener l)
    {
        mListViewListener = l;
    }

    public void cleartList()
    {
        BaseQuickAdapter<?> wrappedAdapter = (BaseQuickAdapter<?>) ((HeaderViewListAdapter) getAdapter()).getWrappedAdapter();
        wrappedAdapter.clearList();
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener
    {
        public void onXScrolling(View view);
    }
    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IXListViewListener
    {
        public void onRefresh();
        public void onLoadMore();
    }
}
