package com.example.cxj.zhihu.modular.detail.entity;

/**
 * Created by cxj on 2016/1/23.
 */
public class StoryExtraInfo {

    /**
     * 长评的个数
     */
    private String long_comments;

    /**
     * 点赞总数
     */
    private String popularity;

    /**
     * 评论总数
     */
    private String comments;

    /**
     * 短评的个数
     */
    private String short_comments;

    public String getLong_comments() {
        return long_comments;
    }

    public void setLong_comments(String long_comments) {
        this.long_comments = long_comments;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(String short_comments) {
        this.short_comments = short_comments;
    }
}
