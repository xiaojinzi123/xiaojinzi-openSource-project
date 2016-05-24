package com.example.cxj.zhihu.common.skin;

/**
 * Created by cxj on 2016/3/20.
 * 评论界面的皮肤设定
 */
public class CommentActSkin {

    /**
     * 默认是蓝色
     */
    private String titleBarBgColor = "#00A2ED";
//    private String titleBarBgColor = "#222222";

    public int getTitleBarBgColor() {
        return ColorUtil.getColor(titleBarBgColor);
    }


    /**
     * 容器背景颜色
     */
    private String containerBgColor = "#FFFFFF";
//    private String containerBgColor = "#303030";


    public int getContainerBgColor() {
        return ColorUtil.getColor(containerBgColor);
    }

    private String listBgColor = "#FFFFFF";
//    private String listBgColor = "#303030";

    public int getListBgColor() {
        return ColorUtil.getColor(listBgColor);
    }

    private String commentTextColor = "#000000";
//    private String commentTextColor = "#FFFFFF";

    public int getCommentTextColor() {
        return ColorUtil.getColor(commentTextColor);
    }

}
