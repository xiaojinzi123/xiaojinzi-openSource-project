package com.example.cxj.zhihu.common.skin;

import android.graphics.Color;

/**
 * Created by cxj on 2016/3/24.
 */
public class ColorUtil {

    public static int getColor(String color) {
        try {
            if (color == null || "".equals(color)) {
                return Color.WHITE;
            } else {
                if (color.startsWith("#")) {
                    return Color.parseColor(color);
                } else {
                    return Color.parseColor("#" + color);
                }
            }
        } catch (Exception e) {
            return Color.WHITE;
        }
    }

}
