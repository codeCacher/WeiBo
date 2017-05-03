package com.cs.microblog.custom;

import android.support.annotation.Nullable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/4/26.
 */

public interface GetHomeTimelineService {
    @GET("2/statuses/home_timeline.json")
    Call<HomeTimelineList> getHomeTimelineList(@Query(Constants.KEY_ACCESS_TOKEN) String token,
                                               @Query(Constants.KEY_MAX_ID) long maxId);
}
