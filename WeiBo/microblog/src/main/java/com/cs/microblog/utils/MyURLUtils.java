package com.cs.microblog.utils;

import com.cs.microblog.custom.Constants;

/**
 * Created by Administrator on 2017/4/25.
 */

public class MyURLUtils {
    public static String parseCode(String url){
        return url.substring(url.lastIndexOf(Constants.KEY_CODE) + Constants.KEY_CODE.length() + 1);
    }
}
