package com.example.cxj.zhihu.modular.main.home.entity;

import java.util.List;



/**
 * Created by cxj on 2016/1/20.
 * 故事
 */
public class Story {

    private Integer id;

    private String title;

    private String ga_prefix;


    private List<String> images;

    private String image;

    private Integer type;

    private boolean isTag = false;

    private String date = null;

    //另外加的字段

    /**
     * 是否已经点击过了
     */
    private boolean isClick;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isTag() {
        return isTag;
    }

    public void setIsTag(boolean isTag) {
        this.isTag = isTag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
    }
}
