/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cs.microblog.bean;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 评论列表结构体。
 * 
 * @author SINA
 * @since 2013-11-24
 */
public class RepostList {

    /** 微博列表 */
    public ArrayList<Repost> repostList;
    public String previous_cursor;
    public String next_cursor;
    public int total_number;
    
    public static RepostList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        
        RepostList reposts = new RepostList();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            reposts.previous_cursor = jsonObject.optString("previous_cursor", "0");
            reposts.next_cursor     = jsonObject.optString("next_cursor", "0");
            reposts.total_number    = jsonObject.optInt("total_number", 0);
            
            JSONArray jsonArray      = jsonObject.optJSONArray("reposts");
            if (jsonArray != null && jsonArray.length() > 0) {
                int length = jsonArray.length();
                reposts.repostList = new ArrayList<Repost>(length);
                for (int ix = 0; ix < length; ix++) {
                    reposts.repostList.add(Repost.parse(jsonArray.optJSONObject(ix)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return reposts;
    }
}
