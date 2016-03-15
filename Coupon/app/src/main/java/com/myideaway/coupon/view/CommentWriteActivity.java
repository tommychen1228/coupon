package com.myideaway.coupon.view;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.myideaway.coupon.R;
import com.myideaway.coupon.model.ApplicationContext;
import com.myideaway.coupon.model.comment.service.biz.CommentBSSubmitData;
import com.myideaway.coupon.model.user.User;
import com.myideaway.coupon.view.common.BaseActivity;
import com.myideaway.easyapp.core.service.Service;
import roboguice.util.Ln;


/**
 * Created with IntelliJ IDEA.
 * User: duanchang
 * Date: 13-11-23
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class CommentWriteActivity extends BaseActivity {
    private ImageButton backButton;
    private ImageButton submitButton;
    private EditText commentEditText;
    private int couponId;

    @Override
    protected void onCreateMainView() {
        setMainView(R.layout.comment_write);
        showNavigationBar(false);

        navigationBar.setTitle("写评论");

        couponId = getIntent().getIntExtra("couponID", 0);

        commentEditText = (EditText) findViewById(R.id.commentEditText);
        backButton = (ImageButton) layoutInflater.inflate(R.layout.back_button, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        navigationBar.addLeftView(backButton);

        submitButton = (ImageButton) layoutInflater.inflate(R.layout.navigation_bar_image_view, null);
        submitButton.setImageResource(R.drawable.ok);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationContext applicationContext = ApplicationContext.getInstance();
                boolean isSubmit = applicationContext.isLogin(CommentWriteActivity.this, true);
                if (isSubmit) {
                    showLoadingNewDataProgresssDialog();
                    User user = applicationContext.getUser(CommentWriteActivity.this);
                    CommentBSSubmitData commentBSSubmitData = new CommentBSSubmitData(CommentWriteActivity.this);
                    commentBSSubmitData.setUserID(couponId);
                    commentBSSubmitData.setPassword(user.getPassword());
                    commentBSSubmitData.setEmail(user.getEmail());
                    commentBSSubmitData.setContent(commentEditText.getText().toString());
                    commentBSSubmitData.asyncExecute();
                    commentBSSubmitData.setOnSuccessHandler(new Service.OnSuccessHandler() {
                        @Override
                        public void onSuccess(Service service, Object o) {
                            hideProgressDialog();
                            CommentBSSubmitData.CommentBSSubmitDataResult commentBSSubmitDataResult = (CommentBSSubmitData.CommentBSSubmitDataResult) o;
                            if (commentBSSubmitDataResult.isSuccess()) {

                                showToastMessage("评论成功，稍后将显示您的评论");
//                                sendNotification(Notification.WRITE_COUPON_COMMENT_SUCCESS, couponId);
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                showAlertDialog(commentBSSubmitDataResult.getErrorMSG());
                            }


                        }
                    });
                    commentBSSubmitData.setOnFaultHandler(new Service.OnFaultHandler() {
                        @Override
                        public void onFault(Service service, Exception e) {
                            hideProgressDialog();
                            Ln.e(e.getMessage(), e);
                            showExceptionMessage(e);
                        }
                    });

                } else {


                }


            }
        });
        navigationBar.addRightView(submitButton);


    }


}
