package com.cs.microblog.custom;

/**
 * Created by Administrator on 2017/4/25.
 */

public class AccessTokenRequest {
    String client_id = Constants.APP_KEY;
    String client_secret = Constants.APP_SECRET;
    String grant_type = Constants.GRANT_TYPE;
    String code;
    String redirect_uri = Constants.REDIRECT_URI;

    public AccessTokenRequest(String code) {
        this.code = code;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public String getCode() {
        return code;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
