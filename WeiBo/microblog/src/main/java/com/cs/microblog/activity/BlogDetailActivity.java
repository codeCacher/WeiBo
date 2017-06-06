package com.cs.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cs.microblog.R;
import com.cs.microblog.adapter.CommentDetailAdapter;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.custom.Statuse;
import com.cs.microblog.utils.SharedPreferencesUtils;
import com.cs.microblog.utils.WeiBoUtils;
import com.sina.weibo.sdk.openapi.models.CommentList;

import rx.functions.Action1;

/**
 * Created by Administrator on 2017/6/1.
 */

public class BlogDetailActivity extends AppCompatActivity {

    private static final String TAG = "BlogDetailActivity";
    public CommentList mCommentList;
    private String mAaccessToken;
    private RecyclerView rv_comment;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout srl_update;
    private Statuse mStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        getIntentExtra();
        bindView();
        initRecyclerView();
        getCommentList();
    }

    private void getCommentList() {
        mAaccessToken = SharedPreferencesUtils.getString(this, Constants.KEY_ACCESS_TOKEN, "");

        WeiBoUtils.getCommentList(this, mStatus.getId(), 0, 0, 50, 1, 0)
                .subscribe(new Action1<CommentList>() {
                    @Override
                    public void call(CommentList commentList) {
                        mCommentList = commentList;
                        CommentDetailAdapter commentDetailAdapter = new CommentDetailAdapter(getApplicationContext(), mCommentList, mStatus);
                        rv_comment.setAdapter(commentDetailAdapter);
                    }
                });
    }

    private void bindView() {
        rv_comment = (RecyclerView) findViewById(R.id.rv_comment);
        srl_update = (SwipeRefreshLayout) findViewById(R.id.srl_update);
    }

    private void initRecyclerView() {

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_comment.setLayoutManager(mLinearLayoutManager);
        rv_comment.setAdapter(new CommentDetailAdapter(getApplicationContext(), mCommentList, mStatus));
    }

    public void getIntentExtra() {
        Intent intent = getIntent();
        mStatus = intent.getParcelableExtra("status");
    }
}
