package com.example.cxj.zhihu.common.skin;

/**
 * Created by cxj on 2016/3/15.
 * 故事详情的皮肤
 */
public class DetailFragmentSkin {

    /**
     * webview文字的暗色,默认是黑色
     */
    private String webViewTextColor = "#000000";
//    private String webViewTextColor = "#999999";

    public String getWebViewTextColor() {
        return webViewTextColor.startsWith("#")?webViewTextColor:"#" + webViewTextColor;
    }

        private String webViewBgColor = "#FFFFFF";
//    private String webViewBgColor = "#303030";

    public int getWebViewBgColor() {
        return ColorUtil.getColor(webViewBgColor);
    }
}
