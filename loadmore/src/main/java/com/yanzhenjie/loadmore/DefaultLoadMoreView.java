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
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.loading.LoadingView;

/**
 * Created by YanZhenjie on 2017/7/21.
 */
public class DefaultLoadMoreView extends LinearLayout implements LoadMoreView, View.OnClickListener {

    private LoadingView mLoadingView;
    private TextView mTvMessage;

    private LoadMoreListener mLoadMoreListener;

    public DefaultLoadMoreView(Context context) {
        super(context);
        inflate(getContext(), R.layout.loadmore_default, this);
        mLoadingView = (LoadingView) findViewById(R.id.loading_view);
        mTvMessage = (TextView) findViewById(R.id.tv_load_more_message);

        int color1 = getColor(R.color.loadmore_color_loading_color1);
        int color2 = getColor(R.color.loadmore_color_loading_color2);
        int color3 = getColor(R.color.loadmore_color_loading_color3);

        mLoadingView.setCircleColors(color1, color2, color3);

        setOnClickListener(this);
    }

    private int getColor(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return getContext().getColor(resId);
        else
            return getResources().getColor(resId);
    }

    @Override
    public void onLoading() {
        setVisibility(VISIBLE);
        mLoadingView.setVisibility(VISIBLE);
        mTvMessage.setVisibility(VISIBLE);
        mTvMessage.setText(R.string.loadmore_load_more_message);
    }

    @Override
    public void onLoadFinish(boolean dataEmpty, boolean hasMore) {
        if (!hasMore) {
            setVisibility(VISIBLE);

            if (dataEmpty) {
                mLoadingView.setVisibility(GONE);
                mTvMessage.setVisibility(VISIBLE);
                mTvMessage.setText(R.string.loadmore_data_empty);
            } else {
                mLoadingView.setVisibility(GONE);
                mTvMessage.setVisibility(VISIBLE);
                mTvMessage.setText(R.string.loadmore_more_not);
            }
        } else {
            setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onWaitToLoadMore(LoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;

        setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mTvMessage.setVisibility(VISIBLE);
        mTvMessage.setText(R.string.loadmore_click_load_more);
    }

    @Override
    public void onLoadError(int errorCode, String errorMessage) {
        setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mTvMessage.setVisibility(VISIBLE);
        mTvMessage.setText(TextUtils.isEmpty(errorMessage) ? getContext().getString(R.string.loadmore_load_error) : errorMessage);
    }

    @Override
    public void onClick(View v) {
        if (mLoadMoreListener != null) mLoadMoreListener.onLoadMore();
    }
}
