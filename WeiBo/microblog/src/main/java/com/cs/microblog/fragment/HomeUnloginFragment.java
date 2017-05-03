package com.cs.microblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.activity.WebViewActivity;
import com.cs.microblog.custom.Constants;

/**
 * Created by Administrator on 2017/4/25.
 */

public class HomeUnloginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_unlogin, container,false);
        TextView tv_login = (TextView) view.findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.AUTHORIZE_URL + "?" + Constants.KEY_CLIENT_ID + "=" + Constants.APP_KEY + "&" + Constants.KEY_REDIRECT_URI + "=" + Constants.REDIRECT_URI;
                WebViewActivity.openUrl(getContext(),url);
            }
        });
        return view;
    }
}
