package com.example.cxj.huaishi.modular.main.home.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.cxj.huaishi.MyApp;
import com.example.cxj.huaishi.R;
import com.example.cxj.huaishi.common.Msg;
import com.example.cxj.huaishi.modular.login.adapter.DynamicListAdapter;
import com.example.cxj.huaishi.modular.postCard.entity.Card;
import com.example.cxj.huaishi.modular.postCard.ui.PostCardAct;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xiaojinzi.activity.fragment.BaseViewPagerFragment;
import xiaojinzi.annotation.Injection;
import xiaojinzi.annotation.ViewInjectionUtil;
import xiaojinzi.base.android.activity.ActivityUtil;
import xiaojinzi.base.android.adapter.listView.CommonAdapter;
import xiaojinzi.base.android.adapter.listView.CommonViewHolder;
import xiaojinzi.base.android.os.T;

/**
 * Created by cxj on 2016/6/13.
 * 显示首页帖子内容的fragment
 */
public class HomeFragment extends Fragment {

    @Injection(value = R.id.iv_frag_home_add, click = "viewClick")
    private ImageView iv_add = null;

    @Injection(R.id.lv_frag_home)
    private ListView lv;

    /**
     * 显示数据的适配器
     */
    private BaseAdapter adapter;

    /**
     * 要显示的数据
     */
    private List<Card> data = new ArrayList<Card>();

    /**
     * 请求数据的时候,需要带上这个日期去请求数据,分页的效果
     * 0的时候标识第一次
     * 不是为0的时候都是前一次请求数据的最后一个说说的日期值,靠这个获取过往的说说,每一次获取成功都更新这个值
     */
    private long lastDate = 0l;

    /**
     * 上下文
     */
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_home, null);

        context = getActivity();

        //让注解起作用
        ViewInjectionUtil.injectView(this, v);

        initView();

        initData();

        setOnListener();

        return v;
    }

    /**
     * 初始化控件
     */
    public void initView() {
    }

    /**
     * 加载数据
     */
    public void initData() {

        //创建显示的适配器
        adapter = new DynamicListAdapter(context, data, R.layout.dynamic_list_item);

        //设置适配器,开始集合长度为0,所以设置上没有效果,但是数据加载好了之后,改变原有集合并且通知改变,就有效果啦
        lv.setAdapter(adapter);

        //第一次加载数据
        initCardList();

    }

    private void setOnListener(){
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if(view.getLastVisiblePosition() == view.getCount() - 1){
                        initCardList();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    /**
     * 初始化动态列表,其实就是第一次获取
     */
    private void initCardList() {
        Call<String> call = MyApp.netWorkService.listCard(lastDate);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                try {
                    JSONObject jb = new JSONObject(body);
                    //获取是否成功的标识
                    String msg = jb.getString("msg");
                    if (Msg.OK.equals(msg)) { //标识获取数据成功
                        Type type = new TypeToken<ArrayList<Card>>() {
                        }.getType();
                        List<Card> tmp = MyApp.gson.fromJson(jb.getString("data"), type);
                        data.addAll(tmp);
                        if (data.size() > 0) {
                            //更新最后一个日期值,用于获取下一页数据
                            lastDate = data.get(data.size() - 1).getDate();
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    /**
     * 点击事件的集中处理
     *
     * @param v
     */
    public void viewClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.iv_frag_home_add: //如果要发表说说,就跳转到发表说说的界面
                ActivityUtil.startActivity(context, PostCardAct.class);
                break;
        }

    }

}
