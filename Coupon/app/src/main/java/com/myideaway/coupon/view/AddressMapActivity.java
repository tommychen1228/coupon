package com.myideaway.coupon.view;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.coupon.Coupon;
import com.myideaway.coupon.model.merchant.Category;
import com.myideaway.coupon.view.common.BaseActivity;
import com.myideaway.easyapp.core.intent.IntentObjectPool;
import roboguice.util.Ln;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class AddressMapActivity extends BaseActivity {
    private MapView BDMapView;
    private List<Category> categoryList;
    private String CategoryAreas[];
    private ApplicationContext applicationContext = ApplicationContext.getInstance();
    private boolean isMap = false;
    private MapController mapController;
    private int cityId;
    private ImageButton backButton;
    private Coupon coupon;
    //地图相关
    private PoiOverlay<OverlayItem> itemItemizedOverlay;
    private View mapPopWindow;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.address_map);
        showNavigationBar(false);
        Resources res = getResources();

        navigationBar.setTitle("地址");

        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        navigationBar.addLeftView(backButton);


        BDMapView = (MapView) findViewById(R.id.BDMapView);
        BDMapView.setBuiltInZoomControls(false);
        BDMapView.setDoubleClickZooming(true);


        mapController = BDMapView.getController();
        mapController.setZoom(15);
        mapController.enableClick(true);

        coupon = (Coupon) IntentObjectPool.getObjectExtra(getIntent(), "coupon", null);
        BDMapView.getOverlays().remove(itemItemizedOverlay);

        Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
        itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker, BDMapView);

        GeoPoint locationGeoPoint = new GeoPoint((int) (coupon.getLongitude() * 1E6), (int) (coupon.getLatitude() * 1E6));
        Ln.d(coupon.getLatitude() + "ssssss" + coupon.getLongitude() + locationGeoPoint.getLongitudeE6() + coupon.getTitle() + "ssss");
        OverlayItem overlayItem = new OverlayItem(locationGeoPoint, null, null);
        itemItemizedOverlay.addItem(overlayItem);
        BDMapView.getController().animateTo(locationGeoPoint);


        layoutInflater = LayoutInflater.from(AddressMapActivity.this);
        mapPopWindow = layoutInflater.inflate(R.layout.coupon_format, null);
        BDMapView.addView(mapPopWindow);
        BDMapView.getOverlays().add(itemItemizedOverlay);
        mapController.animateTo(locationGeoPoint);
        mapPopWindow.setVisibility(View.GONE);
        BDMapView.refresh();


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
            String title = coupon.getTitle();
            String content = coupon.getContent();
            String address = coupon.getAddress();


            TextView titleTextView = (TextView) mapPopWindow.findViewById(R.id.couponTitleTextView);
            TextView contentTextView = (TextView) mapPopWindow.findViewById(R.id.couponContentTextView);
            TextView addressTextView = (TextView) mapPopWindow.findViewById(R.id.couponAddressTextView);

            ImageView couponImageView = (ImageView) mapPopWindow.findViewById(R.id.new_content_img);
            titleTextView.setText(title);
            contentTextView.setText(content);
            addressTextView.setText(address);


            imageLoader.displayImage(coupon.getIconUrl(), couponImageView);

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


//跨activity间传对象
//Intent intent = new Intent(this, AboutUsActivity.class);
// IntentObjectPool.putObjectExtra(intent, "aaa", myLocationTextView);
//startActivity(intent);