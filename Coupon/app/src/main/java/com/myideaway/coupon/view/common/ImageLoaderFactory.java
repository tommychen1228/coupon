package com.myideaway.coupon.view.common;

import android.content.Context;
import android.graphics.Bitmap;
import com.myideaway.coupon.Config;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

/**
 * Organization: http://www.myideaway.com
 * User: Tommy Chen
 * Date: 12/12/12
 * Time: 7:40 PM
 */
public class ImageLoaderFactory {
    private static final int CACHE_SIZE = 128 * 1024 * 1024;
    private static final int TIME_OUT = 60 * 1000;
    private static ImageLoader imageLoader;
    private static ImageLoaderConfiguration imageLoaderConfiguration;
    private static DiskCache diskCache;
    private static Context context;

    public static void init(Context ctx) {
        context = ctx;
        initImageLoader();
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    private static void initImageLoader() {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
            initImageLoaderConfiguration();
            imageLoader.init(imageLoaderConfiguration);
        }
    }

    private static void initImageLoaderConfiguration() {

        if (imageLoaderConfiguration == null) {
            DisplayImageOptions defaultOptions = generateDefaultDisplayImageOptions();

            initDiscCacheAware();

            imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context)
                    .threadPoolSize(3)
                    .memoryCache(new WeakMemoryCache())
                    .diskCache(diskCache) // default
                    .diskCacheSize(CACHE_SIZE)
                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                    .imageDownloader(new BaseImageDownloader(context, TIME_OUT, TIME_OUT)) // connectTimeout (5 s), readTimeout (20 s)
                    .defaultDisplayImageOptions(defaultOptions)
                    .build();
        }
    }

    private static void initDiscCacheAware() {
        if (diskCache == null) {
            diskCache = new UnlimitedDiscCache(new File(Config.PATH_CACHE_IMAGES));
        }
    }

    public static File getDiscCacheImageFile(String url) {
        return diskCache.get(url);
    }


    public static DisplayImageOptions generateDefaultDisplayImageOptions() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        return defaultOptions;
    }

    public static DisplayImageOptions generateDefaultDisplayImageOptionsWithStubImage(int stubImageRes) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(stubImageRes)
                .showImageForEmptyUri(stubImageRes)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        return defaultOptions;
    }
}
