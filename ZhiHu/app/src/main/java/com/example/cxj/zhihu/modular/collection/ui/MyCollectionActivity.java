package com.example.cxj.zhihu.modular.collection.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.common.skin.Skin;
import com.example.cxj.zhihu.modular.collection.adapter.MyCollectionActAdapter;
import com.example.cxj.zhihu.modular.collection.db.MyCollectionDb;
import com.example.cxj.zhihu.modular.collection.entity.DbStory;
import com.example.cxj.zhihu.modular.collection.entity.TmpImfo;
import com.example.cxj.zhihu.modular.detail.ui.DetailActivity;

import java.util.List;

import xiaojinzi.activity.BaseActivity;

import xiaojinzi.base.android.log.L;
import xiaojinzi.viewAnnotation.Injection;


/**
 * 我的收藏
 */
public class MyCollectionActivity extends BaseActivity {

    public static final String TAG = "MyCollectionActivity";

    @Injection(R.id.rl_act_my_collection_container)
    private RelativeLayout rl_container = null;

    @Injection(R.id.rl_act_my_collection_titlebar)
    private RelativeLayout rl_titleBar = null;

    @Injection(value = R.id.iv_act_my_collection_back, click = "clickView")
    private ImageView iv_back = null;

    @Injection(R.id.iv_act_my_collection_empty)
    private ImageView iv_empty = null;

    @Injection(R.id.lv_act_my_collection)
    private ListView lv = null;

    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            afterLoadData();
        }
    };

    /**
     * 上下文对象
     */
    private Context context = null;

    /**
     * 要显示的数据
     */
    private List<DbStory> stories;

    /**
     * listview的适配器
     */
    private BaseAdapter adapter = null;

    @Override
    public int getLayoutId() {
        return R.layout.act_my_collection;
    }

    @Override
    public void initView() {
        context = this;
        //加载样式
        loadStyle();
    }

    @Override
    public void initData() {
        //加载数据
        asyncLoadData();
    }

    /**
     * 异步加载数据,不卡顿
     */
    private void asyncLoadData() {
        //获取要显示的数据
        stories = MyCollectionDb.getInstance(context).queryAll();
        h.sendEmptyMessage(0);
    }

    /**
     * 再加载数据之后
     */
    private void afterLoadData() {
        if (stories.size() == 0) {
            iv_empty.setVisibility(View.VISIBLE);
        }else{
            //创建显示数据的适配器对象
            adapter = new MyCollectionActAdapter(context, stories, R.layout.horizontal_story_item);
            //挂载适配器对象
            lv.setAdapter(adapter);
        }
    }

    @Override
    public void setOnlistener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onEventGoToDetail(position);
            }
        });
    }

    //私有方法 start

    /**
     * 加载样式
     */
    private void loadStyle() {
        rl_container.setBackgroundColor(MyApp.skin.myCollectionActSkin.getContainerBgColor());
        rl_titleBar.setBackgroundColor(MyApp.skin.myCollectionActSkin.getTitleBarBgColor());
        if (MyApp.skin.getEnvironment() == Skin.DAY_ENVIRONMENT) {
            iv_empty.setImageResource(R.mipmap.act_my_collection_empty_day);
        }else{
            iv_empty.setImageResource(R.mipmap.act_my_collection_empty_night);
        }
    }

    //私有方法结束 end

    /**
     * view点击事件集中处理
     *
     * @param v
     */
    public void clickView(View v) {
        int id = v.getId();
        if (id == R.id.iv_act_my_collection_back) {
            finish();
        }
    }

    /**
     * 去详情的界面
     *
     * @param position
     */
    public void onEventGoToDetail(Integer position) {
        Intent i = new Intent(context, DetailActivity.class);
        String ids = getAllStoryId();
        i.putExtra("ids", ids);
        i.putExtra("position", position);
        i.putExtra("from", MyCollectionActivity.TAG);
        context.startActivity(i);
    }

    /**
     * 取消收藏某一项
     *
     * @param position
     */
    public void onEventCancelCollect(Integer position) {
        int index = position;
        DbStory dbStory = stories.get(index);
        MyCollectionDb.getInstance(context).delete(dbStory);
        stories.remove(index);
        if (stories.size() == 0) {
            iv_empty.setVisibility(View.VISIBLE);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 刷新列表
     */
    public void onEventFreshCollectData() {
        List<DbStory> dbStories = MyCollectionDb.getInstance(context).queryAll();
        stories.clear();
        stories.addAll(dbStories);
        L.s("stories.size() = " + stories.size());
        if (stories.size() == 0) {
            iv_empty.setVisibility(View.VISIBLE);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取所有的id,用&标识符连接起来
     *
     * @return
     */
    private String getAllStoryId() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < stories.size(); i++) {
            DbStory story = stories.get(i);
            sb.append(story.getId() + "&");
        }
        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }

}
