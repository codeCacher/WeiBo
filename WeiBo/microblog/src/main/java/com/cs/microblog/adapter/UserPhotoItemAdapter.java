package com.cs.microblog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs.microblog.R;
import com.cs.microblog.bean.Statuse;
import com.cs.microblog.utils.DisplayUtils;
import com.cs.microblog.view.EndlessRecyclerView;
import com.cs.microblog.view.EndlessRecyclerViewAdapter;
import com.cs.microblog.view.EndlessRecyclerViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/27.
 */

public class UserPhotoItemAdapter extends EndlessRecyclerViewAdapter<UserPhotoItemAdapter.PhotoItemViewHolder> {
    public static final int ITEM_NORMAL = 1;
    public static final int ITEM_FOOT = 0;
    private final EndlessRecyclerView mEndlessRV;
    private final Context mContext;
    private final int mWith;
    ArrayList<Statuse> mStatuses;
    ArrayList<String> mPhotoUrlList;


    public UserPhotoItemAdapter(Context context, EndlessRecyclerView erv, ArrayList<Statuse> mStatuses) {
        super(context);
        this.mContext = context;
        this.mEndlessRV = erv;
        this.mStatuses = mStatuses;
        mWith = DisplayUtils.getInstance(context).getDisplayWight()/3;
        mPhotoUrlList = new ArrayList<>();
        if (mStatuses != null) {
            for (Statuse s : mStatuses) {
                mPhotoUrlList.addAll(s.getBmiddlePicUrlList());
            }
        }
    }

    @Override
    public int getEndlessItemCount() {
        mPhotoUrlList.clear();
        if (mStatuses != null) {
            for (Statuse s : mStatuses) {
                mPhotoUrlList.addAll(s.getBmiddlePicUrlList());
            }
        }
        return mPhotoUrlList.size();
    }

    @Override
    public int getEndlessItemViewType(int position) {
        return ITEM_NORMAL;
    }

    @Override
    public PhotoItemViewHolder onCreateEndlessViewHolder(ViewGroup parent, int viewType) {
        return new PhotoItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.user_photo, parent, false), ITEM_NORMAL);
    }

    @Override
    public void onBindEndlessViewHolder(PhotoItemViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.ivPhoto.getLayoutParams();
        layoutParams.height = mWith;
        holder.ivPhoto.setLayoutParams(layoutParams);
        holder.ivPhoto.setImageURI(mPhotoUrlList.get(position));
    }

    class PhotoItemViewHolder extends EndlessRecyclerViewHolder {
        @BindView(R.id.iv_photo)
        SimpleDraweeView ivPhoto;
        PhotoItemViewHolder(View itemView, int viewType) {
            super(itemView, viewType);
            ButterKnife.bind(this,itemView);
        }
    }
}
