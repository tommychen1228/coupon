package com.myideaway.coupon.model.global.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class GlobalRSInitHomePageData extends CouponJSONStringRemoteService {
    @Override
    public void whenPutParams() {
        putParam("act", "index");
        putParam("page", "1");
        putParam("email", "");
        putParam("pwd", "");
    }
}
