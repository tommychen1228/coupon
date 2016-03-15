package com.myideaway.coupon.model.merchant.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.merchant.Shop;
import com.myideaway.coupon.model.merchant.service.remote.MerchantRSGetMerchantListData;
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
public class MerchantBSGetMerchantListData extends BizService {
    private int merchantID;
    private int page;

    public MerchantBSGetMerchantListData(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        MerchantRSGetMerchantListData merchantRSGetMerchantListData = new MerchantRSGetMerchantListData();
        MerchantRSGetMerchantListDataResult merchantBSGetMerchantListDataResult = new MerchantRSGetMerchantListDataResult();

        merchantRSGetMerchantListData.setBrandID(merchantID);
        merchantRSGetMerchantListData.setPage(page);

        JSONObject shopJsonObject = (JSONObject) merchantRSGetMerchantListData.syncExecute();

        JSONArray shopJSONArray = shopJsonObject.optJSONArray("item");
        List<Shop> shopList = new ArrayList<Shop>();
        for (int i = 0; i < shopJSONArray.length(); i++) {
            Shop shop = new Shop();
            JSONObject couponJSONObject = shopJSONArray.optJSONObject(i);
            shop.setId(couponJSONObject.optInt("id"));
            shop.setName(couponJSONObject.optString("name"));
            shop.setAddress(couponJSONObject.optString("address"));
            shop.setCommentCount(couponJSONObject.optInt("comment_count"));
            shop.setBrief(couponJSONObject.optString("brief"));
            shop.setLogoUrl(couponJSONObject.optString("logo"));
            shop.setLatitude((float) couponJSONObject.optDouble("ypoint"));
            shop.setLongitude((float) couponJSONObject.optDouble("xpoint"));
            shopList.add(shop);

        }
        merchantBSGetMerchantListDataResult.setShopList(shopList);

        String page = shopJsonObject.optJSONObject("page").optString("page");
        merchantBSGetMerchantListDataResult.setPage(page);
        String pageTotal = shopJsonObject.optJSONObject("page").optString("page_total");
        merchantBSGetMerchantListDataResult.setPageTotal(pageTotal);


        return merchantBSGetMerchantListDataResult;
    }

    public int getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(int merchantID) {
        this.merchantID = merchantID;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public class MerchantRSGetMerchantListDataResult {
        List<Shop> shopList = new ArrayList<Shop>();
        String page;
        String pageTotal;

        public List<Shop> getShopList() {
            return shopList;
        }

        public void setShopList(List<Shop> shopList) {
            this.shopList = shopList;
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

