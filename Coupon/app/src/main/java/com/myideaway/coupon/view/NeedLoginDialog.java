package com.myideaway.coupon.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by duanchang on 13-12-17.
 */
public class NeedLoginDialog {
    private Context context;
    private AlertDialog dialog;

    public NeedLoginDialog(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("现在需要登陆吗？");
        builder.setPositiveButton("登陆", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });
        builder.setNeutralButton("注册", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, RegisterActivity.class);
                context.startActivity(intent);
            }

        });
        dialog = builder.create();
    }

    public void show() {
        dialog.show();
    }
}
