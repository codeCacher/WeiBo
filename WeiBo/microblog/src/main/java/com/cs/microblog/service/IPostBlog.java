package com.cs.microblog.service;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/6/12.
 */

public interface IPostBlog {
    void postTextBlog(String content, String lat, String lon);
    void postTextAndPhotoBlog(String content, Bitmap bitmap, String lat, String lon);
}
