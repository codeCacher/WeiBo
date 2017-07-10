package com.cs.microblog.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.microblog.R;
import com.cs.microblog.adapter.SelectPhotoAdapter;
import com.cs.microblog.bean.Photo;
import com.cs.microblog.bean.Statuse;
import com.cs.microblog.service.BlogPostService;
import com.cs.microblog.service.IPostBlog;
import com.cs.microblog.utils.WeiBoUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/6/11.
 */

public class PostBlogActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_center_text)
    TextView tvCenterText;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.bt_send)
    Button btSend;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_text_count)
    TextView tvTextCount;
    @BindView(R.id.iv_post_type)
    ImageView ivPostType;
    @BindView(R.id.tv_post_type)
    TextView tvPostType;
    @BindView(R.id.rl_post_type)
    RelativeLayout rlPostType;
    @BindView(R.id.rl_state)
    RelativeLayout rlState;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.iv_at)
    ImageView ivAt;
    @BindView(R.id.iv_topic)
    ImageView ivTopic;
    @BindView(R.id.iv_emoj)
    ImageView ivEmoj;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.rv_selected_picture)
    RecyclerView rvSelectedPicture;
    @BindView(R.id.iv_repost_user_image)
    SimpleDraweeView ivRepostUserImage;
    @BindView(R.id.tv_repost_user_name)
    TextView tvRepostUserName;
    @BindView(R.id.tv_repost_text)
    TextView tvRepostText;
    @BindView(R.id.rl_repost)
    RelativeLayout rlRepost;

    public static final int TAG_POST_TEXT = 0;
    public static final int TAG_POST_IMAGE = 1;
    public static final int TAG_POST_PHOTO = 2;
    public static final int TAG_REPOST = 3;



    private IPostBlog mBlogPostService;
    private int mTag;
    private ArrayList<Photo> mSelectedPhoto = new ArrayList<>();


    Subscriber<User> userSubscriber = new Subscriber<User>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            tvUserName.setText("");
        }

        @Override
        public void onNext(final User user) {
            if (user == null) {
                return;
            }
            tvUserName.setText(user.name);
        }
    };
    private SelectPhotoAdapter mSelectPhotoAdapter;
    private Statuse statuse;

    private class MyServiceConnection implements ServiceConnection {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBlogPostService = (IPostBlog) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_blog);
        ButterKnife.bind(this);

        mTag = getIntent().getIntExtra("TAG", 0);
        if (mTag == TAG_POST_IMAGE) {
            Intent intent = new Intent(PostBlogActivity.this, AlbumActivity.class);
            intent.putExtra("TAG", TAG_POST_IMAGE);
            startActivityForResult(intent, 0);
        } else if (mTag == TAG_POST_PHOTO) {
            Intent intent = new Intent(PostBlogActivity.this, AlbumActivity.class);
            intent.putExtra("TAG", TAG_POST_PHOTO);
            startActivityForResult(intent, 0);
        } else if (mTag == TAG_REPOST) {
            statuse = getIntent().getParcelableExtra("status");
            if(statuse!=null){
                rlRepost.setVisibility(View.VISIBLE);
                ivRepostUserImage.setImageURI(statuse.getUser().getProfile_image_url());
                tvRepostUserName.setText("@"+statuse.getUser().getName());
                tvRepostText.setText(statuse.getText());
            }
        }

        WeiBoUtils
                .getLoginUserInfo(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userSubscriber);

        Intent intent = new Intent(this, BlogPostService.class);
        bindService(intent, new MyServiceConnection(), BIND_AUTO_CREATE);

        btSend.setOnClickListener(this);
        ivPicture.setOnClickListener(this);

        addTextChangeListener();

        initPhotoList();
    }

    private void initPhotoList() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvSelectedPicture.setLayoutManager(gridLayoutManager);
        mSelectPhotoAdapter = new SelectPhotoAdapter(this, mSelectedPhoto);
        rvSelectedPicture.setAdapter(mSelectPhotoAdapter);
        mSelectPhotoAdapter.setOnAddItemClickListener(new SelectPhotoAdapter.OnAddItemClickListener() {
            @Override
            public void OnAddItemClick(int position) {
                startAlbumActivity();

            }
        });
        mSelectPhotoAdapter.setOnPhotoItemClickListener(new SelectPhotoAdapter.OnPhotoItemClickListener() {
            @Override
            public void OnPhotoItemClick(int position) {

            }
        });
        mSelectPhotoAdapter.setOnPhotoItemRemoveClickListener(new SelectPhotoAdapter.OnPhotoItemRemoveClickListener() {
            @Override
            public void OnPhotoItemRemoveClick(int position) {
                mSelectedPhoto.remove(position);
                mSelectPhotoAdapter.notifyItemRemoved(position);
                mSelectPhotoAdapter.notifyItemRangeChanged(position, mSelectedPhoto.size() + 1);

                if (mSelectedPhoto.size() == 0) {
                    btSend.setBackgroundResource(R.drawable.compose_photopreview_btn);
                    btSend.setTextColor(getResources().getColor(R.color.colorGrayBlack));
                }

            }
        });
    }

    private void startAlbumActivity() {
        Intent intent = new Intent(PostBlogActivity.this, AlbumActivity.class);
        intent.putParcelableArrayListExtra("selectedPhoto", mSelectedPhoto);
        startActivityForResult(intent, 0);
    }

    private void addTextChangeListener() {
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                final int length = s.toString().length();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTextCount.setText(length + "");
                        if (length > 140) {
                            tvTextCount.setTextColor(getResources().getColor(R.color.colorOrange));
                        } else {
                            tvTextCount.setTextColor(getResources().getColor(R.color.colorBlack));
                        }
                        if (length >= 1) {
                            btSend.setBackgroundResource(R.drawable.common_button_orange_selector);
                            btSend.setTextColor(getResources().getColor(R.color.colorWhite));
                        } else {
                            btSend.setBackgroundResource(R.drawable.compose_photopreview_btn);
                            btSend.setTextColor(getResources().getColor(R.color.colorGrayBlack));
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
                if(mTag==TAG_REPOST){
                    mBlogPostService.repostBlog(statuse.getId(),etText.getText().toString(),0);
                }else {
                    int length = etText.getText().toString().length();
                    if (length == 0 && mSelectedPhoto.size() == 0) {
                        Toast.makeText(this, "请输入微博内容", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (length > 140) {
                        Toast.makeText(this, "文字不可超过140字", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (mSelectedPhoto.size() > 1) {
                        Toast.makeText(this, "由于微博API限制，只能发布一张图片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mSelectedPhoto.size() == 0) {
                        mBlogPostService.postTextBlog(etText.getText().toString(), "0.0", "0.0");
                    } else {
                        //发送文字和图片微博
                        WindowManager WM = (WindowManager) getSystemService(WINDOW_SERVICE);
                        int screenWidth = WM.getDefaultDisplay().getWidth();
                        int screenHeight = WM.getDefaultDisplay().getHeight();
                        Photo photo = mSelectedPhoto.get(0);
                        String imagePath = photo.getImagePath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(imagePath, options);
                        Bitmap bitmap;
                        if (options.outWidth > screenWidth || options.outHeight > screenHeight) {
                            options.inJustDecodeBounds = false;
                            options.inSampleSize = (int) Math.max(options.outWidth * 1f / screenWidth, options.outHeight * 1f / screenHeight);
                            try {
                                bitmap = BitmapFactory.decodeFile(imagePath, options);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "图片太大了", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            try {
                                bitmap = BitmapFactory.decodeFile(imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "图片太大了", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        mBlogPostService.postTextAndPhotoBlog(etText.getText().toString(), bitmap, "0.0", "0.0");
                    }
                }
                PostBlogActivity.this.finish();

                break;
            case R.id.iv_picture:
                if(mTag!=TAG_REPOST){
                    startAlbumActivity();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            mSelectedPhoto = data.getParcelableArrayListExtra("selectedPhoto");
            mSelectPhotoAdapter.mSelectedPhoto = mSelectedPhoto;
            if (mSelectedPhoto.size() == 0) {
                rvSelectedPicture.setVisibility(View.INVISIBLE);
            } else {
                rvSelectedPicture.setVisibility(View.VISIBLE);
                btSend.setBackgroundResource(R.drawable.common_button_orange_selector);
                btSend.setTextColor(getResources().getColor(R.color.colorWhite));
            }
            mSelectPhotoAdapter.notifyDataSetChanged();
        }
    }
}
