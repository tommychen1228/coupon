package com.myideaway.coupon.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import com.myideaway.coupon.R;
import com.myideaway.coupon.view.common.BaseActivity;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import roboguice.util.Ln;


public class WelcomeActivity extends BaseActivity implements OnPageChangeListener  {
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private  int page;
    private  int last;
    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.main);
        showNavigationBar(false);
        hideNavigationBar(true);
        showToolBar(false);
        vp = (ViewPager) findViewById(R.id.welcomeViewPager);

        initViews();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        // 初始化引导图片列表
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(R.drawable.welcome1);
        views.add(imageView);
        ImageView imageView2 = new ImageView(this);
        imageView2.setBackgroundResource(R.drawable.welcome2);
        views.add(imageView2);

        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views, this);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
    }





    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        if(arg0 != 0){
            last = arg0;
        }else {
            if(page == 1){
                if(last == 1){
                    Ln.d("");
                    vpAdapter.setGuided();
                    vpAdapter.goHome();
                }
            }
        }
        Ln.d("onPageScrollStateChanged : "+arg0 +"last" +last + "page" + page);
//        Ln.d("");
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
//        Ln.d("onPageScrolled : "+arg0);
//        Ln.d("onPageScrolled : "+arg1);
//        Ln.d("onPageScrolled : "+arg2);
//        Ln.d("");
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
//        Ln.d("onPageSelected : "+arg0);
//        Ln.d("");
        page = arg0;
    }



    /**
     */
    public class ViewPagerAdapter extends PagerAdapter {

        // 界面列表
        private List<View> views;
        private Activity activity;
        private static final String SHAREDPREFERENCES_NAME = "first_pref";
        public ViewPagerAdapter(List<View> views, Activity activity) {
            this.views = views;
            this.activity = activity;
        }

        // 销毁arg1位置的界面
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        // 获得当前界面数
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        // 初始化arg1位置的界面
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            if (arg1 == views.size() - 1) {
//                        setGuided();
//                        goHome();
            }
            return views.get(arg1);
        }

        private void goHome() {
            // 跳转
            Intent intent = new Intent(activity, MainTabActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
        /**
         *
         * method desc：设置已经引导过了，下次启动不用再次引导
         */
        private void setGuided() {
            SharedPreferences preferences = activity.getSharedPreferences(
                    SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            // 存入数据
            editor.putBoolean("isFirstIn", false);
            // 提交修改
            editor.commit();
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

    }

}






