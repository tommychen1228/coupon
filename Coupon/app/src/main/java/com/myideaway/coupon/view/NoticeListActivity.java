package com.myideaway.coupon.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.notice.Notice;
import com.myideaway.coupon.view.common.BaseActivity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class NoticeListActivity extends BaseActivity {
    private ImageButton backButton;
    private List<Notice> noticeList;
    private ListView noticeListView;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.notice_list);
        showNavigationBar(false);

        navigationBar.setTitle("公告列表");

        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        navigationBar.addLeftView(backButton);

        noticeListView();
    }

    //创建优惠卷列表
    private void noticeListView() {
        noticeList = ApplicationContext.getInstance().getNoticeList();

        noticeListView = (ListView) findViewById(R.id.noticeListView);
        noticeListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return noticeList.size();  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Object getItem(int position) {
                return noticeList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public long getItemId(int position) {
                return position;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public View getView(final int position, View noticeView, ViewGroup parent) {

                final Notice notice = (Notice) getItem(position);
                if (noticeView == null) {
                    LayoutInflater layoutInflater = getLayoutInflater();
                    noticeView = layoutInflater.inflate(R.layout.notice_format, parent, false);
                }
                TextView noticeTextView = (TextView) noticeView.findViewById(R.id.noticeTextView);
                noticeTextView.setText(notice.getTitle());
                noticeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(NoticeListActivity.this, NoticeActivity.class);
                        intent.putExtra("noticeHTML", notice.getContent());
                        startActivity(intent);
                    }
                });


                return noticeView;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });


    }
}
