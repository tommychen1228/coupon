package com.myideaway.coupon.model.user.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.user.User;
import com.myideaway.coupon.model.user.service.remote.UserRSRegisterData;
import com.myideaway.easyapp.core.service.BizService;

import org.json.JSONObject;
import roboguice.util.Ln;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class UserBSRegisterData extends BizService {
    private final int NUM_SUCCESS = 1;
    private String email;
    private String userName;
    private String password;
    private String telPhone;
    private int gender;
    private boolean isSuccess;
    private Context context;
    private String errorMSG;
    private UserBSRegisterDataResult userBSRegisterDataResult = new UserBSRegisterDataResult();
    private User user = new User();

    public UserBSRegisterData(Context context) {
        super(context);
        this.context = context;

    }

    @Override
    protected Object onExecute() throws Exception {
        UserRSRegisterData userRSRegisterData = new UserRSRegisterData();
        userRSRegisterData.setGender(gender);
        userRSRegisterData.setEmail(email);
        userRSRegisterData.setPassword(password);
        userRSRegisterData.setTelPhone(telPhone);
        userRSRegisterData.setUserName(userName);
        JSONObject userRegisterJsonObject = (JSONObject) userRSRegisterData.syncExecute();
        int stateNum = userRegisterJsonObject.optInt("return");
        if (stateNum == NUM_SUCCESS) {
            user.setHeadImageUrl(userRegisterJsonObject.optString("user_avatar"));
            user.setPassword(userRegisterJsonObject.optString("pwd"));
            user.setUsername(userRegisterJsonObject.optString("user_name"));
            user.setEmail(userRegisterJsonObject.optString("user_email"));
            UserBSLoginData userBSLoginData = new UserBSLoginData(context);
            userBSLoginData.setEmail(user.getEmail());
            userBSLoginData.setPassword(user.getPassword());
            UserBSLoginData.UserBSLoginDataResult userBSLoginDataResult = (UserBSLoginData.UserBSLoginDataResult) userBSLoginData.syncExecute();
            user = userBSLoginDataResult.getUser();
            isSuccess = true;
            userBSRegisterDataResult.setUser(user);
            userBSRegisterDataResult.setSuccess(isSuccess);
        } else {
            errorMSG = userRegisterJsonObject.optString("info");
            isSuccess = false;
            Ln.d("直接跳出");
            userBSRegisterDataResult.setErrorMSG(errorMSG);
            userBSRegisterDataResult.setSuccess(isSuccess);
        }

        return userBSRegisterDataResult;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public class UserBSRegisterDataResult {
        boolean isSuccess;
        String errorMSG;
        User user;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public String getErrorMSG() {
            return errorMSG;
        }

        public void setErrorMSG(String errorMSG) {
            this.errorMSG = errorMSG;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}
