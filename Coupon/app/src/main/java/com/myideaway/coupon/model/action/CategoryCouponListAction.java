package com.myideaway.coupon.model.action;

import com.myideaway.coupon.model.merchant.Category;

/**
 * Organization: http://www.myideaway.com
 * User: Tommy Chen
 * Date: 13-11-23
 * Time: AM11:27
 */
public class CategoryCouponListAction extends Action {
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
