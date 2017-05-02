package com.cs.microblog.custom;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/27.
 */

public class HomeTimelineList {
    private int total_number;
    private ArrayList<Statuse> statuses;

    //Getter and Setter
    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public ArrayList<Statuse> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Statuse> statuses) {
        this.statuses = statuses;
    }
}
