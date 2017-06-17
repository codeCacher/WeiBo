package com.cs.microblog.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cs.microblog.R;
import com.cs.microblog.bean.Photo;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.utils.SimpleDraweeViewUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/15.
 */

public class SelectPhotoAdapter extends RecyclerView.Adapter {

    private static final int ITEM_TYPE_ADD_IMAGE = 0;
    private static final int ITEM_TYPE_PHOTO = 1;
    private final int mPhotoWidth;
    public ArrayList<Photo> mSelectedPhoto;
    private final Context mContext;
    private OnPhotoItemClickListener mOnPhotoItemClickListener;
    private OnPhotoItemRemoveClickListener mOnPhotoItemRemoveClickListener;
    private OnAddItemClickListener mOnAddItemClickListener;


    public SelectPhotoAdapter(Context mContext, ArrayList<Photo> mSelectedPhoto) {
        this.mSelectedPhoto = mSelectedPhoto;
        this.mContext = mContext;
        WindowManager WM = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        int mScreenWith = WM.getDefaultDisplay().getWidth();
        mPhotoWidth = (mScreenWith-32)/3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mSelectedPhoto.size() && mSelectedPhoto.size() < 9) {
            return ITEM_TYPE_ADD_IMAGE;
        } else {
            return ITEM_TYPE_PHOTO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_ADD_IMAGE) {
            return new SelectMoreViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_more_photo, parent, false));
        } else {
            return new SelectPhotoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_photo_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = mPhotoWidth;
        layoutParams.width = mPhotoWidth;
        holder.itemView.setLayoutParams(layoutParams);
        if (itemViewType == ITEM_TYPE_PHOTO) {
            SelectPhotoViewHolder spHolder = (SelectPhotoViewHolder) holder;
            SimpleDraweeViewUtils.setResizeImage(spHolder.sdvPhoto,
                    Uri.parse(Constants.FILE_URI_BEGIN + mSelectedPhoto.get(position).getImagePath()),
                    90, 90);

            if(mOnPhotoItemRemoveClickListener!=null){
                spHolder.ivDiselect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnPhotoItemRemoveClickListener.OnPhotoItemRemoveClick(position);
                    }
                });
            }

            if(mOnPhotoItemClickListener!=null) {
                spHolder.sdvPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnPhotoItemClickListener.OnPhotoItemClick(position);
                    }
                });
            }

        } else {
            SelectMoreViewHolder smHolder = (SelectMoreViewHolder) holder;
            if(mOnAddItemClickListener!=null) {
                smHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnAddItemClickListener.OnAddItemClick(position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mSelectedPhoto.size()==0){
            return 0;
        }else if(mSelectedPhoto.size()>=9){
            return 9;
        }else {
            return mSelectedPhoto.size() + 1;
        }
    }

    public interface OnPhotoItemClickListener{
        void OnPhotoItemClick(int position);
    }

    public void setOnPhotoItemClickListener(OnPhotoItemClickListener listener){
        this.mOnPhotoItemClickListener = listener;
    }

    public interface OnPhotoItemRemoveClickListener{
        void OnPhotoItemRemoveClick(int position);
    }

    public void setOnPhotoItemRemoveClickListener(OnPhotoItemRemoveClickListener listener){
        this.mOnPhotoItemRemoveClickListener = listener;
    }

    public interface OnAddItemClickListener{
        void OnAddItemClick(int position);
    }

    public void setOnAddItemClickListener(OnAddItemClickListener listener){
        this.mOnAddItemClickListener = listener;
    }

    class SelectPhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sdv_photo)
        SimpleDraweeView sdvPhoto;
        @BindView(R.id.iv_diselect)
        ImageView ivDiselect;

        SelectPhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }

    class SelectMoreViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_add)
        ImageView ivAdd;

        SelectMoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);



        }
    }
}
