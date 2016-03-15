package com.myideaway.coupon.model.global.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.action.Action;
import com.myideaway.coupon.model.advertisment.Advertisment;
import com.myideaway.coupon.model.global.service.remote.GlobalRSInitHomePageData;
import com.myideaway.coupon.model.menu.Menu;
import com.myideaway.easyapp.core.service.BizService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class GlobalBSInitHomePageData extends BizService {

    public GlobalBSInitHomePageData(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        GlobalRSInitHomePageData globalRSInitHomePageData = new GlobalRSInitHomePageData();
        JSONObject globalHomePageJsonObject = (JSONObject) globalRSInitHomePageData.syncExecute();
        GlobalBSInitHomePageDataResult globalBSInitHomePageDataResult = new GlobalBSInitHomePageDataResult();

        //获取图片广告列表
        List<Advertisment> advertismentList = new ArrayList<Advertisment>();
        JSONArray advertismentJsonArray = globalHomePageJsonObject.optJSONArray("advs");
        for (int i = 0; i < advertismentJsonArray.length(); i++) {
            JSONObject advertismentJSONObject = advertismentJsonArray.optJSONObject(i);
            Advertisment advertisment = new Advertisment();
            advertisment.setId(advertismentJSONObject.optInt("id"));
            advertisment.setName(advertismentJSONObject.optString("name"));
            advertisment.setImageUrl(advertismentJSONObject.optString("img"));
            Action action = Action.generateAction(advertismentJSONObject.optInt("type"), advertismentJSONObject.optJSONObject("data"));
            advertisment.setAction(action);
            advertismentList.add(advertisment);
        }
        globalBSInitHomePageDataResult.setAdvertismentList(advertismentList);

        //获取菜单列表
        List<Menu> menuList = new ArrayList<Menu>();
        JSONArray menuJsonArray = globalHomePageJsonObject.optJSONArray("indexs");
        for (int i = 0; i < menuJsonArray.length(); i++) {
            JSONObject menuJSONObject = menuJsonArray.optJSONObject(i);
            Menu menu = new Menu();
            menu.setId(menuJSONObject.optInt("id"));
            menu.setName(menuJSONObject.optString("name"));
            menu.setSubtitle(menuJSONObject.optString("vice_name"));
            menu.setDescription(menuJSONObject.optString("desc"));
            menu.setImageUrl(menuJSONObject.optString("img"));
            menu.setHot(intToBoolean(menuJSONObject.optInt("is_hot")));
            menu.setNew(intToBoolean(menuJSONObject.optInt("is_new")));
            Action action = Action.generateAction(menuJSONObject.optInt("type"), menuJSONObject.optJSONObject("data"));
            menu.setAction(action);
            menuList.add(menu);
        }
        globalBSInitHomePageDataResult.setMenuList(menuList);

        return globalBSInitHomePageDataResult;
    }

    private boolean intToBoolean(int num) {
        if (num == 0) {
            return false;
        } else {
            return true;
        }

    }

    public class GlobalBSInitHomePageDataResult {
        List<Advertisment> advertismentList = new ArrayList<Advertisment>();
        List<Menu> menuList = new ArrayList<Menu>();

        public List<Advertisment> getAdvertismentList() {
            return advertismentList;
        }

        public void setAdvertismentList(List<Advertisment> advertismentList) {
            this.advertismentList = advertismentList;
        }

        public List<Menu> getMenuList() {
            return menuList;
        }

        public void setMenuList(List<Menu> menuList) {
            this.menuList = menuList;
        }
    }
}
