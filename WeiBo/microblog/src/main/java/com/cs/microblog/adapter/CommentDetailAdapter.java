package com.cs.microblog.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.activity.PictureViewerActivity;
import com.cs.microblog.custom.Statuse;
import com.cs.microblog.utils.WeiBoUtils;
import com.cs.microblog.view.CircleImageView;
import com.cs.microblog.view.SudokuImage;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/2.
 */

public class CommentDetailAdapter extends RecyclerView.Adapter<CommentDetailAdapter.MyViewHolder> {

    private static final int ITEM_TYPE_BLOG = 0;
    private static final int ITEM_TYPE_SWITCH = 1;
    private static final int ITEM_TYPE_COMMENT = 3;
    private static final int ITEM_TYPE_FOOT = 2;
    private Statuse mStatus;
    private final Context mContext;
    private final CommentList mCommentLists;

    public CommentDetailAdapter(Context context, CommentList commentList, Statuse status) {
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
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_BLOG;
        } else if (position == 1) {
            return ITEM_TYPE_SWITCH;
        } else if (position == mCommentLists.commentList.size() + 2) {
            return ITEM_TYPE_FOOT;
        } else {
            return ITEM_TYPE_COMMENT;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_BLOG) {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.blog_detail, parent, false), viewType);
        } else if (viewType == ITEM_TYPE_SWITCH) {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.blog_detail_switch_comment_retween, parent, false), viewType);
        } else if (viewType == ITEM_TYPE_FOOT) {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.blog_foot_item, parent, false), viewType);
        } else {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.blog_comment_item, null, false), viewType);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_TYPE_BLOG) {
            String blogInfo = WeiBoUtils.parseBlogTimeAndSourceInfo(mStatus);

            Picasso.with(mContext).load(mStatus.getUser().getProfile_image_url()).into(holder.iv_blog_title);

            holder.tv_user_name.setText(mStatus.getUser().getName());
            holder.tv_blog_text.setText(mStatus.getText());
            holder.tv_blog_info.setText(blogInfo);

            if (mStatus.getBmiddlePicUrlList() != null) {
                holder.si_picture.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        if (holder.si_picture.isPaused()) {
                            holder.si_picture.resume();
                        }
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        if (!holder.si_picture.isPaused()) {
                            holder.si_picture.pause();
                        }
                    }
                });
                setPicture(holder.si_picture, mStatus.getBmiddlePicUrlList());
            }

            if (mStatus.getRetweeted_status() != null) {
                setRetweetLayout(holder);
                setRetweetText(holder);

                if (mStatus.getRetweeted_status().getBmiddlePicUrlList() != null) {
                    holder.si_retweet_picture.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {
                            if (holder.si_retweet_picture.isPaused()) {
                                holder.si_retweet_picture.resume();
                            }
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            if (!holder.si_retweet_picture.isPaused()) {
                                holder.si_retweet_picture.pause();
                            }
                        }
                    });
                    setPicture(holder.si_retweet_picture, mStatus.getRetweeted_status().getBmiddlePicUrlList());
                }
            } else {
                removeRetweetLayout(holder);
            }
            if (mStatus.getPic_urls() != null) {
                holder.si_picture.setOnItemClickListener(mStatus.getBmiddlePicUrlList(), new SudokuImage.OnItemClickListener() {
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
                holder.si_retweet_picture.setOnItemClickListener(mStatus.getRetweeted_status().getBmiddlePicUrlList(), new SudokuImage.OnItemClickListener() {
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
            String retweetCountStr = Integer.toString(mStatus.getReposts_count());
            String commentCountStr = Integer.toString(mStatus.getComments_count());
            String likeCountStr = Integer.toString(mStatus.getAttitudes_count());

            holder.tv_retweet_count.setText("转发 " + retweetCountStr);
            holder.tv_comment_count.setText("评论 " + commentCountStr);
            holder.tv_like_count.setText("赞 " + likeCountStr);
        } else if (itemViewType == ITEM_TYPE_COMMENT) {
            Comment comment = mCommentLists.commentList.get(position - 2);
            String blogInfo = WeiBoUtils.parseCommentTime(comment);

            Picasso.with(mContext).load(comment.user.profile_image_url).into(holder.iv_comment_title);

            holder.tv_comment_user_name.setText(comment.user.name);
            holder.tv_comment_text.setText(comment.text);
            holder.tv_comment_info.setText(blogInfo);
        } else if (itemViewType == ITEM_TYPE_FOOT) {
            AnimationDrawable background = (AnimationDrawable) holder.iv_loading.getDrawable();
            background.start();
        }

    }

    @Override
    public int getItemCount() {
        return mCommentLists.commentList.size() + 3;
    }

    private void setPicture(SudokuImage sudokuImage, ArrayList<String> bmiddleUrls) {
        sudokuImage.setImageUrls(bmiddleUrls);
    }

    private void removeRetweetLayout(MyViewHolder holder) {
        holder.ll_retweet.setVisibility(View.GONE);
    }

    private void setRetweetLayout(MyViewHolder holder) {
        holder.ll_retweet.setVisibility(View.VISIBLE);
    }

    private void setRetweetText(MyViewHolder holder) {
        String userName = "";
        if (mStatus.getRetweeted_status() == null) {
            return;
        }
        if (mStatus.getRetweeted_status().getUser() != null) {
            userName = mStatus.getRetweeted_status().getUser().getName();
        }
        holder.tv_retweet_text.setText("@" +
                userName +
                ":" +
                mStatus.getRetweeted_status().getText());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_loading = null;
        TextView tv_foot_text = null;
        CircleImageView iv_comment_title = null;
        TextView tv_comment_user_name = null;
        TextView tv_comment_info = null;
        TextView tv_comment_text = null;
        TextView tv_retweet_count = null;
        TextView tv_comment_count = null;
        TextView tv_like_count = null;
        CircleImageView iv_blog_title = null;
        TextView tv_user_name = null;
        TextView tv_blog_info = null;
        TextView tv_blog_text = null;
        SudokuImage si_picture = null;
        LinearLayout ll_retweet = null;
        TextView tv_retweet_text = null;
        SudokuImage si_retweet_picture = null;


        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == ITEM_TYPE_BLOG) {
                iv_blog_title = (CircleImageView) itemView.findViewById(R.id.iv_blog_title);
                tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
                tv_blog_info = (TextView) itemView.findViewById(R.id.tv_blog_info);
                tv_blog_text = (TextView) itemView.findViewById(R.id.tv_blog_text);
                si_picture = (SudokuImage) itemView.findViewById(R.id.si_picture);
                ll_retweet = (LinearLayout) itemView.findViewById(R.id.ll_retweet);
                tv_retweet_text = (TextView) itemView.findViewById(R.id.tv_retweet_text);
                si_retweet_picture = (SudokuImage) itemView.findViewById(R.id.si_retweet_picture);
            } else if (viewType == ITEM_TYPE_SWITCH) {
                tv_retweet_count = (TextView) itemView.findViewById(R.id.tv_retweet_count);
                tv_comment_count = (TextView) itemView.findViewById(R.id.tv_comment_count);
                tv_like_count = (TextView) itemView.findViewById(R.id.tv_like_count);
            } else if (viewType == ITEM_TYPE_COMMENT) {
                iv_comment_title = (CircleImageView) itemView.findViewById(R.id.iv_comment_title);
                tv_comment_user_name = (TextView) itemView.findViewById(R.id.tv_comment_user_name);
                tv_comment_info = (TextView) itemView.findViewById(R.id.tv_comment_info);
                tv_comment_text = (TextView) itemView.findViewById(R.id.tv_comment_text);
            } else if (viewType == ITEM_TYPE_FOOT) {
                iv_loading = (ImageView) itemView.findViewById(R.id.iv_loading);
                tv_foot_text = (TextView) itemView.findViewById(R.id.tv_foot_text);
            }
        }
    }
}

