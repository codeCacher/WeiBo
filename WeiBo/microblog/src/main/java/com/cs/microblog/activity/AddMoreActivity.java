package com.cs.microblog.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cs.microblog.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/10.
 */

public class AddMoreActivity extends Activity implements View.OnClickListener {


    @BindView(R.id.vp_item)
    ViewPager vpItem;
    @BindView(R.id.iv_dot_left)
    ImageView ivDotLeft;
    @BindView(R.id.iv_dot_right)
    ImageView ivDotRight;
    @BindView(R.id.ll_dot)
    LinearLayout llDot;
    @BindView(R.id.iv_exit)
    ImageView ivExit;
    @BindView(R.id.rl_item)
    RelativeLayout rlItem;
    @BindView(R.id.rl_root)
    LinearLayout rlRoot;
    @BindView(R.id.iv_slogan)
    ImageView ivSlogan;
    private List<View> mViewList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more);
        ButterKnife.bind(this);

        setStartAnimation();

        initViewPager();
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEndAnimation(new CallBack() {
                    @Override
                    public void onFinished() {
                        AddMoreActivity.this.finish();
                    }
                });
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        setEndAnimation(new CallBack() {
            @Override
            public void onFinished() {
                AddMoreActivity.this.finish();
            }
        });
    }

    private void initViewPager() {

        mViewList = new ArrayList<>();
        View pageView1 = View.inflate(this, R.layout.add_more_page_one, null);
        View pageView2 = View.inflate(this, R.layout.add_more_page_two, null);
        mViewList.add(pageView1);
        mViewList.add(pageView2);
        setItemOnClickListener(pageView1, pageView1);
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        vpItem.setAdapter(myViewPagerAdapter);
        vpItem.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ivDotLeft.setImageResource(R.drawable.compose_color_yellow);
                    ivDotRight.setImageResource(R.drawable.compose_color_gray);
                } else if (position == 1) {
                    ivDotLeft.setImageResource(R.drawable.compose_color_gray);
                    ivDotRight.setImageResource(R.drawable.compose_color_yellow);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setItemOnClickListener(View pageView1, View view1) {
        View postText = pageView1.findViewById(R.id.post_text);
        View postImage = pageView1.findViewById(R.id.post_image);
        View postPhoto = pageView1.findViewById(R.id.post_photo);
        postText.setOnClickListener(this);
        postImage.setOnClickListener(this);
        postPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        setEndAnimation(new CallBack() {
            @Override
            public void onFinished() {
                switch (v.getId()) {
                    case R.id.post_text:
                        AddMoreActivity.this.finish();
                        Intent postTextIntent = new Intent(AddMoreActivity.this, PostBlogActivity.class);
                        postTextIntent.putExtra("TAG", PostBlogActivity.TAG_POST_TEXT);
                        startActivity(postTextIntent);
                        break;
                    case R.id.post_image:
                        AddMoreActivity.this.finish();
                        Intent imageIntent = new Intent(AddMoreActivity.this, PostBlogActivity.class);
                        imageIntent.putExtra("TAG", PostBlogActivity.TAG_POST_IMAGE);
                        startActivity(imageIntent);
                        break;
                    case R.id.post_photo:
                        AddMoreActivity.this.finish();
                        Intent photoIntent = new Intent(AddMoreActivity.this, PostBlogActivity.class);
                        photoIntent.putExtra("TAG", PostBlogActivity.TAG_POST_PHOTO);
                        startActivity(photoIntent);
                        break;
                }
            }
        });
    }

    private class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }

    private void setStartAnimation() {
        AlphaAnimation alphaAm = new AlphaAnimation(0, 1);
        alphaAm.setDuration(500);
        LinearInterpolator linearInterpolator = new LinearInterpolator();

        TranslateAnimation tranAm0 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_PARENT, 1,
                Animation.RELATIVE_TO_SELF, -0.05f);
        tranAm0.setDuration(500);

        tranAm0.setInterpolator(linearInterpolator);

        TranslateAnimation tranAm1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -0.05f,
                Animation.RELATIVE_TO_SELF, 0);
        tranAm1.setDuration(500);

        tranAm1.setInterpolator(linearInterpolator);

        AnimationSet amSet = new AnimationSet(false);
        amSet.addAnimation(tranAm0);
        amSet.addAnimation(tranAm1);

        rlRoot.startAnimation(alphaAm);
        rlItem.startAnimation(amSet);
    }

    public static void startAddMoreActivity(Context context) {
        Intent intent = new Intent(context, AddMoreActivity.class);
        context.startActivity(intent);
    }

    interface CallBack {
        void onFinished();
    }

    private void setEndAnimation(final CallBack callBack) {
        AlphaAnimation alphaAm = new AlphaAnimation(1, 0);
        alphaAm.setDuration(500);
        alphaAm.setFillAfter(true);

        TranslateAnimation tranAm0 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_PARENT, 1);
        tranAm0.setDuration(500);
        tranAm0.setFillAfter(true);

        rlRoot.startAnimation(alphaAm);
        rlItem.startAnimation(tranAm0);

        alphaAm.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callBack.onFinished();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
