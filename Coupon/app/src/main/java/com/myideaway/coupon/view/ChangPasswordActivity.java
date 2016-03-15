package com.myideaway.coupon.view;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.user.User;
import com.myideaway.coupon.model.user.service.biz.UserBSChangePassword;
import com.myideaway.coupon.view.common.BaseActivity;
import com.myideaway.easyapp.core.service.Service;
import roboguice.util.Ln;


/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class ChangPasswordActivity extends BaseActivity {

    private ImageButton backButton;
    private Button changeButton;
    private EditText myPasswordEditText;
    private EditText newPasswordEditText;
    private EditText reNewPasswordEditText;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.change_password);
        showNavigationBar(false);


        navigationBar.setTitle("修改密码");

        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.addLeftView(backButton);


        myPasswordEditText = (EditText) findViewById(R.id.myPasswordEditText);
        newPasswordEditText = (EditText) findViewById(R.id.newPasswordEditText);
        reNewPasswordEditText = (EditText) findViewById(R.id.reNewPasswordEditText);


        changeButton = (Button) findViewById(R.id.changeButton);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingNewDataProgresssDialog();
                User user = ApplicationContext.getInstance().getUser(ChangPasswordActivity.this);
                UserBSChangePassword userBSChangePassword = new UserBSChangePassword(ChangPasswordActivity.this);
                userBSChangePassword.setEmail(user.getEmail());
                userBSChangePassword.setPassword(myPasswordEditText.getText().toString());
                userBSChangePassword.setNewPassword(newPasswordEditText.getText().toString());
                userBSChangePassword.asyncExecute();
                userBSChangePassword.setOnSuccessHandler(new Service.OnSuccessHandler() {
                    @Override
                    public void onSuccess(Service service, Object o) {
                        UserBSChangePassword.UserBSChangePasswordResult userBSChangePasswordResult = (UserBSChangePassword.UserBSChangePasswordResult) o;
                        if (userBSChangePasswordResult.isSuccess()) {
                            finish();
                        } else {
                            showAlertDialog(userBSChangePasswordResult.getErrorMSG());
                        }
                        hideProgressDialog();
                    }
                });
                userBSChangePassword.setOnFaultHandler(new Service.OnFaultHandler() {
                    @Override
                    public void onFault(Service service, Exception e) {
                        hideProgressDialog();
                        Ln.e(e.getMessage(), e);
                        showExceptionMessage(e);

                    }
                });
            }
        });


    }


}
