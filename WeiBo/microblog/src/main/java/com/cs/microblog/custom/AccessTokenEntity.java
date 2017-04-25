package com.cs.microblog.custom;

/**
 * Created by Administrator on 2017/4/25.
 */

public class AccessTokenEntity {
    private String access_token;
    private int expires_in;
    private String remind_in;
    private String uid;

    public AccessTokenEntity(String access_token, int expires_in, String remind_in, String uid) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.remind_in = remind_in;
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRemind_in() {
        return remind_in;
    }

    public void setRemind_in(String remind_in) {
        this.remind_in = remind_in;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
