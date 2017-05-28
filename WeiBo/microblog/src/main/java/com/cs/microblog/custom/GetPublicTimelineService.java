package com.cs.microblog.custom;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/4/26.
 */

public interface GetPublicTimelineService {
    @GET("2/statuses/public_timeline.json")
    Call<HomeTimelineList> getPublicTimelineList(@Query(Constants.KEY_ACCESS_TOKEN) String token,
                                                 @Query(Constants.KEY_MAX_ID) long maxId ,
                                                 @Query("count") int count);
}