package com.myideaway.coupon.model.user.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class UserRSLoginData extends CouponJSONStringRemoteService {
    private String email;
    private String password;

    @Override
    public void whenPutParams() {
        putParam("act", "login");
        putParam("email", email);
        putParam("pwd", password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
