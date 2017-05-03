package com.cs.microblog.utils;

import android.util.Log;

import com.cs.microblog.custom.GetHomeTimelineService;
import com.cs.microblog.custom.HomeTimelineList;
import com.cs.microblog.custom.Statuse;

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
     * @param token access token
     * @param callBack call back when finished
     */
    public static void getHomeTimelineLists(String token, long maxId,final CallBack callBack){
        //POST and get the Access Token
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weibo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetHomeTimelineService getHomeTimelineService = retrofit.create(GetHomeTimelineService.class);
        Call<HomeTimelineList> accessToken = getHomeTimelineService.getHomeTimelineList(token,maxId);
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
}
