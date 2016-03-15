package com.myideaway.coupon.model.coupon.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class CouponRSGetNearListData extends CouponJSONStringRemoteService {
    private int cityID = 0;
    private int cateID = 0;
    private int page = 1;
    private float longitude_left;
    private float latitude_top;
    private float longitude_right;
    private float latitude_bottom;
    private float m_longitude;
    private float m_latitude;

    @Override
    public void whenPutParams() {
        putParam("city_id", cityID);
        putParam("page", page);
        putParam("cate_id", cateID);
        putParam("pwd", "");
        putParam("nearby_id", "0");
        putParam("email", "");
        putParam("act", "nearbyyouhui");

        putParam("m_longitude", m_longitude);
        putParam("m_latitude", m_latitude);

        putParam("longitude_left", longitude_left);
        putParam("latitude_top", latitude_top);
        putParam("longitude_right", longitude_right);
        putParam("latitude_bottom", latitude_bottom);


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
}

