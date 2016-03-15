package com.myideaway.coupon.view;

import android.view.View;
import android.widget.ImageButton;
import com.myideaway.coupon.R;
import com.myideaway.coupon.view.common.BaseActivity;

/**
 * Created by duanchang on 13-12-17.
 */
public class LoginActivity extends BaseActivity {
    private ImageButton backButton;
    private LoginView loginView;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.login_activity);
        showNavigationBar(false);

        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        navigationBar.addLeftView(backButton);


        //登陆
        loginView = (LoginView) findViewById(R.id.loginView);
        loginView.setListener(new LoginView.LoginViewListener() {
            @Override
            public void loginSuccess(LoginView view) {
                finish();
            }

            @Override
            public void loginFault(LoginView view) {

            }
        });


    }
}
