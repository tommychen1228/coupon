package com.myideaway.coupon.model.comment.service.biz;

import android.content.Context;
import com.myideaway.coupon.model.comment.Comment;
import com.myideaway.coupon.model.comment.service.remote.CommentRSGetListData;
import com.myideaway.coupon.model.user.User;
import com.myideaway.easyapp.core.service.BizService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-2
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class CommentBSGetListData extends BizService {
    private int couponID;
    private int page;

    public CommentBSGetListData(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        CommentRSGetListData commentRSGetListData = new CommentRSGetListData();
        CommentBSGetListDataResult commentBSGetListDataResult = new CommentBSGetListDataResult();
        commentRSGetListData.setPage(page);
        commentRSGetListData.setCouponID(couponID);

        JSONObject  commentJsonObject = (JSONObject) commentRSGetListData.syncExecute();


        JSONArray commentListJSONArray = commentJsonObject.optJSONArray("item");
        List<Comment> commentList = new ArrayList<Comment>();
        for (int i = 0; i < commentListJSONArray.length(); i++) {
            Comment comment = new Comment();
            JSONObject commentJSONObject = commentListJSONArray.optJSONObject(i);
            comment.setId(commentJSONObject.optInt("id"));
            User user = new User();
            user.setUsername(commentJSONObject.optString("user_name"));
            comment.setWriter(user);
            Date date = new Date(commentJSONObject.optInt("create_time"));
            comment.setCreateTime(date);
            comment.setContent(commentJSONObject.optString("content"));
            comment.setCreateTimeString(commentJSONObject.optString("create_time_format"));

            commentList.add(comment);
        }
        commentBSGetListDataResult.setCommentList(commentList);

        String page = commentJsonObject.optJSONObject("page").optString("page");
        commentBSGetListDataResult.setPage(page);
        String pageTotal = commentJsonObject.optJSONObject("page").optString("page_total");
        commentBSGetListDataResult.setPageTotal(pageTotal);


        return commentBSGetListDataResult;
    }

    public class CommentBSGetListDataResult {
        List<Comment> commentList = new ArrayList<Comment>();
        String page;
        String pageTotal;

        public List<Comment> getCommentList() {
            return commentList;
        }

        public void setCommentList(List<Comment> commentList) {
            this.commentList = commentList;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPageTotal() {
            return pageTotal;
        }

        public void setPageTotal(String pageTotal) {
            this.pageTotal = pageTotal;
        }
    }

    public int getCouponID() {
        return couponID;
    }

    public void setCouponID(int couponID) {
        this.couponID = couponID;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

