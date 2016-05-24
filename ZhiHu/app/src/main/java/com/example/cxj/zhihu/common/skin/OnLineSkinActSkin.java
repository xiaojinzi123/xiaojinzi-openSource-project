package com.example.cxj.zhihu.common.skin;

/**
 * Created by cxj on 2016/3/25.
 */
public class OnLineSkinActSkin {

    /**
     * 默认是蓝色
     */
    private String titleBarBgColor = "#00A2ED";
//    private String titleBarBgColor = "#222222";

    public int getTitleBarBgColor() {
        return ColorUtil.getColor(titleBarBgColor);
    }

    /**
     * 容器的背景
     */
    private String containerBgColor = "#FFFFFF";
//    private String containerBgColor = "#303030";


    public int getContainerBgColor() {
        return ColorUtil.getColor(containerBgColor);
    }

    /**
     * viewpager的背景
     */
    private String viewPagerBgColor = "#FFFFFF";
//    private String viewPagerBgColor = "#303030";


    public int getViewPagerBgColor() {
        return ColorUtil.getColor(containerBgColor);
    }

}
