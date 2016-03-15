package com.myideaway.coupon.model.action;

import com.myideaway.coupon.model.coupon.Coupon;
import com.myideaway.coupon.model.merchant.Category;

import org.json.JSONObject;

/**
 * Organization: http://www.myideaway.com
 * User: Tommy Chen
 * Date: 13-11-23
 * Time: AM11:25
 */
public abstract class Action {
    public final static int ABOUT_US_ACTION = 19;
    public final static int CATEGORY_COUPON_LIST_ACTION = 12;
    public final static int COUPON_DETAIL_ACTION = 17;
    public final static int NEARBY_ACTION = 22;
    public final static int NOTICE_LIST_ACTION = 21;
    public final static int SEARCH_COUPON_ACTION = 23;
    public final static int SEARCH_SHOP_ACTION = 25;
    public final static int URL_ACTION = 2;

    public static Action generateAction(int type, JSONObject data) {
        if (type == URL_ACTION) {
            UrlAction urlAction = new UrlAction();
            String url = data.optString("url");
            urlAction.setUrl(url);
            return urlAction;
        } else if (type == SEARCH_SHOP_ACTION) {
            SearchShopAction searchShopAction = new SearchShopAction();
            return searchShopAction;
        } else if (type == SEARCH_COUPON_ACTION) {
            SearchCouponAction searchCouponAction = new SearchCouponAction();
            return searchCouponAction;
        } else if (type == NOTICE_LIST_ACTION) {
            NoticeListAction noticeListAction = new NoticeListAction();
            return noticeListAction;
        } else if (type == NEARBY_ACTION) {
            NearbyAction nearbyAction = new NearbyAction();
            return nearbyAction;
        } else if (type == COUPON_DETAIL_ACTION) {
            CouponDetailAction couponDetailAction = new CouponDetailAction();
            Coupon coupon = new Coupon();
            coupon.setId(data.optInt("data_id"));
            couponDetailAction.setCoupon(coupon);
            return couponDetailAction;
        } else if (type == CATEGORY_COUPON_LIST_ACTION) {
            CategoryCouponListAction categoryCouponListAction = new CategoryCouponListAction();
            Category category = new Category();
            category.setId(data.optInt("cate_id"));
            categoryCouponListAction.setCategory(category);
            return categoryCouponListAction;
        } else if (type == ABOUT_US_ACTION) {
            AboutUsAction aboutUsAction = new AboutUsAction();
            return aboutUsAction;
        }else {
            return null;
        }

    }
}
