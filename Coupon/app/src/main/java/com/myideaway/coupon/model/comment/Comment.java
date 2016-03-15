package com.myideaway.coupon.model.comment;

import com.myideaway.coupon.model.user.User;

import java.util.Date;

/**
 * Organization: http://www.myideaway.com
 * User: Tommy Chen
 * Date: 13-11-23
 * Time: AM11:44
 */
public class Comment {
    private int id;
    private User writer;
    private Date createTime;
    private String content;
    private String createTimeString;

    public String getCreateTimeString() {
        return createTimeString;
    }

    public void setCreateTimeString(String createTimeString) {
        this.createTimeString = createTimeString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
