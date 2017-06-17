package com.cs.microblog.bean;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/5/11.
 */

public class PictureInfo {

    /**
     * picture type
     * normal picture
     */
    public static final int  PICTURE_NOMAL= 0;
    /**
     * picture type
     * long picture
     */
    public static final int  PICTURE_LONG= 1;
    /**
     * picture type
     * gif picture
     */
    public static final int  PICTURE_GIF= 2;

    private int pictureWidth;
    private int pictureHeight;
    private int pictureType;
    private Bitmap bimapPicture;

    public PictureInfo(int pictureWidth, int pictureHeight, int pictureType, @Nullable Bitmap bimapPicture) {
        this.pictureWidth = pictureWidth;
        this.pictureHeight = pictureHeight;
        this.pictureType = pictureType;
        this.bimapPicture = bimapPicture;
    }

    public PictureInfo() {
    }

    //getter and setter
    public int getPictureWidth() {
        return pictureWidth;
    }

    public void setPictureWidth(int pictureWidth) {
        this.pictureWidth = pictureWidth;
    }

    public int getPictureHeight() {
        return pictureHeight;
    }

    public void setPictureHeight(int pictureHeight) {
        this.pictureHeight = pictureHeight;
    }

    public int getPictureType() {
        return pictureType;
    }

    public void setPictureType(int pictureType) {
        this.pictureType = pictureType;
    }

    public Bitmap getBimapPicture() {
        return bimapPicture;
    }

    public void setBimapPicture(Bitmap bimapPicture) {
        this.bimapPicture = bimapPicture;
    }
}