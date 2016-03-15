package com.myideaway.coupon.model.coupon.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.coupon.Coupon;
import com.myideaway.coupon.model.coupon.service.remote.CouponRSGetByUserData;
import com.myideaway.easyapp.core.service.BizService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duanchang on 13-12-10.
 */
public class CouponBSGetByUserData extends BizService {
    private int page;
    private String password;
    private String email;

    public CouponBSGetByUserData(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        CouponRSGetByUserData couponRSGetByUserData = new CouponRSGetByUserData();
        CouponBSGetByUserDataResult couponBSGetByUserDataResult = new CouponBSGetByUserDataResult();
        couponRSGetByUserData.setEmail(email);
        couponRSGetByUserData.setPage(page);
        couponRSGetByUserData.setPassword(password);
        JSONObject couponJsonObject = (JSONObject) couponRSGetByUserData.syncExecute();

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
        couponBSGetByUserDataResult.setCouponList(couponList);

        String page = couponJsonObject.optJSONObject("page").optString("page");
        couponBSGetByUserDataResult.setPage(page);
        String pageTotal = couponJsonObject.optJSONObject("page").optString("page_total");
        couponBSGetByUserDataResult.setPageTotal(pageTotal);


        return couponBSGetByUserDataResult;
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

    public class CouponBSGetByUserDataResult {
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
