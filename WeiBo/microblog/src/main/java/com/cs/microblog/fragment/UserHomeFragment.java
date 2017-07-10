package com.cs.microblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cs.microblog.R;
import com.cs.microblog.adapter.UserHomeAdapter;
import com.cs.microblog.bean.HomeTimelineList;
import com.cs.microblog.bean.User;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.utils.SharedPreferencesUtils;
import com.cs.microblog.utils.WeiBoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import rx.functions.Action1;

public class UserHomeFragment extends Fragment {
    @BindView(R.id.rv_content)
    RecyclerView rvContent;

    User user;

    public UserHomeFragment(User user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tap_neednt_refresh, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvContent.setLayoutManager(linearLayoutManager);
        UserHomeAdapter userHomeAdapter = new UserHomeAdapter(getContext(),user);
        rvContent.setAdapter(userHomeAdapter);
        return view;
    }
}
