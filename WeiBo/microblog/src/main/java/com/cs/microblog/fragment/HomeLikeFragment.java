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
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/27.
 * home fragment,show the user concerned blog
 */

public class HomeLikeFragment extends Fragment {

    private String TAG = "HomeLikeFragment";

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
        mBlogItemAdapter = new BlogItemAdapter(getActivity(), rv_homeblog, statuse);
        rv_homeblog.setAdapter(mBlogItemAdapter);
        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_homeblog.setLayoutManager(mLinearLayoutManager);
        rv_homeblog.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        Fresco.getImagePipeline().pause();
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Fresco.getImagePipeline().resume();
                        break;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_like, container, false);

        boundView();

        srl.setRefreshing(true);
        mIsRefreshing = false;

        WeiBoUtils.getHomeTimelineLists(SharedPreferencesUtils.getString(getContext(),
                Constants.KEY_ACCESS_TOKEN, ""), 0, new WeiBoUtils.CallBack() {
            @Override
            public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                statuse.clear();
                if (response.body() == null) {
                    srl.setRefreshing(false);
                    return;
                }
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
                WeiBoUtils.getHomeTimelineLists(SharedPreferencesUtils.getString(getContext(),
                        Constants.KEY_ACCESS_TOKEN, ""), 0, new WeiBoUtils.CallBack() {
                    @Override
                    public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                        statuse.clear();
                        if (response.body() == null) {
                            srl.setRefreshing(false);
                            return;
                        }
                        statuse.addAll(response.body().getStatuses());
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
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                itemCount = recyclerView.getAdapter().getItemCount();
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == itemCount && dy > 0 && !mIsRefreshing) {
                    mIsRefreshing = true;
                    if(mFootView==null){
                        BoundFootView();
                    }
                    setFootViewLoading();

                    WeiBoUtils.getHomeTimelineLists(SharedPreferencesUtils.getString(getContext(),
                            Constants.KEY_ACCESS_TOKEN, ""), statuse.get(statuse.size() - 1).getId() - 1, new WeiBoUtils.CallBack() {
                        @Override
                        public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                            if (response.body() == null || response.body().getStatuses().size() == 0) {
                                mBlogItemAdapter.removeFootItem();
                                rv_homeblog.getAdapter().notifyDataSetChanged();
                                rv_homeblog.refreshDrawableState();
                            } else {
                                statuse.addAll(response.body().getStatuses());
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