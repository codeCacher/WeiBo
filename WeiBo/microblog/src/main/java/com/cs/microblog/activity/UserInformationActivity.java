package com.cs.microblog.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.bean.User;
import com.cs.microblog.fragment.UserBlogFragment;
import com.cs.microblog.fragment.UserHomeFragment;
import com.cs.microblog.fragment.UserPhotoFragment;
import com.cs.microblog.view.StickyNavLayout;
import com.cs.microblog.view.ViewPagerIndicator;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/19.
 */

public class UserInformationActivity extends AppCompatActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.id_stickynavlayout_topview)
    SimpleDraweeView ivBackImage;
    @BindView(R.id.rl_indicator)
    RelativeLayout rlIndicator;
    @BindView(R.id.id_stickynavlayout_viewpager)
    ViewPager vpContent;
    @BindView(R.id.id_stickynavlayout_indicator)
    ViewPagerIndicator blvpiIndicator;
    @BindView(R.id.sn_root)
    StickyNavLayout snRoot;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.v_top_bottom_line)
    View vTopBottomLine;
    @BindView(R.id.id_stickynavlayout_toolbar)
    RelativeLayout rl_toolbar;
    @BindView(R.id.v_cut)
    View vCut;
    @BindView(R.id.rl_count)
    RelativeLayout rlCount;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.sdv_profile_image)
    SimpleDraweeView sdvProfileImage;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.tv_friends_count)
    TextView tvFriendsCount;
    @BindView(R.id.tv_followers_count)
    TextView tvFollowersCount;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private User mUser;
    private ArrayList<Fragment> mFragmentList;
    private ArrayList<View.OnClickListener> mTitlesOnClickListenterList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infor);
        ButterKnife.bind(this);

        mUser = getIntent().getParcelableExtra("user");
        initTop();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new UserHomeFragment(mUser));
        mFragmentList.add(new UserBlogFragment());
        mFragmentList.add(new UserPhotoFragment());
        vpContent.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
    }

    private void initTop() {
        if (mUser != null) {
            ivBackImage.setImageURI(mUser.getCover_image_phone());
            sdvProfileImage.setImageURI(mUser.getAvatar_large());
            tvName.setText(mUser.getName());
            tvFriendsCount.setText("关注：" + mUser.getFriends_count());
            tvFollowersCount.setText("粉丝：" + mUser.getFollowers_count());
            if (mUser.isVerified()) {
                tvIntroduce.setText("微博认证：" + mUser.getVerified_reason());
            } else {
                tvIntroduce.setText("简介：" + mUser.getDescription());
            }
        } else {
            ivBackImage.setImageResource(R.drawable.user_info_back_image);
        }

        snRoot.init(rl_toolbar.getLayoutParams().height, rlRoot);
        snRoot.setOnTopHideListener(new StickyNavLayout.OnTopHideListener() {
            @Override
            public void OnTopHide() {
                rl_toolbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                vTopBottomLine.setBackgroundColor(getResources().getColor(R.color.colorGrayWhite));
                ivBack.setImageResource(R.drawable.navigationbar_back);
                ivSearch.setImageResource(R.drawable.searchbar_icon_on_banner_normal);
                ivMore.setImageResource(R.drawable.navigationbar_more);
                if(mUser!=null){
                    tvTitle.setText(mUser.getName());
                }
            }
        });
        snRoot.setOnTopShowListener(new StickyNavLayout.OnTopShowListener() {
            @Override
            public void OnTopShow() {
                rl_toolbar.setBackgroundColor(getResources().getColor(R.color.translucent));
                vTopBottomLine.setBackgroundColor(getResources().getColor(R.color.translucent));
                ivBack.setImageResource(R.drawable.btn_back_n);
                ivSearch.setImageResource(R.drawable.searchbar_second_textfield_search_icon);
                ivMore.setImageResource(R.drawable.toolbar_more);
                tvTitle.setText("");
            }
        });

        mTitlesOnClickListenterList = new ArrayList<>();
        View.OnClickListener homeOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpContent.setCurrentItem(0);
            }
        };
        View.OnClickListener blogOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpContent.setCurrentItem(1);
            }
        };
        View.OnClickListener photoOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpContent.setCurrentItem(2);
            }
        };
        mTitlesOnClickListenterList.add(homeOnClickListener);
        mTitlesOnClickListenterList.add(blogOnClickListener);
        mTitlesOnClickListenterList.add(photoOnClickListener);
        blvpiIndicator.initIndicator(new String[]{"主页", "微博", "相册"}, mTitlesOnClickListenterList, vpContent);
    }

}
