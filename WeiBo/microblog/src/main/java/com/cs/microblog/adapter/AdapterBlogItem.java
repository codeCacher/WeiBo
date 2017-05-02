package com.cs.microblog.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Administrator on 2017/4/28.
 * Blog item adapter
 */

public class AdapterBlogItem extends RecyclerView.Adapter<AdapterBlogItem.ViewHolderBlogItem> {
    private static final String TAG = "AdapterBlogItem";
    private Context context;
    private ArrayList<Statuse> statuses;

    public AdapterBlogItem(Context context, ArrayList<Statuse> statuses) {
        this.context = context;
        this.statuses = statuses;
    }

    @Override
    public ViewHolderBlogItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderBlogItem(View.inflate(context, R.layout.blog_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolderBlogItem holder, int position) {

        String blogInfo = parseBlogInfo(position);

        Picasso.with(context).load(statuses.get(position).getUser().getProfile_image_url()).into(holder.iv_blogTitle);
        holder.getTv_userName().setText(statuses.get(position).getUser().getName());
        holder.getTv_blogText().setText(statuses.get(position).getText());
        holder.getTv_blogInfo().setText(blogInfo);
        holder.getBibbv_repost().setText(getCountText(statuses.get(position).getReposts_count(),"转发"));
        holder.getBibbv_comment().setText(getCountText(statuses.get(position).getComments_count(),"评论"));
        holder.getBibbv_like().setText(getCountText(statuses.get(position).getAttitudes_count(),"赞"));

        setPictures(holder, position);
    }

    private void setPictures(ViewHolderBlogItem holder, int position) {
        //set picture recycler view
        PictureAdapter pGAdapter = new PictureAdapter(context,statuses.get(position).getPic_urls(),holder.getRv_picture());
        holder.getRv_picture().setAdapter(pGAdapter);

        //different picture number,different style
        int columNumber;
        switch (statuses.get(position).getPic_urls().size()){
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

    private String getCountText(int count,String defaultText) {
        if(count==0) {
            return defaultText;
        }else {
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
            blogInfo.append( (creatCalendar.get(Calendar.MONTH) - current.get(Calendar.MONTH)) + "个月前");
        } else if (current.get(Calendar.DAY_OF_YEAR) - creatCalendar.get(Calendar.DAY_OF_YEAR) > 2) {
            blogInfo.append( (creatCalendar.get(Calendar.DAY_OF_YEAR) - current.get(Calendar.DAY_OF_YEAR)) + "天前");
        } else if (current.get(Calendar.DAY_OF_YEAR) - creatCalendar.get(Calendar.DAY_OF_YEAR) == 2) {
            blogInfo.append( "前天 " + creatCalendar.get(Calendar.HOUR_OF_DAY) + ":" + creatCalendar.get(Calendar.MINUTE));
        } else if (current.get(Calendar.DAY_OF_YEAR) - creatCalendar.get(Calendar.DAY_OF_YEAR) == 1) {
            blogInfo.append( "昨天 " + creatCalendar.get(Calendar.HOUR_OF_DAY) + ":" + creatCalendar.get(Calendar.MINUTE));
        } else if (current.get(Calendar.DAY_OF_YEAR) == creatCalendar.get(Calendar.DAY_OF_YEAR)) {
            if (current.get(Calendar.HOUR_OF_DAY) != creatCalendar.get(Calendar.HOUR_OF_DAY)) {
                blogInfo.append( current.get(Calendar.HOUR_OF_DAY) - creatCalendar.get(Calendar.HOUR_OF_DAY) + "小时前");
            } else if(current.get(Calendar.HOUR_OF_DAY) == creatCalendar.get(Calendar.HOUR_OF_DAY)) {
                if(current.get(Calendar.MINUTE) == creatCalendar.get(Calendar.MINUTE)) {
                    blogInfo.append( "1分钟前");
                }else {
                    blogInfo.append( current.get(Calendar.MINUTE) - creatCalendar.get(Calendar.MINUTE) + "分钟前");
                }
            }
        }

        //parse the source
        blogInfo.append(" 来自 ");
        String fullSource = statuses.get(position).getSource();
        String source = fullSource.substring(fullSource.indexOf(">") + 1, fullSource.lastIndexOf("<"));
        blogInfo.append(source);

        return blogInfo.toString();
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    class ViewHolderBlogItem extends RecyclerView.ViewHolder {

        private final CircleImageView iv_blogTitle;
        private final TextView tv_userName;
        private final TextView tv_blogInfo;
        private final TextView tv_blogText;
        private final BlogItemBottomButtonView bibbv_repost;
        private final BlogItemBottomButtonView bibbv_comment;
        private final BlogItemBottomButtonView bibbv_like;
        private final RecyclerView rv_picture;

        ViewHolderBlogItem(View itemView) {
            super(itemView);
            iv_blogTitle = (CircleImageView) itemView.findViewById(R.id.iv_blog_title);
            tv_userName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tv_blogInfo = (TextView) itemView.findViewById(R.id.tv_blog_info);
            tv_blogText = (TextView) itemView.findViewById(R.id.tv_blog_text);
            bibbv_repost = (BlogItemBottomButtonView) itemView.findViewById(R.id.bibbv_repost);
            bibbv_comment = (BlogItemBottomButtonView) itemView.findViewById(R.id.bibbv_comment);
            bibbv_like = (BlogItemBottomButtonView) itemView.findViewById(R.id.bibbv_like);
            rv_picture = (RecyclerView) itemView.findViewById(R.id.rv_picture);
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
    }
}
