我的主页：[http://www.yanzhenjie.com](http://www.yanzhenjie.com)  
欢迎关注我的微博：[http://weibo.com/yanzhenjieit](http://weibo.com/yanzhenjieit)  

QQ技术交流群：[547839514](https://jq.qq.com/?_wv=1027&k=4CHkvzr)  

----

本库是基于`ListView`和`GridView`的封装，`ListView`、`GridView`加载更多。  
1. [为什么没有下拉刷新？](http://blog.csdn.net/yanzhenjie1003/article/details/75949335)  
2. [RecyclerView怎么办？](https://github.com/yanzhenjie/LoadMore)

# 截图
对上面提到的效果都例举演示，但不是全部，更多效果可以下载Demo查看。  
<image src="./image/1.gif" width="180px"/> <image src="./image/2.gif" width="180px"/> <image src="./image/3.gif" width="180px"/>

# 如何使用
首先引入`LoadMore`到你的项目中，然后就可以进行开发工作了。

## 引入
* Gradle
```groovy
compile 'com.yanzhenjie:loadmore:1.0.0'
```

* Maven
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>loadmore</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

## 开发
ListView：
```xml
<com.yanzhenjie.loadmore.LoadMoreListView
    .../>
```

一般的GridView，只带有addHeaderView()和addFooterView()的功能：
```xml
<com.yanzhenjie.loadmore.GridView
    .../>
```

在上述基础上再附加了加载更多功能的GridView：
```xml
<com.yanzhenjie.loadmore.LoadMoreGridView
    .../>
```

### GridView添加HeaderView和FooterView
这里唯一要注意的是：必须先addHeaderView()/addFooterView()，最后setAdapter()。
```
// HeaderView。
View headerView = ...;
gridView.addHeaderView(headerView);

// FooterView。
View footerView = ...;
gridView.addFooterView(footerView);

// 最后设置适配器。
gridView.setAdapter(new MainAdapter(getItemList()));
```

### 加载更多
本库默认提供了加载更多的动画和View，开发者也可以自定义。

因为`LoadMoreListView`和`LoadMoreGridView`的加载更多的Api完全相同，设计思想也完全相同，所以这里仅仅给出`LoadMoreListView`的示例：
```java
LoadMoreListView listView = ...;
...

listView.useDefaultLoadMore(); // 使用默认的加载更多的View。
listView.setLoadMoreListener(mLoadMoreListener); // 加载更多的监听。

LoadMoreListener mLoadMoreListener = new LoadMoreListener() {
    @Override
    public void onLoadMore() {
        // 该加载更多啦。
        
        ... // 请求数据，并更新数据源操作。
        mMainAdapter.notifyDataSetChanged();

        // 数据完更多数据，一定要调用这个方法。
        // 第一个参数：表示此次数据是否为空。
        // 第二个参数：表示是否还有更多数据。
        listView.loadMoreFinish(false, true);

        // 如果加载失败调用下面的方法，传入errorCode和errorMessage。
        // errorCode随便传，你自定义LoadMoreView时可以根据errorCode判断错误类型。
        // errorMessage是会显示到loadMoreView上的，用户可以看到。
        // listView.loadMoreError(0, "请求网络失败");
    }
};
```

自定义加载更多View也很简单，自定义一个View，并实现一个接口即可：
```java
public class DefineLoadMoreView extends LinearLayout
        implements LoadMoreView,
        View.OnClickListener {

    private LoadMoreListener mLoadMoreListener;

    public DefineLoadMoreView(Context context) {
        super(context);
        ...
        setOnClickListener(this);
    }

    /**
     * 马上开始回调加载更多了，这里应该显示进度条。
     */
    @Override
    public void onLoading() {
        // 展示加载更多的动画和提示信息。
        ...
    }

    /**
     * 加载更多完成了。
     *
     * @param dataEmpty 是否请求到空数据。
     * @param hasMore   是否还有更多数据等待请求。
     */
    @Override
    public void onLoadFinish(boolean dataEmpty, boolean hasMore) {
        // 根据参数，显示没有数据的提示、没有更多数据的提示。
        // 如果都不存在，则都不用显示。
    }

    /**
     * 加载出错啦，下面的错误码和错误信息二选一。
     *
     * @param errorCode    错误码。
     * @param errorMessage 错误信息。
     */
    @Override
    public void onLoadError(int errorCode, String errorMessage) {
    }

    /**
     * 调用了setAutoLoadMore(false)后，在需要加载更多的时候，此方法被调用，并传入listener。
     */
    @Override
    public void onWaitToLoadMore(LoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
        }

    /**
     * 非自动加载更多时mLoadMoreListener才不为空。
     */
    @Override
    public void onClick(View v) {
        if (mLoadMoreListener != null) mLoadMoreListener.onLoadMore();
    }
}
```

# 混淆
本库的所有类都是可以混淆的，如果混淆之后出现问题，请在你的混淆规则文件追加：
```
-dontwarn com.yanzhenjie.loadmore.**
-keep class com.yanzhenjie.loadmore.** {*;}
```

# 引用资料
* [cube-sdk](https://github.com/liaohuqiu/cube-sdk)
* [GridViewWithHeaderAndFooter](https://github.com/liaohuqiu/android-GridViewWithHeaderAndFooter/)
* [更纯粹的下拉刷新和加载更多](http://blog.csdn.net/yanzhenjie1003/article/details/75949335)

本库的加载更多的API参考了cube-sdk，GridView添加HeaderView来自GridViewWithHeaderAndFooter。  

特别感谢秋百万：[https://liaohuqiu.net](https://liaohuqiu.net)

# License
```text
Copyright 2017 Yan Zhenjie

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```