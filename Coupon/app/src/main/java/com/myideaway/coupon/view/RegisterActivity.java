package com.myideaway.coupon.view;

import android.view.View;
import android.widget.*;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.user.User;
import com.myideaway.coupon.model.user.service.biz.UserBSRegisterData;
import com.myideaway.coupon.view.common.BaseActivity;
import com.myideaway.easyapp.core.service.Service;
import roboguice.util.Ln;


import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-26
 * Time: 上午8:55
 * To change this template use File | Settings | File Templates.
 */
public class RegisterActivity extends BaseActivity {

    private Button registerButton;
    private EditText EmailEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private EditText telEditText;
    private RadioGroup radioGroup;
    private int sexInt = 1;
    private ImageButton backButton;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.register);
        showNavigationBar(false);

        navigationBar.setTitle("新用户注册");
        init();
    }

    private void init() {

        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        navigationBar.addLeftView(backButton);

        //获取Email输入框
        EmailEditText = (EditText) findViewById(R.id.EmailEditText);
        //获取昵称输入框
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        //获取密码输入框
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        //获取重复确认密码输入框
        repeatPasswordEditText = (EditText) findViewById(R.id.repeatPasswordEditText);
        //获取电话输入框
        telEditText = (EditText) findViewById(R.id.telEditText);
        //性别选择
        radioGroup = (RadioGroup) findViewById(R.id.sexRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) RegisterActivity.this.findViewById(radioButtonId);
                if (radioButton.getText().equals("女")) {
                    sexInt = 0;
                } else {
                    sexInt = 1;
                }


            }
        });


        // 注册按钮
        registerButton = (Button) this.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
                String num = "^\\d{6,20}$";
                if (!Pattern.matches(email, EmailEditText.getText().toString())) {
                    (RegisterActivity.this).showAlertDialog("请输入正确的邮箱");
                } else if (userNameEditText.getText().toString().equals("")) {
                    (RegisterActivity.this).showAlertDialog("昵称不能为空");
                } else if (passwordEditText.getText().toString().equals("")) {
                    (RegisterActivity.this).showAlertDialog("密码不能为空");

                } else if (!passwordEditText.getText().toString().equals(repeatPasswordEditText.getText().toString())) {
                    (RegisterActivity.this).showAlertDialog("两次密码输入不同");

                } else if (!Pattern.matches(num, telEditText.getText().toString())) {
                    (RegisterActivity.this).showAlertDialog("手机号码格式有误");

                } else {
                    showLoadingNewDataProgresssDialog();
                    UserBSRegisterData userBSRegisterData = new UserBSRegisterData(RegisterActivity.this);
                    userBSRegisterData.setEmail(EmailEditText.getText().toString());
                    userBSRegisterData.setPassword(passwordEditText.getText().toString());
                    userBSRegisterData.setTelPhone(telEditText.getText().toString());
                    userBSRegisterData.setGender(sexInt);
                    userBSRegisterData.setUserName(userNameEditText.getText().toString());
                    userBSRegisterData.asyncExecute();
                    userBSRegisterData.setOnSuccessHandler(new Service.OnSuccessHandler() {
                        @Override
                        public void onSuccess(Service service, Object o) {
                            UserBSRegisterData.UserBSRegisterDataResult userBSRegisterDataResult = (UserBSRegisterData.UserBSRegisterDataResult) o;
                            Ln.d("isSuccess;" + userBSRegisterDataResult.isSuccess());
                            if (userBSRegisterDataResult.isSuccess()) {
                                User user = userBSRegisterDataResult.getUser();
                                ApplicationContext applicationContext = ApplicationContext.getInstance();
                                applicationContext.setUser(user, RegisterActivity.this);
                                Ln.d("+user.getHeadImageUrl());" + user.getHeadImageUrl());
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                (RegisterActivity.this).showAlertDialog(userBSRegisterDataResult.getErrorMSG());

                            }

                            (RegisterActivity.this).hideProgressDialog();
                        }

                    });
                    userBSRegisterData.setOnFaultHandler(new Service.OnFaultHandler() {
                        @Override
                        public void onFault(Service service, Exception e) {
                            (RegisterActivity.this).hideProgressDialog();
                            Ln.e(e.getMessage(), e);
                            (RegisterActivity.this).showExceptionMessage(e);

                        }
                    });
                }


            }
        });

    }


}
