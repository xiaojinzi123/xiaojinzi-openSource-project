package com.example.cxj.zhihu.modular.main.entity;

import java.util.List;

/**
 * Created by cxj on 2016/1/19.
 */
public class ThemeList {

    private String limit;

    /**
     * 主题的列表
     */
    private List<Theme> others;

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public List<Theme> getOthers() {
        return others;
    }

    public void setOthers(List<Theme> others) {
        this.others = others;
    }

}
