package com.cs.microblog.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2017/4/29.
 */

public class TimeUtils {
    /**
     * parse the String contain the time informaton
     * the String format is "EEE MMM d HH:mm:ss Z yyyy"
     * @param calendarString the String contain the time information "EEE MMM d HH:mm:ss Z yyyy"
     * @return  Calendar instance
     * @throws ParseException
     */
    public static Calendar parseCalender(String calendarString) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", Locale.ENGLISH)).parse(calendarString));
        return calendar;
    }
}
