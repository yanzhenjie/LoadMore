/*
 * Copyright 2017 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.loadmore;

import android.content.Context;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.widget.AbsListView;

/**
 * Created by YanZhenjie on 2017/7/23.
 */
public class LoadMoreGridView extends GridView {

    private boolean isLoadMore = false;
    private boolean isAutoLoadMore = true;
    private boolean isLoadError = false;

    private boolean mDataEmpty = true;
    private boolean mHasMore = false;

    private LoadMoreListener mLoadMoreListener;
    private LoadMoreView mLoadMoreView;

    private OnScrollListener mOnScrollListener;

    private NestedScrollingChildHelper mScrollingChildHelper;

    public LoadMoreGridView(Context context) {
        this(context, null, 0);
    }

    public LoadMoreGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        super.setOnScrollListener(new OnScrollListener() {

            int mScrollState = -1;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (null != mOnScrollListener) {
                    mOnScrollListener.onScrollStateChanged(view, scrollState);
                }

                mScrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (null != mOnScrollListener) {
                    mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }

                if (firstVisibleItem + visibleItemCount >= totalItemCount - 1 &&
                        (mScrollState == SCROLL_STATE_TOUCH_SCROLL || mScrollState == SCROLL_STATE_FLING)) {
                    dispatchLoadMore();
                }
            }
        });
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    /**
     * Listen to load more.
     */
    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    private void dispatchLoadMore() {
        if (isLoadError) return;

        if (!isAutoLoadMore) {
            if (mLoadMoreView != null)
                mLoadMoreView.onWaitToLoadMore(mLoadMoreListener);
        } else {
            if (isLoadMore || mDataEmpty || !mHasMore) return;

            isLoadMore = true;

            if (mLoadMoreView != null)
                mLoadMoreView.onLoading();

            if (mLoadMoreListener != null)
                mLoadMoreListener.onLoadMore();
        }
    }

    /**
     * Use the default to load more View.
     */
    public void useDefaultLoadMore() {
        DefaultLoadMoreView defaultLoadMoreView = new DefaultLoadMoreView(getContext());
        addFooterView(defaultLoadMoreView);
        setLoadMoreView(defaultLoadMoreView);
    }

    /**
     * Load more view.
     */
    public void setLoadMoreView(LoadMoreView loadMoreView) {
        mLoadMoreView = loadMoreView;
    }

    /**
     * Automatically load more automatically.
     * <p>
     * Non-auto-loading mode, you can to click on the item to load.
     * </p>
     *
     * @param autoLoadMore you can use false.
     * @see LoadMoreView#onWaitToLoadMore(LoadMoreListener)
     */
    public void setAutoLoadMore(boolean autoLoadMore) {
        isAutoLoadMore = autoLoadMore;
    }

    /**
     * Load more done.
     *
     * @param dataEmpty data is empty ?
     * @param hasMore   has more data ?
     */
    public final void loadMoreFinish(boolean dataEmpty, boolean hasMore) {
        isLoadMore = false;
        isLoadError = false;

        mDataEmpty = dataEmpty;
        mHasMore = hasMore;

        if (mLoadMoreView != null) {
            mLoadMoreView.onLoadFinish(dataEmpty, hasMore);
        }
    }

    /**
     * Called when data is loaded incorrectly.
     *
     * @param errorCode    Error code, will be passed to the LoadView, you can according to it to customize the prompt information.
     * @param errorMessage Error message.
     */
    public void loadMoreError(int errorCode, String errorMessage) {
        isLoadMore = false;
        isLoadError = true;

        if (mLoadMoreView != null) {
            mLoadMoreView.onLoadError(errorCode, errorMessage);
        }
    }
}
