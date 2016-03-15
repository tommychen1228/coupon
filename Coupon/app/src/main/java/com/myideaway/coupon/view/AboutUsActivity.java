package com.myideaway.coupon.view;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.view.common.BaseActivity;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class AboutUsActivity extends BaseActivity {
    private ImageButton backButton;
    private WebView aboutWebView;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.about_us);
        showNavigationBar(false);

        navigationBar.setTitle("关于我们");

        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        navigationBar.addLeftView(backButton);


        aboutWebView = (WebView) findViewById(R.id.aboutWebView);
         String aboutHtml = ApplicationContext.getInstance().getAppInfo().getAboutInfo();
        aboutWebView.loadDataWithBaseURL(null, aboutHtml, "text/html", "utf-8", null);
    }
}
