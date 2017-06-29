package com.cs.microblog.fragment;

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

import com.cs.microblog.R;
import com.cs.microblog.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;


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
    private Typeface typeface;
    private ViewPagerIndicator blvpi_indicator;
    private List<View.OnClickListener> mTitlesOnClickListenterList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        bindView();
        initData();
        initFragment();
        showPage(0);
        return view;
    }

    private void showPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                vp_blog.setCurrentItem(0);
                break;
            case 1:
                vp_blog.setCurrentItem(1);
                break;
        }
    }

    private void initData() {
        typeface = Typeface.create("homeTextType", Typeface.NORMAL);

        mTitlesOnClickListenterList = new ArrayList<>();
        View.OnClickListener likeOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(0);
            }
        };
        View.OnClickListener hotOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(1);
            }
        };
        mTitlesOnClickListenterList.add(likeOnClickListener);
        mTitlesOnClickListenterList.add(hotOnClickListener);
        blvpi_indicator.initIndicator(new String[]{"关注","热门"},mTitlesOnClickListenterList,vp_blog);
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
        blvpi_indicator = (ViewPagerIndicator) view.findViewById(R.id.blvpi_indicator);

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
