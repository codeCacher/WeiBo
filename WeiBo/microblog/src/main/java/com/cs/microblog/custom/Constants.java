package com.cs.microblog.custom;

/**
 * Created by Administrator on 2017/4/25.
 * Constants
 */

public class Constants {
    //URL
    public static final String AUTHORIZE_URL = "https://api.weibo.com/oauth2/authorize";
    public static final String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
    public static final String REDIRECT_URI = "https://api.weibo.com/oauth2/default.html";
    public static final String HOME_TIMELINE_URL = "https://api.weibo.com/2/statuses/home_timeline.json";

    // SharedPreferences file name
    public static final String SP_FILE_NAME = "cfg_sp";
    // SharedPreferences keys
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_EXPIRES_IN = "expires_in";
    public static final String KEY_UID = "uid";

    public static final String APP_KEY = "3605082758";
    public static final String APP_SECRET = "f9158445b50c555c94ca9f7fb9197d32";
    public static final String GRANT_TYPE= "authorization_code";

    //Name
    public static final String NAME_ACCESS_TOKEN = "access_token";
    public static final String NAME_CLIENT_ID = "client_id";
    public static final String NAME_CODE = "code";
    public static final String NAME_REDIRECT_URI = "redirect_uri";
    public static final String NAME_CLIENT_SECRET = "client_secret";
    public static final String NAME_GRANT_TYPE= "grant_type";
}
