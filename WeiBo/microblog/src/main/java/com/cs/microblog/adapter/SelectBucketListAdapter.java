package com.cs.microblog.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.bean.PhotoBucket;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.utils.SimpleDraweeViewUtils;
import com.facebook.drawee.view.SimpleDraweeView;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/14.
 */
public class SelectBucketListAdapter extends RecyclerView.Adapter<SelectBucketListAdapter.MyViewHolder> {
    private final ArrayList<PhotoBucket> mAlbumList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public SelectBucketListAdapter(Context context, ArrayList<PhotoBucket> mAlbumList) {
        this.mContext = context;
        if(mAlbumList!=null){
            this.mAlbumList = mAlbumList;
        }else{
            this.mAlbumList = new ArrayList<>();
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_bucket_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PhotoBucket photoBucket = mAlbumList.get(position);
        SimpleDraweeViewUtils.setResizeImage(holder.ivCover,
                Uri.parse(Constants.FILE_URI_BEGIN + photoBucket.getPhotoList().get(0).getImagePath()),
                90, 90);
        holder.tvName.setText(photoBucket.getBucketName());
        holder.tvCount.setText("(" + photoBucket.getCount() + ")");
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;
        @BindView(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_count)
        TextView tvCount;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.OnItemClick(getPosition());
                    }
                });
            }
        }
    }
}
