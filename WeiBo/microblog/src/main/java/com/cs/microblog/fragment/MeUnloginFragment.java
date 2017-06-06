package com.cs.microblog.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cs.microblog.R;

/**
 * Created by Administrator on 2017/6/4.
 */

public class MeUnloginFragment extends Fragment {

    private ImageView iv_back_image;
    private RelativeLayout rl_scroll;
    private int mBackImageHeight;
    private float startY;
    private int mDefaultBackImageHeight = 0;
    private int mMaxBackImageHeight;
    private ViewGroup.LayoutParams mBackImageLayoutParams;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_me_unlogin, container, false);

        rl_scroll = (RelativeLayout) view.findViewById(R.id.rl_scroll);
        iv_back_image = (ImageView) view.findViewById(R.id.iv_back_image);

        Bitmap backImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_info_back_image);

        WindowManager WM = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        final int screenWidth = WM.getDefaultDisplay().getWidth();
        float hightScale = 1f * screenWidth / backImageBitmap.getWidth();

        mMaxBackImageHeight = (int) (backImageBitmap.getHeight() * hightScale);

        rl_scroll.setOnTouchListener(new ViewGroup.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mBackImageLayoutParams == null) {
                            mBackImageLayoutParams = iv_back_image.getLayoutParams();
                        }
                        if (mDefaultBackImageHeight == 0) {
                            mDefaultBackImageHeight = iv_back_image.getHeight();
                        }
                        mBackImageHeight = iv_back_image.getHeight();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dY = event.getY() - startY;
                        startY = event.getY();
                        if ((mBackImageHeight <= mDefaultBackImageHeight && dY < 0) || (mBackImageHeight >= mMaxBackImageHeight && dY > 0)) {
                            return true;
                        } else {
                            if (mBackImageHeight + dY <= mDefaultBackImageHeight) {
                                mBackImageHeight = mDefaultBackImageHeight;
                                mBackImageLayoutParams.height = mDefaultBackImageHeight;
                                iv_back_image.setLayoutParams(mBackImageLayoutParams);
                            } else if (mBackImageHeight + dY >= mMaxBackImageHeight) {
                                mBackImageHeight = mMaxBackImageHeight;
                                mBackImageLayoutParams.height = mMaxBackImageHeight;
                                iv_back_image.setLayoutParams(mBackImageLayoutParams);
                            } else {
                                mBackImageHeight = (int) (mBackImageHeight + dY / 2);
                                mBackImageLayoutParams.height = mBackImageHeight;
                                iv_back_image.setLayoutParams(mBackImageLayoutParams);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        final int moveDy = (mBackImageHeight - mDefaultBackImageHeight) / 10;
                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                iv_back_image.setLayoutParams(mBackImageLayoutParams);
                            }
                        };
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < 20; i++) {
                                    mBackImageHeight = mBackImageHeight - moveDy;
                                    if (mBackImageHeight < mDefaultBackImageHeight) {
                                        mBackImageHeight = mDefaultBackImageHeight;
                                    }
                                    mBackImageLayoutParams.height = mBackImageHeight;
                                    handler.sendEmptyMessage(0);
                                    try {
                                        Thread.sleep(5);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                        mBackImageLayoutParams.height = mDefaultBackImageHeight;
                                        handler.sendEmptyMessage(0);
                                        return;
                                    }
                                }
                                if (mBackImageHeight > mDefaultBackImageHeight) {
                                    mBackImageLayoutParams.height = mDefaultBackImageHeight;
                                    handler.sendEmptyMessage(0);
                                }
                            }
                        }).start();

                        break;
                }
                return true;
            }
        });
        return view;
    }
}
