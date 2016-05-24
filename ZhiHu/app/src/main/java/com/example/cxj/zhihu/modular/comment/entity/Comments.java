package com.example.cxj.zhihu.modular.comment.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxj on 2016/3/19.
 * 总的评论的实体类
 */
public class Comments {

    /**
     * 长评的集合
     */
    private List<Comment> longComments = new ArrayList<Comment>();

    /**
     * 短评的集合
     */
    private List<Comment> shortComments = new ArrayList<Comment>();

    /**
     * 临时的集合,是因为这个集合的名字和返回的json数据中的名字是一样的,方便注入,一旦注入成功这个数据就会被转走
     */
    private List<Comment> comments;

    public List<Comment> getLongComments() {
        return longComments;
    }

    public void setLongComments(List<Comment> longComments) {
        this.longComments = longComments;
    }

    public List<Comment> getShortComments() {
        return shortComments;
    }

    public void setShortComments(List<Comment> shortComments) {
        this.shortComments = shortComments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
