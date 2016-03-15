package com.myideaway.coupon.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.comment.Comment;
import com.myideaway.coupon.model.comment.service.biz.CommentBSGetListData;
import com.myideaway.coupon.model.coupon.Coupon;
import com.myideaway.coupon.view.common.BaseActivity;
import com.myideaway.easyapp.core.service.Service;
import roboguice.util.Ln;


import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class CommentListActivity extends BaseActivity {
    public static final int REQUEST_CODE_WRITE_COMMENT = 1;
    private ImageButton backButton;
    private ImageButton commentImageButton;
    private Coupon coupon;
    private List<Comment> commentList = new ArrayList<Comment>();
    private PullToRefreshListView commentListView;
    private CommentAdapter commentAdapter = new CommentAdapter();
    private int page = 1;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.comment_list);
        showNavigationBar(false);

        navigationBar.setTitle("用户评论");
        coupon = new Coupon();

        coupon.setId(getIntent().getIntExtra("couponID", 0));


        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        navigationBar.addLeftView(backButton);

        commentImageButton = (ImageButton) layoutInflater.inflate(R.layout.navigation_bar_image_view, null);
        commentImageButton.setImageResource(R.drawable.chat);

        commentImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationContext applicationContext = ApplicationContext.getInstance();
                boolean isSubmit = applicationContext.isLogin(CommentListActivity.this, true);
                if (isSubmit) {
                    Intent intent = new Intent(CommentListActivity.this, CommentWriteActivity.class);
                    intent.putExtra("couponID", coupon.getId());
                    startActivityForResult(intent, REQUEST_CODE_WRITE_COMMENT);
                }


            }
        });
        navigationBar.addRightView(commentImageButton);


        commentListView = (PullToRefreshListView) findViewById(R.id.commentListView);
        commentListView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        commentListView.getLoadingLayoutProxy(false, true).setPullLabel("正在加载");
        commentListView.setAdapter(commentAdapter);
        commentListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                createCommentList(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                lanfMoreList(++page);
            }
        });
        createCommentList(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_WRITE_COMMENT) {
            if (resultCode == RESULT_OK) {
                createCommentList(true);
            }
        }
    }

    private void createCommentList(final boolean isLock) {
        if (isLock) {
            showLoadingNewDataProgresssDialog();
        }
        page = 1;
        CommentBSGetListData commentBSGetListData = new CommentBSGetListData(this);
        commentBSGetListData.setCouponID(coupon.getId());
        commentBSGetListData.setPage(page);
        commentBSGetListData.asyncExecute();
        commentBSGetListData.setOnSuccessHandler(new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                CommentBSGetListData.CommentBSGetListDataResult commentBSGetListDataResult = (CommentBSGetListData.CommentBSGetListDataResult) o;

                commentList = commentBSGetListDataResult.getCommentList();
                if (commentList.size() == 0) {
                    showNoDataToast();
                }
                commentListView.setAdapter(commentAdapter);


                commentListView.onRefreshComplete();
                if (isLock) {
                    hideProgressDialog();
                }
            }
        });
        commentBSGetListData.setOnFaultHandler(new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                commentListView.onRefreshComplete();
                Ln.e(e.getMessage(), e);
                showExceptionMessage(e);
                if (isLock) {
                    hideProgressDialog();
                }
            }
        });


    }

    private void lanfMoreList(int page) {

        CommentBSGetListData commentBSGetListData = new CommentBSGetListData(this);
        commentBSGetListData.setCouponID(coupon.getId());
        commentBSGetListData.setPage(page);
        commentBSGetListData.asyncExecute();
        commentBSGetListData.setOnSuccessHandler(new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                CommentBSGetListData.CommentBSGetListDataResult commentBSGetListDataResult = (CommentBSGetListData.CommentBSGetListDataResult) o;
                commentList.addAll(commentBSGetListDataResult.getCommentList());
                commentAdapter.notifyDataSetChanged();
                commentListView.onRefreshComplete();

                if (CommentListActivity.this.page > Integer.parseInt(commentBSGetListDataResult.getPageTotal())) {
                    showNoMoreDataToast();
                    CommentListActivity.this.page--;
                }
            }
        });
        commentBSGetListData.setOnFaultHandler(new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                CommentListActivity.this.page--;
                commentListView.onRefreshComplete();
                Ln.e(e.getMessage(), e);
                showExceptionMessage(e);
            }
        });


    }

    //创建优惠卷列表

    private class CommentAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return commentList.size();  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getItem(int position) {
            return commentList.get(position);  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getItemId(int position) {
            return position;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public View getView(final int position, View couponView, ViewGroup parent) {
            Comment comment = (Comment) getItem(position);

            if (couponView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();

                couponView = layoutInflater.inflate(R.layout.comment_format, parent, false);
            }
            TextView userNameTextView = (TextView) couponView.findViewById(R.id.userNameTextView);
            TextView contentTextView = (TextView) couponView.findViewById(R.id.contentTextView);
            TextView dateTextView = (TextView) couponView.findViewById(R.id.dateTextView);

            userNameTextView.setText(" " + comment.getWriter().getUsername());
            contentTextView.setText(comment.getContent());

            dateTextView.setText(comment.getCreateTimeString());

            couponView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });


            return couponView;  //To change body of implemented methods use File | Settings | File Templates.
        }

    }

}
