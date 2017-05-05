package com.cs.microblog.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.custom.Statuse;
import com.cs.microblog.utils.TimeUtils;
import com.cs.microblog.view.BlogItemBottomButtonView;
import com.cs.microblog.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2017/4/28.
 * Blog item adapter
 */

public class BlogItemAdapter extends RecyclerView.Adapter<BlogItemAdapter.BlogItemViewHolder> {
    private static final String TAG = "BlogItemAdapter";
    private static final int FOOT_ITEM = 0;
    private static final int NORMAL_ITEM = 1;
    private Context context;
    private ArrayList<Statuse> statuses;
    private LayoutInflater mInflater;

    public BlogItemAdapter(Context context, @Nullable ArrayList<Statuse> statuses) {
        this.context = context;
        this.statuses = statuses;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == statuses.size()) {
            return FOOT_ITEM;
        } else {
            return NORMAL_ITEM;
        }
    }

    @Override
    public BlogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_ITEM) {

            return new BlogItemViewHolder(mInflater.inflate(R.layout.blog_item,null,false), NORMAL_ITEM);
        } else {
            return new BlogItemViewHolder(mInflater.inflate(R.layout.blog_foot_item,parent,false), FOOT_ITEM);
        }
    }

    @Override
    public void onBindViewHolder(BlogItemViewHolder holder, int position) {

        if (position == statuses.size()) {
            AnimationDrawable background = (AnimationDrawable)holder.getIv_loading().getDrawable();
            background.start();

        } else {
            String blogInfo = parseBlogInfo(position);

            Picasso.with(context).load(statuses.get(position).getUser().getProfile_image_url()).into(holder.iv_blogTitle);

            holder.getTv_userName().setText(statuses.get(position).getUser().getName());
            holder.getTv_blogText().setText(statuses.get(position).getText());
            holder.getTv_blogInfo().setText(blogInfo);
            holder.getBibbv_repost().setText(getCountText(statuses.get(position).getReposts_count(), "转发"));
            holder.getBibbv_comment().setText(getCountText(statuses.get(position).getComments_count(), "评论"));
            holder.getBibbv_like().setText(getCountText(statuses.get(position).getAttitudes_count(), "赞"));

            setPictures(holder, position);
        }

    }

    private void setPictures(BlogItemViewHolder holder, int position) {
        //set picture recycler view
        PictureAdapter pGAdapter = new PictureAdapter(context, statuses.get(position).getPic_urls(), holder.getRv_picture());
        holder.getRv_picture().setAdapter(pGAdapter);

        //different picture number,different style
        int columNumber;
        switch (statuses.get(position).getPic_urls().size()) {
            case 1:
                columNumber = 1;
                break;
            case 4:
                columNumber = 2;
                break;
            default:
                columNumber = 3;
                break;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, columNumber, LinearLayoutManager.VERTICAL, false);
        holder.getRv_picture().setLayoutManager(gridLayoutManager);
    }

    private String getCountText(int count, String defaultText) {
        if (count == 0) {
            return defaultText;
        } else {
            return Integer.toString(count);
        }
    }

    private String parseBlogInfo(int position) {

        //parse the create time
        String created_at = statuses.get(position).getCreated_at();
        StringBuffer blogInfo = new StringBuffer();
        Calendar current = Calendar.getInstance();
        Calendar creatCalendar;
        try {
            creatCalendar = TimeUtils.parseCalender(created_at);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        if (current.get(Calendar.YEAR) != creatCalendar.get(Calendar.YEAR)) {
            blogInfo.append((creatCalendar.get(Calendar.YEAR) - current.get(Calendar.YEAR)) + "年前");
        } else if (current.get(Calendar.MONTH) != creatCalendar.get(Calendar.MONTH)) {
            blogInfo.append((creatCalendar.get(Calendar.MONTH) - current.get(Calendar.MONTH)) + "个月前");
        } else if (current.get(Calendar.DAY_OF_YEAR) - creatCalendar.get(Calendar.DAY_OF_YEAR) > 2) {
            blogInfo.append((creatCalendar.get(Calendar.DAY_OF_YEAR) - current.get(Calendar.DAY_OF_YEAR)) + "天前");
        } else if (current.get(Calendar.DAY_OF_YEAR) - creatCalendar.get(Calendar.DAY_OF_YEAR) == 2) {
            blogInfo.append("前天 " + creatCalendar.get(Calendar.HOUR_OF_DAY) + ":" + creatCalendar.get(Calendar.MINUTE));
        } else if (current.get(Calendar.DAY_OF_YEAR) - creatCalendar.get(Calendar.DAY_OF_YEAR) == 1) {
            blogInfo.append("昨天 " + creatCalendar.get(Calendar.HOUR_OF_DAY) + ":" + creatCalendar.get(Calendar.MINUTE));
        } else if (current.get(Calendar.DAY_OF_YEAR) == creatCalendar.get(Calendar.DAY_OF_YEAR)) {
            if (current.get(Calendar.HOUR_OF_DAY) != creatCalendar.get(Calendar.HOUR_OF_DAY)) {
                blogInfo.append(current.get(Calendar.HOUR_OF_DAY) - creatCalendar.get(Calendar.HOUR_OF_DAY) + "小时前");
            } else if (current.get(Calendar.HOUR_OF_DAY) == creatCalendar.get(Calendar.HOUR_OF_DAY)) {
                if (current.get(Calendar.MINUTE) == creatCalendar.get(Calendar.MINUTE)) {
                    blogInfo.append("1分钟前");
                } else {
                    blogInfo.append(current.get(Calendar.MINUTE) - creatCalendar.get(Calendar.MINUTE) + "分钟前");
                }
            }
        }

        //parse the source
        blogInfo.append(" 来自 ");
        String fullSource = statuses.get(position).getSource();
        if (!TextUtils.isEmpty(fullSource)) {
            String source = fullSource.substring(fullSource.indexOf(">") + 1, fullSource.lastIndexOf("<"));
            blogInfo.append(source);

            return blogInfo.toString();
        } else {
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return statuses.size() + 1;
    }

    class BlogItemViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView iv_blogTitle = null;
        private TextView tv_userName = null;
        private TextView tv_blogInfo = null;
        private TextView tv_blogText = null;
        private BlogItemBottomButtonView bibbv_repost = null;
        private BlogItemBottomButtonView bibbv_comment = null;
        private BlogItemBottomButtonView bibbv_like = null;
        private RecyclerView rv_picture = null;
        private TextView tv_foot_text = null;
        private ImageView iv_loading = null;

        BlogItemViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == NORMAL_ITEM) {
                iv_blogTitle = (CircleImageView) itemView.findViewById(R.id.iv_blog_title);
                tv_userName = (TextView) itemView.findViewById(R.id.tv_user_name);
                tv_blogInfo = (TextView) itemView.findViewById(R.id.tv_blog_info);
                tv_blogText = (TextView) itemView.findViewById(R.id.tv_blog_text);
                bibbv_repost = (BlogItemBottomButtonView) itemView.findViewById(R.id.bibbv_repost);
                bibbv_comment = (BlogItemBottomButtonView) itemView.findViewById(R.id.bibbv_comment);
                bibbv_like = (BlogItemBottomButtonView) itemView.findViewById(R.id.bibbv_like);
                rv_picture = (RecyclerView) itemView.findViewById(R.id.rv_picture);
            } else {
                tv_foot_text = (TextView) itemView.findViewById(R.id.tv_foot_text);
                iv_loading = (ImageView) itemView.findViewById(R.id.iv_loading);
            }

        }

        //Getter and Setter
        public CircleImageView getIv_blogTitle() {
            return iv_blogTitle;
        }

        public TextView getTv_userName() {
            return tv_userName;
        }

        public TextView getTv_blogInfo() {
            return tv_blogInfo;
        }

        public TextView getTv_blogText() {
            return tv_blogText;
        }

        public BlogItemBottomButtonView getBibbv_repost() {
            return bibbv_repost;
        }

        public BlogItemBottomButtonView getBibbv_comment() {
            return bibbv_comment;
        }

        public BlogItemBottomButtonView getBibbv_like() {
            return bibbv_like;
        }

        public RecyclerView getRv_picture() {
            return rv_picture;
        }

        public TextView getTv_foot_text() {
            return tv_foot_text;
        }

        public ImageView getIv_loading() {
            return iv_loading;
        }
    }
}
