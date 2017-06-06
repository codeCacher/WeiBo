package com.cs.microblog.custom;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/4/26.
 */

public interface GetCommentsShowService {
    @GET("2/comments/show.json")
    Call<CommentsShowList> getCommentsShowList(@Query(Constants.KEY_ACCESS_TOKEN) String token,
                                               @Query("id") long blogId,
                                               @Query("since_id") long sinceId);
}