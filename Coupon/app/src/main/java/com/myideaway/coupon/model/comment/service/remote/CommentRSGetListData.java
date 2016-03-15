package com.myideaway.coupon.model.comment.service.remote;

import com.myideaway.coupon.model.common.CouponJSONStringRemoteService;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class CommentRSGetListData extends CouponJSONStringRemoteService {
    private int couponID;
    private int page;
    @Override

    public void whenPutParams() {
        putParam("content","");
        putParam("pwd","");
        putParam("yh_id",couponID);
        putParam("act","youhui_comment_list");
        putParam("pwd","");
        putParam("page",page);
        putParam("email","");

    }

    public int getCouponID() {
        return couponID;
    }

    public void setCouponID(int couponID) {
        this.couponID = couponID;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

