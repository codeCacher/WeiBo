package com.cs.microblog.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs.microblog.R;

/**
 * Created by Administrator on 2017/4/28.
 * Blog item adapter
 */

public abstract class EndlessRecyclerViewAdapter<VH extends EndlessRecyclerViewHolder> extends RecyclerView.Adapter<VH> {
    private static final String TAG = "EndlessRecyclerViewAdapter";
    public static final int FOOT_ITEM = 0;
    private Context context;
    private boolean mHasFootItem;

    private View mFootView;
    private ImageView iv_loading;
    private TextView tv_foot_text;
    private ViewGroup mFootViewParent;

    public abstract int getEndlessItemCount();
    public abstract int getEndlessItemViewType(int position);
    public abstract VH onCreateEndlessViewHolder(ViewGroup parent, int viewType);
    public abstract void onBindEndlessViewHolder(VH holder, int position);

    public EndlessRecyclerViewAdapter(Context context) {
        this.context = context;
        mHasFootItem = true;
    }

    /**
     * remove the foot item
     */
    public void hideFootItem() {
        mHasFootItem = false;
        mFootView.setVisibility(View.GONE);
    }

    /**
     * enable the foot item
     */
    public void showFootItem() {
        mHasFootItem = true;
        mFootView.setVisibility(View.VISIBLE);
    }

    public boolean hasFootItem(){
        return mHasFootItem;
    }

    public void setFootViewSuccess() {
        iv_loading.setVisibility(View.GONE);
        tv_foot_text.setText("更多...");
    }

    /**
     * set foot item to fail state
     */
    public void setFootViewFail() {
        iv_loading.setVisibility(View.GONE);
        tv_foot_text.setText("还没有联网哦，去设置网络吧");
    }

    /**
     * set foot item to loading state
     */
    public void setFootViewLoading() {
        iv_loading.setVisibility(View.VISIBLE);
        tv_foot_text.setText("加载中....");
    }

    /**
     * find the foot item view and bound the view
     * @param holder
     */
    private void BoundFootView(VH holder) {
        if(mFootView==null&&mHasFootItem){
            mFootView = holder.itemView;
            iv_loading = holder.iv_loading;
            tv_foot_text = holder.tv_foot_text;
            hideFootItem();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getEndlessItemCount()) {
            return FOOT_ITEM;
        } else {
            return getEndlessItemViewType(position);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOT_ITEM) {
            MyViewHolder viewHolder = new MyViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.blog_foot_item, parent, false),
                    FOOT_ITEM);
            return (VH)viewHolder;
        } else {
            return onCreateEndlessViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        int itemViewType = getItemViewType(position);
        if(itemViewType == FOOT_ITEM){
            BoundFootView(holder);
            AnimationDrawable background = (AnimationDrawable) holder.iv_loading.getDrawable();
            background.start();
        } else {
            onBindEndlessViewHolder(holder, position);
        }
    }


    @Override
    public int getItemCount() {
//        if (mHasFootItem) {
            return getEndlessItemCount() + 1;
//        } else {
//            return getEndlessItemCount();
//        }
    }

    private class MyViewHolder extends EndlessRecyclerViewHolder {
        MyViewHolder(View itemView, int viewType) {
            super(itemView,viewType);
        }
    }
}
