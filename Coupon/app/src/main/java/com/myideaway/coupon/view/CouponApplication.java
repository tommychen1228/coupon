package com.myideaway.coupon.view;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.myideaway.coupon.Config;
import com.myideaway.coupon.R;
import com.myideaway.coupon.view.common.ImageLoaderFactory;
import roboguice.util.Ln;


/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-26
 * Time: 下午5:30
 * To change this template use File | Settings | File Templates.
 */
public class CouponApplication extends Application {

    private BMapManager mBMapManager = null;

    @Override
    public void onCreate() {
        super.onCreate();

//        Ln.setLoggingLevel(5);

        Config.init(getApplicationContext());
        ImageLoaderFactory.init(getApplicationContext());
        initEngineManager(getApplicationContext());

        Ln.d("Application create");
    }

    public void initEngineManager(Context context) {

        String baiduMapKey = context.getString(R.string.app_config_baidumap_key);

        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(baiduMapKey, null)) {
            Toast.makeText(getApplicationContext(),
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
    }
}






