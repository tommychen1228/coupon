package com.myideaway.coupon.model.coupon.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.coupon.Coupon;
import com.myideaway.coupon.model.coupon.service.remote.CouponRSGetNearListData;
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
public class CouponBSGetNearListData extends BizService {
    private int cityID;
    private int cateID;
    private int page;
    private float longitude_left;
    private float latitude_top;
    private float longitude_right;
    private float latitude_bottom;
    private float m_longitude;
    private float m_latitude;

    public CouponBSGetNearListData(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        CouponRSGetNearListData couponRSGetNearListData = new CouponRSGetNearListData();
        CouponBSGetNearListDataResult couponBSGetNearListDataResult = new CouponBSGetNearListDataResult();
        couponRSGetNearListData.setCityID(cityID);
        couponRSGetNearListData.setCateID(cateID);
        couponRSGetNearListData.setPage(page);
        couponRSGetNearListData.setLongitude_left(longitude_left);
        couponRSGetNearListData.setLatitude_top(latitude_top);
        couponRSGetNearListData.setLongitude_right(longitude_right);
        couponRSGetNearListData.setLatitude_bottom(latitude_bottom);
        couponRSGetNearListData.setM_longitude(m_longitude);
        couponRSGetNearListData.setM_latitude(m_latitude);
        JSONObject couponJsonObject = (JSONObject) couponRSGetNearListData.syncExecute();

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
            coupon.setDistance((float) couponJSONObject.optDouble("distance"));

//            coupon.setBeginTime(couponJSONObject.optString("begin_time_format"));
//            coupon.setEndTime(couponJSONObject.optInt("id"));
//            coupon.setComments(couponJSONObject.optString("address"));
//            coupon.setShop(couponJSONObject.optString("address"));
//            coupon.setShareContent(couponJSONObject.optString("address"));
//            coupon.setCreateTime(couponJSONObject.optString("address"));
//            coupon.setImageUrl(couponJSONObject.optString("address"));
//            coupon.setExpireTip(couponJSONObject.optString("address"));
            couponList.add(coupon);
        }
        couponBSGetNearListDataResult.setCouponList(couponList);

        String page = couponJsonObject.optJSONObject("page").optString("page");
        couponBSGetNearListDataResult.setPage(page);
        String pageTotal = couponJsonObject.optJSONObject("page").optString("page_total");
        couponBSGetNearListDataResult.setPageTotal(pageTotal);


        return couponBSGetNearListDataResult;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public int getCateID() {
        return cateID;
    }

    public void setCateID(int cateID) {
        this.cateID = cateID;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public float getLongitude_left() {
        return longitude_left;
    }

    public void setLongitude_left(float longitude_left) {
        this.longitude_left = longitude_left;
    }

    public float getLatitude_top() {
        return latitude_top;
    }

    public void setLatitude_top(float latitude_top) {
        this.latitude_top = latitude_top;
    }

    public float getLongitude_right() {
        return longitude_right;
    }

    public void setLongitude_right(float longitude_right) {
        this.longitude_right = longitude_right;
    }

    public float getLatitude_bottom() {
        return latitude_bottom;
    }

    public void setLatitude_bottom(float latitude_bottom) {
        this.latitude_bottom = latitude_bottom;
    }

    public float getM_longitude() {
        return m_longitude;
    }

    public void setM_longitude(float m_longitude) {
        this.m_longitude = m_longitude;
    }

    public float getM_latitude() {
        return m_latitude;
    }

    public void setM_latitude(float m_latitude) {
        this.m_latitude = m_latitude;
    }

    public class CouponBSGetNearListDataResult {
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

