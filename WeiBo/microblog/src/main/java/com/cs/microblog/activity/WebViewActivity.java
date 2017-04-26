package com.cs.microblog.activity;

import android.content.Context;
import android.content.Intent;
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
    private static final String JUMP_URL = "jumpUrl";

    public static void openUrl(Context context, String url) {
        context.startActivity(new Intent(context,WebViewActivity.class).putExtra(JUMP_URL,url));
    }

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
        wv.loadUrl(getIntent().getStringExtra(JUMP_URL));
    }
}
