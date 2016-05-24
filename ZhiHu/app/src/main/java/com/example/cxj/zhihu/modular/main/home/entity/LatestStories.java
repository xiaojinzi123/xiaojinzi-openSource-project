package com.example.cxj.zhihu.modular.main.home.entity;

import java.util.List;

/**
 * Created by cxj on 2016/1/20.
 * 最新故事实体对象
 */
public class LatestStories {

    //日期
    private String date;

    /**
     * 要显示的故事条目,不是详情,而是简单的显示一下
     */
    private List<Story> stories;

    /**
     * 上面的广告
     */
    private List<Story> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<Story> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<Story> top_stories) {
        this.top_stories = top_stories;
    }
}
