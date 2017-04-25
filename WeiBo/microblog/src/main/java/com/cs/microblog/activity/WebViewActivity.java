package com.cs.microblog.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cs.microblog.R;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.custom.LoginWebViewClient;

/**
 * Created by Administrator on 2017/4/25.
 */

public class WebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView wv = (WebView) findViewById(R.id.wv);
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.setWebViewClient(new LoginWebViewClient(this));
        String url = Constants.AUTHORIZE_URL + "?" + Constants.NAME_CLIENT_ID + "=" + Constants.APP_KEY + "&" + Constants.NAME_REDIRECT_URI + "=" + Constants.REDIRECT_URI;
        wv.loadUrl(url);
    }
}
