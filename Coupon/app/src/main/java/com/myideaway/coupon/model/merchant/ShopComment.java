package com.myideaway.coupon.model.merchant;

import com.myideaway.coupon.model.comment.Comment;

/**
 * Organization: http://www.myideaway.com
 * User: Tommy Chen
 * Date: 13-11-23
 * Time: PM12:09
 */
public class ShopComment extends Comment {
    private Shop shop;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
