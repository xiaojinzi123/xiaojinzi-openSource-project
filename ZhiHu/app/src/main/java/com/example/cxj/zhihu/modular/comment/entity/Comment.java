package com.example.cxj.zhihu.modular.comment.entity;

/**
 * Created by cxj on 2016/3/19.
 * 评论的实体对象
 */
public class Comment {
    /**
     * 是不是头
     */
    private boolean isHeader;

    /**
     * 评论数量
     */
    private int commentNumber;

    /**
     * 评论者的唯一标识
     */
    private String id;

    /**
     * 作者
     */
    private String author;

    /**
     * 评论的内容
     */
    private String content;

    /**
     * 评论所获『赞』的数量
     */
    private String likes;

    /**
     * 评论的时间
     */
    private String time;

    /**
     * 用户头像图片的地址
     */
    private String avatar;

    public boolean isHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
