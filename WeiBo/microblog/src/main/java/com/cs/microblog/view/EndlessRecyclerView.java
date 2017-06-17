package com.cs.microblog.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.cs.microblog.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/5.
 */

public class EndlessRecyclerView extends LinearLayout {

    @BindView(R.id.rv_recycle)
    RecyclerView rv_recycle;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    private static final String TAG = "EndlessRecyclerView";
    public boolean mIsRefreshing;
    private OnDropDownRefeshListener onDropDownRefeshListener;
    private OnPullUpRefeshListener onPullUpRefeshListener;
    private LinearLayoutManager mLayoutManager;
    private EndlessRecyclerViewAdapter mAdapter;
    private int mItemCount;

    public EndlessRecyclerView(Context context) {
        this(context, null);
    }

    public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.addView(LayoutInflater.from(context).inflate(R.layout.endless_recycler_view, this, false));
        ButterKnife.bind(this);

        srl.setRefreshing(true);
        mIsRefreshing = false;

        registerRefreshListener();
    }

    public void setLayoutManager(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        rv_recycle.setLayoutManager(layoutManager);
    }

    public void setAdapter(EndlessRecyclerViewAdapter adapter) {
        this.mAdapter = adapter;
        rv_recycle.setAdapter(adapter);
    }

    public interface OnDropDownRefeshListener {
        void onDonDropDownRefesh();
    }

    public void setOnDropDownRefeshListener(OnDropDownRefeshListener onDropDownRefeshListener) {
        this.onDropDownRefeshListener = onDropDownRefeshListener;
    }

    public interface OnPullUpRefeshListener {
        void onPullUpRefesh();
    }

    public void setOnPullUpRefeshListener(OnPullUpRefeshListener onPullUpRefeshListener) {
        this.onPullUpRefeshListener = onPullUpRefeshListener;
    }

    public void setDropDownRefreshState(Boolean b) {
        srl.setRefreshing(b);
    }

    public void finishDropDownRefreshOnSuccess(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setDropDownRefreshState(false);
                mAdapter.setFootViewSuccess();
            }
        });
    }

    public void finishDropDownRefreshOnFailure(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setDropDownRefreshState(false);
                mAdapter.setFootViewFail();
            }
        });
    }

    public void finishPullUpRefreshOnSuccess(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIsRefreshing = false;
                mAdapter.setFootViewSuccess();
            }
        });
    }

    public void finishPullUpRefreshOnNoMoreData(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIsRefreshing = false;
                mAdapter.setFootViewNoMore();
            }
        });
    }


    public void finishPullUpRefreshOnFailure(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIsRefreshing = false;
                mAdapter.setFootViewFail();
            }
        });
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        rv_recycle.addOnScrollListener(onScrollListener);
    }

    public int getScrollState() {
        return rv_recycle.getScrollState();
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return rv_recycle.getLayoutManager();
    }

    public void removeOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        rv_recycle.removeOnScrollListener(onScrollListener);
    }

    /**
     * set refresh listener
     */
    private void registerRefreshListener() {
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
                if (onDropDownRefeshListener != null) {
                    onDropDownRefeshListener.onDonDropDownRefesh();
                }
            }
        });
    }

    /**
     * set add more refresh listener
     */
    private void setAddMoreRefresh() {
        rv_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mItemCount = mAdapter.getItemCount();
                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == mItemCount && dy > 0 && !mIsRefreshing) {
                    mIsRefreshing = true;
                    mAdapter.setFootViewLoading();
                    if (onPullUpRefeshListener != null) {
                        onPullUpRefeshListener.onPullUpRefesh();
                    }
                }
            }
        });
    }
}
