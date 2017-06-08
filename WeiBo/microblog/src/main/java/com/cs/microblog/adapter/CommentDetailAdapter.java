package com.cs.microblog.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.activity.PictureViewerActivity;
import com.cs.microblog.custom.Statuse;
import com.cs.microblog.utils.WeiBoUtils;
import com.cs.microblog.view.CircleImageView;
import com.cs.microblog.view.EndlessRecyclerViewAdapter;
import com.cs.microblog.view.EndlessRecyclerViewHolder;
import com.cs.microblog.view.SudokuImage;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/2.
 */

public class CommentDetailAdapter extends EndlessRecyclerViewAdapter<EndlessRecyclerViewHolder> {

    private static final int ITEM_TYPE_BLOG = 1;
    private static final int ITEM_TYPE_SWITCH = 2;
    private static final int ITEM_TYPE_COMMENT = 3;

    public Statuse mStatus;
    private final Context mContext;
    public CommentList mCommentLists;

    public CommentDetailAdapter(Context context, CommentList commentList, Statuse status) {
        super(context);
        mContext = context;
        if (commentList == null || commentList.commentList == null) {
            mCommentLists = new CommentList();
            mCommentLists.commentList = new ArrayList<>();
        } else {
            mCommentLists = commentList;
        }
        mStatus = status;
    }

    @Override
    public int getEndlessItemCount() {
        if (mCommentLists == null || mCommentLists.commentList == null) {
            mCommentLists = new CommentList();
            mCommentLists.commentList = new ArrayList<>();
        }
        return mCommentLists.commentList.size() + 2;
    }

    @Override
    public int getEndlessItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_BLOG;
        } else if (position == 1) {
            return ITEM_TYPE_SWITCH;
        } else {
            return ITEM_TYPE_COMMENT;
        }
    }

    @Override
    public EndlessRecyclerViewHolder onCreateEndlessViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_BLOG) {
            return new BlogViewHolder(LayoutInflater.from(mContext).inflate(R.layout.blog_detail, parent, false), viewType);
        } else if (viewType == ITEM_TYPE_SWITCH) {
            return new SwitchViewHolder(LayoutInflater.from(mContext).inflate(R.layout.blog_detail_switch_comment_retween, parent, false), viewType);
        } else if (viewType == ITEM_TYPE_COMMENT) {
            return new CommentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.blog_comment_item, null, false), viewType);
        } else return null;
    }

    @Override
    public void onBindEndlessViewHolder(EndlessRecyclerViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_TYPE_BLOG) {
            if(mStatus==null){
                return;
            }
            final BlogViewHolder blogHolder = (BlogViewHolder) holder;
            String blogInfo = WeiBoUtils.parseBlogTimeAndSourceInfo(mStatus);

            Picasso.with(mContext).load(mStatus.getUser().getProfile_image_url()).into(blogHolder.ivBlogTitle);

            blogHolder.tvUserName.setText(mStatus.getUser().getName());
            blogHolder.tvBlogText.setText(mStatus.getText());
            blogHolder.tvBlogInfo.setText(blogInfo);

            if (mStatus.getBmiddlePicUrlList() != null) {
                blogHolder.siPicture.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        if (blogHolder.siPicture.isPaused()) {
                            blogHolder.siPicture.resume();
                        }
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        if (!blogHolder.siPicture.isPaused()) {
                            blogHolder.siPicture.pause();
                        }
                    }
                });
                setPicture(blogHolder.siPicture, mStatus.getBmiddlePicUrlList());
            }

            if (mStatus.getRetweeted_status() != null) {
                setRetweetLayout(blogHolder);
                setRetweetText(blogHolder);

                if (mStatus.getRetweeted_status().getBmiddlePicUrlList() != null) {
                    blogHolder.siRetweetPicture.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {
                            if (blogHolder.siRetweetPicture.isPaused()) {
                                blogHolder.siRetweetPicture.resume();
                            }
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            if (!blogHolder.siRetweetPicture.isPaused()) {
                                blogHolder.siRetweetPicture.pause();
                            }
                        }
                    });
                    setPicture(blogHolder.siRetweetPicture, mStatus.getRetweeted_status().getBmiddlePicUrlList());
                }
            } else {
                removeRetweetLayout(blogHolder);
            }
            if (mStatus.getPic_urls() != null) {
                blogHolder.siPicture.setOnItemClickListener(mStatus.getBmiddlePicUrlList(), new SudokuImage.OnItemClickListener() {
                    @Override
                    public void OnItemClicked(int index, ArrayList<String> imageUrlList) {
                        Intent intent = new Intent(mContext, PictureViewerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("image_urls", imageUrlList);
                        intent.putExtra("clickIndex", index);
                        mContext.startActivity(intent);
                    }
                });
            }

            if (mStatus.getRetweeted_status() != null && mStatus.getRetweeted_status().getPic_urls() != null) {
                blogHolder.siRetweetPicture.setOnItemClickListener(mStatus.getRetweeted_status().getBmiddlePicUrlList(), new SudokuImage.OnItemClickListener() {
                    @Override
                    public void OnItemClicked(int index, ArrayList<String> imageUrlList) {
                        Intent intent = new Intent(mContext, PictureViewerActivity.class);
                        intent.putExtra("image_urls", imageUrlList);
                        intent.putExtra("clickIndex", index);
                        mContext.startActivity(intent);
                    }
                });
            }
        } else if (itemViewType == ITEM_TYPE_SWITCH) {
            SwitchViewHolder switchHolder = (SwitchViewHolder)holder;
            String retweetCountStr = Integer.toString(mStatus.getReposts_count());
            String commentCountStr = Integer.toString(mStatus.getComments_count());
            String likeCountStr = Integer.toString(mStatus.getAttitudes_count());

            switchHolder.tvRetweetCount.setText("转发 " + retweetCountStr);
            switchHolder.tvCommentCount.setText("评论 " + commentCountStr);
            switchHolder.tvLikeCount.setText("赞 " + likeCountStr);
        } else if (itemViewType == ITEM_TYPE_COMMENT) {
            CommentViewHolder commentHolder = (CommentViewHolder)holder;
            Comment comment = mCommentLists.commentList.get(position - 2);
            String blogInfo = WeiBoUtils.parseCommentTime(comment);

            Picasso.with(mContext).load(comment.user.profile_image_url).into(commentHolder.ivCommentTitle);

            commentHolder.tvCommentUserName.setText(comment.user.name);
            commentHolder.tvCommentText.setText(comment.text);
            commentHolder.tvCommentInfo.setText(blogInfo);
        }
    }

    class BlogViewHolder extends EndlessRecyclerViewHolder {
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
        @BindView(R.id.ll_blog_item)
        LinearLayout llBlogItem;

        BlogViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            ButterKnife.bind(this, itemView);
        }
    }

    class SwitchViewHolder extends EndlessRecyclerViewHolder {
        @BindView(R.id.tv_retweet_count)
        TextView tvRetweetCount;
        @BindView(R.id.tv_comment_count)
        TextView tvCommentCount;
        @BindView(R.id.tv_like_count)
        TextView tvLikeCount;

        SwitchViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            ButterKnife.bind(this, itemView);
        }
    }

    class CommentViewHolder extends EndlessRecyclerViewHolder {
        @BindView(R.id.iv_comment_title)
        CircleImageView ivCommentTitle;
        @BindView(R.id.tv_comment_user_name)
        TextView tvCommentUserName;
        @BindView(R.id.tv_comment_info)
        TextView tvCommentInfo;
        @BindView(R.id.tv_comment_text)
        TextView tvCommentText;
        @BindView(R.id.ll_comment_item)
        RelativeLayout llCommentItem;
        CommentViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            ButterKnife.bind(this, itemView);
        }
    }


    private void setPicture(SudokuImage sudokuImage, ArrayList<String> bmiddleUrls) {
        sudokuImage.setImageUrls(bmiddleUrls);
    }

    private void removeRetweetLayout(BlogViewHolder holder) {
        holder.llRetweet.setVisibility(View.GONE);
    }

    private void setRetweetLayout(BlogViewHolder holder) {
        holder.llRetweet.setVisibility(View.VISIBLE);
    }

    private void setRetweetText(BlogViewHolder holder) {
        String userName = "";
        if (mStatus.getRetweeted_status() == null) {
            return;
        }
        if (mStatus.getRetweeted_status().getUser() != null) {
            userName = mStatus.getRetweeted_status().getUser().getName();
        }
        holder.tvRetweetText.setText("@" +
                userName +
                ":" +
                mStatus.getRetweeted_status().getText());
    }
}
