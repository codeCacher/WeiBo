package com.cs.microblog.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.utils.TimeUtils;
import com.cs.microblog.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Administrator on 2017/4/28.
 */

public class AdapterBlogItem extends RecyclerView.Adapter<AdapterBlogItem.ViewHolderBlogItem> {
    private static final String TAG = "AdapterBlogItem";
    private Context context;
    private Statuse[] statuses;

    public AdapterBlogItem(Context context, Statuse[] statuses) {
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

        Picasso.with(context).load(statuses[position].getUser().getProfile_image_url()).into(holder.iv_blogTitle);
        holder.getTv_userName().setText(statuses[position].getUser().getName());
        holder.getTv_blogText().setText(statuses[position].getText());
        holder.getTv_blogInfo().setText(blogInfo);
    }

    private String parseBlogInfo(int postition) {

        //parse the create time
        String created_at = statuses[postition].getCreated_at();
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
        String fullSource = statuses[postition].getSource();
        String source = fullSource.substring(fullSource.indexOf(">") + 1, fullSource.lastIndexOf("<"));
        blogInfo.append(source);

        return blogInfo.toString();
    }

    @Override
    public int getItemCount() {
        return statuses.length;
    }

    class ViewHolderBlogItem extends RecyclerView.ViewHolder {

        private CircleImageView iv_blogTitle;
        private TextView tv_userName;
        private TextView tv_blogInfo;
        private TextView tv_blogText;

        ViewHolderBlogItem(View itemView) {
            super(itemView);
            iv_blogTitle = (CircleImageView) itemView.findViewById(R.id.iv_blog_title);
            tv_userName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tv_blogInfo = (TextView) itemView.findViewById(R.id.tv_blog_info);
            tv_blogText = (TextView) itemView.findViewById(R.id.tv_blog_text);
        }

        public CircleImageView getIv_blogTitle() {
            return iv_blogTitle;
        }

        public void setIv_blogTitle(CircleImageView iv_blogTitle) {
            this.iv_blogTitle = iv_blogTitle;
        }

        public TextView getTv_userName() {
            return tv_userName;
        }

        public void setTv_userName(TextView tv_userName) {
            this.tv_userName = tv_userName;
        }

        public TextView getTv_blogInfo() {
            return tv_blogInfo;
        }

        public void setTv_blogInfo(TextView tv_blogInfo) {
            this.tv_blogInfo = tv_blogInfo;
        }

        TextView getTv_blogText() {
            return tv_blogText;
        }

        public void setTv_blogText(TextView tv_blogText) {
            this.tv_blogText = tv_blogText;
        }
    }
}
