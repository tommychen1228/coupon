package com.myideaway.coupon.model.user.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.user.service.remote.UserRSChangePassword;
import com.myideaway.easyapp.core.service.BizService;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class UserBSChangePassword extends BizService {
    final int NUM_SUCCESS = 1;
    private String email;
    private String password;
    private String newPassword;

    public UserBSChangePassword(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        UserRSChangePassword userRSChangePassword = new UserRSChangePassword();
        UserBSChangePasswordResult userBSChangePasswordResult = new UserBSChangePasswordResult();
        userRSChangePassword.setEmail(email);
        userRSChangePassword.setPassword(password);
        userRSChangePassword.setNewPassword(newPassword);
        JSONObject userRSChangePasswordObject = (JSONObject) userRSChangePassword.syncExecute();

        int stateNum =   userRSChangePasswordObject.optInt("status");

        if(stateNum == NUM_SUCCESS){
            userBSChangePasswordResult.setSuccess(true);
        }else {
            userBSChangePasswordResult.setSuccess(false);
            userBSChangePasswordResult.setErrorMSG(userRSChangePasswordObject.optString("message"));
        }





        return userBSChangePasswordResult;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public class UserBSChangePasswordResult {
        boolean isSuccess;
        String errorMSG;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getErrorMSG() {
            return errorMSG;
        }

        public void setErrorMSG(String errorMSG) {
            this.errorMSG = errorMSG;
        }
    }
}
