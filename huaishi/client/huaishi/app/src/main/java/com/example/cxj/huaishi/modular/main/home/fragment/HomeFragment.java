package com.example.cxj.huaishi.modular.main.home.fragment;

import android.view.View;
import android.widget.ImageView;

import com.example.cxj.huaishi.R;
import com.example.cxj.huaishi.modular.postCard.ui.PostCardAct;

import xiaojinzi.activity.fragment.BaseViewPagerFragment;
import xiaojinzi.annotation.Injection;
import xiaojinzi.base.android.activity.ActivityUtil;

/**
 * Created by cxj on 2016/6/13.
 * 显示首页帖子内容的fragment
 */
public class HomeFragment extends BaseViewPagerFragment {

    @Injection(value = R.id.iv_frag_home_add,click = "viewClick")
    private ImageView iv_add = null;

    @Override
    public int getLayoutId() {
        return R.layout.frag_home;
    }

    public void viewClick(View v){

        int id = v.getId();

        switch (id) {
            case R.id.iv_frag_home_add:
                ActivityUtil.startActivity(context, PostCardAct.class);
                break;
        }

    }

}
