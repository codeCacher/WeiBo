package com.cs.microblog.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cs.microblog.R;
import com.cs.microblog.adapter.AdapterBlogItem;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.custom.HomeTimelineList;
import com.cs.microblog.custom.Statuse;
import com.cs.microblog.utils.SharedPreferencesUtils;
import com.cs.microblog.utils.WeiBoUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

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
        final RecyclerView rv_homeblog = (RecyclerView) view.findViewById(R.id.rv_homeblog);
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);

        rv_homeblog.setAdapter(new AdapterBlogItem(getContext(), statuse));
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_homeblog.setLayoutManager(linearLayoutManager);
        rv_homeblog.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                            @Override
                                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                                super.onScrollStateChanged(recyclerView, newState);
                                            }

                                            @Override
                                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                                super.onScrolled(recyclerView, dx, dy);
                                                int itemCount = recyclerView.getAdapter().getItemCount();
                                                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                                                if (lastVisibleItemPosition + 4 == itemCount && dy > 0) {
                                                    WeiBoUtils.getHomeTimelineLists(SharedPreferencesUtils.getString(getContext(),
                                                            Constants.KEY_ACCESS_TOKEN, ""), statuse.get(statuse.size() - 1).getId(), new WeiBoUtils.CallBack() {
                                                        @Override
                                                        public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                                                            statuse.addAll(response.body().getStatuses());
                                                            rv_homeblog.refreshDrawableState();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<HomeTimelineList> call, Throwable t) {

                                                        }
                                                    });
                                                }
                                            }
                                        });

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WeiBoUtils.getHomeTimelineLists(SharedPreferencesUtils.getString(getContext(),
                        Constants.KEY_ACCESS_TOKEN, ""), 0, new WeiBoUtils.CallBack() {
                    @Override
                    public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                        statuse = response.body().getStatuses();
                        rv_homeblog.setAdapter(new AdapterBlogItem(getContext(), statuse));
                        srl.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<HomeTimelineList> call, Throwable t) {
                    }
                });
            }
        });

        return view;
    }
}
