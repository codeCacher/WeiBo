package com.cs.microblog.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.custom.Constants;

/**
 * Created by Administrator on 2017/5/1.
 * blog item's bottom bar button
 */

public class BlogItemBottomButtonView extends RelativeLayout {

    private ImageView iv_image;
    private TextView tv_text;

    public BlogItemBottomButtonView(Context context) {
        this(context, null);
    }

    public BlogItemBottomButtonView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlogItemBottomButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.blog_item_bottom_button, this);
        if (attrs != null) {
            BindView(view);
            SetAttrs(attrs);
        }
    }

    /**
     * bind the View in BlogItemBottomButtonView
     * @param view BlogItemBottomButtonView child view
     */
    private void BindView(View view) {
        iv_image = (ImageView) view.findViewById(R.id.iv_image);
        tv_text = (TextView) findViewById(R.id.tv_text);
    }

    /**
     * set attributes
     * @param attrs attribute set
     */
    private void SetAttrs(AttributeSet attrs) {
        iv_image.setImageResource(attrs.getAttributeResourceValue(Constants.ATTRS_NAMESPACE,
                "image_src", R.mipmap.ic_launcher));
        tv_text.setText(attrs.getAttributeValue(Constants.ATTRS_NAMESPACE, "text"));
    }

    public void setText(String string) {
        tv_text.setText(string);
    }
}
