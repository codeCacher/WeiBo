package com.cs.microblog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.bean.Statuse;
import com.cs.microblog.view.BlogItemBottomButtonView;
import com.cs.microblog.view.CircleImageView;
import com.cs.microblog.view.EndlessRecyclerViewAdapter;
import com.cs.microblog.view.EndlessRecyclerViewHolder;
import com.cs.microblog.view.SudokuImage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/27.
 */

public class UserBlogAdapter extends EndlessRecyclerViewAdapter<UserBlogAdapter.UserBlogVeiwHolder> {
    private static final int ITEM_NORMAL = 3;

    private ArrayList<Statuse> mStatusesList;
    private Context mContext;


    public UserBlogAdapter(Context context, ArrayList<Statuse> mStatusesList) {
        super(context);
        this.mContext = context;
        this.mStatusesList = mStatusesList;
    }

    @Override
    public int getEndlessItemCount() {
        return mStatusesList.size();
    }

    @Override
    public int getEndlessItemViewType(int position) {
        return ITEM_NORMAL;
    }

    @Override
    public UserBlogVeiwHolder onCreateEndlessViewHolder(ViewGroup parent, int viewType) {
        return new UserBlogVeiwHolder(LayoutInflater.from(mContext).inflate(R.layout.blog_item, parent, false));
    }

    @Override
    public void onBindEndlessViewHolder(UserBlogVeiwHolder holder, int position) {

    }


    class UserBlogVeiwHolder extends EndlessRecyclerViewHolder {

        @BindView(R.id.iv_blog_title)
        CircleImageView ivBlogTitle;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.tv_blog_info)
        TextView tvBlogInfo;
        @BindView(R.id.rl_blog_title)
        RelativeLayout rlBlogTitle;
        @BindView(R.id.tv_blog_text)
        TextView tvBlogText;
        @BindView(R.id.si_picture)
        SudokuImage siPicture;
        @BindView(R.id.tv_retweet_text)
        TextView tvRetweetText;
        @BindView(R.id.si_retweet_picture)
        SudokuImage siRetweetPicture;
        @BindView(R.id.ll_retweet)
        LinearLayout llRetweet;
        @BindView(R.id.v)
        View v;
        @BindView(R.id.bibbv_repost)
        BlogItemBottomButtonView bibbvRepost;
        @BindView(R.id.bibbv_comment)
        BlogItemBottomButtonView bibbvComment;
        @BindView(R.id.bibbv_like)
        BlogItemBottomButtonView bibbvLike;
        @BindView(R.id.ll_blog_item)
        LinearLayout llBlogItem;

        UserBlogVeiwHolder(View itemView) {
            super(itemView, ITEM_NORMAL);
            ButterKnife.bind(this, itemView);
        }
    }
}
