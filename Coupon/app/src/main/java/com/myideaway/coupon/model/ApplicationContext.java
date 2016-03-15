package com.myideaway.coupon.model;

import android.content.Context;
import com.myideaway.coupon.model.area.BusinessArea;
import com.myideaway.coupon.model.area.City;
import com.myideaway.coupon.model.global.service.AppInfo;
import com.myideaway.coupon.model.merchant.Category;
import com.myideaway.coupon.model.notice.Notice;
import com.myideaway.coupon.model.user.User;
import com.myideaway.coupon.model.user.service.biz.UserBSGetSaveLogin;
import com.myideaway.coupon.model.user.service.biz.UserBSRemoveSaveLogin;
import com.myideaway.coupon.model.user.service.biz.UserBSSaveLogin;
import com.myideaway.coupon.view.NeedLoginDialog;
import com.myideaway.coupon.view.common.BaseActivity;
import com.myideaway.coupon.view.util.Location;
import roboguice.util.Ln;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Organization: http://www.myideaway.com
 * User: Tommy Chen
 * Date: 13-12-2
 * Time: PM9:47
 */
public class ApplicationContext {
    private static ApplicationContext instance = new ApplicationContext();
    private City currentCity = new City();
    private AppInfo appInfo = new AppInfo();
    private Map<Integer, City> cityMap  = new HashMap<Integer, City>();
    private List<Category> categoryList = new ArrayList<Category>();
    private List<Notice> noticeList = new ArrayList<Notice>();
    private List<BusinessArea> businessAreaList = new ArrayList<BusinessArea>();
    private User user = null;
    private Location location;

    private ApplicationContext() {

    }

    public boolean isLogin(final Context context,boolean isShowLoginActivity){
        if(user == null){
           user = getUser(context);
            if (user ==null){
                if(isShowLoginActivity){
                    new NeedLoginDialog(context).show();
                }
                return false;
            }else {
                return true;
            }
        }else {
            return true;
        }
    }

    public static ApplicationContext getInstance() {
        return instance;
    }

    public static void setInstance(ApplicationContext instance) {
        ApplicationContext.instance = instance;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public Map<Integer, City> getCityMap() {
        return cityMap;
    }

    public void setCityMap(Map<Integer, City> cityMap) {
        this.cityMap = cityMap;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Notice> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<Notice> noticeList) {
        this.noticeList = noticeList;
    }

    public List<BusinessArea> getBusinessAreaList() {
        return businessAreaList;
    }

    public void setBusinessAreaList(List<BusinessArea> businessAreaList) {
        this.businessAreaList = businessAreaList;
    }

    public User getUser(final Context context) {
        if (user == null){
            UserBSGetSaveLogin userBSGetSaveLogin = new UserBSGetSaveLogin(context);

            try {
                user = (User) userBSGetSaveLogin.syncExecute();


            } catch (Exception e) {
                ((BaseActivity)context).hideProgressDialog();
                Ln.e(e.getMessage(), e);
                ((BaseActivity)context).showExceptionMessage(e);
            }

        }
        return user;
    }

    public void setUser(User user,final Context context) {
        this.user = user;
        UserBSSaveLogin userBSSaveLogin = new UserBSSaveLogin(context);
        userBSSaveLogin.setUser(user);
        try {
            userBSSaveLogin.syncExecute();
        } catch (Exception e) {
            ((BaseActivity)context).hideProgressDialog();
            Ln.e(e.getMessage(), e);
            ((BaseActivity)context).showExceptionMessage(e);
        }
    }

    public void removeUser(final Context context){
        user = null;
        UserBSRemoveSaveLogin userBSRemoveSaveLogin = new UserBSRemoveSaveLogin(context);
        try {
            userBSRemoveSaveLogin.syncExecute();
        } catch (Exception e) {
            ((BaseActivity)context).hideProgressDialog();
            Ln.e(e.getMessage(), e);
            ((BaseActivity)context).showExceptionMessage(e);
        }

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
