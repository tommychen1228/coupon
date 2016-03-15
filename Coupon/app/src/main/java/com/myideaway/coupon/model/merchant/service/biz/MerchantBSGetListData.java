package com.myideaway.coupon.model.merchant.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.coupon.Coupon;
import com.myideaway.coupon.model.merchant.service.remote.MerchantRSGetListData;
import com.myideaway.easyapp.core.service.BizService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class MerchantBSGetListData extends BizService {
    private int merchantID;
    private int cityID;
    private int page;

    public MerchantBSGetListData(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        MerchantRSGetListData merchantRSGetListData = new MerchantRSGetListData();
        MerchantBSGetListDataResult merchantBSGetListDataResult = new MerchantBSGetListDataResult();
        merchantRSGetListData.setCityID(cityID);
        merchantRSGetListData.setMerchantID(merchantID);
        merchantRSGetListData.setPage(page);

        JSONObject couponJsonObject = (JSONObject) merchantRSGetListData.syncExecute();

        JSONArray couponJSONArray = couponJsonObject.optJSONArray("item");

        List<Coupon> couponList = new ArrayList<Coupon>();
        for (int i = 0; i < couponJSONArray.length(); i++) {
            Coupon coupon = new Coupon();
            JSONObject couponJSONObject = couponJSONArray.optJSONObject(i);
            coupon.setId(couponJSONObject.optInt("id"));
            coupon.setTitle(couponJSONObject.optString("title"));
            coupon.setAddress(couponJSONObject.optString("address"));
            coupon.setCommentCount(couponJSONObject.optInt("comment_count"));
            coupon.setContent(couponJSONObject.optString("content"));
            coupon.setIconUrl(couponJSONObject.optString("merchant_logo"));
            coupon.setLatitude((float) couponJSONObject.optDouble("ypoint"));
            coupon.setLongitude((float) couponJSONObject.optDouble("xpoint"));
            couponList.add(coupon);

        }
        merchantBSGetListDataResult.setCouponList(couponList);

        String page = couponJsonObject.optJSONObject("page").optString("page");
        merchantBSGetListDataResult.setPage(page);
        String pageTotal = couponJsonObject.optJSONObject("page").optString("page_total");
        merchantBSGetListDataResult.setPageTotal(pageTotal);


        return merchantBSGetListDataResult;
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

    public class MerchantBSGetListDataResult {
        List<Coupon> couponList = new ArrayList<Coupon>();
        String page;
        String pageTotal;

        public List<Coupon> getCouponList() {
            return couponList;
        }

        public void setCouponList(List<Coupon> couponList) {
            this.couponList = couponList;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPageTotal() {
            return pageTotal;
        }

        public void setPageTotal(String pageTotal) {
            this.pageTotal = pageTotal;
        }
    }
}

