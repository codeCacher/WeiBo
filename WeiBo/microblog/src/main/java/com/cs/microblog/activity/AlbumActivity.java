package com.cs.microblog.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.adapter.AlbumPhotoGridAdapter;
import com.cs.microblog.adapter.SelectBucketListAdapter;
import com.cs.microblog.bean.Photo;
import com.cs.microblog.bean.PhotoBucket;
import com.cs.microblog.utils.PhotoUpAlbumHelper;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumActivity extends AppCompatActivity {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_bucket_name)
    TextView tvBucketName;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.ll_select_bucket)
    LinearLayout llSelectBucket;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;
    @BindView(R.id.tv_preview)
    TextView tvPreview;
    @BindView(R.id.iv_origin)
    ImageView ivOrigin;
    @BindView(R.id.tv_origin)
    TextView tvOrigin;
    @BindView(R.id.ll_origin)
    LinearLayout llOrigin;
    @BindView(R.id.rv_photo)
    RecyclerView rvPhoto;
    @BindView(R.id.rv_bucket_name)
    RecyclerView rvBucketName;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.v_shadow)
    View vShadow;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.top_bar)
    RelativeLayout rlTopRoot;

    private int mPosition = -1;
    private AlbumPhotoGridAdapter mAlbumAdapter;
    public ArrayList<PhotoBucket> mAlbumList = new ArrayList<>();
    private LinkedList<Photo> mSelectedPhoto = new LinkedList<>();
    private boolean isOriginImage = false;

    private View.OnClickListener onNextStepClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            ArrayList<Photo> selectedPhoto = new ArrayList<>();
            selectedPhoto.addAll(mSelectedPhoto);
            intent.putParcelableArrayListExtra("selectedPhoto", selectedPhoto);
            setResult(0, intent);
            AlbumActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity);
        ButterKnife.bind(this);

        initData();

        setOnClickListener();

        final PhotoUpAlbumHelper mPhotoUpAlbumHelper = PhotoUpAlbumHelper.getInstance(this);
        mPhotoUpAlbumHelper.setOnFinishedListener(new PhotoUpAlbumHelper.OnFinishedListener() {
            @Override
            public void onFinshed(ArrayList<PhotoBucket> list) {
                mAlbumList = list;
                markSelectState();
                addAllPhotoBucket();
                initPhotoList();
            }
        });
        mPhotoUpAlbumHelper.execute(false);
    }

    @Override
    public void onBackPressed() {
        cancelSelectPhoto();
    }

    private void cancelSelectPhoto(){
        Intent intent = new Intent();
        setResult(1, intent);
        AlbumActivity.this.finish();
    }

    private void initPhotoList() {
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(AlbumActivity.this, 4, LinearLayoutManager.VERTICAL, false);
        rvPhoto.setLayoutManager(mGridLayoutManager);
        mAlbumAdapter = new AlbumPhotoGridAdapter(getApplicationContext(), mAlbumList.get(0).getPhotoList(), mSelectedPhoto);
        rvPhoto.setAdapter(mAlbumAdapter);
        mAlbumAdapter.setOnSelectStateChangeListener(new AlbumPhotoGridAdapter.OnSelectStateChangeListener() {
            @Override
            public void OnSelectStateChange() {
                setNextStepButton();
                setOriginButton();
            }
        });
    }

    private void setNextStepButton() {
        if (mSelectedPhoto.size() > 0) {
            tvNextStep.setBackgroundResource(R.drawable.common_button_orange_selector);
            tvNextStep.setText("下一步" + "(" + mSelectedPhoto.size() + ")");
            tvNextStep.setTextColor(getResources().getColor(R.color.colorWhite));
            tvNextStep.setOnClickListener(onNextStepClickListener);
        } else {
            tvNextStep.setBackgroundResource(R.drawable.btn_disable_background);
            tvNextStep.setText("下一步");
            tvNextStep.setTextColor(getResources().getColor(R.color.colorGrayBlack));
            tvNextStep.setClickable(false);
        }
    }

    private void addAllPhotoBucket() {
        ArrayList<Photo> photoList = new ArrayList<>();
        for (PhotoBucket pb : mAlbumList) {
            photoList.addAll(pb.getPhotoList());
        }
        PhotoBucket photoBucket = new PhotoBucket();
        photoBucket.setBucketName("相机胶卷");
        photoBucket.setCount(photoList.size());
        photoBucket.setPhotoList(photoList);
        mAlbumList.add(0, photoBucket);
    }

    private void markSelectState() {
        for (int i=0;i<mSelectedPhoto.size();i++) {
            for (PhotoBucket pb : mAlbumList) {
                for (Photo pi : pb.getPhotoList()) {
                    if (mSelectedPhoto.get(i).getImageId().equals(pi.getImageId())) {
                        mSelectedPhoto.set(i,pi);
                        pi.setSelected(true);
                    }
                }
            }
        }
        setNextStepButton();
        setOriginButton();
    }

    private void initData() {
        ArrayList<Photo> selectedPhoto = getIntent().getParcelableArrayListExtra("selectedPhoto");
        if (selectedPhoto != null) {
            mSelectedPhoto.addAll(selectedPhoto);
        }
    }

    private void setOnClickListener() {
        final View.OnClickListener onSelectBucketClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEnterAnimation();
                initBucketList();
                llSelectBucket.setClickable(false);
            }
        };
        View.OnClickListener onCancelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExitAnimation();
                llSelectBucket.setOnClickListener(onSelectBucketClickListener);
            }
        };
        vShadow.setOnClickListener(onCancelClickListener);
        rlTopRoot.setOnClickListener(onCancelClickListener);
        llSelectBucket.setOnClickListener(onSelectBucketClickListener);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSelectPhoto();
            }
        });

        llOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOriginImage = !isOriginImage;
                setOriginButton();

            }
        });

    }

    private void setOriginButton() {
        if (isOriginImage) {
            ivOrigin.setImageResource(R.drawable.compose_color_yellow);
            tvOrigin.setTextColor(getResources().getColor(R.color.colorBlack));
            long totleSize = 0;
            for (Photo p : mSelectedPhoto) {
                totleSize += p.getLongImageSize();
            }
            long tep = totleSize * 100 / 1024 / 1024;
            tvOrigin.setText("原图" + "(" + tep / 100f + "M" + ")");
            tvOrigin.setTextColor(getResources().getColor(R.color.colorBlack));
        } else {
            ivOrigin.setImageResource(R.drawable.choose_interest_unchecked);
            tvOrigin.setText("原图");
            tvOrigin.setTextColor(getResources().getColor(R.color.colorGrayBlack));
        }
    }

    private void startEnterAnimation() {
        rvBucketName.setVisibility(View.VISIBLE);
        vShadow.setVisibility(View.VISIBLE);
        ivArrow.setImageResource(R.drawable.navigationbar_arrow_up);
        ObjectAnimator tranAnim0 = ObjectAnimator.ofFloat(rvBucketName, "translationY", -700, 30).setDuration(300);
        ObjectAnimator tranAnim1 = ObjectAnimator.ofFloat(rvBucketName, "translationY", 30, 0).setDuration(50);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(tranAnim0, tranAnim1);
        animatorSet.start();
    }

    private void startExitAnimation() {
        ivArrow.setImageResource(R.drawable.headlines_icon_arrow);
        ObjectAnimator tranAnim0 = ObjectAnimator.ofFloat(rvBucketName, "translationY", 0, 30).setDuration(50);
        ObjectAnimator tranAnim1 = ObjectAnimator.ofFloat(rvBucketName, "translationY", 30, -700).setDuration(300);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(tranAnim0, tranAnim1);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rvBucketName.setVisibility(View.GONE);
                vShadow.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void initBucketList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        SelectBucketListAdapter selectBucketListAdapter = new SelectBucketListAdapter(this, mAlbumList);
        rvBucketName.setLayoutManager(linearLayoutManager);
        rvBucketName.setAdapter(selectBucketListAdapter);
        selectBucketListAdapter.setOnItemClickListener(new SelectBucketListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                tvBucketName.setText(mAlbumList.get(position).getBucketName());
                mPosition = position;
                startExitAnimation();
                mAlbumAdapter.mPhotoList = mAlbumList.get(mPosition).getPhotoList();
                mAlbumAdapter.notifyDataSetChanged();
            }
        });
    }

}
