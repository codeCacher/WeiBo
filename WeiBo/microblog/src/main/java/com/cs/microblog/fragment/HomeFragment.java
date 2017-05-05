package com.cs.microblog.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.cs.microblog.adapter.BlogItemAdapter;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.custom.HomeTimelineList;
import com.cs.microblog.custom.Statuse;
import com.cs.microblog.utils.SharedPreferencesUtils;
import com.cs.microblog.utils.WeiBoUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by Administrator on 2017/4/27.
 */

public class HomeFragment extends Fragment {

    private String TAG = "HomeFragment";

    private ArrayList<Statuse> statuse = new ArrayList<>();
    private int itemCount;
    private int RecyclerViewScrollState;
    private View view;
    private RecyclerView rv_homeblog;
    private SwipeRefreshLayout srl;
    private LinearLayoutManager mLinearLayoutManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            initRecyclerView();
            srl.setRefreshing(false);
        }
    };

    private void initRecyclerView() {
        rv_homeblog.setAdapter(new BlogItemAdapter(getContext(), statuse));
        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_homeblog.setLayoutManager(mLinearLayoutManager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        boundView();

        srl.setRefreshing(true);

        WeiBoUtils.getHomeTimelineLists(SharedPreferencesUtils.getString(getContext(),
                Constants.KEY_ACCESS_TOKEN, ""), 0, new WeiBoUtils.CallBack() {
            @Override
            public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                statuse.clear();
                statuse.addAll(response.body().getStatuses());
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Call<HomeTimelineList> call, Throwable t) {
                Toast.makeText(getContext(), "没有网络了", Toast.LENGTH_SHORT).show();
                srl.setRefreshing(false);
            }
        });

        rv_homeblog.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i(TAG, "State:" + newState);
                if (newState == SCROLL_STATE_IDLE && RecyclerViewScrollState == 1) {
                    RecyclerViewScrollState = 0;
                    WeiBoUtils.getHomeTimelineLists(SharedPreferencesUtils.getString(getContext(),
                            Constants.KEY_ACCESS_TOKEN, ""), statuse.get(statuse.size() - 1).getId() - 1, new WeiBoUtils.CallBack() {
                        @Override
                        public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                            statuse.addAll(response.body().getStatuses());
                            rv_homeblog.getAdapter().notifyDataSetChanged();
                            rv_homeblog.refreshDrawableState();
                        }

                        @Override
                        public void onFailure(Call<HomeTimelineList> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                itemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == itemCount && dy > 0) {
                    RecyclerViewScrollState = 1;
                }
            }
        });

        //The drop-down refresh
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WeiBoUtils.getHomeTimelineLists(SharedPreferencesUtils.getString(getContext(),
                        Constants.KEY_ACCESS_TOKEN, ""), 0, new WeiBoUtils.CallBack() {
                    @Override
                    public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                        statuse.clear();
                        statuse.addAll(response.body().getStatuses());
                        if (rv_homeblog.getAdapter() == null) {
                            initRecyclerView();
                        } else {
                            rv_homeblog.getAdapter().notifyDataSetChanged();
                            rv_homeblog.refreshDrawableState();
                        }
                        srl.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<HomeTimelineList> call, Throwable t) {
                        Toast.makeText(getContext(), "没有网络了", Toast.LENGTH_SHORT).show();
                        srl.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }

    private void boundView() {
        rv_homeblog = (RecyclerView) view.findViewById(R.id.rv_homeblog);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
    }
}
