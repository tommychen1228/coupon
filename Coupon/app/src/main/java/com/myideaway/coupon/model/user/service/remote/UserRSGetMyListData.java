package com.myideaway.coupon.model.user.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class UserRSGetMyListData extends CouponJSONStringRemoteService {
    private String userID;
    private String password;
    private String userName;
    private String Email;

    @Override
    public void whenPutParams() {
        putParam("uid",userID);
        putParam("user_pwd",password);
        putParam("user_name", userName);
        putParam("email", Email);
        putParam("act", "mylist");
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
