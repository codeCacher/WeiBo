package com.cs.microblog.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.microblog.R;
import com.cs.microblog.custom.Emoticons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wenmingvs on 16/4/16.
 */
public class BlogTextUtils {

    private static final String AT = "@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}";// @人
    private static final String TOPIC = "#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#";// ##话题
    private static final String URL = "http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]";// url
    private static final String EMOJI = "\\[(\\S+?)\\]";//emoji 表情
    private static final String ALL = "(" + AT + ")" + "|" + "(" + TOPIC + ")" + "|" + "(" + URL + ")" + "|" + "(" + EMOJI + ")";

    public static void setBlogTextContent(String source, final Context context, TextView textView) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(source);
        //设置正则
        Pattern pattern = Pattern.compile(ALL);
        final Matcher matcher = pattern.matcher(spannableStringBuilder);

        while (matcher.find()) {
            final String at = matcher.group(1);
            final String topic = matcher.group(2);
            final String url = matcher.group(3);
            final String emoji = matcher.group(4);

            //处理@用户
            if (at != null) {
                int start = matcher.start(1);
                int end = start + at.length();
                BlogTextContentClickableSpan myClickableSpan = new BlogTextContentClickableSpan(context) {
                    @Override
                    public void onClick(View widget) {
//                        Intent intent = new Intent(context, UserActivity.class);
//                        String screen_name = at.substring(1);
//                        intent.putExtra("screenName", screen_name);
//                        context.startActivity(intent);
                        Toast.makeText(context, "点击了用户：" + at, Toast.LENGTH_SHORT).show();
                    }
                };
                spannableStringBuilder.setSpan(myClickableSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
            //处理##话题
            if (topic != null) {
                int start = matcher.start(2);
                int end = start + topic.length();
                BlogTextContentClickableSpan clickableSpan = new BlogTextContentClickableSpan(context) {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(context, "点击了话题：" + topic, Toast.LENGTH_LONG).show();
                    }
                };
                spannableStringBuilder.setSpan(clickableSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

            // 处理url地址
            if (url != null) {
                int start = matcher.start(3);
                int end = start + url.length();
                BlogUrlSpan urlSpan = new BlogUrlSpan(context,url);
                spannableStringBuilder.setSpan(urlSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

            //emoji
            if (emoji != null) {
                int start = matcher.start(4);
                int end = start + emoji.length();
                String imgName = Emoticons.getImgName(emoji);
                if (!TextUtils.isEmpty(imgName)) {
                    int resId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
                    Drawable emojiDrawable = context.getResources().getDrawable(resId);
                    if (emojiDrawable != null) {
                        emojiDrawable.setBounds(0, 0, DensityUtil.sp2px(context, 17), DensityUtil.sp2px(context, 17));
                        ImageSpan imageSpan = new ImageSpan(emojiDrawable, ImageSpan.ALIGN_BOTTOM) {
                            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                                Drawable b = getDrawable();
                                canvas.save();
                                int transY = bottom - b.getBounds().bottom;
                                transY -= paint.getFontMetricsInt().descent / 2;
                                canvas.translate(x, transY);
                                b.draw(canvas);
                                canvas.restore();
                            }
                        };
                        spannableStringBuilder.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                }
            }
        }
        textView.setText(spannableStringBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
