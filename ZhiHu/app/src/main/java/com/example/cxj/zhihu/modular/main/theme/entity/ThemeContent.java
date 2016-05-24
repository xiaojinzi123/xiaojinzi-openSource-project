package com.example.cxj.zhihu.modular.main.theme.entity;

import com.example.cxj.zhihu.modular.main.home.entity.Story;

import java.util.List;

/**
 * Created by cxj on 2016/1/26.
 * 主题内容
 */
public class ThemeContent {

    /**
     * 对应的故事的集合
     */
    private List<Story> stories;

    /**
     * 该主题日报的介绍
     */
    private String description;

    /**
     * 该主题日报的背景图片（大图）
     */
    private String background;

    /**
     *颜色，作用未知
     */
    private String color;

    /**
     * 该主题日报的名称
     */
    private String name;

    /**
     * 背景图片的小图版本
     */
    private String image;

    /**
     * 图像的版权信息
     */
    private String image_source;

    /**
     * 该主题日报的编辑
     */
    private List<Editor> editors;

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public List<Editor> getEditors() {
        return editors;
    }

    public void setEditors(List<Editor> editors) {
        this.editors = editors;
    }
}
