package com.cs.microblog.custom;

/**
 * Created by Administrator on 2017/4/25.
 * Constants
 */

public class Constants {
    public static final String APP_KEY = "3605082758";
    public static final String APP_SECRET = "f9158445b50c555c94ca9f7fb9197d32";
    public static final String GRANT_TYPE= "authorization_code";
    //URL
    public static final String AUTHORIZE_URL = "https://api.weibo.com/oauth2/authorize";
    public static final String ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
    public static final String REDIRECT_URI = "https://api.weibo.com/oauth2/default.html";
    public static final String HOME_TIMELINE_URL = "https://api.weibo.com/2/statuses/home_timeline.json";

    /**
     * SharedPreferences file name
     */
    public static final String SP_FILE_NAME = "cfg_sp";

    //Keys
    /**
     * oauth2
     * access token key
     */
    public static final String KEY_ACCESS_TOKEN = "access_token";
    /**
     * oauth2
     * expires_in key
     * the life cycle of access_token
     */
    public static final String KEY_EXPIRES_IN = "expires_in";
    /**
     * oauth2
     * uid key
     */
    public static final String KEY_UID = "uid";
    /**
     * oauth2
     * client_id key
     */
    public static final String KEY_CLIENT_ID = "client_id";
    /**
     * oauth2
     * code key
     */
    public static final String KEY_CODE = "code";
    /**
     * oauth2
     * redirect_uri key
     */
    public static final String KEY_REDIRECT_URI = "redirect_uri";
    /**
     * oauth2
     * client_secret key
     */
    public static final String KEY_CLIENT_SECRET = "client_secret";
    /**
     * oauth2
     * grant_type key
     */
    public static final String KEY_GRANT_TYPE = "grant_type";
    /**
     * oauth2
     * since_id key
     * 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
     */
    public static final String KEY_SINCE_ID = "since_id";
    /**
     * oauth2
     * max_id key
     * 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
     */
    public static final String KEY_MAX_ID = "max_id";
    /**
     * oauth2
     * count key
     * 单页返回的记录条数，最大不超过100，默认为20。
     */
    public static final String KEY_COUNT = "count";
    /**
     * oauth2
     * page key
     * 返回结果的页码，默认为1。
     */
    public static final String KEY_PAGE = "page";
    /**
     * oauth2
     * base_app key
     * 是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0。
     */
    public static final String KEY_BASE_APP = "base_app";
    /**
     * oauth2
     * feature key
     * 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
     */
    public static final String KEY_FEATURE = "feature";
    /**
     * statuses/request
     * trim_user key
     * 返回值中user字段开关，0：返回完整user字段、1：user字段仅返回user_id，默认为0。
     */
    public static final String KEY_TRIM_USER = "trim_user";



    /**
     * app attribute name space
     */
    public static final String ATTRS_NAMESPACE="http://schemas.android.com/apk/com.cs.microblog";
}
