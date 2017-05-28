package com.cs.microblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.activity.WebViewActivity;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.view.ImageWithType;

/**
 * Created by Administrator on 2017/4/25.
 */

public class HomeUnloginFragment extends Fragment {
    private View view;
    private ImageView iv_span;
    private TextView tv_login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_unlogin, container,false);
        bindView();
        setRotateAnimation();
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.AUTHORIZE_URL + "?" + Constants.KEY_CLIENT_ID + "=" + Constants.APP_KEY + "&" + Constants.KEY_REDIRECT_URI + "=" + Constants.REDIRECT_URI;
                WebViewActivity.openUrl(getContext(),url);
            }
        });
        return view;
    }
    private void setRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0,359,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(20000);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        iv_span.startAnimation(rotateAnimation);
    }

    private void bindView() {
        Button bt_tofollow = (Button) view.findViewById(R.id.bt_tofollow);
        iv_span = (ImageView) view.findViewById(R.id.iv_span);
        tv_login = (TextView) view.findViewById(R.id.tv_login);
    }
}
