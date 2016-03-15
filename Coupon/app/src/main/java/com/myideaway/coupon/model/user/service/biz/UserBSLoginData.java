package com.myideaway.coupon.model.user.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.user.User;
import com.myideaway.coupon.model.user.service.remote.UserRSGetMyListData;
import com.myideaway.coupon.model.user.service.remote.UserRSLoginData;
import com.myideaway.easyapp.core.service.BizService;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class UserBSLoginData extends BizService {
    final int NUM_SUCCESS = 1;
    private String userID;
    private String password;
    private String userName;
    private String Email;
    private boolean isSuccess;
    private String errorMSG;

    public UserBSLoginData(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        UserRSLoginData userRSLoginData = new UserRSLoginData();
        userRSLoginData.setEmail(Email);
        userRSLoginData.setPassword(password);
        JSONObject userLoginJsonObject = (JSONObject) userRSLoginData.syncExecute();
        int stateNum =   userLoginJsonObject.optInt("return");
        UserBSLoginDataResult userBSLoginDataResult = new UserBSLoginDataResult();

         if (stateNum == NUM_SUCCESS){
             UserRSGetMyListData userRSGetMyListData = new UserRSGetMyListData();
             userID =  userLoginJsonObject.optString("uid");
             password = userLoginJsonObject.optString("user_pwd");
             userName = userLoginJsonObject.optString("user_name");
             Email = userLoginJsonObject.optString("user_email");
             userRSGetMyListData.setUserID(userID);
             userRSGetMyListData.setPassword(password);
             userRSGetMyListData.setUserName(userName);
             userRSGetMyListData.setEmail(Email);
             JSONObject userMSGJsonObject = (JSONObject) userRSGetMyListData.syncExecute();
             User user = new User();
             user.setEmail(userMSGJsonObject.optString("email"));
             user.setUsername(userMSGJsonObject.optString("user_name"));
             user.setCredits(userMSGJsonObject.optInt("jifen"));
             user.setHeadImageUrl(userMSGJsonObject.optString("img"));
             user.setPassword(userMSGJsonObject.optString("user_pwd"));
             user.setId(userMSGJsonObject.optInt("uid"));
             userBSLoginDataResult.setUser(user);
             isSuccess = true;
             userBSLoginDataResult.setSuccess(isSuccess);
         } else {
             errorMSG =  userLoginJsonObject.optString("info");
             isSuccess = false;
             userBSLoginDataResult.setErrorMSG(errorMSG);
             userBSLoginDataResult.setSuccess(isSuccess);
         }



        return userBSLoginDataResult;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public class UserBSLoginDataResult {
        boolean isSuccess;
        String errorMSG;
        User user = new User();

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getErrorMSG() {
            return errorMSG;
        }

        public void setErrorMSG(String errorMSG) {
            this.errorMSG = errorMSG;
        }
    }
}
