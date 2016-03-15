package com.myideaway.coupon.view;

import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.merchant.Shop;
import com.myideaway.coupon.model.merchant.service.biz.MerchantBSGetMerchantListData;
import com.myideaway.coupon.view.common.BaseActivity;
import com.myideaway.easyapp.core.service.Service;
import roboguice.util.Ln;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class OtherShopActivity extends BaseActivity {

    private ImageButton backButton;
    private PullToRefreshListView shopListView;
    private List<Shop> shopList = new ArrayList<Shop>();
    private CouponAdapter couponAdapter = new CouponAdapter();
    private int page = 1;
    private int BrandID;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.common_list);
        showNavigationBar(false);
        Resources res = getResources();

        navigationBar.setTitle("其它分店");

        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        navigationBar.addLeftView(backButton);


        shopListView = (PullToRefreshListView) findViewById(R.id.couponListView);
        shopListView.setAdapter(couponAdapter);
        shopListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initshopListView(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                landMoreShopListView(++page);

            }
        });
        initshopListView(true);
    }

    private void initshopListView(final boolean isLock) {
        if (isLock) {
            showLoadingNewDataProgresssDialog();
        }
        page = 1;
        BrandID = getIntent().getIntExtra("BrandID", 0);
        MerchantBSGetMerchantListData merchantBSGetMerchantListData = new MerchantBSGetMerchantListData(this);
        merchantBSGetMerchantListData.setMerchantID(BrandID);
        merchantBSGetMerchantListData.setPage(page);
        merchantBSGetMerchantListData.asyncExecute();
        merchantBSGetMerchantListData.setOnSuccessHandler(new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                MerchantBSGetMerchantListData.MerchantRSGetMerchantListDataResult merchantRSGetMerchantListDataResult = (MerchantBSGetMerchantListData.MerchantRSGetMerchantListDataResult) o;
                shopList = merchantRSGetMerchantListDataResult.getShopList();
                shopListView.onRefreshComplete();
                shopListView.setAdapter(couponAdapter);
                if (isLock) {
                    hideProgressDialog();
                }
                if (shopList.size() == 0) {
                    showNoDataToast();
                }

            }
        });
        merchantBSGetMerchantListData.setOnFaultHandler(new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                shopListView.onRefreshComplete();
                Ln.e(e.getMessage(), e);
                showExceptionMessage(e);
                if (isLock) {
                    hideProgressDialog();
                }

            }
        });


    }

    private void landMoreShopListView(final int page) {

        BrandID = getIntent().getIntExtra("BrandID", 0);
        MerchantBSGetMerchantListData merchantBSGetMerchantListData = new MerchantBSGetMerchantListData(this);
        merchantBSGetMerchantListData.setMerchantID(BrandID);
        merchantBSGetMerchantListData.setPage(page);
        merchantBSGetMerchantListData.asyncExecute();
        merchantBSGetMerchantListData.setOnSuccessHandler(new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {

                MerchantBSGetMerchantListData.MerchantRSGetMerchantListDataResult merchantRSGetMerchantListDataResult = (MerchantBSGetMerchantListData.MerchantRSGetMerchantListDataResult) o;
                if (page > Integer.parseInt(merchantRSGetMerchantListDataResult.getPageTotal())) {
                    showNoMoreDataToast();
                    OtherShopActivity.this.page--;
                } else {
                    shopList.addAll(merchantRSGetMerchantListDataResult.getShopList());
                    couponAdapter.notifyDataSetChanged();
                }
                shopListView.onRefreshComplete();
            }
        });
        merchantBSGetMerchantListData.setOnFaultHandler(new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                shopListView.onRefreshComplete();
                Ln.e(e.getMessage(), e);
                showExceptionMessage(e);

            }
        });


    }

    //创建优惠卷列表
    private class CouponAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return shopList.size();  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getItem(int position) {
            return shopList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getItemId(int position) {
            return position;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public View getView(final int position, View couponView, ViewGroup parent) {
            final Shop shop = (Shop) getItem(position);
            if (couponView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                couponView = layoutInflater.inflate(R.layout.coupon_format, parent, false);
            }
            ImageView imageView = (ImageView) couponView.findViewById(R.id.new_content_img);
            imageLoader.displayImage(shop.getLogoUrl(), imageView,normalImageDisplayOptions);
            TextView couponTitleTextView = (TextView) couponView.findViewById(R.id.couponTitleTextView);
            couponTitleTextView.setText(shop.getName());
            // Ln.d(coupon.getTitle());
            TextView couponContentTextView = (TextView) couponView.findViewById(R.id.couponContentTextView);
            couponContentTextView.setText(shop.getBrief());
            TextView couponAddressTextView = (TextView) couponView.findViewById(R.id.couponAddressTextView);
            couponAddressTextView.setText(shop.getAddress());

            couponView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(OtherShopActivity.this, OtherCouponActivity.class);
                    intent.putExtra("MerchantID", shop.getId());
                    startActivity(intent);

                }
            });


            return couponView;  //To change body of implemented methods use File | Settings | File Templates.
        }


    }


}
