package com.myideaway.coupon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.user.User;
import com.myideaway.coupon.model.user.service.biz.UserBSLoginData;
import com.myideaway.coupon.view.common.BaseActivity;
import com.myideaway.easyapp.core.service.Service;
import roboguice.util.Ln;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-26
 * Time: 上午8:55
 * To change this template use File | Settings | File Templates.
 */
public class LoginView extends RelativeLayout {

    private Button registerPageButton;
    private Button loginButton;
    private EditText EmailEditText;
    private EditText passwordEditText;
    private LoginViewListener listener;
    private RegisterListener registerListener;

    public LoginView(Context context) {
        super(context);    //To change body of overridden methods use File | Settings | File Templates.
        init();
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);    //To change body of overridden methods use File | Settings | File Templates.
        init();
    }

    public LoginView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);    //To change body of overridden methods use File | Settings | File Templates.
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.login, this);

        //获取Email输入框
        EmailEditText = (EditText) findViewById(R.id.EmailEditText);
        //获取密码输入框
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);


        //确定登陆按钮
        loginButton = (Button) this.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    login();

                } else {

                }

            }
        });
        //注册按钮
        registerPageButton = (Button) findViewById(R.id.registerPageButton);
        registerPageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                registerListener.register(LoginView.this);
            }
        });

    }

    private void login() {
        ((BaseActivity) getContext()).showLoadingNewDataProgresssDialog();
        UserBSLoginData userBSLoginData = new UserBSLoginData(getContext());
        userBSLoginData.setEmail(EmailEditText.getText().toString());
        userBSLoginData.setPassword(passwordEditText.getText().toString());
        userBSLoginData.asyncExecute();

        userBSLoginData.setOnSuccessHandler(new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                UserBSLoginData.UserBSLoginDataResult userBSLoginDataResult = (UserBSLoginData.UserBSLoginDataResult) o;
                if (userBSLoginDataResult.isSuccess()) {
                    User user = userBSLoginDataResult.getUser();
                    ApplicationContext applicationContext = ApplicationContext.getInstance();
                    applicationContext.setUser(user, getContext());
                    EmailEditText.setText("");
                    passwordEditText.setText("");
                    listener.loginSuccess(LoginView.this);

                } else {

                    ((BaseActivity) getContext()).showAlertDialog(userBSLoginDataResult.getErrorMSG());
                    listener.loginFault(LoginView.this);
                }

                ((BaseActivity) getContext()).hideProgressDialog();
            }
        });
        userBSLoginData.setOnFaultHandler(new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                ((BaseActivity) getContext()).hideProgressDialog();
                Ln.e(e.getMessage(), e);
                ((BaseActivity) getContext()).showExceptionMessage(e);

            }
        });

    }

    public void setListener(LoginViewListener listener) {
        this.listener = listener;
    }

    public void setRegisterListener(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    public interface RegisterListener {
        public void register(LoginView loginView);
    }

    public interface LoginViewListener {
        public void loginSuccess(LoginView loginView);

        public void loginFault(LoginView loginView);


    }
}
