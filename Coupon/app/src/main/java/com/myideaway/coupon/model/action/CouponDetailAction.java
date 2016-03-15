package com.myideaway.coupon.model.action;

import com.myideaway.coupon.model.coupon.Coupon;

/**
 * Organization: http://www.myideaway.com
 * User: Tommy Chen
 * Date: 13-11-23
 * Time: AM11:26
 */
public class CouponDetailAction extends Action {
    private Coupon coupon;

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
