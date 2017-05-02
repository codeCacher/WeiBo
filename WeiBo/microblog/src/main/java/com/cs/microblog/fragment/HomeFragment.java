package com.cs.microblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs.microblog.R;
import com.cs.microblog.adapter.AdapterBlogItem;
import com.cs.microblog.custom.Statuse;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/27.
 */

public class HomeFragment extends Fragment {

    private ArrayList<Statuse> statuse;

    public HomeFragment(ArrayList<Statuse> statuse) {
        this.statuse = statuse;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView rv_homeblog = (RecyclerView) view.findViewById(R.id.rv_homeblog);
        rv_homeblog.setAdapter(new AdapterBlogItem(getContext(),statuse));
        rv_homeblog.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        return view;
    }
}
