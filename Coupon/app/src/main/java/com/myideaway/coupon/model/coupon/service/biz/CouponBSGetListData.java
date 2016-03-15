package com.myideaway.coupon.model.coupon.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.coupon.Coupon;
import com.myideaway.coupon.model.coupon.service.remote.CouponRSGetListData;
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
public class CouponBSGetListData extends BizService {
    private int cityID;
    private int sort;
    private int page;
    private int cateID;
    private int areaID;
    private float longitude;
    private float latitude;
    private String key;

    public CouponBSGetListData(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        CouponRSGetListData couponRSGetListData = new CouponRSGetListData();
        CouponBSGetDataResult couponBSGetDataResult = new CouponBSGetDataResult();
        couponRSGetListData.setAreaID(areaID);
        couponRSGetListData.setCateID(cateID);
        couponRSGetListData.setCityID(cityID);
        couponRSGetListData.setLatitude(latitude);
        couponRSGetListData.setLongitude(longitude);
        couponRSGetListData.setPage(page);
        couponRSGetListData.setSort(sort);
        couponRSGetListData.setKey(key);
        JSONObject couponJsonObject = (JSONObject) couponRSGetListData.syncExecute();

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
        couponBSGetDataResult.setCouponList(couponList);

        int page = couponJsonObject.optJSONObject("page").optInt("page");
        couponBSGetDataResult.setPage(page);
        int pageTotal = couponJsonObject.optJSONObject("page").optInt("page_total");
        couponBSGetDataResult.setPageTotal(pageTotal);


        return couponBSGetDataResult;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCateID() {
        return cateID;
    }

    public void setCateID(int cateID) {
        this.cateID = cateID;
    }

    public int getAreaID() {
        return areaID;
    }

    public void setAreaID(int areaID) {
        this.areaID = areaID;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public class CouponBSGetDataResult {
        List<Coupon> couponList = new ArrayList<Coupon>();
        int page;
        int pageTotal;

        public List<Coupon> getCouponList() {
            return couponList;
        }

        public void setCouponList(List<Coupon> couponList) {
            this.couponList = couponList;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageTotal() {
            return pageTotal;
        }

        public void setPageTotal(int pageTotal) {
            this.pageTotal = pageTotal;
        }
    }
}

