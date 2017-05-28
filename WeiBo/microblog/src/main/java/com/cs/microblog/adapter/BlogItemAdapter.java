package com.cs.microblog.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import com.cs.microblog.R;
import com.cs.microblog.activity.PictureViewerActivity;
import com.cs.microblog.custom.Statuse;
import com.cs.microblog.utils.TimeUtils;
import com.cs.microblog.view.BlogItemBottomButtonView;
import com.cs.microblog.view.CircleImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.squareup.picasso.Picasso;
import com.cs.microblog.custom.PicUrl;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import com.cs.microblog.view.SudokuImage;
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
    private boolean mHasFootItem;
    private RecyclerView recyclerViewParent;

    public BlogItemAdapter(Context context, RecyclerView parent, @Nullable ArrayList<Statuse> statuses) {
        this.context = context;
        this.statuses = statuses;
        this.mInflater = LayoutInflater.from(context);
        this.recyclerViewParent = parent;
        mHasFootItem = true;
    }

    /**
     * remove the foot item
     */
    public void removeFootItem() {
        mHasFootItem = false;
    }

    /**
     * enable the foot item
     */
    public void addFootItem() {
        mHasFootItem = true;
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
            return new BlogItemViewHolder(mInflater.inflate(R.layout.blog_item, null, false), NORMAL_ITEM);
        } else {
            return new BlogItemViewHolder(mInflater.inflate(R.layout.blog_foot_item, parent, false), FOOT_ITEM);
        }
    }

    @Override
    public void onBindViewHolder(BlogItemViewHolder holder, int position) {

        if (position == statuses.size()) {
            AnimationDrawable background = (AnimationDrawable) holder.getIv_loading().getDrawable();
            background.start();

        } else {
            final Statuse statuse = statuses.get(position);
            String blogInfo = parseBlogInfo(position);

            Picasso.with(context).load(statuse.getUser().getProfile_image_url()).into(holder.iv_blogTitle);

            holder.getTv_userName().setText(statuse.getUser().getName());
            holder.getTv_blogText().setText(statuse.getText());
            holder.getTv_blogInfo().setText(blogInfo);
            holder.getBibbv_repost().setText(getCountText(statuse.getReposts_count(), "转发"));
            holder.getBibbv_comment().setText(getCountText(statuse.getComments_count(), "评论"));
            holder.getBibbv_like().setText(getCountText(statuse.getAttitudes_count(), "赞"));

            setPicture(holder.getSi_picture(),position,statuse.getBmiddlePicUrlList());

            if (statuse.getRetweeted_status() != null) {
                setRetweetLayout(holder);
                setRetweetText(holder, position);

                setPicture(holder.getSi_retweet_picture(), position,statuse.getRetweeted_status().getBmiddlePicUrlList());
            } else {
                removeRetweetLayout(holder);
            }
            if(statuse.getPic_urls() != null) {
                holder.getSi_picture().setOnItemClickListener(new SudokuImage.OnItemClickListener() {
                    @Override
                    public void OnItemClicked(int index, ArrayList<String> imageUrlList) {
                        //TODO 传过去的url不对
                        Log.i(TAG,"image " + index + " clicked");
                        Fresco.getImagePipeline().pause();
                        Intent intent = new Intent(context, PictureViewerActivity.class);
                        intent.putExtra("image_urls",imageUrlList);
                        intent.putExtra("clickIndex",index);
                        context.startActivity(intent);
                    }
                });
            }
        }

    }
    private void setPicture(SudokuImage sudokuImage, int position,ArrayList<String> bmiddleUrls) {
        sudokuImage.setImageUrls(bmiddleUrls);
        if(recyclerViewParent.getScrollState()==RecyclerView.SCROLL_STATE_IDLE) {
            sudokuImage.showImageUrls();
        } else {
            sudokuImage.setImageVisible();
            sudokuImage.setImageDimension();
            MyOnScrollListener onScrollListener = new MyOnScrollListener(position,sudokuImage);
            recyclerViewParent.addOnScrollListener(onScrollListener);
        }
    }

    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        SudokuImage sudokuImage;
        int postion;
        public MyOnScrollListener(int position, SudokuImage sudokuImage) {
            this.postion = position;
            this.sudokuImage = sudokuImage;
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE ) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerViewParent.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if(postion>=firstVisibleItemPosition && postion<=lastVisibleItemPosition) {
                    sudokuImage.loadImage();
                    recyclerViewParent.removeOnScrollListener(this);
                }

            }
        }
    }

    private void removeRetweetLayout(BlogItemViewHolder holder) {
        holder.getLl_retweet().setVisibility(View.GONE);
    }

    private void setRetweetLayout(BlogItemViewHolder holder) {
        holder.getLl_retweet().setVisibility(View.VISIBLE);
    }

    private void setRetweetText(BlogItemViewHolder holder, int position) {
        holder.getTv_retweet_text().setText("@" +
                statuses.get(position).getRetweeted_status().getUser().getName() +
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
    /**
     * get blog information
     *
     * @param position item position
     * @return the information string
     */
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
        if (mHasFootItem) {
            return statuses.size() + 1;
        } else {
            return statuses.size();
        }
    }

    class BlogItemViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView iv_blogTitle = null;
        private TextView tv_userName = null;
        private TextView tv_blogInfo = null;
        private TextView tv_blogText = null;
        private BlogItemBottomButtonView bibbv_repost = null;
        private BlogItemBottomButtonView bibbv_comment = null;
        private BlogItemBottomButtonView bibbv_like = null;
//        private RecyclerView rv_picture = null;
        private TextView tv_foot_text = null;
        private ImageView iv_loading = null;
        private TextView tv_retweet_text = null;
        //        private RecyclerView rv_retweet_picture = null;
        private LinearLayout ll_retweet = null;
        private SudokuImage si_picture = null;
        private SudokuImage si_retweet_picture = null;

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
                si_picture = (SudokuImage) itemView.findViewById(R.id.si_picture);

                tv_retweet_text = (TextView) itemView.findViewById(R.id.tv_retweet_text);
                si_retweet_picture = (SudokuImage) itemView.findViewById(R.id.si_retweet_picture);
                ll_retweet = (LinearLayout) itemView.findViewById(R.id.ll_retweet);
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

        public TextView getTv_foot_text() {
            return tv_foot_text;
        }

        public ImageView getIv_loading() {
            return iv_loading;
        }

        public TextView getTv_retweet_text() {
            return tv_retweet_text;
        }

        public LinearLayout getLl_retweet() {
            return ll_retweet;
        }

        public SudokuImage getSi_picture() {
            return si_picture;
        }

        public SudokuImage getSi_retweet_picture() {
            return si_retweet_picture;
        }
    }
}
