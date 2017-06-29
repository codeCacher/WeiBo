package com.cs.microblog.utils;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.cs.microblog.R;


/**
 * Created by wenmingvs on 16/4/15.
 */
public class BlogTextContentClickableSpan extends ClickableSpan {

    private Context mContext;

    public BlogTextContentClickableSpan(Context context) {
        mContext = context;
    }

    @Override
    public void onClick(View widget) {
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mContext.getResources().getColor(R.color.colorBlueDark));
        ds.setUnderlineText(false);
    }
}
