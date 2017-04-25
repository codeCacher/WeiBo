package com.cs.microblog.custom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cs.microblog.activity.MainActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/4/25.
 */

public class LoginWebViewClient extends WebViewClient {

    Context context;

    public LoginWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (url.contains(Constants.REDIRECT_URI + "?" + Constants.NAME_CODE + "=")) {
            String code = MyURLUtils.parseCode(url);
            view.stopLoading();

            //POST and get the Access Token
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.weibo.com/oauth2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            AccessTokenService accessTokenService = retrofit.create(AccessTokenService.class);
            Call<AccessTokenEntity> accessToken = accessTokenService.getAccessToken(Constants.APP_KEY,Constants.APP_SECRET,Constants.GRANT_TYPE,code,Constants.REDIRECT_URI);
            accessToken.enqueue(new Callback<AccessTokenEntity>() {
                @Override
                public void onResponse(Call<AccessTokenEntity> call, Response<AccessTokenEntity> response) {

                    if (response.isSuccessful()) {
                        System.out.println("" + response.body().toString());
                        AccessTokenEntity accessTokenEntity = response.body();
                        Toast.makeText(context,"登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        //TODO 获取了Token
                    }
                }

                @Override
                public void onFailure(Call<AccessTokenEntity> call, Throwable t) {
                    System.out.println("error:" + t.getMessage());
                }
            });
        }
    }
}
