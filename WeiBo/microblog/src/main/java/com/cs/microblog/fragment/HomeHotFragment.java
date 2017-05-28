package com.cs.microblog.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

/**
 * Created by Administrator on 2017/4/27.
 * home fragment,show the user concerned blog
 */

public class HomeHotFragment extends Fragment {

    private static final int DEFAULT_COUNT = 50;
    private int publicBlogCount = 0;

    private String TAG = "HomeHotFragment";

    private ArrayList<Statuse> statuse = new ArrayList<>();
    private int itemCount;
    private View view;
    private RecyclerView rv_homeblog;
    private SwipeRefreshLayout srl;
    private LinearLayoutManager mLinearLayoutManager;
    /**
     * add more refresh state
     */
    private boolean mIsRefreshing;
    private BlogItemAdapter mBlogItemAdapter;
    private ImageView iv_loading;
    private TextView tv_foot_text;
    private View mFootView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            initRecyclerView();
            srl.setRefreshing(false);
        }
    };


    private void initRecyclerView() {
        mBlogItemAdapter = new BlogItemAdapter(getContext(), rv_homeblog, statuse);
        rv_homeblog.setAdapter(mBlogItemAdapter);
        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_homeblog.setLayoutManager(mLinearLayoutManager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_like, container, false);

        boundView();

        srl.setRefreshing(true);
        mIsRefreshing = false;

        WeiBoUtils.getPublicTimelineLists(SharedPreferencesUtils.getString(getContext(),
                Constants.KEY_ACCESS_TOKEN, ""), 0,DEFAULT_COUNT, new WeiBoUtils.CallBack() {
            @Override
            public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                if (response.body() == null) {
                    srl.setRefreshing(false);
                    return;
                }
                statuse.clear();
                publicBlogCount = response.body().getStatuses().size();
                statuse.addAll(response.body().getStatuses());
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Call<HomeTimelineList> call, Throwable t) {
                Toast.makeText(getContext(), "没有网络了", Toast.LENGTH_SHORT).show();
                srl.setRefreshing(false);
            }
        });
        setRefreshListener();
        return view;
    }

    /**
     * set refresh listener
     */
    private void setRefreshListener() {
        setAddMoreRefresh();
        setDropDownRefesh();
    }

    /**
     * set drop-down refresh listener
     */
    private void setDropDownRefesh() {

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WeiBoUtils.getPublicTimelineLists(SharedPreferencesUtils.getString(getContext(),
                        Constants.KEY_ACCESS_TOKEN, ""), 0,publicBlogCount + DEFAULT_COUNT, new WeiBoUtils.CallBack() {
                    @Override
                    public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {

                        if (response.body() == null) {
                            srl.setRefreshing(false);
                            return;
                        }
                        statuse.clear();
                        statuse.addAll(response.body().getStatuses().subList(publicBlogCount-1,response.body().getStatuses().size() - 1));
                        publicBlogCount = response.body().getStatuses().size();
                        if (rv_homeblog.getAdapter() == null) {
                            initRecyclerView();
                        } else {
                            rv_homeblog.getAdapter().notifyDataSetChanged();
                            rv_homeblog.refreshDrawableState();
                        }
                        mBlogItemAdapter.addFootItem();
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
    }

    /**
     * set add more refresh listener
     */
    private void setAddMoreRefresh() {
        rv_homeblog.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                itemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == itemCount && dy > 0 && !mIsRefreshing) {
                    mIsRefreshing = true;
                    if(mFootView==null){
                        BoundFootView();
                    }
                    setFootViewLoading();

                    //TODO
                    WeiBoUtils.getPublicTimelineLists(SharedPreferencesUtils.getString(getContext(),
                            Constants.KEY_ACCESS_TOKEN, ""), 0,publicBlogCount + DEFAULT_COUNT, new WeiBoUtils.CallBack() {
                        @Override
                        public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                            if (response.body() == null || response.body().getStatuses().size() == 0) {
                                mBlogItemAdapter.removeFootItem();
                                rv_homeblog.getAdapter().notifyDataSetChanged();
                                rv_homeblog.refreshDrawableState();
                            } else {
                                statuse.addAll(response.body().getStatuses().subList(publicBlogCount-1,response.body().getStatuses().size()-1));
                                publicBlogCount = response.body().getStatuses().size();
                                rv_homeblog.getAdapter().notifyDataSetChanged();
                                rv_homeblog.refreshDrawableState();

                                mBlogItemAdapter.addFootItem();
                                setFootViewSuccess();
                            }
                            mIsRefreshing = false;
                        }

                        @Override
                        public void onFailure(Call<HomeTimelineList> call, Throwable t) {
                            mIsRefreshing = false;

                            setFootViewFail();
                        }
                    });
                }
            }
        });
    }

    private void setFootViewSuccess() {
        iv_loading.setVisibility(View.GONE);
        tv_foot_text.setText("更多...");
    }

    /**
     * set foot item to fail state
     */
    private void setFootViewFail() {
        iv_loading.setVisibility(View.GONE);
        tv_foot_text.setText("还没有联网哦，去设置网络吧");
    }

    /**
     * set foot item to loading state
     */
    private void setFootViewLoading() {
        iv_loading.setVisibility(View.VISIBLE);
        tv_foot_text.setText("加载中....");
    }

    /**
     * find the foot item view and bound the view
     */
    private void BoundFootView() {
        mFootView = mLinearLayoutManager.findViewByPosition(statuse.size());
        iv_loading = (ImageView) mFootView.findViewById(R.id.iv_loading);
        tv_foot_text = (TextView) mFootView.findViewById(R.id.tv_foot_text);
    }

    /**
     * bound view in home fragment
     */
    private void boundView() {
        rv_homeblog = (RecyclerView) view.findViewById(R.id.rv_homeblog);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.srl);
    }
}