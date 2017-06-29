package com.cs.microblog.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/6/28.
 */

public class DisplayUtils {
    private WindowManager mWindowsManager;
    private Context mContext;

    private DisplayUtils(Context context){
        this.mContext = context;
        mWindowsManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
    }

    public static DisplayUtils getInstance(Context context) {
        return new DisplayUtils(context);
    }

    public int getDisplayHeight(){
        return mWindowsManager.getDefaultDisplay().getHeight();
    }

    public int getDisplayWight(){
        return mWindowsManager.getDefaultDisplay().getWidth();
    }

    public float getDisplayDensity(){
        return mContext.getResources().getDisplayMetrics().density;
    }
}
