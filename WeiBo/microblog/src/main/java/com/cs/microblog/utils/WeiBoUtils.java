package com.cs.microblog.utils;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.cs.microblog.custom.GetHomeTimelineService;
import com.cs.microblog.custom.GetPublicTimelineService;
import com.cs.microblog.custom.HomeTimelineList;
import com.cs.microblog.custom.PictureInfo;
import com.cs.microblog.custom.Statuse;
import com.facebook.common.logging.FLog;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/5/3.
 * WeiBo Utils
 */

public class WeiBoUtils {

    /**
     * interface when finish the requests to call back
     */
    public interface CallBack {
        public abstract void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response);
        public abstract void onFailure(Call<HomeTimelineList> call, Throwable t);
    }

    /**
     * Post for the Home time line blog lists
     *
     * @param token    access token
     * @param callBack call back when finished
     */
    public static void getHomeTimelineLists(String token, long maxId, final CallBack callBack) {
        //POST and get the Access Token
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weibo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetHomeTimelineService getHomeTimelineService = retrofit.create(GetHomeTimelineService.class);
        Call<HomeTimelineList> accessToken = getHomeTimelineService.getHomeTimelineList(token, maxId);
        accessToken.enqueue(new Callback<HomeTimelineList>() {
            @Override
            public void onResponse(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                callBack.onSuccess(call, response);
            }

            @Override
            public void onFailure(Call<HomeTimelineList> call, Throwable t) {
                callBack.onFailure(call, t);
            }
        });
    }

    /**
     * Post for the Public time line blog lists
     *
     * @param token    access token
     * @param callBack call back when finished
     */
    public static void getPublicTimelineLists(String token, long maxId, int count,final CallBack callBack) {
        //POST and get the Access Token
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weibo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetPublicTimelineService getPublicTimelineService = retrofit.create(GetPublicTimelineService.class);
        Call<HomeTimelineList> accessToken = getPublicTimelineService.getPublicTimelineList(token, maxId, count);
        accessToken.enqueue(new Callback<HomeTimelineList>() {
            @Override
            public void onResponse(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                callBack.onSuccess(call, response);
            }

            @Override
            public void onFailure(Call<HomeTimelineList> call, Throwable t) {
                callBack.onFailure(call, t);
            }
        });
    }
    public interface onFinishGetPictureTypeListener {
        void onSeccess(int Type);
        void onFailed(Throwable throwable);
    }

    /**
     * get the picture's type through the url,
     *
     * @param url picture's url
     * @return picture type
     * PICTURE_NOMAL:normal picture
     * PICTURE_LONG :long picture
     * PICTURE_GIF  :gif picture
     */
    static private int Type;
    public static void getPictureType(final Context context, String url, SimpleDraweeView simpleDraweeView, final onFinishGetPictureTypeListener Listener) {
        //TODO 获取图片类型
        Type = -1;
        if(url.endsWith(".gif")) {
            Type = PictureInfo.PICTURE_GIF;
        }
        BaseControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    Listener.onSeccess(Type);
                    return;
                }
                int width = imageInfo.getWidth();
                int height = imageInfo.getHeight();

                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                int screenWidth = windowManager.getDefaultDisplay().getWidth();
                int screenHeight = windowManager.getDefaultDisplay().getHeight();

                if(Type == -1) {
                    if(0.1f * height/width > 0.1f*screenHeight/screenWidth) {
                        Type = PictureInfo.PICTURE_LONG;
                    }else {
                        Type = PictureInfo.PICTURE_NOMAL;
                    }
                }
                Listener.onSeccess(Type);
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                Listener.onFailed(throwable);
            }
        };

        Uri uri = Uri.parse(url);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(uri)
                .build();
        simpleDraweeView.setController(controller);
    }

    /**
     * get picture information
     * @param url picture's url
     * @return PictureInfo contain picture information
     */
    public static PictureInfo getPictrueInfo(String url) {
        //TODO 获取图片信息
        return null;
    }
}
