package com.cs.microblog.custom;

import com.cs.microblog.bean.Statuse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/26.
 */

public interface GetBlogByIDService {
    @GET("2/statuses/show.json")
    Observable<Statuse> getBlogByID(@Query(Constants.KEY_ACCESS_TOKEN) String token,
                                    @Query("id") long id);
}