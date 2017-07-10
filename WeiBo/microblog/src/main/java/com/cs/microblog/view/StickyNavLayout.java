package com.cs.microblog.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cs.microblog.R;
import com.cs.microblog.utils.DisplayUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class StickyNavLayout extends RelativeLayout implements NestedScrollingParent {
    private static final String TAG = "StickyNavLayout";
    private static final int MIN_DROPDOWN_DISTENCE = 100;
    private int mToolBarHeight = 0;
    private View mTop;
    private View mNav;
    private ViewPager mViewPager;
    private int mTopViewHeight;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private float mLastY;
    private RecyclerView mNestedChilde;
    private int mLastScrollY;
    private float mVelocityY;
    private View mRoot;
    private boolean mRefreshDrag = false;
    private int mTopDefaultHeight;
    private int mTopMinHeight;
    private OnTopHideListener mOnTopHideListener;
    private OnTopShowListener mOnTopShowListener;
    private boolean mTopHide = false;
    private OnDropDownRefreshListener mOnDropDownRefreshListener;
    private OnDragRefreshListener mOnDragRefreshListener;

    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        mScroller.abortAnimation();
        mNestedChilde = (RecyclerView) target;
        return true;
    }


    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        mNestedChilde = (RecyclerView) target;
        if (StickyNavLayout.this.getScrollY() == 0 && dy < 0 && !ViewCompat.canScrollVertically(target, -1)) {
            mRefreshDrag = true;
            ViewGroup.LayoutParams layoutParams = mTop.getLayoutParams();
            layoutParams.height -= dy / 2;
            mTop.setLayoutParams(layoutParams);
            if (mOnDragRefreshListener != null) {
                mOnDragRefreshListener.StartDrag();
            }
        } else if (dy > 0 && mTop.getMeasuredHeight() > mTopDefaultHeight) {
            ViewGroup.LayoutParams layoutParams = mTop.getLayoutParams();
            if (layoutParams.height - dy / 2 < mTopMinHeight) {
                layoutParams.height = mTopMinHeight;
            } else {
                layoutParams.height -= dy / 2;
            }
            mTop.setLayoutParams(layoutParams);
        } else {
            mRefreshDrag = false;
            boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
            boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

            if (hiddenTop || showTop) {
                scrollBy(0, dy);
                consumed[1] = dy;
            }
        }

    }

    @Override
    public void onStopNestedScroll(View child) {
        mNestedChilde = (RecyclerView) child;
        if (mRefreshDrag || mTop.getMeasuredHeight() > mTopDefaultHeight) {
            mRefreshDrag = false;
            if (mTop.getMeasuredHeight() - mTopDefaultHeight > MIN_DROPDOWN_DISTENCE * DisplayUtils.getInstance(getContext()).getDisplayDensity()) {
                if (mOnDropDownRefreshListener != null) {
                    mOnDropDownRefreshListener.refresh();
                }
            } else {
                if (mOnDragRefreshListener != null) {
                    mOnDragRefreshListener.OnRelease();
                }
            }
            flyBack();
        }
    }

    private void flyBack() {
        final int measuredHeight = mTop.getMeasuredHeight();
        final int cut = (measuredHeight - mTopDefaultHeight) / 10;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 10; i++) {
                    Observable.just(measuredHeight - cut * i)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Integer>() {
                                @Override
                                public void call(Integer aInteger) {
                                    ViewGroup.LayoutParams layoutParams = mTop.getLayoutParams();
                                    layoutParams.height = aInteger;
                                    mTop.setLayoutParams(layoutParams);
                                }
                            });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onNestedPreFling(final View target, float velocityX, float velocityY) {
        mNestedChilde = (RecyclerView) target;
        mVelocityY = velocityY;
        mLastScrollY = getScrollY();
        fling((int) velocityY);
        return true;
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                mLastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                if (getScrollY() == 0 && dy > 0) {
                    mRefreshDrag = true;
                    ViewGroup.LayoutParams layoutParams = mTop.getLayoutParams();
                    layoutParams.height += dy / 2;
                    mTop.setLayoutParams(layoutParams);
                    if (mOnDragRefreshListener != null) {
                        mOnDragRefreshListener.StartDrag();
                    }
                } else if (dy < 0 && mTop.getMeasuredHeight() > mTopDefaultHeight) {
                    ViewGroup.LayoutParams layoutParams = mTop.getLayoutParams();
                    if (layoutParams.height + dy / 2 < mTopMinHeight) {
                        layoutParams.height = mTopMinHeight;
                    } else {
                        layoutParams.height += dy / 2;
                    }
                    mTop.setLayoutParams(layoutParams);
                } else {
                    mRefreshDrag = false;
                    scrollBy(0, (int) -dy);
                }
                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mRefreshDrag) {
                    mRefreshDrag = false;
                    if (mTop.getMeasuredHeight() - mTopDefaultHeight > MIN_DROPDOWN_DISTENCE * DisplayUtils.getInstance(getContext()).getDisplayDensity()) {
                        if (mOnDropDownRefreshListener != null) {
                            mOnDropDownRefreshListener.refresh();
                        }
                    } else {
                        if (mOnDragRefreshListener != null) {
                            mOnDragRefreshListener.OnRelease();
                        }
                    }
                    flyBack();
                } else {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    mVelocityY = -mVelocityTracker.getYVelocity();
                    if (Math.abs(mVelocityY) > mMinimumVelocity) {
                        mLastScrollY = getScrollY();
                        fling((int) mVelocityY);
                    }
                    recycleVelocityTracker();
                }
                break;
        }

        return super.onTouchEvent(event);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = findViewById(R.id.id_stickynavlayout_topview);
        mNav = findViewById(R.id.id_stickynavlayout_indicator);
        View view = findViewById(R.id.id_stickynavlayout_viewpager);
        if (!(view instanceof ViewPager)) {
            throw new RuntimeException(
                    "id_stickynavlayout_viewpager show used by ViewPager !");
        }
        mViewPager = (ViewPager) view;

        mTopDefaultHeight = mTop.getLayoutParams().height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = mRoot.getLayoutParams();
        params.height = getMeasuredHeight() - mToolBarHeight + mTop.getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), mTop.getMeasuredHeight() + mNav.getMeasuredHeight() + mViewPager.getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTop.getMeasuredHeight() - mToolBarHeight;
    }


    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y >= mTopViewHeight) {
            y = mTopViewHeight;
            if (!mTopHide) {
                if (mOnTopHideListener != null) {
                    mOnTopHideListener.OnTopHide();
                }
                mTopHide = true;
            }
        } else if (mTopHide) {
            if (mOnTopShowListener != null) {
                mOnTopShowListener.OnTopShow();
            }
            mTopHide = false;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mVelocityY < 0) {
                if (getScrollY() >= mTopViewHeight) {//此时top完全隐藏

                    if (!ViewCompat.canScrollVertically(mNestedChilde, -1)) {//如果子view已经滑动到顶部，这个时候父亲自己滑动
                        scrollBy(0, mScroller.getCurrY() - mLastScrollY);
                    } else {
                        mNestedChilde.scrollBy(0, mScroller.getCurrY() - mLastScrollY);
                    }

                } else if (getScrollY() == 0) {//parent自己完全显示，交给子view滑动
                    if (ViewCompat.canScrollVertically(mNestedChilde, -1)) {
                        mNestedChilde.scrollBy(0, mScroller.getCurrY() - mLastScrollY);
                    }
                } else {//此时top没有完全显示，让parent自己滑动
                    scrollBy(0, mScroller.getCurrY() - mLastScrollY);
                }
            } else if (mVelocityY > 0) {
                if (getScrollY() >= mTopViewHeight) {//此时top完全隐藏
                    int currY = mScroller.getCurrY();
                    mNestedChilde.scrollBy(0, mScroller.getCurrY() - mLastScrollY);
                } else {
                    scrollTo(0, mScroller.getCurrY());
                }
            }
            mLastScrollY = mScroller.getCurrY();
            postInvalidate();
        }
    }

    private void setToolBarHeight(int toolBarHeight) {
        mToolBarHeight = toolBarHeight;
    }

    private void setRoot(View root) {
        this.mRoot = root;
    }

    public void init(int height, RelativeLayout rlRoot) {
        setToolBarHeight(height);
        setRoot(rlRoot);
        mTopMinHeight = mToolBarHeight + mNav.getMeasuredHeight();
    }

    public interface OnTopHideListener {
        void OnTopHide();
    }

    public void setOnTopHideListener(OnTopHideListener onTopHideListener) {
        this.mOnTopHideListener = onTopHideListener;
    }

    public interface OnTopShowListener {
        void OnTopShow();
    }

    public void setOnTopShowListener(OnTopShowListener onTopShowListener) {
        this.mOnTopShowListener = onTopShowListener;
    }

    public interface OnDropDownRefreshListener {
        void refresh();
    }

    public void setOnDropDownRefreshListener(OnDropDownRefreshListener listener) {
        this.mOnDropDownRefreshListener = listener;
    }

    public interface OnDragRefreshListener {
        void StartDrag();

        void OnRelease();
    }

    public void setOnDragRefreshListener(OnDragRefreshListener listener) {
        this.mOnDragRefreshListener = listener;
    }
}
