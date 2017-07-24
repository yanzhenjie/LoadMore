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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.yanzhenjie.loadmore.LoadMoreGridView;
import com.yanzhenjie.loadmore.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * GridView LoadMore.
 * </p>
 * Created by YanZhenjie on 2017/7/23.
 */
public class GridActivity extends AppCompatActivity {

    private LoadMoreGridView mLoadMoreListView;
    private Adapter mAdapter;
    private List<String> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLoadMoreListView = (LoadMoreGridView) findViewById(R.id.alv_loadmore);
        mLoadMoreListView.useDefaultLoadMore(); // 使用默认的加载功能的View。
        mLoadMoreListView.setLoadMoreListener(mLoadMoreListener); // 监听加载更多。

        mAdapter = new Adapter(mDataList);
        mLoadMoreListView.setAdapter(mAdapter);

        requestData();
    }

    /**
     * First request data.
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
     * LoadMore listener.
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

    private List<String> getDataList() {
        List<String> mDataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mDataList.add("我是第%1$s个Item");
        }
        return mDataList;
    }

}
