package com.myideaway.coupon.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.area.City;
import com.myideaway.coupon.view.common.BaseActivity;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class SelectCityActivity extends BaseActivity {
    ApplicationContext applicationContext = ApplicationContext.getInstance();
    private ImageButton backButton;
    private ListView cityCategoryListView;
    private Map<Integer, City> cityMap;
    private List<City> cityList = new ArrayList<City>();

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.city_category);
        showNavigationBar(false);
        navigationBar.setTitle(applicationContext.getCurrentCity().getName());

        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navigationBar.addLeftView(backButton);

//        City city = applicationContext.getCityMap().get(Config.DEFAULT_CITY_ID);
//        City city = applicationContext.getCurrentCity();
//        cityMap = applicationContext.getCityMap();
        //* cityList.add(city);
//        cityList.addAll(cityMap.get(Config.DEFAULT_CITY_ID).getChildren());
        //* cityList.addAll(cityMap.get(applicationContext.getCurrentCity().getId()).getChildren());

        Map<Integer, City> cityMap = applicationContext.getCityMap();
        Set<Integer> cityMapKeySet = cityMap.keySet();
        Iterator<Integer> cityIDIterator = cityMapKeySet.iterator();
        City countryCity = new City();
        List<City> cityListTemp = new ArrayList<City>();
        while (cityIDIterator.hasNext()) {
            int cityID = cityIDIterator.next();
            City city = cityMap.get(cityID);
            if(city.getName().equals("全国")){
                countryCity = city;
            }else {
                cityListTemp.add(city);
            }
        }
        cityList.add(countryCity);
        cityList.addAll(cityListTemp);
        //cityList.addAll(cityMap.get(applicationContext.getCurrentCity().getId()).getChildren());
        cityCategoryListView = (ListView) findViewById(R.id.cityCategoryListView);
        cityCategoryListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return cityList.size();
            }

            @Override
            public Object getItem(int i) {
                return cityList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                final City city = (City) getItem(i);
                if (view == null) {
                    LayoutInflater layoutInflater = getLayoutInflater();
                    view = layoutInflater.inflate(R.layout.city_category_format, viewGroup, false);
                }

                TextView couponTitleTextView = (TextView) view.findViewById(R.id.cityNameTextView);
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.couponFormatLinearLayout);
                couponTitleTextView.setText(city.getName());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        applicationContext.setCurrentCity(city);
//                        sendNotification(Notification.SELECT_CITY_SUCCESS, city.getId());
                        setResult(RESULT_OK);
                        finish();

                    }
                });

                if (i == 0) {
                    linearLayout.setBackgroundResource(R.drawable.more_top_item_bg);
                }
                if (i == cityList.size() - 1) {
                    linearLayout.setBackgroundResource(R.drawable.more_bottom_item_bg);
                }
                return view;
            }
        });

    }


}
