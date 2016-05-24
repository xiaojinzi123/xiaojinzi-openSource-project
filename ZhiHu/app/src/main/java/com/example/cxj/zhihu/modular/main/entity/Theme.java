package com.example.cxj.zhihu.modular.main.entity;

/**
 * Created by cxj on 2016/1/19.
 * 主题
 */
public class Theme {

    /**
     * 颜色，作用未知
     */
    private String color;

    /**
     * 描述
     */
    private String description;

    /**
     * 供显示的图片地址
     */
    private String thumbnail;

    /**
     * 主题的名称
     */
    private String name;

    /**
     * 主题的id
     */
    private Integer id;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
