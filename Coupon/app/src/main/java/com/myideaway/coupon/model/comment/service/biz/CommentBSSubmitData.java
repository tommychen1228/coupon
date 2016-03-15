package com.myideaway.coupon.model.comment.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.comment.Comment;
import com.myideaway.coupon.model.comment.service.remote.CommentRSGetListData;
import com.myideaway.coupon.model.comment.service.remote.CommentRSSubmitData;
import com.myideaway.coupon.model.user.User;
import com.myideaway.easyapp.core.service.BizService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class CommentBSSubmitData extends BizService {
    private   String content;
    private String email;
    private int userID;
    private  String password;
    public CommentBSSubmitData(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        CommentBSSubmitDataResult commentBSSubmitDataResult = new CommentBSSubmitDataResult();
        CommentRSSubmitData commentRSSubmitData = new CommentRSSubmitData();
        commentRSSubmitData.setContent(content);
        commentRSSubmitData.setEmail(email);
        commentRSSubmitData.setPassword(password);
        commentRSSubmitData.setUserID(userID);
        JSONObject  resultJsonObject = (JSONObject) commentRSSubmitData.syncExecute();
        int  resultNum =  resultJsonObject.optInt("return");
        if(resultNum==1){
            commentBSSubmitDataResult.setSuccess(true);
        }else {
            String errorMSG = resultJsonObject.optString("info");
            commentBSSubmitDataResult.setErrorMSG(errorMSG);
            commentBSSubmitDataResult.setSuccess(false);
        }

        return commentBSSubmitDataResult;
    }

    public class CommentBSSubmitDataResult {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

