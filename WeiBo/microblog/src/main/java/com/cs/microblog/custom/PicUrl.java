package com.cs.microblog.custom;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
/**
 * Created by Administrator on 2017/5/1.
 */

public class PicUrl implements Parcelable {
    private String thumbnail_pic;

    protected PicUrl(Parcel in) {
        thumbnail_pic = in.readString();
    }

    public static final Creator<PicUrl> CREATOR = new Creator<PicUrl>() {
        @Override
        public PicUrl createFromParcel(Parcel in) {
            return new PicUrl(in);
        }

        @Override
        public PicUrl[] newArray(int size) {
            return new PicUrl[size];
        }
    };

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumbnail_pic);
    }
}
