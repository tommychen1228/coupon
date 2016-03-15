package com.myideaway.coupon.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.widget.*;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.coupon.Coupon;
import com.myideaway.coupon.model.coupon.service.biz.CouponBSCollectCoupon;
import com.myideaway.coupon.model.coupon.service.biz.CouponBSGetDetailData;
import com.myideaway.coupon.model.user.User;
import com.myideaway.coupon.view.common.BaseActivity;
import com.myideaway.coupon.view.common.Notification;
import com.myideaway.easyapp.core.intent.IntentObjectPool;
import com.myideaway.easyapp.core.service.Service;
import roboguice.util.Ln;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class CouponDetailActivity extends BaseActivity {
    private ImageButton backButton;
    private ImageButton moreImageButton;
    private ImageView couponDetailImageView;
    private TextView couponDetailTextView;
    private TextView couponNameTextView;
    private ImageView couponDetailBigImageView;
    private TextView couponTimeMSGTextView;
    private Button couponCommentButton;
    private Button couponCollectionButton;
    private Button couponShareButton;
    private Button couponSendButton;
    private TextView couponAddressTextView;
    private LinearLayout couponAddressLinearLayout;
    private TextView couponTelPhoneTextView;
    private LinearLayout couponTelPhoneLinearLayout;
    private TextView couponCommentTextView;
    private LinearLayout couponCommentLinearLayout;
    private Coupon coupon;
    private boolean isFavor = false;
    private String[] snss;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.coupon_detail);
        showNavigationBar(false);
        Resources res = getResources();

        navigationBar.setTitle("优惠详情");

        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.addLeftView(backButton);

        moreImageButton = (ImageButton) layoutInflater.inflate(R.layout.navigation_bar_image_view, null);
        moreImageButton.setImageResource(R.drawable.more);

        final String otherCoupon[] = new String[]{"本店全部特权地址", "其他分店"};
        moreImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(CouponDetailActivity.this).setItems(otherCoupon, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (otherCoupon[i] == otherCoupon[0]) {
                            Intent intent = new Intent(CouponDetailActivity.this, OtherCouponActivity.class);
                            intent.putExtra("MerchantID", coupon.getShop().getId());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(CouponDetailActivity.this, OtherShopActivity.class);
                            intent.putExtra("BrandID", coupon.getShop().getMerchant().getId());
                            startActivity(intent);
                        }
                    }
                }).show();

            }
        });

        navigationBar.addRightView(moreImageButton);
        init();
        initDetailData();
    }


    @Override
    protected String[] observeNotifications() {
        return new String[]{Notification.WRITE_COUPON_COMMENT_SUCCESS, Notification.LOGIN_SUCCESS};
    }

    @Override
    protected void processNotifications(String name, Object param) {
        if (name.equals(Notification.WRITE_COUPON_COMMENT_SUCCESS)) {
            if (coupon.getId() ==  (Integer)param) {
                coupon.setCommentCount(coupon.getCommentCount() + 1);
                couponCommentTextView.setText("评论(" + coupon.getCommentCount() + ")");
            }
        }
    }

    private void init() {

        coupon = new Coupon();

        coupon.setId(getIntent().getIntExtra("couponID", 0));
        Ln.d(coupon.getId() + "");
        //优惠卷详情 顶部缩略图
        couponDetailImageView = (ImageView) findViewById(R.id.couponDetailImageView);

        //优惠卷顶部的名称
        couponNameTextView = (TextView) findViewById(R.id.couponNameTextView);

        //优惠卷详情
        couponDetailTextView = (TextView) findViewById(R.id.couponDetailTextView);

        //优惠卷详情中部大图
        couponDetailBigImageView = (ImageView) findViewById(R.id.couponDetailBigImageView);

        //优惠卷时间信息
        couponTimeMSGTextView = (TextView) findViewById(R.id.couponTimeMSGTextView);

        //评论按钮
        couponCommentButton = (Button) findViewById(R.id.couponCommentButton);
        couponCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSubmit = ApplicationContext.getInstance().isLogin(CouponDetailActivity.this, true);
                if (isSubmit) {
                    Intent intent = new Intent(CouponDetailActivity.this, CommentWriteActivity.class);
                    intent.putExtra("couponID", coupon.getId());
                    startActivity(intent);
                }

            }
        });

        //收藏按钮
        couponCollectionButton = (Button) findViewById(R.id.couponCollectionButton);
        couponCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationContext applicationContext = ApplicationContext.getInstance();
                if (applicationContext.isLogin(CouponDetailActivity.this, true)) {
                    showLoadingNewDataProgresssDialog();
                    CouponBSCollectCoupon couponBSCollectCoupon = new CouponBSCollectCoupon(CouponDetailActivity.this);
                    couponBSCollectCoupon.setCouponid(coupon.getId());
                    User user = applicationContext.getUser(CouponDetailActivity.this);
                    couponBSCollectCoupon.setPassword(user.getPassword());
                    couponBSCollectCoupon.setEmail(user.getEmail());
                    Ln.d(user.getEmail() + "user.getEmail()");
                    couponBSCollectCoupon.asyncExecute();
                    couponBSCollectCoupon.setOnSuccessHandler(new Service.OnSuccessHandler() {
                        @Override
                        public void onSuccess(Service service, Object o) {
                            hideProgressDialog();
                            if (isFavor) {
                                isFavor = false;
                                showToastMessage("取消特权");
                                couponCollectionButton.setText("加入特权");
                            } else {
                                isFavor = true;
                                showToastMessage("加入特权成功");
                                couponCollectionButton.setText("取消特权");
                            }
                        }
                    });
                    couponBSCollectCoupon.setOnFaultHandler(new Service.OnFaultHandler() {
                        @Override
                        public void onFault(Service service, Exception e) {
                            hideProgressDialog();
                            Ln.e(e.getMessage(), e);
                            showExceptionMessage(e);
                        }
                    });

                } else {
                }


            }
        });

        //分享按钮
        couponShareButton = (Button) findViewById(R.id.couponShareButton);
        couponShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //第一套 系统自带分享
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_SUBJECT, coupon.getTitle());
//                intent.putExtra(Intent.EXTRA_TEXT, coupon.getShareContent());
//                intent.putExtra("sms_body", coupon.getShareContent());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(Intent.createChooser(intent, "达人说"));
                //web分享
                snss = new String[getSnsList().size()];
                for (int i = 0; i < getSnsList().size(); i++) {
                    snss[i] = getSnsList().get(i).get("title");
                }
                new AlertDialog.Builder(CouponDetailActivity.this).setItems(snss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri content_url = Uri.parse(getSnsList().get(i).get("url") + "&title=" + "达人说特权:" + coupon.getShareContent());
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                }).show();
            }
        });
        //发送按钮
        couponSendButton = (Button) findViewById(R.id.couponSendButton);
        couponSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("smsto:");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "达人说特权:"  + coupon.getShareContent());
                startActivity(it);

//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("message/rfc822");
//                i.putExtra(Intent.EXTRA_EMAIL, "");
//                i.putExtra(Intent.EXTRA_TEXT,"达人说特权:"  + coupon.getShareContent());
//                try {
//                    CouponDetailActivity.this.startActivity(i);
//                } catch (android.content.ActivityNotFoundException e) {
//                    Ln.e("未找到能发送邮件的客户端", e);
//                }
            }
        });

        //优惠卷地址
        couponAddressTextView = (TextView) findViewById(R.id.couponAddressTextView);
        //优惠卷地址LinearLayout
        couponAddressLinearLayout = (LinearLayout) findViewById(R.id.couponAddressLinearLayout);
        couponAddressLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CouponDetailActivity.this, AddressMapActivity.class);
                IntentObjectPool.putObjectExtra(intent, "coupon", coupon);
                startActivity(intent);
            }
        });

        //优惠电话
        couponTelPhoneTextView = (TextView) findViewById(R.id.couponTelPhoneTextView);
        //优惠电话LinearLayout
        couponTelPhoneLinearLayout = (LinearLayout) findViewById(R.id.couponTelPhoneLinearLayout);
        couponTelPhoneLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + couponTelPhoneTextView.getText()));
                CouponDetailActivity.this.startActivity(intent);
            }
        });

        //优惠评论
        couponCommentTextView = (TextView) findViewById(R.id.couponCommentTextView);
        //优惠评论LinearLayout
        couponCommentLinearLayout = (LinearLayout) findViewById(R.id.couponCommentLinearLayout);
        couponCommentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CouponDetailActivity.this, CommentListActivity.class);
                intent.putExtra("couponID", coupon.getId());
                startActivity(intent);
            }
        });
    }

    //    private  void isCollection(){
//        if (!isFavor){
//            couponCollectionButton.setText("收藏");
//        }else {
//            couponCollectionButton.setText("取消收藏");
//        }
//    }
    private void initDetailData() {
        showLoadingNewDataProgresssDialog();
        CouponBSGetDetailData couponBSGetDetailData = new CouponBSGetDetailData(CouponDetailActivity.this);
        couponBSGetDetailData.setCoupon(coupon);
        User user = ApplicationContext.getInstance().getUser(CouponDetailActivity.this);
        if (user != null) {
            couponBSGetDetailData.setEmail(user.getEmail());
            couponBSGetDetailData.setPassword(user.getPassword());
        }
        couponBSGetDetailData.asyncExecute();
        couponBSGetDetailData.setOnSuccessHandler(new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                CouponBSGetDetailData.CouponBSGetDetailDataResult couponBSGetDetailDataResult = (CouponBSGetDetailData.CouponBSGetDetailDataResult) o;

                coupon = couponBSGetDetailDataResult.getCoupon();

                //设置顶部图片
                imageLoader.displayImage(coupon.getIconUrl(), couponDetailImageView,normalImageDisplayOptions);

                //顶部名称
                couponNameTextView.setText(coupon.getTitle());

                //主要信息
                couponDetailTextView.setText(coupon.getContent());

                //设置中部大图
                imageLoader.displayImage(coupon.getImageUrl(), couponDetailBigImageView,normalImageDisplayOptions);

                if (coupon.getFavorers().size() > 0) {
                    isFavor = true;
                    couponCollectionButton.setText("取消特权");
                }

                //优惠卷时间信息
                couponTimeMSGTextView.setText(coupon.getExpireTip());

                //优惠卷地址信息
                couponAddressTextView.setText(coupon.getAddress());

                //优惠卷电话
                couponTelPhoneTextView.setText(coupon.getShop().getTel());

                //评论数量
                couponCommentTextView.setText("评论(" + coupon.getCommentCount() + ")");

                hideProgressDialog();
            }
        });
        couponBSGetDetailData.setOnFaultHandler(new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                hideProgressDialog();
                Ln.e(e.getMessage(), e);
                showExceptionMessage(e);
                showAlertDialog("优惠卷已过期或被商家删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
            }
        });
    }

    private ArrayList<HashMap<String, String>> getSnsList() {
        ArrayList<HashMap<String, String>> snsLst = new ArrayList<HashMap<String, String>>();
        // // 腾讯微博
        HashMap<String, String> snsMap = new HashMap<String, String>();
        // 新浪微博
        snsMap = new HashMap<String, String>();
        snsMap.put("title", "新浪微博");
        snsMap.put("url", "http://www.jiathis.com/send/?webid=tsina");
        snsLst.add(snsMap);
        // 腾讯微博
        snsMap = new HashMap<String, String>();
        snsMap.put("title", "腾讯微博");
        snsMap.put("url", "http://www.jiathis.com/send/?webid=tqq");
        snsLst.add(snsMap);
        // 搜狐微博
        snsMap = new HashMap<String, String>();
        snsMap.put("title", "搜狐微博");
        snsMap.put("url", "http://www.jiathis.com/send/?webid=tsohu");
        snsLst.add(snsMap);
        // 网易微博
        snsMap = new HashMap<String, String>();
        snsMap.put("title", "网易微博");
        snsMap.put("url", "http://www.jiathis.com/send/?webid=t163");
        snsLst.add(snsMap);

        return snsLst;
    }
}
