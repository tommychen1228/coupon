package com.myideaway.coupon.view;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.view.common.BaseActivity;

import com.umeng.update.*;
import roboguice.util.Ln;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class MoreActivity extends BaseActivity {
    LinearLayout noticeLinearLayout;
    LinearLayout serviceTelephoneLinearLayout;
    LinearLayout serviceEmailLinearLayout;
    LinearLayout aboutUsLinearLayout;
    LinearLayout versionCheckingLinearLayout;
    TextView telPhoneTextView;
    TextView emailAddressTextView;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.more);
        showNavigationBar(false);

        navigationBar.setTitle("更多信息");

        //公告列表
        noticeLinearLayout = (LinearLayout) findViewById(R.id.noticeLinearLayout);
        noticeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoreActivity.this, NoticeListActivity.class);
                startActivity(intent);

            }
        });

        //客服电话
        telPhoneTextView = (TextView) findViewById(R.id.telPhoneTextView);
//        telPhoneTextView.setText(ApplicationContext.getInstance().getAppInfo().getCustomerServicePhone());
        serviceTelephoneLinearLayout = (LinearLayout) findViewById(R.id.serviceTelephoneLinearLayout);
        serviceTelephoneLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ApplicationContext.getInstance().getAppInfo().getCustomerServicePhone()));
                MoreActivity.this.startActivity(intent);
            }
        });

        //serviceEmail
        emailAddressTextView = (TextView) findViewById(R.id.emailAddressTextView);
//        emailAddressTextView.setText(ApplicationContext.getInstance().getAppInfo().getCustomerServiceEmail());
        serviceEmailLinearLayout = (LinearLayout) findViewById(R.id.serviceEmailLinearLayout);
        serviceEmailLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{ApplicationContext.getInstance().getAppInfo().getCustomerServiceEmail()});
                try {
                    MoreActivity.this.startActivity(i);
                } catch (android.content.ActivityNotFoundException e) {
                    Ln.e("未找到能发送邮件的客户端", e);
                }

            }
        });

        //About us
        aboutUsLinearLayout = (LinearLayout) findViewById(R.id.aboutUsLinearLayout);
        aboutUsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoreActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
        //版本检测
        versionCheckingLinearLayout = (LinearLayout) findViewById(R.id.versionCheckingLinearLayout);
        versionCheckingLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                UmengUpdateAgent.forceUpdate(MoreActivity.this);
                showLoadingNewDataProgresssDialog();
               UmengUpdateAgent.setUpdateAutoPopup(true);
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
                        hideProgressDialog();
                        switch (updateStatus) {
                            case UpdateStatus.Yes: // has update
                                UmengUpdateAgent.showUpdateDialog(MoreActivity.this, updateInfo);
                                break;
                            case UpdateStatus.No: // has no update
                                Toast.makeText(MoreActivity.this, "没有更新", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.NoneWifi: // none wifi
                                Toast.makeText(MoreActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.Timeout: // time out
                                Toast.makeText(MoreActivity.this, "超时", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                UmengUpdateAgent.update(MoreActivity.this);
            }
        });
    }


}
