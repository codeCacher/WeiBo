package com.cs.microblog.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.cs.microblog.R;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.utils.NotificationUtils;
import com.cs.microblog.utils.SharedPreferencesUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/6/12.
 */

public class BlogPostService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyIBinder();
    }

    /**
     * 发布一条新微博(连续两次发布的微博不可以重复)。
     *
     * @param content   要发布的微博文本内容，内容不超过140个汉字
     * @param lat       纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0
     * @param lon       经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0
     */
    private void PostTextBlog(String content, String lat, String lon){
        Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken();
        oauth2AccessToken.setToken(SharedPreferencesUtils.getString(getApplicationContext(), Constants.KEY_ACCESS_TOKEN, ""));
        StatusesAPI mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, oauth2AccessToken);

        final NotificationUtils notificationUtils = NotificationUtils.getInstance(getApplicationContext());
        notificationUtils.sendNotification(0,"正在发送...","","正在发送...");

        mStatusesAPI.update(content, lat, lon, new RequestListener() {
            @Override
            public void onComplete(String s) {
                notificationUtils.sendNotification(0,"微博发送成功","","微博发送成功");
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notificationUtils.cancelNotification(0);
                    }
                },5000);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                notificationUtils.sendNotification(0,"微博发送失败","","微博发送失败");
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notificationUtils.cancelNotification(0);
                    }
                },5000);
            }

        });
    }
    /**
     * 上传图片并发布一条新微博，此方法会处理urlencode。
     *
     * @param content   要发布的微博文本内容，内容不超过140个汉字
     * @param bitmap    要上传的图片，仅支持JPEG、GIF、PNG格式，图片大小小于5M
     * @param lat       纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0
     * @param lon       经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0

     */
    private void PostTextAndPhotoBlog(String content, Bitmap bitmap, String lat, String lon){
        Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken();
        oauth2AccessToken.setToken(SharedPreferencesUtils.getString(getApplicationContext(), Constants.KEY_ACCESS_TOKEN, ""));
        StatusesAPI mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, oauth2AccessToken);

        final NotificationUtils notificationUtils = NotificationUtils.getInstance(getApplicationContext());
        notificationUtils.sendNotification(0,"正在发送...","","正在发送...");

        if(TextUtils.isEmpty(content)){
            content = "分享图片";
        }
        String encodeContent = "%E5%88%86%E4%BA%AB%E5%9B%BE%E7%89%87";
        try {
            encodeContent = URLEncoder.encode(content, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mStatusesAPI.upload(encodeContent,bitmap, lat, lon, new RequestListener() {
            @Override
            public void onComplete(String s) {
                notificationUtils.sendNotification(0,"微博发送成功","","微博发送成功");
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notificationUtils.cancelNotification(0);
                    }
                },5000);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                notificationUtils.sendNotification(0,"微博发送失败","","微博发送失败");
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notificationUtils.cancelNotification(0);
                    }
                },5000);
            }

        });
    }

    private class MyIBinder extends Binder implements IPostBlog{

        @Override
        public void postTextBlog(String content, String lat, String lon) {
            PostTextBlog(content,lat,lon);
        }

        @Override
        public void postTextAndPhotoBlog(String content, Bitmap bitmap, String lat, String lon) {
            PostTextAndPhotoBlog(content, bitmap, lat, lon);
        }
    }
}
