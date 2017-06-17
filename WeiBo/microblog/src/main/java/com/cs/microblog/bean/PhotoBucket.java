package com.cs.microblog.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * 一个目录下的相册对象
 */
public class PhotoBucket implements Parcelable {
	public int count = 0;
	public String bucketName;
	public ArrayList<Photo> photoList;

	public PhotoBucket() {
		count = 0;
		bucketName = "";
		photoList = new ArrayList<>();
	}

	protected PhotoBucket(Parcel in) {
		count = in.readInt();
		bucketName = in.readString();
		photoList = in.createTypedArrayList(Photo.CREATOR);
	}

	public static final Creator<PhotoBucket> CREATOR = new Creator<PhotoBucket>() {
		@Override
		public PhotoBucket createFromParcel(Parcel in) {
			return new PhotoBucket(in);
		}

		@Override
		public PhotoBucket[] newArray(int size) {
			return new PhotoBucket[size];
		}
	};

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public ArrayList<Photo> getPhotoList() {
		return photoList;
	}
	public void setPhotoList(ArrayList<Photo> photoList) {
		this.photoList = photoList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(count);
		dest.writeString(bucketName);
		dest.writeTypedList(photoList);
	}
}
