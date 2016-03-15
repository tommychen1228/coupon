package com.myideaway.coupon.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.user.User;
import com.myideaway.coupon.view.common.BaseActivity;
import roboguice.util.Ln;


/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class UserActivity extends BaseActivity {
    public static final int REQUEST_CODE_REGISTER = 1;
    private RegisterActivity RegisterActivity;
    private LoginView loginView;
    private ImageView userPortraitImageView;
    private TextView userNameTextView;
    private TextView creditsTextView;
    private LinearLayout changePasswordLinearLayout;
    private LinearLayout collectionLinearLayout;
    private LinearLayout couponLinearLayout;
    private LinearLayout exitLinearLayout;
    private ApplicationContext applicationContext = ApplicationContext.getInstance();

    @Override
    protected void onResume() {
        super.onResume();
        if (applicationContext.isLogin(this, false)) {
            initUserMSGPage();
            loginView.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.user_info);
        showNavigationBar(false);

        navigationBar.setTitle("我的帐户");

        //获取用户头像 ImageView
        userPortraitImageView = (ImageView) findViewById(R.id.userPortraitImageView);
        //获取用户名 TextView
        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        //获取用户积分  TextView  只有积分分数
        creditsTextView = (TextView) findViewById(R.id.creditsTextView);

        //修改密码
        changePasswordLinearLayout = (LinearLayout) findViewById(R.id.changePasswordLinearLayout);
        changePasswordLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, ChangPasswordActivity.class);
                startActivity(intent);

            }
        });

        //我的收藏
        collectionLinearLayout = (LinearLayout) findViewById(R.id.collectionLinearLayout);
        collectionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, MyCollectionActivity.class);
                startActivity(intent);

            }
        });

        //优惠券
//        couponLinearLayout = (LinearLayout) findViewById(R.id.couponLinearLayout);
//        couponLinearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(UserActivity.this, MyCouponActivity.class);
//                startActivity(intent);
//
//            }
//        });

        //退出
        exitLinearLayout = (LinearLayout) findViewById(R.id.exitLinearLayout);
        exitLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(UserActivity.this).setMessage("您真的要退出吗?").setPositiveButton("是",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loginView = (LoginView) findViewById(R.id.loginView);
                        loginView.setVisibility(View.VISIBLE);
                        applicationContext.removeUser(UserActivity.this);
                    }
                }).setNegativeButton("否",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });


        //登陆
        loginView = (LoginView) findViewById(R.id.loginView);
        loginView.setListener(new LoginView.LoginViewListener() {
            @Override
            public void loginSuccess(LoginView view) {
                initUserMSGPage();
                loginView.setVisibility(View.GONE);
            }

            @Override
            public void loginFault(LoginView view) {

            }
        });

        loginView.setRegisterListener(new LoginView.RegisterListener() {
            @Override
            public void register(LoginView loginView) {
                Intent intent = new Intent(UserActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_CODE_REGISTER);
            }
        });
        if (applicationContext.isLogin(this, false)) {
            initUserMSGPage();
            loginView.setVisibility(View.GONE);
        }

    }

    private void initUserMSGPage() {
        User user = applicationContext.getUser(this);
        imageLoader.displayImage(user.getHeadImageUrl(), userPortraitImageView,normalImageDisplayOptions);
        userNameTextView.setText(user.getUsername());
        Ln.d(user.getCredits() + "积分");
        creditsTextView.setText(user.getCredits() + "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_REGISTER) {
            if (resultCode == RESULT_OK) {
                loginView.setVisibility(View.GONE);
                initUserMSGPage();
            }
        }
    }

}
