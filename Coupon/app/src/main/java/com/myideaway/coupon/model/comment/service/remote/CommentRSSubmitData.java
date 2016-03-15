package com.myideaway.coupon.model.comment.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class CommentRSSubmitData extends CouponJSONStringRemoteService {
    private String content;
    private String email;
    private  int userID;
    private  String password;

    @Override

    public void whenPutParams() {
        putParam("content",content);
        putParam("email",email);
        putParam("yh_id",userID);
        putParam("pwd",password);
        putParam("act","save_youhui_comment");

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

