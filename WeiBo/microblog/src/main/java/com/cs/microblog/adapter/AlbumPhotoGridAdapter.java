package com.cs.microblog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.cs.microblog.R;
import com.cs.microblog.bean.Photo;
import com.cs.microblog.view.SelectImageView;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/14.
 */

public class AlbumPhotoGridAdapter extends RecyclerView.Adapter<AlbumPhotoGridAdapter.AlbumPhotoViewHolder> {


    private final LinkedList<Photo> mSelectedPhoto;
    public ArrayList<Photo> mPhotoList;
    private final Context mContext;
    private final int photoHeight;
    private OnSelectStateChangeListener mOnSelectStateChangeListener;


    public interface OnSelectStateChangeListener{
        void OnSelectStateChange();
    }
    public void setOnSelectStateChangeListener(OnSelectStateChangeListener listener){
        this.mOnSelectStateChangeListener = listener;
    }

    public AlbumPhotoGridAdapter(Context context, ArrayList<Photo> photoList, LinkedList<Photo> mSelectedPhoto) {
        this.mPhotoList = photoList;
        this.mContext = context;
        WindowManager WM = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = WM.getDefaultDisplay().getWidth();
        photoHeight = screenWidth/4;
        this.mSelectedPhoto = mSelectedPhoto;
    }

    @Override
    public AlbumPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.album_item, parent, false);
        return new AlbumPhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlbumPhotoViewHolder holder,int position) {
        final int fPosition = position;
        ViewGroup.LayoutParams layoutParams = holder.sivPhoto.getLayoutParams();
        layoutParams.height = photoHeight;
        holder.sivPhoto.setLayoutParams(layoutParams);
        holder.sivPhoto.showImage(mPhotoList.get(position).getImagePath());
        holder.sivPhoto.setSelect(mPhotoList.get(position).isSelected());
        holder.sivPhoto.setOnSelectClickedListener(new SelectImageView.OnSelectClickedListener() {
            @Override
            public void onSelectClick() {
                if(holder.sivPhoto.isSelected()){
                    if(mSelectedPhoto.size()>=9) {
                        Toast.makeText(mContext,"最多选择9张照片",Toast.LENGTH_SHORT).show();
                        holder.sivPhoto.setSelect(false);
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

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    class AlbumPhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.siv_photo)
        SelectImageView sivPhoto;
        AlbumPhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
