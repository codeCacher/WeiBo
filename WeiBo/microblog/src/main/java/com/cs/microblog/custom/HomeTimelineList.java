package com.cs.microblog.custom;

import android.database.Cursor;

import java.util.List;

/**
 * Created by Administrator on 2017/4/27.
 */

public class HomeTimelineList {
    private int total_number;

    //TODO 是否有更好的方法
    private Statuse[] statuses;
    private Advertisement[] ad;

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public Statuse[] getStatuses() {
        return statuses;
    }

    public void setStatuses(Statuse[] statuses) {
        this.statuses = statuses;
    }

    public Advertisement[] getAd() {
        return ad;
    }

    public void setAd(Advertisement[] ad) {
        this.ad = ad;
    }
}
