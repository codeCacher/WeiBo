package com.cs.microblog.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cs.microblog.R;
import com.cs.microblog.utils.SimpleDraweeViewUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/14.
 */

public class SelectImageView extends RelativeLayout {

    Boolean mSelected = false;
    @BindView(R.id.iv_photo)
    SimpleDraweeView ivPhoto;
    @BindView(R.id.iv_cloud)
    ImageView ivCloud;
    @BindView(R.id.iv_state)
    ImageView ivState;

    private OnSelectClickedListener mOnSelectClickedListener;

    public SelectImageView(Context context) {
        this(context, null);
    }

    public SelectImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.selectable_photo_item, this);
        ButterKnife.bind(this, view);

        ivState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelected = !mSelected;
                setSelect(mSelected);
                if(mOnSelectClickedListener!=null){
                    mOnSelectClickedListener.onSelectClick();
                }
            }
        });
    }

    public interface OnSelectClickedListener{
        void onSelectClick();
    }
    public void setOnSelectClickedListener(OnSelectClickedListener listener){
        this.mOnSelectClickedListener = listener;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setOnPhotoClickedListener(OnClickListener onClickListener) {
        ivPhoto.setOnClickListener(onClickListener);
    }

    public void setSelect(boolean b) {
        mSelected=b;
        if(b){
            ivState.setImageResource(R.drawable.choose_interest_checked);
            ivCloud.setBackgroundResource(R.color.shadowTransclute);
        } else {
            ivState.setImageResource(R.drawable.select_none_selected_round);
            ivCloud.setBackgroundResource(R.color.transclute);
        }
    }

    public void showImage(String path) {
        Uri uri = Uri.parse("file://" + path);
        SimpleDraweeViewUtils.setResizeImage(ivPhoto, uri, 90, 90);
    }


}
