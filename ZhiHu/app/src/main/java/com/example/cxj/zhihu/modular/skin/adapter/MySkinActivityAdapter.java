package com.example.cxj.zhihu.modular.skin.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.common.skin.Skin;
import com.example.cxj.zhihu.modular.skin.entity.JsonSkin;

import java.util.List;

import xiaojinzi.base.android.adapter.listView.CommonAdapter;
import xiaojinzi.base.android.adapter.listView.CommonViewHolder;


/**
 * Created by cxj on 2016/3/25.
 */
public class MySkinActivityAdapter extends CommonAdapter<JsonSkin> {

    /**
     * 选中的下标
     */
    private Integer[] selectIndex = null;

    /**
     * 构造函数
     *
     * @param context
     * @param data
     * @param layout_id
     */
    public MySkinActivityAdapter(Context context, List<JsonSkin> data, int layout_id, Integer[] selectIndex) {
        super(context, data, layout_id);
        this.selectIndex = selectIndex;
    }

    @Override
    public void convert(CommonViewHolder holder, JsonSkin item, int position) {
        TextView tv_name = holder.getView(R.id.tv_act_myskin_item_name);
        TextView tv_description = holder.getView(R.id.tv_act_myskin_item_description);
        ImageView iv_environment = holder.getView(R.id.iv_act_myskin_item_environment);
        View convertView = holder.getConvertView();
        if (position == selectIndex[0]) {
            convertView.setBackgroundColor(MyApp.skin.mySkinActSkin.getSelectSkinItemBgColor());
        } else {
            convertView.setBackgroundColor(MyApp.skin.mySkinActSkin.getListBgColor());
        }

        tv_name.setText(item.getName());
        tv_description.setText(item.getDescription());
        //图片资源第一个参数是图片本身的含义,第二个是使用环境
        if (MyApp.skin.getEnvironment() == Skin.DAY_ENVIRONMENT) {   //先判断当前皮肤的环境是白天还是晚上
            if (item.getUseEnvironment() == JsonSkin.DAY_ENVIRONMENT) {  //当前是晚上,这个皮肤是白天使用的
                iv_environment.setImageResource(R.mipmap.act_myskin_day_day);
            }else{
                iv_environment.setImageResource(R.mipmap.act_myskin_night_day);
            }
        }else{
            if (item.getUseEnvironment() == JsonSkin.DAY_ENVIRONMENT) {
                iv_environment.setImageResource(R.mipmap.act_myskin_day_night);
            }else{
                iv_environment.setImageResource(R.mipmap.act_myskin_night_night);
            }
        }

    }

}
