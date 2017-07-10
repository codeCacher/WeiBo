package com.cs.microblog.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.cs.microblog.bean.HomeTimelineList;
import com.cs.microblog.bean.Statuse;
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

public class HomeLikeFragment extends Fragment {

    @BindView(R.id.erv)
    EndlessRecyclerView erv;

    private String TAG = "HomeLikeFragment";

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
        mActivity = HomeLikeFragment.this.getActivity();
    }

    private void firstFetchData() {
        WeiBoUtils.getHomeTimelineLists(SharedPreferencesUtils.getString(getContext(),
                Constants.KEY_ACCESS_TOKEN, ""), 0, new WeiBoUtils.CallBack() {
            @Override
            public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                if (response.body() == null) {
                    erv.setDropDownRefreshState(false);
                    return;
                }

                if(response.body().getStatuses().size()>0){
                    SharedPreferences sp = getContext().getSharedPreferences("StatusesCache", Context.MODE_PRIVATE);
                }

                mStatuses.clear();
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
                WeiBoUtils.getHomeTimelineLists(SharedPreferencesUtils.getString(getContext(),
                        Constants.KEY_ACCESS_TOKEN, ""), 0, new WeiBoUtils.CallBack() {
                    @Override
                    public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                        if (response.body() == null) {
                            erv.setDropDownRefreshState(false);
                            return;
                        }
                        mStatuses.clear();
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
        });
    }

    /**
     * set add more refresh listener
     */
    private void setAddMoreRefresh() {
        erv.setOnPullUpRefeshListener(new EndlessRecyclerView.OnPullUpRefeshListener() {
            @Override
            public void onPullUpRefesh() {
                WeiBoUtils.getHomeTimelineLists(SharedPreferencesUtils.getString(getContext(),
                        Constants.KEY_ACCESS_TOKEN, ""), mStatuses.get(mStatuses.size() - 1).getId() - 1, new WeiBoUtils.CallBack() {
                    @Override
                    public void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                        if (response.body() == null || response.body().getStatuses().size() == 0) {
                            erv.finishPullUpRefreshOnNoMoreData(mActivity);
                        } else {
                            mStatuses.addAll(response.body().getStatuses());
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

}