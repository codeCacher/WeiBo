package com.cs.microblog.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.URLSpan;

import com.cs.microblog.R;

/**
 * Created by Administrator on 2017/6/17.
 */

public class BlogUrlSpan extends URLSpan {
    private Context mContext;
    public BlogUrlSpan(Context context, String url) {
        super(url);
        mContext = context;
    }

    protected BlogUrlSpan(Parcel in) {
        super(in);
    }

    public static final Creator<BlogUrlSpan> CREATOR = new Creator<BlogUrlSpan>() {
        @Override
        public BlogUrlSpan createFromParcel(Parcel in) {
            return new BlogUrlSpan(in);
        }

        @Override
        public BlogUrlSpan[] newArray(int size) {
            return new BlogUrlSpan[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mContext.getResources().getColor(R.color.colorBlueDark));
        ds.setUnderlineText(false);
    }
}