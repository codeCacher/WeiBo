package com.cs.microblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cs.microblog.R;
import com.cs.microblog.adapter.EndlessBlogItemAdapter;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.custom.HomeTimelineList;
import com.cs.microblog.custom.Statuse;
import com.cs.microblog.utils.SharedPreferencesUtils;
import com.cs.microblog.utils.WeiBoUtils;
import com.cs.microblog.view.EndlessRecyclerView;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/27.
 * home fragment,show the user concerned blog
 */

public class HomeHotFragment extends Fragment {

    @BindView(R.id.erv)
    EndlessRecyclerView erv;

    private String TAG = "HomeHotFragment";
    private static final int DEFAULT_COUNT = 50;
    private int publicBlogCount = 0;

    private ArrayList<Statuse> mStatuses;
    private FragmentActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_like, container, false);
        ButterKnife.bind(this, view);
        initData();
        initRecyclerView();
        firstFetchData();
        setRefreshListener();
        return view;
    }

    private void initData() {
        mActivity = HomeHotFragment.this.getActivity();
    }

    private void firstFetchData() {
        WeiBoUtils.getPublicTimelineLists(SharedPreferencesUtils.getString(getContext(),
                Constants.KEY_ACCESS_TOKEN, ""), 0, DEFAULT_COUNT, new WeiBoUtils.CallBack() {
            @Override
            public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                if (response.body() == null) {
                    erv.setDropDownRefreshState(false);
                    return;
                }
                mStatuses.clear();
                publicBlogCount = response.body().getStatuses().size();
                mStatuses.addAll(response.body().getStatuses());
                erv.finishDropDownRefreshOnSuccess(mActivity);
            }

            @Override
            public void onFailure(Call<HomeTimelineList> call, Throwable t) {
                Toast.makeText(getContext(), "没有网络了", Toast.LENGTH_SHORT).show();
                erv.finishDropDownRefreshOnFailure(mActivity);
            }
        });
    }

    private void initRecyclerView() {
        mStatuses = new ArrayList<>();
        EndlessBlogItemAdapter mBlogItemAdapter = new EndlessBlogItemAdapter(getContext(), erv, mStatuses);
        erv.setAdapter(mBlogItemAdapter);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        erv.setLayoutManager(mLinearLayoutManager);
        erv.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        erv.setOnDropDownRefeshListener(new EndlessRecyclerView.OnDropDownRefeshListener() {
            @Override
            public void onDonDropDownRefesh() {
                WeiBoUtils.getPublicTimelineLists(SharedPreferencesUtils.getString(getContext(),
                        Constants.KEY_ACCESS_TOKEN, ""), 0, publicBlogCount + DEFAULT_COUNT, new WeiBoUtils.CallBack() {
                    @Override
                    public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                        if (response.body() == null) {
                            erv.setDropDownRefreshState(false);
                            return;
                        }
                        mStatuses.clear();
                        mStatuses.addAll(response.body().getStatuses().subList(publicBlogCount - 1, response.body().getStatuses().size() - 1));
                        publicBlogCount = response.body().getStatuses().size();
                        erv.finishDropDownRefreshOnSuccess(mActivity);
                    }

                    @Override
                    public void onFailure(Call<HomeTimelineList> call, Throwable t) {
                        Toast.makeText(getContext(), "没有网络了", Toast.LENGTH_SHORT).show();
                        erv.finishDropDownRefreshOnFailure(mActivity);
                    }
                });
            }
        });
    }

    /**
     * set add more refresh listener
     */
    private void setAddMoreRefresh() {
        erv.setOnPullUpRefeshListener(new EndlessRecyclerView.OnPullUpRefeshListener() {
            @Override
            public void onPullUpRefesh() {
                WeiBoUtils.getPublicTimelineLists(SharedPreferencesUtils.getString(getContext(),
                        Constants.KEY_ACCESS_TOKEN, ""), 0, publicBlogCount + DEFAULT_COUNT, new WeiBoUtils.CallBack() {
                    @Override
                    public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                        if (response.body() == null || response.body().getStatuses().size() == 0) {
                            erv.finishPullUpRefreshOnNoMoreData(mActivity);
                        } else {
                            mStatuses.addAll(response.body().getStatuses().subList(publicBlogCount - 1, response.body().getStatuses().size() - 1));
                            publicBlogCount = response.body().getStatuses().size();
                            erv.finishPullUpRefreshOnSuccess(mActivity);
                        }
                    }

                    @Override
                    public void onFailure(Call<HomeTimelineList> call, Throwable t) {
                        erv.finishPullUpRefreshOnFailure(mActivity);
                    }
                });
            }
        });
    }






//    private void initRecyclerView() {
//        mBlogItemAdapter = new BlogItemAdapter(getContext(), rv_homeblog, statuse);
//        rv_homeblog.setAdapter(mBlogItemAdapter);
//        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        rv_homeblog.setLayoutManager(mLinearLayoutManager);
//        rv_homeblog.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                switch (newState) {
//                    case RecyclerView.SCROLL_STATE_DRAGGING:
//                    case RecyclerView.SCROLL_STATE_SETTLING:
//                        Fresco.getImagePipeline().pause();
//                        break;
//                    case RecyclerView.SCROLL_STATE_IDLE:
//                        Fresco.getImagePipeline().resume();
//                        break;
//                }
//            }
//        });
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_home_like, container, false);
//
//        boundView();
//
//        srl.setRefreshing(true);
//        mIsRefreshing = false;
//
//        WeiBoUtils.getPublicTimelineLists(SharedPreferencesUtils.getString(getContext(),
//                Constants.KEY_ACCESS_TOKEN, ""), 0,DEFAULT_COUNT, new WeiBoUtils.CallBack() {
//            @Override
//            public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
//                if (response.body() == null) {
//                    srl.setRefreshing(false);
//                    return;
//                }
//                statuse.clear();
//                publicBlogCount = response.body().getStatuses().size();
//                statuse.addAll(response.body().getStatuses());
//                handler.sendEmptyMessage(0);
//            }
//
//            @Override
//            public void onFailure(Call<HomeTimelineList> call, Throwable t) {
//                Toast.makeText(getContext(), "没有网络了", Toast.LENGTH_SHORT).show();
//                srl.setRefreshing(false);
//            }
//        });
//        setRefreshListener();
//        return view;
//    }
//
//    /**
//     * set refresh listener
//     */
//    private void setRefreshListener() {
//        setAddMoreRefresh();
//        setDropDownRefesh();
//    }
//
//    /**
//     * set drop-down refresh listener
//     */
//    private void setDropDownRefesh() {
//
//        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                WeiBoUtils.getPublicTimelineLists(SharedPreferencesUtils.getString(getContext(),
//                        Constants.KEY_ACCESS_TOKEN, ""), 0,publicBlogCount + DEFAULT_COUNT, new WeiBoUtils.CallBack() {
//                    @Override
//                    public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
//
//                        if (response.body() == null) {
//                            srl.setRefreshing(false);
//                            return;
//                        }
//                        statuse.clear();
//                        statuse.addAll(response.body().getStatuses().subList(publicBlogCount-1,response.body().getStatuses().size() - 1));
//                        publicBlogCount = response.body().getStatuses().size();
//                        if (rv_homeblog.getAdapter() == null) {
//                            initRecyclerView();
//                        } else {
//                            rv_homeblog.getAdapter().notifyDataSetChanged();
//                        }
//                        mBlogItemAdapter.addFootItem();
//                        srl.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onFailure(Call<HomeTimelineList> call, Throwable t) {
//                        Toast.makeText(getContext(), "没有网络了", Toast.LENGTH_SHORT).show();
//                        srl.setRefreshing(false);
//                    }
//                });
//            }
//        });
//    }
//
//    /**
//     * set add more refresh listener
//     */
//    private void setAddMoreRefresh() {
//        rv_homeblog.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                itemCount = recyclerView.getAdapter().getItemCount();
//                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
//                if (lastVisibleItemPosition + 1 == itemCount && dy > 0 && !mIsRefreshing) {
//                    mIsRefreshing = true;
//                    if(mFootView==null){
//                        BoundFootView();
//                    }
//                    setFootViewLoading();
//
//                    //TODO
//                    WeiBoUtils.getPublicTimelineLists(SharedPreferencesUtils.getString(getContext(),
//                            Constants.KEY_ACCESS_TOKEN, ""), 0,publicBlogCount + DEFAULT_COUNT, new WeiBoUtils.CallBack() {
//                        @Override
//                        public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
//                            if (response.body() == null || response.body().getStatuses().size() == 0) {
//                                mBlogItemAdapter.removeFootItem();
//                                rv_homeblog.getAdapter().notifyDataSetChanged();
//                                rv_homeblog.refreshDrawableState();
//                            } else {
//                                statuse.addAll(response.body().getStatuses().subList(publicBlogCount-1,response.body().getStatuses().size()-1));
//                                publicBlogCount = response.body().getStatuses().size();
//                                rv_homeblog.getAdapter().notifyDataSetChanged();
//
//                                mBlogItemAdapter.addFootItem();
//                                setFootViewSuccess();
//                            }
//                            mIsRefreshing = false;
//                        }
//
//                        @Override
//                        public void onFailure(Call<HomeTimelineList> call, Throwable t) {
//                            mIsRefreshing = false;
//
//                            setFootViewFail();
//                        }
//                    });
//                }
//            }
//        });
//    }
}