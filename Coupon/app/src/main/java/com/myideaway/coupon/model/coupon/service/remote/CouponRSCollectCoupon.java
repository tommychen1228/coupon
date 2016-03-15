package com.myideaway.coupon.model.coupon.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created by duanchang on 13-12-10.
 */
public class CouponRSCollectCoupon extends CouponJSONStringRemoteService {
    private int couponid;
    private String password;
    private String email;

    @Override
    public void whenPutParams() {
        putParam("id",couponid);
        putParam("pwd",password);
        putParam("act","youhuiitem");
        putParam("act_2","sc");
        putParam("email",email);

    }

    public int getCouponid() {
        return couponid;
    }

    public void setCouponid(int couponid) {
        this.couponid = couponid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
