package com.example.cxj.zhihu.modular.skin.entity;

import java.util.Date;

import xiaojinzi.dbOrm.android.annotation.Column;
import xiaojinzi.dbOrm.android.annotation.Table;


/**
 * app使用的皮肤实体对象
 *
 * @author xiaojinzi
 * @dateTime 2016年3月20日 下午5:12:19
 * @Company xjzCompany
 */
@Table("jsonSkin")
public class JsonSkin {

    /**
     * 白天使用的环境
     */
    public static final int DAY_ENVIRONMENT = 0;

    /**
     * 晚上使用的环境
     */
    public static final int NIGHT_ENVIRONMENT = 1;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "_id", autoPk = true)
    private Integer dbId;

    @Column(name = "jsonSkinId")
    private Integer id;

    /**
     * 皮肤的使用环境
     */
    @Column(name = "useEnvironment")
    private int useEnvironment = DAY_ENVIRONMENT;

    /**
     * 皮肤的名称
     */
    @Column(name = "skinName")
    private String name;

    /**
     * 皮肤的描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 皮肤的json数据,由Skin对象转化成json得来{@link com.example.cxj.zhihu.common.skin.Skin}
     */
    @Column(name = "jsonSkinData")
    private String skinJsonData;

    /**
     * 皮肤创建的时间
     */
    private Date date = null;

    /**
     * 示例图片地址
     */
    @Column(name = "imageUrl")
    private String imageUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUseEnvironment() {
        return useEnvironment;
    }

    public void setUseEnvironment(int useEnvironment) {
        this.useEnvironment = useEnvironment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkinJsonData() {
        return skinJsonData;
    }

    public void setSkinJsonData(String skinJsonData) {
        this.skinJsonData = skinJsonData;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
