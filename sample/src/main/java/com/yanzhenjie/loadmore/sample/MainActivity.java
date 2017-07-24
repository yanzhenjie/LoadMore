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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;

/**
 * <p>
 * Demo入口。
 * </p>
 * Created by YanZhenjie on 2017/7/23.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.lv_main);
        Adapter adapter = new Adapter(Arrays.asList(getResources().getStringArray(R.array.item_main)));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                        break;
                    }
                    case 1: {
                        startActivity(new Intent(MainActivity.this, GridActivity.class));
                        break;
                    }
                    case 2: {
                        startActivity(new Intent(MainActivity.this, DefineActivity.class));
                        break;
                    }
                    case 3: {
                        startActivity(new Intent(MainActivity.this, HeaderActivity.class));
                        break;
                    }
                }
            }
        });
    }
}
