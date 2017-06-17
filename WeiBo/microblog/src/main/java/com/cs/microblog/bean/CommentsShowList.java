package com.cs.microblog.bean;

import com.cs.microblog.bean.User;

/**
 * Created by Administrator on 2017/6/1.
 */

public class CommentsShowList {
    public String created_at;//评论创建时间
    public long id;//评论的ID
    public String text;//评论的内容
    public String source;//评论的来源
    public String mid;//评论的MID
    public User user;
}
