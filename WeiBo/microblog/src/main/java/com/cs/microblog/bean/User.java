package com.cs.microblog.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/27.
 */

public class User implements Parcelable{
    private long id;
    private String screen_name;
    private String name;
    private String province;
    private String city;
    private String location;
    private String description;
    private String url;
    private String profile_image_url;
    private String cover_image_phone;
    private String domain;
    private String gender;
    private int followers_count;
    private int statuses_count;
    private int favourites_count;
    private int friends_count;
    private String created_at;
    private boolean following;
    private boolean allow_all_act_msg;
    private String remark;
    private boolean geo_enabled;
    private boolean verified;
    private boolean allow_all_comment;
    private String avatar_large;
    private String verified_reason;
    private boolean follow_me;
    private int online_status;
    private int bi_followers_count;

    protected User(Parcel in) {
        id = in.readLong();
        screen_name = in.readString();
        name = in.readString();
        province = in.readString();
        city = in.readString();
        location = in.readString();
        description = in.readString();
        url = in.readString();
        profile_image_url = in.readString();
        cover_image_phone = in.readString();
        domain = in.readString();
        gender = in.readString();
        followers_count = in.readInt();
        statuses_count = in.readInt();
        favourites_count = in.readInt();
        friends_count = in.readInt();
        created_at = in.readString();
        following = in.readByte() != 0;
        allow_all_act_msg = in.readByte() != 0;
        remark = in.readString();
        geo_enabled = in.readByte() != 0;
        verified = in.readByte() != 0;
        allow_all_comment = in.readByte() != 0;
        avatar_large = in.readString();
        verified_reason = in.readString();
        follow_me = in.readByte() != 0;
        online_status = in.readInt();
        bi_followers_count = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getStatuses_count() {
        return statuses_count;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isAllow_all_act_msg() {
        return allow_all_act_msg;
    }

    public void setAllow_all_act_msg(boolean allow_all_act_msg) {
        this.allow_all_act_msg = allow_all_act_msg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isGeo_enabled() {
        return geo_enabled;
    }

    public void setGeo_enabled(boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isAllow_all_comment() {
        return allow_all_comment;
    }

    public void setAllow_all_comment(boolean allow_all_comment) {
        this.allow_all_comment = allow_all_comment;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public String getVerified_reason() {
        return verified_reason;
    }

    public void setVerified_reason(String verified_reason) {
        this.verified_reason = verified_reason;
    }

    public boolean isFollow_me() {
        return follow_me;
    }

    public void setFollow_me(boolean follow_me) {
        this.follow_me = follow_me;
    }

    public int getOnline_status() {
        return online_status;
    }

    public void setOnline_status(int online_status) {
        this.online_status = online_status;
    }

    public int getBi_followers_count() {
        return bi_followers_count;
    }

    public void setBi_followers_count(int bi_followers_count) {
        this.bi_followers_count = bi_followers_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(screen_name);
        dest.writeString(name);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(profile_image_url);
        dest.writeString(cover_image_phone);
        dest.writeString(domain);
        dest.writeString(gender);
        dest.writeInt(followers_count);
        dest.writeInt(statuses_count);
        dest.writeInt(favourites_count);
        dest.writeInt(friends_count);
        dest.writeString(created_at);
        dest.writeByte((byte) (following ? 1 : 0));
        dest.writeByte((byte) (allow_all_act_msg ? 1 : 0));
        dest.writeString(remark);
        dest.writeByte((byte) (geo_enabled ? 1 : 0));
        dest.writeByte((byte) (verified ? 1 : 0));
        dest.writeByte((byte) (allow_all_comment ? 1 : 0));
        dest.writeString(avatar_large);
        dest.writeString(verified_reason);
        dest.writeByte((byte) (follow_me ? 1 : 0));
        dest.writeInt(online_status);
        dest.writeInt(bi_followers_count);
    }

    public String getCover_image_phone() {
        return cover_image_phone;
    }

    public void setCover_image_phone(String cover_image_phone) {
        this.cover_image_phone = cover_image_phone;
    }
}
