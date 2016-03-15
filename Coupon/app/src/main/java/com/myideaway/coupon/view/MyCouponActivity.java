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
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.coupon.Coupon;
import com.myideaway.coupon.model.coupon.service.biz.CouponBSGetByUserData;
import com.myideaway.coupon.model.user.User;
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
public class MyCouponActivity extends BaseActivity {

    private ImageButton backButton;
    private PullToRefreshListView couponListView;
    private List<Coupon> couponList = new ArrayList<Coupon>();
    private CouponAdapter couponAdapter = new CouponAdapter();
    private int page = 1;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.common_list);
        showNavigationBar(false);
        Resources res = getResources();

        navigationBar.setTitle("我的优惠卷");

        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        navigationBar.addLeftView(backButton);
        couponListView = (PullToRefreshListView) findViewById(R.id.couponListView);
        couponListView.setAdapter(couponAdapter);
        couponListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initCouponListView(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                landMoreCouponListView(++page);

            }
        });
        initCouponListView(true);
    }

    private void initCouponListView(final boolean isLock) {

        if (isLock) {
            showLoadingNewDataProgresssDialog();
        }

        ApplicationContext applicationContext = ApplicationContext.getInstance();
        page = 1;
        boolean isSubmit = applicationContext.isLogin(MyCouponActivity.this, true);
        if (isSubmit) {
            User user = applicationContext.getUser(this);
            CouponBSGetByUserData couponBSGetByUserData = new CouponBSGetByUserData(this);
            couponBSGetByUserData.setEmail(user.getEmail());
            couponBSGetByUserData.setPassword(user.getPassword());
            couponBSGetByUserData.setPage(page);
            couponBSGetByUserData.asyncExecute();
            couponBSGetByUserData.setOnSuccessHandler(new Service.OnSuccessHandler() {
                @Override
                public void onSuccess(Service service, Object o) {
                    couponListView.onRefreshComplete();
                    CouponBSGetByUserData.CouponBSGetByUserDataResult couponBSGetByUserDataResult = (CouponBSGetByUserData.CouponBSGetByUserDataResult) o;
                    couponList = couponBSGetByUserDataResult.getCouponList();
                    couponListView.setAdapter(couponAdapter);
                    if (isLock) {
                        hideProgressDialog();
                    }
                    if (couponList.size() == 0) {
                        showNoDataToast();
                    }
                }
            });
            couponBSGetByUserData.setOnFaultHandler(new Service.OnFaultHandler() {
                @Override
                public void onFault(Service service, Exception e) {
                    if (isLock) {
                        hideProgressDialog();
                    }
                    couponListView.onRefreshComplete();
                    Ln.e(e.getMessage(), e);
                    showExceptionMessage(e);
                }
            });
        } else {
        }
    }

    private void landMoreCouponListView(final int page) {

        ApplicationContext applicationContext = ApplicationContext.getInstance();
        boolean isSubmit = applicationContext.isLogin(MyCouponActivity.this, true);
        if (isSubmit) {
            User user = applicationContext.getUser(this);
            CouponBSGetByUserData couponBSGetByUserData = new CouponBSGetByUserData(this);
            couponBSGetByUserData.setEmail(user.getEmail());
            couponBSGetByUserData.setPassword(user.getPassword());
            couponBSGetByUserData.setPage(page);
            couponBSGetByUserData.asyncExecute();
            couponBSGetByUserData.setOnSuccessHandler(new Service.OnSuccessHandler() {
                @Override
                public void onSuccess(Service service, Object o) {
                    couponListView.onRefreshComplete();
                    CouponBSGetByUserData.CouponBSGetByUserDataResult couponBSGetByUserDataResult = (CouponBSGetByUserData.CouponBSGetByUserDataResult) o;
                    if (page > Integer.parseInt(couponBSGetByUserDataResult.getPageTotal())) {
                        showNoMoreDataToast();
                        MyCouponActivity.this.page--;
                    } else {
                        couponList.addAll(couponBSGetByUserDataResult.getCouponList());
                        couponAdapter.notifyDataSetChanged();
                    }
                }
            });
            couponBSGetByUserData.setOnFaultHandler(new Service.OnFaultHandler() {
                @Override
                public void onFault(Service service, Exception e) {
                    couponListView.onRefreshComplete();
                    Ln.e(e.getMessage(), e);
                    showExceptionMessage(e);
                }
            });
        } else {
        }
    }

    //创建优惠卷列表
    private class CouponAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return couponList.size();  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getItem(int position) {
            return couponList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getItemId(int position) {
            return position;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public View getView(final int position, View couponView, ViewGroup parent) {
            final Coupon coupon = (Coupon) getItem(position);
            if (couponView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                couponView = layoutInflater.inflate(R.layout.coupon_format, parent, false);
            }
            ImageView imageView = (ImageView) couponView.findViewById(R.id.new_content_img);
            imageLoader.displayImage(coupon.getIconUrl(), imageView,normalImageDisplayOptions);
            TextView couponTitleTextView = (TextView) couponView.findViewById(R.id.couponTitleTextView);
            couponTitleTextView.setText(coupon.getTitle());
            // Ln.d(coupon.getTitle());
            TextView couponContentTextView = (TextView) couponView.findViewById(R.id.couponContentTextView);
            couponContentTextView.setText(coupon.getContent());
            TextView couponAddressTextView = (TextView) couponView.findViewById(R.id.couponAddressTextView);
            couponAddressTextView.setText(coupon.getAddress());

            couponView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyCouponActivity.this, CouponDetailActivity.class);
                    intent.putExtra("couponID", coupon.getId());
                    startActivity(intent);

                }
            });


            return couponView;  //To change body of implemented methods use File | Settings | File Templates.
        }


    }


}
