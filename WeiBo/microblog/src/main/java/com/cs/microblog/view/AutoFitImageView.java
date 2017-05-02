package com.cs.microblog.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/5/1.
 */

public class AutoFitImageView extends android.support.v7.widget.AppCompatImageView {
    Rect srcRect;
    Rect desRect;
    Paint paint;
    public AutoFitImageView(Context context) {
        super(context);
        init();
    }

    public AutoFitImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoFitImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        srcRect = new Rect();
        desRect = new Rect();
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable = getDrawable();
        if(drawable == null) {
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int density = bitmap.getDensity();
        int drawableWidth = bitmap.getWidth();
        int drawalbeHeight = bitmap.getHeight();

        float viewWHRate = 1.0f * viewWidth / viewHeight;
        float drawableWHRate = 1.0f * drawableWidth / drawalbeHeight;

        desRect.set(0,0,getWidth(),getHeight());
        if(viewWHRate >= drawableWHRate) {
            int cutDrawalbeHeight = (int) (drawableWidth/viewWHRate);
            int heightPadding = drawalbeHeight/2 - cutDrawalbeHeight/2;
            srcRect.set(0,heightPadding,drawableWidth, cutDrawalbeHeight + heightPadding);
        } else {
            int cutDrawalbeWidth = (int) (drawalbeHeight*viewWHRate);
            int widthPadding = drawableWidth/2 - cutDrawalbeWidth/2;
            srcRect.set(widthPadding,0, cutDrawalbeWidth + widthPadding,drawalbeHeight);
        }

        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap,srcRect,desRect,paint);
    }
}
