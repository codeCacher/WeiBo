package com.cs.microblog.utils;

import com.cs.microblog.custom.Constants;

import java.net.URL;

/**
 * Created by Administrator on 2017/4/25.
 */

public class MyURLUtils {
    public static String parseCode(String url){
        return url.substring(url.lastIndexOf(Constants.NAME_CODE) + Constants.NAME_CODE.length() + 1);
    }
}
