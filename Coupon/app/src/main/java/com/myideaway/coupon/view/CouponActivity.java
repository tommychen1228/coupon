package com.myideaway.coupon.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.zxing.client.android.Intents;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.area.BusinessArea;
import com.myideaway.coupon.model.coupon.Coupon;
import com.myideaway.coupon.model.coupon.service.biz.CouponBSGetListData;
import com.myideaway.coupon.model.merchant.Category;
import com.myideaway.coupon.view.common.BaseActivity;
import com.myideaway.coupon.view.common.Notification;
import com.myideaway.easyapp.core.service.Service;
import roboguice.util.Ln;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class CouponActivity extends BaseActivity {

    public static final int REQUEST_CODE_SCAN = 1;
    String businessAreas[];
    String CategoryAreas[];
    private Button businessAreaButton;
    private Button CategoryButton;
    private Button orderButton;
    private Button searchCouponButton;
    private EditText searchCouponEditText;
    private List<Coupon> couponList = new ArrayList<Coupon>();
    private PullToRefreshListView couponListView;
    private ApplicationContext applicationContext = ApplicationContext.getInstance();
    private List<BusinessArea> businessAreaList;
    private List<Category> categoryList;
    private int page = 1;
    private int areaID = 0;
    private int cateID = 0;
    private final int SORT_NEW = 2;
    private final int SORT_HOT = 3;
    private final int SORT_PRINT = 0;
    private final int SORT_DEFAULT = -1;
    private int sort = SORT_DEFAULT;
    private String key = "";
    private CouponAdapter couponAdapter;
    private boolean isPressQrButton = false;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.coupon);
        showNavigationBar(false);

        navigationBar.setTitle("优惠");
        isPressQrButton = false;
        ImageButton qrButton = (ImageButton) layoutInflater.inflate(R.layout.navigation_bar_image_view, null);
        qrButton.setImageResource(R.drawable.qrcode);
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!isPressQrButton){
                   qrButtonOnClick();
                   isPressQrButton = true;
               }

            }
        });
        navigationBar.addRightView(qrButton);


        init();

    }

    private void init() {

        searchCouponEditText = (EditText) findViewById(R.id.searchCouponEditText);
        searchCouponButton = (Button) findViewById(R.id.searchCouponButton);
        searchCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = searchCouponEditText.getText().toString();
                areaID = 0;
                cateID = 0;
                sort = SORT_DEFAULT;
                couponListRefresh(key, areaID, cateID, sort, true);
            }
        });


        //商圈列表
        businessAreaList = applicationContext.getBusinessAreaList();
        businessAreas = new String[businessAreaList.size() + 1];
        businessAreas[0] = "全部商圈";
        for (int i = 1; i < businessAreas.length; i++) {
            businessAreas[i] = businessAreaList.get(i - 1).getName();
        }
        businessAreaButton = (Button) findViewById(R.id.businessAreaButton);
        businessAreaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(CouponActivity.this).setItems(businessAreas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cateID = 0;
                        sort = SORT_DEFAULT;
                        key = "";
                        if (i == 0) {
                            areaID = 0;
                            couponListRefresh(key, areaID, cateID, sort, true);
                        } else {
                            areaID = businessAreaList.get(i - 1).getId();
                            couponListRefresh(key, areaID, cateID, sort, true);

                        }
                        businessAreaButton.setText(businessAreas[i]);
                    }
                }).show();
            }
        });

        //分类列表
        categoryList = applicationContext.getCategoryList();
        CategoryAreas = new String[categoryList.size()];
        CategoryAreas[0] = "全部分类";
        for (int i = 1; i < CategoryAreas.length; i++) {
            CategoryAreas[i] = categoryList.get(i - 1).getName();
        }
        CategoryButton = (Button) findViewById(R.id.CategoryButton);
        CategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applicationContext = ApplicationContext.getInstance();
                new AlertDialog.Builder(CouponActivity.this).setItems(CategoryAreas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        key = "";
                        areaID = 0;
                        sort = SORT_DEFAULT;
                        if (i == 0) {
                            cateID = 0;
                            couponListRefresh(key, areaID, cateID, sort, true);
                        } else {
                            cateID = categoryList.get(i - 1).getId();
                            couponListRefresh("", 0, cateID, 0, true);

                        }
                        CategoryButton.setText(CategoryAreas[i]);
                    }
                }).show();
            }
        });

        //
        final String orderButtonAreas[] = new String[]{"默认排序", "最新", "最热", "打印数"};
        orderButton = (Button) findViewById(R.id.orderButton);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(CouponActivity.this).setItems(orderButtonAreas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        key = "";
                        areaID = 0;
                        cateID = 0;
                        if(i == 0){
                            sort = SORT_DEFAULT;
                        }else if(i == 1){
                            sort = SORT_NEW;
                        }else if(i == 2){
                            sort = SORT_HOT;
                        }else if(i == 3){
                            sort = SORT_PRINT;
                        }
                        couponListRefresh(key, areaID, cateID, sort, true);
                        orderButton.setText(orderButtonAreas[i]);
                    }
                }).show();
            }

        });

        couponListView = (PullToRefreshListView) findViewById(R.id.couponListView);
        couponListView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        couponListView.getLoadingLayoutProxy(false, true).setPullLabel("正在加载");
        couponListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                couponListRefresh(key, areaID, cateID, sort, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                couponListLandMore(key, areaID, cateID, sort, ++page);

            }
        });

        couponAdapter = new CouponAdapter();
        couponListView.setAdapter(couponAdapter);


        couponListRefresh(key, areaID, cateID, sort, true);
    }

    private void couponListRefresh(String key, int areaID, int cateID, int Sort, final boolean isLock) {
        CouponBSGetListData couponBSGetData = new CouponBSGetListData(CouponActivity.this);

        couponBSGetData.setCityID(applicationContext.getCurrentCity().getId());
        couponBSGetData.setAreaID(areaID);
        couponBSGetData.setCateID(cateID);
        couponBSGetData.setPage(1);
        couponBSGetData.setSort(Sort);
        if (key != null) {
            couponBSGetData.setKey(key);
        }

        if (isLock) {
            showLoadingNewDataProgresssDialog();
        }

        couponBSGetData.asyncExecute();
        couponBSGetData.setOnSuccessHandler(new Service.OnSuccessHandler() {

            @Override
            public void onSuccess(Service service, Object o) {
                CouponBSGetListData.CouponBSGetDataResult couponBSGetDataResult = (CouponBSGetListData.CouponBSGetDataResult) o;
                couponList = couponBSGetDataResult.getCouponList();
                couponAdapter.notifyDataSetChanged();
                couponListView.getRefreshableView().scrollTo(0, 0);
                page = 1;
                if (isLock) {
                    hideProgressDialog();
                }
                if(couponList.size()==0){
                    showNoDataToast();
                }

                couponListView.onRefreshComplete();
            }

        });
        couponBSGetData.setOnFaultHandler(new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                Ln.e(e.getMessage(), e);
                showExceptionMessage(e);

                if (isLock) {
                    hideProgressDialog();
                }
                showNoDataToast();
                couponListView.onRefreshComplete();
            }
        });
    }

    private void couponListLandMore(String key, int areaID, int cateID, int Sort, final int page) {
        this.page = page;
        CouponBSGetListData couponBSGetData = new CouponBSGetListData(CouponActivity.this);
        couponBSGetData.setCityID(applicationContext.getCurrentCity().getId());
        couponBSGetData.setAreaID(areaID);
        couponBSGetData.setCateID(cateID);
        couponBSGetData.setPage(page);
        couponBSGetData.setSort(Sort);
        if (key != null) {
            couponBSGetData.setKey(key);
        }
        couponBSGetData.asyncExecute();
        couponBSGetData.setOnSuccessHandler(new Service.OnSuccessHandler() {

            @Override
            public void onSuccess(Service service, Object o) {
                CouponBSGetListData.CouponBSGetDataResult couponBSGetDataResult = (CouponBSGetListData.CouponBSGetDataResult) o;
                if (page <= couponBSGetDataResult.getPageTotal()) {
                    couponList.addAll(couponBSGetDataResult.getCouponList());
                    couponAdapter.notifyDataSetChanged();
                } else {
                    CouponActivity.this.page--;
                    showNoMoreDataToast();

                }
                couponListView.onRefreshComplete();

            }
        });
        couponBSGetData.setOnFaultHandler(new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                CouponActivity.this.page--;

                Ln.e(e.getMessage(), e);
                showExceptionMessage(e);
                couponListView.onRefreshComplete();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        isPressQrButton  = false;
        if (requestCode == REQUEST_CODE_SCAN) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                try {
                    contents = new String(contents.getBytes("ISO-8859-1"), "GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

//                String keyword = "fdetail/id-";
                String keyword = "fdetail&id=";
                int keywordLocation = contents.indexOf(keyword);
                String  couponIDStr =  contents.substring(keywordLocation+keyword.length());
//                showAlertDialog("format:" + format + ", contents:" + contents + "couponID" + couponIDStr);

                if(keywordLocation == -1){
                    showAlertDialog("非本系统二维码");
                    return;
                }
                int couponID = 0;
                try {

                     couponID = Integer.parseInt(couponIDStr);

                }catch (Exception e){
                    showAlertDialog("非本系统二维码");
                    return;
                }

                Intent intent = new Intent(CouponActivity.this, CouponDetailActivity.class);
                intent.putExtra("couponID", couponID);
                startActivity(intent);


            } else {

                showToastMessage("Scan fault");
            }
        }
    }

    public void qrButtonOnClick() {
        Intent intentScan = new Intent(this, com.google.zxing.client.android.CaptureActivity.class);
        intentScan.setAction(Intents.Scan.ACTION);
        startActivityForResult(intentScan, REQUEST_CODE_SCAN);
    }

    //创建优惠卷列表
    public class CouponAdapter extends BaseAdapter {
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
                    Intent intent = new Intent(CouponActivity.this, CouponDetailActivity.class);
                    intent.putExtra("couponID", coupon.getId());
                    startActivity(intent);

                }
            });
            return couponView;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
    //切换城市后的通知
    @Override
    protected String[] observeNotifications() {
        return new String[]{Notification.SELECT_CITY_SUCCESS, Notification.LOGIN_SUCCESS};
    }

    @Override
    protected void processNotifications(String name, Object param) {
        if (name.equals(Notification.SELECT_CITY_SUCCESS)) {
//            if (coupon.getId() ==  (Integer)param) {
//                coupon.setCommentCount(coupon.getCommentCount() + 1);
//                couponCommentTextView.setText("评论(" + coupon.getCommentCount() + ")");
//            }
            init();
        }
    }
}


