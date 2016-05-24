package com.example.cxj.zhihu.modular.collection.entity;

import java.util.List;

import xiaojinzi.dbOrm.android.annotation.Column;
import xiaojinzi.dbOrm.android.annotation.Table;


/**
 * Created by cxj on 2016/4/1.
 */
@Table("dbStory")
public class DbStory {

    @Column(name = "_id", autoPk = true)
    private Integer dbId;

    @Column(name = "dbStoryId")
    private Integer id;

    @Column
    private String title;

    @Column
    private String ga_prefix;

    @Column
    private String image;

    @Column
    private Integer type;

    @Column
    private String date = null;

    public Integer getDbId() {
        return dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

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


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
