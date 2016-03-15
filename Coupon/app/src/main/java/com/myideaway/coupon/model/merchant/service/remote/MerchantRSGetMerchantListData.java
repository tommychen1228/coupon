package com.myideaway.coupon.model.merchant.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class MerchantRSGetMerchantListData extends CouponJSONStringRemoteService {
    private int brandID;
    private int page;

    @Override

    public void whenPutParams() {
        putParam("brand_id", brandID);
        putParam("page", page);
        putParam("email", "");
        putParam("act", "merchantlist");
        putParam("m_latitude", "");
        putParam("m_longitude", "");
    }

    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

