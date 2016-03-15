package com.myideaway.coupon.model.coupon.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.coupon.service.remote.CouponRSCollectCoupon;
import com.myideaway.easyapp.core.service.BizService;

/**
 * Created by duanchang on 13-12-10.
 */
public class CouponBSCollectCoupon extends BizService {
    private int couponid;
    private String password;
    private String email;

    public CouponBSCollectCoupon(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        CouponBSCollectCouponResult couponBSCollectCouponResult = new CouponBSCollectCouponResult();
        CouponRSCollectCoupon couponRSCollectCoupon = new CouponRSCollectCoupon();
        couponRSCollectCoupon.setEmail(email);
        couponRSCollectCoupon.setPassword(password);
        couponRSCollectCoupon.setCouponid(couponid);
        couponRSCollectCoupon.asyncExecute();

        return couponBSCollectCouponResult;
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

    public class CouponBSCollectCouponResult {

    }
}
