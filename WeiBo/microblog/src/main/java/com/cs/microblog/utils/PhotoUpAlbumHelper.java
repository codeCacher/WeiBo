package com.cs.microblog.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore.Images.Media;
import android.util.Log;


import com.cs.microblog.bean.Photo;
import com.cs.microblog.bean.PhotoBucket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhotoUpAlbumHelper extends AsyncTask<Boolean, Object, ArrayList<PhotoBucket>> {
    private final String TAG = "PhotoUpAlbumHelper";
    private Context mContext;
    private ContentResolver mContentRecolver;
    HashMap<String, String> thumbnailList = new HashMap<String, String>();
    private HashMap<String, PhotoBucket> mBucketList = new HashMap<>();
    private boolean hasBuildImagesBucketList = false;
    private OnFinishedListener mOnFinishedListener;


    private PhotoUpAlbumHelper(Context context) {
        if (this.mContext == null) {
            this.mContext = context;
            mContentRecolver = context.getContentResolver();
        }
    }

    public static PhotoUpAlbumHelper getInstance(Context context) {
        return new PhotoUpAlbumHelper(context);
    }

    /**
     * 得到图片集
     */
    private void buildImagesBucketList() {
        String columns[] = new String[]{
                Media._ID, Media.BUCKET_ID, Media.PICASA_ID,
                Media.DATA, Media.DISPLAY_NAME,
                Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME};
        Cursor cur = mContentRecolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, Media.DATE_MODIFIED + " desc");
        if (cur == null) {
            return;
        }
        if (cur.moveToFirst()) {
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int displayNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
            int fileSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            // Description:这里增加了一个判断：判断照片的名字是否合法，例如.jpg .png    图片名字是不合法的，直接过滤掉
            do {
                if (cur.getString(photoPathIndex).substring(
                        cur.getString(photoPathIndex).lastIndexOf("/") + 1,
                        cur.getString(photoPathIndex).lastIndexOf("."))
                        .replaceAll(" ", "").length() <= 0) {
                    Log.d(TAG, "出现了异常图片的地址：cur.getString(photoPathIndex)=" + cur.getString(photoPathIndex));
                    Log.d(TAG, "出现了异常图片的地址：cur.getString(photoPathIndex).substring=" + cur.getString(photoPathIndex)
                            .substring(cur.getString(photoPathIndex).lastIndexOf("/") + 1, cur.getString(photoPathIndex).lastIndexOf(".")));
                } else {
                    String _id = cur.getString(photoIDIndex);
                    String path = cur.getString(photoPathIndex);
                    String bucketName = cur.getString(bucketDisplayNameIndex);
                    String bucketId = cur.getString(bucketIdIndex);
                    String displayName = cur.getString(displayNameIndex);
                    String fileSize = cur.getString(fileSizeIndex);
                    PhotoBucket bucket = mBucketList.get(bucketId);
                    if (bucket == null) {
                        bucket = new PhotoBucket();
                        mBucketList.put(bucketId, bucket);
                        bucket.photoList = new ArrayList<>();
                        bucket.bucketName = bucketName;
                    }
                    bucket.count++;
                    Photo imageItem = new Photo();
                    imageItem.setImageId(_id);
                    imageItem.setImagePath(path);
                    imageItem.setImageName(displayName);
                    imageItem.setImageSize(fileSize);
                    bucket.photoList.add(imageItem);
                }
            } while (cur.moveToNext());
        }
        cur.close();

        hasBuildImagesBucketList = true;
    }

    /**
     * 得到图片集
     *
     * @param refresh refresh or first build
     * @return
     */
    private ArrayList<PhotoBucket> getImagesBucketList(Boolean refresh) {
        if (refresh || !hasBuildImagesBucketList) {
            buildImagesBucketList();
        }
        ArrayList<PhotoBucket> tmpList = new ArrayList<>();
        for (Map.Entry<String, PhotoBucket> entry : mBucketList.entrySet()) {
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    public void setOnFinishedListener(OnFinishedListener onFinishedListener) {
        this.mOnFinishedListener = onFinishedListener;
    }

    public interface OnFinishedListener {
        void onFinshed(ArrayList<PhotoBucket> list);
    }

    @Override
    protected ArrayList<PhotoBucket> doInBackground(Boolean... params) {
        return getImagesBucketList(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<PhotoBucket> result) {
        super.onPostExecute(result);
        mOnFinishedListener.onFinshed(result);
    }

}
