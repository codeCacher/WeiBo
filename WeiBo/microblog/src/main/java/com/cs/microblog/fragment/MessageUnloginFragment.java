package com.cs.microblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cs.microblog.R;
import com.cs.microblog.activity.WebViewActivity;
import com.cs.microblog.custom.Constants;

/**
 * Created by Administrator on 2017/6/3.
 */

public class MessageUnloginFragment extends Fragment {

    private Button bt_login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_message_unlogin, container,false);

        bt_login = (Button) view.findViewById(R.id.bt_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.AUTHORIZE_URL + "?" + Constants.KEY_CLIENT_ID + "=" + Constants.APP_KEY + "&" + Constants.KEY_REDIRECT_URI + "=" + Constants.REDIRECT_URI;
                WebViewActivity.openUrl(getContext(),url);
            }
        });
        return view;
    }
}
