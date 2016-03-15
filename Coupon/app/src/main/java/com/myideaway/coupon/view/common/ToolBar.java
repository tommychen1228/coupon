package com.myideaway.coupon.view.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.myideaway.coupon.R;

public class ToolBar extends RelativeLayout {
    protected LayoutInflater layoutInflater;
    protected LinearLayout contentLinearLayout;
    protected LinearLayout overlayLinearLayout;

    public ToolBar(Context context) {
        super(context);
        initComponent();
    }

    public ToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }


    public void setContentView(View view) {
        contentLinearLayout.addView(view);
    }


    public void clearContentView() {
        contentLinearLayout.removeAllViews();
    }

    public void setOverlayView(View view) {
        overlayLinearLayout.addView(view);
    }

    public void clearOverlayView() {
        overlayLinearLayout.removeAllViews();
    }

    protected void initComponent() {

        layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.tool_bar, this);
        contentLinearLayout = (LinearLayout) findViewById(R.id.contentLinearLayout);
        overlayLinearLayout = (LinearLayout) findViewById(R.id.overlayLinearLayout);
    }


    public LinearLayout getContentLinearLayout() {
        return contentLinearLayout;
    }

    public LinearLayout getOverlayLinearLayout() {
        return overlayLinearLayout;
    }
}
