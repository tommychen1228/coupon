package com.myideaway.coupon.view;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TabHost;
import com.myideaway.coupon.R;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:26
 * To change this template use File | Settings | File Templates.
 */
public class MainTabActivity extends TabActivity {
    TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        final Resources res = getResources();

        tabHost = getTabHost();

        //主页
        Intent homeIntent = new Intent().setClass(this, HomePageActivity.class);
        //优惠页面
        Intent couponIntent = new Intent().setClass(this, CouponActivity.class);
        //周边界面
        Intent nearByIntent = new Intent().setClass(this, NearByActivity.class);
        //我的信息界面
        Intent userActivityIntent = new Intent().setClass(this, UserActivity.class);
        //更多信息界面
        Intent moreActivityIntent = new Intent().setClass(this, MoreActivity.class);

        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageDrawable(res.getDrawable(R.drawable.tab_home));

        tabHost.addTab(tabHost
                .newTabSpec("0")
                .setContent(homeIntent)
                .setIndicator(imageView));
        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageDrawable(res.getDrawable(R.drawable.tab_coupon));

        tabHost.addTab(tabHost.newTabSpec("1")
                .setContent(couponIntent)
                .setIndicator(imageView));

        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageDrawable(res.getDrawable(R.drawable.tab_nearby));

        tabHost.addTab(tabHost.newTabSpec("2")
                .setContent(nearByIntent)
                .setIndicator(imageView));

        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageDrawable(res.getDrawable(R.drawable.tab_user));

        tabHost.addTab(tabHost.newTabSpec("3")
                .setContent(userActivityIntent)
                .setIndicator(imageView));
        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageDrawable(res.getDrawable(R.drawable.tab_more));

        tabHost.addTab(tabHost.newTabSpec("4")
                .setContent(moreActivityIntent)
                .setIndicator(imageView));

        onPressedView("0");
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                onPressedView(tabId);
            }
        });
    }

    public void onPressedView(String tabId) {
        final Resources res = getResources();
        int tabHostId = Integer.parseInt(tabId);

        final int TAB_HOME = 0;
        final int TAB_COUPON = 1;
        final int TAB_NEARBY = 2;
        final int TAB_USER = 3;
        final int TAB_MORE = 4;

        ((ImageView) tabHost.getTabWidget().getChildAt(TAB_HOME)).setImageDrawable(res.getDrawable(R.drawable.tab_home));
        ((ImageView) tabHost.getTabWidget().getChildAt(TAB_COUPON)).setImageDrawable(res.getDrawable(R.drawable.tab_coupon));
        ((ImageView) tabHost.getTabWidget().getChildAt(TAB_NEARBY)).setImageDrawable(res.getDrawable(R.drawable.tab_nearby));
        ((ImageView) tabHost.getTabWidget().getChildAt(TAB_USER)).setImageDrawable(res.getDrawable(R.drawable.tab_user));
        ((ImageView) tabHost.getTabWidget().getChildAt(TAB_MORE)).setImageDrawable(res.getDrawable(R.drawable.tab_more));

        if (tabHostId == TAB_HOME) {
            ((ImageView) tabHost.getTabWidget().getChildAt(tabHostId)).setImageDrawable(res.getDrawable(R.drawable.tab_home_highlight));
        }
        if (tabHostId == TAB_COUPON) {
            ((ImageView) tabHost.getTabWidget().getChildAt(tabHostId)).setImageDrawable(res.getDrawable(R.drawable.tab_coupon_highlight));
        }
        if (tabHostId == TAB_NEARBY) {
            ((ImageView) tabHost.getTabWidget().getChildAt(tabHostId)).setImageDrawable(res.getDrawable(R.drawable.tab_nearby_highlight));
        }
        if (tabHostId == TAB_USER) {
            ((ImageView) tabHost.getTabWidget().getChildAt(tabHostId)).setImageDrawable(res.getDrawable(R.drawable.tab_user_highlight));
        }
        if (tabHostId == TAB_MORE) {
            ((ImageView) tabHost.getTabWidget().getChildAt(tabHostId)).setImageDrawable(res.getDrawable(R.drawable.tab_more_highlight));
        }

    }
}