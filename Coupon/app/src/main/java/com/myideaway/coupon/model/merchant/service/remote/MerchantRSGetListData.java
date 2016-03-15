package com.myideaway.coupon.model.merchant.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class MerchantRSGetListData extends CouponJSONStringRemoteService {
    private int merchantID;
    private int cityID;
    private int page;

    @Override

    public void whenPutParams() {
        putParam("city_id", cityID);
        putParam("pwd", "");
        putParam("act", "merchantyouhui");
        putParam("merchant_id", merchantID);
        putParam("page", page);
        putParam("email", "");
    }

    public int getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(int merchantID) {
        this.merchantID = merchantID;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

