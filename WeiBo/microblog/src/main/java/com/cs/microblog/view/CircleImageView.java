package com.cs.microblog.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/4/29.
 * Circle Image View
 * recommend that the layout_wight equals to the layout_height
 */

public class CircleImageView extends android.support.v7.widget.AppCompatImageView {
    private Paint mPaint;
    private Canvas mDrawCanvas;
    private PorterDuffXfermode mPDM_srcIn;
    private PorterDuffXfermode mPDM_srcOver;
    private Rect mSrcRect;
    private Rect mDesRect;

    public CircleImageView(Context context) {
        super(context);
        Init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    /**
     * Init the member variable
     * Init the paint
     */
    private void Init() {
        mDrawCanvas = new Canvas();
        mPaint = new Paint();
        mPDM_srcIn = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mPDM_srcOver = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
        mSrcRect = new Rect();
        mDesRect = new Rect();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap circleBitmap = getCircleBitmap(bitmap, Math.min(getWidth() / 2, getHeight()/2));
        canvas.drawBitmap(circleBitmap, 0, 0, mPaint);
    }

    /**
     * Cut the Bitmap to a Circle Bitmap
     * @param bitmap source Bitmap
     * @param Radius the Circle Bitmap radius
     * @return
     */
    private Bitmap getCircleBitmap(Bitmap bitmap, int Radius) {
        Bitmap outBitmap = Bitmap.createBitmap(getWidth(), getHeight(), bitmap.getConfig());
        mDrawCanvas.setBitmap(outBitmap);
        mDrawCanvas.drawCircle(Radius, Radius, Radius, mPaint);
        mPaint.setXfermode(mPDM_srcIn);
        mSrcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mDesRect.set(0, 0, getWidth(), getHeight());
        mDrawCanvas.drawBitmap(bitmap, mSrcRect, mDesRect, mPaint);
        mPaint.setXfermode(mPDM_srcOver);

        return outBitmap;
    }

}
