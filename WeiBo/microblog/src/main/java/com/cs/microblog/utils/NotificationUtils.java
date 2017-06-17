package com.cs.microblog.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;

import com.cs.microblog.R;

/**
 * Created by Administrator on 2017/6/12.
 */

public class NotificationUtils {
    private NotificationManager mNotiManager;
    private Context mContext;
    private final Notification.Builder mProgressBuilder;
    private final Notification.Builder mNomalBuilder;

    private NotificationUtils(Context context) {
        mContext = context;
        mNotiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mProgressBuilder = new Notification.Builder(mContext);
        mProgressBuilder
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon))
                .setSmallIcon(R.drawable.icon)
                .setProgress(100, 0, false);

        mNomalBuilder = new Notification.Builder(mContext);
        mNomalBuilder
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon))
                .setSmallIcon(R.drawable.icon);
    }

    public static NotificationUtils getInstance(Context context) {
        return new NotificationUtils(context);
    }

    public void sendProgressNotification(int id, String contentTitle, String ticker, int max, int progress) {
        Notification notification = mProgressBuilder
                .setContentTitle(contentTitle)
                .setTicker(ticker)
                .setProgress(max, progress, false)
                .build();
        mNotiManager.notify(id, notification);
    }

    public void sendNotification(int id, String contentTitle,String contentText, String ticker) {
        Notification notification = mNomalBuilder
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setTicker(ticker)
                .build();
        mNotiManager.notify(id, notification);
    }

    public void cancelNotification(int id){
        mNotiManager.cancel(id);
    }
}
