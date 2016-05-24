package com.example.cxj.zhihu.modular.detail.entity;

import android.content.Intent;

import java.util.List;

/**
 * Created by cxj on 2016/1/23.
 * 故事详情的实体对象
 */
public class DetailStory {

    /**
     * 网页源码,用WebView加载的
     */
    private String body;

    private String image_source;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片的地址
     */
    private String image;

    /**
     * 分享的地址
     */
    private String share_url;

    /**
     * 文章的所有提供者
     */
    private List<Recommender> recommenders;

    /**
     * 供 Google Analytics 使用
     */
    private String ga_prefix;

    /**
     * 栏目的信息
     */
    private Section section;

    /**
     * 新闻的类型
     */
    private Intent type;

    /**
     * 故事详情的id
     */
    private Integer id;

    /**
     * 供手机端的 WebView(UIWebView) 使用
     */
    private List<String> css;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public List<Recommender> getRecommenders() {
        return recommenders;
    }

    public void setRecommenders(List<Recommender> recommenders) {
        this.recommenders = recommenders;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Intent getType() {
        return type;
    }

    public void setType(Intent type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }
}
