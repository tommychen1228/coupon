package com.myideaway.coupon.model.global.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.area.BusinessArea;
import com.myideaway.coupon.model.area.City;
import com.myideaway.coupon.model.global.service.AppInfo;
import com.myideaway.coupon.model.global.service.remote.GlobalRSInitData;
import com.myideaway.coupon.model.merchant.Category;
import com.myideaway.coupon.model.notice.Notice;
import com.myideaway.easyapp.core.service.BizService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class GlobalBSInitData extends BizService {
    private String cityName;
    public GlobalBSInitData(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        GlobalRSInitData globalRSInitData = new GlobalRSInitData();
        globalRSInitData.setCityName(cityName);
        JSONObject globalJsonObject = (JSONObject) globalRSInitData.syncExecute();
        GlobalBSInitDataResult globalBSInitDataResult = new GlobalBSInitDataResult();

        //获取软件信息
        AppInfo appInfo = new AppInfo();
        appInfo.setAboutInfo(globalJsonObject.optString("about_info"));
        appInfo.setCustomerServiceEmail(globalJsonObject.optString("kf_email"));
        appInfo.setCustomerServicePhone(globalJsonObject.optString("kf_phone"));
        globalBSInitDataResult.setAppInfo(appInfo);

        //获取当前城市信息
        City currentCity = new City();
        currentCity.setId(globalJsonObject.optInt("city_id"));
        currentCity.setName(globalJsonObject.optString("city_name"));

        globalBSInitDataResult.setCurrentCity(currentCity);

        //获取公告列表
        List<Notice> noticeList = new ArrayList<Notice>();
        JSONArray noticeJsonArray = globalJsonObject.optJSONArray("newslist");
        for (int i = 0; i < noticeJsonArray.length(); i++) {
            JSONObject noticeJSONObject = noticeJsonArray.optJSONObject(i);
            Notice notice = new Notice();
            notice.setTitle(noticeJSONObject.optString("title"));
            notice.setContent(noticeJSONObject.optString("content"));
            noticeList.add(notice);
        }
        globalBSInitDataResult.setNoticeList(noticeList);

        //获取商圈列表
        List<BusinessArea> businessAreaList = new ArrayList<BusinessArea>();
        JSONArray businessAreaJsonArray = globalJsonObject.optJSONArray("quanlist");
        for (int i = 0; i < businessAreaJsonArray.length(); i++) {
            JSONObject businessAreaJSONObject = businessAreaJsonArray.optJSONObject(i);
            BusinessArea businessArea = new BusinessArea();
            businessArea.setId(businessAreaJSONObject.optInt("id"));
            businessArea.setName(businessAreaJSONObject.optString("name"));
            businessAreaList.add(businessArea);
        }
        globalBSInitDataResult.setBusinessAreaList(businessAreaList);

        //获取城市列表
        Map<Integer, City> cityMap = new HashMap<Integer, City>();
        JSONArray cityJsonArray = globalJsonObject.optJSONArray("citylist");
        for (int i = 0; i < cityJsonArray.length(); i++) {
            JSONObject cityJSONObject = cityJsonArray.optJSONObject(i);
            City city = new City();
            city.setId(cityJSONObject.optInt("id"));
            city.setName(cityJSONObject.optString("name"));
            city.setNamePinYin(cityJSONObject.optString("py"));
            //添加父城市列表
            City parentCity = new City();
            parentCity.setId(cityJSONObject.optInt("pid"));
            city.setParent(parentCity);
            //添加子城市列表
            List<City> childrenCity = new ArrayList<City>();
            city.setChildren(childrenCity);
            cityMap.put(city.getId(), city);
        }
        Set<Integer> cityMapKeySet = cityMap.keySet();
        Iterator<Integer> cityIDIterator = cityMapKeySet.iterator();
        while (cityIDIterator.hasNext()) {
            int cityID = cityIDIterator.next();
            City city = cityMap.get(cityID);
            int parentCityID = city.getParent().getId();
            if (parentCityID != 0) {
                City parentCity = cityMap.get(parentCityID);
                parentCity.getChildren().add(city);
                city.setParent(parentCity);
                cityMap.put(parentCityID, parentCity);
                cityMap.put(cityID, city);
            }
        }
        globalBSInitDataResult.setCityMap(cityMap);

        //获取分类列表
        List<Category> categoryList = new ArrayList<Category>();
        JSONArray categoryJsonArray = globalJsonObject.optJSONArray("cataloglist");
        for (int i = 0; i < categoryJsonArray.length(); i++) {
            JSONObject childrenJsonObject = categoryJsonArray.optJSONObject(i);
            Category childrenCategory = getChildCategory(childrenJsonObject, null);
            categoryList.add(childrenCategory);
        }
        globalBSInitDataResult.setCategoryList(categoryList);
        return globalBSInitDataResult;
    }

    //迭代法获取多层级分类列表
    private Category getChildCategory(JSONObject jsonObject, Category parentCategory) {
        Category category = new Category();
        category.setId(jsonObject.optInt("id"));
        category.setName(jsonObject.optString("name"));
        category.setNamePinyin(jsonObject.optString("py"));
        if (jsonObject.optJSONArray("has_child") != null) {
            JSONArray jsonArray = jsonObject.optJSONArray("has_child");
            List<Category> categoryList = new ArrayList<Category>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject childrenJsonObject = jsonArray.optJSONObject(i);
                Category childrenCategory = getChildCategory(childrenJsonObject, category);
                categoryList.add(childrenCategory);
            }
            category.setChildren(categoryList);
        }
        category.setParent(parentCategory);
        return category;
    }

    public class GlobalBSInitDataResult {

        City currentCity = new City();
        AppInfo appInfo = new AppInfo();
        Map<Integer, City> cityMap = new HashMap<Integer, City>();
        List<Category> categoryList = new ArrayList<Category>();
        List<Notice> noticeList = new ArrayList<Notice>();
        List<BusinessArea> businessAreaList = new ArrayList<BusinessArea>();

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
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
