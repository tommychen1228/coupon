package com.myideaway.coupon.view.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.myideaway.coupon.R;

import java.util.ArrayList;

public class NavigationBar extends RelativeLayout {
    protected View rootView;
    protected LayoutInflater layoutInflater;
    protected LinearLayout leftLinearLayout;
    protected LinearLayout centerLinearlayout;
    protected LinearLayout rightLinearLayout;
    protected ArrayList<View> leftViews;
    protected ArrayList<View> rightViews;
    protected TextView titleTextView;
    private boolean isUseContentView;
    private Context context;

    public NavigationBar(Context ctx) {
        super(ctx);
        context = ctx;
        initComponent();
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initComponent();
    }

    public void addRightView(View view) {
        rightLinearLayout.addView(view);
        view.getLayoutParams();

        final float density = context.getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);  // , 1是可选写的
        lp.setMargins(0, 0, (int) (10 * density), 0);

        view.setLayoutParams(lp);
    }

    public void removeRightView(View view) {
        rightLinearLayout.removeView(view);
    }

    public void clearRightViews() {
        rightLinearLayout.removeAllViews();
    }

    public void addLeftView(View view) {
        leftLinearLayout.addView(view);
    }

    public void removeLeftView(View view) {
        leftLinearLayout.removeView(view);
    }

    public void clearLeftViews() {
        leftLinearLayout.removeAllViews();
    }

    public int getRightViewsCount() {
        return rightLinearLayout.getChildCount();
    }

    public int getLeftViewsCount() {
        return leftLinearLayout.getChildCount();
    }

    protected void initComponent() {
        leftViews = new ArrayList<View>();
        rightViews = new ArrayList<View>();

        layoutInflater = LayoutInflater.from(getContext());
        rootView = layoutInflater.inflate(R.layout.navigation_bar, null);
        addView(rootView);

        leftLinearLayout = (LinearLayout) findViewById(R.id.leftLinearLayout);
        centerLinearlayout = (LinearLayout) findViewById(R.id.contentLinearLayout);
        rightLinearLayout = (LinearLayout) findViewById(R.id.rightLinearLayout);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
    }


    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setTitleColor(int color) {
        titleTextView.setTextColor(color);
    }

    public void setContentView(View view) {
        centerLinearlayout.removeAllViews();
        centerLinearlayout.addView(view);

        isUseContentView = true;
    }

    public boolean isUseContentView() {
        return isUseContentView;
    }

    @Override
    public void setBackgroundColor(int color) {
        rootView.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resid) {
        rootView.setBackgroundResource(resid);
    }

    @Override
    public void setBackgroundDrawable(Drawable d) {
        rootView.setBackgroundDrawable(d);
    }
}
