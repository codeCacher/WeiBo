package com.cs.microblog.utils;

import android.content.Context;
import android.text.TextUtils;

import com.cs.microblog.bean.CommentsShowList;
import com.cs.microblog.custom.Constants;
import com.cs.microblog.custom.GetBlogByIDService;
import com.cs.microblog.custom.GetCommentsShowService;
import com.cs.microblog.custom.GetHomeTimelineService;
import com.cs.microblog.custom.GetPublicTimelineService;
import com.cs.microblog.bean.HomeTimelineList;
import com.cs.microblog.bean.Repost;
import com.cs.microblog.bean.RepostList;
import com.cs.microblog.bean.Statuse;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.User;

import java.text.ParseException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/5/3.
 * WeiBo Utils
 */

public class WeiBoUtils {

    /**
     * interface when finish the requests to call back
     */
    public interface CallBack {
        public abstract void onSuccess(Call<HomeTimelineList> call, Response<HomeTimelineList> response);

        public abstract void onFailure(Call<HomeTimelineList> call, Throwable t);
    }

    /**
     * Post for the Home time line blog lists
     *
     * @param token    access token
     * @param callBack call back when finished
     */
    public static void getHomeTimelineLists(String token, long maxId, final CallBack callBack) {
        //POST and get the Access Token
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weibo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetHomeTimelineService getHomeTimelineService = retrofit.create(GetHomeTimelineService.class);
        Call<HomeTimelineList> accessToken = getHomeTimelineService.getHomeTimelineList(token, maxId);
        accessToken.enqueue(new Callback<HomeTimelineList>() {
            @Override
            public void onResponse(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                callBack.onSuccess(call, response);
            }

            @Override
            public void onFailure(Call<HomeTimelineList> call, Throwable t) {
                callBack.onFailure(call, t);
            }
        });
    }

    /**
     * Post for the Public time line blog lists
     *
     * @param token    access token
     * @param callBack call back when finished
     */
    public static void getPublicTimelineLists(String token, long maxId, int count, final CallBack callBack) {
        //POST and get the Access Token
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weibo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetPublicTimelineService getPublicTimelineService = retrofit.create(GetPublicTimelineService.class);
        Call<HomeTimelineList> accessToken = getPublicTimelineService.getPublicTimelineList(token, maxId, count);
        accessToken.enqueue(new Callback<HomeTimelineList>() {
            @Override
            public void onResponse(Call<HomeTimelineList> call, Response<HomeTimelineList> response) {
                callBack.onSuccess(call, response);
            }

            @Override
            public void onFailure(Call<HomeTimelineList> call, Throwable t) {
                callBack.onFailure(call, t);
            }
        });
    }

    /**
     * interface when finish the requests to call back
     */
    public interface CommentCallBack {
        public abstract void onSuccess(Call<CommentsShowList> call, Response<CommentsShowList> response);

        public abstract void onFailure(Call<CommentsShowList> call, Throwable t);
    }

    public static void getCommentShowLists(String token, long blogId, long sinceId, final CommentCallBack callBack) {
        //get the Access Token
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weibo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetCommentsShowService getCommentsShowService = retrofit.create(GetCommentsShowService.class);
        Call<CommentsShowList> accessToken = getCommentsShowService.getCommentsShowList(token, blogId, sinceId);
        accessToken.enqueue(new Callback<CommentsShowList>() {
            @Override
            public void onResponse(Call<CommentsShowList> call, Response<CommentsShowList> response) {
                callBack.onSuccess(call, response);
            }

            @Override
            public void onFailure(Call<CommentsShowList> call, Throwable t) {
                callBack.onFailure(call, t);
            }
        });
    }

    public static String parseBlogTimeAndSourceInfo(Statuse statuse) {

        //parse the create time
        String created_at = statuse.getCreated_at();
        StringBuilder blogInfo = new StringBuilder();
        Calendar current = Calendar.getInstance();
        Calendar creatCalendar;
        try {
            creatCalendar = TimeUtils.parseCalender(created_at);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        if (current.get(Calendar.YEAR) != creatCalendar.get(Calendar.YEAR)) {
            blogInfo.append((creatCalendar.get(Calendar.YEAR) - current.get(Calendar.YEAR)) + "年前");
        } else if (current.get(Calendar.MONTH) != creatCalendar.get(Calendar.MONTH)) {
            blogInfo.append((creatCalendar.get(Calendar.MONTH) - current.get(Calendar.MONTH)) + "个月前");
        } else if (current.get(Calendar.DAY_OF_YEAR) - creatCalendar.get(Calendar.DAY_OF_YEAR) > 2) {
            blogInfo.append((creatCalendar.get(Calendar.DAY_OF_YEAR) - current.get(Calendar.DAY_OF_YEAR)) + "天前");
        } else if (current.get(Calendar.DAY_OF_YEAR) - creatCalendar.get(Calendar.DAY_OF_YEAR) == 2) {
            blogInfo.append("前天 " + creatCalendar.get(Calendar.HOUR_OF_DAY) + ":" + creatCalendar.get(Calendar.MINUTE));
        } else if (current.get(Calendar.DAY_OF_YEAR) - creatCalendar.get(Calendar.DAY_OF_YEAR) == 1) {
            blogInfo.append("昨天 " + creatCalendar.get(Calendar.HOUR_OF_DAY) + ":" + creatCalendar.get(Calendar.MINUTE));
        } else if (current.get(Calendar.DAY_OF_YEAR) == creatCalendar.get(Calendar.DAY_OF_YEAR)) {
            if (current.get(Calendar.HOUR_OF_DAY) != creatCalendar.get(Calendar.HOUR_OF_DAY)) {
                blogInfo.append(current.get(Calendar.HOUR_OF_DAY) - creatCalendar.get(Calendar.HOUR_OF_DAY) + "小时前");
            } else if (current.get(Calendar.HOUR_OF_DAY) == creatCalendar.get(Calendar.HOUR_OF_DAY)) {
                if (current.get(Calendar.MINUTE) == creatCalendar.get(Calendar.MINUTE)) {
                    blogInfo.append("1分钟前");
                } else {
                    blogInfo.append(current.get(Calendar.MINUTE) - creatCalendar.get(Calendar.MINUTE) + "分钟前");
                }
            }
        }

        //parse the source
        blogInfo.append(" 来自 ");
        String fullSource = statuse.getSource();
        if (!TextUtils.isEmpty(fullSource)) {
            String source = fullSource.substring(fullSource.indexOf(">") + 1, fullSource.lastIndexOf("<"));
            blogInfo.append(source);

            return blogInfo.toString();
        } else {
            return "";
        }
    }

    public static String parseCommentTime(Comment comment) {

        //parse the create time
        String created_at = comment.created_at;

        Calendar current = Calendar.getInstance();
        Calendar creatCalendar;
        try {
            creatCalendar = TimeUtils.parseCalender(created_at);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return (creatCalendar.get(Calendar.MONTH) + 1) + "-" +
                creatCalendar.get(Calendar.DAY_OF_MONTH) + " " +
                (creatCalendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                (creatCalendar.get(Calendar.MINUTE));
    }

    public static String parseRepostTime(Repost repost) {

        //parse the create time
        String created_at = repost.created_at;

        Calendar current = Calendar.getInstance();
        Calendar creatCalendar;
        try {
            creatCalendar = TimeUtils.parseCalender(created_at);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return (creatCalendar.get(Calendar.MONTH) + 1) + "-" +
                creatCalendar.get(Calendar.DAY_OF_MONTH) + " " +
                (creatCalendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                (creatCalendar.get(Calendar.MINUTE));
    }

    public interface GetCommentListCallBack {
        void OnSuccess(CommentList commentList);

        void OnFailure(WeiboException e);
    }

    /**
     * 根据微博ID返回某条微博的评论列表。
     *
     * @param id         需要查询的微博ID。
     * @param since_id   若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
     * @param max_id     若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
     * @param count      单页返回的记录条数，默认为50
     * @param page       返回结果的页码，默认为1。
     * @param authorType 作者筛选类型，0：全部、1：我关注的人、2：陌生人 ,默认为0。可为以下几种 :
     */
    public static void getCommentList(Context context, long id,
                                      long since_id, long max_id,
                                      int count, int page,
                                      int authorType, final GetCommentListCallBack callBack) {
        Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken();
        oauth2AccessToken.setToken(SharedPreferencesUtils.getString(context, Constants.KEY_ACCESS_TOKEN, ""));
        CommentsAPI commentsAPI = new CommentsAPI(context, Constants.APP_KEY, oauth2AccessToken);
        commentsAPI.show(id, since_id, max_id, count, page, authorType, new RequestListener() {
            @Override
            public void onComplete(String s) {
                CommentList commentList = CommentList.parse(s);
                callBack.OnSuccess(commentList);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                callBack.OnFailure(e);
            }
        });
    }

    /**
     * 根据微博ID返回某条微博的评论列表。
     *
     * @param id         需要查询的微博ID。
     * @param since_id   若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
     * @param max_id     若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
     * @param count      单页返回的记录条数，默认为50
     * @param page       返回结果的页码，默认为1。
     * @param authorType 作者筛选类型，0：全部、1：我关注的人、2：陌生人 ,默认为0。可为以下几种 :
     */
    public static Observable<CommentList> getCommentList(final Context context, final long id,
                                                         final long since_id, final long max_id,
                                                         final int count, final int page,
                                                         final int authorType) {
        return Observable.create(new Observable.OnSubscribe<CommentList>() {
            @Override
            public void call(final Subscriber<? super CommentList> subscriber) {
                Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken();
                oauth2AccessToken.setToken(SharedPreferencesUtils.getString(context, Constants.KEY_ACCESS_TOKEN, ""));
                CommentsAPI commentsAPI = new CommentsAPI(context, Constants.APP_KEY, oauth2AccessToken);
                commentsAPI.show(id, since_id, max_id, count, page, authorType, new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        CommentList commentList = CommentList.parse(s);
                        subscriber.onNext(commentList);
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }

    public static Observable<Statuse> getBlogByID(String token, long id) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.weibo.com/")
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        GetBlogByIDService getBlogByIDService = retrofit.create(GetBlogByIDService.class);
        GetBlogByIDService getBlogByIDService = RxService.create(GetBlogByIDService.class);
        return getBlogByIDService.getBlogByID(token, id);
    }

    /**
     * 获取指定微博的转发微博列表
     *
     * @param id         需要查询的微博ID。
     * @param since_id   若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id     若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count      单页返回的记录条数，默认为50
     * @param page       返回结果的页码，默认为1
     * @param authorType 作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。可为以下几种：
     */
    public static Observable<RepostList> getRepostList(final Context context, final long id,
                                                       final long since_id, final long max_id,
                                                       final int count, final int page,
                                                       final int authorType) {
        return Observable.create(new Observable.OnSubscribe<RepostList>() {
            @Override
            public void call(final Subscriber<? super RepostList> subscriber) {
                Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken();
                oauth2AccessToken.setToken(SharedPreferencesUtils.getString(context, Constants.KEY_ACCESS_TOKEN, ""));
//                oauth2AccessToken.setUid(SharedPreferencesUtils.getString(context, Constants.KEY_UID, ""));
//                oauth2AccessToken.setExpiresIn(SharedPreferencesUtils.getString(context, Constants.KEY_EXPIRES_IN, ""));

                StatusesAPI statusesAPI = new StatusesAPI(context, Constants.APP_KEY, oauth2AccessToken);
                statusesAPI.repostTimeline(id, since_id, max_id, count, page, authorType, new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        RepostList repostList = RepostList.parse(s);
                        subscriber.onNext(repostList);
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }


    public static Observable<User> getUserInfo(final Context context) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(final Subscriber<? super User> subscriber) {
                Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken();
                oauth2AccessToken.setToken(SharedPreferencesUtils.getString(context, Constants.KEY_ACCESS_TOKEN, ""));
                UsersAPI usersAPI = new UsersAPI(context, Constants.APP_KEY, oauth2AccessToken);
                String uidStr = SharedPreferencesUtils.getString(context, Constants.KEY_UID, "");
                long uid = Long.parseLong(uidStr);
                usersAPI.show(uid, new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        User user = User.parse(s);
                        subscriber.onNext(user);
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }
}
