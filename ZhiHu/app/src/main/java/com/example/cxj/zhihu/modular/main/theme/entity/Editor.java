package com.example.cxj.zhihu.modular.main.theme.entity;

/**
 * Created by cxj on 2016/1/26.
 * 该主题日报的编辑
 */
public class Editor {

    /**
     * 数据库中的唯一表示符
     */
    private Integer id;

    /**
     * 主编的姓名
     */
    private String name;

    /**
     * 主编的知乎用户主页
     */
    private String url;

    /**
     * 主编的个人简介
     */
    private String bio;

    /**
     * 主编的头像
     */
    private String avatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
