package com.cs.microblog.custom;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/4/25.
 */

public interface AccessTokenService {
    @POST("access_token")
    Call<AccessTokenEntity> getAccessToken(@Query(Constants.NAME_CLIENT_ID) String appKey,
                                           @Query(Constants.NAME_CLIENT_SECRET) String appSecret,
                                           @Query(Constants.NAME_GRANT_TYPE) String grantType,
                                           @Query(Constants.NAME_CODE) String code,
                                           @Query(Constants.NAME_REDIRECT_URI) String redirectUri);
}
