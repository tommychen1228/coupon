package com.myideaway.coupon.model.coupon;

import com.myideaway.coupon.model.merchant.Shop;
import com.myideaway.coupon.model.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Organization: http://www.myideaway.com
 * User: Tommy Chen
 * Date: 13-11-23
 * Time: AM8:37
 * To change this template use File | Settings | File Templates.
 */
public class Coupon {
    private int id;
    private String title;
    private String iconUrl;
    private float latitude;
    private float longitude;
    private String address;
    private String imageUrl;
    private String content;
    private Date createTime;
    private Date beginTime;
    private Date endTime;
    private String expireTip;
    private String shareContent;
    private int commentCount;
    private float distance;
    private List<CouponComment> comments;
    private Shop shop;
    private List<User> favorers = new ArrayList<User>();


    public Coupon() {
        comments = new ArrayList<CouponComment>();
    }

    public List<User> getFavorers() {
        return favorers;
    }

    public void setFavorers(List<User> favorers) {
        this.favorers = favorers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getExpireTip() {
        return expireTip;
    }

    public void setExpireTip(String expireTip) {
        this.expireTip = expireTip;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<CouponComment> getComments() {
        return comments;
    }

    public void setComments(List<CouponComment> comments) {
        this.comments = comments;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
