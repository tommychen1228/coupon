package com.myideaway.coupon;

import android.content.Context;
import android.content.res.Resources;

/**
 * Organization: http://www.myideaway.com
 * User: Tommy Chen
 * Date: 13-4-19
 * Time: PM9:02
 */
public class Config {
    public static String PATH_CACHE;
    public static String PATH_CACHE_IMAGES;
    public static String URL_SERVER;
    public static void init(Context context){
        Resources res = context.getResources();


        try {
            PATH_CACHE = context.getExternalCacheDir().getPath();
        } catch (Exception e) {
            PATH_CACHE = context.getCacheDir().getPath();
        }

        PATH_CACHE_IMAGES = PATH_CACHE + res.getString(R.string.app_config_path_cache_images);

        URL_SERVER = res.getString(R.string.app_config_uri_server);

    }
}
