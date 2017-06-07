package com.cs.microblog.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.activity.BlogDetailActivity;
import com.cs.microblog.activity.PictureViewerActivity;
import com.cs.microblog.custom.Statuse;
import com.cs.microblog.utils.WeiBoUtils;
import com.cs.microblog.view.BlogItemBottomButtonView;
import com.cs.microblog.view.CircleImageView;
import com.cs.microblog.view.EndlessRecyclerView;
import com.cs.microblog.view.EndlessRecyclerViewAdapter;
import com.cs.microblog.view.EndlessRecyclerViewHolder;
import com.cs.microblog.view.SudokuImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/6.
 */

public class EndlessBlogItemAdapter extends EndlessRecyclerViewAdapter<EndlessBlogItemAdapter.MyViewHolder> {
    private static final String TAG = "EndlessBlogItemAdapter";
    private static final int NORMAL_ITEM = 1;

    private Context context;
    private ArrayList<Statuse> statuses;
    private EndlessRecyclerView mEndlessRecyclerView;

    public EndlessBlogItemAdapter(Context context, EndlessRecyclerView endlessRecyclerView, ArrayList<Statuse> statuses) {
        super(context);
        this.context = context;
        this.mEndlessRecyclerView = endlessRecyclerView;
        this.statuses = statuses;
    }

    @Override
    public int getEndlessItemCount() {
        return statuses.size();
    }

    @Override
    public int getEndlessItemViewType(int position) {
        return NORMAL_ITEM;
    }

    @Override
    public MyViewHolder onCreateEndlessViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.blog_item, null, false), NORMAL_ITEM);

    }

    @Override
    public void onBindEndlessViewHolder(final MyViewHolder holder, int position) {
        final Statuse statuse = statuses.get(position);
        String blogInfo = WeiBoUtils.parseBlogTimeAndSourceInfo(statuse);

        Picasso.with(context).load(statuse.getUser().getProfile_image_url()).into(holder.ivBlogTitle);

        holder.tvUserName.setText(statuse.getUser().getName());
        holder.tvBlogText.setText(statuse.getText());
        holder.tvBlogInfo.setText(blogInfo);
        holder.bibbvRepost.setText(getCountText(statuse.getReposts_count(), "转发"));
        holder.bibbvComment.setText(getCountText(statuse.getComments_count(), "评论"));
        holder.bibbvLike.setText(getCountText(statuse.getAttitudes_count(), "赞"));

        if(statuse.getBmiddlePicUrlList()!=null){
            holder.siPicture.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    if(holder.siPicture.isPaused()){
                        holder.siPicture.resume();
                    }
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    if(!holder.siPicture.isPaused()){
                        holder.siPicture.pause();
                    }
                }
            });
            setPicture(holder.siPicture,position,statuse.getBmiddlePicUrlList());
        }

        if (statuse.getRetweeted_status() != null) {
            setRetweetLayout(holder);
            setRetweetText(holder, position);

            if(statuse.getRetweeted_status().getBmiddlePicUrlList()!=null){
                holder.siRetweetPicture.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        if(holder.siRetweetPicture.isPaused()){
                            holder.siRetweetPicture.resume();
                        }
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        if(!holder.siRetweetPicture.isPaused()){
                            holder.siRetweetPicture.pause();
                        }
                    }
                });
                setPicture(holder.siRetweetPicture, position,statuse.getRetweeted_status().getBmiddlePicUrlList());
            }
        } else {
            removeRetweetLayout(holder);
        }
        if(statuse.getPic_urls() != null) {
            holder.siPicture.setOnItemClickListener(statuse.getBmiddlePicUrlList(),new SudokuImage.OnItemClickListener() {
                @Override
                public void OnItemClicked(int index, ArrayList<String> imageUrlList) {
                    Log.i(TAG,"image " + index + " clicked");
                    Intent intent = new Intent(context, PictureViewerActivity.class);
                    intent.putExtra("image_urls",imageUrlList);
                    intent.putExtra("clickIndex",index);
                    context.startActivity(intent);
                }
            });
        }

        if(statuse.getRetweeted_status()!=null && statuse.getRetweeted_status().getPic_urls() != null) {
            holder.siRetweetPicture.setOnItemClickListener(statuse.getRetweeted_status().getBmiddlePicUrlList(),new SudokuImage.OnItemClickListener() {
                @Override
                public void OnItemClicked(int index, ArrayList<String> imageUrlList) {
                    Log.i(TAG,"image " + index + " clicked");
                    Intent intent = new Intent(context, PictureViewerActivity.class);
                    intent.putExtra("image_urls",imageUrlList);
                    intent.putExtra("clickIndex",index);
                    context.startActivity(intent);
                }
            });
        }

        holder.llBlogItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BlogDetailActivity.class);
                intent.putExtra("status",statuse);
                context.startActivity(intent);
            }
        });
    }

    private void setPicture(SudokuImage sudokuImage, int position,ArrayList<String> bmiddleUrls) {
        if(mEndlessRecyclerView.getScrollState()==RecyclerView.SCROLL_STATE_IDLE) {
            sudokuImage.setImageUrls(bmiddleUrls);
        } else {
            sudokuImage.setImageVisible(bmiddleUrls.size());
            sudokuImage.setImageDimension(bmiddleUrls.size());
            MyOnScrollListener onScrollListener = new MyOnScrollListener(position,sudokuImage,bmiddleUrls);
            mEndlessRecyclerView.addOnScrollListener(onScrollListener);
        }
    }

    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        SudokuImage sudokuImage;
        int postion;
        ArrayList<String> bmiddleUrls;
        MyOnScrollListener(int position, SudokuImage sudokuImage, ArrayList<String> bmiddleUrls) {
            this.postion = position;
            this.sudokuImage = sudokuImage;
            this.bmiddleUrls = bmiddleUrls;
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)mEndlessRecyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if(postion>=firstVisibleItemPosition && postion<=lastVisibleItemPosition) {
                    sudokuImage.loadImage(bmiddleUrls);
                    mEndlessRecyclerView.removeOnScrollListener(this);
                }
            }
        }
    }

    private void removeRetweetLayout(MyViewHolder holder) {
        holder.llRetweet.setVisibility(View.GONE);
    }

    private void setRetweetLayout(MyViewHolder holder) {
        holder.llRetweet.setVisibility(View.VISIBLE);
    }

    private void setRetweetText(MyViewHolder holder, int position) {
        String userName = "";
        if(statuses.get(position).getRetweeted_status()==null){
            return;
        }
        if(statuses.get(position).getRetweeted_status().getUser()!=null){
            userName = statuses.get(position).getRetweeted_status().getUser().getName();
        }
        holder.tvRetweetText.setText("@" +
                userName +
                ":" +
                statuses.get(position).getRetweeted_status().getText());
    }

    private String getCountText(int count, String defaultText) {
        if (count == 0) {
            return defaultText;
        } else {
            return Integer.toString(count);
        }
    }

    class MyViewHolder extends EndlessRecyclerViewHolder {
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
        @BindView(R.id.bibbv_repost)
        BlogItemBottomButtonView bibbvRepost;
        @BindView(R.id.bibbv_comment)
        BlogItemBottomButtonView bibbvComment;
        @BindView(R.id.bibbv_like)
        BlogItemBottomButtonView bibbvLike;
        @BindView(R.id.ll_blog_item)
        LinearLayout llBlogItem;

        MyViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            ButterKnife.bind(this, itemView);
        }
    }
}
