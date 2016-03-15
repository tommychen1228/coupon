package com.myideaway.coupon.model.coupon.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.coupon.Coupon;
import com.myideaway.coupon.model.coupon.service.remote.CouponRSGetDetailData;
import com.myideaway.coupon.model.merchant.Merchant;
import com.myideaway.coupon.model.merchant.Shop;
import com.myideaway.coupon.model.user.User;
import com.myideaway.easyapp.core.service.BizService;
import org.json.JSONObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class CouponBSGetDetailData extends BizService {
    private Coupon coupon = new Coupon();
    private String email;
    private String password ;

    public CouponBSGetDetailData(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        CouponRSGetDetailData couponRSGetDetailData = new CouponRSGetDetailData();
        couponRSGetDetailData.setCoupon(coupon);
        couponRSGetDetailData.setPassword(password);
        couponRSGetDetailData.setEmail(email);
        CouponBSGetDetailDataResult couponBSGetDetailDataResult = new CouponBSGetDetailDataResult();

        JSONObject couponJsonObject = (JSONObject) couponRSGetDetailData.syncExecute();


        Coupon coupon = new Coupon();
        coupon.setId(couponJsonObject.optInt("id"));

        coupon.setTitle(couponJsonObject.optString("title"));
        coupon.setAddress(couponJsonObject.optString("address"));
        coupon.setCommentCount(couponJsonObject.optInt("comment_count"));
        coupon.setContent(couponJsonObject.optString("content"));
        coupon.setIconUrl(couponJsonObject.optString("merchant_logo"));
        coupon.setLatitude((float) couponJsonObject.optDouble("xpoint"));
        coupon.setLongitude((float) couponJsonObject.optDouble("ypoint"));
        coupon.setImageUrl(couponJsonObject.optString("logo_2"));
        coupon.setShareContent(couponJsonObject.optString("share_content"));
        coupon.setExpireTip(couponJsonObject.optString("ycq"));
         int isFavor = couponJsonObject.optInt("is_sc");
        if (isFavor == 1){
            List<User> userList = coupon.getFavorers();
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            userList.add(user);
        }



      //        coupon.setBeginTime(couponJSONObject.optString("begin_time_format"));
//        coupon.setEndTime(couponJSONObject.optInt("id"));
//        coupon.setComments(couponJSONObject.optString("address"));
//        coupon.setCreateTime(couponJSONObject.optString("create_time"));
//        coupon.setExpireTip(couponJSONObject.optString("address"));

        Shop shop = new Shop();
        JSONObject shopJsonObject = couponJsonObject.optJSONObject("merchant");
        shop.setId(shopJsonObject.optInt("id"));
        shop.setName(shopJsonObject.optString("name"));
        shop.setBrief(shopJsonObject.optString("brief"));
        shop.setLogoUrl(shopJsonObject.optString("logo"));
        shop.setAddress(shopJsonObject.optString("address"));
        shop.setTel(shopJsonObject.optString("tel"));
        shop.setCommentCount(shopJsonObject.optInt("address"));
        Merchant merchant  = new Merchant();
        merchant.setId(shopJsonObject.optInt("brand_id"));
        shop.setMerchant(merchant);



        coupon.setShop(shop);
        couponBSGetDetailDataResult.setCoupon(coupon);
        couponBSGetDetailDataResult.setShop(shop);


        return couponBSGetDetailDataResult;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public class CouponBSGetDetailDataResult {

        Coupon coupon = new Coupon();
        Shop shop = new Shop();

        public Coupon getCoupon() {
            return coupon;
        }

        public void setCoupon(Coupon coupon) {
            this.coupon = coupon;
        }

        public Shop getShop() {
            return shop;
        }

        public void setShop(Shop shop) {
            this.shop = shop;
        }
    }
}

