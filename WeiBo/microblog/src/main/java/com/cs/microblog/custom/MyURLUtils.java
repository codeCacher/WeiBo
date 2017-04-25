package com.cs.microblog.custom;

import java.net.URL;

/**
 * Created by Administrator on 2017/4/25.
 */

public class MyURLUtils {
    public static String parseCode(String url){
        return url.substring(url.lastIndexOf(Constants.NAME_CODE) + Constants.NAME_CODE.length() + 1);
    }
}
