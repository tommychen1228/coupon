package com.myideaway.coupon.model.coupon.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;
import com.myideaway.coupon.model.coupon.Coupon;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class CouponRSGetDetailData extends CouponJSONStringRemoteService {

    private Coupon coupon;
    private String email = "";
    private String password = "";

    @Override
    public void whenPutParams() {
        putParam("id", coupon.getId());
        putParam("pwd", password);
        putParam("act_2", "");
        putParam("act", "youhuiitem");
        putParam("email", email);
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
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

