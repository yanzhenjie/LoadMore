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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.yanzhenjie.loadmore.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * GridView添加HeaderView和FooterView。
 * </p>
 * Created by YanZhenjie on 2017/7/23.
 */
public class HeaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridView gridView = (GridView) findViewById(R.id.alv_loadmore);

        // HeaderView。
        View headerView = getLayoutInflater().inflate(R.layout.layout_header, gridView, false);
        headerView.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "HeaderView", Toast.LENGTH_SHORT).show();
            }
        });
        gridView.addHeaderView(headerView);

        // FooterView。
        View footerView = getLayoutInflater().inflate(R.layout.layout_footer, gridView, false);
        footerView.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "FooterView", Toast.LENGTH_SHORT).show();
            }
        });
        gridView.addFooterView(footerView);

        Adapter adapter = new Adapter(getDataList());
        gridView.setAdapter(adapter);
    }

    private List<String> getDataList() {
        List<String> mDataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDataList.add("我是第%1$s个Item");
        }
        return mDataList;
    }
}
