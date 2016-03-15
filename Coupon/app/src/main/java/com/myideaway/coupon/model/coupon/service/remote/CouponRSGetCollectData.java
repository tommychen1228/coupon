package com.myideaway.coupon.model.coupon.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created by duanchang on 13-12-10.
 */
public class CouponRSGetCollectData extends CouponJSONStringRemoteService {
    private int page;
    private String password;
    private String email;

    @Override
    public void whenPutParams() {
        putParam("pwd",password);
        putParam("page",page);
        putParam("act","mycollect");
        putParam("email",email);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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
