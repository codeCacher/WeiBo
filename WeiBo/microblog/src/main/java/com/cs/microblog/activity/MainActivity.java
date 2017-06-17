package com.cs.microblog.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.fragment.HomeFragment;
import com.cs.microblog.fragment.HomeUnloginFragment;
import com.cs.microblog.fragment.MeUnloginFragment;
import com.cs.microblog.fragment.MessageUnloginFragment;
import com.cs.microblog.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/23.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.ll_fragment)
    LinearLayout ll_fragment;
    @BindView(R.id.iv_home)
    ImageView iv_home;
    @BindView(R.id.tv_home)
    TextView tv_home;
    @BindView(R.id.rl_home)
    RelativeLayout rl_home;
    @BindView(R.id.iv_message)
    ImageView iv_message;
    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.rl_message)
    RelativeLayout rl_message;
    @BindView(R.id.iv_add)
    ImageView iv_add;
    @BindView(R.id.rl_add)
    RelativeLayout rl_add;
    @BindView(R.id.iv_discover)
    ImageView iv_discover;
    @BindView(R.id.tv_discover)
    TextView tv_discover;
    @BindView(R.id.rl_discover)
    RelativeLayout rl_discover;
    @BindView(R.id.iv_me)
    ImageView iv_me;
    @BindView(R.id.tv_me)
    TextView tv_me;
    @BindView(R.id.rl_me)
    RelativeLayout rl_me;
    @BindView(R.id.rl_main)
    RelativeLayout rl_main;

    private List<ImageView> imageViewList;
    private HomeUnloginFragment mFragmentUnloginHome;
    private FragmentManager mFragmentManager;

    private HomeFragment mHomeFragment;
    private boolean isLogin;
    private MessageUnloginFragment mFragmentUnloginMessage;
    private MeUnloginFragment mFragmentUnloginMe;
    private FragmentTransaction mFragmentTransaction;
    private ArrayList<Fragment> FragmentList;
    private int FragmentShowIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        BindViews();
        mFragmentManager = getSupportFragmentManager();
        FragmentList = new ArrayList<>();
        SetItemOnClickedListener();

        //get the token from sp
        String token = SharedPreferencesUtils.getString(this, Constants.KEY_ACCESS_TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            isLogin = false;
            showUnloginHomeFragment();
        } else {
            isLogin = true;
            Log.i(TAG, token);
            showHomeFragment();
        }
    }

    private void showHomeFragment() {
        CancelSelectAll();
        iv_home.setSelected(true);
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            if (FragmentList.size() != 0) {
                mFragmentTransaction.hide(FragmentList.get(FragmentShowIndex));
            }
            mFragmentTransaction.add(R.id.ll_fragment, mHomeFragment);
            mFragmentTransaction.commit();

            FragmentShowIndex = FragmentList.size();
            FragmentList.add(mHomeFragment);
        } else {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(FragmentList.get(FragmentShowIndex));
            mFragmentTransaction.show(mHomeFragment);
            mFragmentTransaction.commit();

            FragmentShowIndex = FragmentList.indexOf(mHomeFragment);
        }
    }

    private void showUnloginHomeFragment() {
        CancelSelectAll();
        iv_home.setSelected(true);
        if (mFragmentUnloginHome == null) {
            mFragmentUnloginHome = new HomeUnloginFragment();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            if (FragmentList.size() != 0) {
                mFragmentTransaction.hide(FragmentList.get(FragmentShowIndex));
            }
            mFragmentTransaction.add(R.id.ll_fragment, mFragmentUnloginHome);
            mFragmentTransaction.commit();

            FragmentShowIndex = FragmentList.size();
            FragmentList.add(mFragmentUnloginHome);
        } else {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(FragmentList.get(FragmentShowIndex));
            mFragmentTransaction.show(mFragmentUnloginHome);
            mFragmentTransaction.commit();

            FragmentShowIndex = FragmentList.indexOf(mFragmentUnloginHome);
        }
    }

    private void showUnloginMessageFragment() {
        CancelSelectAll();
        iv_message.setSelected(true);
        if (mFragmentUnloginMessage == null) {
            mFragmentUnloginMessage = new MessageUnloginFragment();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            if (FragmentList.size() != 0) {
                mFragmentTransaction.hide(FragmentList.get(FragmentShowIndex));
            }
            mFragmentTransaction.add(R.id.ll_fragment, mFragmentUnloginMessage);
            mFragmentTransaction.commit();

            FragmentShowIndex = FragmentList.size();
            FragmentList.add(mFragmentUnloginMessage);
        } else {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(FragmentList.get(FragmentShowIndex));
            mFragmentTransaction.show(mFragmentUnloginMessage);
            mFragmentTransaction.commit();

            FragmentShowIndex = FragmentList.indexOf(mFragmentUnloginMessage);
        }
    }

    private void showUnloginMeFragment() {
        CancelSelectAll();
        iv_me.setSelected(true);
        if (mFragmentUnloginMe == null) {
            mFragmentUnloginMe = new MeUnloginFragment();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            if (FragmentList.size() != 0) {
                mFragmentTransaction.hide(FragmentList.get(FragmentShowIndex));
            }
            mFragmentTransaction.add(R.id.ll_fragment, mFragmentUnloginMe);
            mFragmentTransaction.commit();

            FragmentShowIndex = FragmentList.size();
            FragmentList.add(mFragmentUnloginMe);
        } else {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.hide(FragmentList.get(FragmentShowIndex));
            mFragmentTransaction.show(mFragmentUnloginMe);
            mFragmentTransaction.commit();

            FragmentShowIndex = FragmentList.indexOf(mFragmentUnloginMe);
        }
    }

    /**
     * set the item Relative Layout On Touch Listener
     */
    private void SetItemOnClickedListener() {
        rl_home.setOnClickListener(this);
        rl_message.setOnClickListener(this);
        rl_discover.setOnClickListener(this);
        rl_me.setOnClickListener(this);
        iv_add.setOnClickListener(this);
    }

    /**
     * Bind the View through id,and add to the ArrayList
     */
    private void BindViews() {
        imageViewList = new ArrayList<>();
        imageViewList.add(iv_home);
        imageViewList.add(iv_message);
        imageViewList.add(iv_discover);
        imageViewList.add(iv_me);
        imageViewList.add(iv_add);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                BottomBarAddTouchProcess();
                break;
            case R.id.rl_home:
                BottomBarHomeTouchProcess();
                break;
            case R.id.rl_message:
                BottomBarMessageTouchProcess();
                break;
            case R.id.rl_discover:
                BottomBarDiscoverTouchProcess();
                break;
            case R.id.rl_me:
                BottomBarMeTouchProcess();
                break;
        }
    }

    /**
     * Process the home item touch event
     */
    private void BottomBarHomeTouchProcess() {
        if (isLogin) {
            showHomeFragment();
        } else {
            showUnloginHomeFragment();
        }
    }

    /**
     * Process the message item touch event
     */
    private void BottomBarMessageTouchProcess() {
        showUnloginMessageFragment();
    }

    /**
     * Process the discover item touch event
     */
    private void BottomBarDiscoverTouchProcess() {
        CancelSelectAll();
        iv_discover.setSelected(true);
    }

    /**
     * Process the me item touch event
     */
    private void BottomBarMeTouchProcess() {
        showUnloginMeFragment();
    }

    /**
     * Process the add item touch event
     */
    private void BottomBarAddTouchProcess() {
        AddMoreActivity.startAddMoreActivity(this);
    }


    /**
     * disselect all item
     */
    private void CancelSelectAll() {
        for (ImageView iv :
                imageViewList) {
            iv.setSelected(false);
        }
    }


    public static void openMainActivity(Context context, int flag) {
        context.startActivity(new Intent(context, MainActivity.class).setFlags(flag));
    }


    @Override
    public void onBackPressed() {
        //TODO 退出到桌面而不结束该页面
    }
}
