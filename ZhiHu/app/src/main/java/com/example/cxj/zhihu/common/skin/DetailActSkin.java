package com.example.cxj.zhihu.common.skin;

/**
 * Created by cxj on 2016/3/15.
 */
public class DetailActSkin {
    /**
     * 默认是蓝色
     */
//    private String titleBarBgColor = "#00A2ED";
    private String titleBarBgColor = "#222222";

    public int getTitleBarBgColor() {
        return ColorUtil.getColor(titleBarBgColor);
    }

    //    private String viewPagerBgColor = "#FFFFFF";
    private String viewPagerBgColor = "#303030";

    public int getViewPagerBgColor() {
        return ColorUtil.getColor(viewPagerBgColor);
    }

    //    private String contentTextForeColor = "#000000";
    private String contentTextForeColor = "#DDDDDD";


    public int getContentTextForeColor() {
        return ColorUtil.getColor(contentTextForeColor);
    }
}
