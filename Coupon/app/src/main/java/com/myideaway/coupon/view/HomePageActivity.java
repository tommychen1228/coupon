package com.myideaway.coupon.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.action.*;
import com.myideaway.coupon.model.advertisment.Advertisment;
import com.myideaway.coupon.model.area.City;
import com.myideaway.coupon.model.global.service.biz.GlobalBSInitData;
import com.myideaway.coupon.model.global.service.biz.GlobalBSInitHomePageData;
import com.myideaway.coupon.model.menu.Menu;
import com.myideaway.coupon.model.user.User;
import com.myideaway.coupon.model.user.service.biz.UserBSLoginData;
import com.myideaway.coupon.view.common.BaseActivity;
import com.myideaway.coupon.view.common.Notification;
import com.myideaway.coupon.view.util.Location;
import com.myideaway.coupon.view.util.LocationManager;

import com.myideaway.easyapp.core.service.Service;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import roboguice.util.Ln;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class HomePageActivity extends BaseActivity {
    public static final int REQUEST_CODE_CHANGE_CITY = 1;
    private ImgGallery imgGallery;
    private ListView menuListView;
    private List<Menu> menuList;
    private List<Advertisment> advertismentList;
    private ApplicationContext applicationContext = ApplicationContext.getInstance();
    private String cityName = "";

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.home_page);
//
//      UmengUpdateAgent.setUpdateListener(null);
        UmengUpdateAgent.setDefault();
        UmengUpdateAgent.update(this);
        MobclickAgent.updateOnlineConfig(HomePageActivity.this);


        showNavigationBar(false);
        ImageButton selectButton = new ImageButton(this);

        selectButton.setImageDrawable(getResources().getDrawable(R.drawable.home));
        selectButton.setBackgroundResource(0);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, SelectCityActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CHANGE_CITY);
            }
        });
        navigationBar.addLeftView(selectButton);

        navigationBar.setTitle("");
        navigationBar.setTitleColor(Color.WHITE);
        navigationBar.setBackgroundResource(R.drawable.navigation_bar_bg);

        navigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(HomePageActivity.this, SelectCityActivity.class);
//                List<Category> categoryList = ApplicationContext.getInstance().getCategoryList();
//                intent.putExtra("categoryList", (Serializable) categoryList);
//                intent.putExtra("cityName","全国");
//                IntentObjectPool.putObjectExtra();
            }
        });


        locateMe();
    }

    //初始化程序
    private void init() {
        showLoadingNewDataProgresssDialog();
        validat();


        GlobalBSInitData globalBSInitData = new GlobalBSInitData(HomePageActivity.this);
        globalBSInitData.setCityName(cityName);
        globalBSInitData.asyncExecute();
        globalBSInitData.setOnSuccessHandler(new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                GlobalBSInitData.GlobalBSInitDataResult globalBSInitDataResult = (GlobalBSInitData.GlobalBSInitDataResult) o;
                applicationContext.setCategoryList(globalBSInitDataResult.getCategoryList());
                applicationContext.setAppInfo(globalBSInitDataResult.getAppInfo());
                applicationContext.setBusinessAreaList(globalBSInitDataResult.getBusinessAreaList());
                applicationContext.setCityMap(globalBSInitDataResult.getCityMap());
//                City city = globalBSInitDataResult.getCityMap().get(Config.DEFAULT_CITY_ID);
//              City city =  globalBSInitDataResult.getCityMap().get(33);
//              applicationContext.setCurrentCity(globalBSInitDataResult.getCurrentCity());
                applicationContext.setNoticeList(globalBSInitDataResult.getNoticeList());
                City defaultCity = globalBSInitDataResult.getCurrentCity();
                City city = applicationContext.getCityMap().get(defaultCity.getId());


                if (city == null) {
                    showAlertDialog("初始化失败");
                    return;
                }

                if (city != null && cityName != null && !city.getName().contains(cityName)) {
                    showToastMessage("您所在的城市未开通服务，感谢您的关注");
                }

                applicationContext.setCurrentCity(city);
                navigationBar.setTitle(applicationContext.getCurrentCity().getName());
                initHomePage();
                sendNotification(Notification.SELECT_CITY_SUCCESS, city.getId());


            }
        });
        globalBSInitData.setOnFaultHandler(new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                hideProgressDialog();
                Ln.e(e.getMessage(), e);
                showExceptionMessage(e);


            }
        });
    }

    private void validat() {
        if (applicationContext.isLogin(this, false)) {
            User user = applicationContext.getUser(this);

            UserBSLoginData userBSLoginData = new UserBSLoginData(this);
            userBSLoginData.setEmail(user.getEmail());
            userBSLoginData.setPassword(user.getPassword());
            userBSLoginData.asyncExecute();

            userBSLoginData.setOnSuccessHandler(new Service.OnSuccessHandler() {
                @Override
                public void onSuccess(Service service, Object o) {
                    UserBSLoginData.UserBSLoginDataResult userBSLoginDataResult = (UserBSLoginData.UserBSLoginDataResult) o;
                    if (userBSLoginDataResult.isSuccess()) {
                        User user = userBSLoginDataResult.getUser();
                        applicationContext.setUser(user, HomePageActivity.this);

                    } else {
                        applicationContext.removeUser(HomePageActivity.this);

                    }

                }
            });
            userBSLoginData.setOnFaultHandler(new Service.OnFaultHandler() {
                @Override
                public void onFault(Service service, Exception e) {
                    Ln.e(e.getMessage(), e);
                    showExceptionMessage(e);

                }
            });

        }

    }

    //首页化程序
    private void initHomePage() {
        showLoadingNewDataProgresssDialog();
        GlobalBSInitHomePageData globalBSInitHomePageData = new GlobalBSInitHomePageData(HomePageActivity.this);
        globalBSInitHomePageData.asyncExecute();
        globalBSInitHomePageData.setOnSuccessHandler(new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                GlobalBSInitHomePageData.GlobalBSInitHomePageDataResult globalBSInitHomePageDataResult = (GlobalBSInitHomePageData.GlobalBSInitHomePageDataResult) o;
                advertismentList = globalBSInitHomePageDataResult.getAdvertismentList();
                createImgGallery();
                menuList = globalBSInitHomePageDataResult.getMenuList();
                createMenuListView();
                hideProgressDialog();
            }
        });
        globalBSInitHomePageData.setOnFaultHandler(new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                hideProgressDialog();
                Ln.e(e.getMessage(), e);
                showExceptionMessage(e);

            }
        });
    }

    //创建菜单
    private void createMenuListView() {


        menuListView = (ListView) findViewById(R.id.menuListView);


        menuListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return menuList.size();  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Object getItem(int position) {
                return menuList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public long getItemId(int position) {
                return position;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    final LayoutInflater layoutInflater = getLayoutInflater();
                    convertView = layoutInflater.inflate(R.layout.menulist, parent, false);
                }
                final Menu menu = (Menu) getItem(position);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        actionToActivity(menu.getAction());
                    }
                });
                TextView nameTextView = (TextView) convertView.findViewById(R.id.textTextView);
                nameTextView.setText(menuList.get(position).getName());

                return convertView;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });


    }

    private void createImgGallery() {

        imgGallery = (ImgGallery) findViewById(R.id.newsimg);


        imgGallery.setAdapter(new ImgAdapter());

        imgGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                actionToActivity(advertismentList.get(i).getAction());
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHANGE_CITY) {
            if (resultCode == RESULT_OK) {
                navigationBar.setTitle(applicationContext.getCurrentCity().getName());
                cityName = applicationContext.getCurrentCity().getName();
                init();
            }
        }
    }

    //根据不同类型进行不同的跳转
    private void actionToActivity(Action action) {
        if (action instanceof UrlAction) {
            UrlAction urlAction = (UrlAction) action;
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String urlstr = urlAction.getUrl();
            if (!urlstr.startsWith("http://")) {
                urlstr = "http://" + urlstr;
            }
            Uri content_url = Uri.parse(urlstr);
            intent.setData(content_url);
            startActivity(intent);


        }
        if (action instanceof AboutUsAction) {
            Intent intent = new Intent(HomePageActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }
        if (action instanceof CategoryCouponListAction) {
            //todo 未完成
            CategoryCouponListAction categoryCouponListAction = (CategoryCouponListAction) action;
            Intent intent = new Intent(HomePageActivity.this, CouponActivity.class);
            categoryCouponListAction.getCategory().getId();
            startActivity(intent);
        }
        if (action instanceof CouponDetailAction) {
            CouponDetailAction couponDetailAction = (CouponDetailAction) action;
            Intent intent = new Intent(HomePageActivity.this, CouponDetailActivity.class);
            intent.putExtra("couponID", couponDetailAction.getCoupon().getId());
            startActivity(intent);
            couponDetailAction.getCoupon().getId();
        }
        if (action instanceof NearbyAction) {
            NearbyAction nearbyAction = (NearbyAction) action;
            Intent intent = new Intent(HomePageActivity.this, NearByActivity.class);
            startActivity(intent);
        }
        if (action instanceof NoticeListAction) {
            Intent intent = new Intent(HomePageActivity.this, NoticeListActivity.class);
            startActivity(intent);

        }
        if (action instanceof SearchCouponAction) {
            SearchCouponAction searchCouponAction = (SearchCouponAction) action;
            Intent intent = new Intent(HomePageActivity.this, CouponActivity.class);
            startActivity(intent);
        }
        if (action instanceof SearchShopAction) {
            Intent intent = new Intent(HomePageActivity.this, OtherShopActivity.class);
            startActivity(intent);

        }

    }

    private void locateMe() {


        LocationManager locationManager = new LocationManager(getApplicationContext());

        showProgressDialog("正在定位..");

        locationManager.requestLocation(new LocationManager.LocationManagetListener() {
            @Override
            public void onReceiveLocation(Location location) {
                hideProgressDialog();
                //当前城市名称
                cityName = location.getCity();

                if (cityName == null) {
                    showToastMessage("无法定位当前城市");
                }

                init();
//                Map<Integer, City> cityMap = applicationContext.getCityMap();
//                Set<Integer> cityMapKeySet = cityMap.keySet();
//                Iterator<Integer> cityIDIterator = cityMapKeySet.iterator();
//                boolean isHaveCity = false;
//                while (cityIDIterator.hasNext()) {
//                    int cityID = cityIDIterator.next();
//                    City city = cityMap.get(cityID);
//                    if(cityName == null){
//                        showToastMessage("无法获取您的位置，切换至默认城市");
//                        isHaveCity = true;
//                    }else {
//                        if (cityName.contains(city.getName())) {
//                            applicationContext.setCurrentCity(city);
//                            isHaveCity = true;
//                        }
//                    }
//
//                }
//                  if(!isHaveCity){
//                      showToastMessage("您所在的城市未开通服务，感谢您的关注");
//                  }
//                navigationBar.setTitle(applicationContext.getCurrentCity().getName());
//                hideProgressDialog();
//                initHomePage();


            }
        });
        //跨activity间传对象
        //Intent intent = new Intent(this, AboutUsActivity.class);
        // IntentObjectPool.putObjectExtra(intent, "aaa", myLocationTextView);
        //startActivity(intent);


    }

    //图片 Gallery
    class ImgAdapter extends BaseAdapter {

        public ImgAdapter() {
        }

        @Override
        public int getCount() {
            return advertismentList.size();
        }

        @Override
        public Object getItem(int i) {
            return advertismentList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View linearLayout, ViewGroup viewGroup) {
            final Advertisment advertisment = (Advertisment) getItem(position);
            if (linearLayout == null) {
                linearLayout = getLayoutInflater().inflate(R.layout.gallery, viewGroup, false);
            }
            ImageView imageView = (ImageView) linearLayout.findViewById(R.id.galleryImage);
            TextView textView = (TextView) linearLayout.findViewById(R.id.gallerytext);
            imageLoader.displayImage(advertisment.getImageUrl(), imageView, normalImageDisplayOptions);
            textView.setText(advertisment.getName());

            return linearLayout;
        }
    }


}
