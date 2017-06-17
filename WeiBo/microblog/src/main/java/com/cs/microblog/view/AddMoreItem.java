package com.cs.microblog.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.microblog.R;
import com.cs.microblog.custom.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/11.
 */

public class AddMoreItem extends RelativeLayout {
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_text)
    TextView tvText;

    public AddMoreItem(Context context) {
        this(context, null);
    }

    public AddMoreItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddMoreItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.add_more_item, this);
        ButterKnife.bind(this, view);
        int image_src = attrs.getAttributeResourceValue(Constants.ATTRS_NAMESPACE, "top_src", 0);
        if(image_src!=0){
            ivImage.setImageResource(image_src);
        }
        String text = attrs.getAttributeValue(Constants.ATTRS_NAMESPACE,"bottom_text");
        tvText.setText(text);

        ivImage.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ViewGroup.LayoutParams layoutParams = ivImage.getLayoutParams();
                layoutParams.height = right-left;
                ivImage.setLayoutParams(layoutParams);
            }
        });
    }
}
