package com.myideaway.coupon.view.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.myideaway.coupon.R;
import com.myideaway.easyapp.core.exception.RemoteDataEmptyException;
import com.myideaway.easyapp.core.exception.RemoteServiceException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.facade.Facade;
import org.puremvc.java.multicore.patterns.mediator.Mediator;
import roboguice.util.Ln;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Organization: http://www.myideaway.com
 * User: Tommy Chen
 * Date: 13-2-28
 * Time: AM10:44
 */
public abstract class BaseActivity extends Activity {


    public static final int NO_ICON_IN_DIALOG = 0;

    public static final int TOOLBAR_MODE_FILL = 1;
    public static final int TOOLBAR_MODE_OVERLAP = 2;

    public static final int NAVIGATION_BAR_MODE_FILL = 1;
    public static final int NAVIGATION_BAR_MODE_OVERLAP = 2;

    public static final int TOOLBAR_STATE_SHOW = 1;
    public static final int TOOLBAR_STATE_HIDE = 2;
    public static final int NAVIGATION_BAR_STATE_SHOW = 1;
    public static final int NAVIGATION_BAR_STATE_HIDE = 2;

    public static int BAR_DISAPPEAR_DURATION = 200;

    protected LayoutInflater layoutInflater;
    protected Handler handler;
    protected RelativeLayout rootBackgroundLayout;
    protected RelativeLayout rootLayout;
    protected RelativeLayout mainLayout;
    protected LinearLayout leftShaftLayout;
    protected LinearLayout rightShaftLayout;
    protected ToolBar toolBar;
    protected NavigationBar navigationBar;
    protected TipMessageBar tipMessageBar;
    protected View lockMainViewMask;
    protected ProgressDialog progressDialog;
    protected AlertDialog alertDialog;
    protected Toast toast;
    protected Timer hideTipMessageTimer;
    protected Context context = this;
    protected ImageLoader imageLoader;
    protected DisplayImageOptions headImageDisplayOptions;
    protected DisplayImageOptions normalImageDisplayOptions;

    protected int toolBarMode = TOOLBAR_MODE_FILL;
    protected int navigationBarMode = NAVIGATION_BAR_MODE_FILL;
    private boolean isFullScreen;
    private int toolBarState;
    private int navigationBarState;
    private ActivityMediator activityMediator;
    private boolean instanceStateSaved;
    private boolean toolBarEnabled = true;
    private boolean navigationBarEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.base);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 初始化基本字段
        context = this;

        imageLoader = ImageLoaderFactory.getImageLoader();
        normalImageDisplayOptions = ImageLoaderFactory.generateDefaultDisplayImageOptionsWithStubImage(R.drawable.friends_sends_pictures_no);
        // 注册通知
        registerNotification();

        // 加载根布局
        handler = new Handler();
        layoutInflater = LayoutInflater.from(this);
        ViewGroup view = (ViewGroup) getWindow().getDecorView();

        rootLayout = (RelativeLayout) view.findViewById(R.id.rootRelativeLayout);
        rootBackgroundLayout = (RelativeLayout) view.findViewById(R.id.rootLayoutBackground);

        mainLayout = (RelativeLayout) rootLayout.findViewById(R.id.mainRelativeLayout);

        // 加载导航栏
        navigationBar = (NavigationBar) rootLayout.findViewById(R.id.navigationBar);
        navigationBar.setVisibility(View.GONE);
        // 加载工具栏
        toolBar = (ToolBar) rootLayout.findViewById(R.id.toolBar);
        toolBarState = TOOLBAR_STATE_SHOW;

        // 加载提示栏
        tipMessageBar = (TipMessageBar) rootLayout.findViewById(R.id.tipMessageBar);

        // 加载主视图锁定窗口
        lockMainViewMask = view.findViewById(R.id.lockMainViewMask);
        lockMainViewMask.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // 创建进度等待对话框
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        // 创建Toast对话框
        toast = Toast.makeText(context, null, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);

        // 创建主试图
        createMainView();


        // 将工具栏移动到最上层。
        toolBar.bringToFront();
        // 讲导航栏移动到最上层
        navigationBar.bringToFront();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        instanceStateSaved = true;
    }


    @Override
    protected void onDestroy() {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        if (alertDialog != null) {
            alertDialog.dismiss();
        }

        // 取消监听消息通知
        unregisterNotification();
        // 停止ImageLoader下载
        if (!instanceStateSaved && imageLoader != null) {
            Ln.d("Stop image loader");
            imageLoader.stop();
        }


        super.onDestroy();
    }


    private void registerNotification() {
        if (activityMediator == null) {
            activityMediator = new ActivityMediator();
        }

        Facade.getInstance("main").registerMediator(activityMediator);
        Ln.d("Register notification for " + getNotificationObserverName());
    }

    private void unregisterNotification() {
        if (activityMediator != null) {
            Facade.getInstance("main").removeMediator(getNotificationObserverName());
            Ln.d("Unregister notification for " + getNotificationObserverName());
        }

    }


    public void setShowImageState(int showImageState) {
        SharedPreferences.Editor editor =
                context.getSharedPreferences("ShowImageState", 0).edit();
        editor.putInt("show_image_state", showImageState);
        editor.commit();
    }

    public int getShowImageState() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ShowImageState", 0);
        return sharedPreferences.getInt("show_image_state", 0);
    }

    protected void setMainView(int resId) {

        mainLayout.removeAllViews();
        layoutInflater.inflate(resId, mainLayout);
    }

    protected void setMainView(View view) {
        mainLayout.removeAllViews();
        mainLayout.addView(view);
    }


    protected void createMainView() {

        hideNavigationBar(false);
        hideToolBar(false);

        onCreateMainView();

        // 加载自定义导航栏
        View customNavigationBar = rootLayout.findViewById(R.id.customNavigationBar);
        if (customNavigationBar != null) {
            // 先将原来的自定义工具栏从父控件移除
            Method parentRemoveChildMethod;
            try {
                parentRemoveChildMethod = customNavigationBar.getParent().getClass()
                        .getMethod("removeView", View.class);
                parentRemoveChildMethod.invoke(customNavigationBar.getParent(),
                        customNavigationBar);
            } catch (Exception e) {
                Ln.e("Can't add custom navigation bar", e);
            }

            // 将自定义导航栏添加到原有导航栏中
            navigationBar.setContentView(customNavigationBar);

        }


        // 加载自定义工具栏
        View customToolBar = rootLayout.findViewById(R.id.customToolBar);
        if (customToolBar != null) {
            // 先将原来的自定义工具栏从父控件移除
            Method parentRemoveChildMethod;
            try {
                parentRemoveChildMethod = customToolBar.getParent().getClass()
                        .getMethod("removeView", View.class);
                parentRemoveChildMethod.invoke(customToolBar.getParent(),
                        customToolBar);
            } catch (Exception e) {
                Ln.e("Can't add custom too bar", e);
            }

            // 将自定义工具栏添加到原有工具栏中
            toolBar.setContentView(customToolBar);

        }

        //加载自定义工具栏覆盖物
        View customToolBarOverlay = rootLayout.findViewById(R.id.customToolBarOverlay);
        if (customToolBarOverlay != null) {
            // 先将原来的自定义工具栏从父控件移除
            Method parentRemoveChildMethod;
            try {
                parentRemoveChildMethod = customToolBarOverlay.getParent().getClass()
                        .getMethod("removeView", View.class);
                parentRemoveChildMethod.invoke(customToolBarOverlay.getParent(),
                        customToolBarOverlay);
            } catch (Exception e) {
                Ln.e("Can't add custom too bar overlay", e);
            }

            // 将自定义工具栏覆盖物添加到原有工具栏中
            toolBar.setOverlayView(customToolBarOverlay);
        }

        //需要将rootLayout的尺寸改为和mainLayout中的view一样
        View childView = mainLayout.getChildAt(0);
        ViewGroup.LayoutParams rootLayoutParams = rootLayout.getLayoutParams();
        ViewGroup.LayoutParams childLayoutParams = childView.getLayoutParams();
        rootLayoutParams.width = childLayoutParams.width;
        rootLayoutParams.height = childLayoutParams.height;
        rootLayout.setLayoutParams(rootLayoutParams);
    }

    public void setLocale(Locale newLocale) {

        // 更新默认语言
        Locale.setDefault(newLocale);

        Configuration config = getBaseContext().getResources()
                .getConfiguration();

        config.locale = newLocale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public Locale getLocale() {
        return Locale.getDefault();
    }

    public void showProgressDialog(String message) {
        if (message != null) {
            progressDialog.setMessage(message);
        }

        progressDialog.show();

    }


    public void showLoadingNewDataProgresssDialog() {
        showProgressDialog(getString(R.string.loading_new_data));
    }

    public void showRefreshingProgressDialog() {
        showProgressDialog(getString(R.string.refreshing));
    }


    public void showProgressDialog(String message, boolean cancel) {
        if (message != null) {
            progressDialog.setMessage(message);
            progressDialog.setCancelable(cancel);
        }


        progressDialog.show();

    }

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    public void showAlertDialog(String message,
                                final DialogInterface.OnClickListener okListener,
                                final DialogInterface.OnClickListener noListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setTitle(null);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (okListener != null) {
                    okListener.onClick(dialog, which);
                }
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (noListener != null) {
                    noListener.onClick(dialog, which);
                }
            }

        });

        alertDialog = builder.show();
    }


    public void showAlertDialog(String message, String okText, String cancelText,
                                final DialogInterface.OnClickListener okListener,
                                final DialogInterface.OnClickListener noListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setTitle(null);
        builder.setPositiveButton(okText, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (okListener != null) {
                    okListener.onClick(dialog, which);
                }
            }
        });

        builder.setNegativeButton(cancelText, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (noListener != null) {
                    noListener.onClick(dialog, which);
                }
            }

        });

        alertDialog = builder.show();
    }


    public void showAlertDialog(String message,
                                final DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setTitle(null);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (okListener != null) {
                    okListener.onClick(dialog, which);
                }
            }
        });

        alertDialog = builder.show();
    }

    public void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setTitle(null);
        builder.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog = builder.show();
    }

    public void showToastMessage(String message) {
        toast.setText(message);
        toast.show();
    }

    public void showToastMessage(String message, int gravity, int xOffset, int yOffset) {
        toast.setText(message);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.show();
    }

    public void hideToastMessage() {
        toast.cancel();
    }



    public void showNoDataToast() {
        showToastMessage(getString(R.string.no_data));
    }

    public void showNoMoreDataToast() {
        showToastMessage(getString(R.string.no_more_data));
    }
    //todo showNoDataToast


    public void showTipMessage(String message, int type) {
        stopHideTipMessageTimer();

        tipMessageBar.setTipIconType(type);
        tipMessageBar.setMessage(message);

        if (tipMessageBar.getVisibility() == View.VISIBLE) {
            return;
        }

        tipMessageBar.setVisibility(View.VISIBLE);

        TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, -1.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(BAR_DISAPPEAR_DURATION);
        tipMessageBar.startAnimation(animation);
    }

    public void showTipMessageAndHide(String message, int type, int delay) {
        showTipMessage(message, type);
        hideTipMessage(delay);
    }

    public void showTipMessageAndHide(String message, int type) {
        showTipMessage(message, type);
        hideTipMessage(3000);
    }

    public void showLoadingNewDataTipMessage() {
        showTipMessage(getString(R.string.loading_new_data),
                TipMessageBar.TIP_ICON_TYPE_PROGRESS);
    }

    public void showRefreshingTipMessage() {
        showTipMessage(getString(R.string.refreshing),
                TipMessageBar.TIP_ICON_TYPE_PROGRESS);
    }

    public void showFaultTipMessageAndHide(String msg) {
        showTipMessageAndHide(msg, TipMessageBar.TIP_ICON_TYPE_FAULT);
    }

    public void showSuccessTipMessageAndHide(String msg) {
        showTipMessageAndHide(msg, TipMessageBar.TIP_ICON_TYPE_SUCCESS);
    }

    public void hideTipMessage() {

        if (tipMessageBar.getVisibility() == View.GONE) {
            return;
        }

        TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, -1.0f);
        animation.setDuration(BAR_DISAPPEAR_DURATION);
        tipMessageBar.startAnimation(animation);
        tipMessageBar.setVisibility(View.GONE);
    }

    public void hideTipMessage(int delay) {

        if (tipMessageBar.getVisibility() == View.GONE) {
            return;
        }

        startHideTipMessageTimer(delay);
    }

    public void startHideTipMessageTimer(int delay) {
        stopHideTipMessageTimer();

        hideTipMessageTimer = new Timer();
        hideTipMessageTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {

                    public void run() {
                        hideTipMessage();
                    }
                });

            }
        }, delay);
    }

    public void stopHideTipMessageTimer() {
        if (hideTipMessageTimer != null) {
            hideTipMessageTimer.cancel();
        }
    }


    public void showToolBar(boolean animated, int offset) {
        if (toolBarState == TOOLBAR_STATE_SHOW) {
            return;
        }

        toolBarState = TOOLBAR_STATE_SHOW;

        if (!animated) {
            toolBar.getContentLinearLayout().setVisibility(View.VISIBLE);
            return;

        }


        TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 1.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(BAR_DISAPPEAR_DURATION);

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                toolBar.getContentLinearLayout().setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toolBar.clearAnimation();

            }
        });

        toolBar.startAnimation(animation);

    }

    public void enableToolBar() {
        toolBarEnabled = true;
    }

    public void disableToolBar() {
        toolBarEnabled = false;
    }

    public void showToolBar(boolean animated) {
        if (!toolBarEnabled) {
            return;
        }

        showToolBar(animated, toolBar.getContentLinearLayout().getMeasuredHeight());
    }

    public void hideToolBar(boolean animated, int offset) {
        if (toolBarState == TOOLBAR_STATE_HIDE) {
            return;
        }

        toolBarState = TOOLBAR_STATE_HIDE;

        if (!animated) {
            toolBar.getContentLinearLayout().setVisibility(View.GONE);
            return;
        }


        TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 1.0f);
        animation.setDuration(BAR_DISAPPEAR_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toolBar.getContentLinearLayout().setVisibility(View.GONE);
                toolBar.clearAnimation();

            }
        });

        toolBar.startAnimation(animation);
    }

    public void hideToolBar(boolean animated) {
        hideToolBar(animated, toolBar.getMeasuredHeight());
    }

    public void hideNavigationBar(boolean animated) {

        if (navigationBarState == NAVIGATION_BAR_STATE_HIDE) {
            return;
        }

        navigationBarState = NAVIGATION_BAR_STATE_HIDE;

        if (!animated) {
            navigationBar.setVisibility(View.GONE);
            return;
        }


        TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, -1.0f);
        animation.setDuration(BAR_DISAPPEAR_DURATION);

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                navigationBar.setVisibility(View.GONE);
                navigationBar.clearAnimation();

            }
        });

        navigationBar.startAnimation(animation);
    }

    public void showNavigationBar(boolean animated) {

        if (navigationBarState == NAVIGATION_BAR_STATE_SHOW) {
            return;
        }

        navigationBarState = NAVIGATION_BAR_STATE_SHOW;

        if (!animated) {
            navigationBar.setVisibility(View.VISIBLE);
            return;
        }

        TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, -1.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(BAR_DISAPPEAR_DURATION);

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                navigationBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {

                navigationBar.clearAnimation();
            }
        });

        navigationBar.startAnimation(animation);
    }

    public void hideStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void showStatusBar() {
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    public void showExceptionMessage(Exception ex) {
        Ln.e(ex.getMessage(), ex);
        showExceptionMessage(ex, "");
    }

    public void showExceptionMessage(Exception ex, String addition) {
        if (ex instanceof RemoteDataEmptyException) {
            showFaultTipMessageAndHide(getString(R.string.occur_server_data_error) + addition);
        } else if (ex instanceof RemoteServiceException) {
            showFaultTipMessageAndHide(getString(R.string.occur_network_error) + addition);
        } else {
            showFaultTipMessageAndHide(getString(R.string.occur_unknow_error) + addition);
        }
    }

    /**
     * 全屏
     */
    public void fullScreen() {
        isFullScreen = true;
        hideStatusBar();
        hideNavigationBar(true);
        hideToolBar(true);
    }

    /**
     * 退出全屏
     */
    public void quitFullScreen() {
        isFullScreen = false;
        showStatusBar();
        showNavigationBar(true);
        showToolBar(true);
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }


    public void exitApp() {
        System.exit(0);
        // ScreenManager.getInstance().popAllActivity();
    }




    public float getDensity() {
        return getResources().getDisplayMetrics().density;
    }


    protected abstract void onCreateMainView();

    private String getNotificationObserverName() {
        return this.toString() + "_" + this.hashCode();
    }

    protected void processNotifications(String name, Object param) {

    }

    protected String[] observeNotifications() {
        return new String[]{};
    }

    protected void sendNotification(String name, Object param) {
        Facade.getInstance("main").sendNotification(name, param);
    }

    class ActivityMediator extends Mediator {

        public ActivityMediator() {
            super(getNotificationObserverName(), null);
        }

        @Override
        public void handleNotification(final INotification iNotification) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    processNotifications(iNotification.getName(), iNotification.getBody());
                }
            });

        }

        @Override
        public String[] listNotificationInterests() {
            return observeNotifications();
        }
    }

    //umeng
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);


    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }
}
