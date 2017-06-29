package com.cs.microblog.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs.microblog.custom.Constants;
import com.cs.microblog.utils.DensityUtil;

import java.util.List;

public class ViewPagerIndicator extends LinearLayout {

    private static final int DEFAULT_INDICATOR_COLOR = Color.BLUE;
    private static final float DEFAULT_TEXT_SIZE = 18f;
    private static final float DEFAULT_BOTTOM_LINE_WIDTH = 8;
    private static final String TAG = "Indicator";
    private final Typeface typeface;

    private int mIndicatorColor = DEFAULT_INDICATOR_COLOR;
    private final float mTextSize = DEFAULT_TEXT_SIZE;
    private final float mBottomLineWidth = DEFAULT_BOTTOM_LINE_WIDTH;

    private String[] mTitles;
    private int mTabCount;
    private Paint mPaint = new Paint();
    private int mTabWidth;
    private float mTextWidth;
    private float mBlankWidth;
    private float mLineLeft;
    private float mLineRight;
    private List<OnClickListener> mOnClickListenerList;
    private int mSelectedPageIndex = -1;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int colorId = attrs.getAttributeResourceValue(Constants.ATTRS_NAMESPACE, "bottom_line_color", 0);
        mIndicatorColor = getResources().getColor(colorId);

        mPaint.setColor(mIndicatorColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mBottomLineWidth);
        typeface = Typeface.create("homeTextType", Typeface.NORMAL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTabWidth = w / mTabCount;
        mTextWidth = DensityUtil.sp2dp(getContext(), mTextSize) * 2;
        mBlankWidth = (mTabWidth - mTextWidth) / 2;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.drawLine(mLineLeft, getHeight() - 15, mLineRight, getHeight() - 15, mPaint);
        canvas.restore();

    }


    private void scroll(int position, float offset) {
        if (offset <= 0.5) {
            mLineLeft = mBlankWidth + mTabWidth * position;
            mLineRight = mTabWidth * offset * 2 + mTextWidth + mBlankWidth + mTabWidth * position;
        } else {
            mLineLeft = mBlankWidth + mTabWidth * 2f * (offset - 0.5f) + mTabWidth * position;
            mLineRight = 2 * mTabWidth - mBlankWidth + mTabWidth * position;
        }
        invalidate();
    }

    public void initIndicator(String[] titles, List<OnClickListener> list, ViewPager viewPager) {
        setTitlesOnClickListener(list);
        setTitles(titles);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
                if (positionOffset == 0) {
                    switchTextStyle(position);
                    mSelectedPageIndex = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
                switchTextStyle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void switchTextStyle(int position) {
        if(mSelectedPageIndex!=-1){
            TextView childAtBehind = (TextView) getChildAt(mSelectedPageIndex);
            childAtBehind.setTypeface(typeface, Typeface.NORMAL);
            childAtBehind.setTextColor(Color.GRAY);
        }
        TextView childAt = (TextView) getChildAt(position);
        childAt.setTypeface(typeface, Typeface.BOLD);
        childAt.setTextColor(Color.BLACK);
    }

    private void setTitles(String[] titles) {
        mTitles = titles;
        mTabCount = titles.length;
        generateTitleView();
    }

    private void setTitlesOnClickListener(List<OnClickListener> list) {
        this.mOnClickListenerList = list;
    }

    private void generateTitleView() {
        if (getChildCount() > 0) {
            this.removeAllViews();
        }

        setWeightSum(mTabCount);
        for (int i = 0; i < mTabCount; i++) {
            TextView tv = new TextView(getContext());
            LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            tv.setTypeface(typeface, Typeface.NORMAL);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.GRAY);
            tv.setText(mTitles[i]);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
            tv.setLayoutParams(lp);
            if (mOnClickListenerList != null && i < mOnClickListenerList.size() && mOnClickListenerList.get(i) != null) {
                tv.setOnClickListener(mOnClickListenerList.get(i));
            }
            addView(tv);
        }
    }

}
