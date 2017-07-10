package com.cs.microblog.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.cs.microblog.R;
import com.cs.microblog.activity.AlbumActivity;
import com.cs.microblog.bean.Photo;
import com.cs.microblog.view.SelectImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/14.
 */

public class AlbumPhotoGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int ITEM_CAMERA = 0;
    private static final int ITEM_NORMAL = 1;
    private final LinkedList<Photo> mSelectedPhoto;
    private final AlbumActivity mAlbumActivity;
    public ArrayList<Photo> mPhotoList;
    private final Context mContext;
    private final int photoHeight;
    private OnSelectStateChangeListener mOnSelectStateChangeListener;
    private View.OnClickListener mOnTakePhotoItemClickListener;


    public interface OnSelectStateChangeListener{
        void OnSelectStateChange();
    }
    public void setOnSelectStateChangeListener(OnSelectStateChangeListener listener){
        this.mOnSelectStateChangeListener = listener;
    }

    public AlbumPhotoGridAdapter(Context context, AlbumActivity albumActivity, ArrayList<Photo> photoList, LinkedList<Photo> mSelectedPhoto) {
        this.mPhotoList = photoList;
        this.mContext = context;
        this.mAlbumActivity = albumActivity;
        WindowManager WM = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = WM.getDefaultDisplay().getWidth();
        photoHeight = screenWidth/4;
        this.mSelectedPhoto = mSelectedPhoto;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==ITEM_CAMERA){
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.album_item_camera, parent, false);
            return new AlbumPhotoCameraViewHolder(itemView);
        }else {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);
            return new AlbumPhotoViewHolder(itemView);
        }
    }

    public void setOnTakePhotoItemClickListener(View.OnClickListener listener){
        this.mOnTakePhotoItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,int position) {
        int itemViewType = getItemViewType(position);
        if(itemViewType==ITEM_CAMERA){
            AlbumPhotoCameraViewHolder cameraHolder = (AlbumPhotoCameraViewHolder) holder;
            ViewGroup.LayoutParams layoutParams = cameraHolder.ivCamera.getLayoutParams();
            layoutParams.height = photoHeight;
            cameraHolder.ivCamera.setLayoutParams(layoutParams);
            if(mOnTakePhotoItemClickListener!=null){
                cameraHolder.ivCamera.setOnClickListener(mOnTakePhotoItemClickListener);
            }
        }else {
            final AlbumPhotoViewHolder photoHolder = (AlbumPhotoViewHolder) holder;
            final int fPosition = position-1;
            ViewGroup.LayoutParams layoutParams = photoHolder.sivPhoto.getLayoutParams();
            layoutParams.height = photoHeight;
            photoHolder.sivPhoto.setLayoutParams(layoutParams);
            photoHolder.sivPhoto.showImage(mPhotoList.get(fPosition).getImagePath());
            photoHolder.sivPhoto.setSelect(mPhotoList.get(fPosition).isSelected());
            photoHolder.sivPhoto.setOnSelectClickedListener(new SelectImageView.OnSelectClickedListener() {
                @Override
                public void onSelectClick() {
                    if(photoHolder.sivPhoto.isSelected()){
                        if(mSelectedPhoto.size()>=9) {
                            Toast.makeText(mContext,"最多选择9张照片",Toast.LENGTH_SHORT).show();
                            photoHolder.sivPhoto.setSelect(false);
                            return;
                        }
                        mPhotoList.get(fPosition).setSelected(true);
                        mSelectedPhoto.add(mPhotoList.get(fPosition));
                    }else {
                        mPhotoList.get(fPosition).setSelected(false);
                        mSelectedPhoto.remove(mPhotoList.get(fPosition));
                    }
                    if(mOnSelectStateChangeListener!=null) {
                        mOnSelectStateChangeListener.OnSelectStateChange();
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return ITEM_CAMERA;
        }else {
            return  ITEM_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size()+1;
    }

    class AlbumPhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.siv_photo)
        SelectImageView sivPhoto;
        AlbumPhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class AlbumPhotoCameraViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_camera)
        ImageView ivCamera;
        AlbumPhotoCameraViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
