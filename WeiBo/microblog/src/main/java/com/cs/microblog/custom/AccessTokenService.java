package com.cs.microblog.custom;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/4/25.
 */

public interface AccessTokenService {
    @POST("access_token")
    Call<AccessTokenEntity> getAccessToken(@Query(Constants.KEY_CLIENT_ID) String appKey,
                                           @Query(Constants.KEY_CLIENT_SECRET) String appSecret,
                                           @Query(Constants.KEY_GRANT_TYPE) String grantType,
                                           @Query(Constants.KEY_CODE) String code,
                                           @Query(Constants.KEY_REDIRECT_URI) String redirectUri);
}
