package com.cs.microblog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.adapter.CommentDetailAdapter;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.custom.Statuse;
import com.cs.microblog.utils.SharedPreferencesUtils;
import com.cs.microblog.utils.WeiBoUtils;
import com.cs.microblog.view.BlogItemBottomButtonView;
import com.cs.microblog.view.EndlessRecyclerView;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/1.
 */

public class BlogDetailActivity extends AppCompatActivity {

    private static final String TAG = "BlogDetailActivity";

    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.erv_comment)
    EndlessRecyclerView ervComment;
    @BindView(R.id.bibbv_repost)
    BlogItemBottomButtonView bibbvRepost;
    @BindView(R.id.bibbv_comment)
    BlogItemBottomButtonView bibbvComment;
    @BindView(R.id.bibbv_like)
    BlogItemBottomButtonView bibbvLike;


    private String mAaccessToken;
    private LinearLayoutManager mLinearLayoutManager;
    private Statuse mStatuse;
    private CommentDetailAdapter mCommentDetailAdapter;

    Subscriber<Statuse> blogDownRefreshSubscriber = new Subscriber<Statuse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            ervComment.finishDropDownRefreshOnFailure(BlogDetailActivity.this);
        }

        @Override
        public void onNext(Statuse status) {
            mCommentDetailAdapter.mStatus = status;
            ervComment.finishDropDownRefreshOnSuccess(BlogDetailActivity.this);
        }
    };

    Subscriber<CommentList> commentDownRefreshSubscriber = new Subscriber<CommentList>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            ervComment.finishDropDownRefreshOnFailure(BlogDetailActivity.this);
        }

        @Override
        public void onNext(CommentList commentList) {
            mCommentDetailAdapter.mCommentLists = commentList;
            ervComment.finishDropDownRefreshOnSuccess(BlogDetailActivity.this);
        }
    };

    Subscriber<CommentList> commentUpRefreshSubscriber = new Subscriber<CommentList>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            ervComment.finishDropDownRefreshOnFailure(BlogDetailActivity.this);
        }

        @Override
        public void onNext(CommentList commentList) {
            if (commentList != null && commentList.commentList != null && commentList.commentList.size() != 0) {
                mCommentDetailAdapter.mCommentLists.commentList.addAll(commentList.commentList);
                ervComment.finishPullUpRefreshOnSuccess(BlogDetailActivity.this);
            } else {
                ervComment.finishPullUpRefreshOnNoMoreData(BlogDetailActivity.this);
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        ButterKnife.bind(this);
        getIntentExtra();
        initRecyclerView();
        firstFetchData();
        setRefreshListener();
    }

    public void getIntentExtra() {
        Intent intent = getIntent();
        mStatuse = intent.getParcelableExtra("status");
    }

    private void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ervComment.setLayoutManager(mLinearLayoutManager);
        mCommentDetailAdapter = new CommentDetailAdapter(getApplicationContext(), new CommentList(), mStatuse);
        ervComment.setAdapter(mCommentDetailAdapter);
    }

    private void firstFetchData() {
        mAaccessToken = SharedPreferencesUtils.getString(this, Constants.KEY_ACCESS_TOKEN, "");

        WeiBoUtils.getCommentList(this, mStatuse.getId(), 0, 0, 50, 1, 0)
                .subscribe(commentDownRefreshSubscriber);
    }


    /**
     * set refresh listener
     */
    private void setRefreshListener() {
        setAddMoreRefresh();
        setDropDownRefesh();
    }

    /**
     * set drop-down refresh listener
     */
    private void setDropDownRefesh() {

        ervComment.setOnDropDownRefeshListener(new EndlessRecyclerView.OnDropDownRefeshListener() {
            @Override
            public void onDonDropDownRefesh() {
                String token = SharedPreferencesUtils.getString(getApplicationContext(), Constants.KEY_ACCESS_TOKEN, "");
                //TODO 无法根据ID直接获取非自己发送的微博
                WeiBoUtils
                        .getBlogByID(token, mStatuse.getId())
                        .subscribeOn(Schedulers.io())
                        .subscribe(blogDownRefreshSubscriber);
                WeiBoUtils
                        .getCommentList(getApplicationContext(), mStatuse.getId(), 0, 0, 50, 1, 0)
                        .subscribe(commentDownRefreshSubscriber);
            }
        });
    }

    /**
     * set add more refresh listener
     */
    private void setAddMoreRefresh() {
        ervComment.setOnPullUpRefeshListener(new EndlessRecyclerView.OnPullUpRefeshListener() {
            @Override
            public void onPullUpRefesh() {
                long min_id = 0;
                if (mCommentDetailAdapter.mCommentLists.commentList.size() != 0) {
                    int size = mCommentDetailAdapter.mCommentLists.commentList.size();
                    Comment comment = mCommentDetailAdapter.mCommentLists.commentList.get(size - 1);
                    min_id = Long.parseLong(comment.id);
                }
                WeiBoUtils.getCommentList(getApplicationContext(), mStatuse.getId(), 0, min_id - 1, 50, 1, 0)
                        .subscribe(commentUpRefreshSubscriber);
            }
        });
    }
}
