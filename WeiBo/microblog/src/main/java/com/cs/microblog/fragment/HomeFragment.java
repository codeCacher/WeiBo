package com.cs.microblog.fragment;

import android.graphics.Color;
import android.graphics.Typeface;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs.microblog.R;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/4/27.
 * home fragment,show the user concerned blog
 */

public class HomeFragment extends Fragment {

    private String TAG = "HomeFragment";

    private View view;
    private HomeLikeFragment mHomeLikeFragment;
    private FragmentManager mFragmentManager;
    private HomeHotFragment mHomeHotFragment;
    private ViewPager vp_blog;
    private ArrayList<Fragment> mFragmentList;
    private MyFragmentAdapter mMyFragmentAdapter;
    private TextView tv_home_like;
    private TextView tv_home_hot;
    private Typeface typeface;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        bindView();
        initData();
        initFragment();
        tv_home_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(0);
            }
        });

        tv_home_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(1);
            }
        });
        vp_blog.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    showPage(0);
                } else if (position == 1) {
                    showPage(1);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return view;
    }

    private void showPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                vp_blog.setCurrentItem(0);
                tv_home_like.setTypeface(typeface, Typeface.BOLD);
                tv_home_like.setTextColor(Color.BLACK);
                tv_home_hot.setTypeface(typeface, Typeface.NORMAL);
                tv_home_hot.setTextColor(Color.GRAY);
                break;
            case 1:
                vp_blog.setCurrentItem(1);
                tv_home_like.setTypeface(typeface, Typeface.NORMAL);
                tv_home_like.setTextColor(Color.GRAY);
                tv_home_hot.setTypeface(typeface, Typeface.BOLD);
                tv_home_hot.setTextColor(Color.BLACK);
                break;
        }
    }

    private void initData() {
        typeface = Typeface.create("homeTextType", Typeface.NORMAL);
    }

    private void initFragment() {
        mFragmentManager = getChildFragmentManager();
        mHomeLikeFragment = new HomeLikeFragment();
        mHomeHotFragment = new HomeHotFragment();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(mHomeLikeFragment);
        mFragmentList.add(mHomeHotFragment);
        mMyFragmentAdapter = new MyFragmentAdapter(mFragmentManager, mFragmentList);
        vp_blog.setAdapter(mMyFragmentAdapter);
    }

    /**
     * bound view in home fragment
     */
    private void bindView() {
        vp_blog = (ViewPager) view.findViewById(R.id.vp_blog);
        tv_home_like = (TextView) view.findViewById(R.id.tv_home_like);
        tv_home_hot = (TextView) view.findViewById(R.id.tv_home_hot);

        showPage(0);
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragmentList;

        MyFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
