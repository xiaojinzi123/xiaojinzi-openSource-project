package com.example.cxj.zhihu.modular.welcome.entity;

/**
 * Created by cxj on 2016/1/19.
 * 启动图片的响应实体对象
 */
public class StartImage {

    /**
     * 图片的文本说明
     */
    private String text;

    /**
     * 图片的地址
     */
    private String img;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
