package com.myideaway.coupon.model.coupon.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class CouponRSGetListData extends CouponJSONStringRemoteService {

    private int cityID = 0;
    private int sort = 0;
    private int page = 1;
    private int cateID = 0;
    private int areaID = 0;
    private double longitude = 108.90913391113281;
    private double latitude = 34.237911224365234;
    private String key = "";

    @Override
    public void whenPutParams() {
        putParam("city_id", cityID);
        putParam("pwd", "");
        putParam("sort", sort);
        putParam("page", page);
        putParam("email", "");
        putParam("cate_id", cateID);
        putParam("m_longitude", longitude);
        putParam("m_latitude", latitude);
        putParam("quan_id", areaID);
        putParam("act", "youhuilist");
        putParam("key", key);
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

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

