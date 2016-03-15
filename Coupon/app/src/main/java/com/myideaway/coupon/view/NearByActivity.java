package com.myideaway.coupon.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.coupon.Coupon;
import com.myideaway.coupon.model.coupon.service.biz.CouponBSGetNearListData;
import com.myideaway.coupon.model.merchant.Category;
import com.myideaway.coupon.view.common.BaseActivity;
import com.myideaway.coupon.view.util.LatLonUtil;
import com.myideaway.coupon.view.util.Location;
import com.myideaway.coupon.view.util.LocationManager;
import com.myideaway.easyapp.core.service.Service;
import roboguice.util.Ln;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class NearByActivity extends BaseActivity {
    MapView BDMapView;
    private Button distanceButton;
    private Button categoryButton;
    private ListView couponListView;
    private List<Coupon> couponList;
    private ImageButton myLocationImageButton;
    private ImageButton mapListImageButton;
    private LinearLayout couponListLinearLayout;
    private TextView myLocationTextView;
    private ImageButton leftArrowImageButton;
    private ImageButton rightArrowImageButton;
    private TextView pageTextView;
    private Location myLocation;
    private List<Category> categoryList;
    private String CategoryAreas[];
    private ApplicationContext applicationContext = ApplicationContext.getInstance();
    private boolean isMap = false;
    private MyLocationOverlay mylocationOverlay;
    private MapController mapController;
    private int cityId;
    private int raidus = 1000;
    private int page = 1;
    //地图相关
    private PoiOverlay<OverlayItem> itemItemizedOverlay;
    private View mapPopWindow;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.nearby);
        showNavigationBar(false);
        Resources res = getResources();

        navigationBar.setTitle("附近");

        myLocationTextView = (TextView) findViewById(R.id.myLocationTextView);

        BDMapView = (MapView) findViewById(R.id.BDMapView);
        BDMapView.setBuiltInZoomControls(false);
        BDMapView.setDoubleClickZooming(true);
        //卫星图层
        // mapView.setSatellite(true);
        //交通图层
        // mapView.setTraffic(true);


        mapController = BDMapView.getController();
        //控制缩放等级
        mapController.setZoom(15);
        mapController.enableClick(true);

        couponListLinearLayout = (LinearLayout) findViewById(R.id.couponListLinearLayout);

        //我的位置信息按钮
        myLocationImageButton = (ImageButton) layoutInflater.inflate(R.layout.navigation_bar_image_view, null);
        myLocationImageButton.setImageResource(R.drawable.location);
        myLocationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locateMe();
                initCouponList();
            }
        });
        navigationBar.addRightView(myLocationImageButton);

        //地图，列表切换
        mapListImageButton = (ImageButton) layoutInflater.inflate(R.layout.navigation_bar_image_view, null);
        mapListImageButton.setImageResource(R.drawable.map);
        mapListImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMap) {
                    couponListLinearLayout.setVisibility(View.VISIBLE);
                    BDMapView.setVisibility(View.GONE);
                    mapListImageButton.setImageResource(R.drawable.map);
                    isMap = false;
                } else {
                    locateMe();
                    BDMapView.setVisibility(View.VISIBLE);
                    couponListLinearLayout.setVisibility(View.GONE);
                    mapListImageButton.setImageResource(R.drawable.list);
                    isMap = true;
                }
            }
        });
        navigationBar.addRightView(mapListImageButton);
        pageTextView = (TextView) findViewById(R.id.pageTextView);
        pageTextView.setText("第" + page + "页");

        //上一页
        leftArrowImageButton = (ImageButton) findViewById(R.id.leftArrowImageButton);
        leftArrowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCouponList(myLocation, --page, raidus, 0);

            }
        });
        //下一页
        rightArrowImageButton = (ImageButton) findViewById(R.id.rightArrowImageButton);
        rightArrowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextCouponList(myLocation, ++page, raidus, 0);

            }
        });

        //分类
        categoryList = applicationContext.getCategoryList();
        CategoryAreas = new String[categoryList.size()];
        CategoryAreas[0] = "全部分类";
        for (int i = 1; i < CategoryAreas.length; i++) {
            CategoryAreas[i] = categoryList.get(i - 1).getName();
        }
        categoryButton = (Button) findViewById(R.id.categoryButton);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(NearByActivity.this).setItems(CategoryAreas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            getCouponList(myLocation, 1, raidus, 0);
                        } else {
                            getCouponList(myLocation, 1, raidus, categoryList.get(i - 1).getId());

                        }
                        categoryButton.setText(CategoryAreas[i]);

                    }
                }).show();
            }
        });


        final String distanceButtons[] = new String[]{"1000米", "2000米", "3000米", "4000米", "5000米"};
        distanceButton = (Button) findViewById(R.id.distanceButton);
        distanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(NearByActivity.this).setItems(distanceButtons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        raidus = i * 1000 + 1000;
                        getCouponList(myLocation, 1, raidus, 0);
                        distanceButton.setText(distanceButtons[i]);

                    }
                }).show();
            }
        });
        initCouponList();


    }

    private void initCouponList() {
        cityId = applicationContext.getCurrentCity().getId();

        showLoadingNewDataProgresssDialog();
        LocationManager locationManager = new LocationManager(getApplicationContext());

        locationManager.requestLocation(new LocationManager.LocationManagetListener() {

            @Override
            public void onReceiveLocation(Location location) {
                LocationData locData = new LocationData();

                locData.latitude = location.getLatitude();
                locData.longitude = location.getLongitude();

                myLocation = new Location();
                myLocation.setLatitude(location.getLatitude());
                myLocation.setLongitude(location.getLongitude());
                hideProgressDialog();

                getCouponList(myLocation, 1, raidus, 0);

                myLocationTextView.setText(location.getAddress());


            }
        });
    }

    private void nextCouponList(Location myLocation, int page, int raidus, int cateID) {
        showLoadingNewDataProgresssDialog();
        if (NearByActivity.this.page <= 0) {
            NearByActivity.this.page = 1;
            showAlertDialog("没有上一页了");
            hideProgressDialog();
            return;
        }
        CouponBSGetNearListData couponBSGetNearListData = new CouponBSGetNearListData(NearByActivity.this);
        double around[] = LatLonUtil.getAround(myLocation.getLatitude(), myLocation.getLongitude(), raidus);

        couponBSGetNearListData.setM_latitude((float) (myLocation.getLatitude()));
        couponBSGetNearListData.setM_longitude((float) myLocation.getLongitude());

        couponBSGetNearListData.setLongitude_left((float) around[LatLonUtil.LNG_LEFT]);
        couponBSGetNearListData.setLatitude_top((float) around[LatLonUtil.LAT_TOP]);
        couponBSGetNearListData.setLongitude_right((float) around[LatLonUtil.LNG_RIGHT]);
        couponBSGetNearListData.setLatitude_bottom((float) around[LatLonUtil.LAT_BOTTOM]);


        couponBSGetNearListData.setCityID(cityId);
        couponBSGetNearListData.setCateID(cateID);

        couponBSGetNearListData.setPage(page);

        couponBSGetNearListData.asyncExecute();
        couponBSGetNearListData.setOnSuccessHandler(new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                CouponBSGetNearListData.CouponBSGetNearListDataResult couponBSGetNearListDataResult = (CouponBSGetNearListData.CouponBSGetNearListDataResult) o;
                if (NearByActivity.this.page > Integer.parseInt(couponBSGetNearListDataResult.getPageTotal())) {
                    NearByActivity.this.page--;
                    if (NearByActivity.this.page == 0) {
                        NearByActivity.this.page++;
                    }

                    showNoMoreDataToast();
                } else {
                    couponList = couponBSGetNearListDataResult.getCouponList();
                    if (isMap) {
                        locateMe();
                    } else {
                        couponListView();
                    }
                }


                pageTextView.setText("第" + NearByActivity.this.page + "页");
                hideProgressDialog();
            }
        });
        couponBSGetNearListData.setOnFaultHandler(new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                hideProgressDialog();
                Ln.e(e.getMessage(), e);
                showExceptionMessage(e);

            }
        });

    }

    private void getCouponList(Location myLocation, int page, int raidus, int cateID) {
        showLoadingNewDataProgresssDialog();
        if (NearByActivity.this.page <= 0) {
            NearByActivity.this.page = 1;
//            showNoDataToast();
//            hideProgressDialog();
            leftArrowImageButton.setEnabled(true);
            return;
        } else {
            leftArrowImageButton.setEnabled(false);
        }
        CouponBSGetNearListData couponBSGetNearListData = new CouponBSGetNearListData(NearByActivity.this);
        double around[] = LatLonUtil.getAround(myLocation.getLatitude(), myLocation.getLongitude(), raidus);

        couponBSGetNearListData.setM_latitude((float) (myLocation.getLatitude()));
        couponBSGetNearListData.setM_longitude((float) myLocation.getLongitude());

        couponBSGetNearListData.setLongitude_left((float) around[LatLonUtil.LNG_LEFT]);
        couponBSGetNearListData.setLatitude_top((float) around[LatLonUtil.LAT_TOP]);
        couponBSGetNearListData.setLongitude_right((float) around[LatLonUtil.LNG_RIGHT]);
        couponBSGetNearListData.setLatitude_bottom((float) around[LatLonUtil.LAT_BOTTOM]);


        couponBSGetNearListData.setCityID(cityId);
        couponBSGetNearListData.setCateID(cateID);

        couponBSGetNearListData.setPage(page);

        couponBSGetNearListData.asyncExecute();
        couponBSGetNearListData.setOnSuccessHandler(new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                CouponBSGetNearListData.CouponBSGetNearListDataResult couponBSGetNearListDataResult = (CouponBSGetNearListData.CouponBSGetNearListDataResult) o;
                if (NearByActivity.this.page > Integer.parseInt(couponBSGetNearListDataResult.getPageTotal())) {
                    NearByActivity.this.page--;
                    if (NearByActivity.this.page == 0) {
                        NearByActivity.this.page++;
                    }

                    showToastMessage("您附近暂无特权优惠，敬请期待");
                }
                couponList = couponBSGetNearListDataResult.getCouponList();
                if (isMap) {
                    locateMe();
                } else {
                    couponListView();
                }


                pageTextView.setText("第" + NearByActivity.this.page + "页");
                hideProgressDialog();
            }
        });
        couponBSGetNearListData.setOnFaultHandler(new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                hideProgressDialog();
                Ln.e(e.getMessage(), e);
                showExceptionMessage(e);

            }
        });

    }

    private void locateMe() {


        LocationManager locationManager = new LocationManager(getApplicationContext());

        locationManager.requestLocation(new LocationManager.LocationManagetListener() {
            @Override
            public void onReceiveLocation(Location location) {

                if (mylocationOverlay != null) {
                    BDMapView.getOverlays().remove(mylocationOverlay);
                }

                //扎点
                mylocationOverlay = new MyLocationOverlay(BDMapView);
                //设置定位数据
                LocationData locData = new LocationData();
                locData.latitude = location.getLatitude();
                locData.longitude = location.getLongitude();
                mylocationOverlay.setData(locData);


                //添加定位图层
                GeoPoint locationGeoPoint = new GeoPoint((int) (locData.latitude * 1E6), (int) (locData.longitude * 1E6));

                BDMapView.getOverlays().remove(itemItemizedOverlay);
                BDMapView.getController().animateTo(locationGeoPoint);
                BDMapView.getOverlays().add(mylocationOverlay);
                Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
                itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker, BDMapView);


                for (int i = 0; i < couponList.size(); i++) {
                    Coupon coupon = couponList.get(i);
                    locationGeoPoint = new GeoPoint((int) (coupon.getLatitude() * 1E6), (int) (coupon.getLongitude() * 1E6));
                    OverlayItem overlayItem = new OverlayItem(locationGeoPoint, null, null);
                    itemItemizedOverlay.addItem(overlayItem);
                }

                layoutInflater = LayoutInflater.from(NearByActivity.this);
                mapPopWindow = layoutInflater.inflate(R.layout.coupon_format, null);
                mapPopWindow.setVisibility(View.GONE);
                BDMapView.addView(mapPopWindow);

                BDMapView.getOverlays().add(itemItemizedOverlay);
                BDMapView.getController().zoomToSpan(itemItemizedOverlay.getLatSpanE6(), itemItemizedOverlay.getLonSpanE6());
                BDMapView.refresh();

                Ln.d("My location " + location.getAddress());
                myLocationTextView.setText(location.getAddress());

            }
        });
        //跨activity间传对象
        //Intent intent = new Intent(this, AboutUsActivity.class);
        // IntentObjectPool.putObjectExtra(intent, "aaa", myLocationTextView);
        //startActivity(intent);


    }

    //创建优惠卷列表
    private void couponListView() {


        couponListView = (ListView) findViewById(R.id.couponListView);
        couponListView.setAdapter(new BaseAdapter() {
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
                TextView couponContentTextView = (TextView) couponView.findViewById(R.id.couponContentTextView);
                couponContentTextView.setText(coupon.getContent());
                TextView couponAddressTextView = (TextView) couponView.findViewById(R.id.couponAddressTextView);
                couponAddressTextView.setText(coupon.getAddress());

                couponView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(NearByActivity.this, CouponDetailActivity.class);
                        intent.putExtra("couponID", coupon.getId());
                        startActivity(intent);

                    }
                });
                return couponView;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });


    }

    @Override
    protected void onPause() {
        //   ((DemoApplication)this.getApplication()).mBMapManager.stop();
        BDMapView.onPause();

        super.onPause();
    }

    @Override
    protected void onResume() {
        BDMapView.onResume();
        // ((DemoApplication)this.getApplication()).mBMapManager.start();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        BDMapView.destroy();
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }

    class PoiOverlay<OverlayItem> extends ItemizedOverlay {

        public PoiOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }

        @Override
        protected boolean onTap(final int i) {

            Log.d("BaiduMapDemo", "onTap " + i);
            com.baidu.mapapi.map.OverlayItem item = itemItemizedOverlay.getItem(i);
            GeoPoint point = item.getPoint();
            String title = couponList.get(i).getTitle();
            String content = couponList.get(i).getContent();
            String address = couponList.get(i).getAddress();

            mapPopWindow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(NearByActivity.this, CouponDetailActivity.class);
                    intent.putExtra("couponID", couponList.get(i).getId());
                    startActivity(intent);
                }
            });

            TextView titleTextView = (TextView) mapPopWindow.findViewById(R.id.couponTitleTextView);
            TextView contentTextView = (TextView) mapPopWindow.findViewById(R.id.couponContentTextView);
            TextView addressTextView = (TextView) mapPopWindow.findViewById(R.id.couponAddressTextView);

            ImageView couponImageView = (ImageView) mapPopWindow.findViewById(R.id.new_content_img);
            titleTextView.setText(title);
            contentTextView.setText(content);
            addressTextView.setText(address);


            imageLoader.displayImage(couponList.get(i).getIconUrl(), couponImageView,normalImageDisplayOptions);

            contentTextView.setVisibility(View.VISIBLE);

            MapView.LayoutParams layoutParam = new MapView.LayoutParams(
                    //控件宽,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //控件高,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //使控件固定在某个地理位置
                    point,
                    0,
                    -50,
                    //控件对齐方式
                    MapView.LayoutParams.BOTTOM_CENTER);

            mapPopWindow.setVisibility(View.VISIBLE);

            mapPopWindow.setLayoutParams(layoutParam);

            mapController.animateTo(point);

            return super.onTap(i);
        }

        @Override
        public boolean onTap(GeoPoint geoPoint, MapView mapView) {
            Log.d("BaiduMapDemo", "onTap geoPoint " + geoPoint);

            mapPopWindow.setVisibility(View.GONE);

            return super.onTap(geoPoint, mapView);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }


}
