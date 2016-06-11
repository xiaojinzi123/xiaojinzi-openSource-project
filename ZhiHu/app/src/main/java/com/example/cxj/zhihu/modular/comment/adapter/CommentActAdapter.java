package com.example.cxj.zhihu.modular.comment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cxj.zhihu.MyApp;
import com.example.cxj.zhihu.R;
import com.example.cxj.zhihu.common.skin.Skin;
import com.example.cxj.zhihu.modular.comment.entity.Comment;
import com.example.cxj.zhihu.modular.comment.entity.TmpInfo;

import java.util.Date;
import java.util.List;

import xiaojinzi.base.android.adapter.listView.CommonAdapter;
import xiaojinzi.base.android.adapter.listView.CommonViewHolder;
import xiaojinzi.base.android.os.ScreenUtils;

import xiaojinzi.base.java.common.DateUtil;
import xiaojinzi.imageLoad.ImageLoad;


/**
 * Created by cxj on 2016/3/19.
 */
public class CommentActAdapter extends CommonAdapter<Comment> {

    private DateUtil dateUtil = new DateUtil(DateUtil.CHINADATETIME_STYLE1);

    /**
     * 标题栏的高度
     */
    private int titleBarHeight = 0;

    /**
     * 短评的头所在的位置
     */
//    private int shortCommentTagPosition = -1;

    /**
     * 是否展开短评
     */
//    private boolean isOpenShortComment = false;

    private TmpInfo tmpInfo = null;

    /**
     * 构造函数
     *
     * @param context
     * @param data
     * @param layout_id
     */
    public CommentActAdapter(Context context, List<Comment> data, int layout_id, int titleBarHeight, TmpInfo tmpInfo) {
        super(context, data, layout_id);
        this.titleBarHeight = titleBarHeight;
        this.tmpInfo = tmpInfo;
    }

    @Override
    public int getCount() {
        if (tmpInfo.isOpenShortComment) {
            return data.size();
        } else {
            return tmpInfo.shortCommentTagPosition + 1;
        }
    }

    @Override
    public void convert(CommonViewHolder h, Comment item, int position) {

        //头的部分
        RelativeLayout rl_header = h.getView(R.id.rl_act_comment_item_header);
        TextView tv_commentNumber = h.getView(R.id.tv_act_comment_item_comment_numner);
        ImageView iv_menu = h.getView(R.id.iv_act_comment_item_menu);

        //内容的部分
        RelativeLayout rl_content = h.getView(R.id.rl_act_comment_item_content);
        ImageView iv_author_icon = h.getView(R.id.iv_act_comment_item_author_icon);
        TextView tv_author_name = h.getView(R.id.tv_act_comment_item_author_name);
        TextView tv_zan = h.getView(R.id.tv_act_comment_item_zan);
        TextView tv_content = h.getView(R.id.tv_act_comment_item_content);
        TextView tv_time = h.getView(R.id.tv_act_comment_item_time);

        //没有长评的时候显示的沙发
        ImageView iv_shafa = h.getView(R.id.iv_act_comment_item_shafa);
        iv_shafa.setVisibility(View.GONE);

        //获取条目的View
        ViewGroup.LayoutParams lp_convertView = h.getConvertView().getLayoutParams();

        if (item.isHeader()) { //如果是头
            rl_header.setVisibility(View.VISIBLE);
            rl_content.setVisibility(View.GONE);
            //加载主题样式
            tv_commentNumber.setTextColor(MyApp.skin.commentActSkin.getCommentTextColor());
            if (position == 0) {
                tv_commentNumber.setText(item.getCommentNumber() + "条长评");
                iv_menu.setVisibility(View.GONE);
            } else {
                iv_menu.setVisibility(View.VISIBLE);
                tv_commentNumber.setText(item.getCommentNumber() + "条短评");
                if (tmpInfo.isOpenShortComment) {
                    iv_menu.setImageResource(R.mipmap.act_comment_close);
                } else {
                    iv_menu.setImageResource(R.mipmap.act_comment_open);
                }
            }
            //如果第二条也是一个头
            if (position == 0 && data.size() > 1 && data.get(1).isHeader() && !tmpInfo.isOpenShortComment) {
                //获取屏幕的高度
                int screenHeight = ScreenUtils.getScreenHeight(context);
                rl_header.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //获取条目头的高度
                int measuredHeight = rl_header.getMeasuredHeight();
                lp_convertView.height = screenHeight - measuredHeight - titleBarHeight;
                iv_shafa.setVisibility(View.VISIBLE);
                if (MyApp.skin.getEnvironment() == Skin.DAY_ENVIRONMENT) {
                    iv_shafa.setImageResource(R.mipmap.act_comment_shafa_day);
                }else{
                    iv_shafa.setImageResource(R.mipmap.act_comment_shafa_night);
                }
            } else {
                lp_convertView.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        } else {
            rl_header.setVisibility(View.GONE);
            rl_content.setVisibility(View.VISIBLE);
            //加载样式
            tv_author_name.setTextColor(MyApp.skin.commentActSkin.getCommentTextColor());
            tv_zan.setTextColor(MyApp.skin.commentActSkin.getCommentTextColor());
            tv_content.setTextColor(MyApp.skin.commentActSkin.getCommentTextColor());
            tv_time.setTextColor(MyApp.skin.commentActSkin.getCommentTextColor());
            //加载头像
            ImageLoad.getInstance().asyncLoadImage(iv_author_icon, item.getAvatar());
            tv_author_name.setText(item.getAuthor());
            tv_zan.setText(item.getLikes());
            tv_content.setText(item.getContent());
            long tmp = Long.parseLong(item.getTime()) * 1000;
            tv_time.setText(dateUtil.formatDate(new Date(tmp)));
            lp_convertView.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }

}
