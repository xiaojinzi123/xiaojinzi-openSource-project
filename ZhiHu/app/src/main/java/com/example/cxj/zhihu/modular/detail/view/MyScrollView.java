package com.example.cxj.zhihu.modular.detail.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by cxj on 2016/3/29.
 */
public class MyScrollView extends ScrollView {
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChange != null) {
            onScrollChange.onScrollChanged(l, t, oldl, oldt);
        }
    }




    private OnScrollChange onScrollChange = null;

    public void setOnScrollChange(OnScrollChange onScrollChange) {
        this.onScrollChange = onScrollChange;
    }

    /**
     * 滚动的监听
     */
    public interface OnScrollChange {
        /**
         * 回掉的方法
         *
         * @param l
         * @param t
         * @param oldl
         * @param oldt
         */
        public void onScrollChanged(int l, int t, int oldl, int oldt);
    }

}
