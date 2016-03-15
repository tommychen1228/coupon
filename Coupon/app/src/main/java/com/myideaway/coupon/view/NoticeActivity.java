package com.myideaway.coupon.view;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import com.myideaway.coupon.R;
import com.myideaway.coupon.view.common.BaseActivity;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class NoticeActivity extends BaseActivity {
    private ImageButton backButton;
    private WebView noticeWebView;
    private String noticeHTML;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.notice);
        showNavigationBar(false);

        navigationBar.setTitle("公告");

        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
        navigationBar.addLeftView(backButton);

        noticeHTML = getIntent().getStringExtra("noticeHTML");

        noticeWebView = (WebView) findViewById(R.id.noticeWebView);

        noticeWebView.loadDataWithBaseURL(null, noticeHTML, "text/html", "utf-8", null);
//        try {
//
//        } catch (UnsupportedEncodingException e) {
//            Ln.e("URLEncoder encode error",e);
//
//        }

    }
}
