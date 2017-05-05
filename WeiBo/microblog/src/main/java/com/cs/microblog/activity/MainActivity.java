package com.cs.microblog.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cs.microblog.R;
import com.cs.microblog.custom.AccessTokenEntity;
import com.cs.microblog.custom.AccessTokenService;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.custom.GetHomeTimelineService;
import com.cs.microblog.custom.HomeTimelineList;
import com.cs.microblog.custom.Statuse;
import com.cs.microblog.fragment.HomeFragment;
import com.cs.microblog.fragment.HomeUnloginFragment;
import com.cs.microblog.utils.SharedPreferencesUtils;
import com.cs.microblog.utils.WeiBoUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/4/23.
 */

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "MainActivity";
    private ImageView iv_home;
    private ImageView iv_message;
    private ImageView iv_discover;
    private ImageView iv_me;
    private ImageView iv_add;
    private RelativeLayout rl_home;
    private List<ImageView> imageViewList;
    private RelativeLayout rl_message;
    private RelativeLayout rl_discover;
    private RelativeLayout rl_me;
    private HomeUnloginFragment mFragmentUnloginHome;
    private FragmentManager mFragmentManager;

    private HomeFragment mHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BindViews();
        SetItemOnTouchListener();

        //get the token from sp
        String token = SharedPreferencesUtils.getString(this, Constants.KEY_ACCESS_TOKEN, "");
        if (TextUtils.isEmpty(token)) {
            showUnloginHomeFragment();
        } else {
            Log.i(TAG, token);
            showHomeFragment();
        }
    }

    private void showHomeFragment() {
        CancelSelectAll();
        iv_home.setSelected(true);
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            mFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentHomeTransaction = mFragmentManager.beginTransaction();
            fragmentHomeTransaction.replace(R.id.ll_fragment, mHomeFragment);
            fragmentHomeTransaction.commit();
        } else {
            FragmentTransaction fragmentHomeTransaction = mFragmentManager.beginTransaction();
            fragmentHomeTransaction.add(R.id.ll_fragment, mHomeFragment);
            fragmentHomeTransaction.commit();
        }
    }

    private void showUnloginHomeFragment() {
        CancelSelectAll();
        iv_home.setSelected(true);
        if (mFragmentUnloginHome == null) {
            mFragmentUnloginHome = new HomeUnloginFragment();
            mFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentHomeTransaction = mFragmentManager.beginTransaction();
            fragmentHomeTransaction.add(R.id.ll_fragment, mFragmentUnloginHome);
            fragmentHomeTransaction.commit();
        } else {
            FragmentTransaction fragmentHomeTransaction = mFragmentManager.beginTransaction();
            fragmentHomeTransaction.show(mFragmentUnloginHome);
            fragmentHomeTransaction.commit();
        }

    }

    /**
     * set the item Relative Layout On Touch Listener
     */
    private void SetItemOnTouchListener() {
        rl_home.setOnTouchListener(this);
        rl_message.setOnTouchListener(this);
        rl_discover.setOnTouchListener(this);
        rl_me.setOnTouchListener(this);
        iv_add.setOnTouchListener(this);
    }

    /**
     * Bind the View through id,and add to the ArrayList
     */
    private void BindViews() {
        imageViewList = new ArrayList<>();
        iv_home = (ImageView) findViewById(R.id.iv_home);
        imageViewList.add(iv_home);
        iv_message = (ImageView) findViewById(R.id.iv_message);
        imageViewList.add(iv_message);
        iv_discover = (ImageView) findViewById(R.id.iv_discover);
        imageViewList.add(iv_discover);
        iv_me = (ImageView) findViewById(R.id.iv_me);
        imageViewList.add(iv_me);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        imageViewList.add(iv_add);

        rl_home = (RelativeLayout) findViewById(R.id.rl_home);
        rl_message = (RelativeLayout) findViewById(R.id.rl_message);
        rl_discover = (RelativeLayout) findViewById(R.id.rl_discover);
        rl_me = (RelativeLayout) findViewById(R.id.rl_me);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.iv_add:
                BottomBarAddTouchProcess(event);
                break;
            case R.id.rl_home:
                BottomBarHomeTouchProcess(event);
                break;
            case R.id.rl_message:
                BottomBarMessageTouchProcess(event);
                break;
            case R.id.rl_discover:
                BottomBarDiscoverTouchProcess(event);
                break;
            case R.id.rl_me:
                BottomBarMeTouchProcess(event);
                break;
        }

        return true;
    }

    /**
     * Process the home item touch event
     *
     * @param event touch event
     */
    private void BottomBarHomeTouchProcess(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                showUnloginHomeFragment();
                break;
        }
    }

    /**
     * Process the message item touch event
     *
     * @param event touch event
     */
    private void BottomBarMessageTouchProcess(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                CancelSelectAll();
                iv_message.setSelected(true);
                break;
        }
    }

    /**
     * Process the discover item touch event
     *
     * @param event touch event
     */
    private void BottomBarDiscoverTouchProcess(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                CancelSelectAll();
                iv_discover.setSelected(true);
                break;
        }
    }

    /**
     * Process the me item touch event
     *
     * @param event touch event
     */
    private void BottomBarMeTouchProcess(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                CancelSelectAll();
                iv_me.setSelected(true);
                break;
        }
    }

    /**
     * Process the add item touch event
     *
     * @param event touch event
     */
    private void BottomBarAddTouchProcess(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                iv_add.setPressed(true);
                break;
            case MotionEvent.ACTION_UP:
                iv_add.setPressed(false);
                break;
        }
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

}
