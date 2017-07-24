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
package com.yanzhenjie.loadmore.sample;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.loading.LoadingView;
import com.yanzhenjie.loadmore.LoadMoreListView;
import com.yanzhenjie.loadmore.LoadMoreListener;
import com.yanzhenjie.loadmore.LoadMoreView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 自定义加载更多的View。
 * </p>
 * Created by YanZhenjie on 2017/7/23.
 */
public class DefineActivity extends AppCompatActivity {

    private LoadMoreListView mLoadMoreListView;
    private Adapter mAdapter;
    private List<String> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLoadMoreListView = (LoadMoreListView) findViewById(R.id.alv_loadmore);
        // 使用自定义的加载更多的View。
        DefineLoadMoreView loadMoreView = new DefineLoadMoreView(this);
        mLoadMoreListView.addFooterView(loadMoreView);
        mLoadMoreListView.setLoadMoreView(loadMoreView);

        mLoadMoreListView.setLoadMoreListener(mLoadMoreListener); // 监听加载更多。

        mAdapter = new Adapter(mDataList);
        mLoadMoreListView.setAdapter(mAdapter);

        requestData();
    }

    /**
     * 第一次请求数据。
     */
    private void requestData() {
        List<String> strings = getDataList();
        mDataList.addAll(strings);

        mAdapter.notifyDataSetChanged();

        // 第一次加载数据，一定要调用这个方法，否则不会触发加载更多。
        // 第一个参数：表示此次数据是否为空。
        // 第二个参数：表示是否还有更多数据。
        mLoadMoreListView.loadMoreFinish(false, true);
    }

    /**
     * 加载更多的监听。
     */
    private LoadMoreListener mLoadMoreListener = new LoadMoreListener() {
        @Override
        public void onLoadMore() {
            mLoadMoreListView.postDelayed(new Runnable() { // 模拟请求服务器。
                @Override
                public void run() {
                    List<String> strings = getDataList();
                    mDataList.addAll(strings);

                    mAdapter.notifyDataSetChanged();

                    // 数据完更多数据，一定要掉用这个方法。
                    // 第一个参数：表示此次数据是否为空。
                    // 第二个参数：表示是否还有更多数据。
                    mLoadMoreListView.loadMoreFinish(false, true);

                    // 如果加载失败调用下面的方法，传入errorCode和errorMessage。
                    // errorCode随便传，你自定义LoadMoreView时可以根据errorCode判断错误类型。
                    // errorMessage是会显示到loadMoreView上的，用户可以看到。
                    // mLoadMoreListView.loadMoreError(0, "请求网络失败");
                }
            }, 1000);

        }
    };

    /**
     * 自定义加载动画的View，主要是需要实现LoadMoreView这个接口。
     */
    private static class DefineLoadMoreView extends LinearLayout implements LoadMoreView, View.OnClickListener {

        private LoadingView mLoadingView;
        private TextView mTvMessage;

        private LoadMoreListener mLoadMoreListener;

        DefineLoadMoreView(Context context) {
            super(context);
            inflate(getContext(), R.layout.loadmore_define, this);
            mLoadingView = (LoadingView) findViewById(R.id.loading_view);
            mTvMessage = (TextView) findViewById(R.id.tv_load_more_message);

            int color1 = getColor(R.color.colorAccent);
            int color2 = getColor(R.color.colorPrimary);
            int color3 = getColor(R.color.divider_color);

            mLoadingView.setCircleColors(color1, color2, color3);

            setOnClickListener(this);
        }

        /**
         * 马上开始回调加载更多了，这里应该显示进度条。
         */
        @Override
        public void onLoading() {
            setVisibility(VISIBLE);
            mLoadingView.setVisibility(VISIBLE);
            mTvMessage.setVisibility(VISIBLE);
            mTvMessage.setText(com.yanzhenjie.loadmore.R.string.loadmore_load_more_message);
        }

        /**
         * 加载更多完成了。
         *
         * @param dataEmpty 是否请求到空数据。
         * @param hasMore   是否还有更多数据等待请求。
         */
        @Override
        public void onLoadFinish(boolean dataEmpty, boolean hasMore) {
            if (!hasMore) {
                setVisibility(VISIBLE);

                if (dataEmpty) {
                    mLoadingView.setVisibility(GONE);
                    mTvMessage.setVisibility(VISIBLE);
                    mTvMessage.setText(com.yanzhenjie.loadmore.R.string.loadmore_data_empty);
                } else {
                    mLoadingView.setVisibility(GONE);
                    mTvMessage.setVisibility(VISIBLE);
                    mTvMessage.setText(com.yanzhenjie.loadmore.R.string.loadmore_more_not);
                }
            } else {
                setVisibility(INVISIBLE);
            }
        }

        /**
         * 调用了setAutoLoadMore(false)后，在需要加载更多的时候，这个方法会被调用，并传入加载更多的listener。
         */
        @Override
        public void onWaitToLoadMore(LoadMoreListener loadMoreListener) {
            this.mLoadMoreListener = loadMoreListener;

            setVisibility(VISIBLE);
            mLoadingView.setVisibility(GONE);
            mTvMessage.setVisibility(VISIBLE);
            mTvMessage.setText(com.yanzhenjie.loadmore.R.string.loadmore_click_load_more);
        }

        /**
         * 加载出错啦，下面的错误码和错误信息二选一。
         *
         * @param errorCode    错误码。
         * @param errorMessage 错误信息。
         */
        @Override
        public void onLoadError(int errorCode, String errorMessage) {
            setVisibility(VISIBLE);
            mLoadingView.setVisibility(GONE);
            mTvMessage.setVisibility(VISIBLE);
            mTvMessage.setText(TextUtils.isEmpty(errorMessage) ? getContext().getString(com.yanzhenjie.loadmore.R.string.loadmore_load_error) : errorMessage);
        }

        /**
         * 非自动加载更多时mLoadMoreListener才不为空。
         */
        @Override
        public void onClick(View v) {
            if (mLoadMoreListener != null) mLoadMoreListener.onLoadMore();
        }

        private int getColor(int resId) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                return getContext().getColor(resId);
            else
                return getResources().getColor(resId);
        }
    }


    private List<String> getDataList() {
        List<String> mDataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDataList.add("我是第%1$s个Item");
        }
        return mDataList;
    }

}
