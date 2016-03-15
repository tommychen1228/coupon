package com.myideaway.coupon.model.user.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created with IntelliJ IDEA.
 * User: duancahng
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class UserRSRegisterData extends CouponJSONStringRemoteService {
    private String email;
    private String userName;
    private String password;
    private String telPhone;
    private int gender;

    @Override
    public void whenPutParams() {
        putParam("gender", gender);
        putParam("user_name",userName );
        putParam("user_phoneNum", telPhone);
        putParam("act", "register");
        putParam("password", password);
        putParam("email", email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
