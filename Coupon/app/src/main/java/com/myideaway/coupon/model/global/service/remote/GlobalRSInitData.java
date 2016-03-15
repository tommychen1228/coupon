package com.myideaway.coupon.model.global.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class GlobalRSInitData extends CouponJSONStringRemoteService {
    private String cityName;
    @Override
    public void whenPutParams() {
        putParam("act", "init");
        putParam("cur_city_name", cityName);
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
