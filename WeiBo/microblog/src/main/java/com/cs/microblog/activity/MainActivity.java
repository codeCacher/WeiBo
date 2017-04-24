package com.cs.microblog.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cs.microblog.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/23.
 */

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BindViews();
        SetItemOnTouchListener();


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
     * @param event touch event
     */
    private void BottomBarHomeTouchProcess(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                CancelSelectAll();
                iv_home.setSelected(true);
                break;
        }
    }

    /**
     * Process the message item touch event
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

}
